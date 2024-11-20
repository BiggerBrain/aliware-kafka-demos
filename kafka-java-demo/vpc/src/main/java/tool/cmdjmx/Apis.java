package tool.cmdjmx;

/**
 * @ClassName Apis
 * @Description APIS
 * @Author hugo
 * @Date 2019-12-13 15:53
 * @Version 1.0
 **/
public enum Apis {

    JMX_BROKER_METRICS("kafka.metrics.mbean:type=MonitorMetricsBean,name=BrokerMetrics"),
    JMX_INSTANCE_METRICS("kafka.common:type=InstanceInfo,name=InstanceMetrics,instance=%s"),
    JMX_APP_INFO("kafka.server:id=%d,type=app-info"),
    JMX_JVM_METRICS("kafka.metrics.mbean:type=MonitorMetricsBean,name=JVMMetrics"),

    HTTP_BROKER_AGENT_RUNTIME("agent.getRuntime"),
    HTTP_BROKER_AVAILABLE_CHECK("agent.brokerAvailableCheck"),

    CONNECTOR_METRICS("kafka.ckafka:type=ConnectorMetrics")
    ;

    Apis(String apiKey) {
        this.apiKey = apiKey;
    }

    private final String apiKey;

    public String getApiKey() {
        return apiKey;
    }

}
