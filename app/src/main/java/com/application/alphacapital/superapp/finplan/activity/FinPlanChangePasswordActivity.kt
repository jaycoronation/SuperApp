package com.application.alphacapital.superapp.finplan.activity

import android.os.Bundle
import android.view.View
import com.alphafinancialplanning.model.CommonResponse
import com.application.alphacapital.superapp.R
import kotlinx.android.synthetic.main.fin_plan_activity_change_password.*
import kotlinx.android.synthetic.main.fin_plan_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinPlanChangePasswordActivity : FinPlanBaseActivity(), View.OnClickListener
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fin_plan_activity_change_password)
        initViews()
    }

    private fun initViews()
    {
        tvTitle.text = "Change Password"
        ivBack.backNav()

        btnSubmit.setOnClickListener(this)

        removeError(edtOldPassword, inputOldPassword)
        removeError(edtNewPassword, inputNewPassword)
        removeError(edtConNewPassword, inputConNewPassword)
    }

    override fun onClick(view: View?)
    {
        when (view?.id)
        {
            btnSubmit.id ->
            {
                when
                {
                    edtOldPassword.isEmpty() ->
                    {
                        inputOldPassword.error = "Please enter your existing password"
                    }
                    edtNewPassword.isEmpty() ->
                    {
                        inputNewPassword.error = "Please enter your existing password"
                    }
                    !appUtils.isPasswordValid(edtNewPassword.getValue()) ->
                    {
                        inputNewPassword.error = "Your password should contain one special character, one letter one number and minimum 8 character long"
                    }
                    edtNewPassword.getValue() != edtConNewPassword.getValue() ->
                    {
                        inputConNewPassword.error = "New password and confirm new password do not match"
                    }
                    else ->
                    {
                        if (isOnline())
                        {
                            loader.show()
                            apiService.changePassword(edtOldPassword.getValue(), edtNewPassword.getValue(), sessionManager.userId).enqueue(object : Callback<CommonResponse>
                                {
                                    override fun onFailure(call: Call<CommonResponse>, t: Throwable)
                                    {
                                        apiFailedToast()
                                        loader.dismiss()
                                    }

                                    override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>)
                                    {
                                        if (response.isSuccessful)
                                        {
                                            loader.dismiss()
                                            showToast(response.body()!!.message)
                                            if (response.body()!!.success == 1)
                                            {
                                                finish()
                                                finishActivityAnimation()
                                            }
                                        }
                                        else
                                        {
                                            apiFailedToast()
                                            loader.dismiss()
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

            }
        }
    }
}
