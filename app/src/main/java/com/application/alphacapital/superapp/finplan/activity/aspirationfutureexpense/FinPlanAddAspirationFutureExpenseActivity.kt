package com.application.alphacapital.superapp.finplan.activity.aspirationfutureexpense

import android.os.Bundle
import android.os.Handler
import android.view.View
import com.application.alphacapital.superapp.finplan.activity.FinPlanBaseActivity
import com.application.alphacapital.superapp.finplan.activity.FinPlanSelectionActivity
import com.alphafinancialplanning.model.*
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.CAME_FROM
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.SELECT_ASPIRATION_TYPE
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.SELECT_END_YEAR
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.SELECT_PERIODICITY
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.SELECT_START_YEAR
import com.application.alphacapital.superapp.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fin_plan_activity_add_aspire_future_expense.*
import kotlinx.android.synthetic.main.fin_plan_toolbar.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinPlanAddAspirationFutureExpenseActivity : FinPlanBaseActivity(), View.OnClickListener
{
    private var isForEdit = false

    private lateinit var editGetSet: AspirationFutureExpenseListResponse.AspirationFutureExpense

    companion object
    {
        var handler = Handler()
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fin_plan_activity_add_aspire_future_expense)

        if (intent.hasExtra("isForEdit"))
        {
            isForEdit = intent.getBooleanExtra("isForEdit", false)
            if (isForEdit)
            {
                if (intent.hasExtra("data"))
                {
                    editGetSet = Gson().fromJson(intent.getStringExtra("data"), AspirationFutureExpenseListResponse.AspirationFutureExpense::class.java)
                }
            }
        }

        initViews()

        handler = Handler {
            when (it.what)
            {
                1 ->
                {
                    val getSet: AspirationTypeResponse.AspirationType = it.obj as AspirationTypeResponse.AspirationType
                    edtAspirationType.setText(getSet.aspiration_type)
                }
                2 ->
                {
                    edtStartYear.setText(it.obj as String)
                    edtEndYear.setText("")
                }
                3 ->
                {
                    if (Integer.parseInt(edtStartYear.getValue()) > Integer.parseInt(it.obj as String))
                    {
                        showToast("End Year should less than or equals start year")
                    }
                    else
                    {
                        edtEndYear.setText(it.obj as String)
                    }
                }
                4 ->
                {
                    edtPeriodicity.setText(it.obj as String)
                }
            }

            false
        }
    }

    private fun initViews()
    {
        tvTitle.text = "Add Future Expense".takeIf { !isForEdit } ?: "Update Future Expense"
        ivBack.backNav()

        edtAspirationType.setOnClickListener(this)
        edtStartYear.setOnClickListener(this)
        edtEndYear.setOnClickListener(this)
        edtPeriodicity.setOnClickListener(this)
        btnSubmit.setOnClickListener(this)

        if (isForEdit)
        {
            edtAspirationType.setText(editGetSet.aspiration_type)
            edtStartYear.setText(editGetSet.start_year)
            edtEndYear.setText(editGetSet.end_year)
            edtAmount.setText(editGetSet.amount.getAmountWithoutSymbol())
            edtPeriodicity.setText(editGetSet.periodicity)
        }

        removeError(edtAspirationType, inputAspirationType)
        removeError(edtStartYear, inputStartYear)
        removeError(edtEndYear, inputEndYear)
        removeError(edtAmount, inputAmount)
        removeError(edtPeriodicity, inputPeriodicity)
    }

    override fun onClick(view: View?)
    {
        when (view?.id)
        {
            edtAspirationType.id ->
            {
                startActivity<FinPlanSelectionActivity>(CAME_FROM to SELECT_ASPIRATION_TYPE)
                startActivityAnimation()
            }
            edtStartYear.id ->
            {
                startActivity<FinPlanSelectionActivity>(CAME_FROM to SELECT_START_YEAR)
                startActivityAnimation()
            }
            edtEndYear.id ->
            {
                if (edtStartYear.isEmpty())
                {
                    showToast("Select Start Year First")
                    return
                }
                startActivity<FinPlanSelectionActivity>(CAME_FROM to SELECT_END_YEAR, "startYear" to edtStartYear.getValue()

                )
                startActivityAnimation()
            }
            edtPeriodicity.id ->
            {
                startActivity<FinPlanSelectionActivity>(CAME_FROM to SELECT_PERIODICITY)
                startActivityAnimation()
            }
            btnSubmit.id ->
            {
                when
                {
                    edtAspirationType.isEmpty() ->
                    {
                        inputAspirationType.error = "Please select aspiration type for future expense"
                    }
                    edtStartYear.isEmpty() ->
                    {
                        inputStartYear.error = "Please select start year"
                    }
                    edtEndYear.isEmpty() ->
                    {
                        inputEndYear.error = "Please select end year"
                    }
                    edtPeriodicity.isEmpty() ->
                    {
                        inputPeriodicity.error = "Please select periodicity of expense"
                    }
                    edtAmount.isEmpty() ->
                    {
                        inputAmount.error = "Please enter expense amount"
                    }
                    else ->
                    {
                        if (isOnline())
                        {
                            loader.show()
                            apiService.saveAspirationFutureExpense(sessionManager.userId, edtAspirationType.getValue(), edtStartYear.getValue(), edtEndYear.getValue(), edtPeriodicity.getValue(), edtAmount.getValue(), "".takeIf { !isForEdit } ?: editGetSet.aspiration_id).enqueue(object : Callback<CommonResponse>
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
                                            showToast(response.body()!!.message)
                                            loader.dismiss()
                                            if (response.body()!!.success == 1)
                                            {

                                                appUtils.sendMessageFromHandler(FinPlanAspirationFutureExpenseListActivity.handler, "", 1, 0, 1)

                                                appUtils.sendMessageFromHandler(FinPlanAspirationListReportActivity.handler, "", 1, 0, 1)

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
