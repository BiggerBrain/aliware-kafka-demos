
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureExample {
    public static void main(String[] args) {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);

        // 提交一个耗时任务
        Callable<String> task = () -> {
            System.out.println("任务开始执行...");
            Thread.sleep(10000); // 模拟耗时操作
            System.out.println("任务执行完成。");
            return "任务执行结果";
        };

        Future<String> future = executor.submit(task);

        // 可以做其他事情
        System.out.println("主线程继续执行其他任务...");

        try {
            // 获取结果（阻塞直到任务完成）
            String result = future.get(2, TimeUnit.SECONDS); // 可以设置超时：future.get(5, TimeUnit.SECONDS);
            System.out.println("获取到结果: " + result);
        } catch (InterruptedException e) {
            System.out.println("主线程被中断");
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            System.out.println("任务执行过程中发生异常: " + e.getCause().getMessage());
        } catch (TimeoutException e) {
            System.out.println("获取结果超时");
            boolean mayInterruptIfRunning = true;
            boolean canceled = future.cancel(mayInterruptIfRunning);

            if (canceled) {
                System.out.println("任务已成功取消");
            } else {
                System.out.println("任务无法取消（可能已完成或已被取消）");
            }
        } finally {
            executor.shutdown();
        }
    }
}