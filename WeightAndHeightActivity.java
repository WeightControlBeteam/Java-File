package com.example.tuananh.manhinhchinh;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class WeightAndHeightActivity extends AppCompatActivity {

    Button btWeightUp, btWeightDown, btHeightUp, btHeightDown, btCancel, btSave;
    private Handler repeatUpdateHandler = new Handler();
    private boolean mWeightAutoIncrement = false;
    private boolean mWeightAutoDecrement = false;
    private boolean mHeightAutoIncrement = false;
    private boolean mHeightAutoDecrement = false;


    class RptUpdater implements Runnable {
        public void run() {
            if( mWeightAutoIncrement ){
                incrementWeight();
                repeatUpdateHandler.postDelayed( new RptUpdater(), 200 );
            } else if( mWeightAutoDecrement ){
                decrementWeight();
                repeatUpdateHandler.postDelayed( new RptUpdater(), 200 );
            } else if( mHeightAutoIncrement ){
                incrementHeight();
                repeatUpdateHandler.postDelayed( new RptUpdater(), 200 );
            } else if( mHeightAutoDecrement ) {
                decrementHeight();
                repeatUpdateHandler.postDelayed(new RptUpdater(), 200);
            }
    }

        public void decrementWeight(){
            TextView txtWeight = (TextView)findViewById(R.id.txtWeight);
            double weight = Double.parseDouble( txtWeight.getText().toString());
            weight=weight-0.5;
            txtWeight.setText(weight+"");
        }

        public void incrementWeight(){
            TextView txtWeight = (TextView)findViewById(R.id.txtWeight);
            double weight = Double.parseDouble( txtWeight.getText().toString());
            weight=weight+0.5;
            txtWeight.setText(weight+"");
        }

        public void decrementHeight(){
            TextView txtHeight = (TextView)findViewById(R.id.txtHeight);
            double height = Double.parseDouble( txtHeight.getText().toString());
            height=height-0.5;
            txtHeight.setText(height+"");
        }

        public void incrementHeight(){
            TextView txtHeight = (TextView)findViewById(R.id.txtHeight);
            double height = Double.parseDouble( txtHeight.getText().toString());
            height=height+0.5;
            txtHeight.setText(height+"");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_and_height);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorWeight)));

        btWeightUp = (Button)findViewById(R.id.btWeightUp);
        btWeightDown = (Button)findViewById(R.id.btWeightDown);
        btHeightUp = (Button)findViewById(R.id.btHeightUp);
        btHeightDown = (Button)findViewById(R.id.btHeightDown);
        btCancel = (Button)findViewById(R.id.btCancel);
        btSave = (Button)findViewById(R.id.btSave);

        final TextView txtWeight = (TextView)findViewById(R.id.txtWeight);
        final TextView txtHeight = (TextView)findViewById(R.id.txtHeight);

        btWeightUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double weight = Double.parseDouble(txtWeight.getText().toString());
                weight = weight + 0.5;
                txtWeight.setText(weight+"");
            }
        });

        btWeightDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double weight = Double.parseDouble(txtWeight.getText().toString());
                weight = weight - 0.5;
                txtWeight.setText(weight+"");
            }
        });

        btHeightUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double height = Double.parseDouble(txtHeight.getText().toString());
                height = height + 0.5;
                txtHeight.setText(height+"");
            }
        });

        btHeightDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double height = Double.parseDouble(txtHeight.getText().toString());
                height = height - 0.5;
                txtHeight.setText(height+"");
            }
        });

        btWeightDown.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mWeightAutoDecrement = true;
                repeatUpdateHandler.post(new RptUpdater());
                return false;
            }
        });

        btWeightDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                        && mWeightAutoDecrement) {
                    mWeightAutoDecrement = false;
                }
                return false;
            }
        });

        btWeightUp.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mWeightAutoIncrement = true;
                repeatUpdateHandler.post(new RptUpdater());
                return false;
            }
        });

        btWeightUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                        && mWeightAutoIncrement) {
                    mWeightAutoIncrement = false;
                }
                return false;
            }
        });

        btHeightDown.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mHeightAutoDecrement = true;
                repeatUpdateHandler.post(new RptUpdater());
                return false;
            }
        });

        btHeightDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                        && mHeightAutoDecrement) {
                    mHeightAutoDecrement = false;
                }
                return false;
            }
        });

        btHeightUp.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mHeightAutoIncrement = true;
                repeatUpdateHandler.post(new RptUpdater());
                return false;
            }
        });

        btHeightUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                        && mHeightAutoIncrement) {
                    mHeightAutoIncrement = false;
                }
                return false;
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pre = getSharedPreferences("user2", MODE_PRIVATE);
                SharedPreferences.Editor editor = pre.edit();
                editor.putString("weight", txtWeight.getText().toString());
                editor.putString("height", txtHeight.getText().toString());
                editor.apply();

                double weight = Double.parseDouble(txtWeight.getText().toString());
                double height = Double.parseDouble(txtHeight.getText().toString());

                TextView txtBMI = (TextView) findViewById(R.id.txtBMI);
                double bmi1 = weight * 10000 / (height * height);
                Long bmi2 = Math.round(bmi1 * 10);
                double bmi = Double.parseDouble(bmi2.toString()) / 10;
                txtBMI.setText(bmi+"");

                TextView txtBMIInfo = (TextView)findViewById(R.id.txtBMIInfo);
                if(bmi<18.5) txtBMIInfo.setText("Người gầy");
                else if (bmi>=18.5 && bmi<23) txtBMIInfo.setText("Người bình thường");
                else if (bmi>=23 && bmi<25) txtBMIInfo.setText("Thừa cân");
                else if (bmi>=25) txtBMIInfo.setText("Béo phì");

                Toast.makeText(getApplicationContext(), "Information updated", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pre = getSharedPreferences("user2", MODE_PRIVATE);

        double weight = Double.parseDouble( pre.getString("weight","0"));
        TextView txtWeight = (TextView)findViewById(R.id.txtWeight);
        txtWeight.setText(weight+"");

        double height = Double.parseDouble(pre.getString("height", "0"));
        TextView txtHeight = (TextView)findViewById(R.id.txtHeight);
        txtHeight.setText(height+"");

        TextView txtBMI = (TextView)findViewById(R.id.txtBMI);
        double bmi1 = weight*10000/(height*height);
        Long bmi2 =  Math.round(bmi1*10);
        double bmi = Double.parseDouble(bmi2.toString())/10;
        txtBMI.setText(bmi+"");

        TextView txtBMIInfo = (TextView)findViewById(R.id.txtBMIInfo);
        if(bmi<18.5) txtBMIInfo.setText("Người gầy");
        else if (bmi>=18.5 && bmi<23) txtBMIInfo.setText("Người bình thường");
        else if (bmi>=23 && bmi<25) txtBMIInfo.setText("Thừa cân");
        else if (bmi>=25) txtBMIInfo.setText("Béo phì");
    }


}
