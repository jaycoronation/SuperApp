package com.application.alphacapital.superapp.pms.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

import com.application.alphacapital.superapp.pms.utils.AppUtils;

public class CustomButton extends AppCompatButton
{
    public CustomButton(Context context) {
        super(context);
        setTypeFace(context);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeFace(context);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeFace(context);
    }

    private void setTypeFace(Context context)
    {
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), AppUtils.boldFonts));
    }
}
