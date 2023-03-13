package com.application.alphacapital.superapp.acpital.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Html;
import android.widget.Button;
import android.widget.TextView;

import com.application.alphacapital.superapp.R;


public class LogoutManager 
{
	private Activity activity;
	private SessionManager sessionManager;
	
	public LogoutManager(Activity activity) 
	{
		this.activity = activity;
	}

	public void logout() 
	{
		String message = "";
		try {
			message = activity.getResources().getString(R.string.logout_message);
            String appname = activity.getResources().getString(R.string.app_name);
            String boldappname = "<b>" + appname + "</b>";
            message = message.replace("XXX", boldappname);
		} catch (Exception e1) {
			message = activity.getResources().getString(R.string.logout_message);
		}
		try {
			AlertDialog builder = new AlertDialog.Builder(activity)
            .setTitle("Logout")
            .setMessage(Html.fromHtml(message))
            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    try {
                        dialog.dismiss();
                        dialog.cancel();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    sessionManager = new SessionManager(activity);
                    sessionManager.logoutUser();
                }
             })
            .setNegativeButton("No", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    try {
                        dialog.dismiss();
                        dialog.cancel();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
             })
            .create();
            builder.show();

            TextView textView = (TextView) builder.findViewById(android.R.id.message);
            textView.setTypeface(AppUtils.getTypefaceRegular(activity));

            Button button = builder.getButton(DialogInterface.BUTTON_POSITIVE);
            button.setTypeface(AppUtils.getTypefaceMedium(activity));

            Button button1 = builder.getButton(DialogInterface.BUTTON_NEGATIVE);
            button1.setTypeface(AppUtils.getTypefaceMedium(activity));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}