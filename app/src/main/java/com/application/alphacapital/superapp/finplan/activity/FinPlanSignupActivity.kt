package com.application.alphacapital.superapp.finplan.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import com.alphafinancialplanning.model.CommonResponse
import com.application.alphacapital.superapp.R
import kotlinx.android.synthetic.main.fin_plan_activity_signup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class FinPlanSignupActivity : FinPlanBaseActivity(), View.OnClickListener
{

    var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fin_plan_activity_signup)

        initViews()
    }

    private fun initViews()
    {
        btnSignup.setOnClickListener(this)
        edtDob.setOnClickListener(this)

        removeError(edtFirstName, inputFirstName)
        removeError(edtLastName, inputLastName)
        removeError(edtEmail, inputEmail)
        removeError(edtDob, inputDob)
        removeError(edtMobile, inputMobile)
        removeError(edtPassword, inputPassword)
    }

    override fun onClick(v: View?)
    {
        when (v?.id)
        {
            btnSignup.id ->
            {
                if (edtFirstName.isEmpty())
                {
                    inputFirstName.error = "Please enter first name"
                }
                else if (edtLastName.isEmpty())
                {
                    inputLastName.error = "Please enter last name"
                }
                else if (edtEmail.isEmpty())
                {
                    inputEmail.error = "Please enter email address"
                }
                else if (!appUtils.isEmailValid(edtEmail.getValue()))
                {
                    inputEmail.error = "Please enter valid email address"
                }
                else if (edtMobile.isEmpty())
                {
                    inputMobile.error = "Please enter mobile number"
                }
                else if (edtMobile.getValue().length != 10)
                {
                    inputMobile.error = "Please enter valid mobile number"
                }
                else if (edtDob.isEmpty())
                {
                    inputDob.error = "Please select your date of birth"
                }
                else if (edtPassword.isEmpty())
                {
                    inputPassword.error = "Please enter password"
                }
                else if (!appUtils.validPassword(edtPassword.getValue()))
                {
                    inputPassword.error =
                        "Your password should contain one special character, one letter one number and minimum 8 character long"
                }
                else
                {
                    hideKeyBoard()
                    signup()
                }
            }
            edtDob.id ->
            {
                val datePickerDialog = DatePickerDialog(
                    activity,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        cal.set(Calendar.YEAR, year)
                        cal.set(Calendar.MONTH, monthOfYear)
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        val myFormat = "dd MMM yyyy" // mention the format you need
                        val sdf = SimpleDateFormat(myFormat)
                        edtDob.setText(sdf.format(cal.time))
                    },
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                )

                datePickerDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis

                datePickerDialog.show()
            }
        }
    }

    private fun signup()
    {
        if (isOnline())
        {
            loader.show()
            apiService.signup(
                edtFirstName.getValue(),
                edtLastName.getValue(),
                edtEmail.getValue(),
                edtMobile.getValue(),
                appUtils.universalDateConvert(edtDob.getValue(), "dd MMM yyyy", "dd-MM-yyyy"),
                edtPassword.getValue()).enqueue(object : Callback<CommonResponse>
                {
                    override fun onFailure(call: Call<CommonResponse>, t: Throwable)
                    {
                        loader.dismiss()
                        apiFailedToast()
                    }

                    override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>)
                    {
                        if (response.isSuccessful)
                        {
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
                        loader.dismiss()
                    }

                })
        }
        else
        {
            noInternetToast()
        }
    }
}
