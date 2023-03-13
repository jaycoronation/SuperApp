package com.application.alphacapital.superapp.finplan.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.alphafinancialplanning.model.UserProfileResponse
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.CAME_FROM
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.SELECT_LIFE_EXPECTANCY
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.SELECT_RISKPROFILE
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.SELECT_TAXSLAB
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.SELECT_TIMEHORIZON
import com.application.alphacapital.superapp.finplan.utils.AppConstant.other.DONT_KNOW_RISK_PROFILE
import com.application.alphacapital.superapp.R
import kotlinx.android.synthetic.main.fin_plan_activity_user_profile.*
import kotlinx.android.synthetic.main.fin_plan_toolbar.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class FinPlanUserProfileActivity : FinPlanBaseActivity(), View.OnClickListener
{

    var cal = Calendar.getInstance()

    companion object
    {
        var handler = Handler()
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fin_plan_activity_user_profile)
        initViews()
        getUserDetails()
        handler = Handler(Handler.Callback {
            when (it.what)
            {
                1 ->
                {
                    edtTimeHorizon.setText(it.obj as String)
                }
                2 ->
                {
                    if (it.obj as String == DONT_KNOW_RISK_PROFILE)
                    {
                        startActivity<FinPlanCheckRiskProfileActivity>()
                        startActivityAnimation()
                    }
                    else
                    {
                        edtRiskProfile.setText(it.obj as String)
                    }
                }
                3 ->
                {
                    edtTaxSlab.setText(it.obj as String)
                }
                4 ->
                {
                    edtRiskProfile.setText(it.obj as String)
                }
                5 ->
                {
                    edtLifeExpectancy.setText(it.obj as String)
                }
            }
            false
        })
    }

    private fun initViews()
    {
        tvTitle.text = "Update Profile"
        ivBack.backNav()

        tvFindRiskProfile.visibility = View.GONE

        btnUpdateProfile.setOnClickListener(this)
        edtTimeHorizon.setOnClickListener(this)
        edtRiskProfile.setOnClickListener(this)
        edtTaxSlab.setOnClickListener(this)
        edtLifeExpectancy.setOnClickListener(this)
        tvFindRiskProfile.setOnClickListener(this)
    }

    override fun onClick(v: View?)
    {
        when (v?.id)
        {
            btnUpdateProfile.id ->
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
                else
                {
                    hideKeyBoard()
                    updateProfile()
                }

            }
            edtDob.id ->
            {
                val datePickerDialog = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val myFormat = "dd MMM yyyy" // mention the format you need
                    val sdf = SimpleDateFormat(myFormat)
                    edtDob.setText(sdf.format(cal.time))
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))

                datePickerDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis

                datePickerDialog.show()
            }
            edtTimeHorizon.id ->
            {
                startActivity<FinPlanSelectionActivity>(CAME_FROM to SELECT_TIMEHORIZON)
                startActivityAnimation()
            }
            edtRiskProfile.id ->
            {
                startActivity<FinPlanSelectionActivity>(CAME_FROM to SELECT_RISKPROFILE)
                startActivityAnimation()
            }
            edtTaxSlab.id ->
            {
                startActivity<FinPlanSelectionActivity>(CAME_FROM to SELECT_TAXSLAB)
                startActivityAnimation()
            }
            edtLifeExpectancy.id ->
            {
                startActivity<FinPlanSelectionActivity>(CAME_FROM to SELECT_LIFE_EXPECTANCY)
                startActivityAnimation()
            }
            tvFindRiskProfile.id ->
            {
                startActivity<FinPlanCheckRiskProfileActivity>()
                startActivityAnimation()
            }
        }
    }

    private fun getUserDetails()
    {
        if (isOnline())
        {
            loader.show()
            apiService.getUserProfile(sessionManager.userId).enqueue(object : Callback<UserProfileResponse>
                {
                    override fun onFailure(call: Call<UserProfileResponse>, t: Throwable)
                    {
                        loader.dismiss()
                        apiFailedToast()
                    }

                    override fun onResponse(call: Call<UserProfileResponse>, response: Response<UserProfileResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.body()!!.success == 1)
                            {

                                edtFirstName.setText(response.body()!!.profile.first_name)
                                edtLastName.setText(response.body()!!.profile.last_name)
                                edtEmail.setText(response.body()!!.profile.email)
                                edtMobile.setText(response.body()!!.profile.mobile)
                                edtDob.setText(appUtils.universalDateConvert(response.body()!!.profile.dob, "dd-MM-yyyy", "dd MMM yyyy"))
                                edtRetirementAge.setText(response.body()!!.profile.retirement_age)
                                edtLifeExpectancy.setText(response.body()!!.profile.life_expectancy)
                                edtTaxSlab.setText(response.body()!!.profile.tax_slab)
                                edtRiskProfile.setText(response.body()!!.profile.risk_profile)
                                edtTimeHorizon.setText(response.body()!!.profile.time_horizon)
                                edtAmountInvested.setText(response.body()!!.profile.amount_invested)

                                loader.dismiss()

                            }
                            else
                            {
                                apiFailedToast()
                                loader.dismiss()
                                finish()
                                finishActivityAnimation()
                            }
                        }
                        else
                        {
                            apiFailedToast()
                            finish()
                        }
                    }

                })
        }
        else
        {
            noInternetToast()
            finish()
            finishActivityAnimation()
        }
    }

    private fun updateProfile()
    {
        if (isOnline())
        {
            loader.show()
            apiService.updatePRofile(sessionManager.userId, edtFirstName.getValue(), edtLastName.getValue(), edtEmail.getValue(), edtMobile.getValue(), appUtils.universalDateConvert(edtDob.getValue(), "dd MMM yyyy", "dd-MM-yyyy"), edtRetirementAge.getValue(), edtLifeExpectancy.getValue(), edtTaxSlab.getValue(), edtRiskProfile.getValue(), edtTimeHorizon.getValue(), edtAmountInvested.getValue()).enqueue(object : Callback<UserProfileResponse>
                {
                    override fun onFailure(call: Call<UserProfileResponse>, t: Throwable)
                    {
                        apiFailedToast()
                        loader.dismiss()
                    }

                    override fun onResponse(call: Call<UserProfileResponse>, response: Response<UserProfileResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            showToast(response.body()!!.message)
                            loader.dismiss()
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
