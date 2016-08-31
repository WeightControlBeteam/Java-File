package com.example.tuananh.manhinhchinh;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by SidaFPT on 7/30/2016.
 */
public class listAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;

    public listAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_tr_item, null);

        TextView monan = (TextView)vi.findViewById(R.id.text_trua); // title
        final TextView kcalo = (TextView)vi.findViewById(R.id.textKcalo); //kcalo
        final EditText inputTr = (EditText) vi.findViewById(R.id.inputTrua); //soluong
        inputTr.setEnabled(false);
        CheckBox checkbox = (CheckBox) vi.findViewById(R.id.radioTrua);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) inputTr.setEnabled(true);
                else inputTr.setEnabled(false);
            }
        });
/*        TextView view = (TextView)vi.findViewById(R.id.view); // artist name
        TextView date = (TextView)vi.findViewById(R.id.date); // duration
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image
        */
//        String link;

        HashMap<String, String> unit = new HashMap<String, String>();
        unit = data.get(position);

        // Setting all values in listview
        monan.setText(unit.get("tenmon"));
        kcalo.setText(unit.get("calo"));

        //display calo each food
        final int listCaloGoc = Integer.parseInt(unit.get("calo"));
        final HashMap<String, String> finalUnit = unit;
        inputTr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String soluong = String.valueOf(inputTr.getText());
                if(!soluong.equals("")){
                    int temp = Integer.parseInt(soluong);
                    int soKcalo = temp*listCaloGoc;
                    kcalo.setText(String.valueOf(soKcalo));
                }
                else {
                    kcalo.setText(finalUnit.get("calo"));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

/*        view.setText(unit.get(xmlnew.KEY_VIEW));
        date.setText(unit.get(xmlnew.KEY_DATE));
        imageLoader.DisplayImage(unit.get(xmlnew.KEY_THUMBNAIL), thumb_image);*/
//        link = unit.get(xmlnew.KEY_VIDEO);
        return vi;
    }

    public View getRowView(int position, View convertView, ViewGroup parent) {
        return LayoutInflater.from(activity).inflate(R.layout.list_tr_item, parent, false);
    }
}
