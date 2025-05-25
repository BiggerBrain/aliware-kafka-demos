package publicnet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.security.UserGroupInformation;

public class KerberosBaseTest {
    public static String krb5Path = "/tmp/krb5.conf";
    public static String keytabFilePath = "/tmp/kafka.keytab";
    public static final String JAVA_SECURITY_KRB5_CONF = "java.security.krb5.conf";
    public static final String principal1 = "kafka@EMR-S0M9K67E";
    public static final String HADOOP_SECURITY_AUTHENTICATION = "hadoop.security.authentication";
    public static final String KERBEROS = "kerberos";

    public static void main(String[] args) {
        String version = System.getProperty("java.version");
        System.out.printf("version: " + version + "\n");
        try {
            login(krb5Path, principal1, keytabFilePath, "true");
            System.out.printf("login successfully.\n");
        } catch (Exception e) {
            throw new RuntimeException("exception: ", e);
        }
    }

    public static UserGroupInformation login(String krb5Path, String kerberos, String keytab, String boolVal)
            throws Exception {
        System.setProperty(JAVA_SECURITY_KRB5_CONF, krb5Path);
        System.setProperty("sun.security.krb5.debug", "true");
        System.setProperty("javax.security.auth.useSubjectCredsOnly", boolVal);

        Configuration configuration = new Configuration();
        configuration.set(HADOOP_SECURITY_AUTHENTICATION, KERBEROS);
        UserGroupInformation.setConfiguration(configuration);
        UserGroupInformation.loginUserFromKeytab(kerberos, keytab);
        UserGroupInformation ugi = UserGroupInformation.loginUserFromKeytabAndReturnUGI(kerberos, keytab);
        return ugi;
    }
}