package kafka;

import org.apache.kafka.common.utils.Utils;

public class FindCoordinator {
    public static void main(String[] args) {
        System.out.println(Utils.abs("ckafka-ybexb9a7#commerce_wq24186_finder_goods_appid_acc_pay_product_cnt_agg_v2".hashCode()) % 50);
    }
}
