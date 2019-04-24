package com.mxnavi.gf.library.fastdao.parser;

import com.mxnavi.gf.library.fastdao.annotation.Delete;
import com.mxnavi.gf.library.fastdao.annotation.FastParam;
import com.mxnavi.gf.library.fastdao.annotation.Insert;
import com.mxnavi.gf.library.fastdao.annotation.Query;
import com.mxnavi.gf.library.fastdao.annotation.Update;
import com.mxnavi.gf.library.fastdao.bean.MethodEntity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 描述 ：方法解析类
 *
 * @author Mark
 * @date 2019.04.22
 */
public final class MethodParser {

    private static final String TAG = "MethodParser";

    private MethodEntity entity;

    MethodParser() {
        entity = new MethodEntity();
    }

    public static MethodEntity paser(Method method) {
        method.setAccessible(true);
        MethodParser parser = new MethodParser();
        if (parser.parseMethodAnnotations(method)) {
            parser.parseParamAnnotations(method);
            parser.paserParams(method);
        }
        return parser.getEntity();
    }

    public MethodEntity getEntity() {
        return entity;
    }

    /**
     * 解析方法注解
     *
     * @param method
     */
    private boolean parseMethodAnnotations(Method method) {
        if (method.isAnnotationPresent(Insert.class)) {
            Insert insert = method.getAnnotation(Insert.class);
            entity.setType(Insert.class.getName());
            entity.setSql(insert.value());
        } else if (method.isAnnotationPresent(Delete.class)) {
            Delete delete = method.getAnnotation(Delete.class);
            entity.setType(Delete.class.getName());
            entity.setSql(delete.value());
        } else if (method.isAnnotationPresent(Update.class)) {
            Update update = method.getAnnotation(Update.class);
            entity.setType(Update.class.getName());
            entity.setSql(update.value());
        } else if (method.isAnnotationPresent(Query.class)) {
            Query query = method.getAnnotation(Query.class);
            entity.setType(Query.class.getName());
            entity.setSql(query.value());
        } else {
            throw new IllegalArgumentException("The method must assign method annotation");
        }

        return entity.isValid();
    }


    /**
     * 解析参数注解
     *
     * @param method
     */
    private void parseParamAnnotations(Method method) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        int count = parameterAnnotations.length;
        String[] paramNames = new String[count];
        for (int i = 0; i < count; i++) {
            Annotation[] annotations = parameterAnnotations[i];
            if (annotations != null) {
                if (annotations.length > 1) {
                    throw new IllegalArgumentException("The params annotation must be one");
                }
                if (annotations[0] instanceof FastParam) {
                    paramNames[i] = ((FastParam) annotations[0]).value();
                } else {
                    throw new IllegalArgumentException("The params annotation must inatance of FastParam");
                }
            }
        }
        entity.setParamNames(paramNames);
    }

    /**
     * 解析参数
     *
     * @param method
     */
    private void paserParams(Method method) {
        entity.setReturnType(method.getReturnType());
        entity.setName(method.toString());
    }

}
