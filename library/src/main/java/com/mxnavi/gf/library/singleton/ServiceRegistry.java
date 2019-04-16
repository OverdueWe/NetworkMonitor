package com.mxnavi.gf.library.singleton;

import android.content.Context;

import com.mxnavi.gf.library.fastdao.factory.FastDaoFactoty;
import com.mxnavi.gf.library.network.NetWorkManager;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述 ： 单例容器类
 *
 * @author Mark
 * @date 2019.2.12
 */
final class ServiceRegistry {

    private static final String TAG = "ServiceRegistry";

    private static final Map<String, ServiceFetcher> SERVICE_FETCHERS = new HashMap<>();

    private static <T> void registerService(String name, ServiceFetcher<T> fetcher) {
        SERVICE_FETCHERS.put(name, fetcher);
    }

    static {
        registerService(NetWorkManager.class.getName(), new ServiceFetcher<NetWorkManager>() {
            @Override
            public NetWorkManager createService(Context context) {
                return new NetWorkManager(context);
            }
        });

        registerService(FastDaoFactoty.class.getName(), new ServiceFetcher<FastDaoFactoty>() {
            @Override
            public FastDaoFactoty createService(Context context) {
                return new FastDaoFactoty();
            }
        });
    }

    public static <T> T getService(Context context, Class<T> tClass) {
        final ServiceFetcher fetcher = SERVICE_FETCHERS.get(tClass.getName());
        return fetcher != null ? (T) fetcher.getService(context) : null;
    }

    private static abstract class ServiceFetcher<T> {
        private T mService;

        public final T getService(Context context) {
            synchronized (ServiceFetcher.this) {
                if (mService == null) {
                    mService = createService(context);
                }
                return mService;
            }
        }
        public abstract T createService(Context context);
    }
}