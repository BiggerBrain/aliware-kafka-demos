package tool.cmdjmx.http;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class HttpClient {

    private final static Logger logger = LoggerFactory.getLogger(HttpClient.class);

    private CloseableHttpClient client ;
    private RequestConfig config;
    private String host;
    private int retryCount;

    public HttpClient(String host, int connectTimeout, int requestTimeout, int retryCount){
        this.client =HttpClients.createDefault();
        this.host = host;
        this.config = RequestConfig.custom().setConnectTimeout(connectTimeout).setSocketTimeout(connectTimeout).
                setConnectionRequestTimeout(requestTimeout).build();
        this.retryCount = retryCount;

    }

    public HttpClient(String host, int connectTimeout, int requestTimeout, int retryCount, int maxConnTotal, int maxConnPerRoute){
        this.client = HttpClientBuilder.create().setMaxConnTotal(maxConnTotal).setMaxConnPerRoute(maxConnPerRoute).build();
        this.host = host;
        this.config = RequestConfig.custom().setConnectTimeout(connectTimeout).setSocketTimeout(connectTimeout).
                setConnectionRequestTimeout(requestTimeout).build();
        this.retryCount = retryCount;
    }

    public String doPost(String postData,String contentMd5) {
        return doPost(postData,"application/json;charset=utf-8",contentMd5);
    }
    public String doPost(String postData) {
        return doPost(postData,"application/json;charset=utf-8","");
    }
    public String doPost(String postData,String contentType,String contentMd5) {
        if(logger.isDebugEnabled()) {
            logger.debug("Http post uri[{}]", this.host);
        }
        if(logger.isTraceEnabled()) {
            logger.trace("request message[{}]", postData);
        }
        CloseableHttpResponse response = null;
        HttpPost httpPost = null;
        RuntimeException exception = null;
        for(int i = 0; i < retryCount; i++) {
            try {
                StringEntity entity = new StringEntity(postData, "utf-8");
                httpPost = new HttpPost(this.host);
                httpPost.setConfig(config);
                httpPost.setEntity(entity);
                httpPost.setHeader("Content-type",contentType);
                if (!contentMd5.isEmpty()) {
                    httpPost.setHeader("content-md5",contentMd5);
                }
                response =  this.client.execute(httpPost);
                int statusCode = response.getStatusLine().getStatusCode();
                if(statusCode != 200)
                {
                    exception = new RuntimeException(String.format("%s status code %d,request %s",this.host,statusCode,entity.toString()));
                } else {
                    ResponseHandler<String> handler = new BasicResponseHandler();
                    String result = handler.handleResponse(response);
                    if(logger.isDebugEnabled()) {
                        logger.debug("Http post uri[{}], response body[{}]", this.host, result);
                    }
                    return result;
                }
            }catch(UnsupportedEncodingException e) {
                exception = new RuntimeException(String.format("%s unsupported encoding",this.host),e);
            }catch(Exception e){
                exception = new RuntimeException(String.format("%s http post err %s",this.host, e.getMessage()),e);
            }finally {
                if(response != null){
                    try {
                        response.close();
                    } catch (IOException ignored) {

                    }
                }
            }
        }
        throw exception;
    }

    public void close(){
        if(this.client != null){
            try {
                this.client.close();
            } catch (IOException ignored) {

            }
        }
    }
}
