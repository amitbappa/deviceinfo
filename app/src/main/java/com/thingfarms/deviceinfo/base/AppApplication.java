package com.thingfarms.deviceinfo.base;

import android.app.Application;
import android.content.Context;


public class AppApplication extends Application {
    private static Context singletonInstance;

    public static synchronized Context getAppContext() {
        if (null == singletonInstance) {
            singletonInstance = new AppApplication();
        }
        return singletonInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singletonInstance = this;
    }
}
