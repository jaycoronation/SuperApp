package com.application.alphacapital.superapp.acpital.textutils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;

import com.application.alphacapital.superapp.R;
import com.application.alphacapital.superapp.acpital.utils.AppUtils;
import com.google.android.material.snackbar.Snackbar;


public class CapitalSnackbarHelper
{
    private Context context;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void configSnackbar(Activity context, Snackbar snack)
    {
        context = context;
        addMargins(snack);
        setRoundBordersBg(context, snack);
        ViewCompat.setElevation(snack.getView(), 6f);
    }

    private static void addMargins(Snackbar snack)
    {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) snack.getView().getLayoutParams();
        params.setMargins(12, 12, 12, 20);
        snack.getView().setLayoutParams(params);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static void setRoundBordersBg(Activity context, Snackbar snackbar)
    {
        snackbar.getView().setBackground(context.getDrawable(R.drawable.capital_snackbar_bg));
        TextView textView = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
        textView.setMaxLines(5);
        textView.setTypeface(AppUtils.getTypefaceRegular(context));
    }
}
