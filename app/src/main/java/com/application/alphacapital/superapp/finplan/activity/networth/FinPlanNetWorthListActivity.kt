package com.application.alphacapital.superapp.finplan.activity.networth

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.alphacapital.superapp.finplan.activity.FinPlanBaseActivity
import com.alphafinancialplanning.model.NetWorthListResponse
import com.application.alphacapital.superapp.R
import kotlinx.android.synthetic.main.fin_plan_activity_list_report.*
import kotlinx.android.synthetic.main.fin_plan_item_networth.view.*
import kotlinx.android.synthetic.main.fin_plan_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinPlanNetWorthListActivity : FinPlanBaseActivity()
{

    private var listAspiration: MutableList<NetWorthListResponse.Networth.NetWorthList> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fin_plan_activity_list_report)
        initViews()
        getNetWorthList()
    }

    private fun initViews()
    {
        tvTitle.text = "Networth"
        ivBack.backNav()

        tvNoData.text = "Networth not found!"
        rvAspiration.layoutManager = LinearLayoutManager(activity)
    }

    private fun getNetWorthList()
    {
        if (isOnline())
        {
            loader.show()
            apiService.getNetWorth(sessionManager.userId).enqueue(object : Callback<NetWorthListResponse>
                {
                    override fun onFailure(call: Call<NetWorthListResponse>, t: Throwable)
                    {
                        apiFailedToast()
                        loader.dismiss()
                    }

                    override fun onResponse(call: Call<NetWorthListResponse>, response: Response<NetWorthListResponse>)
                    {
                        if (response.isSuccessful)
                        {

                            if (response.body()!!.success == 1)
                            {
                                listAspiration.addAll(response.body()!!.networth.list)
                                if (listAspiration.isNotEmpty())
                                {
                                    tvNoData.visibility = View.GONE
                                    rvAspiration.adapter = AspirationAdapter(listAspiration)

                                    cardNetWorth.visibility = View.VISIBLE
                                    tvCurrentValue.text = getPrice(response.body()!!.networth.total.current_value)


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

    inner class AspirationAdapter(private val list: List<NetWorthListResponse.Networth.NetWorthList>) : RecyclerView.Adapter<AspirationAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.fin_plan_item_networth, parent, false))
        }

        override fun getItemCount(): Int
        {
            return list.size
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            var getSet: NetWorthListResponse.Networth.NetWorthList = list[position]

            holder.itemView.tvInvestmentType.text = getSet.investment_type
            holder.itemView.tvCurrentValue.text = getPrice(getSet.current_value)
            holder.itemView.tvAssetsType.text = getSet.asset_type
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        {
            var tvInvestmentType: TextView = itemView.findViewById(R.id.tvInvestmentType)
            var tvCurrentValue: TextView = itemView.findViewById(R.id.tvCurrentValue)
            var tvAssetsType: TextView = itemView.findViewById(R.id.tvAssetsType)
        }

    }
}
