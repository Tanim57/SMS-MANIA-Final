package com.tanim.smsmania.ui;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import com.tanim.smsmania.R;
import com.tanim.smsmania.model.Contact;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Tanim on 10/24/2017.
 */

public class AllContactAdapter extends ArrayAdapter<Contact> implements Filterable {

    private ArrayList<Contact> mOriginalValues; // Original Values
    private ArrayList<Contact> mDisplayedValues;    // Values to be displayed
    LayoutInflater inflater;
    Contact contact = null;

    public AllContactAdapter(Context context,ArrayList<Contact> contacts) {

        super(context,0,contacts);
        mOriginalValues = contacts;
        mDisplayedValues = contacts;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return mDisplayedValues.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_contact, parent, false);
        }
        CheckBox checkContact = (CheckBox) convertView.findViewById(R.id.check_contact);

        TextView tvName = (TextView) convertView.findViewById(R.id.tv_contact_name);
        TextView tvNumber = (TextView) convertView.findViewById(R.id.tv_contact_number);

        contact = mDisplayedValues.get(position);
        tvName.setText(String.format(Locale.ENGLISH,contact.name).trim());
        tvNumber.setText(String.format(Locale.ENGLISH,contact.number).trim());

        checkContact.setOnCheckedChangeListener (new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged (CompoundButton btn, boolean isChecked) {
                contact.isSelected = isChecked;
            }
        });
        checkContact.setChecked(contact.isSelected);
        return convertView;



    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                mDisplayedValues = (ArrayList<Contact>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<Contact> FilteredArrList = new ArrayList<Contact>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<Contact>(mDisplayedValues); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        String nameData = mOriginalValues.get(i).name;
                        String numberData = mOriginalValues.get(i).number;
                        if (nameData.toLowerCase().contains(constraint.toString())) {
                            //Contact contact = new Contact(mOriginalValues.get(i).name,mOriginalValues.get(i).number,mOriginalValues.get(i).isSelected);

                                FilteredArrList.add(contact);


                        }
                        else if (numberData.toLowerCase().contains(constraint.toString())) {
                            //Contact contact = new Contact(mOriginalValues.get(i).name,mOriginalValues.get(i).number,mOriginalValues.get(i).isSelected);
                            FilteredArrList.add(contact);
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}
