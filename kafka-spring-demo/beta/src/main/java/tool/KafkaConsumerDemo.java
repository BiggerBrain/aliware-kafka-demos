package tool;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class KafkaConsumerDemo{

    public static void main(String args[]) {
        ApplicationContext context = new ClassPathXmlApplicationContext("kafka-consumer.xml");
    }


}
