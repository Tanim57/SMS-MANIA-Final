package com.tanim.smsmania.tasks;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import com.tanim.smsmania.Common.Global;
import com.tanim.smsmania.interfaces.ReadContactListener;
import com.tanim.smsmania.model.Contact;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Pattern;

/**
 * Created by Tanim on 10/24/2017.
 */

public class ReadContactsTasks extends AsyncTask<Void, Void, Boolean> {

    private Context mContext;
    private ReadContactListener mContactListener;
    ContentResolver mContentResolver;


    public ReadContactsTasks(Context mContext, ReadContactListener contactListener, ContentResolver mContentResolver) {
        this.mContext = mContext;
        this.mContentResolver = mContentResolver;
        this.mContactListener = contactListener;
    }


    /*@Override
    protected void onPreExecute() {
        mContactListener.onPreExecute();
    }*/

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            ArrayList<Contact> allContacts = new ArrayList<>();
            ArrayList<Contact> gpContacts = new ArrayList<>();
            ArrayList<Contact> robiContacts = new ArrayList<>();
            ArrayList<Contact> blContacts = new ArrayList<>();
            ArrayList<Contact> teletalkContacts = new ArrayList<>();
            ArrayList<Contact> airtelContact = new ArrayList<>();
            Cursor cur = mContentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);

            int i = 0;
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    String id = cur.getString(
                            cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(
                            ContactsContract.Contacts.DISPLAY_NAME));

                    if (cur.getInt(cur.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        Cursor pCur = mContentResolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        while (pCur.moveToNext()) {
                            String phoneNo = pCur.getString(pCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));

                            if (phoneNo.length() >= 11 && phoneNo.length() <= 14) {
                                Contact contact = new Contact(name, phoneNo, false,i++);
                                allContacts.add(contact);
                                if (isGpNumber(phoneNo)) {
                                    gpContacts.add(contact);
                                } else if (isRobiNumber(phoneNo)) {
                                    robiContacts.add(contact);
                                } else if (isTeleTalkNumber(phoneNo)) {
                                    teletalkContacts.add(contact);
                                } else if (isAirtelNumber(phoneNo)) {
                                    airtelContact.add(contact);
                                } else if (isBlNumber(phoneNo)) {
                                    blContacts.add(contact);
                                }
                            }
                        }
                        pCur.close();
                    }
                }
            }
            sort(allContacts);
            sort(gpContacts);
            sort(teletalkContacts);
            sort(airtelContact);
            sort(robiContacts);
            sort(blContacts);
            Global.isMarked = new boolean[allContacts.size()];
            Arrays.fill(Global.isMarked,false);
            i = 0;
            Global.ALL_CONTACTS = allContacts;
            for(Contact item:Global.ALL_CONTACTS)
            {
                item.id = i++;
            }
            Global.GP_ALL_CONTACTS = gpContacts;
            Global.ROBI_ALL_CONTACTS = robiContacts;
            Global.AIRTEL_ALL_CONTACTS = airtelContact;
            Global.TELETALK_ALL_CONTACTS = teletalkContacts;
            Global.BL_ALL_CONTACTS = blContacts;
            Log.d("Check", "Complete");
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    private void sort(ArrayList<Contact> allContacts) {

        Collections.sort(allContacts, new Comparator<Contact>() {
            public int compare(Contact o1, Contact o2) {
                if(o1.name.equalsIgnoreCase(o2.name))
                {
                    return o1.number.compareToIgnoreCase(o2.number);
                }
                return o1.name.compareToIgnoreCase(o2.name);
            }
        });
    }

    private boolean isGpNumber(String number) {
        Pattern pattern;
        String gpPattern = "[+]?(88)?(017)[0-9]{8}";
        pattern = Pattern.compile(gpPattern);
        return pattern.matcher(number).matches();
    }

    private boolean isRobiNumber(String number) {
        Pattern pattern;
        String gpPattern = "[+]?(88)?(018)[0-9]{8}";
        pattern = Pattern.compile(gpPattern);
        return pattern.matcher(number).matches();
    }

    private boolean isBlNumber(String number) {
        Pattern pattern;
        String gpPattern = "[+]?(88)?(019)[0-9]{8}";
        pattern = Pattern.compile(gpPattern);
        return pattern.matcher(number).matches();
    }

    private boolean isTeleTalkNumber(String number) {
        Pattern pattern;
        String gpPattern = "[+]?(88)?(015)[0-9]{8}";
        pattern = Pattern.compile(gpPattern);
        return pattern.matcher(number).matches();
    }

    private boolean isAirtelNumber(String number) {
        Pattern pattern;
        String gpPattern = "[+]?(88)?(016)[0-9]{8}";
        pattern = Pattern.compile(gpPattern);
        return pattern.matcher(number).matches();
    }

    @Override
    protected void onPostExecute(Boolean isContactLoadComplete) {
        mContactListener.onReadContactsCompleteResponse(isContactLoadComplete);
    }
}