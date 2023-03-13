package com.application.alphacapital.superapp.finplan.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.alphacapital.superapp.finplan.activity.aspirationfutureexpense.FinPlanAddAspirationFutureExpenseActivity
import com.application.alphacapital.superapp.finplan.activity.existingassets.FinPlanAddExistingAssetsActivity
import com.application.alphacapital.superapp.finplan.activity.existingliabilities.FinPlanAddExistingLiabilitiesActivity
import com.application.alphacapital.superapp.finplan.activity.futureinflow.FinPlanAddFutureInFlowActivity
import com.alphafinancialplanning.model.AspirationTypeResponse
import com.alphafinancialplanning.model.InvestmentTypeResponse
import com.alphafinancialplanning.model.LiabilitiesTypeResponse
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.CAME_FROM
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.SELECT_ASPIRATION_TYPE
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.SELECT_END_YEAR
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.SELECT_INVESTMENT_TYPE
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.SELECT_LIABILITIES_TYPE
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.SELECT_LIFE_EXPECTANCY
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.SELECT_PERIODICITY
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.SELECT_RISKPROFILE
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.SELECT_START_YEAR
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.SELECT_TAXSLAB
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.SELECT_TIMEHORIZON
import com.application.alphacapital.superapp.finplan.utils.DataUtils
import com.application.alphacapital.superapp.R
import kotlinx.android.synthetic.main.fin_plan_activity_selection.*
import kotlinx.android.synthetic.main.fin_plan_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class FinPlanSelectionActivity : FinPlanBaseActivity()
{

    private val listTimeHorizon: MutableList<String> = mutableListOf()
    private val listTimeHorizonSearch: MutableList<String> = mutableListOf()

    private val listRiskProfile: MutableList<String> = mutableListOf()
    private val listRiskProfileSearch: MutableList<String> = mutableListOf()

    private val listTaxSlab: MutableList<String> = mutableListOf()
    private val listTaxSlabSearch: MutableList<String> = mutableListOf()

    private val listInvestmentTypes: MutableList<InvestmentTypeResponse.InvestmentType> = mutableListOf()
    private val listInvestmentTypesSearch: MutableList<InvestmentTypeResponse.InvestmentType> = mutableListOf()

    private val listLiabilitiesTypes: MutableList<LiabilitiesTypeResponse.Liability> = mutableListOf()
    private val listLiabilitiesTypesSearch: MutableList<LiabilitiesTypeResponse.Liability> = mutableListOf()

    private val listAspirationTypes: MutableList<AspirationTypeResponse.AspirationType> = mutableListOf()
    private val listAspirationTypeSearch: MutableList<AspirationTypeResponse.AspirationType> = mutableListOf()

    private val listStartYear: MutableList<String> = mutableListOf()
    private val listStartYearSearch: MutableList<String> = mutableListOf()

    private val listEndYear: MutableList<String> = mutableListOf()
    private val listEndYearSearch: MutableList<String> = mutableListOf()

    private val listPeriodicity: MutableList<String> = mutableListOf()
    private val listPeriodicitySearch: MutableList<String> = mutableListOf()

    private val listLifeExpectancy: MutableList<String> = mutableListOf()
    private val listLifeExpectancySearch: MutableList<String> = mutableListOf()


    private var cameFrom = ""
    private var startYear = ""

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fin_plan_activity_selection)

        if (intent.hasExtra(CAME_FROM))
        {
            cameFrom = intent.getStringExtra(CAME_FROM).toString()

            if (cameFrom == SELECT_END_YEAR)
            {
                if (intent.hasExtra("startYear"))
                {
                    startYear = intent.getStringExtra("startYear").toString()
                }
            }

        }

        initViews()
        getData()
    }

    private fun initViews()
    {
        ivBack.backNav()
        tvTitle.text = "Select $cameFrom"

        rvSelection.layoutManager = LinearLayoutManager(activity)

        if (cameFrom == SELECT_RISKPROFILE || cameFrom == SELECT_TIMEHORIZON || cameFrom == SELECT_TAXSLAB || cameFrom == SELECT_PERIODICITY || cameFrom == SELECT_LIFE_EXPECTANCY)
        {
            frameSearch.visibility = View.GONE
        }

        edtSearch.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(s: Editable?)
            {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
            {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
            {
                if (s?.length!! > 1)
                {
                    when (cameFrom)
                    {
                        SELECT_INVESTMENT_TYPE ->
                        {
                            listInvestmentTypesSearch.clear()
                            for (getSet in listInvestmentTypes)
                            {
                                if (getSet.investment_type.toLowerCase().contains(s?.toString().toLowerCase()))
                                {
                                    listInvestmentTypesSearch.add(getSet)
                                }
                            }
                            listInvestmentTypesSearch.sortBy { it.asset_type }
                            rvSelection.adapter = SelectionAdapter(true)
                        }
                        SELECT_LIABILITIES_TYPE ->
                        {
                            listLiabilitiesTypesSearch.clear()
                            for (getSet in listLiabilitiesTypes)
                            {
                                if (getSet.liability.toLowerCase().contains(s?.toString().toLowerCase()))
                                {
                                    listLiabilitiesTypesSearch.add(getSet)
                                }
                            }
                            listLiabilitiesTypesSearch.sortBy { it.liability }
                            rvSelection.adapter = SelectionAdapter(true)
                        }
                        SELECT_ASPIRATION_TYPE ->
                        {
                            listAspirationTypeSearch.clear()
                            for (getSet in listAspirationTypes)
                            {
                                if (getSet.aspiration_type.toLowerCase().contains(s?.toString().toLowerCase()))
                                {
                                    listAspirationTypeSearch.add(getSet)
                                }
                            }
                            listAspirationTypeSearch.sortBy { it.aspiration_type }
                            rvSelection.adapter = SelectionAdapter(true)
                        }
                        SELECT_START_YEAR ->
                        {
                            listStartYearSearch.clear()
                            for (getSet in listStartYear)
                            {
                                if (getSet.contains(s.toString()))
                                {
                                    listStartYearSearch.add(getSet)
                                }
                            }

                            rvSelection.adapter = SelectionAdapter(true)
                        }
                        SELECT_END_YEAR ->
                        {
                            listEndYearSearch.clear()
                            for (getSet in listEndYear)
                            {
                                if (getSet.contains(s.toString()))
                                {
                                    listEndYearSearch.add(getSet)
                                }
                            }

                            rvSelection.adapter = SelectionAdapter(true)
                        }
                        SELECT_PERIODICITY ->
                        {
                            listPeriodicitySearch.clear()
                            for (getSet in listPeriodicity)
                            {
                                if (getSet.contains(s.toString()))
                                {
                                    listPeriodicitySearch.add(getSet)
                                }
                            }

                            rvSelection.adapter = SelectionAdapter(true)
                        }
                    }
                }
                else if (s.isEmpty())
                {
                    rvSelection.adapter = SelectionAdapter(false)
                }
            }

        })
    }

    private fun getData()
    {
        when (cameFrom)
        {
            SELECT_TIMEHORIZON ->
            {
                listTimeHorizon.addAll(DataUtils.getTimeHorizon())
                rvSelection.adapter = SelectionAdapter(false)
            }
            SELECT_RISKPROFILE ->
            {
                listRiskProfile.addAll(DataUtils.getRiskProfile())
                rvSelection.adapter = SelectionAdapter(false)
            }
            SELECT_TAXSLAB ->
            {
                listTaxSlab.addAll(DataUtils.getTaxSlab())
                rvSelection.adapter = SelectionAdapter(false)
            }
            SELECT_PERIODICITY ->
            {
                listPeriodicity.addAll(DataUtils.getPeriodicity())
                rvSelection.adapter = SelectionAdapter(false)
            }
            SELECT_LIFE_EXPECTANCY ->
            {
                listLifeExpectancy.addAll(DataUtils.getLifeExpectancy())
                rvSelection.adapter = SelectionAdapter(false)
            }
            SELECT_INVESTMENT_TYPE ->
            {
                if (isOnline())
                {
                    loader.show()
                    apiService.getInvestmentTypes().enqueue(object : Callback<InvestmentTypeResponse>
                        {
                            override fun onFailure(call: Call<InvestmentTypeResponse>, t: Throwable)
                            {
                                apiFailedToast()
                                loader.dismiss()
                            }

                            override fun onResponse(call: Call<InvestmentTypeResponse>, response: Response<InvestmentTypeResponse>)
                            {
                                if (response.isSuccessful)
                                {
                                    loader.dismiss()
                                    if (response.body()!!.success == 1)
                                    {
                                        listInvestmentTypes.addAll(response.body()!!.investment_types)
                                        listInvestmentTypesSearch.sortBy { it.asset_type }
                                        rvSelection.adapter = SelectionAdapter(false)
                                    }
                                    else
                                    {
                                        showToast(response.body()!!.message)
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
                    finish()
                    finishActivityAnimation()
                }
            }
            SELECT_LIABILITIES_TYPE ->
            {
                if (isOnline())
                {
                    loader.show()
                    apiService.getLiabilitiesType().enqueue(object : Callback<LiabilitiesTypeResponse>
                        {
                            override fun onFailure(call: Call<LiabilitiesTypeResponse>, t: Throwable)
                            {
                                apiFailedToast()
                                loader.dismiss()
                            }

                            override fun onResponse(call: Call<LiabilitiesTypeResponse>, response: Response<LiabilitiesTypeResponse>)
                            {
                                if (response.isSuccessful)
                                {
                                    loader.dismiss()
                                    if (response.body()!!.success == 1)
                                    {
                                        listLiabilitiesTypes.addAll(response.body()!!.liabilities)
                                        listLiabilitiesTypes.sortBy { it.liability }
                                        rvSelection.adapter = SelectionAdapter(false)
                                    }
                                    else
                                    {
                                        showToast(response.body()!!.message)
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
                    finish()
                    finishActivityAnimation()
                }
            }
            SELECT_ASPIRATION_TYPE ->
            {
                if (isOnline())
                {
                    loader.show()
                    apiService.getAspirationTypes().enqueue(object : Callback<AspirationTypeResponse>
                        {
                            override fun onFailure(call: Call<AspirationTypeResponse>, t: Throwable)
                            {
                                apiFailedToast()
                                loader.dismiss()
                            }

                            override fun onResponse(call: Call<AspirationTypeResponse>, response: Response<AspirationTypeResponse>)
                            {
                                if (response.isSuccessful)
                                {
                                    loader.dismiss()
                                    if (response.body()!!.success == 1)
                                    {
                                        listAspirationTypes.addAll(response.body()!!.aspiration_types)
                                        listAspirationTypes.sortBy { it.aspiration_type }
                                        rvSelection.adapter = SelectionAdapter(false)
                                    }
                                    else
                                    {
                                        showToast(response.body()!!.message)
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
                    finish()
                    finishActivityAnimation()
                }
            }
            SELECT_START_YEAR ->
            {
                listStartYear.addAll(DataUtils.getYear())
                rvSelection.adapter = SelectionAdapter(false)
            }
            SELECT_END_YEAR ->
            {
                listEndYear.addAll(DataUtils.getYear().filter { Integer.parseInt(it) > Integer.parseInt(startYear) })
                rvSelection.adapter = SelectionAdapter(false)
            }
        }
    }

    inner class SelectionAdapter(val isForSearch: Boolean) : RecyclerView.Adapter<SelectionAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.fin_plan_rowview_selection, parent, false)
            )
        }

        override fun getItemCount(): Int
        {

            return when (cameFrom)
            {
                SELECT_TIMEHORIZON ->
                {
                    listTimeHorizon.size.takeIf { !isForSearch } ?: listTimeHorizonSearch.size
                }
                SELECT_RISKPROFILE ->
                {
                    listRiskProfile.size.takeIf { !isForSearch } ?: listRiskProfileSearch.size
                }
                SELECT_TAXSLAB ->
                {
                    listTaxSlab.size.takeIf { !isForSearch } ?: listTaxSlabSearch.size
                }
                SELECT_INVESTMENT_TYPE ->
                {
                    listInvestmentTypes.size.takeIf { !isForSearch } ?: listInvestmentTypesSearch.size
                }
                SELECT_LIABILITIES_TYPE ->
                {
                    listLiabilitiesTypes.size.takeIf { !isForSearch } ?: listLiabilitiesTypesSearch.size
                }
                SELECT_ASPIRATION_TYPE ->
                {
                    listAspirationTypes.size.takeIf { !isForSearch } ?: listAspirationTypeSearch.size
                }
                SELECT_START_YEAR ->
                {
                    listStartYear.size.takeIf { !isForSearch } ?: listStartYearSearch.size
                }
                SELECT_END_YEAR ->
                {
                    listEndYear.size.takeIf { !isForSearch } ?: listEndYearSearch.size
                }
                SELECT_PERIODICITY ->
                {
                    listPeriodicity.size.takeIf { !isForSearch } ?: listPeriodicitySearch.size
                }
                SELECT_LIFE_EXPECTANCY ->
                {
                    listLifeExpectancy.size.takeIf { !isForSearch } ?: listLifeExpectancySearch.size
                }
                else ->
                {
                    0
                }
            }
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            when (cameFrom)
            {
                SELECT_TIMEHORIZON ->
                {
                    val getSet: String = listTimeHorizon[position].takeIf { !isForSearch } ?: listTimeHorizonSearch[position]
                    holder.tvSelection.text = getSet
                }
                SELECT_RISKPROFILE ->
                {
                    val getSet: String = listRiskProfile[position].takeIf { !isForSearch } ?: listRiskProfileSearch[position]
                    holder.tvSelection.text = getSet
                }
                SELECT_TAXSLAB ->
                {
                    val getSet: String = listTaxSlab[position].takeIf { !isForSearch } ?: listTaxSlabSearch[position]
                    holder.tvSelection.text = getSet
                }
                SELECT_INVESTMENT_TYPE ->
                {
                    val getSet: InvestmentTypeResponse.InvestmentType = listInvestmentTypes[position].takeIf { !isForSearch } ?: listInvestmentTypesSearch[position]
                    holder.tvSelection.text = getSet.investment_type
                }
                SELECT_LIABILITIES_TYPE ->
                {
                    val getSet: LiabilitiesTypeResponse.Liability = listLiabilitiesTypes[position].takeIf { !isForSearch } ?: listLiabilitiesTypesSearch[position]
                    holder.tvSelection.text = getSet.liability
                }
                SELECT_ASPIRATION_TYPE ->
                {
                    val getSet: AspirationTypeResponse.AspirationType = listAspirationTypes[position].takeIf { !isForSearch } ?: listAspirationTypeSearch[position]
                    holder.tvSelection.text = getSet.aspiration_type
                }
                SELECT_START_YEAR ->
                {
                    val getSet: String = listStartYear[position].takeIf { !isForSearch } ?: listStartYearSearch[position]
                    holder.tvSelection.text = getSet
                }
                SELECT_END_YEAR ->
                {
                    val getSet: String = listEndYear[position].takeIf { !isForSearch } ?: listEndYearSearch[position]
                    holder.tvSelection.text = getSet
                }
                SELECT_PERIODICITY ->
                {
                    val getSet: String = listPeriodicity[position].takeIf { !isForSearch } ?: listPeriodicitySearch[position]
                    holder.tvSelection.text = getSet
                }
                SELECT_LIFE_EXPECTANCY ->
                {
                    val getSet: String = listLifeExpectancy[position].takeIf { !isForSearch } ?: listLifeExpectancySearch[position]
                    holder.tvSelection.text = getSet
                }
            }

            holder.itemView.setOnClickListener {
                when (cameFrom)
                {
                    SELECT_TIMEHORIZON ->
                    {
                        appUtils.sendMessageFromHandler(FinPlanUserProfileActivity.handler, listTimeHorizon[position].takeIf { !isForSearch } ?: listTimeHorizonSearch[position], 1, 0, 0)
                        finish()
                        finishActivityAnimation()
                    }
                    SELECT_RISKPROFILE ->
                    {
                        appUtils.sendMessageFromHandler(FinPlanUserProfileActivity.handler, listRiskProfile[position].takeIf { !isForSearch } ?: listRiskProfileSearch[position], 2, 0, 0)
                        finish()
                        finishActivityAnimation()
                    }
                    SELECT_TAXSLAB ->
                    {
                        appUtils.sendMessageFromHandler(FinPlanUserProfileActivity.handler, listTaxSlab[position].takeIf { !isForSearch } ?: listTaxSlabSearch[position], 3, 0, 0)
                        finish()
                        finishActivityAnimation()
                    }
                    SELECT_INVESTMENT_TYPE ->
                    {
                        appUtils.sendMessageFromHandler(FinPlanAddExistingAssetsActivity.handler, listInvestmentTypes[position].takeIf { !isForSearch } ?: listInvestmentTypesSearch[position], 1, 0, 0)
                        finish()
                        finishActivityAnimation()
                    }
                    SELECT_LIABILITIES_TYPE ->
                    {
                        appUtils.sendMessageFromHandler(FinPlanAddExistingLiabilitiesActivity.handler, listLiabilitiesTypes[position].takeIf { !isForSearch } ?: listLiabilitiesTypesSearch[position], 1, 0, 0)
                        finish()
                        finishActivityAnimation()
                    }
                    SELECT_START_YEAR ->
                    {
                        appUtils.sendMessageFromHandler(FinPlanAddFutureInFlowActivity.handler, listStartYear[position].takeIf { !isForSearch } ?: listStartYearSearch[position], 1, 0, 0)

                        appUtils.sendMessageFromHandler(FinPlanAddAspirationFutureExpenseActivity.handler, listStartYear[position].takeIf { !isForSearch } ?: listStartYearSearch[position], 2, 0, 0)

                        finish()
                        finishActivityAnimation()
                    }
                    SELECT_END_YEAR ->
                    {
                        appUtils.sendMessageFromHandler(FinPlanAddFutureInFlowActivity.handler, listEndYear[position].takeIf { !isForSearch } ?: listEndYearSearch[position], 2, 0, 0)

                        appUtils.sendMessageFromHandler(FinPlanAddAspirationFutureExpenseActivity.handler, listEndYear[position].takeIf { !isForSearch } ?: listEndYearSearch[position], 3, 0, 0)

                        finish()
                        finishActivityAnimation()
                    }
                    SELECT_ASPIRATION_TYPE ->
                    {
                        appUtils.sendMessageFromHandler(FinPlanAddAspirationFutureExpenseActivity.handler, listAspirationTypes[position].takeIf { !isForSearch } ?: listAspirationTypeSearch[position], 1, 0, 0)
                        finish()
                        finishActivityAnimation()
                    }
                    SELECT_PERIODICITY ->
                    {
                        appUtils.sendMessageFromHandler(FinPlanAddAspirationFutureExpenseActivity.handler, listPeriodicity[position].takeIf { !isForSearch } ?: listPeriodicitySearch[position], 4, 0, 0)
                        finish()
                        finishActivityAnimation()
                    }
                    SELECT_LIFE_EXPECTANCY ->
                    {
                        appUtils.sendMessageFromHandler(FinPlanUserProfileActivity.handler, listLifeExpectancy[position].takeIf { !isForSearch } ?: listLiabilitiesTypesSearch[position], 5, 0, 0)
                        finish()
                        finishActivityAnimation()
                    }
                }
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        {
            var tvSelection: TextView = itemView.findViewById(R.id.tvSelection)
        }
    }
}
