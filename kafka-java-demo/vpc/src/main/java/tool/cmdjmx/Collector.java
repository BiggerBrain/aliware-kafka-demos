package tool.cmdjmx;

import java.io.IOException;

/**
 * @ClassName Collector
 * @Description 监控指标采集器
 * @Author hugo
 * @Date 2019-12-12 19:10
 * @Version 1.0
 **/
public interface Collector {

    <RQ extends IRequest, RP extends IResponse> RP collect(RQ request) throws Exception;

    default boolean isActive() throws IOException {
        return true;
    }

}
