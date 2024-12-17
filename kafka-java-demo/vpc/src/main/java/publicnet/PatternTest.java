package publicnet;

import java.util.regex.Pattern;

public class PatternTest {
    public static void main(String[] args) {
        String value = "==127|==18".trim().replaceAll(" ", "");
        Pattern pattern = Pattern.compile(
                "^(>=|>|==|<|<=)((-?\\d+)(\\.\\d+)?)((\\||&)(>=|>|==|<|<=)((-?\\d+)(\\.\\d+)?))*$");
        if (!pattern.matcher(value).matches()) {
            System.out.println("sss");
        }
    }
}


