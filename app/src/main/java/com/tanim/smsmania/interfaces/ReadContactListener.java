package com.tanim.smsmania.interfaces;

import com.tanim.smsmania.model.Contact;

import java.util.ArrayList;

/**
 * Created by Tanim on 10/24/2017.
 */

public interface ReadContactListener {
    void onPreExecute();
    void onReadContactsCompleteResponse(boolean isContactLoadComplete);
    void onPostExecute();
}
