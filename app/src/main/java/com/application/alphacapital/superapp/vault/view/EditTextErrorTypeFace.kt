package com.alphaestatevault.view

import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.MetricAffectingSpan

class EditTextErrorTypeFace(private val mNewFont: Typeface) : MetricAffectingSpan() {

    override fun updateDrawState(ds: TextPaint) {
        ds.typeface = mNewFont
    }

    override fun updateMeasureState(paint: TextPaint) {
        paint.typeface = mNewFont
    }
}