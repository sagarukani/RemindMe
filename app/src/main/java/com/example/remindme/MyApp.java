package com.example.remindme;

import android.app.Application;

import androidx.lifecycle.LifecycleObserver;

import com.google.firebase.FirebaseApp;

public class MyApp extends Application implements LifecycleObserver {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
