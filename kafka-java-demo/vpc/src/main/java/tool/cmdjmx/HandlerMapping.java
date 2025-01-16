package tool.cmdjmx;
public @interface HandlerMapping {

    String value() default "";

    Apis api();

}
