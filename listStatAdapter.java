package com.example.tuananh.manhinhchinh;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by SidaFPT on 7/30/2016.
 */
public class listStatAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;

    public listStatAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.statistic_item, null);

        TextView ngaythang = (TextView)vi.findViewById(R.id.txt_stat_ngay); // title
        TextView buaan = (TextView)vi.findViewById(R.id.txt_stat_bua);
        TextView tenmonan = (TextView)vi.findViewById(R.id.txt_stat_tenmon);
        TextView luong = (TextView)vi.findViewById(R.id.txt_stat_soluong);
        TextView calori = (TextView)vi.findViewById(R.id.txt_stat_calo);
//        final TextView kcalo = (TextView)vi.findViewById(R.id.textKcalo); //kcalo
//        final EditText inputTr = (EditText) vi.findViewById(R.id.inputTrua); //soluong
/*        TextView view = (TextView)vi.findViewById(R.id.view); // artist name
        TextView date = (TextView)vi.findViewById(R.id.date); // duration
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image
        */
//        String link;

        HashMap<String, String> unit = new HashMap<String, String>();
        unit = data.get(position);

        // Setting all values in listview
        ngaythang.setText(unit.get("ngay"));
        buaan.setText(unit.get("buaan"));
        tenmonan.setText(unit.get("tenmon"));
        luong.setText(unit.get("soluong"));
        calori.setText(unit.get("kcalo"));


        //display calo each food
//        final int listCaloGoc = Integer.parseInt(unit.get("calo"));
//        final HashMap<String, String> finalUnit = unit;


/*        view.setText(unit.get(xmlnew.KEY_VIEW));
        date.setText(unit.get(xmlnew.KEY_DATE));
        imageLoader.DisplayImage(unit.get(xmlnew.KEY_THUMBNAIL), thumb_image);*/
//        link = unit.get(xmlnew.KEY_VIDEO);
        return vi;
    }

    public View getRowView(int position, View convertView, ViewGroup parent) {
        return LayoutInflater.from(activity).inflate(R.layout.list_tr_item, parent, false);
    }
}
