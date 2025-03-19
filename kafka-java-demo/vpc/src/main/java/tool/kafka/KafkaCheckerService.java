package tool.kafka;

import java.util.Map;

public abstract class KafkaCheckerService {
    protected void printEnv() {
        for (Map.Entry<String, String> env : System.getenv().entrySet()) {
            System.out.println("环境变量:" + env);
        }
    }
}
