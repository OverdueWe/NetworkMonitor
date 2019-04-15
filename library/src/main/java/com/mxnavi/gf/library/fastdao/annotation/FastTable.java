package com.mxnavi.gf.library.fastdao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 描述 ：数据表明
 *
 * @author Mark
 * @date 2019.04.15
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface FastTable {

    String value();
}
