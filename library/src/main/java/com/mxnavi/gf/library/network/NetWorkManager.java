package com.mxnavi.gf.library.network;

import android.content.Context;
import android.content.IntentFilter;

import com.mxnavi.gf.library.network.type.NetType;
import com.mxnavi.gf.library.network.utils.NetConstant;
import com.mxnavi.gf.library.network.utils.NetWorkUtil;

/**
 * 描述 ： 网络状态管理类
 *
 * @author Mark
 * @date 2019.03.11
 */
public class NetWorkManager {

    public static final String TAG = "NetWorkManager";

    private NetWorkReceiver netWorkReceiver;

    private Context context;

    public NetWorkManager(Context context) {

        this.context = context;

        netWorkReceiver = new NetWorkReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(NetConstant.ACTION);
        context.registerReceiver(netWorkReceiver, filter);
    }

    /**
     * 注册网络观察者
     * @param register
     */
    public void registerObserver(Object register) {
        netWorkReceiver.registerObserver(register);
    }

    /**
     * 移除网络观察者
     * @param register
     */
    public void unRegisterObserver(Object register) {
        netWorkReceiver.unRegisterObserver(register);
    }

    /**
     * 移除所有网络观察者
     */
    public void unRegisterAllObserver() {
        netWorkReceiver.unRegisterAllObserver();
    }

    /**
     * 是否有网络
     * @return
     */
    public boolean hasNetWork() {
        return NetWorkUtil.isNetWorkAvailable(context);
    }

    /**
     * 获取网络类型
     * @return
     */
    public NetType getNetType() {
        return NetWorkUtil.getNetType(context);
    }

}
