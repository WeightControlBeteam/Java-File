package com.example.tuananh.manhinhchinh;

import android.animation.Animator;
import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by TUANANH on 18-May-16.
 */
public class CanNang_Fragment extends android.support.v4.app.Fragment {

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
        View view = inflater.inflate(R.layout.cannang_fragment_layout,container,false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //get calories eating for homescreen

        Calendar c = Calendar.getInstance();
        String mYear = String.valueOf(c.get(Calendar.YEAR));
        String mMonth = String.valueOf(c.get(Calendar.MONTH)+1);
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        String date = String.valueOf(mDay + '/' + mMonth + '/' + mYear);

        final btDatabaseAdapter btdatabase = new btDatabaseAdapter(getActivity());
        btdatabase.open();
        final SQLiteDatabase databt = btdatabase.getMyDatabase();

//        Cursor checkSang = databt.rawQuery("SELECT kcalo FROM dachon WHERE buaan = 'Sáng'", null);
//        Cursor checkTrua = databt.rawQuery("SELECT kcalo FROM dachon WHERE buaan = 'Trưa'", null);
//        Cursor checkToi = databt.rawQuery("SELECT kcalo FROM dachon WHERE buaan = 'Tối'",null);

//        Cursor cursorS = databt.rawQuery("SELECT * FROM luachon WHERE buaan = 'Sáng'",null);

        double caloriesSang = 0;
        double caloriesTrua = 0;
        double caloriesToi = 0;

//        while (checkSang.moveToNext()){
//            double calo = Double.parseDouble(checkSang.getString(0));
//            caloriesSang = caloriesSang + calo;
//        }
//        while (checkTrua.moveToNext()){
//            double calo = Double.parseDouble(checkTrua.getString(0));
//            caloriesTrua = caloriesTrua + calo;
//        }
//        while (checkToi.moveToNext()){
//            double calo = Double.parseDouble(checkToi.getString(0));
//            caloriesToi = caloriesToi + calo;
//        }
//
//        checkSang.close();
//        checkTrua.close();
//        checkToi.close();
        btdatabase.close();

        TextView txtCaloriesSang = (TextView)getView().findViewById(R.id.txtCaloriesSang);
        TextView txtCaloriesTrua = (TextView)getView().findViewById(R.id.txtCaloriesTrua);
        TextView txtCaloriesToi = (TextView)getView().findViewById(R.id.txtCaloriesToi);

        txtCaloriesSang.setText(caloriesSang+"");
        txtCaloriesTrua.setText(caloriesTrua+"");
        txtCaloriesToi.setText(caloriesToi+"");

        TextView txtCaloriesTong = (TextView)getView().findViewById(R.id.txtCaloriesTong);
        txtCaloriesTong.setText(caloriesSang+caloriesTrua+caloriesToi+"");
    }
}
