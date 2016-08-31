package com.example.tuananh.manhinhchinh;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class SleepActivity extends AppCompatActivity {

    Calendar rightnow = new GregorianCalendar();

    Button btSetTimeSleep;
    Button btSetTimeWakeUp;
    Button btSaveTime;

    TextView txtTimeSleep;
    TextView txtTimeWakeUp;

    int hour_sleep;
    int minute_sleep;
    int hour_wakeup;
    int minute_wakeup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //draw action bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorSleep)));

        showTimePickerDialog();

        txtTimeSleep = (TextView)findViewById(R.id.txtTimeSleep);
        txtTimeWakeUp = (TextView)findViewById(R.id.txtTimeWakeUp);


        btSaveTime = (Button)findViewById(R.id.btSaveTime);
        btSaveTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAlarm();

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences pre = getSharedPreferences("timesleep", MODE_PRIVATE);

        txtTimeSleep.setText(pre.getString("timeGoSleep","00:00"));
        txtTimeWakeUp.setText(pre.getString("timeWakeUp","00:00"));

        long timeSleep = pre.getLong("timesleep", 0);

        pre = getSharedPreferences("user2",MODE_PRIVATE);
        double weight = Double.parseDouble(pre.getString("weight","0"));

        TextView txtSleep = (TextView)findViewById(R.id.txtSleep);
        long diffSec = timeSleep / 1000 % 60;
        long diffMin = timeSleep / (60*1000) % 60;
        long diffHour = timeSleep / (60*60*1000);
        String out = diffHour + " hours " + diffMin + " mins " + diffSec + " secs";
        txtSleep.setText(out);

        double calories = (0.95 * weight * 3.5 * (diffHour*60 + diffMin))/200;
        TextView txtCalories = (TextView)findViewById(R.id.txtCaloriesSleep);
        txtCalories.setText(calories+" cal");
    }

    public void showTimePickerDialog(){
        btSetTimeSleep = (Button)findViewById(R.id.btSetTimeSleep);
        btSetTimeSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(1);

            }
        });

        btSetTimeWakeUp = (Button)findViewById(R.id.btSetTimeWakeUp);
        btSetTimeWakeUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(2);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 1) return new TimePickerDialog(SleepActivity.this , kTimePickerListenerSleep, hour_sleep, minute_sleep, true);
        if (id == 2) return new TimePickerDialog(SleepActivity.this , kTimePickerListenerWakeUp, hour_sleep, minute_sleep, true);

        return null;

    }

    protected TimePickerDialog.OnTimeSetListener kTimePickerListenerSleep = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hour_sleep = hourOfDay;
            minute_sleep = minute;

            if(hour_sleep<10 && minute_sleep<10) txtTimeSleep.setText("0"+hour_sleep + " : " + "0"+minute_sleep);
            if(hour_sleep<10 && minute_sleep>=10) txtTimeSleep.setText("0"+hour_sleep + " : " + minute_sleep);
            if(hour_sleep>=10 && minute_sleep<10) txtTimeSleep.setText(hour_sleep + " : " + "0"+minute_sleep);
            if(hour_sleep>=10 && minute_sleep>=10) txtTimeSleep.setText(hour_sleep + " : " + minute_sleep);
        }
    };

    protected TimePickerDialog.OnTimeSetListener kTimePickerListenerWakeUp = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hour_wakeup = hourOfDay;
            minute_wakeup = minute;

            if(hour_wakeup<10 && minute_wakeup<10) txtTimeWakeUp.setText("0"+hour_wakeup + " : " + "0"+minute_wakeup);
            if(hour_wakeup<10 && minute_wakeup>=10) txtTimeWakeUp.setText("0"+hour_wakeup + " : " + minute_wakeup);
            if(hour_wakeup>=10 && minute_wakeup<10) txtTimeWakeUp.setText(hour_wakeup + " : " + "0"+minute_wakeup);
            if(hour_wakeup>=10 && minute_wakeup>=10) txtTimeWakeUp.setText(hour_wakeup + " : " + minute_wakeup);
        }
    };


    public void getAlarm(){

        SharedPreferences pre = getSharedPreferences("timesleep", MODE_PRIVATE);

        SharedPreferences.Editor editor = pre.edit();
        editor.putLong("timesleep", 0);
        editor.apply();

        rightnow.setTimeInMillis(System.currentTimeMillis());

        Calendar timeCalON = new GregorianCalendar();
        timeCalON.set(Calendar.HOUR_OF_DAY, hour_sleep);
        timeCalON.set(Calendar.MINUTE, minute_sleep);
        timeCalON.set(Calendar.SECOND, 0);


        Intent intentON = new Intent(this, MyBroadcastReceiverOn.class);

        PendingIntent pendingIntentON = PendingIntent.getBroadcast(getApplicationContext(), 1, intentON, 0);
        AlarmManager alarmManagerON = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManagerON.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, timeCalON.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntentON);


        Calendar timeCalOFF = new GregorianCalendar();
        timeCalOFF.set(Calendar.HOUR_OF_DAY, hour_wakeup);
        timeCalOFF.set(Calendar.MINUTE, minute_wakeup );
        timeCalOFF.set(Calendar.SECOND, 0);


        Intent intentOFF = new Intent(this, MyBroadcastReceiverOff.class);

        PendingIntent pendingIntentOFF = PendingIntent.getBroadcast(getApplicationContext(), 1, intentOFF, 0);
        AlarmManager alarmManagerOFF = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManagerOFF.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, timeCalOFF.getTimeInMillis(),AlarmManager.INTERVAL_DAY ,pendingIntentOFF);

        editor.putString("timeGoSleep", txtTimeSleep.getText().toString());
        editor.putString("timeWakeUp", txtTimeWakeUp.getText().toString());
        editor.apply();

        Toast.makeText(getApplicationContext(), "Cài thời gian ngủ thành công",Toast.LENGTH_LONG).show();
    }

}
