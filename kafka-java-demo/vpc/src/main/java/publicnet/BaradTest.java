package publicnet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.google.gson.Gson;

/**
 * java -cp *:kafka-vpc-demo-jar-with-dependencies.jar publicnet.BaradTest
 */
public class BaradTest {

    static ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, TreeSet<String>>>> opMap = new ConcurrentHashMap<>();
    private static final ExecutorService executorService = Executors.newFixedThreadPool(60000);
    static List<Future<?>> futures = new ArrayList<>();
    public static void sendBaradApiResponse(String instanceId, String ip, BaradRequest apiRequest, String region, String op) {
        HttpURLConnection connection = null;
        try {
            // 目标URL
            String url = "http://" + region + ".api.barad.tencentyun.com/metric/statisticsbatch";

            // 创建HTTP连接
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            // 将BaradApiResponse对象序列化为JSON字符串
            Gson gson = new Gson();
            String jsonInputString = gson.toJson(apiRequest);
            //System.out.println("instanceId:" + instanceId + ":" + ip + jsonInputString);

            // 发送请求
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // 获取响应
            int responseCode = connection.getResponseCode();
            System.out.println("Response:" + instanceId + ":" + region + " Code: " + responseCode);

            if (responseCode == 200) {
                // 读取响应内容
                String responseContent;
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    responseContent = br.lines().collect(Collectors.joining());
                }
                //System.out.println(responseContent);
                BaradApiResponse response = new Gson().fromJson(responseContent, BaradApiResponse.class);

                // 提取points
                List<List<Double>> points = response.getData().getPoints();
                for (List<Double> point : points) {
                    for (Double value : point) {
                        if (value != null && value > 0) {
                            System.out.println(instanceId + ":" + ip + ":" + op + ":" + value);
                            opMap.computeIfAbsent(instanceId, (k) -> new ConcurrentHashMap<>())
                                    .computeIfAbsent(op, (k) -> new ConcurrentHashMap<>())
                                    .computeIfAbsent(ip, (k) -> new TreeSet<>()).add(String.valueOf(value));
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            // 关闭连接
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 数据库连接信息:mysql -h 11.180.146.140 -P3306 -uroot -p'aIEFdkf*b0239_354'
        //select * from instance_broker_detail limit 10;
        String url = "jdbc:mysql://11.180.146.140:3306/ckafka"; // 替换为你的数据库地址和名称
        String username = "root"; // 替换为你的数据库用户名
        String password = "aIEFdkf*b0239_354"; // 替换为你的数据库密码

        // 声明 JDBC 连接对象
        Connection connection = null;

        Map<String, List<String>> instanceIpMap = new HashMap<>();
        Map<String, String> instanceRegionMap = new HashMap<>();
        try {
            // 1. 加载 MySQL 驱动程序（可选，现代 JDBC 不需要显式加载）
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. 创建数据库连接
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("成功连接到数据库！");

            // 3. 创建 SQL 查询
            String sql = " select region,instance_id,instance_type,ip from instance_broker_detail where instance_status = 1"; // 替换为你的表名
            PreparedStatement statement = connection.prepareStatement(sql);

            // 4. 执行查询并获取结果
            ResultSet resultSet = statement.executeQuery();

            // 5. 遍历结果集
            while (resultSet.next()) {
                String region = resultSet.getString("region"); // 替换为你的列名
                String instanceId = resultSet.getString("instance_id"); // 替换为你的列名
                String instanceType = resultSet.getString("instance_type"); // 替换为你的列名
                String ip = resultSet.getString("ip"); // 替换为你的列名
                //  System.out.println("ID: " + region + ", instanceId: " + instanceId + ", instanceType: " + instanceType + ", ip: " + ip);
                instanceIpMap.computeIfAbsent(instanceId, k -> new ArrayList<>()).add(ip);
                instanceRegionMap.put(instanceId, region);
            }

            // 6. 关闭结果集和语句
            resultSet.close();
            statement.close();
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL 驱动未找到，请检查依赖！");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("数据库连接失败！");
            e.printStackTrace();
        } finally {
            // 7. 关闭数据库连接
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("数据库连接已关闭！");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("instanceIpMap: " + instanceIpMap.size());
        System.out.println("instanceRegionMap: " + instanceRegionMap.size());
        List<String> ops = Arrays.asList("alter_configs_error", "delete_topic_serror", "create_acls_error", "incremental_alterconfigs_error");
        AtomicInteger i = new AtomicInteger();
        instanceIpMap.forEach((instanceId, ipList) -> {
            System.out.println(i.getAndIncrement());
            String region = instanceRegionMap.get(instanceId);
            for (String ip : ipList) {
                for (String op : ops) {
                    Future<?> observableInner = executorService.submit(() -> {
                        int number = i.get();
                        BaradRequest request = new BaradRequest(op);
                        request.getDimensions().add(new BaradRequest.Dimension(region, "observable_inner", ip));
                        sendBaradApiResponse(instanceId, ip, request, region, op);
                        System.out.println(number + "执行完");
                    });
                    futures.add(observableInner);
                }
            }
        });
        for (Future<?> future : futures) {
            future.get(); // 等待每个任务完成
        }
        executorService.shutdown(); // 启动一次顺序关闭，执行以前提交的任务，但不接受新任务。
        try {
            // 请求关闭、发生超时或者当前线程中断，无论哪一个首先发生之后，都将导致阻塞，直到所有任务完成执行
            // 设置最长等待10秒
            executorService.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("高危操作实例数量:" + opMap.size());
        opMap.entrySet().stream().forEach(entry -> {
            String instanceId = entry.getKey();
            ConcurrentHashMap<String, ConcurrentHashMap<String, TreeSet<String>>> opsMap = entry.getValue();
            opsMap.entrySet().stream().forEach(opsEntry -> {
                opsEntry.getValue().entrySet().stream().forEach(ipEntry -> {
                    System.out.println("高危instanceId:" + instanceId + ",高危操作:" + opsEntry.getKey() + ",ip:" + ipEntry.getKey() + ",高危操作监控:" + ipEntry.getValue().stream().findFirst());
                });
            });
        });
    }
}
