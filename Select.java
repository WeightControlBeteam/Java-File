package com.example.tuananh.manhinhchinh;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by SidaFPT on 8/18/2016.
 */
public class Select extends Fragment {
    static TextView textSang, textTrua, textToi, caloSang;
    static Spinner spinnerSang;
    static ListView listviewTrua,listviewToi;
    static String s;
    static EditText txtsoluong;
    static int caloSgoc,tongkcaloS, tongkcaloTr, tongkcaloToi;
    static Button buttonSave, buttonReset, buttonAdd;
    private Context context;
    private String[] arraySpinner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tuchon, container, false);
            context = this.getActivity();

//        setContentView(R.layout.tuchon);
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_WEEK);
        int sogio = c.get(Calendar.HOUR_OF_DAY);
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH)+1;
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        int mDate = c.get(Calendar.DATE);
        String songay = String.valueOf(day);
        final String date = String.valueOf(mDate+'/'+mMonth+'/'+mYear);

        Time t = new Time(Time.getCurrentTimezone());
        t.setToNow();
//        final String date = t.format("%d/%m/%Y");
        //ketnoi data
        final btDatabaseAdapter btdatabase = new btDatabaseAdapter(getActivity());
        btdatabase.open();
        final SQLiteDatabase databt = btdatabase.getMyDatabase();

        spinnerSang = (Spinner) rootView.findViewById(R.id.spinSang);
        caloSang = (TextView) rootView.findViewById(R.id.txtKcaloS);

        List<String> listSang = new ArrayList<String>();
        Cursor cursorS = databt.rawQuery("SELECT * FROM luachon WHERE buaan = 'Sáng'",null);
        if(cursorS.moveToFirst()) {
            do {
                listSang.add(cursorS.getString(cursorS.getColumnIndex("tenmon")));
            }while (cursorS.moveToNext());

        }
        ArrayAdapter<String> sangAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, listSang);
        sangAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSang.setAdapter(sangAdapter);
        final String textSpinSang = spinnerSang.getSelectedItem().toString();
        Cursor curs = databt.rawQuery("SELECT * FROM luachon WHERE buaan = 'Sáng' AND tenmon='"+textSpinSang+"'", null);

        if(curs.moveToFirst()) {
            s = curs.getString(curs.getColumnIndex("calo"));
        }
        caloSang.setText(s);
        caloSang.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String temptextSpinSang = spinnerSang.getSelectedItem().toString();
                Cursor tempcurs = databt.rawQuery("SELECT * FROM luachon WHERE buaan = 'Sáng' AND tenmon='"+temptextSpinSang+"'", null);
                if (tempcurs.moveToFirst()) {
                    String tempString = tempcurs.getString(tempcurs.getColumnIndex("calo"));
                    caloSgoc = Integer.parseInt(String.valueOf(tempString));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        caloSgoc = Integer.parseInt(String.valueOf(caloSang.getText()));
        //get number of breakfast
        txtsoluong = (EditText) rootView.findViewById(R.id.editTextSang);
        txtsoluong.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String numberSang = String.valueOf(txtsoluong.getText());
                if(!numberSang.equals("")) {
                    int temp2 = Integer.parseInt(numberSang);
                    tongkcaloS = caloSgoc * temp2;
                    caloSang.setText(String.valueOf(tongkcaloS));
                }
                else {
                    caloSang.setText(String.valueOf(caloSgoc));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        spinnerSang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String textSpinSang = spinnerSang.getSelectedItem().toString();
                Cursor curs = databt.rawQuery("SELECT * FROM luachon WHERE buaan = 'Sáng' AND tenmon='" + textSpinSang + "'", null);

                if (curs.moveToFirst()) {
                    s = curs.getString(curs.getColumnIndex("calo"));
                }
                caloSang.setText(s);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Cursor cursorTr = databt.rawQuery("SELECT * FROM luachon WHERE buaan = 'Trưa'", null);
        Cursor cursorT = databt.rawQuery("SELECT * FROM luachon WHERE buaan = 'Tối'",null);
        listviewTrua = (ListView) rootView.findViewById(R.id.listTrua);
        listviewToi = (ListView) rootView.findViewById(R.id.listToi);
        getlistview(cursorTr,listviewTrua);
        getlistview(cursorT, listviewToi);

        //save to database
        buttonSave = (Button) rootView.findViewById(R.id.btnSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!txtsoluong.getText().equals(null)&&!txtsoluong.getText().equals("")) {
                    writetoDatabase(databt, "Sáng", spinnerSang.getSelectedItem().toString().trim(), Integer.parseInt(txtsoluong.getText().toString().trim()), Integer.parseInt(caloSang.getText().toString()), date);
                }
                txtsoluong.setText(null);
                for (int i=0;i<listviewTrua.getCount();i++){
                    View wViewTr = (View) listviewTrua.getAdapter().getItem(i);
                    CheckBox wCheck = (CheckBox) wViewTr.findViewById(R.id.radioTrua);
                    TextView wtentrua = (TextView) wViewTr.findViewById(R.id.text_trua);
                    EditText wsoluongtrua = (EditText) wViewTr.findViewById(R.id.inputTrua);
                    TextView wcalotrua = (TextView) wViewTr.findViewById(R.id.textKcalo);
                    if (wCheck.isChecked()){
                        String s1 = String.valueOf(wtentrua.getText());
                        int s2 = Integer.parseInt(wsoluongtrua.getText().toString());
                        writetoDatabase(databt,"Trưa",String.valueOf(wtentrua.getText()).trim(),Integer.parseInt(wsoluongtrua.getText().toString().trim()),Integer.parseInt(wcalotrua.getText().toString()),date);
                        wCheck.setChecked(false);
                        wsoluongtrua.setText(null);
                    }
                }
                for (int j=0;j<listviewToi.getCount();j++){
                    View wViewT = listviewToi.getChildAt(j);
                    CheckBox wCheckT = (CheckBox) rootView.findViewById(R.id.radioTrua);
                    TextView wtentoi = (TextView) rootView.findViewById(R.id.text_trua);
                    EditText wsoluongtoi = (EditText) rootView.findViewById(R.id.inputTrua);
                    TextView wcalotoi = (TextView) rootView.findViewById(R.id.textKcalo);
                    if (wCheckT.isChecked()){
                        writetoDatabase(databt,"Tối",String.valueOf(wtentoi.getText()).trim(),Integer.parseInt(wsoluongtoi.getText().toString().trim()),Integer.parseInt(wcalotoi.getText().toString()),date);
                        wCheckT.setChecked(false);
                        wsoluongtoi.setText(null);
                    }
                }
            }
        });

        //reset all value
        buttonReset = (Button) rootView.findViewById(R.id.btnHuyChon);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtsoluong.setText(null);
                for (int i=0;i<listviewTrua.getCount();i++){
                    CheckBox rCheckTr = (CheckBox) rootView.findViewById(R.id.radioTrua);
                    EditText rSoluongTr = (EditText) rootView.findViewById(R.id.inputTrua);
                    if (rCheckTr.isChecked()){
                        rSoluongTr.setText(null);
                        rCheckTr.setChecked(false);
                    }
                }
                for (int j=0;j<listviewToi.getCount();j++){
                    CheckBox rCheckT = (CheckBox) rootView.findViewById(R.id.radioTrua);
                    EditText rSoluongT = (EditText) rootView.findViewById(R.id.inputTrua);
                    if (rCheckT.isChecked()){
                        rSoluongT.setText(null);
                        rCheckT.setChecked(false);
                    }
                }
            }
        });

        //add more food
        buttonAdd = (Button) rootView.findViewById(R.id.btnThemmon);
//        buttonAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //creat Dialog
//                final Dialog addDialog = new Dialog(context);
//                addDialog.setContentView(R.layout.addfood);
//
//                //Dialog items
//                arraySpinner =new String[] {"Sáng","Trưa","Tối"};
//                final Spinner spinAdd = (Spinner) addDialog.findViewById(R.id.spinAddFood);
//                ArrayAdapter<String> spinAddAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, arraySpinner);
//                spinAdd.setAdapter(spinAddAdapter);
//
//                final EditText addFoodName = (EditText) addDialog.findViewById(R.id.inputAddTen);
//                final EditText addCalo = (EditText) addDialog.findViewById(R.id.inputAddCalo);
//
//                Button btnAF_Save = (Button) addDialog.findViewById(R.id.btnAdd_Save);
//                Button btnAF_Reset = (Button) addDialog.findViewById(R.id.btnAdd_Reset);
//
//                //implement action for buttons
//                btnAF_Reset.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        addFoodName.setText(null);
//                        addCalo.setText(null);
//                        spinAdd.setSelection(0);
//                    }
//                });
//                addDialog.show();
//            }
//        });
        return rootView;
    }

    public void getlistview(Cursor cursor, ListView listview){
        final ArrayList<HashMap<String, String>> itemlist = new ArrayList<HashMap<String, String>>();
        listAdapter listadapter;
        cursor.moveToFirst();
        int i = 0;
        int k = cursor.getCount();
        while ((!cursor.isAfterLast())&&i<k)
        {
            i++;
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("tenmon", cursor.getString(cursor.getColumnIndex("tenmon")));
            map.put("calo", cursor.getString(cursor.getColumnIndex("calo")));
            // adding HashList to ArrayList
            itemlist.add(map);
            cursor.moveToNext();
        }

        listadapter = new listAdapter((Activity) context,itemlist);
        listview.setAdapter(listadapter);
    }
    public void writetoDatabase(SQLiteDatabase wDatabase, String wBuaan,String wTenmon, int wSoluong, int wCalo, String wDate){
        String n1,n2,n5;
        int n3,n4;
        n1 = wBuaan;
        n2 = wTenmon;
        n3 = wSoluong;
        n4 = wCalo;
        n5 = wDate;
        wDatabase.execSQL("INSERT INTO "+"dachon" + " ("
                + "buaan" + ", " + "tenmon" + ", "
                + "soluong" + ", " + "kcalo" + ", "
                + "ngay" + ") VALUES ('"+n1+"', '"+n2+"', '"+n3+"', '"+n4+"', '"+n5+"')");
    }
}
