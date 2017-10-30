package com.tanim.smsmania.model;

/**
 * Created by Tanim on 10/24/2017.
 */

public class Contact {
    public String name;
    public String number;
    public int id;
    public boolean isSelected;

    public Contact(String name, String number,boolean isSelected,int id) {
        this.name = name;
        this.number = number;
        this.isSelected = isSelected;
        this.id = id;
    }
}
