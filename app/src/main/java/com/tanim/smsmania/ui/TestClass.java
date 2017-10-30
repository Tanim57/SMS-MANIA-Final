package com.tanim.smsmania.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.tanim.smsmania.Common.Global;
import com.tanim.smsmania.R;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Tanim on 10/24/2017.
 */

public class TestClass extends AppCompatActivity {
    EditText editText;
    ListView listView;
    ArrayAdapter<String> stringArrayAdapter;
    ArrayList<String> stringArrayList;
    AllContactAdapter adapter;
    String[] items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        editText = (EditText) findViewById(R.id.edit_text);
        listView = (ListView) findViewById(R.id.contact_list);
        items = new String[]{"abc","abcd","bangladesh","bang","egfabc"};
        stringArrayList = new ArrayList<>(Arrays.asList(items));
        //stringArrayAdapter = new ArrayAdapter<>(this,R.layout.itemslist,R.id.text,stringArrayList);
        adapter = new AllContactAdapter(this, Global.ALL_CONTACTS);
        listView.setTextFilterEnabled(true);
        listView.setAdapter(adapter);
        adapter.getFilter().filter("Tan");

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //adapter.getFilter().filter(s.toString());
            }
        });

    }


}
