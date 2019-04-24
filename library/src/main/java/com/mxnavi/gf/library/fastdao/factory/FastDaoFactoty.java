package com.mxnavi.gf.library.fastdao.factory;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.mxnavi.gf.library.fastdao.basedao.BaseDaoImpl;
import com.mxnavi.gf.library.fastdao.basedao.FastDaoImpl;
import com.mxnavi.gf.library.fastdao.basedao.IBaseDao;
import com.mxnavi.gf.library.fastdao.utils.FastDaoConstant;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;

/**
 * 描述 ：数据库工厂类
 *
 * @author Mark
 * @date 2019.04.15
 */
public class FastDaoFactoty {

    private static final String TAG = "FastDaoFactoty";

    private SQLiteDatabase sqLiteDatabase;

    private HashMap<String, IBaseDao> fastDaoCache;

    public FastDaoFactoty() {
        fastDaoCache = new HashMap<>();
        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(
                Environment.getExternalStorageDirectory().getAbsolutePath()
                        + File.separator + FastDaoConstant.FASTDAO_PATH, null);
    }

    /**
     * 根据数据类型创建对应的数据库
     *
     * @param entityClass
     * @param <T>
     * @return
     */
    public <T> IBaseDao create(Class<T> entityClass) {
        if (entityClass == null) {
            throw new IllegalArgumentException("Entity must not be null");
        }

        if (sqLiteDatabase == null) {
            throw new IllegalStateException("SQLiteDatabase is init faild");
        }

        try {
            if (!fastDaoCache.containsKey(entityClass.getName())) {
                synchronized (fastDaoCache) {
                    BaseDaoImpl baseDao = BaseDaoImpl.class.newInstance();
                    baseDao.init(entityClass, sqLiteDatabase);
                    fastDaoCache.put(entityClass.getName(), baseDao);
                }
            }
            return fastDaoCache.get(entityClass.getName());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据数据类型创建对应的数据库
     *
     * @param entityClass
     * @param <T>
     * @return
     */
    public <T, V extends BaseDaoImpl> V create(Class<T> entityClass, Class<V> daoClass) {
        if (entityClass == null) {
            throw new IllegalArgumentException("Entity must not be null");
        }

        if (sqLiteDatabase == null) {
            throw new IllegalStateException("SQLiteDatabase is init faild");
        }

        try {
            if (!fastDaoCache.containsKey(entityClass.getName())) {
                synchronized (fastDaoCache) {
                    BaseDaoImpl baseDao = daoClass.newInstance();
                    baseDao.init(entityClass, sqLiteDatabase);
                    fastDaoCache.put(daoClass.getName(), baseDao);
                }
            }
            return (V) fastDaoCache.get(daoClass.getName());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

//    /**
//     * 创建数据库代理类
//     *
//     * @param entityClass
//     * @param daoClass
//     * @param <T>
//     * @param <V>
//     * @return
//     */
//    public <T, V> V create(Class<T> entityClass, final Class<V> daoClass) {
//        if (entityClass == null) {
//            throw new IllegalArgumentException("Entity must not be null");
//        }
//
//        if (daoClass == null) {
//            throw new IllegalArgumentException("DaoClass must not be null");
//        }
//
//        if (sqLiteDatabase == null) {
//            throw new IllegalStateException("SQLiteDatabase is init faild");
//        }
//
//        try {
//            if (!fastDaoCache.containsKey(daoClass.getName())) {
//                synchronized (fastDaoCache) {
//                    FastDaoImpl fastDao = FastDaoImpl.class.newInstance();
//                    fastDao.init(entityClass, daoClass, sqLiteDatabase);
//                    fastDaoCache.put(daoClass.getName(), fastDao);
//                }
//            }
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        }
//
//        return (V) Proxy.newProxyInstance(daoClass.getClassLoader(), new Class<?>[]{daoClass},
//                new InvocationHandler() {
//                    @Override
//                    public Object invoke(Object proxy, Method method, Object... args)
//                            throws Throwable {
//                        // If the method is a method from Object then defer to normal invocation.
//                        if (method.getDeclaringClass() == Object.class) {
//                            Log.d(TAG, "defer to normal invocation");
//                            return method.invoke(this, args);
//                        }
//                        return ((FastDaoImpl) fastDaoCache.get(daoClass.getName()))
//                                .invoke(proxy, method, args);
//                    }
//                });
//
//    }

    /**
     * 关闭数据库
     */
    public void close() {
        Log.d(TAG,"close sqLiteDatabase");
        if (sqLiteDatabase.isOpen()) {
            sqLiteDatabase.close();
        }

        if (fastDaoCache != null) {
            fastDaoCache.clear();
        }
    }
}
