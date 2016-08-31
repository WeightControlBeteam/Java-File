package com.example.tuananh.manhinhchinh;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;

import java.util.List;

/**
 * Created by TUANANH on 19-May-16.
 */
public class CustomButtonAdapter extends BaseAdapter {

    private Activity activity;
    private int resource;
    private List<Buttons> list;

    public CustomButtonAdapter(Activity activity, int resource, List<Buttons> list) {
        this.activity = activity;
        this.resource = resource;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        final Buttons button = list.get(position);


        if (convertView!=null){
            view = convertView;
        }else {
            LayoutInflater inflater = activity.getLayoutInflater();
            view = inflater.inflate(resource,null);
        }

        ImageButton imageButton = (ImageButton)view.findViewById(R.id.imageButton);
        imageButton.setImageResource(button.getImageID());

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button.getName().equalsIgnoreCase("sleep")) {
                    Intent intent = new Intent(activity, SleepActivity.class);
                    activity.startActivity(intent);
                }

                if (button.getName().equalsIgnoreCase("weight")) {
                    Intent intent = new Intent(activity, WeightAndHeightActivity.class);
                    activity.startActivity(intent);
                }

                if (button.getName().equalsIgnoreCase("calendar")) {
                    Intent intent = new Intent(activity, CalendarActivity.class);
                    activity.startActivity(intent);
                }
                if (button.getName().equalsIgnoreCase("sport")) {
                    Intent intent = new Intent(activity, SportsActivity.class);
                    activity.startActivity(intent);
                }
                if (button.getName().equalsIgnoreCase("eat")) {
                    Intent intent = new Intent(activity, EatActivity.class);
                    activity.startActivity(intent);
                }
                if (button.getName().equalsIgnoreCase("run")) {
                    Intent intent = new Intent(activity, RunActivity.class);
                    activity.startActivity(intent);
                }
                if (button.getName().equalsIgnoreCase("bicycle")) {
                    Intent intent = new Intent(activity, BicycleActivity.class);
                    activity.startActivity(intent);
                }
                if (button.getName().equalsIgnoreCase("walk")) {
                    Intent intent = new Intent(activity, WalkActivity.class);
                    activity.startActivity(intent);
                }

            }
        });
        return view;
    }
}
