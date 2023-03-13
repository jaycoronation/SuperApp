package com.application.alphacapital.superapp.pms.utils;

import android.content.Context;
import android.widget.TextView;

import com.application.alphacapital.superapp.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
public class MyMarkerView extends MarkerView
{
    private TextView tvContent;

    public MyMarkerView(Context context, int layoutResource)
    {
        super(context, layoutResource);
        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight)
    {
        if (e instanceof CandleEntry)
        {
            CandleEntry ce = (CandleEntry) e;
            /*Log.e("Graph Candle Content",ce.getHigh() + "");*/
            tvContent.setText(AppUtils.convertToTwoDecimalValue("" + ce.getHigh()));
        }
        else
        {
            /*Log.e("Graph Content",e.getY() + "");*/
            tvContent.setText(AppUtils.convertToTwoDecimalValue("" + e.getY()));
        }
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset()
    {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}