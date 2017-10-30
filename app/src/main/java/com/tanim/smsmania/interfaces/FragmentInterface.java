package com.tanim.smsmania.interfaces;

import com.tanim.smsmania.model.Contact;
import com.tanim.smsmania.ui.ContactAdapter;

import java.util.ArrayList;

/**
 * Created by Tanim on 10/26/2017.
 */

public interface FragmentInterface {
    ContactAdapter getAdapter();
    ArrayList<Contact> getContactList();
    boolean getMarkedStatus();
    void setMarkedStatus(boolean marked);
}
