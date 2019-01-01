package com.example.john.kleen.Controller;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.john.kleen.Model.ProgressObject;
import com.example.john.kleen.Model.Util.BusStation;
import com.example.john.kleen.Model.StepEvent;
import com.example.john.kleen.R;

import java.util.ArrayList;

public class StepCounterService extends Service implements SensorEventListener {

    private static int steps;
    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";
    private NotificationManagerCompat notificationManagerCompat;
    private ArrayList<ProgressObject> list = new ArrayList<>();

    private String save = "saveName";

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId){
        createNotificationChannel();
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(stepCounterSensor!=null){
            sensorManager.registerListener(this, stepCounterSensor, sensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Bork, no step sensor detected", Toast.LENGTH_LONG).show();
        }

        notificationManagerCompat = NotificationManagerCompat.from(this);

        return START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        steps = (int) event.values[0];
        BusStation.getBus().post(new StepEvent(steps));
        Log.i("StepCounter","Step Counter: " + Integer.toString(steps));
        saveData();
        sendNotification();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = getSystemService(NotificationManager.class);

            NotificationChannel channelOne = new NotificationChannel(CHANNEL_1_ID, "Step Goal", NotificationManager.IMPORTANCE_HIGH);
            channelOne.setDescription("Shows Step Goal");
            notificationManager.createNotificationChannel(channelOne);

            NotificationChannel channelTwo = new NotificationChannel(CHANNEL_2_ID, "Step Goal", NotificationManager.IMPORTANCE_HIGH);
            channelTwo.setDescription("Shows current steps");
            notificationManager.createNotificationChannel(channelTwo);

            Intent startIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, startIntent, 0);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.ic_steps)
                    .setContentTitle("Steps")
                    .setContentText("Current steps: " + String.format("%d", steps))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setContentIntent(pendingIntent)
                    .build();

            startForeground(1, notification);

        }
    }

    private void sendNotification(){

        Intent startIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, startIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_steps)
                .setContentTitle("Yay")
                .setContentText("Current steps: " + String.format("%d", steps))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_PROGRESS)
                .setContentIntent(pendingIntent)
                .build();
        notificationManagerCompat.notify(1,notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //???
    }

    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences(save, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Log.i("StepCounter","Saved Data");
        editor.putInt("steps",steps);
        editor.apply();
    }



}
