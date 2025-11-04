package kafka;

import org.apache.kafka.common.utils.Utils;

public class FindCoordinator {
    public static void main(String[] args) {
        System.out.println(Utils.abs("bg_group_bdf58b1b072c4d62bf57caf4f60eea3a".hashCode()) % 32);
    }
}
