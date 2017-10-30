package com.tanim.smsmania.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.tanim.smsmania.R;

/**
 * Created by Tanim on 10/24/17.
 */

public class TextViewPlus extends AppCompatTextView {
    private static final String TAG = "RAB_TVPlus";

    public TextViewPlus(Context context) {
        super(context);
    }

    public TextViewPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public TextViewPlus(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomFont(context, attrs);
    }

    public void setCustomFont(Context context, AttributeSet attributeSet){
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.TextViewPlus);
        String fontName = typedArray.getString(R.styleable.TextViewPlus_font_name);
        boolean success = setCustomFont(context, fontName);
        typedArray.recycle();
    }

    public boolean setCustomFont(Context context, String fontName){
        Typeface typeface;
        try {
            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName);
        } catch (RuntimeException ex){
            return false;
        }
        setTypeface(typeface);
        return true;
    }
}
