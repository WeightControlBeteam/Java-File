package com.example.tuananh.manhinhchinh;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by TUANANH on 18-May-16.
 */
public class Calories_Fragment extends android.support.v4.app.Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calories_fragment_layout,container,false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
