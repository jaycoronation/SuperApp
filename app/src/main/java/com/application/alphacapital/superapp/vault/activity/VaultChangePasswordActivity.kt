package com.application.alphacapital.superapp.vault.activity

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.alphaestatevault.model.*
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityChangePasswordBinding
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VaultChangePasswordActivity : VaultBaseActivity()
{
    lateinit var binding: VaultActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_change_password)
        setStatusBarGradiant(activity)
        initView()
        onClicks()
    }

    private fun initView()
    {
        binding.toolbar.txtTitle.setText("Change Password")
        appUtils.removeError(binding.edtCurrentPassword, binding.ipCurrentPassword)
        appUtils.removeError(binding.edtNewPassword, binding.ipNewPassword)
        appUtils.removeError(binding.edtConfirmPassword, binding.ipConfirmPassword)
    }

    private fun onClicks()
    {
        binding.toolbar.llMenuToolBar.setOnClickListener {
            hideKeyBoard()
            finish()
            finishActivityAnimation()
        }

        binding.txtChangePassword.setOnClickListener {

            if (binding.edtCurrentPassword.text!!.isEmpty())
            {
                binding.ipCurrentPassword.error = "Please enter your current password."
            }
            else if (binding.edtNewPassword.text!!.isEmpty())
            {
                binding.ipNewPassword.error = "Please enter your new password."
            }
            else if (binding.edtConfirmPassword.text!!.isEmpty())
            {
                binding.ipConfirmPassword.error = "Please enter confirm password."
            }
            else if (getEditTextString(binding.edtNewPassword) != getEditTextString(binding.edtConfirmPassword))
            {
                binding.ipConfirmPassword.error = "Your new and confirm password do not match."
            }
            else
            {
                hideKeyBoard()
                if (isOnline())
                {
                    changePassword()
                }
                else
                {
                    noInternetToast()
                }
            }
        }
    }

    private fun changePassword()
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.changePasswordCall(getEditTextString(binding.edtCurrentPassword), getEditTextString(binding.edtConfirmPassword), sessionManager.userId)

                call.enqueue(object : Callback<SignUpResponse>
                {
                    override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.body()!!.success == 1)
                            {
                                toast(response.body()!!.message)
                                finish()
                                sessionManager.logoutUser()
                            }
                            else
                            {
                                toast(response.body()!!.message)
                            }
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
        hideKeyBoard()
        finish()
        finishActivityAnimation()
        super.onBackPressed()
    }
}
