package com.mxnavi.gf.networkdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.mxnavi.gf.library.fastdao.basedao.IBaseDao;
import com.mxnavi.gf.library.fastdao.factory.FastDaoFactoty;
import com.mxnavi.gf.library.network.NetWorkManager;
import com.mxnavi.gf.library.network.annotation.NetWork;
import com.mxnavi.gf.library.network.type.NetType;
import com.mxnavi.gf.library.singleton.SingletonManager;
import com.mxnavi.gf.networkdemo.bean.UserInfo;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserInfo userInfo = new UserInfo();
        userInfo.setName("guofeng");
        userInfo.setPassword(14384798);

        Log.d(TAG,"time 1 : " + System.currentTimeMillis());
        for (int i = 0; i < 100; i++) {
            IBaseDao baseDao = SingletonManager.getService(FastDaoFactoty.class).createDao(UserInfo.class);
            baseDao.insert(userInfo);
        }
        Log.d(TAG,"time 2 : " + System.currentTimeMillis());
    }

    @Override
    protected void onStart() {
        super.onStart();
        SingletonManager.getService(NetWorkManager.class).registerObserver(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SingletonManager.getService(NetWorkManager.class).unRegisterObserver(this);
    }

    @NetWork(netType = NetType.AUTO)
    public void autoNet(NetType netType) {
        Log.d(TAG,"autoNet : " + netType.toString());
    }

    @NetWork(netType = NetType.WIFI)
    public void wifiNet(NetType netType) {
        Log.d(TAG,"wifiNet : " + netType.toString());
    }
}