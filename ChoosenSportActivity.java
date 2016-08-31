package com.example.tuananh.manhinhchinh;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ChoosenSportActivity extends AppCompatActivity {
    Sports sport;
    Chronometer mChronometer;
    Button btStart,btPause,btStop,btContinue;
    TextView caloriesSportBurn, txtTimeEnd, txtTime, txtCalories, txtCaloEnd;
    ImageView imgSport;

    long timeWhenStopped = 0;
    long timeOut = 0;
    boolean pause = true;
    boolean stop = false;

    Handler caloriesHandler = new Handler();

    public double calculateCalories(double constSport, long time){
        SharedPreferences pre = getSharedPreferences("user2", MODE_PRIVATE);
        String sex = pre.getString("sex", "no");
        double height = Double.parseDouble(pre.getString("height", "0"));
        double weight = Double.parseDouble(pre.getString("weight", "0"));
        double age = Double.parseDouble(pre.getString("age", "0"));

        double calories = 0;

        if (sex.equalsIgnoreCase("male")){
            double BMR = (13.75 * weight) + (5 * height) - (6.76 * age) +66;
            calories = (((BMR/24) * constSport)/3600)*time;
        }
        else
        if (sex.equalsIgnoreCase("female")){
            double BMR = (9.56 * weight) + (1.85 * height) - (4.68 * age) +655;
            calories = (((BMR/24) * constSport)/3600)*time;
        }
        return calories;
    }

    class  CaloriesUpdater implements Runnable{
        @Override
        public void run() {
            if (pause == false) {
                long elapsedMillis = SystemClock.elapsedRealtime() - mChronometer.getBase();
                long timeInSec = elapsedMillis/1000;
                double calories = calculateCalories(sport.getConstSport(), timeInSec);
                caloriesSportBurn.setText(String.format("%.2f", calories));
                caloriesHandler.postDelayed(new CaloriesUpdater(), 1000);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosen_sport);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //draw toolbar
        sport = (Sports)getIntent().getSerializableExtra("sport");

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ImageView iconChoosenSport = (ImageView)findViewById(R.id.iconChoosenSport);
        iconChoosenSport.setImageResource(sport.getImageID());
        TextView nameChoosenSprot = (TextView)findViewById(R.id.nameChoosenSport);
        nameChoosenSprot.setText(sport.getName());
        imgSport = (ImageView)findViewById(R.id.imgSport);

        imgSport.setImageResource(sport.getImageBodyID());
        //
        caloriesSportBurn = (TextView)findViewById(R.id.txtCaloriesSportBurn);
        mChronometer = (Chronometer)findViewById(R.id.chronometer);
        txtTimeEnd = (TextView)findViewById(R.id.txtTimeEnd);
        txtTime = (TextView)findViewById(R.id.txtTime);
        txtCalories = (TextView)findViewById(R.id.txtCalories);
        txtCaloEnd = (TextView)findViewById(R.id.txtCaloEnd);
        //set format for chronometer
        mChronometer.setText("00:00:00");
        mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long time = SystemClock.elapsedRealtime() - chronometer.getBase();
                int h   = (int)(time /3600000);
                int m = (int)(time - h*3600000)/60000;
                int s= (int)(time - h*3600000- m*60000)/1000 ;
                String hh = h < 10 ? "0"+h: h+"";
                String mm = m < 10 ? "0"+m: m+"";
                String ss = s < 10 ? "0"+s: s+"";
                chronometer.setText(hh+":"+mm+":"+ss);
            }
        });

        btStart = (Button)findViewById(R.id.btStart);
        btContinue = (Button)findViewById(R.id.btContinue);
        btPause = (Button)findViewById(R.id.btPause);
        btStop = (Button)findViewById(R.id.btStop);

        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgSport.setVisibility(View.VISIBLE);
                mChronometer.setBase(SystemClock.elapsedRealtime());
                mChronometer.start();

                pause = false;
                stop = false;

                btStart.setVisibility(View.INVISIBLE);
                btPause.setVisibility(View.VISIBLE);

                caloriesHandler.post(new CaloriesUpdater());

                imgSport.clearAnimation();
                txtTime.clearAnimation();
                txtTimeEnd.clearAnimation();
                txtCalories.clearAnimation();
                txtCaloEnd.clearAnimation();

                DBOpen dbOpen = new DBOpen(getApplicationContext(), "sports", null, 1);
                SQLiteDatabase db = dbOpen.getWritableDatabase();
                db.execSQL("UPDATE SPORTS SET enable = 0 where name != '"+sport.getName()+"';");
                db.close();
            }
        });

        btPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChronometer.stop();
                timeWhenStopped = mChronometer.getBase() - SystemClock.elapsedRealtime();

                pause = true;
                stop = false;

                btPause.setVisibility(View.INVISIBLE);
                btContinue.setVisibility(View.VISIBLE);
                btStop.setVisibility(View.VISIBLE);
            }
        });

        btContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
                mChronometer.start();

                pause = false;
                stop = false;

                btPause.setVisibility(View.VISIBLE);
                btContinue.setVisibility(View.INVISIBLE);
                btStop.setVisibility(View.INVISIBLE);

                caloriesHandler.post(new CaloriesUpdater());
            }
        });

        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double caloEnd = Double.parseDouble(caloriesSportBurn.getText().toString().replaceAll(",","."));
                txtTimeEnd.setText(mChronometer.getText());
                txtCaloEnd.setText(caloriesSportBurn.getText() + " cal");

                mChronometer.stop();

                stop = true;
                btContinue.setVisibility(View.INVISIBLE);
                btStop.setVisibility(View.INVISIBLE);
                btStart.setVisibility(View.VISIBLE);

                mChronometer.setText("00:00:00");
                caloriesSportBurn.setText("0,00");


                //Animation di chuyển 1 ảnh
                final TranslateAnimation translateAnimation = new TranslateAnimation(0f, 250f, 0f, 0f);
                //Tổng thời gian di chuyển
                translateAnimation.setDuration(3000);
                //Start animation
                imgSport.startAnimation(translateAnimation);
                //Stop animation when it done
                translateAnimation.setFillAfter(true);

                //display calories and time
                final AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                alphaAnimation.setDuration(3000);

                txtTime.startAnimation(alphaAnimation);
                txtTimeEnd.startAnimation(alphaAnimation);
                txtCalories.startAnimation(alphaAnimation);
                txtCaloEnd.startAnimation(alphaAnimation);

                alphaAnimation.setFillAfter(true);

                //open database
                DBOpen dbOpen = new DBOpen(getApplicationContext(), "sports", null, 1);
                //get calories of this sport in today
                SQLiteDatabase db=dbOpen.getReadableDatabase();
                Cursor cursor =db.rawQuery("SELECT calories FROM SPORTS WHERE name = '"+sport.getName()+"';", new String[]{});
                double caloToday = 0;
                while (cursor.moveToNext()){
                    caloToday=cursor.getDouble(0);
                }
                cursor.close();
                db.close();

                //update database
                db = dbOpen.getWritableDatabase();
                //update all sport become enable
                db.execSQL("UPDATE SPORTS SET enable = 1 where name != '" + sport.getName() + "';");
                //update calories of this sport in today
                caloToday = caloToday + caloEnd;
                db.execSQL("UPDATE SPORTS SET calories = "+caloToday+" where name = '"+sport.getName()+"';");
                db.close();

            }
        });
    }

    private void showElapsedTime() {
        long elapsedMillis = SystemClock.elapsedRealtime() - mChronometer.getBase();
        Toast.makeText(this, "Elapsed milliseconds: " + elapsedMillis,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pre = getSharedPreferences("timeSportOut",MODE_PRIVATE);
        timeOut = pre.getLong("timeSportOut", 0);
        pause = pre.getBoolean("pauseSport", true);
        timeWhenStopped = pre.getLong("timeWhenStopped",0);
        stop = pre.getBoolean("stopSport", false);
        if(stop == true) return;
        else if (timeOut!=0 && pause == false) {
            mChronometer.setBase(timeOut);
            mChronometer.start();
            btStart.setVisibility(View.INVISIBLE);
            btPause.setVisibility(View.VISIBLE);

            caloriesHandler.post(new CaloriesUpdater());
        }
        else if (timeOut!=0 && pause == true){
            mChronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);

            long time = SystemClock.elapsedRealtime()- mChronometer.getBase();
            int h   = (int)(time /3600000);
            int m = (int)(time - h*3600000)/60000;
            int s= (int)(time - h*3600000- m*60000)/1000 ;
            String hh = h < 10 ? "0"+h: h+"";
            String mm = m < 10 ? "0"+m: m+"";
            String ss = s < 10 ? "0"+s: s+"";
            mChronometer.setText(hh+":"+mm+":"+ss);

            btStart.setVisibility(View.INVISIBLE);
            btPause.setVisibility(View.INVISIBLE);
            btContinue.setVisibility(View.VISIBLE);
            btStop.setVisibility(View.VISIBLE);

            long elapsedMillis = SystemClock.elapsedRealtime() - mChronometer.getBase();
            long timeInSec = elapsedMillis/1000;
            double calories = calculateCalories(sport.getConstSport(), timeInSec);

            caloriesSportBurn.setText(String.format("%.2f", calories));
        }

    }

    @Override
    protected void onPause() {
        timeOut = mChronometer.getBase();
        SharedPreferences pre = getSharedPreferences("timeSportOut",MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putLong("timeSportOut", timeOut);
        editor.putBoolean("pauseSport", pause);
        editor.putLong("timeWhenStopped",timeWhenStopped);
        editor.putBoolean("stopSport",stop);
        editor.apply();

        super.onPause();

    }



}
