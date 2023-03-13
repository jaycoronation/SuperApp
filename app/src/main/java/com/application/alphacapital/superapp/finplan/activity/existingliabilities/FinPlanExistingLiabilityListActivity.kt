package com.application.alphacapital.superapp.finplan.activity.existingliabilities

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
import com.alphafinancialplanning.model.ExistingLiabilitiesListResponse
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.finplan.utils.AppUtils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fin_plan_activity_list.*
import kotlinx.android.synthetic.main.fin_plan_item_existing_assets.view.*
import kotlinx.android.synthetic.main.fin_plan_toolbar.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.yesButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinPlanExistingLiabilityListActivity : FinPlanBaseActivity(), View.OnClickListener
{
    private var listExistingLiabilities: MutableList<ExistingLiabilitiesListResponse.ExistingLiability> =
        mutableListOf()

    private lateinit var adapter: ExistingAssetsAdapter

    companion object
    {
        var handler = Handler()
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fin_plan_activity_list)
        initView()
        getExistingLiabilitiesList()
        handler = Handler {
            when (it.what)
            {
                1 ->
                {
                    getExistingLiabilitiesList()
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
                startActivity<FinPlanAddExistingLiabilitiesActivity>()
                startActivityAnimation()
            }
        }
    }


    private fun initView()
    {
        tvTitle.text = "Existing Liabilities"
        ivBack.backNav()

        tvNoData.text = "Existing Liabilities not found!\nClick on add button to add liabilities."

        btnAdd.setOnClickListener(this)

        rvExistingAssets.layoutManager = LinearLayoutManager(activity)
    }

    private fun getExistingLiabilitiesList()
    {
        if (isOnline())
        {
            loader.show()
            apiService.getExistingLiabilities(sessionManager.userId)
                .enqueue(object : Callback<ExistingLiabilitiesListResponse>
                {
                    override fun onFailure(call: Call<ExistingLiabilitiesListResponse>,
                                           t: Throwable)
                    {
                        apiFailedToast()
                        loader.dismiss()
                    }

                    override fun onResponse(call: Call<ExistingLiabilitiesListResponse>, response: Response<ExistingLiabilitiesListResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            loader.dismiss()
                            if (response.body()!!.success == 1)
                            {
                                tvNoData.visibility = View.GONE
                                rvExistingAssets.visibility = View.VISIBLE
                                listExistingLiabilities.clear()
                                listExistingLiabilities.addAll(response.body()!!.existing_liabilities)
                                adapter = ExistingAssetsAdapter(listExistingLiabilities)
                                rvExistingAssets.adapter = adapter
                            }
                            else
                            {
                                tvNoData.visibility = View.VISIBLE
                                rvExistingAssets.visibility = View.GONE
                                startActivity<FinPlanAddExistingLiabilitiesActivity>()
                                startActivityAnimation()
                                finish()
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

    inner class ExistingAssetsAdapter(private val list: MutableList<ExistingLiabilitiesListResponse.ExistingLiability>) : RecyclerView.Adapter<ExistingAssetsAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity)
                .inflate(R.layout.fin_plan_item_existing_assets, parent, false)
            )
        }

        override fun getItemCount(): Int
        {
            return list.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            var getSet: ExistingLiabilitiesListResponse.ExistingLiability = list[position]
            holder.itemView.tvInvestmentType.text = getSet.liability_type
            holder.tvAssetsType.text = getSet.asset_type
            holder.tvCurrentValue.text = getPrice((getSet.current_value))

            holder.itemView.ivEdit.setOnClickListener {
                startActivity<FinPlanAddExistingLiabilitiesActivity>("data" to Gson().toJson(getSet),
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
                            apiService.deleteExistingLiabilities(getSet.existing_liability_id)
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
            var tvInvestmentType: TextView = itemView.findViewById(R.id.tvInvestmentType)
            var tvCurrentValue: TextView = itemView.findViewById(R.id.tvCurrentValue)
            var tvAssetsType: TextView = itemView.findViewById(R.id.tvAssetsType)
            var ivEdit: ImageView = itemView.findViewById(R.id.ivEdit)
            var ivDelete: ImageView = itemView.findViewById(R.id.ivDelete)
        }

    }
}
