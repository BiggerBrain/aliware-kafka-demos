package tool.kafka;

import java.util.Map;

public class SystemEnv {
    public static void main(String[] args) {
        for (Map.Entry<String, String> entry : System.getenv().entrySet()) {
            System.out.println(entry);
        }
        System.out.println("APP_REGION:"+System.getenv().get("APP_REGION"));
    }
}
