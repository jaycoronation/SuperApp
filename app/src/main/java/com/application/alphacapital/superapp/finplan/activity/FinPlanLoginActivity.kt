package com.application.alphacapital.superapp.finplan.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import com.application.alphacapital.superapp.finplan.model.LoginResponse
import com.application.alphacapital.superapp.supermain.activity.MainActivity
import com.application.alphacapital.superapp.R
import kotlinx.android.synthetic.main.fin_plan_activity_login.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinPlanLoginActivity : FinPlanBaseActivity(), View.OnClickListener
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fin_plan_activity_login)
        initViews()
    }

    private fun initViews()
    {
        btnLogin.setOnClickListener(this)
        tvSignup.setOnClickListener(this)
        tvForgotPassword.setOnClickListener(this)
    }

    override fun onBackPressed()
    {
        super.onBackPressed()
        startActivity<MainActivity>()
        finish()
    }

    override fun onClick(v: View?)
    {
        when (v?.id)
        {
            btnLogin.id ->
            {
                if (edtEmail.isEmpty())
                {
                    inputEmail.error = "Please enter your email or mobile number"
                }
                else if (edtPassword.isEmpty())
                {
                    inputEmail.error = "Please your password"
                }
                else
                {
                    hideKeyBoard()
                    login()
                }
            }
            tvForgotPassword.id ->
            {
                startActivity<FinPlanForgotPasswordActivity>()
                startActivityAnimation()
            }
            tvSignup.id ->
            {
                startActivity<FinPlanSignupActivity>()
                startActivityAnimation()
            }
        }
    }

    private fun login()
    {
        if (isOnline())
        {
            loader.show()
            apiService.signIn(edtEmail.getValue(), edtPassword.getValue())
                .enqueue(object : Callback<LoginResponse>
                {
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable)
                    {
                        loader.dismiss()
                        apiFailedToast()
                        Log.e("<><><>FAILURE", t.message.toString())
                    }

                    override fun onResponse(call: Call<LoginResponse>,
                                            response: Response<LoginResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            showToast(response.body()!!.message)
                            if (response.body()!!.success == 1)
                            {
                                sessionManager.createLogin(response.body()!!.user_id,
                                    response.body()!!.first_name,
                                    response.body()!!.last_name,
                                    response.body()!!.email,
                                    response.body()!!.mobile,
                                    "",
                                    false
                                )

                                loader.dismiss()
                                finish()
                                startActivity<FinPlanMainActivity>()
                                startActivityAnimation()
                            }
                            else
                            {
                                loader.dismiss()
                            }
                        }
                        else
                        {
                            loader.dismiss()
                            apiFailedToast()
                        }
                    }

                })
        }
        else
        {
            noInternetToast()
        }
    }
}
