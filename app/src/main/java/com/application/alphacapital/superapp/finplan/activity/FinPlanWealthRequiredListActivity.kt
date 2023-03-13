package com.application.alphacapital.superapp.finplan.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alphafinancialplanning.model.WealthRequiredListResponse
import com.application.alphacapital.superapp.finplan.utils.AppConstant
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.finplan.utils.AppUtils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fin_plan_activity_list_report.*
import kotlinx.android.synthetic.main.fin_plan_item_wealth_required.view.*
import kotlinx.android.synthetic.main.fin_plan_toolbar.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinPlanWealthRequiredListActivity : FinPlanBaseActivity()
{

    private var listBalances: MutableList<WealthRequiredListResponse.WealthRequired> = mutableListOf()

    private var total_wealth: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fin_plan_activity_list_report)
        initViews()
        getNetWorthList()
    }

    private fun initViews()
    {
        tvTitle.text = "Wealth Required"
        ivBack.backNav()

        tvNoData.text = "Wealth Required data not found!"
        rvAspiration.layoutManager = LinearLayoutManager(activity)

        ivGraph.setOnClickListener {
            startActivity<FinPlanGraphActivity>(AppConstant.camefrom.CAME_FROM to AppConstant.camefrom.GRAPH_WEALTH_REQUIRED, "data" to Gson().toJson(listBalances), "total_wealth" to total_wealth)
        }
    }

    private fun getNetWorthList()
    {
        if (isOnline())
        {
            loader.show()
            apiService.getWealthRequiredList(sessionManager.userId).enqueue(object : Callback<WealthRequiredListResponse>
                {
                    override fun onFailure(call: Call<WealthRequiredListResponse>, t: Throwable)
                    {
                        apiFailedToast()
                        loader.dismiss()
                    }

                    override fun onResponse(call: Call<WealthRequiredListResponse>, response: Response<WealthRequiredListResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.body()!!.success == 1)
                            {
                                total_wealth = response.body()!!.total_wealth

                                listBalances.addAll(response.body()!!.wealth_required)
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

    inner class AspirationAdapter(private val list: List<WealthRequiredListResponse.WealthRequired>) : RecyclerView.Adapter<AspirationAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.fin_plan_item_wealth_required, parent, false))
        }

        override fun getItemCount(): Int
        {
            return list.size
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            var getSet: WealthRequiredListResponse.WealthRequired = list[position]

            holder.itemView.tvYear.text = getSet.year
            holder.itemView.tvExisting.text = getPrice((getSet.existing))
            holder.itemView.tvExistingWealth.text = getPrice((getSet.exisiting_wealth))
            holder.itemView.tvFutureWealth.text = getPrice((getSet.future_wealth))
            holder.itemView.tvHumanValue.text = getPrice((getSet.human_value))
            holder.itemView.tvHealthRequired.text = getPrice((getSet.wealth_required))
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        {
            var tvYear: TextView = itemView.findViewById(R.id.tvYear)
            var tvExisting: TextView = itemView.findViewById(R.id.tvExisting)
            var tvExistingWealth: TextView = itemView.findViewById(R.id.tvExistingWealth)
            var tvFutureWealth: TextView = itemView.findViewById(R.id.tvFutureWealth)
            var tvHumanValue: TextView = itemView.findViewById(R.id.tvHumanValue)
            var tvHealthRequired: TextView = itemView.findViewById(R.id.tvHealthRequired)
        }

    }
}
