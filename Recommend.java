package com.example.tuananh.manhinhchinh;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by SidaFPT on 8/18/2016.
 */
public class Recommend extends Fragment {
    static TextView ngay,gio,sang, trua_chinh,trua_phu, trua_canh, trua_rau, toi_chinh, toi_phu, toi_canh, toi_rau;
    static String ngaythang;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View recommendView = inflater.inflate(R.layout.select, container, false);

        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_WEEK);
        int sogio = c.get(Calendar.HOUR_OF_DAY);
        String mYear = String.valueOf(c.get(Calendar.YEAR));
        String mMonth = String.valueOf(c.get(Calendar.MONTH)+1);
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        int mDate = c.get(Calendar.DATE);
        String songay = String.valueOf(day);
        //ketnoi data
        btDatabaseAdapter btdatabase = new btDatabaseAdapter(getActivity());
        btdatabase.open();
        SQLiteDatabase databt = btdatabase.getMyDatabase();

        //define items
        ngay = (TextView) recommendView.findViewById(R.id.txt_goiy_ngay);
        sang = (TextView) recommendView.findViewById(R.id.txt_goiy_monsang);
        trua_chinh = (TextView) recommendView.findViewById(R.id.txt_goiy_tr_chinh);
        trua_phu = (TextView) recommendView.findViewById(R.id.txt_goiy_tr_phu);
        trua_canh = (TextView) recommendView.findViewById(R.id.txt_goiy_tr_canh);
        trua_rau = (TextView) recommendView.findViewById(R.id.txt_goiy_tr_rau);
        toi_chinh = (TextView) recommendView.findViewById(R.id.txt_goiy_t_chinh);
        toi_phu = (TextView) recommendView.findViewById(R.id.txt_goiy_t_phu);
        toi_canh = (TextView) recommendView.findViewById(R.id.txt_goiy_t_canh);
        toi_rau = (TextView) recommendView.findViewById(R.id.txt_goiy_t_rau);
        
        //defile value
        ngaythang = mDay+"/"+mMonth+"/"+mYear;
        ngay.setText(ngaythang);

        Cursor cursorS = databt.rawQuery("SELECT * FROM sang WHERE id = '"+songay+"'",null);
        if(cursorS.moveToFirst()) {
            sang.setText(cursorS.getString(cursorS.getColumnIndex("tenmon")));
        }

        Cursor cursorTr = databt.rawQuery("SELECT * FROM trua WHERE id = '"+songay+"'",null);
        if(cursorTr.moveToFirst()) {
            trua_chinh.setText(cursorTr.getString(cursorTr.getColumnIndex("monchinh")));
            trua_phu.setText(cursorTr.getString(cursorTr.getColumnIndex("monphu")));
            trua_canh.setText(cursorTr.getString(cursorTr.getColumnIndex("canh")));
            trua_rau.setText(cursorTr.getString(cursorTr.getColumnIndex("rau")));
            if(cursorTr.getString(cursorTr.getColumnIndex("monphu"))==null){
                trua_phu.setVisibility(View.GONE);
            }
            if(cursorTr.getString(cursorTr.getColumnIndex("canh"))==null){
                trua_canh.setVisibility(View.GONE);
            }
        }

        Cursor cursorT = databt.rawQuery("SELECT * FROM toi WHERE id = '"+songay+"'",null);
        if(cursorT.moveToFirst()) {
            toi_chinh.setText(cursorT.getString(cursorT.getColumnIndex("monchinh")));
            toi_phu.setText(cursorT.getString(cursorT.getColumnIndex("monphu")));
            toi_canh.setText(cursorT.getString(cursorT.getColumnIndex("canh")));
            toi_rau.setText(cursorT.getString(cursorT.getColumnIndex("rau")));
        }

        return recommendView;
    }
}
