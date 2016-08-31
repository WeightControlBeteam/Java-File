package com.example.tuananh.manhinhchinh;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class UserActivity extends AppCompatActivity {

    TextView txtName, txtAge, txtSex, txtWeight, txtHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorWhite)));



        SharedPreferences pre = getSharedPreferences("user2", MODE_PRIVATE);
        String name = pre.getString("name", "NO");
        String age = pre.getString("age", "0");
        String sex = pre.getString("sex", "sex");
        String height = pre.getString("height", "0");
        String weight = pre.getString("weight", "0");

        txtName = (TextView)findViewById(R.id.txtName);
        txtAge = (TextView)findViewById(R.id.txtAge);
        txtSex = (TextView)findViewById(R.id.txtSex);
        txtHeight = (TextView)findViewById(R.id.txtHeight);
        txtWeight = (TextView)findViewById(R.id.txtWeight);

        txtName.setText(name);
        txtAge.setText(age);
        txtSex.setText(sex);
        txtHeight.setText(height);
        txtWeight.setText(weight);
    }

}
