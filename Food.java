package com.example.tuananh.manhinhchinh;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Food extends Fragment {
    static TextView chon_ngay,chon_tenmonS,chon_caloS;
    static Button nut_chon_sang,nut_chon_trua,nut_chon_toi;
    static ListView dachon_trua,dachon_toi;
    static String s,mon_dachonSang,calo_dachonSang;
    static int caloSgoc,tongkcaloSang;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        final View foodView = inflater.inflate(R.layout.chonmon_1, container, false);
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_WEEK);
        int sogio = c.get(Calendar.HOUR_OF_DAY);
        String mYear = String.valueOf(c.get(Calendar.YEAR));
        String mMonth = String.valueOf(c.get(Calendar.MONTH)+1);
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        int mDate = c.get(Calendar.DATE);
        String songay = String.valueOf(day);
        final String date = String.valueOf(mDay + '/' + mMonth + '/' + mYear);
        String chonngay = String.valueOf('('+mDay + '/' + mMonth + '/' + mYear+')');
        chon_ngay = (TextView) foodView.findViewById(R.id.txt_chon_ngay);
        chon_ngay.setText(chonngay);

        final btDatabaseAdapter btdatabase = new btDatabaseAdapter(getActivity());
        btdatabase.open();
        final SQLiteDatabase databt = btdatabase.getMyDatabase();
        final Cursor cursorS = databt.rawQuery("SELECT * FROM luachon WHERE buaan = 'Sáng'", null);
        final Cursor cursorTr = databt.rawQuery("SELECT * FROM luachon WHERE buaan = 'Trưa'", null);
        final Cursor cursorT = databt.rawQuery("SELECT * FROM luachon WHERE buaan = 'Tối'",null);

        //click button Bữa Sáng
        final Cursor checkSang = databt.rawQuery("SELECT * FROM dachon WHERE buaan = 'Sáng' AND ngay='"+date+"'",null);
        Cursor checkTrua = databt.rawQuery("SELECT * FROM dachon WHERE buaan = 'Trưa' AND ngay='"+date+"'",null);
        Cursor checkToi = databt.rawQuery("SELECT * FROM dachon WHERE buaan = 'Tối' AND ngay='"+date+"'",null);
        nut_chon_sang = (Button) foodView.findViewById(R.id.btn_chon_sang);
//        if (checkSang.getCount()!=0){nut_chon_sang.setVisibility(View.GONE);}
        nut_chon_sang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSang.getCount() != 0) {
                    AlertDialog.Builder errorbuilder = new AlertDialog.Builder(getActivity());
                    errorbuilder.setTitle("Error!");
                    errorbuilder.setMessage("Bạn đã chọn bữa sáng rồi!");
                    errorbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = errorbuilder.create();
                    alertDialog.show();
                } else {
                    final Dialog sangDialog = new Dialog(getActivity());
                    sangDialog.setContentView(R.layout.diaglog_chonbuasang);
                    final Spinner spin_chonmon_sang = (Spinner) sangDialog.findViewById(R.id.spin_chon_sang);
                    final TextView chonsang_calo = (TextView) sangDialog.findViewById(R.id.txt_chonmonsang_calo);
                    final EditText chonsang_soluong = (EditText) sangDialog.findViewById(R.id.edit_chon_sang_sl);
                    Button btn_sang_save = (Button) sangDialog.findViewById(R.id.btn_chon_sang_save);
                    Button btn_sang_cancel = (Button) sangDialog.findViewById(R.id.btn_chon_sang_huy);
                    Button btn_sang_add = (Button) sangDialog.findViewById(R.id.btn_chon_sang_them);
                    final List<String> listSang = new ArrayList<String>();
                    if (cursorS.moveToFirst()) {
                        do {
                            listSang.add(cursorS.getString(cursorS.getColumnIndex("tenmon")));
                        } while (cursorS.moveToNext());

                    }
                    final ArrayAdapter<String> sangAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listSang);
                    sangAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin_chonmon_sang.setAdapter(sangAdapter);
                    final String textSpinSang = spin_chonmon_sang.getSelectedItem().toString();
                    Cursor curs = databt.rawQuery("SELECT * FROM luachon WHERE buaan = 'Sáng' AND tenmon='" + textSpinSang + "'", null);

                    if (curs.moveToFirst()) {
                        s = curs.getString(curs.getColumnIndex("calo"));
                    }
                    chonsang_calo.setText(s);
                    chonsang_calo.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            String temptextSpinSang = spin_chonmon_sang.getSelectedItem().toString();
                            Cursor tempcurs = databt.rawQuery("SELECT * FROM luachon WHERE buaan = 'Sáng' AND tenmon='" + temptextSpinSang + "'", null);
                            if (tempcurs.moveToFirst()) {
                                String tempString = tempcurs.getString(tempcurs.getColumnIndex("calo"));
                                caloSgoc = Integer.parseInt(String.valueOf(tempString));
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    caloSgoc = Integer.parseInt(String.valueOf(chonsang_calo.getText()));
                    //get number of breakfast
                    chonsang_soluong.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            String numberSang = String.valueOf(chonsang_soluong.getText());
                            if (!numberSang.equals("")) {
                                int temp2 = Integer.parseInt(numberSang);
                                tongkcaloSang = caloSgoc * temp2;
                                chonsang_calo.setText(String.valueOf(tongkcaloSang));
                            } else {
                                chonsang_calo.setText(String.valueOf(caloSgoc));
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                    spin_chonmon_sang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String textSpinSang = spin_chonmon_sang.getSelectedItem().toString();
                            Cursor curs = databt.rawQuery("SELECT * FROM luachon WHERE buaan = 'Sáng' AND tenmon='" + textSpinSang + "'", null);

                            if (curs.moveToFirst()) {
                                s = curs.getString(curs.getColumnIndex("calo"));
                            }
                            chonsang_calo.setText(s);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    //Lưu món sáng
                    btn_sang_save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!chonsang_soluong.getText().equals(null) && !chonsang_soluong.getText().equals("")) {
                                writetoDatabase(databt, "Sáng", spin_chonmon_sang.getSelectedItem().toString().trim(), Integer.parseInt(chonsang_soluong.getText().toString().trim()), Integer.parseInt(chonsang_calo.getText().toString()), date);
                                sangDialog.dismiss();
                                Toast.makeText(getActivity(), "Lưu bữa sáng thành công!", Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                    btn_sang_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sangDialog.dismiss();

                        }
                    });

                    btn_sang_add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addFood(databt, "Sáng");
                        }
                    });


                    //show Dialog Sáng
                    sangDialog.show();
                }
            }
        });

        //click chọn bữa trưa
        nut_chon_trua = (Button) foodView.findViewById(R.id.btn_chon_trua);
        nut_chon_trua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog truaDialog = new Dialog(getActivity());
                truaDialog.setContentView(R.layout.dialog_chontrua);
                truaDialog.getWindow().setLayout(getResources().getDisplayMetrics().widthPixels, WindowManager.LayoutParams.WRAP_CONTENT);
                final ListView list_chon_trua = (ListView) truaDialog.findViewById(R.id.list_chon_trua);
                Button btn_chonTr_save = (Button) truaDialog.findViewById(R.id.btn_chon_trua_save);
                Button btn_chonTr_cancel = (Button) truaDialog.findViewById(R.id.btn_chon_trua_huy);
                Button btn_chonTr_add = (Button) truaDialog.findViewById(R.id.btn_chon_trua_them);

                //add items to list
                final ArrayList<HashMap<String, String>> itemlist = new ArrayList<HashMap<String, String>>();
                final listAdapter listadapter;
                cursorTr.moveToFirst();
                int i = 0;
                int k = cursorTr.getCount();
                while ((!cursorTr.isAfterLast())&&i<k)
                {
                    i++;
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("tenmon", cursorTr.getString(cursorTr.getColumnIndex("tenmon")));
                    map.put("calo", cursorTr.getString(cursorTr.getColumnIndex("calo")));
                    // adding HashList to ArrayList
                    itemlist.add(map);
                    cursorTr.moveToNext();
                }

                listadapter = new listAdapter(getActivity(),itemlist);
                list_chon_trua.setAdapter(listadapter);

                //Lưu bữa trưa
                btn_chonTr_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i=0;i<list_chon_trua.getCount();i++){
                            View wViewTr = list_chon_trua.getChildAt(i);
                            CheckBox wCheck = (CheckBox) wViewTr.findViewById(R.id.radioTrua);
                            TextView wtentrua = (TextView) wViewTr.findViewById(R.id.text_trua);
                            EditText wsoluongtrua = (EditText) wViewTr.findViewById(R.id.inputTrua);
                            TextView wcalotrua = (TextView) wViewTr.findViewById(R.id.textKcalo);
                            if (wCheck.isChecked()){
                                String s1 = String.valueOf(wtentrua.getText());
                                int s2 = Integer.parseInt(wsoluongtrua.getText().toString());
                                writetoDatabase(databt, "Trưa", String.valueOf(wtentrua.getText()).trim(), Integer.parseInt(wsoluongtrua.getText().toString().trim()), Integer.parseInt(wcalotrua.getText().toString()), date);
                                Toast.makeText(getActivity(), "Lưu bữa trưa thành công!", Toast.LENGTH_LONG).show();
                                truaDialog.dismiss();
                            }
                        }
                    }
                });
                btn_chonTr_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        truaDialog.dismiss();
                    }
                });
                btn_chonTr_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addFood(databt, "Trưa");
                    }
                });

                //show Dialog Trưa
                truaDialog.show();
            }
        });


        //chon bua toi
        nut_chon_toi = (Button) foodView.findViewById(R.id.btn_chon_toi);
        nut_chon_toi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog toiDialog = new Dialog(getActivity());
                toiDialog.setContentView(R.layout.dialog_chontoi);
                final ListView list_chon_toi = (ListView) toiDialog.findViewById(R.id.list_chon_toi);
                toiDialog.getWindow().setLayout(getResources().getDisplayMetrics().widthPixels, WindowManager.LayoutParams.WRAP_CONTENT);
                Button btn_chonT_save = (Button) toiDialog.findViewById(R.id.btn_chon_toi_save);
                Button btn_chonT_cancel = (Button) toiDialog.findViewById(R.id.btn_chon_toi_huy);
                Button btn_chonT_add = (Button) toiDialog.findViewById(R.id.btn_chon_toi_them);

                //add items to list
                final ArrayList<HashMap<String, String>> itemlisttoi = new ArrayList<HashMap<String, String>>();
                final listAdapter listtoiadapter;
                cursorT.moveToFirst();
                int i = 0;
                int k = cursorT.getCount();
                while ((!cursorT.isAfterLast())&&i<k)
                {
                    i++;
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("tenmon", cursorT.getString(cursorT.getColumnIndex("tenmon")));
                    map.put("calo", cursorT.getString(cursorT.getColumnIndex("calo")));
                    // adding HashList to ArrayList
                    itemlisttoi.add(map);
                    cursorT.moveToNext();
                }

                listtoiadapter = new listAdapter(getActivity(),itemlisttoi);
                list_chon_toi.setAdapter(listtoiadapter);

                //Lưu bữa tối
                btn_chonT_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i=0;i<list_chon_toi.getCount();i++){
                            View wViewTr = list_chon_toi.getChildAt(i);
                            CheckBox wCheck = (CheckBox) wViewTr.findViewById(R.id.radioTrua);
                            TextView wtentoi = (TextView) wViewTr.findViewById(R.id.text_trua);
                            EditText wsoluongtoi = (EditText) wViewTr.findViewById(R.id.inputTrua);
                            TextView wcalotoi = (TextView) wViewTr.findViewById(R.id.textKcalo);
                            if (wCheck.isChecked()){
                                String s1 = String.valueOf(wtentoi.getText());
                                int s2 = Integer.parseInt(wsoluongtoi.getText().toString());
                                writetoDatabase(databt,"Tối",String.valueOf(wtentoi.getText()).trim(),Integer.parseInt(wsoluongtoi.getText().toString().trim()),Integer.parseInt(wcalotoi.getText().toString()),date);
                                Toast.makeText(getActivity(), "Lưu bữa tối thành công!", Toast.LENGTH_LONG).show();
                                toiDialog.dismiss();
                            }
                        }
                    }
                });
                btn_chonT_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toiDialog.dismiss();
                    }
                });
                btn_chonT_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addFood(databt,"Tối");
                    }
                });

                //show Dialog Trưa
                toiDialog.show();
            }
        });

        //Hiển thị món ăn đã chọn
        //Sáng
        chon_tenmonS = (TextView) foodView.findViewById(R.id.txt_chon_tensang);
        chon_caloS = (TextView) foodView.findViewById(R.id.txt_dachon_calosang);
        Cursor cursorS_ed = databt.rawQuery("SELECT * FROM dachon WHERE buaan = 'Sáng' AND ngay='" +date+ "'", null);
        if(cursorS_ed.moveToFirst()) {
            mon_dachonSang = cursorS_ed.getString(cursorS_ed.getColumnIndex("tenmon"));
            calo_dachonSang = cursorS_ed.getString(cursorS_ed.getColumnIndex("kcalo"));
        }
        chon_tenmonS.setText(mon_dachonSang);
        chon_caloS.setText(calo_dachonSang);

        //Trưa
        Cursor cursorTr_ed = databt.rawQuery("SELECT * FROM dachon WHERE buaan = 'Trưa' AND ngay='" +date+ "'", null);
        dachon_trua = (ListView) foodView.findViewById(R.id.list_dachon_trua);
        final ArrayList<HashMap<String, String>> itemlistTr_dachon = new ArrayList<HashMap<String, String>>();
        ListHomeAdapter listtr_adapter;
        cursorTr_ed.moveToFirst();
        int i = 0;
        int k = cursorTr_ed.getCount();
        while ((!cursorTr_ed.isAfterLast())&&i<k)
        {
            i++;
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("tenmon", cursorTr_ed.getString(cursorTr_ed.getColumnIndex("tenmon")));
            map.put("soluong", cursorTr_ed.getString(cursorTr_ed.getColumnIndex("soluong")));
            map.put("kcalo", cursorTr_ed.getString(cursorTr_ed.getColumnIndex("kcalo")));
            // adding HashList to ArrayList
            itemlistTr_dachon.add(map);
            cursorTr_ed.moveToNext();
        }

        listtr_adapter = new ListHomeAdapter(getActivity(),itemlistTr_dachon);
        dachon_trua.setAdapter(listtr_adapter);

        //Tối
        Cursor cursorT_ed = databt.rawQuery("SELECT * FROM dachon WHERE buaan = 'Tối' AND ngay='" +date+ "'", null);
        dachon_toi = (ListView) foodView.findViewById(R.id.list_dachon_toi);
        final ArrayList<HashMap<String, String>> itemlistT_dachon = new ArrayList<HashMap<String, String>>();
        ListHomeAdapter listt_adapter;
        cursorT_ed.moveToFirst();
        int l = 0;
        int m = cursorT_ed.getCount();
        while ((!cursorT_ed.isAfterLast())&&l<m)
        {
            l++;
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("tenmon", cursorT_ed.getString(cursorT_ed.getColumnIndex("tenmon")));
            map.put("soluong", cursorT_ed.getString(cursorT_ed.getColumnIndex("soluong")));
            map.put("kcalo", cursorT_ed.getString(cursorT_ed.getColumnIndex("kcalo")));
            // adding HashList to ArrayList
            itemlistT_dachon.add(map);
            cursorT_ed.moveToNext();
        }

        listt_adapter = new ListHomeAdapter(getActivity(),itemlistT_dachon);
        dachon_toi.setAdapter(listt_adapter);

        return foodView;
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
    public void addFood(final SQLiteDatabase data, final String buaan){
        final Dialog addDialog = new Dialog(getContext());
        addDialog.setContentView(R.layout.addfood);
        TextView txt_add_bua;
        final EditText txt_add_tenmon,txt_add_kcal;
        Button btn_add_save,btn_add_cancel;

        txt_add_bua = (TextView) addDialog.findViewById(R.id.txt_add_buaan);
        txt_add_bua.setText(buaan);
        txt_add_tenmon = (EditText) addDialog.findViewById(R.id.inputAddTen);
        txt_add_kcal = (EditText) addDialog.findViewById(R.id.inputAddCalo);
        btn_add_save = (Button) addDialog.findViewById(R.id.btnAdd_Save);
        btn_add_cancel = (Button) addDialog.findViewById(R.id.btnAdd_cancel);
        btn_add_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenmon = String.valueOf(txt_add_tenmon.getText());
                String kcalo = String.valueOf(txt_add_kcal.getText());
                data.execSQL("INSERT INTO "+"luachon" + " ("
                        + "tenmon" + ", " + "buaan" + ", "
                        + "calo" + ") VALUES ('"+tenmon+"', '"+buaan+"', '"+kcalo+"')");
                Toast.makeText(getActivity(), "Thêm món thành công!", Toast.LENGTH_LONG).show();
            }
        });
        btn_add_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialog.dismiss();
            }
        });



        addDialog.show();

    }

}
