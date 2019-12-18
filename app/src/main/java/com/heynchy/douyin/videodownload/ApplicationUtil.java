package com.heynchy.douyin.videodownload;

import android.app.Application;
import android.content.Context;

/**
 * ApplicationUtil
 *
 * @author CHY  2017/1/17.
 */
public class ApplicationUtil extends Application {
    private static ApplicationUtil instance;
    private static Context mApplicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationContext = this;
    }

    public static Context getAppContext() {
        return mApplicationContext;
    }

}
