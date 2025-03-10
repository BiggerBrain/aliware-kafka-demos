package tool.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    public static String currentPath(Class cls) {
        try {
            // 获取当前类的URL
            String jarPath = cls.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            return jarPath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> readLines(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();

        // 使用 try-with-resources 语句确保资源会被关闭
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line != null && line.trim().length() > 0)
                    lines.add(line.trim());
            }
        }

        return lines;
    }

    /**
     * 追加写入文件
     *
     * @param filePath 文件路径
     * @param content  要追加写入的内容
     * @throws IOException 如果写入文件时发生错误
     */
    public static void appendToFile(String filePath, String content) throws IOException {
        // 使用 try-with-resources 语句确保资源会被关闭
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(content);
            writer.newLine(); // 添加换行符
        }
    }

}
