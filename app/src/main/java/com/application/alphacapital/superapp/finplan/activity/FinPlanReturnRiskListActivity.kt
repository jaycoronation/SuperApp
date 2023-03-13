package com.application.alphacapital.superapp.finplan.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alphafinancialplanning.model.ReturnOfRiskListResponse
import com.application.alphacapital.superapp.finplan.utils.AppConstant
import com.application.alphacapital.superapp.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fin_plan_activity_list_report.*
import kotlinx.android.synthetic.main.fin_plan_item_return_of_risk.view.*
import kotlinx.android.synthetic.main.fin_plan_toolbar.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinPlanReturnRiskListActivity : FinPlanBaseActivity()
{

    private var listBalances: MutableList<ReturnOfRiskListResponse.ReturnOfRisk> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fin_plan_activity_list_report)
        initViews()
        getNetWorthList()
    }

    private fun initViews()
    {
        tvTitle.text = "Return Of Risk"
        ivBack.backNav()

        tvNoData.text = "Return Of Risk not found!"
        rvAspiration.layoutManager = LinearLayoutManager(activity)

        ivGraph.setOnClickListener {
            startActivity<FinPlanGraphActivity>(AppConstant.camefrom.CAME_FROM to AppConstant.camefrom.GRAPH_RANGE_RETURN_RISK, "data" to Gson().toJson(listBalances)
            )
        }
    }

    private fun getNetWorthList()
    {
        if (isOnline())
        {
            loader.show()
            apiService.getReturnOfRiskList(sessionManager.userId).enqueue(object : Callback<ReturnOfRiskListResponse>
                {
                    override fun onFailure(call: Call<ReturnOfRiskListResponse>, t: Throwable)
                    {
                        apiFailedToast()
                        loader.dismiss()
                    }

                    override fun onResponse(call: Call<ReturnOfRiskListResponse>, response: Response<ReturnOfRiskListResponse>)
                    {
                        if (response.isSuccessful)
                        {

                            if (response.body()!!.success == 1)
                            {
                                listBalances.clear()
                                listBalances.addAll(response.body()!!.return_of_risk)
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

    inner class AspirationAdapter(private val list: List<ReturnOfRiskListResponse.ReturnOfRisk>) : RecyclerView.Adapter<AspirationAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.fin_plan_item_return_of_risk, parent, false))
        }

        override fun getItemCount(): Int
        {
            return list.size
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            var getSet: ReturnOfRiskListResponse.ReturnOfRisk = list[position]

            holder.itemView.tvRangeOfReturn.text = getSet.range_of_return
            holder.itemView.tv1year.text = getSet.one_year
            holder.itemView.tv3year.text = getSet.three_year
            holder.itemView.tv5year.text = getSet.five_year

        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        {}

    }
}
