package com.application.alphacapital.superapp.finplan.activity.assetallocation

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.alphacapital.superapp.finplan.model.RecommendedAssetAllocationListResponse
import com.application.alphacapital.superapp.finplan.utils.AppConstant
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.finplan.activity.FinPlanBaseActivity
import com.application.alphacapital.superapp.finplan.activity.FinPlanGraphActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fin_plan_activity_list_report.*
import kotlinx.android.synthetic.main.fin_plan_item_recommended_asset_allocation.view.*
import kotlinx.android.synthetic.main.fin_plan_toolbar.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinPlanRecommendedAssetAllocationListActivity : FinPlanBaseActivity()
{
    companion object
    {
        var handler = Handler()
        var listAspiration: MutableList<RecommendedAssetAllocationListResponse.RecommendedAssetAllocation.AssetList> = mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fin_plan_activity_list_report)
        initViews()
        getNetWorthList()
        handler = Handler {
            when (it.what)
            {
                1 ->
                {
                    getNetWorthList()
                }
            }
            false
        }
    }

    private fun initViews()
    {
        tvTitle.text = "Recommended Asset Allocation"
        ivBack.backNav()

        tvManage.visibility = View.VISIBLE

        tvNoData.text = "Recommended Asset Allocation not found!"
        rvAspiration.layoutManager = LinearLayoutManager(activity)

        ivGraph.setOnClickListener {
            startActivity<FinPlanGraphActivity>(AppConstant.camefrom.CAME_FROM to AppConstant.camefrom.GRAPH_RECOMMENDED_ASSET_ALLOCATION, "data" to Gson().toJson(listAspiration))
        }


        tvManage.setOnClickListener {
            if (listAspiration.isEmpty())
            {
                startActivity<FinPlanAddRecommendedAssetAllocationActivity>("isForEdit" to false)
                startActivityAnimation()
            }
            else
            {
                startActivity<FinPlanAddRecommendedAssetAllocationActivity>("isForEdit" to true)
                startActivityAnimation()
            }
        }
    }

    private fun getNetWorthList()
    {
        if (isOnline())
        {
            loader.show()
            apiService.getRecommendedAssetAllocation(sessionManager.userId).enqueue(object : Callback<RecommendedAssetAllocationListResponse>
                {
                    override fun onFailure(call: Call<RecommendedAssetAllocationListResponse>, t: Throwable)
                    {
                        apiFailedToast()
                        loader.dismiss()
                    }

                    override fun onResponse(call: Call<RecommendedAssetAllocationListResponse>, response: Response<RecommendedAssetAllocationListResponse>)
                    {
                        if (response.isSuccessful)
                        {

                            if (response.body()!!.success == 1)
                            {
                                listAspiration.clear()
                                listAspiration.addAll(response.body()!!.recommended_asset_allocation.list)
                                //ivGraph.visibility = View.VISIBLE.takeIf { listAspiration.isNotEmpty() } ?: View.GONE
                                if (listAspiration.isNotEmpty())
                                {
                                    tvNoData.visibility = View.GONE
                                    rvAspiration.adapter = AspirationAdapter(listAspiration)

                                    cardRecommendedAssetAllocation.visibility = View.VISIBLE

                                    tvAssetClassRecommended.text = response.body()!!.recommended_asset_allocation.total.asset_class
                                    tvAmountRecommended.text = getPrice(response.body()!!.recommended_asset_allocation.total.amount)
                                    tvAllocationRecommended.text = response.body()!!.recommended_asset_allocation.total.allocation
                                    tvReturnExpectationRecommended.text = response.body()!!.recommended_asset_allocation.total.`return`


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

    inner class AspirationAdapter(private val list: List<RecommendedAssetAllocationListResponse.RecommendedAssetAllocation.AssetList>) : RecyclerView.Adapter<AspirationAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.fin_plan_item_recommended_asset_allocation, parent, false))
        }

        override fun getItemCount(): Int
        {
            return list.size
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            var getSet: RecommendedAssetAllocationListResponse.RecommendedAssetAllocation.AssetList = list[position]

            holder.itemView.tvAmount.text = getPrice(getSet.amount)
            holder.itemView.tvAssetsType.text = getSet.asset_class
            holder.itemView.tvAllocation.text = getSet.allocation
            holder.itemView.tvReturnExpectation.text = getSet.return_expectation
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        {
            var tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
            var tvAssetsType: TextView = itemView.findViewById(R.id.tvAssetsType)
            var tvAllocation: TextView = itemView.findViewById(R.id.tvAllocation)
            var tvReturnExpectation: TextView = itemView.findViewById(R.id.tvReturnExpectation)
        }

    }
}
