import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class HttpUtil {
    /**
     * 使用URLConnection实现GET请求
     * <p>
     * 1.实例化一个java.net.URL对象； 2.通过URL对象的openConnection()方法得到一个java.net.URLConnection;
     * 3.通过URLConnection对象的getInputStream()方法获得输入流； 4.读取输入流； 5.关闭资源；
     */
    public static String get(String urlStr) throws Exception {

        System.out.println(urlStr);
        URL url = new URL(urlStr);
        URLConnection urlConnection = url.openConnection(); // 打开连接


        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8")); // 获取输入流
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();
        return sb.toString();
    }
}
