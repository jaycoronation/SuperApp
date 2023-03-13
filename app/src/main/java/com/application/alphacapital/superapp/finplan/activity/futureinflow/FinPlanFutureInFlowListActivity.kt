package com.application.alphacapital.superapp.finplan.activity.futureinflow

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.alphacapital.superapp.finplan.activity.FinPlanBaseActivity
import com.alphafinancialplanning.model.CommonResponse
import com.alphafinancialplanning.model.FutureInFlowListResponse
import com.application.alphacapital.superapp.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fin_plan_activity_list.*
import kotlinx.android.synthetic.main.fin_plan_item_existing_assets.view.ivDelete
import kotlinx.android.synthetic.main.fin_plan_item_existing_assets.view.ivEdit
import kotlinx.android.synthetic.main.fin_plan_item_futureinflow.view.*
import kotlinx.android.synthetic.main.fin_plan_toolbar.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.yesButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinPlanFutureInFlowListActivity : FinPlanBaseActivity(), View.OnClickListener
{

    private var listExistingLiabilities: MutableList<FutureInFlowListResponse.FutureInflow> =
        mutableListOf()

    private lateinit var adapter: FutureInFlowAdapter

    companion object
    {
        var handler = Handler()
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fin_plan_activity_list)
        initView()
        getFutureInFlowList()
        handler = Handler {
            when (it.what)
            {
                1 ->
                {
                    getFutureInFlowList()
                }
            }
            false
        }
    }

    override fun onClick(view: View?)
    {
        when (view?.id)
        {
            btnAdd.id ->
            {
                startActivity<FinPlanAddFutureInFlowActivity>()
                startActivityAnimation()
            }
        }
    }

    private fun initView()
    {
        tvTitle.text = "Future Inflow"
        ivBack.backNav()

        tvNoData.text =
            "Future Inflow not found!\nClick on add button to add future inflow(Other than assets income)."

        btnAdd.setOnClickListener(this)

        rvExistingAssets.layoutManager = LinearLayoutManager(activity)
    }

    private fun getFutureInFlowList()
    {
        if (isOnline())
        {
            loader.show()
            apiService.getFutureInFlow(sessionManager.userId)
                .enqueue(object : Callback<FutureInFlowListResponse>
                {
                    override fun onFailure(call: Call<FutureInFlowListResponse>, t: Throwable)
                    {
                        apiFailedToast()
                        loader.dismiss()
                    }

                    override fun onResponse(call: Call<FutureInFlowListResponse>,
                                            response: Response<FutureInFlowListResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            loader.dismiss()
                            if (response.body()!!.success == 1)
                            {
                                tvNoData.visibility = View.GONE
                                rvExistingAssets.visibility = View.VISIBLE
                                listExistingLiabilities.clear()
                                listExistingLiabilities.addAll(response.body()!!.future_inflows)
                                adapter = FutureInFlowAdapter(listExistingLiabilities)
                                rvExistingAssets.adapter = adapter
                            }
                            else
                            {
                                tvNoData.visibility = View.VISIBLE
                                rvExistingAssets.visibility = View.GONE
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
            finish()
            finishActivityAnimation()
        }
    }

    inner class FutureInFlowAdapter(private val list: MutableList<FutureInFlowListResponse.FutureInflow>) : RecyclerView.Adapter<FutureInFlowAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity)
                .inflate(R.layout.fin_plan_item_futureinflow, parent, false)
            )
        }

        override fun getItemCount(): Int
        {
            return list.size
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int)
        {
            var getSet: FutureInFlowListResponse.FutureInflow = list[position]

            holder.itemView.tvSource.text = getSet.source
            holder.itemView.tvAmount.text = getPrice(getSet.amount)
            holder.itemView.tvExpectedGrowth.text = getSet.expected_growth
            holder.itemView.tvStartYear.text = getSet.start_year
            holder.itemView.tvEndYear.text = getSet.end_year

            holder.itemView.ivEdit.setOnClickListener {
                startActivity<FinPlanAddFutureInFlowActivity>("data" to Gson().toJson(getSet),
                    "isForEdit" to true
                )
                startActivityAnimation()
            }
            holder.itemView.ivDelete.setOnClickListener {
                alert("Are you sure you want to delete this entry?", "Delete?") {
                    yesButton {
                        if (isOnline())
                        {
                            loader.show()
                            apiService.deleteFutureInFlow(getSet.future_inflow_id)
                                .enqueue(object : Callback<CommonResponse>
                                {
                                    override fun onFailure(call: Call<CommonResponse>, t: Throwable)
                                    {
                                        apiFailedToast()
                                        loader.dismiss()
                                    }

                                    override fun onResponse(call: Call<CommonResponse>,
                                                            response: Response<CommonResponse>)
                                    {
                                        if (response.isSuccessful)
                                        {
                                            loader.dismiss()
                                            if (response.body()!!.success == 1)
                                            {
                                                listExistingLiabilities.removeAt(position)
                                                adapter.let {
                                                    it.notifyDataSetChanged()
                                                }
                                                tvNoData.visibility =
                                                    View.VISIBLE.takeIf { listExistingLiabilities.isEmpty() }
                                                        ?: View.GONE
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
                        }
                    }
                    noButton {}
                }.show()
            }

            holder.itemView.setOnClickListener {

            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        {
            var tvSource: TextView = itemView.findViewById(R.id.tvSource)
            var tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
            var tvExpectedGrowth: TextView = itemView.findViewById(R.id.tvExpectedGrowth)
            var tvStartYear: TextView = itemView.findViewById(R.id.tvStartYear)
            var tvEndYear: TextView = itemView.findViewById(R.id.tvEndYear)
            var ivEdit: ImageView = itemView.findViewById(R.id.ivEdit)
            var ivDelete: ImageView = itemView.findViewById(R.id.ivDelete)
        }

    }
}
