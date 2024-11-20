package tool.cmdjmx;

import java.util.Map;

public interface Handler<T>{

    void handle(T data, Map<String, Object> additional);

    default void post(String data) {};

}
