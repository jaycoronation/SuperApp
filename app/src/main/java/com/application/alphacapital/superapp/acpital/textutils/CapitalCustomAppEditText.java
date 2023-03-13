package com.application.alphacapital.superapp.acpital.textutils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.application.alphacapital.superapp.R;
import com.google.android.material.textfield.TextInputEditText;


public class CapitalCustomAppEditText extends TextInputEditText
{
    public CapitalCustomAppEditText(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        setType(context);
    }

    public CapitalCustomAppEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setType(context);
    }

    public CapitalCustomAppEditText(Context context)
    {
        super(context);
        setType(context);
    }

    private void setType(Context context)
    {
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.font_regular)));
    }
}