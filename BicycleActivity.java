package com.example.tuananh.manhinhchinh;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

public class BicycleActivity extends AppCompatActivity implements SensorEventListener {
    boolean isGPSEnabled = false;
    boolean isNetWorkEnabled = false;

    private SensorManager sensorManager;
    private LocationManager locationManager;

    public Location loc;
    private TextView count, stepsBicycle, distanceBicycle, carloBicycle, countHide;
    private Button buttonStart, buttonPause, buttonContinue, buttonStop;
    private Chronometer chronometer;
    boolean activityRunning;
    private TextView display;
    private int stepsInSensor = 0;
    private int stepsAtReset;
    int stepsSinceReset = 0;

    Thread runThread;

    double currentLon = 0;
    double currentLat = 0;
    double lastLon = 0;
    double lastLat = 0;
    long timePause = 0;

    double distanceMeters = 0;
    double distanceKm = 0;
    double totalDistances = 0;
    int totalSteps = 0;

    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 10; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 10000;
    private Toolbar toolbar;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bicycle);
        toolbar = (Toolbar) findViewById(R.id.toolbarBicycle);
        setSupportActionBar(toolbar);

        chronometer = (Chronometer)findViewById(R.id.chronometerRun);
        buttonStart = (Button)findViewById(R.id.btStartRun);
        buttonPause = (Button)findViewById(R.id.btPauseRun);
        buttonContinue = (Button)findViewById(R.id.btContinueRun);
        buttonStop = (Button)findViewById(R.id.btStopRun);
        stepsBicycle = (TextView)findViewById(R.id.viewStepBicycle);
        distanceBicycle = (TextView)findViewById(R.id.viewDistanceBicycle);
        countHide = (TextView)findViewById(R.id.countBicycleHide);
        count = (TextView) findViewById(R.id.countBicycle);
        display = (TextView) findViewById(R.id.kilometerBicyle);
        carloBicycle = (TextView)findViewById(R.id.viewCarloBicycle);

        prefs = getSharedPreferences("myprefs", MODE_PRIVATE);
        stepsAtReset = prefs.getInt("myprefs", stepsInSensor);

        count.setVisibility(View.INVISIBLE);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
                activityRunning = true;
                count.setVisibility(View.VISIBLE);
                countHide.setVisibility(View.INVISIBLE);

                stepsBicycle.setText("0 vòng");
                distanceBicycle.setText("0.000 km");
                carloBicycle.setText("Bạn đã đốt cháy " + "0.000" + " cal");
                prefs = getSharedPreferences("myprefs", MODE_PRIVATE);
                stepsAtReset = prefs.getInt("myprefs", stepsInSensor);

                stepsBicycle.setVisibility(View.INVISIBLE);
                distanceBicycle.setVisibility(View.INVISIBLE);
                carloBicycle.setVisibility(View.INVISIBLE);
                buttonStart.setVisibility(View.INVISIBLE);
                buttonPause.setVisibility(View.VISIBLE);
            }
        });

        buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.stop();
                timePause = chronometer.getBase() - SystemClock.elapsedRealtime();
                activityRunning = false;
                onPause();

                buttonPause.setVisibility(View.INVISIBLE);
                buttonStop.setVisibility(View.VISIBLE);
                buttonContinue.setVisibility(View.VISIBLE);
            }
        });

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.setBase(SystemClock.elapsedRealtime() + timePause);
                chronometer.start();
                activityRunning = true;
                onResume();

                buttonPause.setVisibility(View.VISIBLE);
                buttonStop.setVisibility(View.INVISIBLE);
                buttonContinue.setVisibility(View.INVISIBLE);
            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityRunning = false;
                chronometer.stop();
                long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
                long timeInSec = elapsedMillis/1000;

                SharedPreferences pre = getSharedPreferences("user2", MODE_PRIVATE);
                String sex = pre.getString("sex", "no");
                double height = Double.parseDouble(pre.getString("height", "0"));
                double weight = Double.parseDouble(pre.getString("weight", "0"));
                double age = Double.parseDouble(pre.getString("age", "0"));

                double calories = 0;

                if (sex.equalsIgnoreCase("male")){
                    double BMR = (13.75 * weight) + (5 * height) - (6.76 * age) +66;
                    calories = (((BMR/24)*7.5)/3600)*timeInSec;
                }
                else
                if (sex.equalsIgnoreCase("female")){
                    double BMR = (9.56 * weight) + (1.85 * height) - (4.68 * age) +655;
                    calories = (((BMR/24)*7.5)/3600)*timeInSec;
                }

                chronometer.setText("00:00");
                if(stepsSinceReset == 0){
                    stepsBicycle.setText(String.valueOf(0) + " vòng");
                    display.setText(String.valueOf(0.000) + " km");
                }

                prefs = getSharedPreferences("myprefs", MODE_PRIVATE);
                stepsAtReset = prefs.getInt("myprefs", stepsInSensor);

                count.setText(String.valueOf(0) + " vòng");
                stepsBicycle.setText(String.valueOf(stepsSinceReset) + " vòng");
                display.setText("0.000 km");
                distanceBicycle.setText(String.format("%.3f", distanceKm) + " km");
                carloBicycle.setText("Bạn đã đốt cháy " + String.format("%.3f", calories) + " cal");

                stepsBicycle.setVisibility(View.VISIBLE);
                distanceBicycle.setVisibility(View.VISIBLE);
                carloBicycle.setVisibility(View.VISIBLE);
                buttonContinue.setVisibility(View.INVISIBLE);
                buttonStop.setVisibility(View.INVISIBLE);
                buttonStart.setVisibility(View.VISIBLE);
            }
        });

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorWhite)));

        startService(new Intent(BicycleActivity.this, ServiceScreenOff.class));

        sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetWorkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetWorkEnabled) {
            Toast.makeText(BicycleActivity.this, "GPS Disabled!!", Toast.LENGTH_LONG).show();
        } else {

            if (ActivityCompat.checkSelfPermission(BicycleActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(BicycleActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
            Log.d("GPS Enabled", "GPS Enabled");

            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            loc = locationManager.getLastKnownLocation(provider);

            if (loc == null) {
                display.setText(R.string.gps_check);
            } else {
                lastLon = loc.getLongitude();
                lastLat = loc.getLatitude();
            }
        }
    }

    LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {

            final Location loc1;
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            //Get last location
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            location = locationManager.getLastKnownLocation(provider);

            loc1 = location;
            //Request new location
            if (ActivityCompat.checkSelfPermission(BicycleActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(BicycleActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, locationListener);

            runThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    currentLat = loc1.getLatitude();
                    currentLon = loc1.getLongitude();
                    Location locationA = new Location("point A");
                    locationA.setLatitude(lastLat);
                    locationA.setLongitude(lastLon);

                    Location locationB = new Location("point B");
                    locationB.setLatitude(currentLat);
                    locationB.setLongitude(currentLon);
                    distanceMeters = locationA.distanceTo(locationB);

                    distanceKm = distanceMeters / 1000f;
                }
            });
            runThread.start();
            display.setText(String.format("%.3f", distanceKm) + "km");

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Toast.makeText(BicycleActivity.this, "GPS is changed !!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(BicycleActivity.this, "GPS is enabled !!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(BicycleActivity.this, "GPS is disabled !!", Toast.LENGTH_LONG).show();
        }
    };

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

        if (ActivityCompat.checkSelfPermission(BicycleActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(BicycleActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(BicycleActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(BicycleActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(locationListener);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(activityRunning){
            stepsInSensor = Integer.valueOf((int) event.values[0]);
            stepsSinceReset = stepsInSensor - stepsAtReset;
            count.setText(String.valueOf(stepsSinceReset) + " vòng");
        }else{
            event.values[0] = 0;
        }
        prefs = getSharedPreferences("myprefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("myprefs", stepsInSensor);
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