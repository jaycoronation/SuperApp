package com.application.alphacapital.superapp.finplan.activity

import android.annotation.TargetApi
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateUtils
import android.util.Base64
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat

import com.application.alphacapital.superapp.finplan.utils.AppUtils
import com.application.alphacapital.superapp.finplan.utils.SessionManager
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.finplan.network.FinPlanApiClient
import com.application.alphacapital.superapp.finplan.network.FinPlanApiInterface
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.jetbrains.anko.toast
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.text.SimpleDateFormat
import java.util.*

open class FinPlanBaseActivity : AppCompatActivity() {
    lateinit var activity: Activity
    lateinit var sessionManager: SessionManager
    lateinit var apiService: FinPlanApiInterface
    lateinit var appUtils: AppUtils
    lateinit var loader: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this

        if (this !is FinPlanSignupActivity &&
            this !is FinPlanLoginActivity &&
            this !is FinPlanForgotPasswordActivity
        ) {
            //setStatusBarGradiant(activity)
        }

        sessionManager = SessionManager(activity)
        apiService = FinPlanApiClient.getClient()!!.create(FinPlanApiInterface::class.java)
        appUtils = AppUtils()
        loader = AppUtils.loadingDialog(activity)
    }

    override fun onBackPressed()
    {
        hideKeyBoard()
        finish()
        finishActivityAnimation()
        super.onBackPressed()
    }

    fun showToast(message: String) {
        toast(message)
    }

    fun ImageView.backNav(){
        this.setOnClickListener {
            finish()
            finishActivityAnimation()
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    open fun setStatusBarGradiant(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = activity.window
            val background = ContextCompat.getDrawable(activity, R.drawable.fin_plan_bg_gradient)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(activity, android.R.color.transparent)
            window.navigationBarColor =
                ContextCompat.getColor(activity, android.R.color.black)
            window.setBackgroundDrawable(background)
        }
    }

    fun removeError(edt: EditText, inputLayout: TextInputLayout) {
        edt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
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

    fun AppCompatEditText.getValue(): String {
        return this.text.toString().trim()
    }

    fun TextInputEditText.getValue(): String {
        return this.text.toString().trim()
    }

    fun com.google.android.material.textfield.TextInputEditText.isEmpty(): Boolean {
        return this.text?.isEmpty()!!
    }

    fun EditText.getValue(): String {
        return this.text.toString().trim()
    }

    fun isEmptyEditTExt(editText: EditText): Boolean {
        return editText.text.toString().trim().isEmpty()
    }

    fun startActivityAnimation() {
        hideKeyBoard()
        //activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
    }

    fun String.getAmountWithoutSymbol():String{
        return if(this.isNotEmpty()){
            try {
                this.replace(activity.getString(R.string.rs),"").replace(",","")
            } catch (e: Exception) {
                ""
            }
        }else{
            ""
        }
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

    fun getBase64(string: String):String{
        return Base64.encodeToString(string.toByteArray(), Base64.NO_WRAP)
    }

    fun getPrice(price: String) = activity.getString(R.string.rs)+" "+AppUtils.convertToCommaSeperatedValue(price).takeIf { price.isNotEmpty() }
}
