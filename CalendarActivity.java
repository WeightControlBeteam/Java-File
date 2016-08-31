package com.example.tuananh.manhinhchinh;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorWhite)));

        HashSet<Date> events = new HashSet<>();
        events.add(new Date());

        CalendarView cv = ((CalendarView)findViewById(R.id.calendar_view));
        cv.updateCalendar(events);

        // assign event handler
        cv.setEventHandler(new CalendarView.EventHandler() {
            @Override
            public void onDayLongPress(Date date) {
                // show returned day
                DateFormat df = SimpleDateFormat.getDateInstance();
                Toast.makeText(CalendarActivity.this, df.format(date), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDayPress(Date date) {
                //show calories of that day
                DateFormat df = SimpleDateFormat.getDateInstance();
                TextView txtToday = (TextView) findViewById(R.id.txtToday);
                txtToday.setText(df.format(date));
            }
        });
    }

}
