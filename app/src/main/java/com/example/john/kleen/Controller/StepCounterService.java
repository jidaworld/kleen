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

import com.example.john.kleen.DB.CallBack;
import com.example.john.kleen.DB.DBHandler;
import com.example.john.kleen.Model.ProgressObject;
import com.example.john.kleen.Model.Util.BusStation;
import com.example.john.kleen.Model.StepEvent;
import com.example.john.kleen.Model.WeightCalories;
import com.example.john.kleen.R;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.otto.Subscribe;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StepCounterService extends Service implements SensorEventListener {

    private static int steps = 1000;
    private int weight = 0;
    private int goal = 0;
    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";
    private NotificationManagerCompat notificationManagerCompat;
    private int counter = 0;
    private String currentDateString;
    private List<ProgressObject> poList = new ArrayList<>();

    private DBHandler dbH = new DBHandler();


    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        createNotificationChannel();
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (stepCounterSensor != null) {
            sensorManager.registerListener(this, stepCounterSensor, sensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Bork, no step sensor detected", Toast.LENGTH_LONG).show();
        }
        getCurrentDate();
        loadData();
        notificationManagerCompat = NotificationManagerCompat.from(this);
        return START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        steps++;
        ProgressObject po = new ProgressObject(steps,goal,weight);
        BusStation.getBus().post(po);
        Log.i("StepCounter", "Step Counter: " + Integer.toString(steps));
        counter++;
        if (counter >= 5) {
            saveData(po);
            counter = 0;
        }
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

    private void sendNotification() {

        Intent startIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, startIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_steps)
                .setContentTitle("Yay")
                .setContentText("Current steps: " + String.format("%d", steps))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_PROGRESS)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .build();
        notificationManagerCompat.notify(1, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //???
    }

    @Subscribe
    public void receivedData(WeightCalories weightCalories){
        Log.i("FirebaseDebug",weightCalories.toString());
        if(weightCalories.getWeight()!=0) {
            this.weight = weightCalories.getWeight();
            saveData(new ProgressObject(steps,goal,weight));
        }
        if(weightCalories.getGoal()!=0){
            this.goal = weightCalories.getGoal();
            saveData(new ProgressObject(steps,goal,weight));
        }
    }


    public void saveData(ProgressObject po) {
        dbH.sendToDB(po);
    }

    public void loadData() {
        dbH.doInBackground(new CallBack() {
            @Override
            public void callBack(List<ProgressObject> list) {
                for (int i = 0; i < list.size(); i++) {
                    Log.i("DebugFirebase", "whenFinished");
                    Log.i("DebugFirebase", list.get(i).toString());
                }
                if (list.size() > 0) {
                    if (currentDateString.equals(
                            list
                                    .get(list.size() - 1)
                                    .getDate()))
                        steps = list.get(list.size() - 1).getSteps();
                        goal = list.get(list.size()-1).getStep_goal();
                        weight = list.get(list.size()-1).getWeight();
                    ProgressObject po = new ProgressObject(steps,goal,weight);
                    BusStation.getBus().post(po);
                }
                poList = list;
                BusStation.getBus().post(poList);
            }
        });

    }

    public void getCurrentDate() {
        Date currentDate = new Date();
        SimpleDateFormat simpleDate = new SimpleDateFormat("d, M, yyyy");
        currentDateString = simpleDate.format(currentDate);
    }


}
