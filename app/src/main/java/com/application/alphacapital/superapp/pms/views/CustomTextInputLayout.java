package com.application.alphacapital.superapp.pms.views;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.application.alphacapital.superapp.pms.utils.AppUtils;
import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.Field;
import java.util.Objects;


public class CustomTextInputLayout extends TextInputLayout
{
    public CustomTextInputLayout(Context context)
    {
        super(context);
        initFont(context);
    }

    public CustomTextInputLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initFont(context);
    }

    @Override
    public void setError(@Nullable CharSequence error)
    {
        if(error != null)
        {
            final Typeface typeface = Typeface.createFromAsset(this.getContext().getAssets(), AppUtils.regularFonts);
            final SpannableString ss = new SpannableString(error);
            ss.setSpan(new EditTextErrorTypeFace(typeface), 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            super.setError(ss);
        }
        else
        {
            super.setError(error);
        }
    }

    private void initFont(Context context)
    {
        final Typeface typeface = Typeface.createFromAsset(context.getAssets(), AppUtils.semiBoldFonts);

        EditText editText = getEditText();
        if (editText != null)
        {
            editText.setTypeface(typeface);
        }
        
        try
        {
            // Retrieve the CollapsingTextHelper Field
            final Field cthf = TextInputLayout.class.getDeclaredField("collapsingTextHelper");
            cthf.setAccessible(true);

            // Retrieve an instance of CollapsingTextHelper and its TextPaint
            final Object cth = cthf.get(this);
            assert cth != null;
            final Field tpf = cth.getClass().getDeclaredField("textPaint");
            tpf.setAccessible(true);

            // Apply your Typeface to the CollapsingTextHelper TextPaint
            ((TextPaint) Objects.requireNonNull(tpf.get(cth))).setTypeface(typeface);
        }
        catch (Exception ignored)
        {
        	ignored.printStackTrace();
        }
    }
}