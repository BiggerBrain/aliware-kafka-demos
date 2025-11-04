package metric;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.UniformReservoir;
import com.codahale.metrics.Snapshot;

public class HistogramDecayDemo {
    public static void main(String[] args) {
        // 创建 Histogram，使用 UniformReservoir（默认大小 1028）
        Histogram histogram = new Histogram(new UniformReservoir());

        // Step 1: 记录一个异常大值
        histogram.update(9999);
        Snapshot snapshot1 = histogram.getSnapshot();
        System.out.println("After adding 9999:");
        System.out.println("  Max = " + snapshot1.getMax());
        System.out.println("  Count = " + histogram.getCount());

        // Step 2: 记录 2000 个较小的值（远超 reservoir 容量 1028）
        for (int i = 0; i < 20000000; i++) {
            histogram.update(100);
        }

        // Step 3: 再次查看最大值
        Snapshot snapshot2 = histogram.getSnapshot();
        System.out.println("\nAfter adding 2000 values of 100:");
        System.out.println("  Max = " + snapshot2.getMax());
        System.out.println("  Min = " + snapshot2.getMin());
        System.out.println("  Mean = " + snapshot2.getMean());
        System.out.println("  95th Percentile = " + snapshot2.get95thPercentile());
        System.out.println("  Count = " + histogram.getCount());

        // Step 4: 验证 reservoir 内部是否还有 9999
        // 注意：我们无法直接访问 reservoir 内容，但可以通过统计推断
        if (snapshot2.getMax() == 100) {
            System.out.println("\n✅ PASS: The outlier value 9999 has been replaced. Max decayed to 100.");
        } else {
            System.out.println("\n❌ FAIL: Max is still " + snapshot2.getMax() + ", 9999 may still exist in reservoir.");
        }
    }
}