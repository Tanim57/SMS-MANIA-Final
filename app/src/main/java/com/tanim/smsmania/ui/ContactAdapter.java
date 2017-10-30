package com.tanim.smsmania.ui;

import android.content.Context;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.tanim.smsmania.Common.Global;
import com.tanim.smsmania.R;
import com.tanim.smsmania.model.Contact;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Tanim on 10/25/2017.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.Holder> implements Filterable {

    private static final String TAG = "Contact Adapter";
    private Context mContext;
    private ArrayList<Contact> mContacts,mOriginalValues;
    private boolean[] itemChecked;

    public ContactAdapter(Context context,ArrayList<Contact> arrayList){
        mContext = context;
        mContacts = arrayList;
        mOriginalValues = arrayList;
        itemChecked = new boolean[arrayList.size()];

    }

    public void setContactList(ArrayList<Contact> arrayList){
        //mContacts = arrayList;
        if(Global.markEdIds==null || arrayList==null)
        {
            return;
        }
        else if(Global.markEdIds.size()<=0 || arrayList.size()<=0)
        {
            return;
        }
        else
        {
            for (int i:Global.markEdIds)
            {
                Contact item = arrayList.get(i);
                item.isSelected = Global.isMarked[item.id];
                Log.d("Check Index",i+"");
            }
            Global.markEdIds.clear();
            notifyDataSetChanged();
        }
    }

    public void selectAll(ArrayList<Contact> arrayList,boolean mark)
    {
        if(arrayList==null || arrayList.size()<=0)
        {
            return;
        }
        for(Contact contact:arrayList)
        {
            Global.isMarked[contact.id] = mark;
            contact.isSelected = mark;
        }
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(final ContactAdapter.Holder holder, final int position) {
        if (holder != null){
            final Contact contact = mContacts.get(position);

            holder.tvName.setText(contact.id+" "+contact.name);
            holder.tvNumber.setText(contact.number);


            /*holder.checkContact.setOnCheckedChangeListener (new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged (CompoundButton btn, boolean isChecked) {
                    contact.isSelected = isChecked;
                    Global.isMarked[contact.id] = isChecked;
                    Global.markEdIds.add(contact.id);
                }
            });*/
            //
            holder.checkContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.checkContact.isChecked())
                    {
                        contact.isSelected = true;
                        Global.isMarked[contact.id] = true;
                        Global.markEdIds.add(contact.id);
                    }
                    else
                    {
                        Global.allMarked = false;
                        contact.isSelected = false;
                        Global.isMarked[contact.id] = false;
                        Global.markEdIds.add(contact.id);
                    }
                }
            });
            holder.checkContact.setChecked(contact.isSelected);

            holder.itemClickListener = new Holder.ItemClickListener() {
                @Override
                public void onItemClick() {
                    contact.isSelected = !contact.isSelected;
                    holder.checkContact.setChecked(contact.isSelected);
                    Global.isMarked[contact.id] = contact.isSelected;
                    Global.markEdIds.add(contact.id);
                }
            };

        }
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    public static class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ItemClickListener itemClickListener;
        CheckBox checkContact;
        TextView tvName;
        TextView tvNumber;

        public Holder(View itemView) {
            super(itemView);

            checkContact = (CheckBox) itemView.findViewById(R.id.check_contact);
            tvName = (TextView) itemView.findViewById(R.id.tv_contact_name);
            tvNumber = (TextView) itemView.findViewById(R.id.tv_contact_number);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick();
        }

        interface ItemClickListener{
            void onItemClick();
        }
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                mContacts = (ArrayList<Contact>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values

                ArrayList<Contact> FilteredArrList = new ArrayList<Contact>();
                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<Contact>(mContacts); // saves the original data in mOriginalValues
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
                        if (nameData.toLowerCase().startsWith(constraint.toString())) {
                            Contact contact = new Contact(mOriginalValues.get(i).name,mOriginalValues.get(i).number,Global.isMarked[mOriginalValues.get(i).id],mOriginalValues.get(i).id);
                            FilteredArrList.add(contact);
                        }
                        else if (numberData.toLowerCase().contains(constraint.toString())) {
                            Contact contact = new Contact(mOriginalValues.get(i).name,mOriginalValues.get(i).number,Global.isMarked[mOriginalValues.get(i).id],mOriginalValues.get(i).id);
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