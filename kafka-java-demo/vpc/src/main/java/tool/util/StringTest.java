package tool.util;

import java.util.Arrays;
import java.util.regex.Pattern;

public class StringTest {
    public static void main(String[] args) {
        Pattern COMMA_WITH_WHITESPACE = Pattern.compile("\\s*,\\s*");
        System.out.println(Arrays.asList(COMMA_WITH_WHITESPACE.split("190:90,", -1)));
        System.out.println(Arrays.asList(COMMA_WITH_WHITESPACE.split("190:90,190:91", -1)));
    }
}
