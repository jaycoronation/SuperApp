package com.application.alphacapital.superapp.finplan.activity.aspirationfutureexpense

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.alphacapital.superapp.finplan.activity.FinPlanBaseActivity
import com.alphafinancialplanning.model.AspirationListReportResponse
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.finplan.utils.AppUtils
import kotlinx.android.synthetic.main.fin_plan_activity_list_report.*
import kotlinx.android.synthetic.main.fin_plan_item_aspiration.view.*
import kotlinx.android.synthetic.main.fin_plan_toolbar.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinPlanAspirationListReportActivity : FinPlanBaseActivity(), View.OnClickListener
{

    private var listAspiration: MutableList<AspirationListReportResponse.Aspirations.AspirationList> = mutableListOf()

    companion object
    {
        var handler: Handler = Handler()
    }

    private var shouldRedirect = true

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fin_plan_activity_list_report)
        initViews()
        getAspirationsList()

        handler = Handler {
            when (it.what)
            {
                1 ->
                {
                    shouldRedirect = false
                    getAspirationsList()
                }
            }
            false
        }
    }

    private fun initViews()
    {
        tvTitle.text = "Aspirations"
        ivBack.backNav()

        tvManage.visibility = View.VISIBLE
        tvManage.setOnClickListener(this)

        rvAspiration.layoutManager = LinearLayoutManager(activity)
    }

    override fun onClick(view: View?)
    {
        when (view?.id)
        {
            tvManage.id ->
            {
                startActivity<FinPlanAspirationFutureExpenseListActivity>()
                startActivityAnimation()
            }
        }
    }

    private fun getAspirationsList()
    {
        if (isOnline())
        {
            loader.show()
            apiService.getAspirationList(sessionManager.userId).enqueue(object : Callback<AspirationListReportResponse>
                {
                    override fun onFailure(call: Call<AspirationListReportResponse>, t: Throwable)
                    {
                        apiFailedToast()
                        loader.dismiss()
                    }

                    override fun onResponse(call: Call<AspirationListReportResponse>, response: Response<AspirationListReportResponse>)
                    {
                        if (response.isSuccessful)
                        {

                            if (response.body()!!.success == 1)
                            {
                                listAspiration.addAll(response.body()!!.aspirations.list)
                                if (listAspiration.isNotEmpty())
                                {
                                    tvNoData.visibility = View.GONE
                                    rvAspiration.adapter = AspirationAdapter(listAspiration)

                                    cardAspirationTotal.visibility = View.GONE
                                    tvTotalOutflow.text = AppUtils.convertToCommaSeperatedValue(response.body()!!.aspirations.total.total_outflow)
                                    tvInflationAdjustedOutflow.text = (response.body()!!.aspirations.total.inflation_adjusted_outflow)
                                    tvWealthRequiredToday.text = (response.body()!!.aspirations.total.wealth_required_today_total)
                                    tvVolatileComponent.text = (response.body()!!.aspirations.total.volatile_component)
                                    tvTargetReturn.text = (response.body()!!.aspirations.total.target_return)

                                    loader.dismiss()
                                }
                                else
                                {
                                    tvNoData.visibility = View.VISIBLE
                                    loader.dismiss()
                                    /*if (shouldRedirect)
                                    {
                                        startActivity<FinPlanAspirationFutureExpenseListActivity>()
                                        startActivityAnimation()
                                    }*/
                                }
                            }
                            else
                            {
                                loader.dismiss()
                                tvNoData.visibility = View.VISIBLE
                                /*if (shouldRedirect)
                                {
                                    startActivity<FinPlanAspirationFutureExpenseListActivity>()
                                    startActivityAnimation()
                                }*/
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

    inner class AspirationAdapter(private val list: List<AspirationListReportResponse.Aspirations.AspirationList>) : RecyclerView.Adapter<AspirationAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.fin_plan_item_aspiration, parent, false))
        }

        override fun getItemCount(): Int
        {
            return list.size
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            var getSet: AspirationListReportResponse.Aspirations.AspirationList = list[position]

            holder.itemView.tvAspirationType.text = getSet.aspiration_type
            holder.itemView.tvStartYear.text = getSet.start_year
            holder.itemView.tvEndYear.text = getSet.end_year
            holder.itemView.tvTotalOutflow.text = getPrice((getSet.total_outflow))
            holder.itemView.tvInflationAdjustedOutflow.text = getPrice((getSet.total_inflation_adjusted_expense))
            holder.itemView.tvWealthRequiredToday.text = getPrice((getSet.wealth_required_today_total))
            holder.itemView.tvVolatileComponent.text = getSet.volatile_component
            holder.itemView.tvTargetReturn.text = getSet.target_return

        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        {
            var tvAspiration: TextView = itemView.findViewById(R.id.tvAspirationType)
            var tvStartYear: TextView = itemView.findViewById(R.id.tvStartYear)
            var tvEndYear: TextView = itemView.findViewById(R.id.tvEndYear)
            var tvTotalOutflow: TextView = itemView.findViewById(R.id.tvTotalOutflow)
            var tvInflationAdjustedOutflow: TextView = itemView.findViewById(R.id.tvInflationAdjustedOutflow)
            var tvWealthRequiredToday: TextView = itemView.findViewById(R.id.tvWealthRequiredToday)
            var tvVolatileComponent: TextView = itemView.findViewById(R.id.tvVolatileComponent)
            var tvTargetReturn: TextView = itemView.findViewById(R.id.tvTargetReturn)
        }

    }
}
