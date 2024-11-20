package tool.cmdjmx.http;


import tool.cmdjmx.IRequest;

import java.util.Map;

/**
 * @ClassName HttpRequest
 * @Description http请求
 * @Author hugo
 * @Date 2020/1/17 11:43 AM
 * @Version 1.0
 **/
public class HttpRequest implements IRequest {

    private final String host;

    private final String apiKey;

    private final Map<String, String> params;

    private final String data;

    public HttpRequest(String apiKey, String host, Map<String, String> params, String data) {
        this.apiKey = apiKey;
        this.host = host;
        this.params = params;
        this.data = data;
    }

    @Override
    public String uri() {
        StringBuilder builder = new StringBuilder(host);
        params.entrySet().forEach(entry -> {
            builder.append(",").append(entry.getKey()).append("=").append(entry.getValue());
        });
        return builder.toString();
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getHost() {
        return host;
    }

    public String getData() {
        return data;
    }

}
