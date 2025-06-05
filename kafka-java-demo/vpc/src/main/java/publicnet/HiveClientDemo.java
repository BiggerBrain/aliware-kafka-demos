package publicnet;


import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.CurrentNotificationEventId;
import org.apache.hadoop.security.UserGroupInformation;

@Slf4j
public class HiveClientDemo {
    private HiveMetaStoreClient hiveMetaStoreClient;
    public static String hiveMetastoreUris = "thrift://cdh-master:9083";
    public static String hadoopConfDir = "/Users/yl/JavaProject/hiveApi/conf/";
    public static String krb5Conf = "/etc/krb5.conf";
    public static String keytab = "/Users/yl/JavaProject/hiveApi/conf/hive.keytab";
    public static String keytabPrincipal = "hive/cdh-master@LCC.COM";



    void setHiveMetaStoreConf() throws Exception {
        HiveConf hiveConf = new HiveConf();
        hiveConf.addResource(new org.apache.hadoop.fs.Path(hadoopConfDir + "hive-site.xml"));
        hiveConf.addResource(new org.apache.hadoop.fs.Path(hadoopConfDir + "core-site.xml"));

        log.info("-------------------------------------------");
        log.info("DEFAULT_CONFIG: hadoop.rpc.protection -> " + hiveConf.get("hadoop.rpc.protection"));
        if (hiveConf.getVar(HiveConf.ConfVars.METASTOREURIS).isEmpty()) {
            hiveConf.setVar(HiveConf.ConfVars.METASTOREURIS, hiveMetastoreUris);
        }

        handleKerberos(hiveConf);

        try {
            this.hiveMetaStoreClient = new HiveMetaStoreClient(hiveConf);
            ping();
        } catch (Exception e) {
            log.error("setHiveMetaStoreConf error", e);
            throw e;
        }
    }

    private void handleKerberos(HiveConf hiveConf) throws Exception {
        System.setProperty("java.security.krb5.conf",krb5Conf);
        log.info("CONFIG: hadoop.rpc.protection -> " + hiveConf.getVar(HiveConf.ConfVars.METASTORE_USE_THRIFT_SASL));
        log.info("CONFIG: hive.server2.authentication -> " + hiveConf.getVar(HiveConf.ConfVars.HIVE_SERVER2_AUTHENTICATION));

        if (!hiveConf.getBoolVar(HiveConf.ConfVars.METASTORE_USE_THRIFT_SASL)) {
            return;
        }
        Configuration hadoopConf = new Configuration();
        hadoopConf.setBoolean("hadoop.security.authorization", true);
        hadoopConf.set("hadoop.security.authentication", "kerberos");

        UserGroupInformation.setConfiguration(hiveConf);
        log.info("UserGroupInformation.loginUserFromKeytab keytabPrincipal ->" + keytabPrincipal + " keytab -> " +
                keytab);
        UserGroupInformation.loginUserFromKeytab(keytabPrincipal, keytab);
    }

    private boolean ping()  throws Exception {
        log.info("ping");
        log.info("show databases");
        for (String database : this.hiveMetaStoreClient.getAllDatabases()) {
            log.info(database);
        }

        CurrentNotificationEventId event = this.hiveMetaStoreClient.getCurrentNotificationEventId();
        log.info("CurrentNotificationEventId -> " + event.getEventId());

        return true;
    }


    public static void main(String[] args) {
        try {
            HiveClientDemo client = new HiveClientDemo();
            client.setHiveMetaStoreConf();
            client.ping();
        } catch (Exception e) {
            log.error("error", e);
        }
    }
}

