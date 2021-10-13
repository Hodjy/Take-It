package com.hod.finalapp.view;

import android.app.Application;
import android.content.Context;

public class ApplicationContext extends Application {
    private static Context mApplicationContext;

    public void onCreate()
    {
        super.onCreate();
        mApplicationContext = getApplicationContext();
    }

    public static Context getAppContext()
    {
        return mApplicationContext;
    }
}
