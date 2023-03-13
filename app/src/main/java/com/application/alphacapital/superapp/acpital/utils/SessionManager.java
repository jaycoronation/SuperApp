package com.application.alphacapital.superapp.acpital.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.application.alphacapital.superapp.acpital.CapitalMainActivity;


public class SessionManager
{
    private SharedPreferences preferences;
    private Editor editor;
    private Activity activity;
    private int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "LoginPref_AlphaCapital";
    private static final String IS_LOGIN = "loginStatus";
    private static final String IS_REGISTERED = "IsRegisterd";

    public static final String KEY_ID = "UserId";
    public static final String KEY_EMP_ID_FOR_ADMIN = "EmployeeIdForAdmin";
    public static final String KEY_SOCIAL_ID = "SocialId";
    public static final String KEY_EMAIL = "UserEmail";
    public static final String KEY_USERNAME = "UserName";
    public static final String KEY_IMAGE = "Image";
    public static final String KEY_GENDER = "Gender";
    public static final String KEY_BIRTHDATE = "Birthdate";
    public static final String KEY_CONTACT_NUMBER = "ContactNumber";

    public static final String KEY_ADDRESS = "address";
    public static final String KEY_COUNTRYID = "countryId";
    public static final String KEY_STATEID = "stateId";
    public static final String KEY_CITYID = "cityId";
    public static final String KEY_IS_ADMIN = "is_admin";
    public static final String KEY_IS_ACTIVE = "is_active";
    public static final String KEY_IS_DELETED = "is_deleted";


    // device info
    public static final String KEY_DEVICE_ID = "device_id";
    public static final String KEY_DEVICE_MODEL_NAME = "device_model_name";
    public static final String KEY_DEVICE_OS = "device_os";
    public static final String KEY_DEVICE_APP_VERSION = "device_app_version";
    public static final String KEY_GCM_TOKEN_ID = "GCMTokenId";

    private static final String LAST_LOGIN_TIME = "lastLoginTime";

    public static final String LIST_CLIENT = "clientList";
    public static final String LIST_ACTIVITY = "activityTypeList";
    public static final String LIST_RM = "RMList";

    private static final String KEY_CONTACT_MENU_ID = "contactMenuId";
    private static final String KEY_CONTACT_MENU_TITLE = "contactMenuTitle";
    private static final String KEY_CONTACT_MENU_Image = "contactMenuImage";
    public static final String CURRENT_TASK_ID = "task_id";
    public static final String KEY_LAST_COMMENT_ID = "LastCommentId";
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
    public void createLoginSession(String userid, String userName, String contactNumber, String email, String address, String countryId, String stateId, String cityId, boolean is_admin, boolean is_active, boolean is_deleted)
    {
        editor = preferences.edit();

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, userid);
        editor.putString(KEY_USERNAME, userName);
        editor.putString(KEY_CONTACT_NUMBER, contactNumber);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_COUNTRYID, countryId);
        editor.putString(KEY_STATEID, stateId);
        editor.putString(KEY_CITYID, cityId);
        editor.putBoolean(KEY_IS_ADMIN, is_admin);
        editor.putBoolean(KEY_IS_ACTIVE, is_active);
        editor.putBoolean(KEY_IS_DELETED, is_deleted);

        editor.commit();
    }

    public boolean isLoggedIn()
    {
        return preferences.getBoolean(IS_LOGIN, false);
    }

    public String getUserId()
    {
        String uid = AppUtils.getValidAPIStringResponse(preferences.getString(KEY_ID, ""));
        return uid;
    }

    public String getEmail()
    {
        String email = AppUtils.getValidAPIStringResponse(preferences.getString(KEY_EMAIL, ""));
        return email;
    }

    public String getUserName()
    {
        String name = AppUtils.getValidAPIStringResponse(preferences.getString(KEY_USERNAME, ""));
        name = AppUtils.toDisplayCase(name).trim();
        return name;
    }

    public String getGender()
    {
        String name = AppUtils.getValidAPIStringResponse(preferences.getString(KEY_GENDER, ""));
        return name;
    }

    public String getProfileImage()
    {
        String image = AppUtils.getValidAPIStringResponse(preferences.getString(KEY_IMAGE, ""));
        return image;
    }

    public String getContactNumber()
    {
        String str = AppUtils.getValidAPIStringResponse(preferences.getString(KEY_CONTACT_NUMBER, ""));
        return str;
    }

    public String getBirthdate()
    {
        String str = AppUtils.getValidAPIStringResponse(preferences.getString(KEY_BIRTHDATE, ""));
        return str;
    }

    public String getUserDeviceId()
    {
        String uname = AppUtils.getValidAPIStringResponse(preferences.getString(KEY_DEVICE_ID, ""));
        return uname;
    }

    public void setUserDeviceId(String name)
    {
        editor = preferences.edit();
        editor.putString(KEY_DEVICE_ID, name);
        editor.commit();
    }

    public String getDeviceModelName()
    {
        String uname = AppUtils.getValidAPIStringResponse(preferences.getString(KEY_DEVICE_MODEL_NAME, ""));
        return uname;
    }

    public void setDeviceModelName(String name)
    {
        editor = preferences.edit();
        editor.putString(KEY_DEVICE_MODEL_NAME, name);
        editor.commit();
    }

    public String getDeviceOS()
    {
        String uname = AppUtils.getValidAPIStringResponse(preferences.getString(KEY_DEVICE_OS, ""));
        return uname;
    }

    public void setDeviceOS(String name)
    {
        editor = preferences.edit();
        editor.putString(KEY_DEVICE_OS, name);
        editor.commit();
    }

    public String getDeviceAppVersion()
    {
        String uname = AppUtils.getValidAPIStringResponse(preferences.getString(KEY_DEVICE_APP_VERSION, "1.8"));
        return uname;
    }

    public void setDeviceAppVersion(String name)
    {
        editor = preferences.edit();
        editor.putString(KEY_DEVICE_APP_VERSION, name);
        editor.commit();
    }

    public String getTokenId()
    {
        String token = AppUtils.getValidAPIStringResponse(preferences.getString(KEY_GCM_TOKEN_ID, ""));
        return token;
    }

    public void saveTokenId(String token)
    {
        editor = preferences.edit();
        editor.putString(KEY_GCM_TOKEN_ID, token);
        editor.commit();
    }

    public void saveProfileImage(String image)
    {
        editor = preferences.edit();
        editor.putString(KEY_IMAGE, image);
        editor.commit();
    }


    public String getAddress()
    {
        String address = AppUtils.getValidAPIStringResponse(preferences.getString(KEY_ADDRESS, ""));
        return address;
    }

    public void saveAddress(String str)
    {
        editor = preferences.edit();
        editor.putString(KEY_ADDRESS, str);
        editor.commit();
    }

    public String getEmpIdForAdmin()
    {
        String address = AppUtils.getValidAPIStringResponse(preferences.getString(KEY_EMP_ID_FOR_ADMIN, ""));
        return address;
    }

    public void setEmpIdForAdmin(String str)
    {
        editor = preferences.edit();
        editor.putString(KEY_EMP_ID_FOR_ADMIN, str);
        editor.commit();
    }

    public String getCountryId()
    {
        String countryId = AppUtils.getValidAPIStringResponse(preferences.getString(KEY_COUNTRYID, ""));
        return countryId;
    }

    public void saveCountryId(String str)
    {
        editor = preferences.edit();
        editor.putString(KEY_COUNTRYID, str);
        editor.commit();
    }

    public String getStateId()
    {
        String stateId = AppUtils.getValidAPIStringResponse(preferences.getString(KEY_STATEID, ""));
        return stateId;
    }

    public void saveStateId(String str)
    {
        editor = preferences.edit();
        editor.putString(KEY_STATEID, str);
        editor.commit();
    }

    public String getCityId()
    {
        String cityId = AppUtils.getValidAPIStringResponse(preferences.getString(KEY_CITYID, ""));
        return cityId;
    }

    public void saveCityId(String str)
    {
        editor = preferences.edit();
        editor.putString(KEY_CITYID, str);
        editor.commit();
    }

    public boolean isAdmin()
    {
        return preferences.getBoolean(KEY_IS_ADMIN, false);
    }

    public boolean isActive()
    {
        return preferences.getBoolean(KEY_IS_ACTIVE, false);
    }

    public boolean isDeleted()
    {
        return preferences.getBoolean(KEY_IS_DELETED, false);
    }

    public void saveEmail(String str)
    {
        editor = preferences.edit();
        editor.putString(KEY_EMAIL, str);
        editor.commit();
    }

    public void saveUserName(String str)
    {
        editor = preferences.edit();
        editor.putString(KEY_USERNAME, str);
        editor.commit();
    }

    public void saveGender(String str)
    {
        editor = preferences.edit();
        editor.putString(KEY_GENDER, str);
        editor.commit();
    }

    public void saveContactNumber(String str)
    {
        editor = preferences.edit();
        editor.putString(KEY_CONTACT_NUMBER, str);
        editor.commit();
    }

    public void saveBirthdate(String str)
    {
        editor = preferences.edit();
        editor.putString(KEY_BIRTHDATE, str);
        editor.commit();
    }

    public void saveSocialId(String str)
    {
        editor = preferences.edit();
        editor.putString(KEY_SOCIAL_ID, str);
        editor.commit();
    }

    public String getSocialId()
    {
        String str = AppUtils.getValidAPIStringResponse(preferences.getString(KEY_SOCIAL_ID, ""));
        return str;
    }

    public boolean isRegistered()
    {
        return preferences.getBoolean(IS_REGISTERED, false);
    }

    public void setIsRegistered(boolean flag)
    {
        editor = preferences.edit();
        editor.putBoolean(IS_REGISTERED, flag);
        editor.commit();
    }

    public void saveLastLoginTime(long time)
    {
        editor = preferences.edit();
        editor.putLong(LAST_LOGIN_TIME, time);
        editor.commit();
    }

    public long getLastLoginTime()
    {
        long time = preferences.getLong(LAST_LOGIN_TIME, 0);
        return time;
    }


    public String getContactMenuId()
    {
        String uname = AppUtils.getValidAPIStringResponse(preferences.getString(KEY_CONTACT_MENU_ID, ""));
        return uname;
    }

    public void setContactMenuId(String id)
    {
        editor = preferences.edit();
        editor.putString(KEY_CONTACT_MENU_ID, id);
        editor.commit();
    }

    public String getContactMenuTitle()
    {
        String uname = AppUtils.getValidAPIStringResponse(preferences.getString(KEY_CONTACT_MENU_TITLE, ""));
        return uname;
    }

    public void setContactMenuTitle(String name)
    {
        editor = preferences.edit();
        editor.putString(KEY_CONTACT_MENU_TITLE, name);
        editor.commit();
    }

    public String getContactMenuImage()
    {
        String uname = AppUtils.getValidAPIStringResponse(preferences.getString(KEY_CONTACT_MENU_Image, ""));
        return uname;
    }

    public void setContactMenuImage(String image)
    {
        editor = preferences.edit();
        editor.putString(KEY_CONTACT_MENU_Image, image);
        editor.commit();
    }

    public String getTaskId()
    {
        String email = AppUtils.getValidAPIStringResponse(preferences.getString(CURRENT_TASK_ID, ""));
        return email;
    }

    public void setTaskId(String str)
    {
        editor = preferences.edit();
        editor.putString(CURRENT_TASK_ID, str);
        editor.commit();
    }

    public String getLastCommentId()
    {
        String email = AppUtils.getValidAPIStringResponse(preferences.getString(KEY_LAST_COMMENT_ID, "0"));
        return email;
    }

    public String getClientList()
    {
        return AppUtils.getValidAPIStringResponse(preferences.getString(LIST_CLIENT, ""));
    }

    public void setClientList(String clientListString)
    {
        editor = preferences.edit();
        editor.putString(LIST_CLIENT, clientListString);
        editor.commit();
    }

    public String getRMList()
    {
        return AppUtils.getValidAPIStringResponse(preferences.getString(LIST_RM, ""));
    }
    public void setRMList(String RMListResponse)
    {
        editor = preferences.edit();
        editor.putString(LIST_RM, RMListResponse);
        editor.commit();
    }

    public String getActivityList()
    {
        return AppUtils.getValidAPIStringResponse(preferences.getString(LIST_ACTIVITY, ""));
    }
    public void setActivityList(String activityListString)
    {
        editor = preferences.edit();
        editor.putString(LIST_ACTIVITY, activityListString);
        editor.commit();
    }

    public void setLastCommentId(String str)
    {
        editor = preferences.edit();
        editor.putString(KEY_LAST_COMMENT_ID, str);
        editor.commit();
    }

    public void logoutUserSession()
    {
        // Clearing all data from Shared Preferences
        try
        {
            editor = preferences.edit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        editor.clear();
        editor.commit();
    }

    @SuppressLint("NewApi")
    public void logoutUser()
    {
        // Clearing all data from Shared Preferences
        try
        {
            editor = preferences.edit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        editor.clear();
        editor.commit();

        try
        {
            activity.finishAffinity();
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }

        Intent i = new Intent(context, CapitalMainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        try
        {
            activity.finish();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //	check Internet connection
    public boolean isNetworkAvailable()
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null)
        {
            // There are no active networks.
            return false;
        }
        else return true;
    }


}
