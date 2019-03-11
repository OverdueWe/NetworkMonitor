package com.mxnavi.gf.library.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mxnavi.gf.library.network.annotation.NetWork;
import com.mxnavi.gf.library.network.bean.MethodEntity;
import com.mxnavi.gf.library.network.type.NetType;
import com.mxnavi.gf.library.network.utils.NetConstant;
import com.mxnavi.gf.library.network.utils.NetWorkUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 描述 ：网络状态监听类
 *
 * @author Mark
 * @date 2019.03.11
 */
public class NetWorkReceiver extends BroadcastReceiver {

    private static final String TAG = "NetWorkReceiver";

    private Map<Object, List<MethodEntity>> netWorkList;

    public NetWorkReceiver() {
        netWorkList = new HashMap<>();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (NetConstant.ACTION.equals(intent.getAction())) {
            Log.d(TAG, "NetWork is changed!");
            if (NetWorkUtil.isNetWorkAvailable(context)) {
                Log.d(TAG, "NetWork is available!");
            } else {
                Log.d(TAG, "NetWork is unavailable!");
            }

            //分发网络状态
            NetType netType = NetWorkUtil.getNetType(context);
            post(netType);
        }
    }

    /**
     * 分发网络状态
     *
     * @param netType
     */
    private void post(NetType netType) {
        Set<Object> keyset = netWorkList.keySet();
        for (Object key : keyset) {
            List<MethodEntity> methodEntities = netWorkList.get(key);
            if (methodEntities != null) {
                for (MethodEntity method : methodEntities) {
                    if (method.getType().isAssignableFrom(netType.getClass())) {
                        Log.d(TAG,"post nettype : " + netType.toString());
                        switch (method.getNetType()) {
                            case AUTO:
                                if (netType == NetType.NONE || netType == NetType.WIFI || netType == NetType.CMWAP) {
                                    invoke(method, key, netType);
                                }
                                break;
                            case WIFI:
                                if (netType == NetType.NONE || netType == NetType.WIFI) {
                                    invoke(method, key, netType);
                                }
                                break;
                            case CMNET:
                                if (netType == NetType.NONE || netType == NetType.CMNET) {
                                    invoke(method, key, netType);
                                }
                                break;
                            case CMWAP:
                                if (netType == NetType.NONE || netType == NetType.CMWAP) {
                                    invoke(method, key, netType);
                                }
                                break;
                            case NONE :
                                if (netType == NetType.NONE) {
                                    invoke(method, key, netType);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 执行注解下方法
     *
     * @param methodEntity
     * @param key
     * @param netType
     */
    private void invoke(MethodEntity methodEntity, Object key, NetType netType) {
        Method excute = methodEntity.getMethod();
        try {
            excute.invoke(key, netType);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册网络观察者
     *
     * @param register
     */
    public void registerObserver(Object register) {
        List<MethodEntity> methodEntities = netWorkList.get(register);
        if (methodEntities == null) {
            //通过反射获取方法
            methodEntities = findAnnotationMethod(register);
            netWorkList.put(register, methodEntities);
        }
    }

    /**
     * 通过反射获取register所有注解方法
     *
     * @param register
     * @return
     */
    private List<MethodEntity> findAnnotationMethod(Object register) {
        List<MethodEntity> methodEntities = new ArrayList<>();
        Class<?> clazz = register.getClass();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            NetWork netWork = method.getAnnotation(NetWork.class);
            if (netWork == null) {
                continue;
            }

            Type type = method.getGenericReturnType();
            if (!"void".equals(type.toString())) {
                throw new RuntimeException(method.getName() + "The method return value must be null");
            }

            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 1) {
                throw new RuntimeException(method.getName() + "Method parameters have one and only one");
            }
            //没有抛出异常 找到完全符合要求的注解方法
            MethodEntity methodEntity = new MethodEntity(parameterTypes[0], netWork.netType(), method);
            methodEntities.add(methodEntity);
        }
        //优化 反射不停的找父类
        return methodEntities;
    }

    /**
     * 移除网络观察者
     *
     * @param register
     */
    public void unRegisterObserver(Object register) {
        if (netWorkList.isEmpty()) {
            netWorkList.remove(register);
        }
    }

    /**
     * 移除所有网络观察者
     */
    public void unRegisterAllObserver() {
        if (netWorkList.isEmpty()) {
            netWorkList.clear();
        }
        netWorkList = null;
    }

}
