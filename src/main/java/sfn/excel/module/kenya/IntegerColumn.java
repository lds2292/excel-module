package sfn.excel.module.kenya;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 우선순위는 index -> name 순으로 시작된다. 아무것도 입력되지 않았을경우 index는 필수 입력해야한다
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface IntegerColumn {
    int headerIndex() default -1;
    String headerName() default "";
    int defaultValue() default 0;

    DefaultPolicy policy() default DefaultPolicy.NULL_TO_ZERO;

    public enum DefaultPolicy{
        NULL_TO_ZERO,
        NULL;
    }
}
