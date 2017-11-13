package com.tanim.smsmania.Common;

import com.tanim.smsmania.model.Contact;
import com.tanim.smsmania.ui.AllContactAdapter;
import com.tanim.smsmania.ui.ContactAdapter;

import java.util.ArrayList;

/**
 * Created by Tanim on 10/24/2017.
 */

public class Global {
    public static ArrayList<Contact> ALL_CONTACTS;
    public static ArrayList<Contact> GP_ALL_CONTACTS;
    public static ArrayList<Contact> ROBI_ALL_CONTACTS;
    public static ArrayList<Contact> BL_ALL_CONTACTS;
    public static ArrayList<Contact> AIRTEL_ALL_CONTACTS;
    public static ArrayList<Contact> TELETALK_ALL_CONTACTS;
    public static ContactAdapter allContactAdapter;
    public static ContactAdapter gpAllContactAdapter;
    public static ContactAdapter robiAllContactAdapter;
    public static ContactAdapter blAllContactAdapter;
    public static ContactAdapter airtelAllContactAdapter;
    public static ContactAdapter teletalkAllContactAdapter;
    public static boolean[] isMarked;
    public static ArrayList<Integer> markEdIds = new ArrayList<>();
    public static boolean allMarked = false;
    public static boolean gpallMarked = false;
    public static boolean robiallMarked = false;
    public static boolean blallMarked = false;
    public static boolean teletalkallMarked = false;
    public static boolean airtelallMarked = false;
    public static String[] OperatorList = new String[]{"All Contact", "All GP", "All Robi","All Airtel",
            "All Teletalk","All Banglalink","Custom Contact List"};
    public static int NUMBER_OF_OPERATORS=0;
    public static int CURRENT_OPERATOR=0;
    public static boolean isCustomContactedSelected = false;
    public static int customSelectContact = 0;
    public static boolean isNormalSMS = true;
}
