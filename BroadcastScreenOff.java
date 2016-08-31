package com.example.tuananh.manhinhchinh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by MinhPQ on 8/12/2016.
 */
public class BroadcastScreenOff extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent myIntent = new Intent(context, ServiceScreenOff.class);
        context.startActivity(myIntent);
    }
}
