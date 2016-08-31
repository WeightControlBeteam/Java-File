package com.example.tuananh.manhinhchinh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by TUANANH on 31-Jul-16.
 */
public class MyBroadcastReceiverOn extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //Toast.makeText(context, "Start....", Toast.LENGTH_LONG).show();
        Intent myIntent = new Intent(context, TimeService.class);

        context.startService(myIntent);


    }
}
