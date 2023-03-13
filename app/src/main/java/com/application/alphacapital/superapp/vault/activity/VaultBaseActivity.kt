package com.application.alphacapital.superapp.vault.activity

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import com.alphaestatevault.model.AccountHolderListResponse
import com.application.alphacapital.superapp.vault.network.ApiClient
import com.application.alphacapital.superapp.vault.network.ApiInterface
import com.alphaestatevault.utils.AppUtils
import com.application.alphacapital.superapp.vault.utils.VaultSessionManager
import com.application.alphacapital.superapp.R
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.toast
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.text.SimpleDateFormat
import java.util.*

open class VaultBaseActivity : AppCompatActivity() {
    lateinit var activity: Activity
    lateinit var sessionManager: VaultSessionManager
    lateinit var apiService: ApiInterface
    lateinit var appUtils: AppUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        sessionManager = VaultSessionManager(activity)
        apiService = ApiClient.getClient()!!.create(ApiInterface::class.java)
        appUtils = AppUtils()
    }

    override fun onResume() {
        super.onResume()
    }

    fun showToast(message: String) {
        toast(message)
    }



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    open fun setStatusBarGradiant(activity: Activity)
    {
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            val window: Window = activity.window
            val background = ContextCompat.getDrawable(activity, R.drawable.vault_bg_gradient)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(activity,android.R.color.transparent)
            window.navigationBarColor = ContextCompat.getColor(activity,android.R.color.black)
            window.setBackgroundDrawable(background)
        }*/
    }

    fun removeError(edt: EditText, inputLayout: TextInputLayout)
    {
        edt.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(p0: Editable?)
            {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int)
            {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int)
            {
                inputLayout.isErrorEnabled = false
            }
        })
    }

    fun noInternetToast() {
        toast(activity.getString(R.string.no_internet_message))
    }

    fun apiFailedToast() {
        toast(activity.getString(R.string.api_failed_message))
    }

    fun getEditTextString(editText: EditText): String {
        return editText.text.toString()
    }

    fun AppCompatEditText.getValue():String{
        return this.text.toString().trim()
    }

    fun EditText.getValue():String{
        return this.text.toString().trim()
    }

    fun isEmptyEditTExt(editText: EditText): Boolean {
        return editText.text.toString().trim().isEmpty()
    }

    fun startActivityAnimation() {
        hideKeyBoard()
        //activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
    }

    fun finishActivityAnimation() {
        hideKeyBoard()
        //activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out)
    }

    fun hideKeyBoard() {
        val view: View = activity.findViewById(android.R.id.content)
        val inputMethodManager: InputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun hideKeyBoard(editText: EditText) {
        val inputMethodManager: InputMethodManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    fun hideStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
// Remember that you should never show the action bar if the
// status bar is hidden, so hide that too if necessary.
        actionBar?.hide()
    }

    fun isOnline(): Boolean {
        try {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)

            val timeoutMs = 1500
            val sock = Socket()
            val sockaddr = InetSocketAddress("8.8.8.8", 53)

            sock.connect(sockaddr, timeoutMs)
            sock.close()

            return true
        } catch (e: IOException) {
            return false
        }

    }

    fun visible(view: View) {
        view.visibility = View.VISIBLE
    }

    fun gone(view: View) {
        view.visibility = View.GONE
    }

    fun invisible(view: View) {
        view.visibility = View.INVISIBLE
    }

    fun getRelativeDateTime(timestamp: Long): String? {
        var datetime = ""

        try {
            datetime = DateUtils.getRelativeDateTimeString(
                activity,
                timestamp,
                DateUtils.MINUTE_IN_MILLIS,
                DateUtils.WEEK_IN_MILLIS,
                0
            ).toString()
            if (datetime.contains("/")) {
                val sdf = SimpleDateFormat("dd MMM yyyy, h:mm a")
                val date = Date(timestamp)
                datetime = sdf.format(date)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return datetime
    }

    fun getHolders():List<String>{
        val itemType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(sessionManager.holders,itemType)
    }

    fun getHoldersFullList():MutableList<AccountHolderListResponse.Holder>{
        val itemType = object : TypeToken<MutableList<AccountHolderListResponse.Holder>>() {}.type
        return Gson().fromJson(sessionManager.holdersList,itemType)
    }

}
