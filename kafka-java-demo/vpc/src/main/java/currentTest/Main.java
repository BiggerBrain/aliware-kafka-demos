package currentTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;


abstract class AbstractFetcherThread {
    protected ReentrantLock partitionMapLock = new ReentrantLock();
}

class AbstractFetcherThread1 extends AbstractFetcherThread {

    Quota quota;

    public AbstractFetcherThread1(Quota quota) {
        this.quota = quota;
    }

    void check() {
        synchronized (partitionMapLock){
            quota.check();
        }
    }
}


public class Main {
    public static void main(String[] args) {        Quota quota = new Quota();
        ArrayList<Thread> array = new ArrayList(100);
        for (int i = 0; i < 24; i++) {
            array.add(new Thread(new Runnable() {
                @Override
                public void run() {
                    quota.check();
                }
            }));
        }
        for (int i = 0; i < 24; i++) {
            array.get(i).start();
        }
    }

    //  // 使用match表达式匹配颜色
    //  def printColorName(color: Color): Unit = color match {
    //    case Red => println("红色" + Red.hashCode())
    //      println("红色1")
    //      println("红色2")
    //      println("红色3")
    //    case Green => println("绿色")
    //    case Blue => println("蓝色")
    //  }
    //
    //  // 测试
    //  printColorName(Red) // 输出：红色
    //  printColorName(Red) // 输出：红色
    //  printColorName(Green) // 输出：绿色
    //  printColorName(Blue) // 输出：蓝色
}