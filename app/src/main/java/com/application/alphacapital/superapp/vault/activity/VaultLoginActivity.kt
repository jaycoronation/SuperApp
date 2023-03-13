package com.application.alphacapital.superapp.vault.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.alphaestatevault.model.LoginResponse
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VaultLoginActivity : VaultBaseActivity()
{
    lateinit var binding: VaultActivityLoginBinding
    lateinit var mGoogleSignInClient: GoogleSignInClient
    val RC_SIGN_IN = 100
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_login)
        appUtils.setLightStatusBar(activity)
        initView()
        onClicks()
    }

    private fun initView()
    {
        appUtils.removeError(binding.edtEmailOrMobile, binding.ipEmailOrMobile)
        appUtils.removeError(binding.edtPassword, binding.ipPassword)
        try
        {
            /*val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()*/
            //mGoogleSignInClient = GoogleSignIn.getClient(activity, gso)
        }
        catch (e: Exception)
        {
        }
    }

    private fun onClicks()
    {
        binding.txtSignup.setOnClickListener {

            val intent = Intent(activity, VaultSignupActivity::class.java)
            startActivity(intent)
            startActivityAnimation()
        }

        binding.llLoginWithGoogle.setOnClickListener { view ->
            signInWithGoogle()
        }

        binding.txtForgotPassword.setOnClickListener {

            val intent = Intent(activity, VaultForgotPasswordActivity::class.java)
            startActivity(intent)
            startActivityAnimation()
        }

        binding.txtLogin.setOnClickListener {

            if (binding.edtEmailOrMobile.text!!.isEmpty())
            {
                binding.ipEmailOrMobile.error = "Please enter your mobile or email address."
            }
            else if (binding.edtPassword.text!!.isEmpty())
            {
                binding.ipPassword.error = "Please enter your password."
            }
            else
            {
                hideKeyBoard()
                if (isOnline())
                {
                    signIn()
                }
                else
                {
                    noInternetToast()
                }
            }
        }
    }

    private fun signIn()
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.signInCall(getEditTextString(binding.edtEmailOrMobile), getEditTextString(binding.edtPassword))

                call.enqueue(object : Callback<LoginResponse>
                {
                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            val signUpResponse = response.body()!!

                            if (signUpResponse.success == 1)
                            {
                                try
                                {
                                    sessionManager.createLogin(response.body()!!.user_id, response.body()!!.name, response.body()!!.email, response.body()!!.mobile, response.body()!!.profile_pic, response.body()!!.country_name, response.body()!!.country, response.body()!!.state_name, response.body()!!.state, response.body()!!.city_name, response.body()!!.city)

                                    val intent = Intent(applicationContext, VaultHomeActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    startActivity(intent)
                                    startActivityAnimation()
                                    finish()
                                }
                                catch (e: Exception)
                                {
                                }
                            }
                            else
                            {
                                toast(signUpResponse.message)
                            }


                        }
                        else
                        {
                            apiFailedToast()
                        }

                        binding.loading.llLoading.visibility = View.GONE
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable)
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

    fun signInWithGoogle()
    {
        try
        {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    fun signOut()
    {
        mGoogleSignInClient.signOut()
    }

    fun handleSignInResult(completedTask: Task<GoogleSignInAccount>)
    {
        try
        {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)!!
            val email: String? = account.email
            val familyName: String? = account.familyName
            val givenName: String? = account.givenName
            print(email)
            print(familyName)
            print(givenName)
            signOut()

            if (email!!.isNotEmpty())
            {
                Log.e("<><> DATA : ",email + " " +familyName + " "+givenName + " <><>")
                val name = givenName+" " +familyName
                signInWithGoogleApi(name,email)
            }
            else
            {
                showToast("Email address not found.")
            }
        }
        catch (e: Exception)
        {
        }
    }

    private fun signInWithGoogleApi(name: String, email: String)
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.socialLogin(name,email,"","2")

                call.enqueue(object : Callback<LoginResponse>
                {
                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            val signUpResponse = response.body()!!

                            if (signUpResponse.success == 1)
                            {
                                try
                                {
                                    sessionManager.createLogin(response.body()!!.user_id, response.body()!!.name, response.body()!!.email, response.body()!!.mobile, response.body()!!.profile_pic, response.body()!!.country_name, response.body()!!.country, response.body()!!.state_name, response.body()!!.state, response.body()!!.city_name, response.body()!!.city)
                                    val intent = Intent(applicationContext, VaultHomeActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    startActivity(intent)
                                    startActivityAnimation()
                                    finish()
                                }
                                catch (e: Exception)
                                {
                                }
                            }
                            else
                            {
                                toast(signUpResponse.message)
                            }
                        }
                        else
                        {
                            apiFailedToast()
                        }

                        binding.loading.llLoading.visibility = View.GONE
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN)
        {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }
}
