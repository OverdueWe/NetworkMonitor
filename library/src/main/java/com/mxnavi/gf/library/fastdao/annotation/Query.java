package com.mxnavi.gf.library.fastdao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 描述 ：查询
 * @author Mark
 * @date 2019.04.15
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface Query {

    String value();
}
