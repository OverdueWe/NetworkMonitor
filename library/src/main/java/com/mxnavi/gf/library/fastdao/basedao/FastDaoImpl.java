package com.mxnavi.gf.library.fastdao.basedao;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mxnavi.gf.library.fastdao.annotation.Delete;
import com.mxnavi.gf.library.fastdao.annotation.Insert;
import com.mxnavi.gf.library.fastdao.annotation.Query;
import com.mxnavi.gf.library.fastdao.annotation.Update;
import com.mxnavi.gf.library.fastdao.bean.MethodEntity;
import com.mxnavi.gf.library.fastdao.parser.MethodParser;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 描述 ：数据库操作类
 *
 * @param <T>
 * @author Mark
 * @date 2019.04.15
 */
public class FastDaoImpl<T> extends BaseDaoImpl<T> {

    private static final String TAG = "FastDaoImpl";

    /**
     * 数据库类
     */
    private Class daoClazz;

    /**
     * 将传入的实例进行解析 得到注解方法 用于支持自定义SQL
     */
    private HashMap<String, List<MethodEntity>> methodMap;

    /**
     * 初始化
     *
     * @param tClass
     * @param sqLiteDatabase
     */
    public final <V extends IBaseDao> boolean init(Class<T> tClass, Class<V> vClass, SQLiteDatabase sqLiteDatabase) {
        daoClazz = vClass;
        createMethodMap();
        return super.init(tClass, sqLiteDatabase);
    }

    /**
     * 解析方法
     *
     * @param proxy  调用该方法的实例
     * @param method 方法体
     * @param args   参数列表
     * @return
     */
    public Object invoke(Object proxy, Method method, Object... args) {
        if (method.isAnnotationPresent(Insert.class)) {
            return insert(method, args);
        } else if (method.isAnnotationPresent(Delete.class)) {
            return delete(args);
        } else if (method.isAnnotationPresent(Update.class)) {
            return update(args);
        } else if (method.isAnnotationPresent(Query.class)) {
            return query(args);
        }
        return null;
    }

    /**
     * 将传入的实例进行解析 得到注解方法 用于支持自定义SQL
     */
    private void createMethodMap() {
        methodMap = new HashMap<>();
        Method[] methods = daoClazz.getDeclaredMethods();
        for (Method method : methods) {
            MethodEntity entity = MethodParser.paser(method);
            if (entity.isValid()) {
                if (methodMap.get(entity.getType()) == null) {
                    methodMap.put(entity.getType(), new ArrayList<MethodEntity>());
                }
                methodMap.get(entity.getType()).add(entity);
            }
        }
    }

    /**
     * 增加数据
     *
     * @param args
     * @return
     */
    private Object insert(Method method, Object... args) {
        MethodEntity entity = null;
        for (MethodEntity methodEntity : methodMap.get(Insert.class.getName())) {
            if (method.toString().equals(methodEntity.getName())) {
                entity = methodEntity;
            }
        }
        try {
            return entity.getReturnType().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "insert exception : " + e.toString());
        }
        return null;
    }

    /**
     * 删除数据
     *
     * @param args
     * @return
     */
    private Object delete(Object... args) {
        MethodEntity entity = null;
        try {
            return Class.forName(entity.getName()).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "delete exception : " + e.toString());
        }
        return null;
    }

    /**
     * 更改数据
     *
     * @param args
     * @return
     */
    private Object update(Object... args) {
        MethodEntity entity = null;
        try {
            return Class.forName(entity.getName()).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "update exception : " + e.toString());
        }
        return null;
    }

    /**
     * 查詢数据
     *
     * @param args
     * @return
     */
    private Object query(Object... args) {
        MethodEntity entity = null;
        try {
            return Class.forName(entity.getName()).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "query exception : " + e.toString());
        }
        return null;
    }


}
