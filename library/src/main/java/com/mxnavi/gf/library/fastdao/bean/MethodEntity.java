package com.mxnavi.gf.library.fastdao.bean;

import android.text.TextUtils;

/**
 * 描述 ：符合要求的注解方法的实体类
 *
 * @author Mark
 * @date 2019.04.18
 */
public class MethodEntity {

    /**
     * 自定义的数据库语句
     */
    private String sql;

    /**
     * 参数类型列表
     */
    private Class<?>[] params;

    /**
     * 方法类型 增删改查
     */
    private String type;

    /**
     * 方法名
     */
    private String name;

    /**
     * 方法参数名
     */
    private String[] paramNames;

    private Class<?> returnType;

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?>[] getParams() {
        return params;
    }

    public void setParams(Class<?>[] params) {
        this.params = params;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String[] getParamNames() {
        return paramNames;
    }

    public void setParamNames(String[] paramNames) {
        this.paramNames = paramNames;
    }

    /**
     * 拼装真正的查询语句
     * @return
     */
    public String createSql() {
        if (!TextUtils.isEmpty(sql)) {

        }
        return null;
    }

    /**
     * 判断解析出来的方法是否有效的
     * @return
     */
    public boolean isValid() {
        return !TextUtils.isEmpty(type);
    }
}
