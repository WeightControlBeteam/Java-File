package com.example.tuananh.manhinhchinh;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

public class RunActivity extends AppCompatActivity implements SensorEventListener {
    private static final String MY_PREFS_NAME = "";
    boolean isGPSEnabled = false;
    boolean isNetWorkEnabled = false;

    private SensorManager sensorManager;
    private LocationManager locationManager;

    public Location loc;
    private TextView count, stepsRun, distanceRun, carloRun, countHide;
    private Button buttonStart, buttonPause, buttonContinue, buttonStop;
    private Chronometer chronometer;
    boolean check;
    private TextView display, displayTime;
    private int stepsInSensor = 0;
    private int stepsAtReset = 0;
    int stepsSinceReset = 0;

    Thread runThread;
    static double n = 0;
    double s1 = 0, r1 = 0;

    double currentLon = 0;
    double currentLat = 0;
    double lastLon = 0;
    double lastLat = 0;
    long timePause = 0;

    double distanceMeters = 0;
    double distanceKm = 0;
    double totalDistances = 0;
    int totalSteps = 0;
    boolean pause = true, stop = false;

    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 10; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 10000;
    private Toolbar toolbar;
    SharedPreferences prefsSteps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        check = false;
        setContentView(R.layout.activity_run);
        toolbar = (Toolbar) findViewById(R.id.toolbarRun);
        setSupportActionBar(toolbar);

        chronometer = (Chronometer)findViewById(R.id.chronometerRun);
        buttonStart = (Button)findViewById(R.id.btStartRun);
        buttonPause = (Button)findViewById(R.id.btPauseRun);
        buttonContinue = (Button)findViewById(R.id.btContinueRun);
        buttonStop = (Button)findViewById(R.id.btStopRun);
        stepsRun = (TextView)findViewById(R.id.viewStepRun);
        distanceRun = (TextView)findViewById(R.id.viewDistanceRun);
        carloRun = (TextView)findViewById(R.id.viewCarloRun);
        count = (TextView) findViewById(R.id.countRun);
        countHide = (TextView)findViewById(R.id.countRunHide);
        display = (TextView) findViewById(R.id.kilometerRun);

        check = false;
        prefsSteps = getSharedPreferences("myprefsSteps", MODE_PRIVATE);
        stepsAtReset = prefsSteps.getInt("myprefsSteps", stepsInSensor);
        count.setVisibility(View.INVISIBLE);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
                count.setVisibility(View.VISIBLE);
                countHide.setVisibility(View.INVISIBLE);
                check = true;
                prefsSteps = getSharedPreferences("myprefsSteps", MODE_PRIVATE);
                stepsAtReset = prefsSteps.getInt("myprefsSteps", stepsInSensor);
                stepsRun.setText("0 bước");
                distanceRun.setText("0.000 km");
                carloRun.setText("Bạn đã đốt cháy " + "0.000" + " cal");
                stepsRun.setVisibility(View.INVISIBLE);
                distanceRun.setVisibility(View.INVISIBLE);
                carloRun.setVisibility(View.INVISIBLE);
                buttonStart.setVisibility(View.INVISIBLE);
                buttonPause.setVisibility(View.VISIBLE);
            }
        });

        buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.stop();
                timePause = chronometer.getBase() - SystemClock.elapsedRealtime();
                check = false;
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
                check = true;
                onResume();

                buttonPause.setVisibility(View.VISIBLE);
                buttonStop.setVisibility(View.INVISIBLE);
                buttonContinue.setVisibility(View.INVISIBLE);
            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = false;
                chronometer.stop();
                chronometer.setText("00:00");
                prefsSteps = getSharedPreferences("myprefsSteps", MODE_PRIVATE);
                stepsAtReset = prefsSteps.getInt("myprefsSteps", stepsInSensor);

                count.setText(String.valueOf(0) + " bước");
                stepsRun.setText(String.valueOf(stepsSinceReset) + " bước");
                display.setText(String.valueOf(0) + " km");
                distanceRun.setText(String.format("%.3f", distanceKm) + " km");
                double caloriesRun = 0;
                caloriesRun = stepsSinceReset * 0.063;
                carloRun.setText("Bạn đã đốt cháy " + String.format("%.3f", caloriesRun) + " cal");

                stepsRun.setVisibility(View.VISIBLE);
                distanceRun.setVisibility(View.VISIBLE);
                carloRun.setVisibility(View.VISIBLE);
                buttonContinue.setVisibility(View.INVISIBLE);
                buttonStop.setVisibility(View.INVISIBLE);
                buttonStart.setVisibility(View.VISIBLE);
            }
        });

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorWhite)));

        startService(new Intent(RunActivity.this, ServiceScreenOff.class));

        sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetWorkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetWorkEnabled) {
            Toast.makeText(RunActivity.this, "GPS Enable!!", Toast.LENGTH_LONG).show();
        } else {

            if (ActivityCompat.checkSelfPermission(RunActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(RunActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            if (ActivityCompat.checkSelfPermission(RunActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(RunActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            Toast.makeText(RunActivity.this, "GPS is changed !!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(RunActivity.this, "GPS is enabled !!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(RunActivity.this, "GPS is disabled !!", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if(check = true) {
            Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            if (countSensor != null) {
                sensorManager.registerListener((SensorEventListener) this, countSensor, SensorManager.SENSOR_DELAY_UI);
            } else {
                Toast.makeText(this, "Count sensor not available!", Toast.LENGTH_LONG).show();
            }
        }

        if (ActivityCompat.checkSelfPermission(RunActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(RunActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        if (ActivityCompat.checkSelfPermission(RunActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(RunActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    @Nullable
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(check){
            stepsInSensor = Integer.valueOf((int) event.values[0]);
            stepsSinceReset = stepsInSensor - stepsAtReset;
            count.setText(String.valueOf(stepsSinceReset) + " bước");
        }else{
            event.values[0] = 0;
        }

        prefsSteps = getSharedPreferences("myprefsSteps", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefsSteps.edit();
        editor.putInt("myprefsSteps", stepsInSensor);
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