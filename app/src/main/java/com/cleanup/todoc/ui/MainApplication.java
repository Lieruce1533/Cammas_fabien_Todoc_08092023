package com.cleanup.todoc.ui;

import android.app.Application;

public class MainApplication extends Application {

    public static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

    }

    public static Application getApplication() {
        if (application == null) {
            throw new IllegalStateException("MainApplication is not properly initialized.");
        }
        return application;
    }
}