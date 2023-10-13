package com.example.localhostconnect;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class MainSecondApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);

        // Diğer başlangıç işlemlerini burada yapabilirsiniz.
    }
}
