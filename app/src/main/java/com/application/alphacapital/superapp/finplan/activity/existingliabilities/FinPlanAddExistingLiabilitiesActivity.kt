package com.application.alphacapital.superapp.finplan.activity.existingliabilities

import android.os.Bundle
import android.os.Handler
import android.view.View
import com.application.alphacapital.superapp.finplan.activity.FinPlanBaseActivity
import com.application.alphacapital.superapp.finplan.activity.FinPlanSelectionActivity
import com.alphafinancialplanning.model.*
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.CAME_FROM
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.SELECT_LIABILITIES_TYPE
import com.application.alphacapital.superapp.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fin_plan_activity_add_existing_liabilities.*
import kotlinx.android.synthetic.main.fin_plan_toolbar.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinPlanAddExistingLiabilitiesActivity : FinPlanBaseActivity(), View.OnClickListener
{

    private var isForEdit = false

    private lateinit var editGetSet: ExistingLiabilitiesListResponse.ExistingLiability

    companion object
    {
        var handler = Handler()
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fin_plan_activity_add_existing_liabilities)

        if (intent.hasExtra("isForEdit"))
        {
            isForEdit = intent.getBooleanExtra("isForEdit", false)
            if (isForEdit)
            {
                if (intent.hasExtra("data"))
                {
                    editGetSet = Gson().fromJson(intent.getStringExtra("data"),
                        ExistingLiabilitiesListResponse.ExistingLiability::class.java
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
                    val getSet: LiabilitiesTypeResponse.Liability =
                        it.obj as LiabilitiesTypeResponse.Liability
                    edtLiabilityType.setText(getSet.liability)
                    edtAssetsType.setText(getSet.asset_type)
                }
            }

            false
        })
    }

    private fun initViews()
    {
        tvTitle.text =
            "Add Existing Liabilities".takeIf { !isForEdit } ?: "Update Existing Liabilities"
        ivBack.backNav()

        edtLiabilityType.setOnClickListener(this)
        btnSubmit.setOnClickListener(this)

        if (isForEdit)
        {
            edtLiabilityType.setText(editGetSet.liability_type)
            edtAssetsType.setText(editGetSet.asset_type)
            edtCurrentValue.setText(editGetSet.current_value.getAmountWithoutSymbol())
        }

        removeError(edtLiabilityType, inputLiabilityType)
        removeError(edtCurrentValue, inputCurrentValue)
    }

    override fun onClick(view: View?)
    {
        when (view?.id)
        {
            edtLiabilityType.id ->
            {
                startActivity<FinPlanSelectionActivity>(CAME_FROM to SELECT_LIABILITIES_TYPE
                )
                startActivityAnimation()
            }
            btnSubmit.id ->
            {
                when
                {
                    edtLiabilityType.isEmpty() ->
                    {
                        inputLiabilityType.error = "Please select liability type"
                    }
                    edtCurrentValue.isEmpty() ->
                    {
                        inputCurrentValue.error = "Please select liability's current value"
                    }
                    else ->
                    {
                        if (isOnline())
                        {
                            loader.show()
                            apiService.saveExistingLiabilities(sessionManager.userId,
                                edtLiabilityType.getValue(),
                                edtAssetsType.getValue(),
                                edtCurrentValue.getValue(),
                                "".takeIf { !isForEdit } ?: editGetSet.existing_liability_id)
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
                                                    FinPlanExistingLiabilityListActivity.handler,
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
