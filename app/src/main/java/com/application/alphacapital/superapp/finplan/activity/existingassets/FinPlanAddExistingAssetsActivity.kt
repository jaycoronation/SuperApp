package com.application.alphacapital.superapp.finplan.activity.existingassets

import android.os.Bundle
import android.os.Handler
import android.view.View
import com.application.alphacapital.superapp.finplan.activity.FinPlanBaseActivity
import com.application.alphacapital.superapp.finplan.activity.FinPlanSelectionActivity
import com.alphafinancialplanning.model.CommonResponse
import com.application.alphacapital.superapp.finplan.model.ExistingAssetsListResponse
import com.alphafinancialplanning.model.InvestmentTypeResponse
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.CAME_FROM
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.SELECT_INVESTMENT_TYPE
import com.application.alphacapital.superapp.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fin_plan_activity_add_existing_assets.*
import kotlinx.android.synthetic.main.fin_plan_toolbar.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinPlanAddExistingAssetsActivity : FinPlanBaseActivity(), View.OnClickListener
{

    private var isForEdit = false

    private lateinit var editGetSet: ExistingAssetsListResponse.ExistingAsset

    companion object
    {
        var handler = Handler()
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fin_plan_activity_add_existing_assets)

        if (intent.hasExtra("isForEdit"))
        {
            isForEdit = intent.getBooleanExtra("isForEdit", false)
            if (isForEdit)
            {
                if (intent.hasExtra("data"))
                {
                    editGetSet = Gson().fromJson(intent.getStringExtra("data"),
                        ExistingAssetsListResponse.ExistingAsset::class.java
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
                    val getSet: InvestmentTypeResponse.InvestmentType =
                        it.obj as InvestmentTypeResponse.InvestmentType
                    edtInvestmentType.setText(getSet.investment_type)
                    edtAssetsType.setText(getSet.asset_type)
                }
            }

            false
        })
    }

    private fun initViews()
    {
        tvTitle.text = "Add Existing Assets".takeIf { !isForEdit } ?: "Update Existing Assets"
        ivBack.backNav()

        edtInvestmentType.setOnClickListener(this)
        btnSubmit.setOnClickListener(this)

        if (isForEdit)
        {
            edtInvestmentType.setText(editGetSet.investment_type)
            edtAssetsType.setText(editGetSet.asset_type)
            edtCurrentValue.setText(editGetSet.current_value.getAmountWithoutSymbol())
        }

        removeError(edtInvestmentType, inputInvestmentType)
        removeError(edtCurrentValue, inputCurrentValue)
    }

    override fun onClick(view: View?)
    {
        when (view?.id)
        {
            edtInvestmentType.id ->
            {
                startActivity<FinPlanSelectionActivity>(CAME_FROM to SELECT_INVESTMENT_TYPE
                )
                startActivityAnimation()
            }
            btnSubmit.id ->
            {
                when
                {
                    edtInvestmentType.isEmpty() ->
                    {
                        inputInvestmentType.error = "Please select investment type"
                    }
                    edtCurrentValue.isEmpty() ->
                    {
                        inputCurrentValue.error = "Please select assets current value"
                    }
                    else ->
                    {
                        if (isOnline())
                        {
                            loader.show()
                            apiService.saveExistingAssets(sessionManager.userId,
                                edtInvestmentType.getValue(),
                                edtAssetsType.getValue(),
                                edtCurrentValue.getValue(),
                                "".takeIf { !isForEdit } ?: editGetSet.existing_assets_id)
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
                                                    FinPlanExistingAssetsListActivity.handler,
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
