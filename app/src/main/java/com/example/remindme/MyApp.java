package com.example.remindme;

import android.app.Application;

import androidx.lifecycle.LifecycleObserver;

import com.google.firebase.FirebaseApp;

/**
 * Application class
 */
public class MyApp extends Application implements LifecycleObserver {

    private MyPreferences myPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        myPreferences = MyPreferences.getPreferences(this);
    }

    public MyPreferences getPreferences(){
        return myPreferences;
    }
}
