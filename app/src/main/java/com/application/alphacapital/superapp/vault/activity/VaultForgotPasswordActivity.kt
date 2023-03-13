package com.application.alphacapital.superapp.vault.activity

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.alphaestatevault.model.SignUpResponse
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityForgotPasswordBinding
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VaultForgotPasswordActivity : VaultBaseActivity()
{

    lateinit var binding: VaultActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_forgot_password)
        appUtils.setLightStatusBar(activity)
        initView()
        onClicks()
    }

    private fun initView()
    {
        appUtils.removeError(binding.edtEmail, binding.ipEmail)
    }

    private fun onClicks()
    {
        binding.txtForgotPassword.setOnClickListener {

            if (binding.edtEmail.text!!.isEmpty())
            {
                binding.ipEmail.error = "Please enter your email address."
            }
            else if (!appUtils.isEmailValid(binding.edtEmail.text.toString()))
            {
                binding.ipEmail.error = "Please enter your valid email address."
            }
            else
            {
                hideKeyBoard()
                if (isOnline())
                {
                    forgotPasswordcall()
                }
                else
                {
                    noInternetToast()
                }
            }
        }
    }

    private fun forgotPasswordcall()
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.forgotPassword(getEditTextString(binding.edtEmail))

                call.enqueue(object : Callback<SignUpResponse>
                {
                    override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            toast(response.body()!!.message)
                            finish()
                            finishActivityAnimation()
                        }
                        else
                        {
                            apiFailedToast()
                        }

                        binding.loading.llLoading.visibility = View.GONE
                    }

                    override fun onFailure(call: Call<SignUpResponse>, t: Throwable)
                    {
                        binding.loading.llLoading.visibility = View.GONE
                        apiFailedToast()
                    }
                })
            }
            else
            {
                noInternetToast()
            }
        }
        catch (e: Exception)
        {
        }
    }

    override fun onBackPressed()
    {
        activity.finish()
        finishActivityAnimation()
        super.onBackPressed()
    }
}
