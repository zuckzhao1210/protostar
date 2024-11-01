package io.github.yezhihao.protostar.annotation;

import java.lang.annotation.*;

/**
 * @author zhaozhe
 * @since 2024/11/1 8:47
 */
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomMessage {
    int[] value() default {};

    String desc() default "";
}
