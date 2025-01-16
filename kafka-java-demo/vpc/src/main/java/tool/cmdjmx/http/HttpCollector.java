package tool.cmdjmx.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.cmdjmx.Collector;
import tool.cmdjmx.IRequest;
import tool.cmdjmx.IResponse;


/**
 * @ClassName HttpCollector
 * @Description Http请求收集器
 * @Author hugo
 * @Date 2019-12-12 19:46
 * @Version 1.0
 **/
public class HttpCollector implements Collector {

    private final static Logger log = LoggerFactory.getLogger(HttpCollector.class);

    private final HttpClient httpClient;

    public HttpCollector(String uri, int connectionTimeout, int reqeustTimeout) {
        this.httpClient = new HttpClient(uri, connectionTimeout, reqeustTimeout, 1);
    }

    @Override
    public <RQ extends IRequest, RP extends IResponse> RP collect(RQ request) {
        HttpRequest httpRequest = (HttpRequest)request;
        String responseString = httpClient.doPost(httpRequest.getData());
        return (RP)new HttpResponse(httpRequest.getApiKey(), responseString);
    }

}
