package com.application.alphacapital.superapp.pms.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.application.alphacapital.superapp.pms.activity.AllUsersActivity;
import com.application.alphacapital.superapp.pms.beans.BSMovementResponseModel;
import com.application.alphacapital.superapp.pms.beans.PerformanceTemp;
import com.application.alphacapital.superapp.pms.beans.PortfoliaResponseModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class SessionManager
{
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Activity activity;
    private int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "LoginPref_Vadilal";
    private static final String IS_LOGIN = "loginStatus";
    public static final String KEY_ID = "UserId";
    public static final String KEY_ADMIN_ID = "AdminId";
    public static final String KEY_USERNAME = "UserName";
    public static final String KEY_PASSWORD="Password";
    public static final String KEY_FIRST_NAME = "FirstName";
    public static final String KEY_LAST_NAME = "LastName";
    public static final String KEY_EMAIL = "EmailId";

    public static final String KEY_PORTFOLIO = "Portfolio";
    public static final String KEY_PERFORMANCE = "Performance";
    public static final String KEY_NEXTYEAR = "NextYear";
    public static final String KEY_PREVIOUSYEAR = "PreviousYear";
    public static final String KEY_BSMOVEMENT = "BsMovement";
    public static final String KEY_BSMOVEMENT_DATA = "BsMovementData";

    private Context context;

    public SessionManager(Activity activity)
    {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        preferences = activity.getApplicationContext().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    }

    public SessionManager(Context context)
    {
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    }

    /*use details*/
    public void createLoginSession(String userid,String adminId , String userName , String firstName, String lastName , String email)
    {
        editor = preferences.edit();
        editor.putString(KEY_ID, userid);
        editor.putString(KEY_ADMIN_ID, adminId);
        editor.putString(KEY_USERNAME, userid);
        editor.putString(KEY_FIRST_NAME, firstName);
        editor.putString(KEY_LAST_NAME, lastName);
        editor.putString(KEY_EMAIL,email);

        editor.putBoolean(IS_LOGIN, true);
        editor.apply();
    }


    public void setIsLoggedIn(boolean isLoggedIn)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_LOGIN, isLoggedIn);
        editor.apply();
    }

    public boolean isLoggedIn()
    {
        return preferences.getBoolean(IS_LOGIN, false);
    }

    public String getUserId()
    {
        String uid = preferences.getString(KEY_ID, "0");
        return uid;
    }

    public String getAdminId()
    {
        String adminId = preferences.getString(KEY_ADMIN_ID, "0");
        return adminId;
    }

    public void setPassword(String password)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
    }

    public String getPassword()
    {
        String password = preferences.getString(KEY_PASSWORD, "");
        return password;
    }

    public String getFirstName()
    {
        return preferences.getString(KEY_FIRST_NAME,"");
    }

    public void setFirstName(String name)
    {
        editor = preferences.edit();
        editor.putString(KEY_FIRST_NAME,name);
        editor.commit();
    }
    public String getLastName()
    {
        return preferences.getString(KEY_LAST_NAME,"");
    }

    public void setLastName(String name)
    {
        editor = preferences.edit();
        editor.putString(KEY_LAST_NAME,name);
        editor.commit();
    }

    public String getEmail()
    {
        return preferences.getString(KEY_EMAIL,"");
    }


    public void setUserName(String username)
    {
        editor = preferences.edit();
        editor.putString(KEY_USERNAME,username);
        editor.commit();
    }

    public String getUsername()
    {
        return  preferences.getString(KEY_USERNAME,"");
    }

    public void logoutUser()
    {
        // Clearing all data from Shared Preferences
        try
        {
            editor = preferences.edit();
            editor.clear();
            editor.commit();

            Intent i = new Intent(context, AllUsersActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(i);
            activity.finish();
            AppUtils.startActivityAnimation(activity);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void logoutUserFromNotification()
    {
        // Clearing all data from Shared Preferences
        try
        {
            editor = preferences.edit();
            editor.clear();
            editor.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void savePerformanceList(ArrayList<PerformanceTemp> listItems)
    {
        Gson gson = new Gson();
        String json = gson.toJson(listItems);
        editor = preferences.edit();
        editor.putString(KEY_PERFORMANCE, json);
        editor.apply();
    }

    public void savePortfolioList(ArrayList<PortfoliaResponseModel.PortfolioDetailsItem> listItems)
    {
        Gson gson = new Gson();
        String json = gson.toJson(listItems);
        editor = preferences.edit();
        editor.putString(KEY_PORTFOLIO, json);
        editor.apply();
    }

    public void saveBsMovementList(ArrayList<BSMovementResponseModel.GraphDataItem> listItems)
    {
        Gson gson = new Gson();
        String json = gson.toJson(listItems);
        editor = preferences.edit();
        editor.putString(KEY_BSMOVEMENT, json);
        editor.apply();
    }

    public void saveBsMovementTableData(String data)
    {
        editor = preferences.edit();
        editor.putString(KEY_BSMOVEMENT_DATA, data);
        editor.apply();
    }


    public static final String KEY_CURRENTVALUE = "CurrentValue";
    public static final String KEY_INITIALVALUE = "InitiallValue";
    public static final String KEY_GAIN = "Gain";

    public void savePortfolioTitle(String grandTotalCurrent, String grandTotalInvest, String grandTotalProfit)
    {
        editor = preferences.edit();
        editor.putString(KEY_CURRENTVALUE, grandTotalCurrent);
        editor.putString(KEY_INITIALVALUE, grandTotalInvest);
        editor.putString(KEY_GAIN, grandTotalProfit);
        editor.apply();
    }

    public String getCurrentValue()
    {
        return  preferences.getString(KEY_CURRENTVALUE,"");
    }

    public String getInitalValue()
    {
        return  preferences.getString(KEY_INITIALVALUE,"");
    }

    public String getGain()
    {
        return  preferences.getString(KEY_GAIN,"");
    }

    public String getBsMovementTableData()
    {
        return  preferences.getString(KEY_BSMOVEMENT_DATA,"");
    }

    public ArrayList<BSMovementResponseModel.GraphDataItem> getBsMovementData()
    {
        ArrayList<BSMovementResponseModel.GraphDataItem> mItems = new ArrayList<>();
        Gson gson = new Gson();
        String json = preferences.getString(KEY_BSMOVEMENT, null);
        Type type = new TypeToken<ArrayList<BSMovementResponseModel.GraphDataItem>>() {}.getType();
        mItems = gson.fromJson(json, type);

        if (mItems == null)
        {
            mItems = new ArrayList<>();
        }

        return mItems;
    }

    public ArrayList<PortfoliaResponseModel.PortfolioDetailsItem> getPortfolioList()
    {
        ArrayList<PortfoliaResponseModel.PortfolioDetailsItem> mItems = new ArrayList<>();
        Gson gson = new Gson();
        String json = preferences.getString(KEY_PORTFOLIO, null);
        Type type = new TypeToken<ArrayList<PortfoliaResponseModel.PortfolioDetailsItem>>() {}.getType();
        mItems = gson.fromJson(json, type);

        if (mItems == null)
        {
            mItems = new ArrayList<>();
        }

        return mItems;
    }

    public ArrayList<PerformanceTemp> getPerformanceList()
    {
        ArrayList<PerformanceTemp> mItems = new ArrayList<>();
        Gson gson = new Gson();
        String json = preferences.getString(KEY_PERFORMANCE, null);
        Type type = new TypeToken<ArrayList<PerformanceTemp>>() {}.getType();
        mItems = gson.fromJson(json, type);

        if (mItems == null)
        {
            mItems = new ArrayList<>();
        }

        return mItems;
    }

    public void saveNextYearList(ArrayList<PerformanceTemp> listItems)
    {
        Gson gson = new Gson();
        String json = gson.toJson(listItems);
        editor = preferences.edit();
        editor.putString(KEY_NEXTYEAR, json);
        editor.apply();
    }

    public ArrayList<PerformanceTemp> getNextYearList()
    {
        ArrayList<PerformanceTemp> mItems = new ArrayList<>();
        Gson gson = new Gson();
        String json = preferences.getString(KEY_NEXTYEAR, null);
        Type type = new TypeToken<ArrayList<PerformanceTemp>>() {}.getType();
        mItems = gson.fromJson(json, type);

        if (mItems == null)
        {
            mItems = new ArrayList<>();
        }

        return mItems;
    }

    public void savePreviousYearList(ArrayList<PerformanceTemp> listItems)
    {
        Gson gson = new Gson();
        String json = gson.toJson(listItems);
        editor = preferences.edit();
        editor.putString(KEY_PREVIOUSYEAR, json);
        editor.apply();
    }

    public ArrayList<PerformanceTemp> getPreviousYearList()
    {
        ArrayList<PerformanceTemp> mItems = new ArrayList<>();
        Gson gson = new Gson();
        String json = preferences.getString(KEY_PREVIOUSYEAR, null);
        Type type = new TypeToken<ArrayList<PerformanceTemp>>() {}.getType();
        mItems = gson.fromJson(json, type);

        if (mItems == null)
        {
            mItems = new ArrayList<>();
        }

        return mItems;
    }

    public boolean isServiceRunning(Class<?> serviceClass)
    {
        try
        {
            ActivityManager manager = null;
            if(activity != null && context == null)
            {
                manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
            }
            else if(activity == null && context != null)
            {
                manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            }

            if(manager != null)
            {
                for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
                {
                    if (serviceClass.getName().equals(service.service.getClassName()))
                    {
                        return true;
                    }
                }
            }
            else
            {
                return false;
            }
        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
