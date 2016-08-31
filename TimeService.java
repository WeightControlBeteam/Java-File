package com.example.tuananh.manhinhchinh;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.widget.Toast;

public class TimeService extends Service {
    long start = System.currentTimeMillis();
    long end = System.currentTimeMillis();
    BroadcastReceiver receiverON, receiverOFF = null;

    public TimeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
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

                SharedPreferences pre = getSharedPreferences("timesleep", MODE_PRIVATE);
                long timeSleep = pre.getLong("timesleep", 0);

                end = System.currentTimeMillis();
                long diff = end - start;
                if (diff>(30*60*1000)) timeSleep = timeSleep + diff;
                long diffSec = timeSleep / 1000 % 60;
                long diffMin = timeSleep / (60*1000) % 60;
                long diffHour = timeSleep / (60*60*1000);
                String out = diffHour + " hours " + diffMin + " mins " + diffSec + " secs";
                //Toast.makeText(context, out, Toast.LENGTH_LONG).show();


                SharedPreferences.Editor editor = pre.edit();
                editor.putLong("timesleep", timeSleep);
                editor.apply();

            }
        };
        registerReceiver(receiverON, intentFilterON);

        IntentFilter intentFilterOFF = new IntentFilter(Intent.ACTION_SCREEN_OFF);

        receiverOFF = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                start = System.currentTimeMillis();

            }
        };
        registerReceiver(receiverOFF, intentFilterOFF);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
