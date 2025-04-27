package tool.kafka;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaAdminDeleteAllTopics {
    public static void main(String[] args) {
        System.out.println("接入点:" + args[0]);
        Properties props = new Properties();
        //设置接入点，请通过控制台获取对应Topic的接入点
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, args[0]);
        try (AdminClient adminClient = KafkaAdminClient.create(props)) {
            ListTopicsResult listTopicsResult = adminClient.listTopics();
            Collection<TopicListing> topicListings = listTopicsResult.listings().get();
            for (TopicListing topicListing : topicListings) {
                System.out.println("获取topic:" + topicListing.name() + ",是否内部" + topicListing.isInternal());
            }
            Collection<String> deleteTopics = new ArrayList<>();
            for (TopicListing topicListing : topicListings) {
                if (!topicListing.name().startsWith("__")) {
                    System.out.println("删除topic：" + topicListing.name());
                    deleteTopics.add(topicListing.name());
                }
            }
            DeleteTopicsResult deleteTopicsResult = adminClient.deleteTopics(deleteTopics);
            deleteTopicsResult.all().get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
