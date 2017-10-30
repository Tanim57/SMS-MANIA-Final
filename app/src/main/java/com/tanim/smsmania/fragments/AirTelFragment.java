package com.tanim.smsmania.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tanim.smsmania.Common.Global;
import com.tanim.smsmania.R;
import com.tanim.smsmania.interfaces.FragmentInterface;
import com.tanim.smsmania.model.Contact;
import com.tanim.smsmania.ui.AllContactAdapter;
import com.tanim.smsmania.ui.ContactAdapter;
import com.tanim.smsmania.ui.DividerItemDecoration;

import java.util.ArrayList;

/**
 * Created by Tanim on 10/24/2017.
 */

public class AirTelFragment extends Fragment implements FragmentInterface {
    ListView lvAllList;
    private RecyclerView mRecyclerView;
    private ContactAdapter mAdapter;
    public AirTelFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_airtel, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_airtel_contact);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext()));

        mAdapter = new ContactAdapter(getContext(),Global.AIRTEL_ALL_CONTACTS);
        mRecyclerView.setAdapter(mAdapter);
        Global.airtelAllContactAdapter = mAdapter;
        //mAdapter.setContactList(Global.AIRTEL_ALL_CONTACTS);
        return view;
    }

    @Override
    public ContactAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public ArrayList<Contact> getContactList() {
        return Global.AIRTEL_ALL_CONTACTS;
    }
    @Override
    public boolean getMarkedStatus()
    {
        return Global.airtelallMarked;
    };
    @Override
    public void setMarkedStatus(boolean marked)
    {
        Global.airtelallMarked = marked;
    };
}
