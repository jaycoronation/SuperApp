package com.application.alphacapital.superapp.acpital.textutils;

import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

public class CapitalEditTextErrorTypeFace extends MetricAffectingSpan
{
    private final Typeface mNewFont;

    public CapitalEditTextErrorTypeFace(Typeface newFont)
    {
        mNewFont = newFont;
    }

    @Override
    public void updateDrawState(TextPaint ds)
    {
        ds.setTypeface(mNewFont);
    }

    @Override
    public void updateMeasureState(TextPaint paint)
    {
        paint.setTypeface(mNewFont);
    }
}