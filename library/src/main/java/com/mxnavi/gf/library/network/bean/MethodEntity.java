package com.mxnavi.gf.library.network.bean;

import com.mxnavi.gf.library.network.type.NetType;

import java.lang.reflect.Method;

/**
 * 描述 ：符合要求的注解方法的实体类
 *
 * @author Mark
 * @date 2019.03.11
 */

public class MethodEntity {

    //参数类型 NetType
    private Class<?> type;

    //网络类型
    private NetType netType;

    //执行注解的方法
    private Method method;

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public NetType getNetType() {
        return netType;
    }

    public void setNetType(NetType netType) {
        this.netType = netType;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public MethodEntity(Class<?> type, NetType netType, Method method) {

        this.type = type;
        this.netType = netType;
        this.method = method;
    }

}
