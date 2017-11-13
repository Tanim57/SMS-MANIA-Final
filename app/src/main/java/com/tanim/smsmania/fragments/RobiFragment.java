package com.tanim.smsmania.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.tanim.smsmania.interfaces.CountCustomContactListener;
import com.tanim.smsmania.interfaces.FragmentInterface;
import com.tanim.smsmania.model.Contact;
import com.tanim.smsmania.ui.AllContactAdapter;
import com.tanim.smsmania.ui.ContactAdapter;
import com.tanim.smsmania.ui.DividerItemDecoration;

import java.util.ArrayList;

/**
 * Created by Tanim on 10/24/2017.
 */

@SuppressLint("ValidFragment")
public class RobiFragment extends Fragment implements FragmentInterface {
    ListView lvAllList;
    private AllContactAdapter adapter;
    private RecyclerView mRecyclerView;
    private ContactAdapter mAdapter;
    private Context mContext;
    private CountCustomContactListener mDelegate;
    public RobiFragment(Context mContext, CountCustomContactListener mDelegate) {
        this.mContext = mContext;
        this.mDelegate = mDelegate;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_robi, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_robi_contact);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext));

        mAdapter = new ContactAdapter(mContext,Global.ROBI_ALL_CONTACTS,mDelegate);
        //mAdapter.setContactList(Global.ROBI_ALL_CONTACTS);
        //mAdapter.setContactList(Global.ROBI_ALL_CONTACTS);
        mRecyclerView.setAdapter(mAdapter);
        //mAdapter.setContactList(Global.ROBI_ALL_CONTACTS);
        Global.robiAllContactAdapter = mAdapter;



        /*lvAllList = (ListView) view.findViewById(R.id.lv_robi_all_contact);
        adapter = new AllContactAdapter(getContext(), Global.ROBI_ALL_CONTACTS);
        lvAllList.setTextFilterEnabled(true);
        lvAllList.setAdapter(adapter);*/
        return view;
    }

    @Override
    public ContactAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public ArrayList<Contact> getContactList() {
        return Global.ROBI_ALL_CONTACTS;
    }

    @Override
    public boolean getMarkedStatus() {
        return Global.robiallMarked;
    }
    @Override
    public void setMarkedStatus(boolean marked)
    {
        Global.robiallMarked = marked;
    };
}
