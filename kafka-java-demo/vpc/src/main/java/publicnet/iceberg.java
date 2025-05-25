package publicnet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;

import java.security.PrivilegedAction;

public class iceberg {
    public static void main(String[] args) {
        try {
            //    /Users/fengqi/Downloads/krb5.conf 是emr集群的krb5配置文件，在/etc/krb5.conf
            System.setProperty("java.security.krb5.conf", "/tmp/krb5.conf");
            Configuration configuration = new Configuration();
            configuration.set("hadoop.security.authentication", "Kerberos");
            UserGroupInformation.setConfiguration(configuration);
            //  /Users/fengqi/Downloads/emr.keytab 为emr集群10.0.0.48节点上的/var/krb5kdc/emr.keytab文件  --需要改
            // hadoop/10.0.0.48@EMR-K4NPHPVO 为 10.0.0.48节点的pricipal --需要改
            // EMR-K4NPHPVO为kdc域名  -- 需要改
            //kafka
            UserGroupInformation.loginUserFromKeytab("kafka@EMR-S0M9K67E", "/tmp/emr.keytab");
            System.out.println(UserGroupInformation.getCurrentUser() + "------" + UserGroupInformation.getLoginUser());

            UserGroupInformation loginUser = UserGroupInformation.getLoginUser();

//            loginUser.doAs(new PrivilegedAction<Object>() {
//
//                public Object run() {
//
//                }
//            });
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }
}

