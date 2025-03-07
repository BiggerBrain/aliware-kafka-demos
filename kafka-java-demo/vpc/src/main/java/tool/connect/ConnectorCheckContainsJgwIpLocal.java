package tool.connect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * java -cp *:kafka-vpc-demo.jar tool.connect.ConnectorCheckContainsJgwIpLocal
 * tkex-login -cls cls-5vzjheeo -n ns-prjm6pcb-1541281-production -p connect-qy-0 -c connect-qy -b /bin/bash
 */
public class ConnectorCheckContainsJgwIpLocal {
    public static class ConnectorClusterInfo {
        public String region;
        public String ip;

        public ConnectorClusterInfo(String region, String ip) {
            this.region = region;
            this.ip = ip;

        }

        @Override
        public String toString() {
            return "ConnectorClusterInfo{" +
                    "region='" + region + '\'' +
                    ", ip='" + ip + '\'' +
                    '}';
        }
    }

    public static String execCmdParts(String[] cmdParts) {
        ProcessBuilder process = new ProcessBuilder(cmdParts);
        Process p;
        try {
            p = process.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }

            return builder.toString();
        } catch (IOException e) {
            System.out.print("error");
            e.printStackTrace();
        }

        return null;
    }

    private static String execCmd(String command) {
        StringBuilder output = new StringBuilder();
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();
    }

    // 格式化JSON字符串
    public static String formatJson(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        JsonNode jsonNode = mapper.readTree(jsonString);
        return mapper.writeValueAsString(jsonNode);
    }

    // 比较两个JSON对象并输出差异
    public static void compareJson(JsonNode json1, JsonNode json2, String parentKey) {
        Iterator<Map.Entry<String, JsonNode>> fields = json1.fields();

        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String key = field.getKey();
            JsonNode value1 = field.getValue();
            JsonNode value2 = json2.get(key);

            if (value2 == null) {
                System.out.println("Key " + (parentKey.isEmpty() ? key : parentKey + "." + key) + " is missing in the second JSON.");
            } else if (!value1.equals(value2)) {
                if (value1.isObject() && value2.isObject()) {
                    compareJson(value1, value2, parentKey.isEmpty() ? key : parentKey + "." + key);
                } else {
                    System.out.println("Value mismatch at key " + (parentKey.isEmpty() ? key : parentKey + "." + key) + ": " + value1 + " vs " + value2);
                }
            }
        }

        fields = json2.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String key = field.getKey();
            if (!json1.has(key)) {
                System.out.println("Key " + (parentKey.isEmpty() ? key : parentKey + "." + key) + " is missing in the first JSON.");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        List<ConnectorClusterInfo> clusterInfoList = new ArrayList<ConnectorClusterInfo>();

        clusterInfoList.add(new ConnectorClusterInfo("local", "127.0.0.1"));

        for (ConnectorClusterInfo connectorClusterInfo : clusterInfoList) {
            // http://11.142.172.114:8083/connectors
            //String connectors = tool.util.HttpUtil.get("http://" + connectorClusterInfo.ip + ":8083/connectors");

            String connectors = execCmd("curl -X GET -H \"Content-Type: application/json\"" + " http://" + connectorClusterInfo.ip + ":8083/connectors");

            ObjectMapper jsonMapper = new ObjectMapper();
            jsonMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            jsonMapper.disable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);


            List<String> connectorsList = jsonMapper.readValue(connectors, new TypeReference<ArrayList<String>>() {
            }).stream().collect(Collectors.toSet()).stream().collect(Collectors.toList());

            System.out.println(connectorClusterInfo);
            System.out.println("connectorsList:" + connectorsList.size());
            connectorsList.stream().sorted();
            for (int i = 0; i < connectorsList.size(); i++) {
                String connector = connectorsList.get(i);
                if (connector.equals("connector-yd4e5ray")) {
                    System.out.println("获取配置:" + connector);
                    String sourceJson = execCmd("curl -X GET -H \"Content-Type: application/json\"" + " http://" + connectorClusterInfo.ip + ":8083/connectors/" + connector + "/config");
                    boolean contains = oldJnsOperation(sourceJson);
                    if (contains) {
                        return;
                    }
                }
            }
        }
    }

    private static boolean oldJnsOperation(String sourceJson) throws IOException {
        ObjectMapper jsonMapper = new ObjectMapper();
        jsonMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        jsonMapper.disable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
        Connector target = jsonMapper.readValue(sourceJson, new TypeReference<Connector>() {
        });
        if (target != null && target.config != null) {
            for (Map.Entry<String, String> entry : target.config.entrySet()) {
                if (entry.getValue().contains("100.78.98.45:10558")) {
                    String key = entry.getKey();
                    target.config.put(key, entry.getValue().replace("100.78.98.45:10558", "30.46.103.13:13186"));
                }
            }
        }

        System.out.println("老配置");
        String oldJsonFormat = formatJson(sourceJson);
        System.out.println(oldJsonFormat);
        System.out.println("新配置");
        String newFormatJson = formatJson(jsonMapper.writeValueAsString(target));
        System.out.println();

        // 比较JSON字符串并输出差异
        ObjectMapper mapper = new ObjectMapper();
        JsonNode old = mapper.readTree(oldJsonFormat);
        JsonNode newJson = mapper.readTree(newFormatJson);

        System.out.println("Differences:");
        compareJson(old, newJson, "");


        return false;
    }

    public static void test() throws JsonProcessingException {
        String json = "{\"name\":\"connector-y9nn95w4\",\"config\":{\"connector.class\":\"io.debezium.connector.mysql.MySqlConnector\",\"tasks.max\":\"1\",\"database.history.kafka.topic\":\"mysql-task-y9j5xm5b\",\"transforms\":\"Reroute\",\"dest.broker\":\"9.139.8.227:6004\",\"include.schema.changes\":\"false\",\"transforms.Reroute.topic.replacement\":\"topic-worker-verb-attachement\",\"decimal.handling.mode\":\"double\",\"database.history.kafka.recovery.poll.interval.ms\":\"5000\",\"producer.compression.type\":\"snappy\",\"value.converter\":\"org.apache.kafka.connect.json.JsonConverter\",\"key.converter\":\"org.apache.kafka.connect.json.JsonConverter\",\"database.user\":\"root\",\"transforms.Reroute.type\":\"io.debezium.transforms.ByLogicalTableRouter\",\"database.server.id\":\"206353\",\"producer.max.request.size\":\"12582912\",\"database.history.kafka.bootstrap.servers\":\"11.135.14.110:12504\",\"producer.buffer.memory\":\"12582912\",\"event.deserialization.failure.handling.mode\":\"warn\",\"database.server.name\":\"task-y9j5xm5b\",\"transforms.Reroute.topic.regex\":\"(.*)\",\"database.port\":\"8888\",\"inconsistent.schema.handling.mode\":\"warn\",\"key.converter.schemas.enable\":\"true\",\"database.hostname\":\"11.145.102.68\",\"database.password\":\"LxzLqemodor2018\",\"value.converter.schemas.enable\":\"true\",\"name\":\"connector-y9nn95w4\",\"table.include.list\":\"attendance.t_worker_verb_attachement\",\"include.query\":\"false\",\"database.include.list\":\"attendance\",\"snapshot.mode\":\"schema_only\"},\"tasks\":[{\"connector\":\"connector-y9nn95w4\",\"task\":0}],\"type\":\"source\"}";
        ObjectMapper jsonMapper = new ObjectMapper();
        jsonMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        Connector connector = jsonMapper.readValue(json, new TypeReference<Connector>() {
        });
        System.out.println(connector.config);

    }

    public static class Connector {
        public String name;
        public Map<String, String> config;
    }
}
