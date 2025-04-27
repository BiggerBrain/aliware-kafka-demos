package tool;

import org.apache.ckafka.common.Node;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.common.config.SaslConfigs;
import tool.connect.CrossNetUtil;

import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class SaSl {
    public static void main(String[] args) {
        String brokerAddress = args[0];
        Properties props = new Properties();
        //设置接入点，请通过控制台获取对应Topic的接入点。
        props.put("bootstrap.servers", brokerAddress);
        String prefix = "org.apache.kafka.common.security.plain.PlainLoginModule";
        String username = "admin";
        String password = "admin";
        String jaasConfig = String.format("%s required username=\"%s\" password=\"%s\";", prefix, username, password);
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
        //  SASL_PLAINTEXT 公网接入
        props.put(SaslConfigs.SASL_JAAS_CONFIG, jaasConfig);
        //  SASL 采用 Plain 方式。
        props.put(SaslConfigs.SASL_MECHANISM, "PLAIN");

        org.apache.ckafka.clients.admin.AdminClient adminClient = org.apache.ckafka.clients.admin.AdminClient.create(props, null);
        try {
            org.apache.ckafka.clients.admin.DescribeClusterResult describeClusterResult = adminClient.describeCluster();
            Collection<Node> nodes = describeClusterResult.nodes().get(10, TimeUnit.SECONDS);
            for (org.apache.ckafka.common.Node node : nodes) {
                System.out.println("node:" + node);
            }
//            //目前只需要非内置topic
//            ListTopicsOptions options = new ListTopicsOptions();
//            options.listInternal(false);
//            ListTopicsResult listTopicsResult = adminClient.listTopics(options);
//            try {
//                listTopicsResult.names().get(3, TimeUnit.SECONDS);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            listTopicsResult = adminClient.listTopics(options);
//            listTopicsResult.names().get(10, TimeUnit.SECONDS);
//            describeClusterResult = adminClient.describeCluster();
//            nodes = describeClusterResult.nodes().get();

        } catch (Exception e) {
            //可忽略
            e.printStackTrace();
        }
//        for (Node node : nodes) {
//            System.out.println("node----:" + node);
//        }
        for (org.apache.ckafka.common.Node node : adminClient.nodes()) {
            System.out.println("admin node:" + node);
        }
        CrossNetUtil.CrossClusterInfo cluster = null;
//        try {
//            Class<?> clazz = KafkaAdminClient.class;
//            // 获取指定字段
//            Field metadataManagerField = clazz.getDeclaredField("metadataManager");
//            // 如果字段是私有的，我们需要调用setAccessible(true)方法
//            metadataManagerField.setAccessible(true);
//            AdminMetadataManager metadataManager = (AdminMetadataManager) metadataManagerField.get(adminClient);
//            Class<?> metadataManagerClass = AdminMetadataManager.class;
//            // 获取指定字段
//            Field updaterField = metadataManagerClass.getDeclaredField("updater");
//            // 如果字段是私有的，我们需要调用setAccessible(true)方法
//            updaterField.setAccessible(true);
//            AdminMetadataManager.AdminMetadataUpdater updater = (AdminMetadataManager.AdminMetadataUpdater) updaterField.get(metadataManager);
//            cluster = new CrossClusterInfo();
//            cluster.setNodes(updater.fetchNodes());
//            cluster.setController(metadataManager.controller());
//            System.out.println("CrossClusterInfo:" + cluster);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (adminClient != null) {
//                adminClient.close();
//            }
//        }
    }
}
