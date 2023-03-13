package com.application.alphacapital.superapp.supermain.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.application.alphacapital.superapp.vault.utils.VaultSessionManager
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.acpital.CapitalPref
import com.application.alphacapital.superapp.acpital.model.LoginResponseModel
import com.application.alphacapital.superapp.databinding.ActivityMainLoginBinding
import com.application.alphacapital.superapp.pms.utils.ApiUtils
import com.application.alphacapital.superapp.pms.utils.SessionManager
import com.application.alphacapital.superapp.supermain.model.SuperLoginResponseModel
import com.application.alphacapital.superapp.vault.services.ServiceUtils
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainLoginActivity : BaseActivity()
{
    private lateinit var binding : ActivityMainLoginBinding
    private lateinit var pmsSession : SessionManager
    private lateinit var vaultSession : VaultSessionManager
    private lateinit var finPlanSession : com.application.alphacapital.superapp.finplan.utils.SessionManager

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(activity,R.layout.activity_main_login)
        //setStatusBarGradiant(activity)
        initView()
    }

    private fun initView()
    {
        pmsSession = SessionManager(activity)
        vaultSession = VaultSessionManager(activity)
        finPlanSession = com.application.alphacapital.superapp.finplan.utils.SessionManager(activity)
        binding.btnLogin.setOnClickListener {
            if (binding.edtEmail.getValue().isEmpty())
            {
                showToast("Please enter email")
                return@setOnClickListener
            }
            if (binding.edtPassword.getValue().isEmpty())
            {
                showToast("Please enter password")
                return@setOnClickListener
            }
            callInvestWellLoginAPI()
        }
    }

    private fun callInvestWellLoginAPI()
    {
        try
        {
            binding.llLoading.llLoading.visibility = View.VISIBLE
            val params: MutableMap<String, String> = HashMap()
            params["user"] = binding.edtEmail.getValue()
            params["password"] = binding.edtPassword.getValue()
            apiService.inveswellLogin(params).enqueue(object : Callback<LoginResponseModel>{
                override fun onResponse(call: Call<LoginResponseModel>, response: Response<LoginResponseModel>)
                {
                    if (response.isSuccessful)
                    {
                        if (response.body()!!.flag == "Y")
                        {
                            callSuperAppLoginAPI(response.body()!!.uid,binding.edtEmail.getValue(),response.body()!!,binding.edtPassword.getValue())
                        }
                        else
                        {
                            showToast("Invalid Username or Password")
                            binding.llLoading.llLoading.visibility = View.GONE
                        }
                    }
                    else
                    {
                        apiFailedToast()
                    }
                }

                override fun onFailure(call: Call<LoginResponseModel>, t: Throwable)
                {
                    apiFailedToast()
                    Log.e("<><>API FAILED", t.message.toString())
                }
            })
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun callSuperAppLoginAPI(username: String, email: String, body: LoginResponseModel, password: String)
    {
        try
        {
            apiService.superappLogin(username,email,password).enqueue(object : Callback<SuperLoginResponseModel>
            {
                override fun onResponse(call: Call<SuperLoginResponseModel>, response: Response<SuperLoginResponseModel>)
                {
                    if (response.isSuccessful)
                    {
                        if (response.body()!!.success == 1)
                        {
                            val user = response.body()!!
                            pmsSession.createLoginSession(user.portfolio.user_id,"","",user.portfolio.first_name ,user.portfolio.last_name,user.portfolio.email)
                            vaultSession.createLogin(user.vault.user_id,user.vault.username,user.vault.email,user.vault.phone,user.vault.image,user.vault.country_name,user.vault.country_id,user.vault.state_name,user.vault.state_id,user.vault.city_name,user.vault.city_id)
                            sessionManager.createLogin(user.profile.user_id, user.profile.username, user.profile.username, user.profile.email, user.profile.phone, "", false)
                            CapitalPref.putString(this@MainLoginActivity, "uid", body.uid)
                            CapitalPref.putString(this@MainLoginActivity, "password", binding.edtPassword.getValue())
                            CapitalPref.putString(this@MainLoginActivity, "bid", body.bid)

                            if(pmsSession.isLoggedIn)
                            {
                                ApiUtils.END_USER_ID = pmsSession.userId
                                val serviceUtils = ServiceUtils()
                                serviceUtils.scheduleJob(activity)
                            }

                            startActivity<MainActivity>()
                            finish()
                        }
                        else
                        {
                            showToast(response.body()!!.message)
                        }
                    }
                    binding.llLoading.llLoading.visibility = View.GONE
                }

                override fun onFailure(call: Call<SuperLoginResponseModel>, t: Throwable)
                {
                    apiFailedToast()
                    Log.e("<><>API FAILED", t.message.toString())
                    binding.llLoading.llLoading.visibility = View.GONE
                }
            })
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

}