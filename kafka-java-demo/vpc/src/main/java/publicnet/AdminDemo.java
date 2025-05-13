package publicnet;

import org.apache.ckafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AlterConsumerGroupOffsetsResult;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.SaslConfigs;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class AdminDemo {
    public static void main(String[] args) {
        // Kafka 配置
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "150.109.162.241:50004");
        //如果客户指定ACL，则采用ACL连接，只支持SASL_PLAINTEXT
        String prefix = "org.apache.kafka.common.security.plain.PlainLoginModule";
        String jaasConfig = String.format("%s required username=\"%s\" password=\"%s\";", prefix, "ckafka-37wobnk3#admin", "12345678a");
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
        //  SASL_PLAINTEXT 公网接入
        props.put(SaslConfigs.SASL_JAAS_CONFIG, jaasConfig);
        //  SASL 采用 Plain 方式。
        props.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        // 创建 AdminClient
        try (AdminClient adminClient = AdminClient.create(props)) {
            // 定义消费者组 ID
            String consumerGroupId = "example-group-admin";

            // 定义要修改的主题分区和新的位点
            TopicPartition topicPartition = new TopicPartition("source", 0);
            OffsetAndMetadata newOffset = new OffsetAndMetadata(100L, "no metadata");

            // 创建一个 Map 来存储要修改的位点
            Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();
            offsets.put(topicPartition, newOffset);

            // 调用 alterConsumerGroupOffsets 方法
            AlterConsumerGroupOffsetsResult result = adminClient.alterConsumerGroupOffsets(consumerGroupId, offsets);

            // 等待操作完成
            try {
                result.all().get();
                System.out.println("Offsets successfully altered.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
