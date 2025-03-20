package tool.kafka;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.admin.internals.AdminMetadataManager;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.TopicCollection;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.acl.AclOperation;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.clients.Metadata;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.quota.ClientQuotaEntity;
import org.apache.kafka.common.quota.ClientQuotaFilter;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
// java -cp *:kafka-vpc-demo.jar tool.kafka.BrokerConfigCheckService  alikafka-serverless-cn-20s46i96l0d-1000-vpc.alikafka.aliyuncs.com:9092
public class BrokerConfigCheckService {
    public static void main(String[] args) {
        mainAliKafka(args);
    }

    public static void mainAliKafka(String[] args) {
        String boot = args[0];
        System.out.println(boot);
        Properties properties = new Properties();
        //设置接入点，请通过控制台获取对应Topic的接入点。
        properties.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, boot);
        properties.setProperty(CommonClientConfigs.CLIENT_ID_CONFIG, "PublicDestAdminClient#" + UUID.randomUUID());
        if (false) {
            //如果客户指定ACL，则采用ACL连接，只支持SASL_PLAINTEXT
            String prefix = "org.apache.kafka.common.security.plain.PlainLoginModule";
            String jaasConfig = String.format("%s required username=\"%s\" password=\"%s\";", prefix, "alikafka_serverless-cn-lf646i6s90c", "7FCqiJsazspPWJdoeTd1cUT5QGr1Jgn9");
            properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
            //  SASL_PLAINTEXT 公网接入
            properties.put(SaslConfigs.SASL_JAAS_CONFIG, jaasConfig);
            //  SASL 采用 Plain 方式。
            properties.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        }


        try (org.apache.kafka.clients.admin.AdminClient adminClient = org.apache.kafka.clients.admin.AdminClient.create(properties)) {
            //列表topic列表
            System.out.println("topic列表:");
            ListTopicsResult listTopicsResult = adminClient.listTopics(new ListTopicsOptions().listInternal(true));
            Collection<TopicListing> topicListings = listTopicsResult.listings().get();
            System.out.println("数量" + topicListings.size());
            for (TopicListing topicListing : topicListings) {
                System.out.println(topicListing.name() + "    " + topicListing.isInternal());
            }
            for (TopicListing topicListing : topicListings) {
                System.out.println(topicListing.name() + "信息" + topicListing);
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


            try {
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
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("集群描述:");
            DescribeClusterResult describeClusterResult = adminClient.describeCluster();
            System.out.println(describeClusterResult.clusterId());
            Collection<Node> nodes = describeClusterResult.nodes().get();
            System.out.println("节点总数:" + nodes.size());
            System.out.println("acl操作总数:");
            Set<AclOperation> aclOperations = describeClusterResult.authorizedOperations().get();
            if (aclOperations != null) {
                for (AclOperation aclOperation : aclOperations) {
                    System.out.println(aclOperation);
                }
            }
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

    public static void mainCkafka(String[] args) {
        Map<String, NewPartitions> newPartitions = new HashMap<>();
        Properties properties = new Properties();
        //设置接入点，请通过控制台获取对应Topic的接入点。
        properties.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "49.233.102.153:50004");
        properties.setProperty(CommonClientConfigs.CLIENT_ID_CONFIG, "PublicDestAdminClient#" + UUID.randomUUID());
        //如果客户指定ACL，则采用ACL连接，只支持SASL_PLAINTEXT
        String prefix = "org.apache.kafka.common.security.plain.PlainLoginModule";
        String jaasConfig = String.format("%s required username=\"%s\" password=\"%s\";", prefix, "ckafka-5k4kzzmv#admin", "12345678a");
        properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
        //  SASL_PLAINTEXT 公网接入
        properties.put(SaslConfigs.SASL_JAAS_CONFIG, jaasConfig);
        //  SASL 采用 Plain 方式。
        properties.put(SaslConfigs.SASL_MECHANISM, "PLAIN");

        try (org.apache.kafka.clients.admin.AdminClient adminClient = org.apache.kafka.clients.admin.AdminClient.create(properties)) {
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

    public static void mainT(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("接入点:" + args[0].trim());
        System.out.println("version:1.0");
        Properties props = new Properties();
        //设置接入点，请通过控制台获取对应Topic的接入点。
        props.put("bootstrap.servers", args[0].trim());
        //设置SASL_PLAINTEXT 接入
        initSaslPlainText(props);
        //消息队列Kafka版消息的序列化方式。
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        //请求的最长等待时间。
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 30 * 1000);
        //设置客户端内部重试次数。
        props.put(ProducerConfig.RETRIES_CONFIG, 5);
        props.put(ProducerConfig.ACKS_CONFIG, "1");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, false);
        //设置客户端内部重试间隔。
        props.put(ProducerConfig.RECONNECT_BACKOFF_MS_CONFIG, 3000);
        //构造消费对象，也即生成一个消费实例。
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer(props);
        ArrayList<Long> dateList = new ArrayList<>();
        dateList.add(System.currentTimeMillis() + 1000 * 60 * 60 * 24);
        dateList.add(System.currentTimeMillis());
        dateList.add(System.currentTimeMillis() - 1000 * 60 * 60 * 24);
        dateList.add(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 2);
        dateList.add(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 3);
        dateList.add(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 4);
        for (Long dateLong : dateList) {
            Map<TopicPartition, Long> timestampsToSearch = new HashMap<TopicPartition, Long>();
            timestampsToSearch.put(new TopicPartition("U_TOPIC_z8300009194", 4), dateLong);
            Map<TopicPartition, OffsetAndTimestamp> topicPartitionOffsetAndTimestampMap =
                    consumer.offsetsForTimes(timestampsToSearch);
            System.out.printf("topicPartitionOffsetAndTimestampMap:" + new Date(dateLong) + ":" + topicPartitionOffsetAndTimestampMap);
        }
//        timestampsToSearch.put(new TopicPartition("U_TOPIC_z8300009194",4), Date.UTC(2024,8,9,1,1,1));
//        timestampsToSearch.put(new TopicPartition("U_TOPIC_z8300009194",4), Date.UTC(2024,8,8,1,1,1));
//        timestampsToSearch.put(new TopicPartition("U_TOPIC_z8300009194",4), Date.UTC(2024,8,7,1,1,1));
//        timestampsToSearch.put(new TopicPartition("U_TOPIC_z8300009194",4), Date.UTC(2024,8,6,1,1,1));

        // System.out.println("============================================");
        //展示Topic列表
        //producerCheck(props);
        //adminCheck(props);
    }

    private static void producerCheck(Properties props) {
        try {
            //构造一个消息队列Kafka版消息。
            String topic = "test"; //消息所属的Topic，请在控制台申请之后，填写在这里。
            String value = "this is kafka msg value"; //消息的内容。
            //构造Producer对象，注意，该对象是线程安全的，一般来说，一个进程内一个Producer对象即可。
            KafkaProducer<String, String> producer = new KafkaProducer(props);
            //批量获取Future对象可以加快速度。但注意，批量不要太大。
            for (int i = 0; i < 10; i++) {
                //发送消息，并获得一个Future对象。
                ProducerRecord<String, String> kafkaMessage = new ProducerRecord(topic, value + ": " + i);
                Future<RecordMetadata> metadataFuture = producer.send(kafkaMessage);
                //同步获得Future对象的结果。
                RecordMetadata recordMetadata = metadataFuture.get(3, TimeUnit.SECONDS);
                System.out.println("写入OK:" + recordMetadata.toString());
            }
            producer.close();
            System.out.println("============================================");
            System.out.println();

            System.out.println("获取元数据");
            try {
                Class<?> clazz = KafkaProducer.class;
                // 获取指定字段
                Field field = clazz.getDeclaredField("metadata");
                // 如果字段是私有的，我们需要调用setAccessible(true)方法
                field.setAccessible(true);
                Metadata metadata = (Metadata) field.get(producer);
                System.out.println("元数据controller: " + metadata.fetch().controller().toString());
                System.out.println("元数据: " + metadata.fetch().toString());
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            System.out.println("============================================");
            System.out.println();
        } catch (Exception e) {
            //客户端内部重试之后，仍然发送失败，业务要应对此类错误。
            e.printStackTrace();
        }
    }

    private static void adminCheck(Properties props) {
        System.out.println("AdminClient检测");
        System.out.println("AdminClient检测");
        System.out.println("AdminClient检测");
        System.out.println("AdminClient检测");

        AdminClient adminClient = AdminClient.create(props);
//        DescribeClusterResult describeClusterResult = adminClient.describeCluster();
//        System.out.println("controller:"+describeClusterResult.controller());
//        for (Node node : describeClusterResult.nodes().get()) {
//            System.out.println("Node:" + node);
//        }
        try {
            //目前只需要非内置topic
            ListTopicsOptions options = new ListTopicsOptions();
            options.listInternal(false);
            ListTopicsResult listTopicsResult = adminClient.listTopics(options);
            try {
                listTopicsResult.names().get(1, TimeUnit.SECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Class<?> clazz = KafkaAdminClient.class;
                // 获取指定字段
                Field metadataManagerField = clazz.getDeclaredField("metadataManager");
                // 如果字段是私有的，我们需要调用setAccessible(true)方法
                metadataManagerField.setAccessible(true);
                AdminMetadataManager metadataManager = (AdminMetadataManager) metadataManagerField.get(adminClient);

                Class<?> metadataManagerClass = AdminMetadataManager.class;

                // 获取指定字段
                Field updaterField = metadataManagerClass.getDeclaredField("updater");
                // 如果字段是私有的，我们需要调用setAccessible(true)方法
                updaterField.setAccessible(true);
                AdminMetadataManager.AdminMetadataUpdater updater = (AdminMetadataManager.AdminMetadataUpdater) updaterField.get(metadataManager);


                System.out.println("元数据controller: " + metadataManager.controller().toString());
                System.out.println("元数据: " + updater.fetchNodes().toString());
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

            System.out.println("adminClient.listTopics:" + Arrays.asList(listTopicsResult.listings().get()));
            System.out.println("============================================");
            System.out.println();
            if (listTopicsResult != null
                    && listTopicsResult.names() != null
                    && !listTopicsResult.names().get().isEmpty()) {
                Collection<ConfigResource> resources = new ArrayList();
                for (String name : listTopicsResult.names().get()) {
                    ConfigResource configResource = new ConfigResource(ConfigResource.Type.TOPIC, name);
                    resources.add(configResource);
                }
                DescribeConfigsResult describeConfigsResult = adminClient.describeConfigs(resources);
                System.out.println("adminClient.describeConfigs:" + describeConfigsResult.all().get().toString());
                System.out.println("============================================");
                System.out.println();
            }
            ListConsumerGroupsResult listConsumerGroupsResult = adminClient.listConsumerGroups();
            System.out.println("adminClient.listConsumerGroups:" + listConsumerGroupsResult.all().get());
            System.out.println("============================================");
            System.out.println();
            for (ConsumerGroupListing consumerGroupListing : listConsumerGroupsResult.all().get()) {
                DescribeConsumerGroupsResult describeConsumerGroupsResult = adminClient.describeConsumerGroups(Collections.singletonList(consumerGroupListing.groupId()));
                System.out.println(" adminClient.describeConsumerGroups:" + describeConsumerGroupsResult.all().get());
                System.out.println("============================================");
                System.out.println();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initSaslPlainText(Properties props) {
        if (false) {
            String prefix = "org.apache.kafka.common.security.plain.PlainLoginModule";
            String username = "ckafka-7kdkjk3d#test12345";
            String password = "test12345";
            String jaasConfig = String.format("%s required username=\"%s\" password=\"%s\";", prefix, username, password);
            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
            //  SASL_PLAINTEXT 公网接入
            props.put(SaslConfigs.SASL_JAAS_CONFIG, jaasConfig);
            //  SASL 采用 Plain 方式。
            props.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        }
    }
}