package com.application.alphacapital.superapp.finplan.activity.futureinflow

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.alphacapital.superapp.finplan.activity.FinPlanBaseActivity
import com.alphafinancialplanning.model.FutureInflowListReportResponse
import com.application.alphacapital.superapp.R
import kotlinx.android.synthetic.main.fin_plan_activity_list_report.*
import kotlinx.android.synthetic.main.fin_plan_item_aspiration.view.tvEndYear
import kotlinx.android.synthetic.main.fin_plan_item_aspiration.view.tvStartYear
import kotlinx.android.synthetic.main.fin_plan_item_futureinflow_report.view.*
import kotlinx.android.synthetic.main.fin_plan_toolbar.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinPlanFutureInflowListReportActivity : FinPlanBaseActivity(), View.OnClickListener
{

    private var listAspiration: MutableList<FutureInflowListReportResponse.FutureInflow.FutureInflowList> =
        mutableListOf()

    companion object
    {
        var handler: Handler = Handler()
    }

    private var shouldRedirect = true

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fin_plan_activity_list_report)
        initViews()
        getFutureinflowList()

        handler = Handler {
            when (it.what)
            {
                1 ->
                {
                    shouldRedirect = false
                    getFutureinflowList()
                }
            }
            false
        }
    }

    private fun initViews()
    {
        tvTitle.text = "Future Inflow"
        ivBack.backNav()

        tvNoData.text = "Future inflow not found!\nclick on above manage button to add"

        tvManage.visibility = View.VISIBLE
        tvManage.setOnClickListener(this)

        rvAspiration.layoutManager = LinearLayoutManager(activity)
    }

    override fun onClick(view: View?)
    {
        when (view?.id)
        {
            tvManage.id ->
            {
                startActivity<FinPlanFutureInFlowListActivity>()
                startActivityAnimation()
            }
        }
    }

    private fun getFutureinflowList()
    {
        if (isOnline())
        {
            loader.show()
            apiService.getFutureInflowListForReport(sessionManager.userId)
                .enqueue(object : Callback<FutureInflowListReportResponse>
                {
                    override fun onFailure(call: Call<FutureInflowListReportResponse>, t: Throwable)
                    {
                        apiFailedToast()
                        loader.dismiss()
                    }

                    override fun onResponse(call: Call<FutureInflowListReportResponse>,
                                            response: Response<FutureInflowListReportResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.body()!!.success == 1)
                            {
                                listAspiration.addAll(response.body()!!.future_inflow.list)
                                if (listAspiration.isNotEmpty())
                                {
                                    tvNoData.visibility = View.GONE
                                    rvAspiration.adapter = AspirationAdapter(listAspiration)

                                    cardFutureInFlow.visibility = View.VISIBLE
                                    tvInflationAdjustedIncome.text = getPrice(response.body()!!.future_inflow.total.inflation_adjusted_income)
                                    tvPVOfIncome.text = getPrice(response.body()!!.future_inflow.total.pv_of_income)

                                    loader.dismiss()
                                }
                                else
                                {
                                    tvNoData.visibility = View.VISIBLE
                                    loader.dismiss()
                                    /*if (shouldRedirect)
                                    {
                                        startActivity<FinPlanFutureInFlowListActivity>()
                                        startActivityAnimation()
                                    }*/
                                }
                            }
                            else
                            {
                                loader.dismiss()
                                tvNoData.visibility = View.VISIBLE
                                /*if (shouldRedirect)
                                {
                                    startActivity<FinPlanFutureInFlowListActivity>()
                                    startActivityAnimation()
                                }*/
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
            activity
        }
    }

    inner class AspirationAdapter(private val list: List<FutureInflowListReportResponse.FutureInflow.FutureInflowList>) : RecyclerView.Adapter<AspirationAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity)
                .inflate(R.layout.fin_plan_item_futureinflow_report, parent, false)
            )
        }

        override fun getItemCount(): Int
        {
            return list.size
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            var getSet: FutureInflowListReportResponse.FutureInflow.FutureInflowList =
                list[position]

            holder.itemView.tvSource.text = getSet.source
            holder.itemView.tvStartYear.text = getSet.start_year
            holder.itemView.tvEndYear.text = getSet.end_year
            holder.itemView.tvExpectedGrowth.text = getSet.expected_growth
            holder.itemView.tvAmount.text = getPrice(getSet.amount)
            holder.itemView.tvPVOfIncome.text = getPrice(getSet.pv_of_income)
            holder.itemView.tvInflationAdjustment.text = getPrice(getSet.inflation_adjusted_income)

        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        {
            var tvSource: TextView = itemView.findViewById(R.id.tvSource)
            var tvStartYear: TextView = itemView.findViewById(R.id.tvStartYear)
            var tvEndYear: TextView = itemView.findViewById(R.id.tvEndYear)
            var tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
            var tvExpectedGrowth: TextView = itemView.findViewById(R.id.tvExpectedGrowth)
            var tvInflationAdjustment: TextView = itemView.findViewById(R.id.tvInflationAdjustment)
            var tvPVOfIncome: TextView = itemView.findViewById(R.id.tvPVOfIncome)
        }

    }
}
