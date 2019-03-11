package com.mxnavi.gf.library.network.annotation;

import com.mxnavi.gf.library.network.type.NetType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NetWork {

    NetType netType() default NetType.NONE;
}
