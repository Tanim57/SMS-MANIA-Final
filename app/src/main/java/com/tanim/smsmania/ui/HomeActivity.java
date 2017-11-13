package com.tanim.smsmania.ui;

import org.apache.commons.lang3.StringEscapeUtils;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.tanim.smsmania.Common.Global;
import com.tanim.smsmania.R;
import com.tanim.smsmania.interfaces.ReadContactListener;
import com.tanim.smsmania.interfaces.SmsSendListener;
import com.tanim.smsmania.model.Contact;
import com.tanim.smsmania.model.MobileOperator;
import com.tanim.smsmania.tasks.ReadContactsTasks;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL;

public class HomeActivity extends AppCompatActivity implements ReadContactListener {
    public static String BROADCAST_CUSTOM_SELECT_CONTACT="BROADCAST CUSTOM SELECT CONTACT";
    private ImageButton btnShowContact;
    private ImageButton btnChangeOperator;
    private FloatingActionButton btnSendMessage;
    private TextView tvOperatorId;
    private ProgressDialog mProgressBar;
    private EditText etMessage;
    private TextView tvMessageSize;
    private TextView tvContactSize;
    private ListView contactList;
    private ReadContactsTasks mReadContactsTasks;
    private Spinner dropdown;
    private static boolean isContactLoadComplete = false;
    private List<Integer> sims;
    private ArrayList<MobileOperator> allOperators;
    private SubscriptionManager subscriptionManager;
    private List<SubscriptionInfo> activeSubscriptionInfoList;
    private MobileOperator currentOperator;
    private int dropdownPosition = 0;
    ArrayList<Contact> selectedDataSource;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        readContactTask();
        initEvent();
    }

    @Override
    protected void onResume() {
        if(Global.isCustomContactedSelected)
        {
            dropdownPosition = 6;
            dropdown.setSelection(dropdownPosition);
            tvContactSize.setText((getString(R.string.selected_contact)+" "+Global.customSelectContact));
            Global.isCustomContactedSelected = false;
        }
        else {
            dropdownPosition = 0;
            dropdown.setSelection(dropdownPosition);
            if(Global.ALL_CONTACTS!=null)
            {
                tvContactSize.setText((getString(R.string.selected_contact)+" "+Global.ALL_CONTACTS.size()));
            }
            Global.isCustomContactedSelected = false;
        }
        CheckOperatorStatus();
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(BROADCAST_CUSTOM_SELECT_CONTACT));
    }

    @Override
    protected void onPause() {
        unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    @SuppressLint("NewApi")
    private void initEvent() {
        btnShowContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isContactLoadComplete) {
                    //Intent intent = new Intent(HomeActivity.this,TestClass.class);
                    Intent intent = new Intent(HomeActivity.this, SelectContactActivity.class);
                    SelectContactActivity.mContext = HomeActivity.this;
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Please wait, Contact Loading", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnChangeOperator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.CURRENT_OPERATOR++;
                if(Global.CURRENT_OPERATOR >= Global.NUMBER_OF_OPERATORS)
                {
                    Global.CURRENT_OPERATOR=0;
                }
                Log.d("Check",Global.CURRENT_OPERATOR+" ");
                try
                {
                    currentOperator = allOperators.get(Global.CURRENT_OPERATOR);
                }
                catch (IndexOutOfBoundsException e)
                {
                    Global.CURRENT_OPERATOR=0;
                    currentOperator = allOperators.get(0);
                }
                tvOperatorId.setText(currentOperator.getSubscriptionId()+"");
            }
        });

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentOperator==null)
                {
                    Toast.makeText(getApplicationContext(),"No Operator Found",Toast.LENGTH_SHORT).show();
                }
                else {

                    if(isContactLoadComplete)
                    {
                        if(dropdownPosition<6)
                        {

                            Toast.makeText(getApplicationContext(), "Send", Toast.LENGTH_SHORT).show();
                            SmsManager smsManager = SmsManager.getSmsManagerForSubscriptionId(currentOperator.getSubscriptionId());
                            for(Contact contact:selectedDataSource)
                            {
                                Log.d("Check",contact.name+" "+contact.number);
                                //smsManager.sendTextMessage(contact.number,null,etMessage.getText().toString(),null,null);
                                //smsManager.sendTextMessage("", "Tanim", "S", null, null);
                            }
                            //smsManager.sendTextMessage("01521455796",null,etMessage.getText().toString(),null,null);
                        }
                        else
                        {
                            for (Contact contact:Global.ALL_CONTACTS)
                            {
                                if(contact.isSelected)
                                {
                                    Log.d("Check",contact.name+" "+contact.number);
                                    //smsManager.sendTextMessage(contact.number,null,etMessage.getText().toString(),null,null);
                                }
                            }
                            if(Global.customSelectContact<=0)
                            {
                                Toast.makeText(getApplicationContext(),"No Contact Selected",Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Please wait, Contact Loading", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                dropdownPosition = position;
                if(position<=5)
                {
                    if(isContactLoadComplete)
                    {
                        selectedDataSource = getDataSource(position);
                        tvContactSize.setText(getString(R.string.selected_contact)+" "+selectedDataSource.size());
                    }
                }
                else{
                    selectedDataSource = null;
                    tvContactSize.setText(getString(R.string.selected_contact)+" "+Global.customSelectContact);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                //StringEscapeUtils.escapeJava(s.toString());
                int textSize = s.toString().length();
                if(s.toString().length() == StringEscapeUtils.escapeJava(s.toString()).length())
                {
                    int currentSize=160;
                    if(textSize<=160)
                    {
                        tvMessageSize.setText((160-textSize)+"/1");
                    }
                    else if(textSize<=160+146)
                    {
                        tvMessageSize.setText((160+146-textSize)+"/2");
                    }
                    else if(textSize<=160+146+153){
                        tvMessageSize.setText((160+146+153-textSize)+"/3");
                    }
                    else
                    {
                        tvMessageSize.setText("1KB");
                        Toast.makeText(getApplicationContext(),"Message too Large",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if(textSize<=70)
                    {
                        tvMessageSize.setText((70-textSize)+"/1");
                    }
                    else if(textSize<=70+64)
                    {
                        tvMessageSize.setText((70+64-textSize)+"/2");
                    }
                    else if(textSize<=70+64+67){
                        tvMessageSize.setText((70+64+67-textSize)+"/3");
                    }
                    else
                    {
                        tvMessageSize.setText("1KB");
                        Toast.makeText(getApplicationContext(),"Message too Large",Toast.LENGTH_SHORT).show();
                    }
                }
                /*
                int numberOfSMS = (int) Math.ceil((double) textSize/Global.oneSMSLength);
                tvMessageSize.setText(currentSize+"/"+numberOfSMS);*/
            }
        });
    }

    private ArrayList<Contact> getDataSource(int position) {
        switch(position){
            case 0: return Global.ALL_CONTACTS;
            case 1: return Global.GP_ALL_CONTACTS;
            case 2: return Global.ROBI_ALL_CONTACTS;
            case 3: return Global.AIRTEL_ALL_CONTACTS;
            case 4: return Global.TELETALK_ALL_CONTACTS;
            default: return Global.BL_ALL_CONTACTS;
        }
    }

    private void initView() {
        btnShowContact = (ImageButton) findViewById(R.id.btn_contact_list);
        btnChangeOperator =(ImageButton) findViewById(R.id.btn_change_operator);
        tvOperatorId = (TextView) findViewById(R.id.tv_operator_id);
        tvContactSize = (TextView) findViewById(R.id.contact_size);
        tvMessageSize = (TextView) findViewById(R.id.message_size);
        etMessage = (EditText) findViewById(R.id.et_message);
        btnSendMessage = (FloatingActionButton) findViewById(R.id.btn_send_message);
        mProgressBar = new ProgressDialog(this);
        mProgressBar.setCancelable(false);
        dropdown = (Spinner) findViewById(R.id.dropdown_operator_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Global.OperatorList);
        dropdown.setAdapter(adapter);
    }

    private void readContactTask() {
        ContentResolver contentResolver = getContentResolver();
        mReadContactsTasks = new ReadContactsTasks(this, this, contentResolver);
        if (!isContactLoadComplete)
        {
            mReadContactsTasks.execute();
        }
    }

    @SuppressLint("NewApi")
    private void CheckOperatorStatus() {
        try {
            allOperators = new ArrayList<>();
            subscriptionManager = SubscriptionManager.from(getApplicationContext());
            activeSubscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();
            for (SubscriptionInfo subscriptionInfo : activeSubscriptionInfoList) {
                MobileOperator mobileOperator = new MobileOperator(subscriptionInfo);
                allOperators.add(mobileOperator);
                Global.NUMBER_OF_OPERATORS++;
                if(currentOperator==null)
                {
                    currentOperator = mobileOperator;
                }
            }
            if(Global.NUMBER_OF_OPERATORS<=0)
            {
                Toast.makeText(getApplicationContext(),"No Operator Found",Toast.LENGTH_LONG).show();
                btnSendMessage.setEnabled(false);
                btnChangeOperator.setVisibility(View.GONE);
                tvOperatorId.setVisibility(View.GONE);
            }
            else if(Global.NUMBER_OF_OPERATORS==1)
            {
                btnChangeOperator.setVisibility(View.GONE);
                tvOperatorId.setVisibility(View.GONE);
            }
            else {
                btnSendMessage.setEnabled(true);
                btnChangeOperator.setVisibility(View.VISIBLE);
                tvOperatorId.setVisibility(View.VISIBLE);
            }
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"This Device has no Permission",Toast.LENGTH_LONG).show();
            btnSendMessage.setEnabled(false);
            btnChangeOperator.setVisibility(View.GONE);
            tvOperatorId.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }



    /*@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public void GetCarriorsInformation() {
        try {

            sims = new ArrayList<Integer>();
            SubscriptionManager mSubscriptionManager = SubscriptionManager.from(getApplication());

            List<SubscriptionInfo> subInfoList = mSubscriptionManager.getActiveSubscriptionInfoList();
            for (int i = 0; i < subInfoList.size(); i++) {
                sims.add(subInfoList.get(i).getSubscriptionId());
            }
        } catch (Exception e) {
            Log.e("ok", e.getMessage());
        }
    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    protected void sendMessage() {

        GetCarriorsInformation();

        TelephonyManager tManager = (TelephonyManager) getBaseContext()
                .getSystemService(Context.TELEPHONY_SERVICE);

// Get Mobile No
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        String pNumber = tManager.getLine1Number();


// Get carrier name (Network Operator Name)
        String networkOperatorName= tManager.getNetworkOperatorName();

        Log.d("operator " , networkOperatorName+" : "+tManager.getPhoneCount());

        //String optName1 = getOutput(getApplicationContext(), "getCarrierName", 0);

        //String optName1 = getOutput(getApplicationContext(), "getCarrierName", 0);
        // Sim One
        //SmsManager sm = SmsManager.getSmsManagerForSubscriptionId(sims.get(0));
        //sm.sendTextMessage("01750202379", null, "01521455796", null, null);

        // Sim Two
        //SmsManager sm = SmsManager.getSmsManagerForSubscriptionId(sims.get(1));
    }*/



    /*@Override
    public void onPreExecute() {
        mProgressBar.show();
    }*/

    @Override
    public void onReadContactsCompleteResponse(boolean isContactLoadComplete) {
        this.isContactLoadComplete = isContactLoadComplete;
        if(isContactLoadComplete)
        {
            if(dropdownPosition<=5)
            {
                tvContactSize.setVisibility(View.VISIBLE);
                selectedDataSource = getDataSource(dropdownPosition);
                tvContactSize.setText(getString(R.string.selected_contact)+" "+selectedDataSource.size());
            }
            Toast.makeText(getApplicationContext(),"Contact Load Complete",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"Failed to Load Contact",Toast.LENGTH_LONG).show();
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Check",intent.getStringExtra("Check"));
        }
    };
}
