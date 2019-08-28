package com.guestlogix;

import android.app.Application;

public class BaseApplication extends Application {

    private static BaseApplication mInstance;

    public static synchronized BaseApplication getInstance() {
        if (mInstance == null) mInstance = new BaseApplication();
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

}
