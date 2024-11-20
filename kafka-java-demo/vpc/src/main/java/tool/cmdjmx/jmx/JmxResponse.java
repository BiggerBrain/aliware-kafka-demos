package tool.cmdjmx.jmx;


import tool.cmdjmx.IResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName JMXResponse
 * @Description JMX响应
 * @Author hugo
 * @Date 2019-12-12 19:42
 * @Version 1.0
 **/
public class JmxResponse implements IResponse {

    private final String uri;


    private final Object proxy;

    private final String ip;

    private final Map<String, Object> additional = new HashMap<>();

    public JmxResponse(String ip, String uri, Object proxy) {
        this.ip = ip;
        this.uri = uri;
        this.proxy = proxy;
        additional.putIfAbsent("brokerIp", ip);
    }

    public JmxResponse(String ip, String uri, Object proxy, Map<String, Object> additional) {
        this(ip, uri, proxy);
        this.additional.putAll(additional);
    }

    public <T> T getData() {
        //外部需要判错，可能存在类型转换异常  试用错误
        return (T)proxy;
    }

    @Override
    public Map<String, Object> additional() {
        return additional;
    }

    @Override
    public String requestUri() {
        return uri;
    }

    public String getIp() {
        return ip;
    }
}
