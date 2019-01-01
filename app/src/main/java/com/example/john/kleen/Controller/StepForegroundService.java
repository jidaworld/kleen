package com.example.john.kleen.Controller;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

public class StepForegroundService extends Application {
    public static final String CHANNEL_ID = "StepForegroundService";

    @Override
    public void onCreate(){
        super.onCreate();
        createNotChannel();
    }

    private void createNotChannel(){
        NotificationChannel nChannel = new NotificationChannel(CHANNEL_ID, "Steps: ", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager nManager = getSystemService(NotificationManager.class);
        nManager.createNotificationChannel(nChannel);
    }



}
