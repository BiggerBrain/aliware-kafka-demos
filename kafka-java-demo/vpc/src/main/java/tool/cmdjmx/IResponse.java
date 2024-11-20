package tool.cmdjmx;

import java.util.Map;

/**
 * @ClassName IResponse
 * @Description 请求响应对象
 * @Author hugo
 * @Date 2019-12-12 19:12
 * @Version 1.0
 **/
public interface IResponse {

    //对应请求的uri
    String requestUri();

    <T> T getData();

    Map<String, Object> additional();

    //状态(自定义封装一些状态，通用化)
    default StatusCode status() {
        return StatusCode.SUCCESS;
    }
}
