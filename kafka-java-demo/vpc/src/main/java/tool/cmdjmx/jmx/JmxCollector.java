package tool.cmdjmx.jmx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.cmdjmx.Collector;
import tool.cmdjmx.IRequest;
import tool.cmdjmx.IResponse;
import tool.cmdjmx.support.DispatcherHandler;

import javax.management.MBeanServerInvocationHandler;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @ClassName JMXCollector
 * @Description JMX 监控指标 收集器
 * @Author hugo
 * @Date 2019-12-12 19:46
 * @Version 1.0
 **/
public class JmxCollector implements Collector {

    private final static Logger log = LoggerFactory.getLogger(JmxCollector.class);

    private final String jmxURL;

    private volatile JMXConnector connector;
    private long lastUpdate;
    private final String ipAndPort;

    public JmxCollector(String ipAndPort) {
        this.ipAndPort = ipAndPort;
        this.jmxURL = "service:jmx:rmi:///jndi/rmi://" +ipAndPort+ "/jmxrmi";;
    }

    public void init() throws IOException {
        log.info("init jmx, jmxUrl: {}, and begin to connect it",jmxURL);
        Socket socket = new Socket();
        try {
            //因为无法设置connect timeout，默认2分钟  所以采取先尝试连接try,如果可以连接上，则连接jmx
            socket.connect(new InetSocketAddress(ipAndPort.substring(0, ipAndPort.lastIndexOf(":")), Integer.parseInt(ipAndPort.substring(ipAndPort.lastIndexOf(":") + 1))), 500);
            lastUpdate = System.currentTimeMillis();
            JMXServiceURL serviceURL = new JMXServiceURL(jmxURL);
            connector = JMXConnectorFactory.connect(serviceURL);
        } catch (IOException e) {
            connector = null;
            throw e;
        } finally {
            socket.close();
        }
    }

    @Override
    public <RQ extends IRequest, RP extends IResponse> RP collect(RQ request) throws MalformedObjectNameException, IOException {
        if (log.isDebugEnabled()) {
            log.debug("request uri[{}]", request.uri());
        }
        lastUpdate = System.currentTimeMillis();
        JmxRequest jmxRequest = (JmxRequest)request;
        ObjectName mbeanName = new ObjectName(jmxRequest.uri());
        //还是可能产生空指针(低可能性)，一通认定失败
        Object proxy = MBeanServerInvocationHandler.newProxyInstance(connector.getMBeanServerConnection(), mbeanName, DispatcherHandler.getParameterType(jmxRequest.getApi()), false);
        return (RP)new JmxResponse(jmxRequest.getIp(), jmxRequest.getApi(), proxy);
    }

    @Override
    public boolean isActive(){
        if(connector != null) {
            Socket socket = new Socket();
            try {
                //因为无法设置connect timeout，默认2分钟  所以采取先尝试连接try,如果可以连接上，则连接jmx
                socket.connect(new InetSocketAddress(ipAndPort.substring(0, ipAndPort.lastIndexOf(":")),
                        Integer.parseInt(ipAndPort.substring(ipAndPort.lastIndexOf(":") + 1))), 2000);
                return true;
            } catch (IOException e) {
                log.error("connect broker timeout:{}",ipAndPort);
                this.close();
            } finally {
                try {
                    socket.close();
                } catch (Exception e) {
                    log.error("Jmx Collector close socket error", e);
                }
            }
        }
        return false;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void close() {
        if(connector != null){
            try {
                connector.close();
            } catch (IOException ignored) {
                log.error(ignored.getMessage());
            } finally {
                connector = null;
            }
        }
    }

}
