package com.tanim.smsmania.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;

import java.util.List;

/**
 * Created by Tanim on 10/30/2017.
 */
@SuppressLint("NewApi")
public class MobileOperator {
    private CharSequence carrierName ;
    private CharSequence displayName;
    private int mcc;
    private int mnc;
    private String subscriptionInfoNumber;
    private int subscriptionId;
    private SubscriptionInfo mSubscriptionInfo;

    public MobileOperator(SubscriptionInfo subscriptionInfo) {
        this.mSubscriptionInfo = subscriptionInfo;
        carrierName = subscriptionInfo.getCarrierName();
        displayName = subscriptionInfo.getDisplayName();
        mcc = subscriptionInfo.getMcc();
        mnc = subscriptionInfo.getMnc();
        subscriptionId = subscriptionInfo.getSubscriptionId();
        subscriptionInfoNumber = subscriptionInfo.getNumber();
        Log.d("Check",carrierName+" "+displayName+" "+" "+mcc+" "+" "+mnc+" "+subscriptionId+" "+subscriptionInfoNumber);
    }

    public CharSequence getCarrierName() {
        return carrierName;
    }

    public CharSequence getDisplayName() {
        return displayName;
    }

    public int getSubscriptionId() {
        return subscriptionId;
    }
}
