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

public class ConnectorCheck {
    public static class ConnectorClusterInfo {
        public String region;
        public String ip;
        public String kafkaIp;

        public ConnectorClusterInfo(String region, String ip, String kafkaIp) {
            this.region = region;
            this.ip = ip;
            this.kafkaIp = kafkaIp;
        }

        @Override
        public String toString() {
            return "ConnectorClusterInfo{" +
                    "region='" + region + '\'' +
                    ", ip='" + ip + '\'' +
                    ", kafkaIp='" + kafkaIp + '\'' +
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

    static String[] cmdParts1 = {"curl", "-H", "Host: www.chineseconverter.com", "-H", "Cache-Control: max-age=0", "--compressed", "https://www.chineseconverter.com/zh-cn/convert/chinese-stroke-order-tool"};
    static String[] cmdParts2 = {"curl", "-H", "Cache-Control: max-age=0", "--compressed", "https://www.chineseconverter.com/zh-cn/convert/chinese-stroke-order-tool"};


    private static void main(String[] args) throws Exception {
        List<ConnectorClusterInfo> clusterInfoList = new ArrayList<ConnectorClusterInfo>();
//        clusterInfoList.add(new ConnectorClusterInfo("gz2","11.142.172.114","11.135.14.110:11456"));
//        clusterInfoList.add(new ConnectorClusterInfo("gz5","30.45.12.101","11.135.14.141:16056"));
//        clusterInfoList.add(new ConnectorClusterInfo("gz7","11.139.118.61","11.135.14.110:12504"));


//        clusterInfoList.add(new ConnectorClusterInfo("bj2","30.164.219.106","9.144.41.135:6232"));
//        clusterInfoList.add(new ConnectorClusterInfo("bj3","9.147.192.204","9.164.21.40:12277"));
//        clusterInfoList.add(new ConnectorClusterInfo("bj4","9.147.196.13","9.144.92.60:6146"));
//        clusterInfoList.add(new ConnectorClusterInfo("bj1","9.144.115.193","9.144.32.56:10887"));
        clusterInfoList.add(new ConnectorClusterInfo("sh1", "9.34.58.134", "100.104.231.55:6002"));
        clusterInfoList.add(new ConnectorClusterInfo("sh3", "11.185.11.167", "9.143.196.158:11072"));
        clusterInfoList.add(new ConnectorClusterInfo("sh4", "30.160.71.65", "9.142.169.25:6336"));
        clusterInfoList.add(new ConnectorClusterInfo("sh5", "30.44.62.50", "9.142.170.174:6542"));
        clusterInfoList.add(new ConnectorClusterInfo("sh6", "11.147.106.166", "11.167.110.7:6943"));
        clusterInfoList.add(new ConnectorClusterInfo("sh7", "21.12.19.250", "9.142.170.174:7675"));
        clusterInfoList.add(new ConnectorClusterInfo("shjr", "11.185.205.104", "11.185.201.31:6004"));
        clusterInfoList.add(new ConnectorClusterInfo("sg1", "11.168.95.64", "11.186.191.226:8616"));
        clusterInfoList.add(new ConnectorClusterInfo("sg2", "21.30.116.33", "11.186.191.154:6357"));
        clusterInfoList.add(new ConnectorClusterInfo("use1", "30.165.5.64", "9.131.195.100:15120"));
        clusterInfoList.add(new ConnectorClusterInfo("use2", "21.11.108.216", "9.208.161.175:6144"));


        for (ConnectorClusterInfo connectorClusterInfo : clusterInfoList) {
            // http://11.142.172.114:8083/connectors
            //String connectors = tool.util.HttpUtil.get("http://" + connectorClusterInfo.ip + ":8083/connectors");

            String connectors = execCmd("curl -X GET -H \"Content-Type: application/json\"" + " http://" + connectorClusterInfo.ip + ":8083/connectors");


            ObjectMapper jsonMapper = new ObjectMapper();
            jsonMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            jsonMapper.disable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);


            ArrayList<String> connectorsList = jsonMapper.readValue(connectors, new TypeReference<ArrayList<String>>() {
            });

            System.out.println(connectorClusterInfo);
            System.out.println("connectorsList:" + connectorsList.size());
            System.out.println("整理表格:");
            for (String connector : connectorsList) {
                String connectorConfigJson = execCmd("curl -X GET -H \"Content-Type: application/json\"" + " http://" + connectorClusterInfo.ip + ":8083/connectors/" + connector);
                Connector connectorObj = jsonMapper.readValue(connectorConfigJson, new TypeReference<Connector>() {
                });
                if (connectorObj != null && connectorObj.config != null && connectorObj.config.containsKey("database.history.kafka.bootstrap.servers")) {
                    if (!connectorObj.config.get("database.history.kafka.bootstrap.servers").equals(connectorClusterInfo.kafkaIp)) {
                        System.out.println(connectorClusterInfo.region + "\t" + connector + "\t" + connectorObj.config.get("database.history.kafka.bootstrap.servers") + "\t" + connectorClusterInfo.kafkaIp);
                    }
                }

                Thread.sleep(500);
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
