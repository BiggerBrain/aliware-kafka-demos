package tool.cmdjmx.http;


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
public class HttpResponse implements IResponse {

    private final String apiKey;

    private final String result;

    private final Map<String, Object> additional;

    public HttpResponse(String apiKey, String result) {
        this(apiKey, result, new HashMap<>());
    }

    public HttpResponse(String apiKey, String result, Map<String, Object> additional) {
        this.apiKey = apiKey;
        this.result = result;
        this.additional = additional;
    }

    public String getData() {
        //外部需要判错，可能存在类型转换异常  试用错误
        return result;
    }

    @Override
    public Map<String, Object> additional() {
        return additional;
    }

    @Override
    public String requestUri() {
        return apiKey;
    }

}
