package com.example.tuananh.manhinhchinh;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by TUANANH on 17-Aug-16.
 */
public class CustomSportAdapter extends BaseAdapter {

    private Activity activity;
    private int resource;
    private List<Sports> list;

    public CustomSportAdapter(Activity activity, int resource, List<Sports> list) {
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

        final Sports sport = list.get(position);

        if (convertView!=null){
            view = convertView;
        }else {
            LayoutInflater inflater = activity.getLayoutInflater();
            view = inflater.inflate(resource,null);
        }

        ImageView sportIcon = (ImageView)view.findViewById(R.id.sportIcon);
        sportIcon.setImageResource(sport.getImageID());
        final TextView sportName = (TextView)view.findViewById(R.id.sportName);
        sportName.setText(sport.getName());
        TextView sportCalories = (TextView)view.findViewById(R.id.sportCalories);
        sportCalories.setText(sport.getCalories()+" calo");

        final RelativeLayout sportListItem = (RelativeLayout)view.findViewById(R.id.sportItemList);
        sportListItem.setEnabled(sport.isEnable());

        if (!sport.isEnable()) sportListItem.setBackgroundColor(view.getResources().getColor(R.color.colorGray));
        else if(sport.isEnable()) sportListItem.setBackgroundColor(view.getResources().getColor(R.color.colorWhite));

        sportListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(activity, ChoosenSportActivity.class);
                    //intent.putExtra("sport",sport.getName());
                    intent.putExtra("sport",sport);
                    activity.startActivity(intent);

            }
        });


        return view;
    }
}
