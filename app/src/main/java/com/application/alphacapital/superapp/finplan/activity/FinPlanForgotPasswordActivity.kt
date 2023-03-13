package com.application.alphacapital.superapp.finplan.activity

import android.os.Bundle
import android.view.View
import com.alphafinancialplanning.model.CommonResponse
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.finplan.activity.FinPlanBaseActivity
import kotlinx.android.synthetic.main.fin_plan_activity_forgot_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinPlanForgotPasswordActivity : FinPlanBaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fin_plan_activity_forgot_password)

        btnForgotPassword.setOnClickListener(this)
        removeError(edtEmail,inputEmail)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            btnForgotPassword.id ->{
                if(edtEmail.isEmpty()){
                    inputEmail.error = "Please enter email"
                }else if(!appUtils.isEmailValid(edtEmail.getValue())){
                    inputEmail.error = "Please enter valid email"
                }else {
                    if(isOnline()){
                        loader.show()
                        apiService.forgotPassword(edtEmail.getValue())
                            .enqueue(object : Callback<CommonResponse>{
                                override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                                    apiFailedToast()
                                    loader.dismiss()
                                }

                                override fun onResponse(
                                    call: Call<CommonResponse>,
                                    response: Response<CommonResponse>
                                ) {
                                    if(response.isSuccessful){
                                        showToast(response.body()!!.message)
                                        if(response.body()!!.success==1){
                                            loader.dismiss()
                                            finish()
                                            finishActivityAnimation()
                                        }
                                    }else{
                                        apiFailedToast()
                                        loader.dismiss()
                                    }
                                }

                            })
                    }else{
                        noInternetToast()
                    }
                }
            }
        }
    }
}
