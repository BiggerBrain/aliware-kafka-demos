package tool;

import java.io.BufferedReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class UnixTimeTrans {
    /**
     * 解析文件内容并转换LogAppendTime后的Unix时间戳为年月日时分秒格式
     *
     * @param filePath 文件路径
     */
    public static void parseLogFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // 查找LogAppendTime的值
                String logAppendTimePrefix = "LogAppendTime: ";
                int logAppendTimeIndex = line.indexOf(logAppendTimePrefix);
                if (logAppendTimeIndex != -1) {
                    // 提取LogAppendTime后的Unix时间戳
                    int startIndex = logAppendTimeIndex + logAppendTimePrefix.length();
                    int endIndex = line.indexOf(' ', startIndex);
                    if (endIndex == -1) {
                        endIndex = line.length();
                    }
                    String logAppendTimeStr = line.substring(startIndex, endIndex);
                    long logAppendTime = Long.parseLong(logAppendTimeStr);

                    // 转换Unix时间戳为年月日时分秒格式
                    LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(logAppendTime), ZoneId.systemDefault());
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedDateTime = dateTime.format(formatter);

                    // 打印结果
                    System.out.println("LogAppendTime: " + formattedDateTime +" "+ line.substring(logAppendTimeIndex));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        parseLogFile("/Users/lishixiong/IdeaProjects/ckafka/aliware-kafka-demos/kafka-java-demo/vpc/src//main/java/tool/tmp");
    }
}
