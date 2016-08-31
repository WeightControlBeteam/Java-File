package com.example.tuananh.manhinhchinh;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by SidaFPT on 8/18/2016.
 */
public class Thongke extends Fragment {
    ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.statistic, container, false);

        final btDatabaseAdapter btdatabase = new btDatabaseAdapter(getActivity());
        btdatabase.open();
        final SQLiteDatabase databt = btdatabase.getMyDatabase();
        Cursor cursor = databt.rawQuery("SELECT * FROM dachon", null);

        listView = (ListView) rootView.findViewById(R.id.list_thongke);
        final ArrayList<HashMap<String, String>> itemlist = new ArrayList<HashMap<String, String>>();
        listStatAdapter listadapter;
        cursor.moveToFirst();
        int i = 0;
        int k = cursor.getCount();
        while ((!cursor.isAfterLast())&&i<k)
        {
            i++;
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ngay", cursor.getString(cursor.getColumnIndex("ngay")));
            map.put("buaan", cursor.getString(cursor.getColumnIndex("buaan")));
            map.put("tenmon", cursor.getString(cursor.getColumnIndex("tenmon")));
            map.put("soluong", cursor.getString(cursor.getColumnIndex("soluong")));
            map.put("kcalo", cursor.getString(cursor.getColumnIndex("kcalo")));
            // adding HashList to ArrayList
            itemlist.add(map);
            cursor.moveToNext();
        }

        listadapter = new listStatAdapter((Activity) getContext(),itemlist);
        listView.setAdapter(listadapter);

        return rootView;
    }
}
