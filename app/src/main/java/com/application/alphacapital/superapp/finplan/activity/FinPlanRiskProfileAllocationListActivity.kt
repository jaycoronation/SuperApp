package com.application.alphacapital.superapp.finplan.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alphafinancialplanning.model.RiskProfileAllocationResponse
import com.application.alphacapital.superapp.finplan.utils.AppConstant
import com.application.alphacapital.superapp.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fin_plan_activity_list_report.*
import kotlinx.android.synthetic.main.fin_plan_item_profile_allocation.view.*
import kotlinx.android.synthetic.main.fin_plan_toolbar.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinPlanRiskProfileAllocationListActivity : FinPlanBaseActivity()
{

    private var listBalances: MutableList<RiskProfileAllocationResponse.RiskProfileAllocation> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fin_plan_activity_list_report)
        initViews()
        getNetWorthList()
    }

    private fun initViews()
    {
        tvTitle.text = "Risk Profile Allocation"
        ivBack.backNav()

        tvNoData.text = "Risk Profile Allocation data not found!"
        rvAspiration.layoutManager = LinearLayoutManager(activity)

        ivGraph.setOnClickListener {
            startActivity<FinPlanGraphActivity>(AppConstant.camefrom.CAME_FROM to AppConstant.camefrom.GRAPH_RISK_PROFILE_ALLOCATION, "data" to Gson().toJson(listBalances)
            )
        }
    }

    private fun getNetWorthList()
    {
        if (isOnline())
        {
            loader.show()
            apiService.getRiskProfileAllocationList(sessionManager.userId).enqueue(object : Callback<RiskProfileAllocationResponse>
                {
                    override fun onFailure(call: Call<RiskProfileAllocationResponse>, t: Throwable)
                    {
                        apiFailedToast()
                        loader.dismiss()
                    }

                    override fun onResponse(call: Call<RiskProfileAllocationResponse>, response: Response<RiskProfileAllocationResponse>)
                    {
                        if (response.isSuccessful)
                        {

                            if (response.body()!!.success == 1)
                            {
                                listBalances.clear()
                                listBalances.addAll(response.body()!!.risk_profile_allocation)
                                ivGraph.visibility = View.VISIBLE.takeIf { listBalances.isNotEmpty() } ?: View.GONE
                                if (listBalances.isNotEmpty())
                                {
                                    tvNoData.visibility = View.GONE
                                    rvAspiration.adapter = AspirationAdapter(listBalances)

                                    loader.dismiss()
                                }
                                else
                                {
                                    tvNoData.visibility = View.VISIBLE
                                    loader.dismiss()
                                }
                            }
                            else
                            {
                                loader.dismiss()
                                tvNoData.visibility = View.VISIBLE
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

    inner class AspirationAdapter(private val list: List<RiskProfileAllocationResponse.RiskProfileAllocation>) : RecyclerView.Adapter<AspirationAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.fin_plan_item_profile_allocation, parent, false))
        }

        override fun getItemCount(): Int
        {
            return list.size
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            var getSet: RiskProfileAllocationResponse.RiskProfileAllocation = list[position]

            holder.itemView.tvAssetClass.text = getSet.asset_class
            holder.itemView.tvAllocation.text = getSet.allocation
            holder.itemView.tvExpectedReturn.text = getSet.expected_return
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        {

        }

    }
}
