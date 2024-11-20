package tool.cmdjmx.jmx;


import tool.cmdjmx.Apis;
import tool.cmdjmx.IRequest;

/**
 * @ClassName JMXRequest
 * @Description jmx请求
 * @Author hugo
 * @Date 2019-12-12 19:41
 * @Version 1.0
 **/

public class JmxRequest implements IRequest {

    //IP and Port
    private final String ip;
    //Port
    private final int port;

    private final Apis api;

    private final Object[] objects;

    public JmxRequest(String ip, int port, Apis api, Object... objects) {
        this.ip = ip;
        this.port = port;
        this.api = api;
        this.objects = objects;
    }

    @Override
    public String uri() {
        return String.format(api.getApiKey(), objects);

    }

    public String getApi() {
        return api.getApiKey();
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getAddress() {
        return ip + ":" + port;
    }
}
