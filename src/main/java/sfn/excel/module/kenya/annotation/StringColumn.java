package sfn.excel.module.kenya.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import sfn.excel.module.kenya.NotFoundHeaderNamePolicy;

/**
 * 우선순위는 index -> name 순으로 시작된다
 * <p>
 * HeaderName을 찾지 못한다면 에러가 발생한다.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface StringColumn {
    int headerIndex() default -1;
    String headerName();
    String defaultValue() default  "";

    NotFoundHeaderNamePolicy policy() default NotFoundHeaderNamePolicy.ERROR;
}
