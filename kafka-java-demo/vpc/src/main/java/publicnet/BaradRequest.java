package publicnet;

import java.util.ArrayList;
import java.util.List;

/**
 * reqdata = {
 * "seqId": "baradapi",
 * "caller": "customApi",
 * "namespace": "qce/ckafka",
 * "viewName": "ctopic_info",
 * "metricName": "con_flow",
 * "statistics": "sum",
 * "startTime": "2023-08-16 17:00:00",
 * "endTime": "2023-08-16 17:10:00",
 * "period": "60",
 * "dimensions": [
 * {
 * "appid": "1251001034",
 * "instance_id": "ckafka-7d54w8qm",
 * "topicid": "inter-topic-4yl64ipw",
 * "topicname": "animal-replay-result-notify-cmem-dev"
 * }
 * ]
 * }
 */
public class BaradRequest {
    private String seqId = "baradapi";
    private String caller = "customApi";
    private String namespace = "qce/ckafka";
    private String viewName = "broker_info_inner_observable";
    private String metricName = "alter_configs_error";
    private String statistics = "first";
    private String startTime = "2025-07-20 17:00:00";
    private String endTime = "2025-08-05 17:10:00";
    private String period = "60";
    private List<Dimension> dimensions = new ArrayList<>();

    public BaradRequest(String metricName) {
        this.metricName = metricName;
    }

    public static class Dimension {
        public String region;
        public String observable = "observable_inner";
        public String broker_ip;

        public Dimension() {

        }
        public Dimension(String region, String observable, String broker_ip) {
            this.region = region;
            this.observable = observable;
            this.broker_ip = broker_ip;
        }
    }


    public String getSeqId() {
        return seqId;
    }

    public void setSeqId(String seqId) {
        this.seqId = seqId;
    }

    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public String getStatistics() {
        return statistics;
    }

    public void setStatistics(String statistics) {
        this.statistics = statistics;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public List<Dimension> getDimensions() {
        return dimensions;
    }

    public void setDimensions(List<Dimension> dimensions) {
        this.dimensions = dimensions;
    }
}