package com.application.alphacapital.superapp.finplan.activity.varianceanalysis

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.alphacapital.superapp.finplan.activity.FinPlanBaseActivity
import com.application.alphacapital.superapp.finplan.activity.FinPlanGraphActivity
import com.alphafinancialplanning.model.VarianceAnalysisMacroListResponse
import com.application.alphacapital.superapp.finplan.utils.AppConstant
import com.application.alphacapital.superapp.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fin_plan_activity_list_report.*
import kotlinx.android.synthetic.main.fin_plan_item_variance_analysis_macro.view.*
import kotlinx.android.synthetic.main.fin_plan_toolbar.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinPlanVarianceAnalysisMacroStrategicListActivity : FinPlanBaseActivity()
{

    private var listAspiration: MutableList<VarianceAnalysisMacroListResponse.VarianceAnalysisMacro.VarianceAnalysis> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fin_plan_activity_list_report)
        initViews()
        getNetWorthList()
    }

    private fun initViews()
    {
        tvTitle.text = "Variance Analysis(Macro-Strategic)"
        ivBack.backNav()

        tvNoData.text = "Variance Analysis(Macro-Strategic) not found!"
        rvAspiration.layoutManager = LinearLayoutManager(activity)

        ivGraph.setOnClickListener {
            startActivity<FinPlanGraphActivity>(AppConstant.camefrom.CAME_FROM to AppConstant.camefrom.GRAPH_VARIANCE_MACRO_STRATEGIC, "data" to Gson().toJson(listAspiration))
        }

    }

    private fun getNetWorthList()
    {
        if (isOnline())
        {
            loader.show()
            apiService.getVarianceAnalysisMacroStrategic(sessionManager.userId).enqueue(object : Callback<VarianceAnalysisMacroListResponse>
                {
                    override fun onFailure(call: Call<VarianceAnalysisMacroListResponse>, t: Throwable)
                    {
                        apiFailedToast()
                        loader.dismiss()
                    }

                    override fun onResponse(call: Call<VarianceAnalysisMacroListResponse>, response: Response<VarianceAnalysisMacroListResponse>)
                    {
                        if (response.isSuccessful)
                        {

                            if (response.body()!!.success == 1)
                            {
                                listAspiration.addAll(response.body()!!.variance_analysis_macro.list) //ivGraph.visibility = View.VISIBLE.takeIf { listAspiration.isNotEmpty() } ?: View.GONE
                                if (listAspiration.isNotEmpty())
                                {
                                    tvNoData.visibility = View.GONE
                                    rvAspiration.adapter = AspirationAdapter(listAspiration)

                                    cardVarianceAnalysisMacro.visibility = View.VISIBLE

                                    tvAssetTypeVarianceAnalysisMacro.text = response.body()!!.variance_analysis_macro.total.asset_class
                                    tvVarianceAnalysisMacroRecommendedAllocation.text = response.body()!!.variance_analysis_macro.total.recommended_allocation
                                    tvVarianceAnalysisMacroExistingAllocation.text = response.body()!!.variance_analysis_macro.total.existing_allocation
                                    tvVarianceAnalysisMacroVariance.text = response.body()!!.variance_analysis_macro.total.variance

                                    /*tvRiskVarianceAnalysisMacro.text = response.body()!!.variance_analysis_macro.total.risk
                                    tvReturnVarianceAnalysisMacro.text = response.body()!!.variance_analysis_macro.total.`return`*/
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

    inner class AspirationAdapter(private val list: List<VarianceAnalysisMacroListResponse.VarianceAnalysisMacro.VarianceAnalysis>) : RecyclerView.Adapter<AspirationAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.fin_plan_item_variance_analysis_macro, parent, false))
        }

        override fun getItemCount(): Int
        {
            return list.size
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            var getSet: VarianceAnalysisMacroListResponse.VarianceAnalysisMacro.VarianceAnalysis = list[position]

            holder.itemView.tvAssetsType.text = getSet.asset_class
            holder.itemView.tvExistingAllocation.text = getSet.existing_allocation
            holder.itemView.tvRecommendedAllocation.text = getSet.recommended_allocation
            holder.itemView.tvVariance.text = getSet.variance
            holder.itemView.tvReturn.text = getSet.`return`
            holder.itemView.tvRisk.text = getSet.risk
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        {
            var tvExistingAllocation: TextView = itemView.findViewById(R.id.tvExistingAllocation)
            var tvAssetsType: TextView = itemView.findViewById(R.id.tvAssetsType)
            var tvRecommendedAllocation: TextView = itemView.findViewById(R.id.tvRecommendedAllocation)
            var tvVariance: TextView = itemView.findViewById(R.id.tvVariance)
            var tvReturn: TextView = itemView.findViewById(R.id.tvReturn)
            var tvRisk: TextView = itemView.findViewById(R.id.tvRisk)
        }

    }
}
