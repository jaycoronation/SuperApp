package com.application.alphacapital.superapp.finplan.activity.assetallocation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alphafinancialplanning.model.AssetAllocationMacroListResponse
import com.application.alphacapital.superapp.finplan.utils.AppConstant
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.finplan.activity.FinPlanBaseActivity
import com.application.alphacapital.superapp.finplan.activity.FinPlanGraphActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fin_plan_activity_list_report.*
import kotlinx.android.synthetic.main.fin_plan_activity_list_report.view.tvAllocation
import kotlinx.android.synthetic.main.fin_plan_activity_list_report.view.tvAmount
import kotlinx.android.synthetic.main.fin_plan_item_asset_allocation_macro.view.*
import kotlinx.android.synthetic.main.fin_plan_toolbar.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinPlanAssetAllocationMacroListActivity : FinPlanBaseActivity()
{
    private var listAspiration: MutableList<AssetAllocationMacroListResponse.AssetAllocationMacro.AssetAllocationMacroList> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fin_plan_activity_list_report)
        initViews()
        getNetWorthList()
    }

    private fun initViews()
    {
        tvTitle.text = "Asset Allocation(Macro)"
        ivBack.backNav()

        tvNoData.text = "Asset Allocation(Macro) not found!"
        rvAspiration.layoutManager = LinearLayoutManager(activity)

        ivGraph.setOnClickListener {
            startActivity<FinPlanGraphActivity>(AppConstant.camefrom.CAME_FROM to AppConstant.camefrom.GRAPH_EXISTING_ASSET_ALLOCATION_MACRO, "data" to Gson().toJson(listAspiration))
        }
    }

    private fun getNetWorthList()
    {
        if (isOnline())
        {
            loader.show()
            apiService.getAssetAllocationMacro(sessionManager.userId).enqueue(object : Callback<AssetAllocationMacroListResponse>
                {
                    override fun onFailure(call: Call<AssetAllocationMacroListResponse>, t: Throwable)
                    {
                        apiFailedToast()
                        loader.dismiss()
                    }

                    override fun onResponse(call: Call<AssetAllocationMacroListResponse>, response: Response<AssetAllocationMacroListResponse>)
                    {
                        if (response.isSuccessful)
                        {

                            if (response.body()!!.success == 1)
                            {
                                listAspiration.addAll(response.body()!!.asset_allocation_macro.list)
                                ivGraph.visibility = View.VISIBLE.takeIf { listAspiration.isNotEmpty() } ?: View.GONE
                                if (listAspiration.isNotEmpty())
                                {
                                    tvNoData.visibility = View.GONE
                                    rvAspiration.adapter = AspirationAdapter(listAspiration)

                                    cardAssetAllocationMacro.visibility = View.VISIBLE

                                    tvAmountMacro.text = getPrice(response.body()!!.asset_allocation_macro.total.amount)
                                    tvAllocationMacro.text = response.body()!!.asset_allocation_macro.total.allocation
                                    tvReturn.text = response.body()!!.asset_allocation_macro.total.`return`
                                    tvRisk.text = response.body()!!.asset_allocation_macro.total.risk


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

    inner class AspirationAdapter(private val list: List<AssetAllocationMacroListResponse.AssetAllocationMacro.AssetAllocationMacroList>) : RecyclerView.Adapter<AspirationAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.fin_plan_item_asset_allocation_macro, parent, false))
        }

        override fun getItemCount(): Int
        {
            return list.size
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            var getSet: AssetAllocationMacroListResponse.AssetAllocationMacro.AssetAllocationMacroList = list[position]

            holder.itemView.tvAmount.text = getPrice(getSet.amount)
            holder.itemView.tvAssetsType.text = getSet.asset_class
            holder.itemView.tvAllocation.text = getSet.allocation/*holder.itemView.tvRisk.text = getSet.risk
            holder.itemView.tvReturn.text = getSet.`return`*/
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        {
            var tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
            var tvAssetsType: TextView = itemView.findViewById(R.id.tvAssetsType)
            var tvAllocation: TextView = itemView.findViewById(R.id.tvAllocation)/*var tvRisk:TextView = itemView.findViewById(R.id.tvRisk)
            var tvReturn:TextView = itemView.findViewById(R.id.tvReturn)*/
        }

    }
}
