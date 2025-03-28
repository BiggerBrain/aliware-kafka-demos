package tool;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.errors.WakeupException;

/**
 * 本demo演示如何在一个进程内开启多个consumer同时消费topic
 * 注意全局consumer数量不要超过订阅的topic总分区数
 */
public class KafkaMultiConsumerDemo {

    public static void main(String args[]) throws InterruptedException {
        //设置sasl文件的路径,区分plain和scram
        JavaKafkaConfigurer.configureSaslPlain();

        //加载kafka.properties
        Properties kafkaProperties = JavaKafkaConfigurer.getKafkaProperties();

        Properties props = new Properties();
        //设置接入点，请通过控制台获取对应Topic的接入点
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getProperty("bootstrap.servers"));

        //接入协议，
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
        // scram 方式和plain方式区别
        props.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        //props.put(SaslConfigs.SASL_MECHANISM, "SCRAM-SHA-256");

        //可更加实际拉去数据和客户的版本等设置此值，默认30s
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 30000);
        //每次poll的最大数量
        //注意该值不要改得太大，如果poll太多数据，而不能在下次poll之前消费完，则会触发一次负载均衡，产生卡顿
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 30);
        //消息的反序列化方式
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        //当前消费实例所属的消费组，请在控制台申请之后填写
        //属于同一个组的消费实例，会负载消费消息
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getProperty("group.id"));

        int consumerNum = 2;
        Thread[] consumerThreads = new Thread[consumerNum];
        for (int i = 0; i < consumerNum; i++) {
            KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);

            List<String> subscribedTopics = new ArrayList<String>();
            subscribedTopics.add(kafkaProperties.getProperty("topic"));
            consumer.subscribe(subscribedTopics);

            KafkaConsumerRunner kafkaConsumerRunner = new KafkaConsumerRunner(consumer);
            consumerThreads[i] = new Thread(kafkaConsumerRunner);
        }

        for (int i = 0; i < consumerNum; i++) {
            consumerThreads[i].start();
        }

        for (int i = 0; i < consumerNum; i++) {
            consumerThreads[i].join();
        }
    }

    static class KafkaConsumerRunner implements Runnable {
        private final AtomicBoolean closed = new AtomicBoolean(false);
        private final KafkaConsumer consumer;

        KafkaConsumerRunner(KafkaConsumer consumer) {
            this.consumer = consumer;
        }

        @Override
        public void run() {
            try {
                while (!closed.get()) {
                    try {
                        ConsumerRecords<String, String> records = consumer.poll(1000);
                        //必须在下次poll之前消费完这些数据, 且总耗时不得超过SESSION_TIMEOUT_MS_CONFIG
                        for (ConsumerRecord<String, String> record : records) {
                            System.out.println(String.format("Thread:%s Consume partition:%d offset:%d", Thread.currentThread().getName(), record.partition(), record.offset()));
                        }
                    } catch (Exception e) {
                        try {
                            Thread.sleep(1000);
                        } catch (Throwable ignore) {

                        }
                        //参考常见报错: https://help.aliyun.com/document_detail/68168.html?spm=a2c4g.11186623.6.567.2OMgCB
                        e.printStackTrace();
                    }
                }
            } catch (WakeupException e) {
                // Ignore exception if closing
                if (!closed.get()) {
                    throw e;
                }
            } finally {
                consumer.close();
            }
        }

        // Shutdown hook which can be called from a separate thread
        public void shutdown() {
            closed.set(true);
            consumer.wakeup();
        }
    }
}

