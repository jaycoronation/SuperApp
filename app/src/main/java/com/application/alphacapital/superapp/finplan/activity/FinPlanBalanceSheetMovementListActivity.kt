package com.application.alphacapital.superapp.finplan.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alphafinancialplanning.model.BalanceSheetMovementListResponse
import com.application.alphacapital.superapp.finplan.utils.AppConstant
import com.application.alphacapital.superapp.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fin_plan_activity_list_report.*
import kotlinx.android.synthetic.main.fin_plan_item_balancesheet_movement.view.*
import kotlinx.android.synthetic.main.fin_plan_toolbar.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinPlanBalanceSheetMovementListActivity : FinPlanBaseActivity()
{

    private var listBalances: MutableList<BalanceSheetMovementListResponse.BalanceSheetMovement> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fin_plan_activity_list_report)
        initViews()
        getNetWorthList()
    }

    private fun initViews()
    {
        tvTitle.text = "Balance Sheet Movement"
        ivBack.backNav()

        tvNoData.text = "Balance Sheet data not found!"
        rvAspiration.layoutManager = LinearLayoutManager(activity)

        ivGraph.setOnClickListener {
            startActivity<FinPlanGraphActivity>(AppConstant.camefrom.CAME_FROM to AppConstant.camefrom.GRAPH_BALANCES_SHEET, "data" to Gson().toJson(listBalances))
        }
    }

    private fun getNetWorthList()
    {
        if (isOnline())
        {
            loader.show()
            apiService.getBalanceSheetMovement(sessionManager.userId).enqueue(object : Callback<BalanceSheetMovementListResponse>
                {
                    override fun onFailure(call: Call<BalanceSheetMovementListResponse>, t: Throwable)
                    {
                        apiFailedToast()
                        loader.dismiss()
                    }

                    override fun onResponse(call: Call<BalanceSheetMovementListResponse>, response: Response<BalanceSheetMovementListResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.body()!!.success == 1)
                            {
                                listBalances.addAll(response.body()!!.balance_sheet_movement)
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

    inner class AspirationAdapter(private val list: List<BalanceSheetMovementListResponse.BalanceSheetMovement>) : RecyclerView.Adapter<AspirationAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.fin_plan_item_balancesheet_movement, parent, false))
        }

        override fun getItemCount(): Int
        {
            return list.size
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            var getSet: BalanceSheetMovementListResponse.BalanceSheetMovement = list[position]

            holder.itemView.tvYear.text = getSet.year
            holder.itemView.tvOpeningBalance.text = getPrice(getSet.opening_balance)
            holder.itemView.tvClosingBalance.text = getPrice(getSet.closing_balance)
            holder.itemView.tvOutflow.text = getPrice(getSet.outflow)
            holder.itemView.tvInflow.text = getPrice(getSet.inflow)
            holder.itemView.tvGrowth.text = getPrice(getSet.growth)
            holder.itemView.tvClosingPV.text = getPrice(getSet.closing_pv)
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        {
            var tvYear: TextView = itemView.findViewById(R.id.tvYear)
            var tvOpeningBalance: TextView = itemView.findViewById(R.id.tvOpeningBalance)
            var tvClosingBalance: TextView = itemView.findViewById(R.id.tvClosingBalance)
            var tvOutflow: TextView = itemView.findViewById(R.id.tvOutflow)
            var tvInflow: TextView = itemView.findViewById(R.id.tvInflow)
            var tvGrowth: TextView = itemView.findViewById(R.id.tvGrowth)
            var tvClosingPV: TextView = itemView.findViewById(R.id.tvClosingPV)
        }

    }
}
