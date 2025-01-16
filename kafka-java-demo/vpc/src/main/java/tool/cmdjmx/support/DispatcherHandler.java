package tool.cmdjmx.support;

import tool.cmdjmx.Apis;
import tool.cmdjmx.Handler;
import tool.cmdjmx.IResponse;

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
public final class DispatcherHandler {


    //因启动时初始化写入，其余时间都是读  所以不会存在并发问题(除非refresh context) 存放key -> handler 而非 key -> method  即尽可能避免反射调用  损耗性能
    private final static Map<String, HandleInfo> handlerMapping = new HashMap<>();

    {
        handlerMapping.put(Apis.JMX_APP_INFO.getApiKey(), new HandleInfo(new Handler() {
            @Override
            public void handle(Object data, Map additional) {
                System.out.println(data);
                System.out.println(additional);
            }
        }));
    }

    public static void handle(IResponse response, IResponse def) {
        if (response == null && def == null) {
            System.out.println("dispatcher receive response is null. check collector is failure.");
            return;
        }
        IResponse resp = (response == null ? def : response);
        System.out.println("handle response[{}] start." + resp.requestUri() + "medata {}" + resp.additional().toString());
        HandleInfo handlerInfo = handlerMapping.get(resp.requestUri());
        if (handlerInfo == null) {
            System.out.println("response[{}] not found mapping handler." + resp.requestUri());
            return;
        }
        try {
            handlerInfo.handler.handle(resp.getData(), resp.additional());
        } catch (RuntimeException e) {
            e.printStackTrace();
            if (resp == def || def == null) {
                throw e;
            } else {
                handlerInfo.handler.handle(def.getData(), def.additional());
            }
        }
        System.out.println("handle response[{}] end." + resp.requestUri());

    }

    public static Class<?> getParameterType(String uri) {
        HandleInfo handlerInfo = handlerMapping.get(uri);
        if (handlerInfo == null) {
            System.out.println("url[{}] not found mapping handler." + uri);
            return null;
        }
        return handlerInfo.parameterType;
    }


    class HandleInfo {
        private final Handler handler;
        private final Class<?> parameterType;

        HandleInfo(Handler handler) {
            this.handler = handler;
            this.parameterType = Arrays.asList(handler.getClass().getMethods()).stream().filter(method -> method.getName().equals("handle") && method.getParameterCount() == 2).findFirst().get().getParameterTypes()[0];
        }
    }

}
