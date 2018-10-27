package com.example.niteshverma.demoweather;

import android.support.multidex.MultiDexApplication;

public class DemoApplication extends MultiDexApplication{

    private static DemoApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public static synchronized DemoApplication get() {
        return application;
    }

}
