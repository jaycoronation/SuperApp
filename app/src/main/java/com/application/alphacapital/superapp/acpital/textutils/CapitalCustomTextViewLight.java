package com.application.alphacapital.superapp.acpital.textutils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatTextView;

import com.application.alphacapital.superapp.R;


public class CapitalCustomTextViewLight extends AppCompatTextView
{
    public CapitalCustomTextViewLight(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        if (!isInEditMode())
        {
            setType(context);
        }
    }

    public CapitalCustomTextViewLight(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        if (!isInEditMode())
        {
            setType(context);
        }
    }

    public CapitalCustomTextViewLight(Context context)
    {
        super(context);
        if (!isInEditMode())
        {
            setType(context);
        }
    }

    private void setType(Context context)
    {
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.font_light)));
        this.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4.0f,  getResources().getDisplayMetrics()), 1.0f);
    }
}