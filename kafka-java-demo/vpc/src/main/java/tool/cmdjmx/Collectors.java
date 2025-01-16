package tool.cmdjmx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.cmdjmx.http.HttpCollector;
import tool.cmdjmx.http.HttpRequest;
import tool.cmdjmx.jmx.JmxCollector;
import tool.cmdjmx.jmx.JmxRequest;

import javax.management.MalformedObjectNameException;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName Collectors
 * @Description collector util工具
 * @Author hugo
 * @Date 2019-12-12 20:58
 * @Version 1.0
 **/
public class Collectors{

    private final static Logger logger = LoggerFactory.getLogger(Collectors.class);

    private final static Map<String, JmxCollector> jmxCollectors = new ConcurrentHashMap<>();
    private final static Map<String, HttpCollector> httpCollectors = new ConcurrentHashMap<>();

    public static <RP extends IResponse, RQ extends IRequest> RP collect(RQ request) {
        try{
            if(request instanceof JmxRequest) {
                JmxRequest jmxRequest = (JmxRequest)request;
                if(jmxCollectors.get(jmxRequest.getAddress()) == null) {
                    JmxCollector jmxCollector = new JmxCollector(jmxRequest.getAddress());
                    jmxCollector.init();
                    jmxCollectors.putIfAbsent(jmxRequest.getAddress(), jmxCollector);
                }
                if (jmxCollectors.get(jmxRequest.getAddress()).isActive()) {
                    return jmxCollectors.get(jmxRequest.getAddress()).collect(jmxRequest);
                } else {
                    logger.warn("JmxCollector is InActive, please check that is inited and no closed.");
                    jmxCollectors.get(jmxRequest.getAddress()).init();
                }
            } else if (request instanceof HttpRequest) {
                HttpRequest httpRequest = (HttpRequest)request;
                if(httpCollectors.get(httpRequest.getHost()) == null) {
                    //固化超时时间，防止流程耗时过长
                    HttpCollector httpCollector = new HttpCollector(httpRequest.uri(), 5000, 3000);
                    httpCollectors.putIfAbsent(httpRequest.getHost(), httpCollector);
                }
                return httpCollectors.get(httpRequest.getHost()).collect(httpRequest);
            }
        } catch (MalformedObjectNameException e) {
            logger.error("collect[" + request.uri() + " " + request  + "] error, object name is malformed. please check it.", e);
        } catch (IOException e) {
            logger.error("collect[" + request.uri() + " " + request + "] error." + e.getMessage());
            if(request instanceof JmxRequest) {
                JmxRequest jmxRequest = (JmxRequest) request;
                if(jmxCollectors.get(jmxRequest.getAddress()) != null) {
                    jmxCollectors.get(jmxRequest.getAddress()).close();
                }
            }
        }
        return null;
    }

    public static void checkExpiredCollectors() {
        Set<String> ips = jmxCollectors.keySet();
        long currentTime = System.currentTimeMillis();
        for(String address : ips) {
            JmxCollector jmxCollector = jmxCollectors.get(address);
            //十分钟空闲，则关闭销毁
            if((currentTime - jmxCollector.getLastUpdate()) > 50 * 600) {
                logger.info("JmxCollector[{}] is expired, close and remove the JmxCollector.", address);
                jmxCollectors.remove(address).close();
            }
        }
    }

}
