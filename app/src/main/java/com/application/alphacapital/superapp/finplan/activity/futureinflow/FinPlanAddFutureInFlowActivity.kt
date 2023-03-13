package com.application.alphacapital.superapp.finplan.activity.futureinflow

import android.os.Bundle
import android.os.Handler
import android.view.View
import com.application.alphacapital.superapp.finplan.activity.FinPlanBaseActivity
import com.application.alphacapital.superapp.finplan.activity.FinPlanSelectionActivity
import com.alphafinancialplanning.model.*
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.CAME_FROM
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.SELECT_END_YEAR
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.SELECT_START_YEAR
import com.application.alphacapital.superapp.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fin_plan_activity_add_futureinflow.*
import kotlinx.android.synthetic.main.fin_plan_toolbar.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinPlanAddFutureInFlowActivity : FinPlanBaseActivity(), View.OnClickListener
{

    private var isForEdit = false

    private lateinit var editGetSet: FutureInFlowListResponse.FutureInflow

    companion object
    {
        var handler = Handler()
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fin_plan_activity_add_futureinflow)

        if (intent.hasExtra("isForEdit"))
        {
            isForEdit = intent.getBooleanExtra("isForEdit", false)
            if (isForEdit)
            {
                if (intent.hasExtra("data"))
                {
                    editGetSet = Gson().fromJson(intent.getStringExtra("data"),
                        FutureInFlowListResponse.FutureInflow::class.java
                    )
                }
            }
        }

        initViews()

        handler = Handler(Handler.Callback {
            when (it.what)
            {
                1 ->
                {
                    edtStartYear.setText(it.obj as String)
                    edtEndYear.setText("")
                }
                2 ->
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
            }

            false
        })
    }

    private fun initViews()
    {
        tvTitle.text = "Add Future Inflow".takeIf { !isForEdit } ?: "Update Future Inflow"
        ivBack.backNav()

        btnSubmit.setOnClickListener(this)
        edtStartYear.setOnClickListener(this)
        edtEndYear.setOnClickListener(this)

        if (isForEdit)
        {
            edtSource.setText(editGetSet.source)
            edtStartYear.setText(editGetSet.start_year)
            edtEndYear.setText(editGetSet.end_year)
            edtAmount.setText(editGetSet.amount.getAmountWithoutSymbol())
            edtExpectedGrowth.setText(editGetSet.expected_growth.replace("%", ""))
        }

        removeError(edtSource, inputSource)
        removeError(edtStartYear, inputStartYear)
        removeError(edtEndYear, inputEndYear)
        removeError(edtAmount, inputAmount)
        removeError(edtExpectedGrowth, inputExpectedGrowth)
    }

    override fun onClick(view: View?)
    {
        when (view?.id)
        {
            edtStartYear.id ->
            {
                startActivity<FinPlanSelectionActivity>(CAME_FROM to SELECT_START_YEAR
                )
                startActivityAnimation()
            }
            edtEndYear.id ->
            {
                if (edtStartYear.isEmpty())
                {
                    showToast("Select Start Year First")
                    return
                }
                startActivity<FinPlanSelectionActivity>(CAME_FROM to SELECT_END_YEAR,
                    "startYear" to edtStartYear.getValue()

                )
                startActivityAnimation()
            }
            btnSubmit.id ->
            {
                when
                {
                    edtSource.isEmpty() ->
                    {
                        inputSource.error = "Please enter future inflow source"
                    }
                    edtStartYear.isEmpty() ->
                    {
                        inputStartYear.error = "Please select start year"
                    }
                    edtEndYear.isEmpty() ->
                    {
                        inputEndYear.error = "Please select end year"
                    }
                    edtExpectedGrowth.isEmpty() ->
                    {
                        inputExpectedGrowth.error = "Please enter expected growth"
                    }
                    else ->
                    {
                        if (isOnline())
                        {
                            loader.show()
                            apiService.saveFutureInFlow(sessionManager.userId,
                                edtSource.getValue(),
                                edtStartYear.getValue(),
                                edtEndYear.getValue(),
                                edtExpectedGrowth.getValue(),
                                edtAmount.getValue(),
                                "".takeIf { !isForEdit } ?: editGetSet.future_inflow_id)
                                .enqueue(object : Callback<CommonResponse>
                                {
                                    override fun onFailure(call: Call<CommonResponse>, t: Throwable)
                                    {
                                        apiFailedToast()
                                        loader.dismiss()
                                    }

                                    override fun onResponse(call: Call<CommonResponse>,
                                                            response: Response<CommonResponse>)
                                    {
                                        if (response.isSuccessful)
                                        {
                                            showToast(response.body()!!.message)
                                            loader.dismiss()
                                            if (response.body()!!.success == 1)
                                            {

                                                appUtils.sendMessageFromHandler(
                                                    FinPlanFutureInFlowListActivity.handler,
                                                    "",
                                                    1,
                                                    0,
                                                    1
                                                )

                                                appUtils.sendMessageFromHandler(
                                                    FinPlanFutureInflowListReportActivity.handler,
                                                    "",
                                                    1,
                                                    0,
                                                    1
                                                )

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
