package tool;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.Config;
import org.apache.kafka.clients.admin.DescribeClientQuotasResult;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.apache.kafka.clients.admin.DescribeConfigsResult;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.TopicCollection;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.quota.ClientQuotaEntity;
import org.apache.kafka.common.quota.ClientQuotaFilter;

public class KafkaProducerDemo {

    public static void main(String args[]) {
        //设置sasl文件的路径
        JavaKafkaConfigurer.configureSasl();

        //加载kafka.properties
        Properties kafkaProperties =  JavaKafkaConfigurer.getKafkaProperties();

        Properties props = new Properties();
        //设置接入点，请通过控制台获取对应Topic的接入点
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getProperty("bootstrap.servers"));
        //设置SSL根证书的路径，请记得将XXX修改为自己的路径
        //与sasl路径类似，该文件也不能被打包到jar中
        props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, kafkaProperties.getProperty("ssl.truststore.location"));
        //根证书store的密码，保持不变
        props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "KafkaOnsClient");
        //接入协议，目前支持使用SASL_SSL协议接入
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");


        // 设置SASL账号
        String saslMechanism = kafkaProperties.getProperty("sasl.mechanism");
        String username = kafkaProperties.getProperty("sasl.username");
        String password = kafkaProperties.getProperty("sasl.password");
        if (!JavaKafkaConfigurer.isEmpty(username)
                && !JavaKafkaConfigurer.isEmpty(password)) {
            String prefix = "org.apache.kafka.common.security.scram.ScramLoginModule";
            if ("PLAIN".equalsIgnoreCase(saslMechanism)) {
                prefix = "org.apache.kafka.common.security.plain.PlainLoginModule";
            }
            String jaasConfig = String.format("%s required username=\"%s\" password=\"%s\";", prefix, username, password);
            props.put(SaslConfigs.SASL_JAAS_CONFIG, jaasConfig);
        }

        //SASL鉴权方式，保持不变
        props.put(SaslConfigs.SASL_MECHANISM, saslMechanism);
        //Kafka消息的序列化方式
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        //请求的最长等待时间
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 30 * 1000);
        //设置客户端内部重试次数
        props.put(ProducerConfig.RETRIES_CONFIG, 5);
        //设置客户端内部重试间隔
        props.put(ProducerConfig.RECONNECT_BACKOFF_MS_CONFIG, 3000);

        //hostname校验改成空
        props.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, "");

        System.out.println(props);
        try (org.apache.kafka.clients.admin.AdminClient adminClient = org.apache.kafka.clients.admin.AdminClient.create(props)) {
            //列表topic列表
            System.out.println("topic列表:");
            ListTopicsResult listTopicsResult = adminClient.listTopics(new ListTopicsOptions().listInternal(true));
            Collection<TopicListing> topicListings = listTopicsResult.listings().get();
            for (TopicListing topicListing : topicListings) {
                System.out.println(topicListing);
                DescribeTopicsResult describeTopicsResult = adminClient.describeTopics(TopicCollection.ofTopicNames(Arrays.asList(topicListing.name())));
                System.out.println(topicListing.name() + "分区配置:" + describeTopicsResult.all().get().entrySet());
                String topicName = topicListing.name();
                ConfigResource configResource = new ConfigResource(ConfigResource.Type.TOPIC, topicName);
                DescribeConfigsResult describeConfigsResult = adminClient.describeConfigs(Collections.singleton(configResource));

                // 获取配置信息
                Config configResult = describeConfigsResult.all().get().get(configResource);

                // 打印配置信息
                configResult.entries().forEach(configEntry -> {
                    System.out.println(topicListing.name() + "详细配置: " + configEntry.name() + ", Config Value: " + configEntry.value());
                });

            }


            // 定义 ClientQuotaEntity
            // 定义 ClientQuotaFilter
            System.out.println("集群配额");

            ClientQuotaFilter filter = ClientQuotaFilter.all();
//            contains(
//                    Arrays.asList(
//                            ClientQuotaFilterComponent.ofEntity(ClientQuotaEntity.CLIENT_ID, ".*"), // 替换为你的客户端ID
//                            ClientQuotaFilterComponent.ofEntity(ClientQuotaEntity.USER, ".*")
//                    )
//            );

            // 描述客户端配额
            DescribeClientQuotasResult describeClientQuotasResult = adminClient.describeClientQuotas(filter);

            // 获取配额信息
            Map<ClientQuotaEntity, Map<String, Double>> clientQuotaEntityMapMap = describeClientQuotasResult.entities().get();

            // 打印配额信息
            clientQuotaEntityMapMap.forEach((key, value) -> System.out.println("配额:" + key + ", Quota Value: " + value));
            System.out.println("集群描述:");
            DescribeClusterResult describeClusterResult = adminClient.describeCluster();
            System.out.println(describeClusterResult.clusterId());
            Collection<Node> nodes = describeClusterResult.nodes().get();
            for (Node node : nodes) {
                System.out.println("节点:" + node);
                int brokerId = node.id(); // 替换为你的 broker ID
                ConfigResource configResource = new ConfigResource(ConfigResource.Type.BROKER, String.valueOf(brokerId));

                // 描述 broker 配置
                DescribeConfigsResult describeConfigsResult = adminClient.describeConfigs(Collections.singleton(configResource));

                // 获取配置信息
                Config configResult = describeConfigsResult.all().get().get(configResource);

                // 打印配置信息
                configResult.entries().forEach(configEntry -> {
                    System.out.println("节点:" + node + "broker详细配置 Key: " + configEntry.name() + ", Config Value: " + configEntry.value());
                });
            }
            System.out.println("controller:");
            System.out.println(describeClusterResult.controller().get());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
