package publicnet;


import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.security.UserGroupInformation;

@Slf4j
public class HiveClientDemo {
    private HiveMetaStoreClient hiveMetaStoreClient;
    //public static String hiveMetastoreUris = "thrift://10.0.1.37:7004";
    public static String hiveMetastoreUris = "thrift://30.46.110.3:11175";
    public static String hadoopConfDir = "/tmp/";
    public static String krb5Conf = "/etc/krb5.conf";
    public static String keytab = "/tmp/kafka.keytab";
    public static String keytabPrincipal = "kafka@EMR-S0M9K67E";



    void setHiveMetaStoreConf() throws Exception {


        HiveConf hiveConf = new HiveConf();
        hiveConf.addResource(new org.apache.hadoop.fs.Path(hadoopConfDir + "hive-site.xml"));
        hiveConf.addResource(new org.apache.hadoop.fs.Path(hadoopConfDir + "core-site.xml"));

        System.setProperty("javax.security.auth.useSubjectCredsOnly","false");
        System.out.println("-------------------------------------------");
        System.out.println("DEFAULT_CONFIG: hadoop.rpc.protection -> " + hiveConf.get("hadoop.rpc.protection"));
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
        System.out.println("CONFIG: hadoop.rpc.protection -> " + hiveConf.getVar(HiveConf.ConfVars.METASTORE_USE_THRIFT_SASL));
        System.out.println("CONFIG: hive.server2.authentication -> " + hiveConf.getVar(HiveConf.ConfVars.HIVE_SERVER2_AUTHENTICATION));

        if (!hiveConf.getBoolVar(HiveConf.ConfVars.METASTORE_USE_THRIFT_SASL)) {
            return;
        }
        Configuration hadoopConf = new Configuration();
        hadoopConf.setBoolean("hadoop.security.authorization", true);
        hadoopConf.set("hadoop.security.authentication", "kerberos");

        UserGroupInformation.setConfiguration(hiveConf);
        System.out.println("UserGroupInformation.loginUserFromKeytab keytabPrincipal ->" + keytabPrincipal + " keytab -> " +
                keytab);
        UserGroupInformation.loginUserFromKeytab(keytabPrincipal, keytab);
    }

    private boolean ping()  throws Exception {
        System.out.println("ping");
        System.out.println("show databases");
        for (String database : this.hiveMetaStoreClient.getAllDatabases()) {
            System.out.println(database);
        }



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

