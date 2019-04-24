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

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "time 1 : " + System.currentTimeMillis());

        UserInfoDao baseDao = SingletonManager.getService(FastDaoFactoty.class)
                .create(UserInfo.class,UserInfoDao.class);
        for (int i = 0; i < 10; i++) {
            UserInfo userInfo = new UserInfo();
            userInfo.setName("guofeng");
            userInfo.setPassword(i);
            baseDao.insert(userInfo);
        }

        List<UserInfo> userInfos = null;
        userInfos = baseDao.query(null, null);
        baseDao.delete(null, null);
        userInfos = baseDao.query(null, null);
        Log.d(TAG, "time 2 : " + System.currentTimeMillis());
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
        Log.d(TAG, "autoNet : " + netType.toString());
    }

    @NetWork(netType = NetType.WIFI)
    public void wifiNet(NetType netType) {
        Log.d(TAG, "wifiNet : " + netType.toString());
    }
}