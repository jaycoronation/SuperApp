package com.application.alphacapital.superapp.supermain.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.alphaestatevault.model.CommonResponse
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.supermain.model.SuperLoginResponseModel
import kotlinx.android.synthetic.main.fin_plan_activity_signup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity : BaseActivity()
{
    private lateinit var binding : com.application.alphacapital.superapp.databinding.ActivityProfileBinding
    var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(activity,R.layout.activity_profile)
        initView()
        onClick()
    }

    private fun initView()
    {
        getProfileAPI()
        binding.toolbar.txtTitle.text = "Profile"
        try
        {
            removeError(binding.edtUserName,binding.ipUserName)
            removeError(binding.edtFName,binding.ipFName)
            removeError(binding.edtLName,binding.ipLName)
            removeError(binding.edtEmail,binding.ipEmail)
            removeError(binding.edtMobile,binding.ipMobile)
            removeError(binding.edtDOB,binding.ipDOB)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun onClick()
    {

        binding.edtDOB.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                activity,
                { view, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val myFormat = "dd MMM,yyyy" // mention the format you need
                    val sdf = SimpleDateFormat(myFormat)
                    binding.edtDOB.setText(sdf.format(cal.time))
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )

            datePickerDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis

            datePickerDialog.show()
        }

        binding.tvSubmit.setOnClickListener {
            if (binding.edtUserName.getValue().isEmpty())
            {
                binding.ipUserName.error = "Please enter your username"
                return@setOnClickListener
            }
            if (binding.edtFName.getValue().isEmpty())
            {
                binding.ipFName.error = "Please enter your first name"
                return@setOnClickListener
            }
            if (binding.edtLName.getValue().isEmpty())
            {
                binding.ipLName.error = "Please enter your last name"
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
            if (binding.edtMobile.getValue().isEmpty())
            {
                binding.ipMobile.error = "Please enter your mobile number"
                return@setOnClickListener
            }
            if (binding.edtMobile.getValue().length != 10)
            {
                binding.ipMobile.error = "Please enter valid mobile number"
                return@setOnClickListener
            }
            callUpdateProfileAPI()
        }
    }

    private fun callUpdateProfileAPI()
    {
        binding.llLoading.llLoading.visibility = View.VISIBLE

        val date = SimpleDateFormat("dd MMM,yyyy").parse(binding.edtDOB.getValue())
        val birthDate = date.time/ 1000
        println(birthDate)

       apiService.updateProfileAPI(binding.edtUserName.getValue(),
           binding.edtFName.getValue(),
           binding.edtLName.getValue(),
           binding.edtEmail.getValue(),
           binding.edtMobile.getValue(),
           birthDate.toString(),
       ).enqueue(object : Callback<CommonResponse>{
           override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
               if (response.isSuccessful)
               {
                   if (response.body()?.success == 1)
                   {
                       showToast(response.body()?.message.toString())
                       finish()
                   }
                   else
                   {
                       showToast(response.body()?.message.toString())
                   }
               }
               else
               {
                   apiFailedToast()
               }
               binding.llLoading.llLoading.visibility = View.GONE
           }

           override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
               binding.llLoading.llLoading.visibility = View.GONE
               apiFailedToast()
           }
       })
    }

    private fun getProfileAPI()
    {
        try
        {
            apiService.superappLogin(sessionManager.userName,sessionManager.email,"").enqueue(object : Callback<SuperLoginResponseModel>
            {
                override fun onResponse(call: Call<SuperLoginResponseModel>, response: Response<SuperLoginResponseModel>)
                {
                    if (response.isSuccessful)
                    {
                        if (response.body()!!.success == 1)
                        {
                            val user = response.body()!!

                            binding.edtUserName.setText(user.profile.username)
                            binding.edtFName.setText(user.portfolio.first_name)
                            binding.edtLName.setText(user.portfolio.last_name)
                            binding.edtEmail.setText(user.profile.email)
                            binding.edtMobile.setText(user.profile.phone)
                            if (user.profile.dob.isNotEmpty())
                            {
                                binding.edtDOB.setText(getDateTime(user.profile.dob))
                            }
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

    private fun getDateTime(s: String): String?
    {
        return try {
            val sdf = SimpleDateFormat("dd MMM,yyyy")
            val netDate = Date(s.toLong() * 1000)
            sdf.format(netDate)
        } catch (e: Exception) {
            e.toString()
        }
    }

}