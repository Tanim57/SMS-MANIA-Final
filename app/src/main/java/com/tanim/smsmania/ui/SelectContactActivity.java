package com.tanim.smsmania.ui;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.tanim.smsmania.Common.Global;
import com.tanim.smsmania.R;
import com.tanim.smsmania.fragments.AirTelFragment;
import com.tanim.smsmania.fragments.AllFragment;
import com.tanim.smsmania.fragments.BanglaLinkFragment;
import com.tanim.smsmania.fragments.GrameenPhoneFragment;
import com.tanim.smsmania.fragments.RobiFragment;
import com.tanim.smsmania.fragments.TeletalkFragment;
import com.tanim.smsmania.interfaces.CommunicationFragmentsListener;
import com.tanim.smsmania.interfaces.FragmentInterface;
import com.tanim.smsmania.model.Contact;
import com.tanim.smsmania.tasks.NotifyFragmentsTasks;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Tanim on 10/24/2017.
 */

public class SelectContactActivity extends AppCompatActivity implements CommunicationFragmentsListener {
    public static Context mContext;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AllFragment allFragment;
    private GrameenPhoneFragment grameenPhoneFragment;
    private RobiFragment robiFragment;
    private AirTelFragment airTelFragment;
    private BanglaLinkFragment banglaLinkFragment;
    private TeletalkFragment teletalkFragment;
    private SearchView searchView;
    private ViewPagerAdapter adapter;
    private FragmentInterface fragmentInterface;
    private String SearchViewText = "";
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);
        intView();
        setupViewPager(viewPager);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void intView() {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        allFragment = new AllFragment();
        grameenPhoneFragment = new GrameenPhoneFragment();
        robiFragment = new RobiFragment();
        banglaLinkFragment = new BanglaLinkFragment();
        teletalkFragment = new TeletalkFragment();
        airTelFragment = new AirTelFragment();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        //viewPager.setOffscreenPageLimit(0);
        viewPager.setPageTransformer(true,new ZoomOutPageTransformer());
        fragmentInterface = allFragment;

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                fragmentInterface = (FragmentInterface) adapter.instantiateItem(viewPager,position);
                if(Global.markEdIds!=null && Global.markEdIds.size()>0)
                {
                    fragmentInterface.getAdapter().setContactList(Global.ALL_CONTACTS);
                    Global.markEdIds.clear();
                }
                //Global.markEdIds = null;
            }

            @Override
            public void onPageSelected(int position) {
                fragmentInterface = (FragmentInterface) adapter.instantiateItem(viewPager,position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void setupViewPager(ViewPager viewPager) {
        adapter.addFragment(allFragment, "ALL");
        adapter.addFragment(grameenPhoneFragment, "GP");
        adapter.addFragment(robiFragment, "Robi");
        adapter.addFragment(airTelFragment, "Airtel");
        adapter.addFragment(banglaLinkFragment, "BL");
        adapter.addFragment(teletalkFragment, "Teletalk");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void sendMessage(boolean marked) {


    }

    public int getSelectedContact() {
        int count =0;
        for(boolean b:Global.isMarked)
        {
            if(b)
            {
                count++;
            }
        }
        return count;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        View searchPlateView = searchView.findViewById(android.support.v7.appcompat.R.id.search_bar);
        searchPlateView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                perFormFilter(query);
                SearchViewText = query;
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                perFormFilter(newText);
                SearchViewText = newText;
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.select_all:
                ContactAdapter contactAdapter = fragmentInterface.getAdapter();
                boolean isMarked = fragmentInterface.getMarkedStatus();
                contactAdapter.selectAll(fragmentInterface.getContactList(),!isMarked);
                if(isMarked)
                {
                    item.setIcon(R.mipmap.select_all);
                    fragmentInterface.setMarkedStatus(!isMarked);
                    if(SearchViewText!=null && SearchViewText.length()>0)
                    {
                        fragmentInterface.getAdapter().getFilter().filter(SearchViewText);
                    }
                }
                else {
                    //Toast.makeText(getApplicationContext(),"Select All",Toast.LENGTH_SHORT).show();
                    item.setIcon(R.mipmap.unselect);
                    fragmentInterface.setMarkedStatus(!isMarked);
                    if(SearchViewText!=null && SearchViewText.length()>0)
                    {
                        fragmentInterface.getAdapter().getFilter().filter(SearchViewText);
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void perFormFilter(String text)
    {
        ContactAdapter contactAdapter = null;
        ArrayList<Contact> contacts=null;

        if(fragmentInterface!=null)
        {
            contactAdapter = fragmentInterface.getAdapter();
            //contacts = fragmentInterface.getContactList();
        }
        if(contactAdapter!=null)
        {
            contactAdapter.getFilter().filter(text);
            contactAdapter.setContactList(Global.ALL_CONTACTS);
        }
    }

    /*@Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            //findViewById(R.id.default_title).setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }*/

    // **ERROR on 'ViewPager.PageTransformer --> ViewPager cannot be resolved to a type **
    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private float MIN_SCALE = 0.85f;
        private float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int totalContactSelected =getSelectedContact();
        builder.setMessage("Total Selected Contact : "+ totalContactSelected)
                .setCancelable(true)
                .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Global.isCustomContactedSelected = true;
                        finish();
                    }
                })
                .setNegativeButton("DISCARD", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Global.isCustomContactedSelected = false;
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
