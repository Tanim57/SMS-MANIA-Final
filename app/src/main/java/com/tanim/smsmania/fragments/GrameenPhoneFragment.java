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

public class GrameenPhoneFragment extends Fragment implements FragmentInterface {
    ListView lvGPAllList;
    private RecyclerView mRecyclerView;
    private ContactAdapter mAdapter;
    public GrameenPhoneFragment() {
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
        View view = inflater.inflate(R.layout.fragment_grameenphone, container, false);
        /*lvGPAllList = (ListView) view.findViewById(R.id.lv_gp_all_contact);
        AllContactAdapter adapter = new AllContactAdapter(getContext(), Global.GP_ALL_CONTACTS);
        lvGPAllList.setAdapter(adapter);*/
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_gp_contact);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        mAdapter = new ContactAdapter(getContext(),Global.GP_ALL_CONTACTS);
        mRecyclerView.setAdapter(mAdapter);
        Global.gpAllContactAdapter = mAdapter;
        //mAdapter.setContactList(Global.GP_ALL_CONTACTS);
        return view;
    }

    @Override
    public ContactAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public ArrayList<Contact> getContactList() {
        return Global.GP_ALL_CONTACTS;
    }

    @Override
    public boolean getMarkedStatus()
    {
        return Global.gpallMarked;
    };
    @Override
    public void setMarkedStatus(boolean marked)
    {
        Global.gpallMarked = marked;
    };
}
