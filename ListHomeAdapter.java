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
public class ListHomeAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;

    public ListHomeAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
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
            vi = inflater.inflate(R.layout.list_home_item, null);

        TextView monan = (TextView)vi.findViewById(R.id.text_listdachon_ten); // title
        TextView soluong = (TextView) vi.findViewById(R.id.text_listdachon_sl);
        TextView kcalo = (TextView)vi.findViewById(R.id.text_listdachon_kcalo); //kcalo
/*        TextView view = (TextView)vi.findViewById(R.id.view); // artist name
        TextView date = (TextView)vi.findViewById(R.id.date); // duration
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image
        */
        String link;

        HashMap<String, String> unit = new HashMap<String, String>();
        unit = data.get(position);

        // Setting all values in listview
        monan.setText(unit.get("tenmon"));
        soluong.setText(unit.get("soluong"));
        kcalo.setText(unit.get("kcalo"));

/*        view.setText(unit.get(xmlnew.KEY_VIEW));
        date.setText(unit.get(xmlnew.KEY_DATE));
        imageLoader.DisplayImage(unit.get(xmlnew.KEY_THUMBNAIL), thumb_image);*/
//        link = unit.get(xmlnew.KEY_VIDEO);
        return vi;
    }
}
