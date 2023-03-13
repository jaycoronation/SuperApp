package com.application.alphacapital.superapp.pms.views;

import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

public class EditTextErrorTypeFace extends MetricAffectingSpan
{
    private final Typeface mNewFont;

    public EditTextErrorTypeFace(Typeface newFont)
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