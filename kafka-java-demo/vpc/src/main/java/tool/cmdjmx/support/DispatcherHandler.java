package tool.cmdjmx.support;

import com.qcloud.ckafka.monitor.collect.Apis;
import com.qcloud.ckafka.monitor.collect.IResponse;
import com.qcloud.ckafka.monitor.handler.Handler;
import com.qcloud.ckafka.monitor.handler.HandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName DispatcherHandler
 * @Description Response结果分发 给 对应Handler
 * @Author hugo
 * @Date 2019-12-12 21:49
 * @Version 1.0
 **/
@Component
public final class DispatcherHandler implements InitializingBean, ApplicationContextAware {

    private final static Logger logger = LoggerFactory.getLogger(DispatcherHandler.class);

    //因启动时初始化写入，其余时间都是读  所以不会存在并发问题(除非refresh context) 存放key -> handler 而非 key -> method  即尽可能避免反射调用  损耗性能
    private final static Map<String, HandleInfo> handlerMapping = new HashMap<>();

    public static void post(Apis apis, String data) {
        HandleInfo handlerInfo = handlerMapping.get(apis.getApiKey());
        if(handlerInfo == null) {
            logger.info("response[{}] not found mapping handler.", apis.getApiKey());
            return;
        }
        handlerInfo.handler.post(data);
    }

    public static void handle(IResponse response) {
        handle(response, null);
    }

    public static void handle(IResponse response, IResponse def) {
        if (response == null && def == null) {
            logger.error("dispatcher receive response is null. check collector is failure.");
            return;
        }
        IResponse resp = (response == null ? def : response);
        if (logger.isDebugEnabled()) {
            logger.debug("handle response[{}] start.", resp.requestUri());
            logger.debug("medata {}", resp.additional().toString());
        }
        HandleInfo handlerInfo = handlerMapping.get(resp.requestUri());
        if(handlerInfo == null) {
            logger.info("response[{}] not found mapping handler.", resp.requestUri());
            return;
        }
        try{
            handlerInfo.handler.handle(resp.getData(),resp.additional());
        } catch (RuntimeException e) {
            logger.info("handle response uri[" + resp.requestUri() + "] error.", e);
            if (resp == def || def == null) {
                throw e;
            } else {
                handlerInfo.handler.handle(def.getData(), def.additional());
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("handle response[{}] end.", resp.requestUri());
        }
    }

    public static Class<?> getParameterType(String uri) {
        HandleInfo handlerInfo = handlerMapping.get(uri);
        if(handlerInfo == null) {
            logger.warn("url[{}] not found mapping handler.", uri);
            return null;
        }
        return handlerInfo.parameterType;
    }

    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        //applicationContext中获取所有Handler Bean
        String[] handlerBeanNames = applicationContext.getBeanNamesForType(Handler.class);
        logger.info("find handler names{}.", Arrays.asList(handlerBeanNames).toString());
        for(String beanName : handlerBeanNames) {
            //获取Bean和注解  保存缓存映射
            Handler handler = applicationContext.getBean(beanName, Handler.class);
            HandlerMapping mapping = handler.getClass().getAnnotation(HandlerMapping.class);
            if(mapping != null) {
                //获取映射信息，并放入map
                handlerMapping.putIfAbsent(mapping.api().getApiKey(), new HandleInfo(handler));
            } else {
                logger.warn("don't find handler object[{}]", beanName);
            }
        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    class HandleInfo {
        private final Handler handler;
        private final Class<?> parameterType;

        HandleInfo(Handler handler) {
            this.handler = handler;
            this.parameterType =Arrays.asList(handler.getClass().getMethods()).stream().filter(method -> method.getName().equals("handle") && method.getParameterCount() == 2).findFirst().get().getParameterTypes()[0];
        }
    }

}
