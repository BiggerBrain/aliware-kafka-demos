package tool;

import org.apache.kafka.common.utils.Utils;

public class ConsumerGroupCoordinator {
    public static void main(String[] args) {
        //get /brokers/topics/ckafka-ybexb9a7#__consumer_offsets/partitions/16/state
        //{"controller_epoch":7,"leader":12960,"version":1,"leader_epoch":20,"isr":[12960,15477,15078]}
        //
        System.out.println(Utils.abs("ckafka-ybexb9a7#commerce_wq24186_finder_goods_appid_acc_pay_product_cnt_agg_v2".hashCode() % 30));
    }
}
