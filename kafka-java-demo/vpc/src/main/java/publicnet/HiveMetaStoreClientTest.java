package publicnet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.Database;
import org.apache.hadoop.hive.metastore.api.Table;
import org.apache.hadoop.security.UserGroupInformation;

import java.util.List;

public class HiveMetaStoreClientTest {
    //thrift://30.46.110.3:11175
    //thrift://10.0.1.37:7004
    public static void main(String[] args) {
        try {
            String uri = args[0];
            System.out.println("uri:" + uri);
            // 设置 Hadoop 配置
            Configuration conf = new Configuration();
            conf.set("hadoop.security.authentication", "kerberos");
            conf.set("hive.metastore.uris", uri); // 替换为你的 HMS 地址
            // 设置 Kerberos 配置
            System.setProperty("java.security.krb5.conf", "/etc/krb5.conf"); // 替换为你的 krb5.conf 路径;
            //System.setProperty("java.security.krb5.conf", "/etc/krb5.conf"); // 替换为你的 krb5.conf 路径;

            UserGroupInformation.setConfiguration(conf);
            UserGroupInformation.loginUserFromKeytab("kafka@EMR-S0M9K67E", "/tmp/kafka.keytab"); // 替换为你的 principal 和 keytab

            // 创建 HiveMetaStoreClient
            HiveMetaStoreClient client = new HiveMetaStoreClient(conf);
            client.setMetaConf("hive.metastore.uris", uri); // 替换为你的 HMS 地址
            client.setMetaConf("hive.metastore.client.capability.check","false");


            // 获取所有数据库
            List<String> databases = client.getAllDatabases();
            System.out.println("Databases: " + databases);

            // 获取默认数据库的信息
            Database defaultDb = client.getDatabase("default");
            System.out.println("Default Database: " + defaultDb);

            Table table = client.getTable("default", "products20");
            System.out.println(table.toString());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
