package com.application.alphacapital.superapp.finplan.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.application.alphacapital.superapp.finplan.activity.FinPlanLoginActivity
import com.application.alphacapital.superapp.finplan.utils.AppConstant.session.KEY_EMAIL
import com.application.alphacapital.superapp.finplan.utils.AppConstant.session.KEY_FIRSTNAME
import com.application.alphacapital.superapp.finplan.utils.AppConstant.session.KEY_ID
import com.application.alphacapital.superapp.finplan.utils.AppConstant.session.KEY_IMAGE
import com.application.alphacapital.superapp.finplan.utils.AppConstant.session.KEY_IS_LOGIN
import com.application.alphacapital.superapp.finplan.utils.AppConstant.session.KEY_LASTNAME
import com.application.alphacapital.superapp.finplan.utils.AppConstant.session.KEY_PHONE
import com.application.alphacapital.superapp.finplan.utils.AppConstant.session.KEY_RISK_PROFILE
import com.application.alphacapital.superapp.finplan.utils.AppConstant.session.KEY_SOCIAL_LOGIN
import com.application.alphacapital.superapp.finplan.utils.AppConstant.session.KEY_USERNAME

class SessionManager {

    private var context: Context? = null
    private var editor: SharedPreferences.Editor? = null
    private val PRIVATE_MODE = 0
    private val PREF_NAME = "AlphaOnlineFinancePlanning2020"
    private var preferences: SharedPreferences? = null

    constructor(context: Context) {
        this.context = context
        preferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    }

    fun createLogin(userId: String,
                    firstname: String,
                    lastname: String,
                    email: String,
                    phone: String,
                    image: String,
                    isSocial: Boolean) {
        editor = preferences?.edit()
        editor!!.putBoolean(KEY_IS_LOGIN, true)
        editor!!.putString(KEY_ID, userId)
        editor!!.putString(KEY_FIRSTNAME,firstname)
        editor!!.putString(KEY_LASTNAME,lastname)
        editor!!.putString(KEY_USERNAME, firstname)
        editor!!.putString(KEY_EMAIL, email)
        editor!!.putString(KEY_PHONE, phone)
        editor!!.putString(KEY_IMAGE, image)
        editor!!.putBoolean(KEY_SOCIAL_LOGIN, isSocial)

        editor?.apply()
    }

    fun isLoggedIn(): Boolean {
        return preferences!!.getBoolean(KEY_IS_LOGIN, false)
    }

    var userId: String
        get() = preferences!!.getString(KEY_ID, "").toString()
        @SuppressLint("CommitPrefEdits")
        set(value) {
            editor = preferences?.edit()
            editor!!.putString(KEY_ID, value)
            editor!!.apply()
        }

    var userName: String
        get() = preferences!!.getString(KEY_USERNAME, "").toString()
        @SuppressLint("CommitPrefEdits")
        set(value) {
            editor = preferences?.edit()
            editor!!.putString(KEY_USERNAME, value)
            editor!!.apply()
        }

     var email: String
        get() = preferences!!.getString(KEY_EMAIL, "").toString()
        @SuppressLint("CommitPrefEdits")
        set(value) {
            editor = preferences?.edit()
            editor!!.putString(KEY_EMAIL, value)
            editor!!.apply()
        }

    var riskProfile: String
        get() = preferences!!.getString(KEY_RISK_PROFILE, "").toString()
        @SuppressLint("CommitPrefEdits")
        set(value) {
            editor = preferences?.edit()
            editor!!.putString(KEY_RISK_PROFILE, value)
            editor!!.apply()
        }

    fun getStringPreference(key: String): String{
        return preferences!!.getString(key,"").toString()
    }
    @SuppressLint("CommitPrefEdits")
    fun setStringPreference(key: String, value: String){
        editor = preferences?.edit()
        editor!!.putString(key, value)
        editor!!.apply()
    }

    fun getIntPreference(key: String): Int{
        return preferences!!.getInt(key,0)
    }
    @SuppressLint("CommitPrefEdits")
    fun setIntPreference(key: String, value: Int){
        editor = preferences?.edit()
        editor!!.putInt(key, value)
        editor!!.apply()
    }

    fun getBooleanPreference(key: String): Boolean{
        return preferences!!.getBoolean(key,false)
    }
    @SuppressLint("CommitPrefEdits")
    fun setBooleanPreference(key: String, value: Boolean){
        editor = preferences?.edit()
        editor!!.putBoolean(key, value)
        editor!!.apply()
    }


    fun logoutUser() {
        editor = preferences?.edit()
        editor?.clear()
        editor?.apply()
        val activity = context as Activity
        /*activity.finishAffinity()
        val intent = Intent(activity, FinPlanLoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        activity.startActivity(intent)*/
    }
}
