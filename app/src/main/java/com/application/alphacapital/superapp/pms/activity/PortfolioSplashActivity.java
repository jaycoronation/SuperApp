package com.application.alphacapital.superapp.pms.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.mit.mitsutils.MitsUtils;
import com.application.alphacapital.superapp.R;
import com.application.alphacapital.superapp.pms.utils.ApiUtils;
import com.application.alphacapital.superapp.pms.utils.AppUtils;
import com.application.alphacapital.superapp.pms.utils.Converter;
import com.application.alphacapital.superapp.pms.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class PortfolioSplashActivity extends AppCompatActivity
{
    private Activity activity;

    private SessionManager sessionManager ;

    private CountDownTimer timer ;

    private LinearLayout llSplash;

    public static String PACKAGE_NAME;

    public static boolean isVersionMismatch = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                Window w = getWindow();
                w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.portfolio_activity_splash);


        activity = PortfolioSplashActivity.this ;
        sessionManager = new SessionManager(activity);
setStatusBarGradiant(activity);
        int value = 15000000 ;

        String convertAmount = Converter.convertIntoEnglish(value) ;
        AppUtils.printLog("Convert Amount",convertAmount);

        llSplash = findViewById(R.id.llSplash);

        PACKAGE_NAME = getApplicationContext().getPackageName();

        StartAnimations();

        /*if(AppUtils.isOnline())
        {
            try
            {
                ValidateAppVersionAsync validateAppVersionAsync = new ValidateAppVersionAsync();
                validateAppVersionAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            AppUtils.showSnackBar(llSplash,"Please check your internet connection.",activity);
        }*/

    }

    void setStatusBarGradiant(Activity activity)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(activity,android.R.color.transparent));
            window.setNavigationBarColor(ContextCompat.getColor(activity,android.R.color.black));
            window.setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.vault_bg_gradient));
        }
    }

    private void StartAnimations()
    {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        llSplash.clearAnimation();
        llSplash.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.scale_animation);
        anim.reset();
        ImageView imgLogo = findViewById(R.id.imgLogo);
        imgLogo.clearAnimation();
        imgLogo.startAnimation(anim);

        goThrough();
    }

    private void goThrough()
    {
        try
        {
            timer = new CountDownTimer(5000, 10000)
            {
                @Override
                public void onTick(long millisUntilFinished)
                {

                }

                @Override
                public void onFinish()
                {
                    try
                    {
                        Intent intent = null;
                        if(sessionManager.isLoggedIn())
                        {
                            intent = new Intent(activity, AllUsersActivity.class);
                        }
                        else
                        {
                            //Call Tutorial Activity
                            intent = new Intent(activity, PortfolioLoginActivity.class);
                        }

                        startActivity(intent);
                        AppUtils.startActivityAnimation(activity);
                        finish();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            };
            timer.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private class ValidateAppVersionAsync extends AsyncTask<Void, Void, Void>
    {
        private String status = "fail";

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            try
            {
                String version = "";

                PackageInfo pInfo;
                try
                {
                    pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    version = pInfo.versionName;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                String URL = ApiUtils.APPVERSION_GETDETAIL;

                HashMap<String, String> nameValuePairs = new HashMap<String, String>();

                nameValuePairs.put("TokenId", ApiUtils.API_TOKEN_ID);

                String serverResponse = MitsUtils.readJSONServiceUsingPOST(URL,nameValuePairs);

                try
                {
                    JSONObject jsonObject = new JSONObject(serverResponse);
                    status = jsonObject.getString("status");

                    //version: "1.0.2",
                    //message: "",
                    //code: "1",
                    //status: "success

                    if(status!=null && status.equalsIgnoreCase("1"))
                    {
                        JSONObject datObj = jsonObject.getJSONObject("data");
                        final String appVersion = datObj.getString("Android_Version");

                        String[] arrVersionApp = version.split("\\.");
                        String[] arrVersionLive = appVersion.split("\\.");

                        int verApp = Integer.parseInt(arrVersionApp[0]+arrVersionApp[1]);
                        int verLive = Integer.parseInt(arrVersionLive[0]+arrVersionLive[1]);

                        if(verApp < verLive)
                        {
                            System.out.println("app needs to be upgraded");
                            isVersionMismatch = true;
                        }
                        else
                        {
                            isVersionMismatch = false;
                            System.out.println("app is upgraded");
                        }
                    }
                    else
                    {
                        isVersionMismatch = false;
                    }
                }
                catch(JSONException je)
                {
                    je.printStackTrace();
                    status = "Fail";
                }
                catch (Exception ex)
                {
                    status = "Fail";
                    ex.printStackTrace();
                }
            } catch(Exception jse)
            {
                status = "Fail";
                jse.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            goThrough();
        }
    }
}