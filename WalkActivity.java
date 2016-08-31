package com.example.tuananh.manhinhchinh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WalkActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;

    public Location loc;
    private TextView count, stepsWalk, distanceWalk, carloWalk;
    boolean activityRunning;
    private int stepsInSensor = 0;
    private int stepsAtReset;
    int stepsSinceReset = 0;
    SharedPreferences prefsSteps;
    SharedPreferences prefsStepsSince;

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk);
        toolbar = (Toolbar) findViewById(R.id.toolbarWalk);
        setSupportActionBar(toolbar);

        stepsWalk = (TextView)findViewById(R.id.viewStepWalk);
        distanceWalk = (TextView)findViewById(R.id.viewDistanceWalk);
        carloWalk = (TextView)findViewById(R.id.viewCarloWalk);

        Date timeLife = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String timeReset = timeFormat.format(timeLife.getTime());
//        if(timeReset == "23:59:00"){
//            onStop();
//            prefsSteps = getSharedPreferences("myprefsSteps", MODE_PRIVATE);
//            stepsAtReset = prefsSteps.getInt("myprefsSteps", stepsInSensor);
//            count.setText(String.valueOf(0) + "/6000");
//        }

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorWhite)));

        startService(new Intent(WalkActivity.this, BroadcastScreenOff.class));

        count = (TextView) findViewById(R.id.countWalk);

        sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(activityRunning = true) {
            Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            if (countSensor != null) {
                sensorManager.registerListener((SensorEventListener) this, countSensor, SensorManager.SENSOR_DELAY_UI);
            } else {
                Toast.makeText(this, "Count sensor not available!", Toast.LENGTH_LONG).show();
            }
        }
   }

    @Override
    protected void onPause() {
        super.onPause();
        activityRunning = false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(activityRunning){
            stepsInSensor = Integer.valueOf((int) event.values[0]);
            stepsSinceReset = stepsInSensor - stepsAtReset;
            count.setText(String.valueOf(stepsSinceReset) + "/6000 bước");
        }else{
            event.values[0] = 0;
        }
        prefsSteps = getSharedPreferences("myprefsSteps", MODE_PRIVATE);
        prefsStepsSince = getSharedPreferences("myprefsStepsSince", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefsSteps.edit();
        editor.putInt("myprefsSteps", stepsInSensor);
        editor.putInt("myprefsStepsSince", stepsSinceReset);
        editor.commit();
        editor.apply();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}