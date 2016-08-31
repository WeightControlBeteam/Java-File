package com.example.tuananh.manhinhchinh;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by MinhPQ on 8/12/2016.
 */
public class ServiceScreenOff extends Service {
    BroadcastScreenOff broadcastScreenOff = null;
    BroadcastReceiver receiverON, receiverOFF = null;
    private int stepsInSensor = 0;
    private int stepsAtReset = 0;
    int stepsSinceReset = 0;
    private SensorManager sensorManager;
    SharedPreferences prefsSteps;
    SharedPreferences prefsStepsSince;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter intentFilterON = new IntentFilter(Intent.ACTION_SCREEN_ON);
        receiverON = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                prefsSteps = getSharedPreferences("myprefsSteps", MODE_PRIVATE);
                prefsStepsSince = getSharedPreferences("myprefsStepsSince", MODE_PRIVATE);
                stepsAtReset = prefsSteps.getInt("myprefsSteps", 0);
                stepsSinceReset = prefsStepsSince.getInt("myprefsStepsSince", 0);

                sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
                Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
                if (countSensor != null) {
                    sensorManager.registerListener((SensorEventListener) context, countSensor, SensorManager.SENSOR_DELAY_UI);
                } else {
                    Toast.makeText(context, "Count sensor not available!", Toast.LENGTH_LONG).show();
                }
            }
        };
        registerReceiver(receiverON, intentFilterON);

        receiverOFF = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                prefsSteps = getSharedPreferences("myprefsSteps", MODE_PRIVATE);
                prefsStepsSince = getSharedPreferences("myprefsStepsSince", MODE_PRIVATE);
                stepsAtReset = prefsSteps.getInt("myprefsSteps", 0);
                stepsSinceReset = prefsStepsSince.getInt("myprefsStepsSince", 0);

                sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
                Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
                if (countSensor != null) {
                    sensorManager.registerListener((SensorEventListener) context, countSensor, SensorManager.SENSOR_DELAY_UI);
                } else {
                    Toast.makeText(context, "Count sensor not available!", Toast.LENGTH_LONG).show();
                }
            }
        };

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(broadcastScreenOff, filter);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastScreenOff);
    }
}
