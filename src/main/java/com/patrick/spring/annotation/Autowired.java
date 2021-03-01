package com.patrick.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author patrick
 * @date 2021/2/28 3:21 下午
 * @Des bean
 * 最簡單的事是堅持，最難的事還是堅持
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface Autowired {
    String value() default "";
}
