package com.example.tuananh.manhinhchinh;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

public class NhapThongTin extends AppCompatActivity {

    EditText txtName, txtWeight, txtHeight, txtAge;
    RadioButton btnMale, btnFemale;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhap_thong_tin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtName = (EditText) findViewById(R.id.txtName);
        txtAge = (EditText) findViewById(R.id.txtAge);
        txtHeight = (EditText) findViewById(R.id.txtHeight);
        txtWeight = (EditText) findViewById(R.id.txtWeight);

        btnMale = (RadioButton) findViewById(R.id.btnMale);
        btnFemale = (RadioButton) findViewById(R.id.btnFemale);



    }
    public void doLogin(View view){
        finish();
        Intent i = new Intent(this, MainActivity.class);

        startActivity(i);

    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences pre = getSharedPreferences("user2", MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        String sex = "";
        if (btnMale.isChecked()){
            sex = "male";
        }
        if (btnFemale.isChecked()){
            sex = "female";
        }
        editor.putString("name", txtName.getText().toString());
        editor.putString("age", txtAge.getText().toString());
        editor.putString("weight", txtWeight.getText().toString());
        editor.putString("height", txtHeight.getText().toString());
        editor.putString("sex", sex);
        editor.putBoolean("hasLoggedIn", true);
        if (txtName.getText().toString().trim().equals("") == true || txtAge.getText().toString().equals("") == true || txtWeight.getText().toString().equals("")==true || txtHeight.getText().toString().equals("")==true) return;
        else editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        SharedPreferences pre = getSharedPreferences("user2", MODE_PRIVATE);
//        SharedPreferences.Editor editor = pre.edit();
//        editor.clear().commit();
        SharedPreferences pre = getSharedPreferences("user2", MODE_PRIVATE);
        boolean hasLoggedIn = pre.getBoolean("hasLoggedIn",false);


        if (hasLoggedIn){
            finish();
            Intent i  = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }
}
