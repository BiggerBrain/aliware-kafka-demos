package tool.kafka;

import org.apache.ckafka.common.KafkaException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Future;

public class TransactionKafkaProducerDemo {

    public static void main(String args[]) {
        //加载kafka.properties

        String BOOTSTRAP_SERVERS = "0.0.0.0:9092";
        Properties props = new Properties();
        //设置接入点，请通过控制台获取对应Topic的接入点
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);

        //Kafka消息的序列化方式
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        //请求的最长等待时间
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 30 * 1000);
        //设置客户端内部重试次数
        props.put(ProducerConfig.RETRIES_CONFIG, 5);
        props.put(ProducerConfig.ACKS_CONFIG, "-1");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        //设置客户端内部重试间隔
        props.put(ProducerConfig.RECONNECT_BACKOFF_MS_CONFIG, 3000);


        try {
            //批量获取 futures 可以加快速度, 但注意，批量不要太大
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                //构造Producer对象，注意，该对象是线程安全的，一般来说，一个进程内一个Producer对象即可；
                //如果想提高性能，可以多构造几个对象，但不要太多，最好不要超过5个
                props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG,"transaction_id_001"+i);
                KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);

                //构造一个Kafka消息
                String topic = "lsx1";
                String value = "this is the message's value"; //消息的内容

                producer.initTransactions();
                producer.beginTransaction();
                try {
                    //发送消息，并获得一个Future对象
                    ProducerRecord<String, String> kafkaMessage = new ProducerRecord<String, String>(topic, value + ": " + i);
                    Future<RecordMetadata> metadataFuture = producer.send(kafkaMessage);
                    producer.flush();
                    //同步获得Future对象的结果
                    try {
                        RecordMetadata recordMetadata = metadataFuture.get();
                        System.out.println("Produce ok:" + recordMetadata.toString());
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                    producer.commitTransaction();
                } catch (Exception e) {
                    // 其他异常可以中止事务
                    producer.abortTransaction();
                } finally {
                    producer.close();
                }
            }

        } catch (Exception e) {
            //客户端内部重试之后，仍然发送失败，业务要应对此类错误
            //参考常见报错: https://help.aliyun.com/document_detail/68168.html?spm=a2c4g.11186623.6.567.2OMgCB
            System.out.println("error occurred");
            e.printStackTrace();
        }


    }
}
