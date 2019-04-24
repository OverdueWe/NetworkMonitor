package com.mxnavi.gf.library.fastdao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 描述 ： 更新
 * @author Mark
 * @date 2019.04.18
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface Update {

    String value();
}
