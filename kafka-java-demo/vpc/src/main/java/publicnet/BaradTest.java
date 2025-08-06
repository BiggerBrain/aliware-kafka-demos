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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;

/**
 * java -cp *:kafka-vpc-demo-jar-with-dependencies.jar publicnet.BaradTest
 */
public class BaradTest {
    public static void sendBaradApiResponse(String instanceId, String ip, BaradApiRequest apiRequest, String region) {
        try {
            // 目标URL
            String url = "http://" + region + ".api.barad.tencentyun.com/metric/statisticsbatch";

            // 创建HTTP连接
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            // 将BaradApiResponse对象序列化为JSON字符串
            Gson gson = new Gson();
            String jsonInputString = gson.toJson(apiRequest);
            System.out.println("instanceId:" + instanceId + ":" + jsonInputString);

            // 发送请求
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // 获取响应
            int responseCode = connection.getResponseCode();
            System.out.println("Response" + region + " Code: " + responseCode);

            if (responseCode == 200) {
                // 读取响应内容
                String responseContent;
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    responseContent = br.lines().collect(Collectors.joining());
                }
                System.out.println(responseContent);
                BaradApiResponse response = new Gson().fromJson(responseContent, BaradApiResponse.class);

                // 提取points
                List<List<Integer>> points = response.getData().getPoints();
                System.out.println("Points: " + points);

            }
            // 关闭连接
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
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
                System.out.println("ID: " + region + ", instanceId: " + instanceId + ", instanceType: " + instanceType + ", ip: " + ip);
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
        int i = 0;
        instanceIpMap.entrySet().forEach(instanceEntry -> {
            String instanceId = instanceEntry.getKey();
            if (instanceId.equals("ckafka-bz4meaae")) {
                List<String> ipList = instanceEntry.getValue();
                String region = instanceRegionMap.get(instanceId);
                for (String ip : ipList) {
                    BaradApiRequest request = new BaradApiRequest();
                    request.getDimensions().add(new BaradApiRequest.Dimension(region, "observable_inner", ip));
                    sendBaradApiResponse(instanceId, ip, request, region);
                }

            }
        });
    }
}
