import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.metrics.KafkaMetric;
import org.apache.kafka.common.metrics.MetricConfig;
import org.apache.kafka.common.metrics.Metrics;
import org.apache.kafka.common.metrics.Quota;
import org.apache.kafka.common.metrics.QuotaViolationException;
import org.apache.kafka.common.metrics.Sensor;
import org.apache.kafka.common.metrics.stats.Rate;

import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RateMultiThreadDemo {

    // 线程数量
    private static final int THREADS = 4;
    // 每个线程单次提交的值范围（模拟字节数/请求数等）
    private static final int MAX_VALUE_PER_RECORD = 32; // 1KB
    // 每个线程提交的间隔
    private static final long RECORD_INTERVAL_MS = 50L;
    // 打印速率的间隔
    private static final long PRINT_INTERVAL_MS = 1000L;
    // 运行总时长
    private static final long TOTAL_RUN_MS = 10_000000L;

    public static void main(String[] args) throws Exception {
        // 1) 创建Metrics与MetricConfig
        Metrics metrics = new Metrics();
        MetricConfig metricConfig = new MetricConfig()
                // 配置窗口与样本数：窗口1秒，样本数11 => 10个完整窗口+当前窗口
                .timeWindow(300, TimeUnit.MILLISECONDS)
                .samples(6)
                .quota(new Quota(
                        2 * 124, true
                ));

        // 2) 创建Sensor并添加Rate统计项
        String sensorName = "demo-rate-sensor";
        Sensor sensor = metrics.sensor(sensorName);
        MetricName rateMetricName = metrics.metricName(
                "byte-rate", // 指标名
                "demo",      // 指标组
                "Demo multi-thread rate metric", // 描述
                Collections.emptyMap()
        );
        Rate rate = new Rate();
        sensor.add(rateMetricName, rate, metricConfig);

        // 3) 启动线程并发record()
        ExecutorService pool = Executors.newFixedThreadPool(THREADS);
        Runnable worker = () -> {
            while (true) {
                try {
                    try {
                        sensor.record(32);
                    } catch (QuotaViolationException e) {
                        long throttleTimeMs = throttleTime(e, rate, metricConfig, System.currentTimeMillis());
                        System.out.println("throttleTimeMs=" + throttleTimeMs);
                        Thread.sleep(throttleTimeMs);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        for (int i = 0; i < THREADS; i++) {
            pool.submit(worker);
        }
        while (true) {
            KafkaMetric kafkaMetric = metrics.metrics().get(rateMetricName);
            Object value = kafkaMetric == null ? null : kafkaMetric.metricValue();
            // 中文注释：metricValue返回当前窗口模型下的速率值（例如字节/秒）
            System.out.printf("[Rate] current=%.2f /s\n", value instanceof Number ? ((Number) value).doubleValue() : 0.0);
            Thread.sleep(PRINT_INTERVAL_MS);
        }

    }

    static Long throttleTime(QuotaViolationException e, Rate rate, MetricConfig config, Long timeMs) {
        Double difference = e.value() - e.bound();
        // Use the precise window used by the rate calculation
        Double throttleTimeMs = difference / e.bound() * windowSize(rate, config, timeMs);

        return Math.round(throttleTimeMs);
    }

    static Long windowSize(Rate rate, MetricConfig config, Long timeMs) {
        return rate.windowSize(config, timeMs);
    }

}