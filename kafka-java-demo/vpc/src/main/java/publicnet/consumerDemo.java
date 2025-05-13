package publicnet;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.SaslConfigs;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class consumerDemo {
    public static void main(String[] args) {
        // Kafka 配置
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "150.109.162.241:50004");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "example-group-admin");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true"); // 自动提交位点
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000"); // 自动提交间隔
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // 自动提交间隔
        //如果客户指定ACL，则采用ACL连接，只支持SASL_PLAINTEXT
        String prefix = "org.apache.kafka.common.security.plain.PlainLoginModule";
        String jaasConfig = String.format("%s required username=\"%s\" password=\"%s\";", prefix, "ckafka-37wobnk3#admin", "12345678a");
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
        //  SASL_PLAINTEXT 公网接入
        props.put(SaslConfigs.SASL_JAAS_CONFIG, jaasConfig);
        //  SASL 采用 Plain 方式。
        props.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        // 创建 Kafka 消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        // 手动分配分区
        String topic = "source";
        TopicPartition partition = new TopicPartition(topic, 0);
        TopicPartition partition1 = new TopicPartition(topic, 1);
        consumer.assign(Collections.singletonList(partition));

        try {
            while (true) {
                // 拉取消息
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                if (records.isEmpty()) {
                    System.out.println("empty");
                }else{
                    for (ConsumerRecord<String, String> record : records){
                        System.out.printf("Consumed message: key = %s, value = %s, offset = %d, partition = %d%n",
                                record.key(), record.value(), record.offset(), record.partition());
                    }

                }

            }
        } finally {
            consumer.close();
        }    }
}
