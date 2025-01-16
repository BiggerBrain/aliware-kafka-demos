package connect;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.protocol.HttpContext;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import java.io.IOException;
import java.io.InputStream;

public class EsTest {
    public static void main(String[] args)  {

        //校验es的用户名、密码、版本
        //填的这个：{"Ip":"11.145.102.205","Port":9200,"ServiceVip":"10.2.0.6","UniqVpcId":"vpc-ngdmayfd","UserName":"retail","Password":"SXLretail*kd6ic9q","SelfBuilt":false,"Resource":"es-fi7aop3c","IsUpdate":null},
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        String userName = "elastic";
        String passWord = "Ckafka2024#";
        String iP = "11.145.101.207";
        Integer port = 9200;
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(userName, passWord));
        RestClientBuilder restClientBuilder = RestClient.builder(
                new HttpHost(iP, port)
        ).setHttpClientConfigCallback(
                httpAsyncClientBuilder -> {
                    httpAsyncClientBuilder.disableAuthCaching();
                    httpAsyncClientBuilder.setRedirectStrategy(new DisableRedirectStrategy());
                    return httpAsyncClientBuilder.setDefaultCredentialsProvider(
                            credentialsProvider);
                }
        );
        RestClient restClient = restClientBuilder.build();

        InputStream inputStream = null;
        try {
            Response getResponse = restClient.performRequest(new Request(
                    "GET",
                    ""
            ));
            inputStream = getResponse.getEntity().getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        EsGetInfoDTO esGetInfoDTO = null;
        try {
            esGetInfoDTO = mapper.readValue(inputStream, EsGetInfoDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(esGetInfoDTO.toString());

    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    public static class EsGetInfoDTO {
        @JsonProperty("version")
        private Version version;


        public EsGetInfoDTO(Version version) {
            this.version = version;
        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        @AllArgsConstructor
        @NoArgsConstructor
        public class Version {
            @JsonProperty("number")
            String number;
        }

        @Override
        public String toString() {
            return "EsGetInfoDTO{" +
                    "version=" + version +
                    '}';
        }
    }
    public static class DisableRedirectStrategy implements RedirectStrategy {

        @Override
        public boolean isRedirected(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext)
                throws ProtocolException {
            return false;
        }

        @Override
        public HttpUriRequest getRedirect(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext)
                throws ProtocolException {
            return null;
        }
    }

}
