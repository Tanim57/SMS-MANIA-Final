package com.tanim.smsmania.tasks;

import android.os.AsyncTask;

import com.tanim.smsmania.Common.Global;
import com.tanim.smsmania.interfaces.FragmentInterface;
import com.tanim.smsmania.ui.ContactAdapter;

/**
 * Created by Tanim on 10/26/2017.
 */

public class NotifyFragmentsTasks extends AsyncTask{

    private FragmentInterface fragmentInterface;
    public NotifyFragmentsTasks(FragmentInterface fragmentInterface) {
        this.fragmentInterface = fragmentInterface;
    }

    @Override
    protected Object doInBackground(Object[] params) {

        return null;
    }
}
