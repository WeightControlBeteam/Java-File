package com.example.tuananh.manhinhchinh;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by TUANANH on 30-Jul-16.
 */
public class SleepView extends LinearLayout {



    public SleepView(Context context, AttributeSet attributeSet){
        super(context);

        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sleep_layout, this, true);
    }



}
