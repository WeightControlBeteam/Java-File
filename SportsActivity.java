package com.example.tuananh.manhinhchinh;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SportsActivity extends AppCompatActivity {

    DBOpen dbOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //draw actionbar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorWhite)));

        //setup Sports database
//        this.deleteDatabase("sports");
        dbOpen = new DBOpen(this, "sports", null, 1);
        SQLiteDatabase db = dbOpen.getReadableDatabase();
        Cursor checkDB = db.rawQuery("SELECT * FROM SPORTS ", new String[]{});
        if (checkDB.getCount()==0){
            db = dbOpen.getWritableDatabase();
            db.execSQL("INSERT INTO SPORTS (name, imageID, calories, imageBodyID, enable, constSport) values ('Bóng đá', "+R.drawable.icon_soccer+", 0, "+R.drawable.body_soccer+", 1,"+ 10.0 +");");
            db.execSQL("INSERT INTO SPORTS (name, imageID, calories, imageBodyID, enable, constSport) values ('Cầu lông', "+R.drawable.icon_badminton+", 0, "+R.drawable.body_badminton+", 1,"+ 7.0 +");");
            db.execSQL("INSERT INTO SPORTS (name, imageID, calories, imageBodyID, enable, constSport) values ('Bóng bàn', "+R.drawable.icon_tabletennis+", 0, "+R.drawable.body_tabletennis+", 1,"+ 4.0 +");");
            db.execSQL("INSERT INTO SPORTS (name, imageID, calories, imageBodyID, enable, constSport) values ('Bóng rổ', "+R.drawable.icon_basketball+", 0, "+R.drawable.body_basketball+", 1,"+ 8.0 +");");
            db.execSQL("INSERT INTO SPORTS (name, imageID, calories, imageBodyID, enable, constSport) values ('Bơi lội', "+R.drawable.icon_swim+", 0, "+R.drawable.body_swim+", 1,"+ 8.0 +");");
            db.execSQL("INSERT INTO SPORTS (name, imageID, calories, imageBodyID, enable, constSport) values ('Yoga', "+R.drawable.icon_yoga+", 0, "+R.drawable.body_yoga+", 1,"+ 3.5 +");");
            db.execSQL("INSERT INTO SPORTS (name, imageID, calories, imageBodyID, enable, constSport) values ('Thể hình', " + R.drawable.icon_gym + ", 0, " + R.drawable.body_gym+", 1,"+ 6.0 +");");
            db.execSQL("INSERT INTO SPORTS (name, imageID, calories, imageBodyID, enable, constSport) values ('Nhảy', " + R.drawable.icon_dance + ", 0, " + R.drawable.body_dance+", 1,"+ 7.2 +");");
            db.execSQL("INSERT INTO SPORTS (name, imageID, calories, imageBodyID, enable, constSport) values ('Kickboxing', "+R.drawable.icon_boxing+", 0, "+R.drawable.body_kickboxing+", 1,"+ 7.8 +");");

            db.close();
        }
        List<Sports> list = new ArrayList<Sports>();

        db = dbOpen.getReadableDatabase();
        Cursor cursor =db.rawQuery("SELECT * FROM SPORTS ", new String[]{});
        while (cursor.moveToNext()){
            String name = cursor.getString(0);
            int imageID = cursor.getInt(1);
            double calories = cursor.getDouble(2);
            int imageBodyID = cursor.getInt(3);
            boolean enable = cursor.getInt(4) > 0;
            double constSport = cursor.getDouble(5);
            Sports sport = new Sports(name, imageID, calories, imageBodyID, enable, constSport);
            list.add(sport);
        }
        cursor.close();
        db.close();


        ListView listView = (ListView) findViewById(R.id.listViewSports);
        CustomSportAdapter adapter = new CustomSportAdapter(this, R.layout.sports, list);
        listView.setAdapter(adapter);


    }

    @Override
    protected void onResume() {
        double caloSportsToday = 0;
        super.onResume();
        List<Sports> list = new ArrayList<Sports>();
        SQLiteDatabase db = dbOpen.getReadableDatabase();
        Cursor cursor =db.rawQuery("SELECT * FROM SPORTS ", new String[]{});
        while (cursor.moveToNext()){
            String name = cursor.getString(0);
            int imageID = cursor.getInt(1);
            double calories = cursor.getDouble(2);
            caloSportsToday = caloSportsToday+calories;
            int imageBodyID = cursor.getInt(3);
            boolean enable = cursor.getInt(4) > 0;
            double constSport = cursor.getDouble(5);
            Sports sport = new Sports(name, imageID, calories, imageBodyID, enable, constSport);
            list.add(sport);
        }
        cursor.close();
        db.close();

        TextView txtCaloSportToday = (TextView)findViewById(R.id.txtCaloSportsToday);
        txtCaloSportToday.setText(caloSportsToday+" cal");
        ListView listView = (ListView) findViewById(R.id.listViewSports);
        CustomSportAdapter adapter = new CustomSportAdapter(this, R.layout.sports, list);
        listView.setAdapter(adapter);

    }


}
