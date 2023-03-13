package com.application.alphacapital.superapp.finplan.activity.existingassets

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
import com.application.alphacapital.superapp.finplan.model.ExistingAssetsListResponse
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.pms.beans.AssetsAllocationTemp
import com.application.alphacapital.superapp.pms.beans.NetworthTempData
import com.application.alphacapital.superapp.pms.network.PortfolioApiClient
import com.application.alphacapital.superapp.pms.network.PortfolioApiInterface
import com.application.alphacapital.superapp.pms.utils.ApiUtils
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
import java.lang.Exception
import java.util.ArrayList

class FinPlanExistingAssetsListActivity : FinPlanBaseActivity(), View.OnClickListener
{

    private var listExistingAssets: MutableList<ExistingAssetsListResponse.ExistingAsset> = mutableListOf()

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
        getExistingAssets()
        handler = Handler {
            when (it.what)
            {
                1 ->
                {
                    getExistingAssets()
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
                startActivity<FinPlanAddExistingAssetsActivity>()
                startActivityAnimation()
            }
        }
    }

    private fun initView()
    {
        tvTitle.text = "Existing Assets"
        ivBack.backNav()

        tvNoData.text = "Existing Assets not found!\nClick on add button to add assets."

        btnAdd.setOnClickListener(this)

        rvExistingAssets.layoutManager = LinearLayoutManager(activity)
    }

    private fun getExistingAssets()
    {
        if (isOnline())
        {
            loader.show()
            apiService.getExistingAssets(sessionManager.userId)
                .enqueue(object : Callback<ExistingAssetsListResponse>
                {
                    override fun onFailure(call: Call<ExistingAssetsListResponse>, t: Throwable)
                    {
                        apiFailedToast()
                        loader.dismiss()
                    }

                    override fun onResponse(call: Call<ExistingAssetsListResponse>,
                                            response: Response<ExistingAssetsListResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            loader.dismiss()
                            if (response.body()!!.success == 1)
                            {
                                tvNoData.visibility = View.GONE
                                rvExistingAssets.visibility = View.VISIBLE
                                listExistingAssets.clear()
                                listExistingAssets.addAll(response.body()!!.existing_assets)
                                adapter = ExistingAssetsAdapter(listExistingAssets)
                                rvExistingAssets.adapter = adapter
                            }
                            else
                            {
                                tvNoData.visibility = View.VISIBLE
                                rvExistingAssets.visibility = View.GONE
                                startActivity<FinPlanAddExistingAssetsActivity>()
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

    inner class ExistingAssetsAdapter(private val list: MutableList<ExistingAssetsListResponse.ExistingAsset>) : RecyclerView.Adapter<ExistingAssetsAdapter.ViewHolder>()
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

        override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int)
        {
            var getSet: ExistingAssetsListResponse.ExistingAsset = list[position]
            holder.itemView.tvInvestmentType.text = getSet.investment_type
            holder.itemView.tvAssetsType.text = getSet.asset_type
            holder.itemView.tvCurrentValue.text = getPrice(getSet.current_value)

            if (getSet.can_delete == 1)
            {
                visible(holder.itemView.ivEdit)
                visible(holder.itemView.ivDelete)
            }
            else
            {
                gone(holder.itemView.ivEdit)
                gone(holder.itemView.ivDelete)
            }

            holder.itemView.ivEdit.setOnClickListener {
                startActivity<FinPlanAddExistingAssetsActivity>("data" to Gson().toJson(getSet), "isForEdit" to true)
                startActivityAnimation()
            }
            holder.itemView.ivDelete.setOnClickListener {
                alert("Are you sure you want to delete this entry?", "Delete?") {
                    yesButton {
                        if (isOnline())
                        {
                            loader.show()
                            apiService.deleteExistingAssets(getSet.existing_assets_id)
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
                                                listExistingAssets.removeAt(position)
                                                adapter.let {
                                                    it.notifyDataSetChanged()
                                                }
                                                tvNoData.visibility =
                                                    View.VISIBLE.takeIf { listExistingAssets.isEmpty() }
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
