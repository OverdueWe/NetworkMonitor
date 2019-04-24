package com.mxnavi.gf.library.fastdao.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface FastParam {

    /**
     * 不能仅为一个默认方法 否则反射將会找不到Anootation
     * @return
     */
    String value();

    boolean encoded() default false;
}
