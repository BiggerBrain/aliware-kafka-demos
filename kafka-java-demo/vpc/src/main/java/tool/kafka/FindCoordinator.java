package tool.kafka;

import org.apache.kafka.common.utils.Utils;

public class FindCoordinator {
    public static void main(String[] args) {
        // consumer_group: commerce_wq24186_fgoods_appid_order_agg
        //consumer_group: commerce_wq24186_finder_goods_appid_acc_pay_product_cnt_agg_v2 100.105.41.174 16
        //consumer_group: commerce_wq24186_fassistant_live_order_agg_v2
        System.out.println(Utils.abs("ckafka-ybexb9a7#commerce_wq24186_finder_goods_appid_acc_pay_product_cnt_agg_v2".hashCode()) % 50);
    }
}
