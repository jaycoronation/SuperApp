package com.application.alphacapital.superapp.supermain.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.alphaestatevault.model.CommonResponse
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.ActivityContactBinding
import com.application.alphacapital.superapp.pms.utils.SessionManager
import com.application.alphacapital.superapp.vault.activity.VaultBaseActivity
import com.application.alphacapital.superapp.vault.network.ApiClient
import com.application.alphacapital.superapp.vault.network.ApiInterface
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactActivity : VaultBaseActivity()
{
    private lateinit var binding : ActivityContactBinding
    private lateinit var pmsSessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(activity,R.layout.activity_contact)
        pmsSessionManager = SessionManager(activity)
        initView()
        onClick()
    }

    private fun initView()
    {
        Glide.with(activity).load(R.drawable.ic_back_nav).into(binding.toolbar.ivBack)
        binding.toolbar.txtTitle.text = "Contact"

        binding.edtName.setText(pmsSessionManager.firstName + " " + pmsSessionManager.lastName)
        binding.edtNumber.setText(sessionManager.phone)
        binding.edtEmail.setText(pmsSessionManager.email)

        try
        {
            removeError(binding.edtName,binding.ipName)
            removeError(binding.edtEmail,binding.ipEmail)
            removeError(binding.edtNumber,binding.ipMobile)
            removeError(binding.edtMessage,binding.ipMessage)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun onClick()
    {
        binding.toolbar.llBack.setOnClickListener { finish() }

        binding.tvSubmit.setOnClickListener {
            if (binding.edtName.getValue().isEmpty())
            {
                binding.ipName.error = "Please enter your name"
                return@setOnClickListener
            }
            if (binding.edtEmail.getValue().isEmpty())
            {
                binding.ipEmail.error = "Please enter your email address"
                return@setOnClickListener
            }
            if (!appUtils.isEmailValid(binding.edtEmail.getValue()))
            {
                binding.ipEmail.error = "Please enter valid email address"
                return@setOnClickListener
            }
            if (binding.edtNumber.getValue().isEmpty())
            {
                binding.ipMobile.error = "Please enter your mobile number"
                return@setOnClickListener
            }
            if (binding.edtNumber.getValue().length != 10)
            {
                binding.ipMobile.error = "Please enter valid mobile number"
                return@setOnClickListener
            }
            callContactUsAPI()
        }
    }

    private fun callContactUsAPI()
    {
        val apiService = com.application.alphacapital.superapp.supermain.network.ApiClient.getClient()!!.create(com.application.alphacapital.superapp.supermain.network.ApiInterface::class.java)
        binding.llLoading.llLoading.visibility = View.VISIBLE
        apiService.callContactUsAPI(binding.edtName.getValue(),binding.edtEmail.getValue(),binding.edtNumber.getValue(),binding.edtMessage.getValue(),sessionManager.userId).enqueue(object : Callback<CommonResponse>{
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>)
            {
                if (response.isSuccessful)
                {
                    if (response.body()!!.success == 1)
                    {
                        finish()
                        showToast(response.body()!!.message)
                    }
                    else
                    {
                        showToast(response.body()!!.message)
                    }
                    binding.llLoading.llLoading.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable)
            {
                apiFailedToast()
                binding.llLoading.llLoading.visibility = View.GONE
            }
        })
    }

}