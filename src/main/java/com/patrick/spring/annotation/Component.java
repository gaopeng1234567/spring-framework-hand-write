package com.patrick.spring.annotation;

import java.lang.annotation.*;

/**
 * @author patrick
 * @date 2021/2/28 3:21 下午
 * @Des bean
 * 最簡單的事是堅持，最難的事還是堅持
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
    String value() default "";
}
