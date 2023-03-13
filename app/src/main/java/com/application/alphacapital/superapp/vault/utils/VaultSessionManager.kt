package com.application.alphacapital.superapp.vault.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class VaultSessionManager
{

    var mApp: MyApplication? = null
    private var context: Context? = null
    lateinit var activity: Activity
    private var editor: SharedPreferences.Editor? = null
    private val PRIVATE_MODE = 0
    private val PREF_NAME = "Alpha Vault"
    private var preferences: SharedPreferences? = null

    private var KEY_ID: String = "UserId"
    private var KEY_USERNAME: String = "UserName"
    private var KEY_EMAIL: String = "Email"
    private var KEY_PHONE: String = "MobileNumber"
    private var KEY_IMAGE: String = "ProfileImage"
    private var KEY_FCM_TOKEN: String = "FCMToken"
    private var KEY_DEVICE_ID: String = "DeviceId"
    private var KEY_IS_LOGIN: String = "loginStatus"

    private var KEY_COUNTRY_NAME: String = "country_name"
    private var KEY_COUNTRY_ID: String = "countryId"
    private var KEY_STATE_NAME: String = "state_name"
    private var KEY_STATE_ID: String = "stateId"
    private var KEY_CITY_NAME: String = "city_name"
    private var KEY_CITY_ID: String = "cityId"
    private var KEY_HOLDERS: String = "holders"
    private var KEY_HOLDERS_NEW: String = "holders_new"

    private val KEY_HOLDER_LIST = "holderlist"

    constructor(app: MyApplication)
    {
        mApp = app
        context = mApp!!.applicationContext
        preferences = context!!.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    }

    constructor(activity: Activity)
    {
        this.activity = activity
        preferences = activity.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    }

    fun createLogin(userId: String,
                    username: String,
                    email: String,
                    phone: String,
                    image: String,
                    country_name: String,
                    countryId: String,
                    state_name: String,
                    stateId: String,
                    city_name: String,
                    cityId: String)
    {
        editor = preferences?.edit()
        editor!!.putBoolean(KEY_IS_LOGIN, true)
        editor!!.putString(KEY_ID, userId)
        editor!!.putString(KEY_USERNAME, username)
        editor!!.putString(KEY_EMAIL, email)
        editor!!.putString(KEY_PHONE, phone)
        editor!!.putString(KEY_IMAGE, image)
        editor!!.putString(KEY_COUNTRY_NAME, country_name)
        editor!!.putString(KEY_COUNTRY_ID, countryId)
        editor!!.putString(KEY_STATE_NAME, state_name)
        editor!!.putString(KEY_STATE_ID, stateId)
        editor!!.putString(KEY_CITY_NAME, city_name)
        editor!!.putString(KEY_CITY_ID, cityId)
        editor?.apply()
    }

    var userId: String
        get() = preferences!!.getString(KEY_ID, "").toString()
        @SuppressLint("CommitPrefEdits")
        set(value)
        {
            editor = preferences?.edit()
            editor!!.putString(KEY_ID, value)
            editor!!.apply()
        }

    var username: String
        get() = preferences!!.getString(KEY_USERNAME, "").toString()
        @SuppressLint("CommitPrefEdits")
        set(value)
        {
            editor = preferences?.edit()
            editor!!.putString(KEY_USERNAME, value)
            editor!!.apply()
        }

    var email: String
        get() = preferences!!.getString(KEY_EMAIL, "").toString()
        @SuppressLint("CommitPrefEdits")
        set(value)
        {
            editor = preferences?.edit()
            editor!!.putString(KEY_EMAIL, value)
            editor!!.apply()
        }

    var phone: String
        get() = preferences!!.getString(KEY_PHONE, "").toString()
        set(value) = preferences!!.edit().putString(KEY_PHONE, value).apply()


    var profilePic: String
        get() = preferences!!.getString(KEY_IMAGE, "").toString()
        @SuppressLint("CommitPrefEdits")
        set(value)
        {
            editor = preferences?.edit()
            editor!!.putString(KEY_IMAGE, value)
            editor!!.apply()
        }

    var country_name: String
        get() = preferences!!.getString(KEY_COUNTRY_NAME, "").toString()
        set(value) = preferences!!.edit().putString(KEY_COUNTRY_NAME, value).apply()

    var countryId: String
        get() = preferences!!.getString(KEY_COUNTRY_ID, "").toString()
        set(value) = preferences!!.edit().putString(KEY_COUNTRY_ID, value).apply()

    var state_name: String
        get() = preferences!!.getString(KEY_STATE_NAME, "").toString()
        set(value) = preferences!!.edit().putString(KEY_STATE_NAME, value).apply()

    var stateId: String
        get() = preferences!!.getString(KEY_STATE_ID, "").toString()
        set(value) = preferences!!.edit().putString(KEY_STATE_ID, value).apply()

    var city_name: String
        get() = preferences!!.getString(KEY_CITY_NAME, "").toString()
        set(value) = preferences!!.edit().putString(KEY_CITY_NAME, value).apply()

    var cityId: String
        get() = preferences!!.getString(KEY_CITY_ID, "").toString()
        set(value) = preferences!!.edit().putString(KEY_CITY_ID, value).apply()

    var token: String
        get() = preferences!!.getString(KEY_FCM_TOKEN, "").toString()
        @SuppressLint("CommitPrefEdits")
        set(value)
        {
            editor = preferences?.edit()
            editor!!.putString(KEY_FCM_TOKEN, value)
            editor!!.apply()
        }

    var deviceId: String
        get() = preferences!!.getString(KEY_DEVICE_ID, "").toString()
        @SuppressLint("CommitPrefEdits")
        set(value)
        {
            editor = preferences?.edit()
            editor!!.putString(KEY_DEVICE_ID, value)
            editor!!.apply()
        }

    fun isLoggedIn(): Boolean
    {
        return preferences!!.getBoolean(KEY_IS_LOGIN, false)
    }

    var holders: String
        get() = preferences!!.getString(KEY_HOLDERS, "").toString()
        set(value) = preferences!!.edit().putString(KEY_HOLDERS, value).apply()


    var holdersList: String
        get() = preferences!!.getString(KEY_HOLDER_LIST, "").toString()
        set(value) = preferences!!.edit().putString(KEY_HOLDER_LIST, value).apply()

    fun logoutUser()
    {
        editor = preferences?.edit()
        editor?.clear()
        editor?.apply()
        //activity.finishAffinity()
        /*val intent = Intent(activity, VaultLoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        activity.startActivity(intent)*/
    }
}
