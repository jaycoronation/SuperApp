package com.application.alphacapital.superapp.finplan.activity.assetallocation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.alphacapital.superapp.finplan.model.AssetAllocationMicroListResponse
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.CAME_FROM
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.GRAPH_EXISTING_ASSET_ALLOCATION_MICRO
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.finplan.activity.FinPlanBaseActivity
import com.application.alphacapital.superapp.finplan.activity.FinPlanGraphActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fin_plan_activity_list_report.*
import kotlinx.android.synthetic.main.fin_plan_item_asset_allocation.view.*
import kotlinx.android.synthetic.main.fin_plan_item_futureinflow_report.view.tvAmount
import kotlinx.android.synthetic.main.fin_plan_item_networth.view.tvAssetsType
import kotlinx.android.synthetic.main.fin_plan_toolbar.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinPlanAssetAllocationMicroListActivity : FinPlanBaseActivity()
{

    private var listAspiration: MutableList<AssetAllocationMicroListResponse.AssetAllocationMicro.AssetAllocationMicroList> =
        mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fin_plan_activity_list_report)
        initViews()
        getNetWorthList()
    }

    private fun initViews()
    {
        tvTitle.text = "Asset Allocation(Micro)"
        ivBack.backNav()

        tvNoData.text = "Asset Allocation(Micro) not found!"
        rvAspiration.layoutManager = LinearLayoutManager(activity)

        ivGraph.setOnClickListener {
            startActivity<FinPlanGraphActivity>(CAME_FROM to GRAPH_EXISTING_ASSET_ALLOCATION_MICRO,
                "data" to Gson().toJson(listAspiration)
            )
        }
    }

    private fun getNetWorthList()
    {
        if (isOnline())
        {
            loader.show()
            apiService.getAssetAllocationMicro(sessionManager.userId)
                .enqueue(object : Callback<AssetAllocationMicroListResponse>
                {
                    override fun onFailure(call: Call<AssetAllocationMicroListResponse>,
                                           t: Throwable)
                    {
                        apiFailedToast()
                        loader.dismiss()
                    }

                    override fun onResponse(call: Call<AssetAllocationMicroListResponse>,
                                            response: Response<AssetAllocationMicroListResponse>)
                    {
                        if (response.isSuccessful)
                        {

                            if (response.body()!!.success == 1)
                            {
                                listAspiration.addAll(response.body()!!.asset_allocation_micro.list)
                                ivGraph.visibility =
                                    View.VISIBLE.takeIf { listAspiration.isNotEmpty() } ?: View.GONE
                                if (listAspiration.isNotEmpty())
                                {
                                    tvNoData.visibility = View.GONE
                                    rvAspiration.adapter = AspirationAdapter(listAspiration)

                                    cardAssetAllocationMicro.visibility = View.VISIBLE

                                    tvAmount.text =
                                        getPrice(response.body()!!.asset_allocation_micro.total.amount)
                                    tvAllocation.text =
                                        response.body()!!.asset_allocation_micro.total.allocation


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

    inner class AspirationAdapter(private val list: List<AssetAllocationMicroListResponse.AssetAllocationMicro.AssetAllocationMicroList>) : RecyclerView.Adapter<AspirationAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity)
                .inflate(R.layout.fin_plan_item_asset_allocation, parent, false)
            )
        }

        override fun getItemCount(): Int
        {
            return list.size
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            var getSet: AssetAllocationMicroListResponse.AssetAllocationMicro.AssetAllocationMicroList =
                list[position]

            holder.itemView.tvAmount.text = getPrice(getSet.amount)
            holder.itemView.tvAssetsType.text = getSet.asset_type
            holder.itemView.tvAllocation.text = getSet.allocation
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        {
            var tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
            var tvAssetsType: TextView = itemView.findViewById(R.id.tvAssetsType)
            var tvAllocation: TextView = itemView.findViewById(R.id.tvAllocation)
        }

    }
}
