package com.example.i_to_sky.functionproject.application;

import android.app.Application;

/**
 * Created by weiyupei on 2018/11/30.
 */

public class AppApplication extends Application {

    private static AppApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static AppApplication getInstance() {
        return mInstance;
    }

}
