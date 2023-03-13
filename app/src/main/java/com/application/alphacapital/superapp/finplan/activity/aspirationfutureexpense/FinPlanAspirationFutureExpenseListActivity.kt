package com.application.alphacapital.superapp.finplan.activity.aspirationfutureexpense

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
import com.alphafinancialplanning.model.AspirationFutureExpenseListResponse
import com.alphafinancialplanning.model.CommonResponse
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.finplan.utils.AppUtils
import com.application.alphacapital.superapp.pms.beans.AssetsAllocationTemp
import com.application.alphacapital.superapp.pms.beans.NetworthTempData
import com.application.alphacapital.superapp.pms.network.PortfolioApiClient
import com.application.alphacapital.superapp.pms.network.PortfolioApiInterface
import com.application.alphacapital.superapp.pms.utils.ApiUtils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fin_plan_activity_list.*
import kotlinx.android.synthetic.main.fin_plan_item_aspirationfuture_expense.view.*
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

class FinPlanAspirationFutureExpenseListActivity : FinPlanBaseActivity(), View.OnClickListener
{
    private var listExistingAssets: MutableList<AspirationFutureExpenseListResponse.AspirationFutureExpense> = mutableListOf()

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
        getAspirationFutureExpensesList()
        handler = Handler {
            when (it.what)
            {
                1 ->
                {
                    getAspirationFutureExpensesList()
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
                startActivity<FinPlanAddAspirationFutureExpenseActivity>()
                startActivityAnimation()
            }
        }
    }

    private fun initView()
    {
        tvTitle.text = "Aspiration/Future Expense"
        ivBack.backNav()

        tvNoData.text = "Future Expenses not found!\nClick on add button to add expenses."

        btnAdd.setOnClickListener(this)

        rvExistingAssets.layoutManager = LinearLayoutManager(activity)
    }

    private fun getAspirationFutureExpensesList()
    {
        if (isOnline())
        {
            loader.show()
            apiService.getAspirationFutureExpense(sessionManager.userId).enqueue(object : Callback<AspirationFutureExpenseListResponse>
                {
                    override fun onFailure(call: Call<AspirationFutureExpenseListResponse>, t: Throwable)
                    {
                        apiFailedToast()
                        loader.dismiss()
                    }

                    override fun onResponse(call: Call<AspirationFutureExpenseListResponse>, response: Response<AspirationFutureExpenseListResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            loader.dismiss()
                            if (response.body()!!.success == 1)
                            {
                                tvNoData.visibility = View.GONE
                                rvExistingAssets.visibility = View.VISIBLE
                                listExistingAssets.clear()
                                listExistingAssets.addAll(response.body()!!.aspiration_future_expenses)
                                adapter = ExistingAssetsAdapter(listExistingAssets)
                                rvExistingAssets.adapter = adapter
                            }
                            else
                            {
                                tvNoData.visibility = View.VISIBLE
                                rvExistingAssets.visibility = View.GONE
                                startActivity<FinPlanAddAspirationFutureExpenseActivity>()
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

    inner class ExistingAssetsAdapter(private val list: MutableList<AspirationFutureExpenseListResponse.AspirationFutureExpense>) : RecyclerView.Adapter<ExistingAssetsAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.fin_plan_item_aspirationfuture_expense, parent, false))
        }

        override fun getItemCount(): Int
        {
            return list.size
        }

        override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int)
        {
            var getSet: AspirationFutureExpenseListResponse.AspirationFutureExpense = list[position]
            holder.itemView.tvAspirationType.text = getSet.aspiration_type
            holder.itemView.tvAmount.text = getPrice(getSet.amount)
            holder.itemView.tvStartYear.text = getSet.start_year
            holder.itemView.tvEndYear.text = getSet.end_year
            holder.itemView.tvPeriodicity.text = getSet.periodicity

            holder.itemView.ivEdit.setOnClickListener {
                startActivity<FinPlanAddAspirationFutureExpenseActivity>("data" to Gson().toJson(getSet), "isForEdit" to true)
                startActivityAnimation()
            }
            holder.itemView.ivDelete.setOnClickListener {
                alert("Are you sure you want to delete this entry?", "Delete?") {
                    yesButton {
                        if (isOnline())
                        {
                            loader.show()
                            apiService.deleteAspireFutureExpense(getSet.aspiration_id).enqueue(object : Callback<CommonResponse>
                                {
                                    override fun onFailure(call: Call<CommonResponse>, t: Throwable)
                                    {
                                        apiFailedToast()
                                        loader.dismiss()
                                    }

                                    override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>)
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
                                                tvNoData.visibility = View.VISIBLE.takeIf { listExistingAssets.isEmpty() } ?: View.GONE
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
            var tvAspirationType: TextView = itemView.findViewById(R.id.tvAspirationType)
            var tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
            var tvPeriodicity: TextView = itemView.findViewById(R.id.tvPeriodicity)
            var tvStartYear: TextView = itemView.findViewById(R.id.tvStartYear)
            var tvEndYear: TextView = itemView.findViewById(R.id.tvEndYear)
            var ivEdit: ImageView = itemView.findViewById(R.id.ivEdit)
            var ivDelete: ImageView = itemView.findViewById(R.id.ivDelete)
        }

    }
}
