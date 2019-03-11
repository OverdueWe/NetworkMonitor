package com.mxnavi.gf.networkdemo;

import android.app.Application;

import com.mxnavi.gf.library.singleton.SingletonManager;

public class MxApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SingletonManager.init(this);
    }
}
