package kafka;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NioSocketFdFailureDemo {

    private static final String UNREACHABLE_HOST = "127.0.0.1";  // 本地
    private static final int UNREACHABLE_PORT = 999;           // 假设这个端口没有服务
    private static final int SOCKET_COUNT = 3000;

    public static void main(String[] args) throws IOException {
        System.out.println("当前 JVM 进程 PID: " + getProcessId());
        System.out.println("请在另一个终端执行：");
        System.out.println("  watch -n 0.1 'ls /proc/" + getProcessId() + "/fd 2>/dev/null | wc -l'");
        System.out.println("\n这个程序将尝试连接 " + UNREACHABLE_HOST + ":" + UNREACHABLE_PORT);
        System.out.println("确保该端口没有服务（如 netstat -an | grep 9999）");
        System.out.println("按回车键开始创建 " + SOCKET_COUNT + " 个失败的连接...");

        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();

        List<SocketChannel> sockets = new ArrayList<>();

        for (int i = 0; i < SOCKET_COUNT; i++) {
            try {
                System.out.println("\n👉 创建第 " + (i+1) + " 个 SocketChannel...");


                SocketChannel channel = SocketChannel.open();  // <-- 这一步就分配了 fd！

                // 输出当前 fd 数量（可选）
                System.out.println("    SocketChannel 已 open，fd 应已 +1");

                // 尝试连接一个不存在的服务
//                boolean connected = channel.connect(new InetSocketAddress(UNREACHABLE_HOST, UNREACHABLE_PORT));
//                if (connected) {
//                    sockets.add(channel);
//                    System.out.println("✅ 竟然连接成功了！（不太可能）");
//                } else {
//                    // 非阻塞模式下可能返回 false，但我们这里会抛异常
//                    System.out.println("❌ connect 返回 false");
//                }
            } catch (Exception e) {
                System.out.println("❌ connect 失败: " + e.getMessage());
                // 注意：即使 connect 失败，open() 已经分配了 fd，但 JVM 会自动 close
            }


        }

        System.out.println("\n所有连接尝试完成。");
        System.out.println("注意观察：fd 数量是否短暂上升后回落？");
        System.out.println("按回车键退出...");
        scanner.nextLine();
    }

    private static String getProcessId() {
        String name = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
        return name.split("@")[0];
    }
}

