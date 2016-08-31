package com.example.tuananh.manhinhchinh;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Buttons> list = new ArrayList<Buttons>();


    public static class FirstFragmentAdapter extends FragmentPagerAdapter{

        public FirstFragmentAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new CanNang_Fragment();
                case 1:
                    return new Calories_Fragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        SharedPreferences pre = getSharedPreferences("user2", MODE_PRIVATE);
        String Username = pre.getString("name","NO");

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorWhite)));
        TextView txtName = (TextView)findViewById(R.id.userName);
        txtName.setText(Username);


        //toolbar.setBackgroundColor(getResources().getColor(R.color.colorGreen));

        Buttons EatButton = new Buttons("Eat", R.drawable.eat);
        Buttons BicycleButton = new Buttons("Bicycle", R.drawable.bicycle);
        Buttons RunButton = new Buttons("Run", R.drawable.run);
        Buttons WalkButton = new Buttons("Walk", R.drawable.walk);
        Buttons SleepButton = new Buttons("Sleep", R.drawable.sleep);
        Buttons SportButton = new Buttons("Sport", R.drawable.sport);
        Buttons WaterButton = new Buttons("Water", R.drawable.water);
        Buttons WeightButton = new Buttons("Weight", R.drawable.weight);
        Buttons HeartButton = new Buttons("Heart", R.drawable.heart);
        Buttons TempButton = new Buttons("Temp", R.drawable.temp);
        Buttons CalendarButton = new Buttons("Calendar", R.drawable.calendar);
        list.add(EatButton);
        list.add(BicycleButton);
        list.add(RunButton);
        list.add(WalkButton);
        list.add(SleepButton);
        list.add(SportButton);
        list.add(WaterButton);
        list.add(WeightButton);
        list.add(HeartButton);
        list.add(TempButton);
        list.add(CalendarButton);



        ViewPager viewPager = (ViewPager)findViewById(R.id.viewPager);
        FirstFragmentAdapter adapter = new FirstFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        ExpandableGridView gridView = (ExpandableGridView)findViewById(R.id.gridView);
        gridView.setExpanded(true);
        CustomButtonAdapter buttonAdapter = new CustomButtonAdapter(this,R.layout.buttons,list);
        gridView.setAdapter(buttonAdapter);
        gridView.setFocusable(false);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.user_information) {
            Intent intent = new Intent(this, UserActivity.class);
            this.startActivity(intent);
        }
        if (id == R.id.action_about){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setTitle("Information")
                    .setCancelable(true)
                    .setMessage("WEIGHT CONTROL\n" +
                            "Version 1.0\n" +
                            "Email: anhltse03107@fpt.edu.vn\n" +
                            "AnhLT\n" +
                            "MinhPQ\n" +
                            "ThinhND\n" +
                            "ThanhPQ\n" +
                            "DungNX\n")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {

        super.onResume();
        //get sleep time for homescreen
        SharedPreferences pre = getSharedPreferences("timesleep", MODE_PRIVATE);
        long timeSleep = pre.getLong("timesleep", 0);

        TextView txtSleepMain = (TextView)findViewById(R.id.txtSleepMain);
        long diffSec = timeSleep / 1000 % 60;
        long diffMin = timeSleep / (60*1000) % 60;
        long diffHour = timeSleep / (60*60*1000);
        String out = diffHour + " hours " + diffMin + " mins " + diffSec + " secs";
        txtSleepMain.setText(out);


    }
}
