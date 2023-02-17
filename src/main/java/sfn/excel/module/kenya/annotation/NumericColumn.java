package sfn.excel.module.kenya.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 우선순위는 index -> name 순으로 시작된다.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface NumericColumn {
    int headerIndex() default -1;
    String headerName();
    double defaultValue() default 0.0;
    NotFoundHeaderNamePolicy policy() default NotFoundHeaderNamePolicy.ERROR;
}
