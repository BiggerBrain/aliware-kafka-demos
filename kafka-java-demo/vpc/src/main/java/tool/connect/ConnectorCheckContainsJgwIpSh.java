package tool.connect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * java -cp *:kafka-vpc-demo.jar tool.connect.ConnectorCheckContainsJgwIpSh
 */
public class ConnectorCheckContainsJgwIpSh {
    public static class ConnectorClusterInfo {
        public String region;
        public String ip;
        public String jnsOldIp;

        public ConnectorClusterInfo(String region, String ip, String jnsOldIp) {
            this.region = region;
            this.ip = ip;
            this.jnsOldIp = jnsOldIp;
        }

        @Override
        public String toString() {
            return "ConnectorClusterInfo{" +
                    "region='" + region + '\'' +
                    ", ip='" + ip + '\'' +
                    ", jnsOldIp='" + jnsOldIp + '\'' +
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


    public static void main(String[] args) throws Exception {
        List<ConnectorClusterInfo> clusterInfoList = new ArrayList<ConnectorClusterInfo>();
//        clusterInfoList.add(new ConnectorClusterInfo("gz2","11.142.172.114","11.135.14.110:11456"));
//        clusterInfoList.add(new ConnectorClusterInfo("gz5","30.45.12.101","11.135.14.141:16056"));
//        clusterInfoList.add(new ConnectorClusterInfo("gz7","11.139.118.61","11.135.14.110:12504"));


//        clusterInfoList.add(new ConnectorClusterInfo("bj2","30.164.219.106","9.144.41.135:6232"));
//        clusterInfoList.add(new ConnectorClusterInfo("bj3","9.147.192.204","9.164.21.40:12277"));
//        clusterInfoList.add(new ConnectorClusterInfo("bj4","9.147.196.13","9.144.92.60:6146"));
//        clusterInfoList.add(new ConnectorClusterInfo("bj1","9.144.115.193","9.144.32.56:10887"));

        clusterInfoList.add(new ConnectorClusterInfo("sh7", "30.44.62.51", "9.18.96.147"));
        clusterInfoList.add(new ConnectorClusterInfo("sh7", "30.44.62.50", "9.18.96.147"));
        clusterInfoList.add(new ConnectorClusterInfo("sh7", "30.170.13.115", "9.18.96.147"));

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
            System.out.println("整理表格:");
            connectorsList.stream().sorted();
            for (int i = 0; i < connectorsList.size(); i++) {
                String connector = connectorsList.get(i);
//                System.out.print(i + " ");
                boolean has = false;
                String connectorConfigJson = execCmd("curl -X GET -H \"Content-Type: application/json\"" + " http://" + connectorClusterInfo.ip + ":8083/connectors/" + connector);
                Connector connectorObj = jsonMapper.readValue(connectorConfigJson, new TypeReference<Connector>() {
                });
                if (connectorObj != null && connectorObj.config != null) {
                    for (String value : connectorObj.config.values()) {
                        if (value.contains(connectorClusterInfo.jnsOldIp)) {
                            System.out.println(connector);
                            has = true;
                            break;
                        }
                    }
                }
                if (!has) {
//                    System.out.print(connector + " " + "pass");
                }
//                System.out.println();
            }
        }
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
