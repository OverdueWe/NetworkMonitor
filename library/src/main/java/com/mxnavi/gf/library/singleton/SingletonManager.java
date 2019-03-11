package com.mxnavi.gf.library.singleton;

import android.content.Context;

/**
 * 描述 ： 单例管理类
 *
 * @author Mark
 * @date 2019.2.12
 */
public final class SingletonManager {

    private static Context mContext;

    public static void init(Context context) {
        mContext = context.getApplicationContext();
    }

    public static <T> T getService(Class<T> tClass) {
        checkContextValid();
        return ServiceRegistry.getService(mContext, tClass);
    }

    private static void checkContextValid() {
        if (mContext == null) {
            throw new IllegalStateException("must call method [init] first !!!");
        }
    }
}
