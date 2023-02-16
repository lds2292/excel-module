package sfn.excel.module.kenya.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import sfn.excel.module.kenya.NotFoundHeaderNamePolicy;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface LocalDateTimeColumn {
    int headerIndex() default -1;
    String headerName();
    String defaultValue() default "";

    String pattern() default "yyyy-MM-dd HH:mm:ss";
    NotFoundHeaderNamePolicy policy() default NotFoundHeaderNamePolicy.ERROR;
}
