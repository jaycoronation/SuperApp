package com.application.alphacapital.superapp.finplan.activity

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.alphafinancialplanning.model.ReturnOfRiskListResponse
import com.alphafinancialplanning.model.RiskProfileAllocationResponse
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.ActivityFinPlanRiskProfileBinding
import com.application.alphacapital.superapp.pms.utils.AppUtils
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.android.synthetic.main.fin_plan_item_return_of_risk.view.tvRangeOfReturn
import kotlinx.android.synthetic.main.fin_plan_return_of_risk.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinPlanRiskProfileActivity : FinPlanBaseActivity()
{
    private lateinit var binding: ActivityFinPlanRiskProfileBinding
    private var listBalances: MutableList<RiskProfileAllocationResponse.RiskProfileAllocation> = mutableListOf()
    private var listReturnofRisk: MutableList<ReturnOfRiskListResponse.ReturnOfRisk> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(activity,R.layout.activity_fin_plan_risk_profile)
        initView()
        onClick()
    }

    private fun initView()
    {
        getNetWorthList()
        binding.toolbar.tvTitle.visibility= View.VISIBLE
        binding.toolbar.tvTitle.text = "Risk Profile"
    }

    private fun onClick()
    {
        binding.toolbar.ivBack.backNav()
    }

    private fun getNetWorthList()
    {
        if (isOnline())
        {
            loader.show()
            apiService.getRiskProfileAllocationList(sessionManager.userId).enqueue(object : Callback<RiskProfileAllocationResponse>
            {
                override fun onFailure(call: Call<RiskProfileAllocationResponse>, t: Throwable)
                {
                    apiFailedToast()
                    loader.dismiss()
                    getReturnOfRisk()

                }

                override fun onResponse(call: Call<RiskProfileAllocationResponse>, response: Response<RiskProfileAllocationResponse>)
                {
                    if (response.isSuccessful)
                    {
                        if (response.body()!!.success == 1)
                        {
                            listBalances.clear()
                            listBalances.addAll(response.body()!!.risk_profile_allocation)
                            if (listBalances.isNotEmpty())
                            {
                                binding.rvRiskProfileAllocation.adapter = ApplicantsAdapter(listBalances)
                            }
                        }
                    }
                    else
                    {
                        apiFailedToast()
                        loader.dismiss()
                    }
                    getReturnOfRisk()
                }

            })
        }
        else
        {
            noInternetToast()
        }
    }

    inner class ApplicantsAdapter(var listItem: MutableList<RiskProfileAllocationResponse.RiskProfileAllocation>) : RecyclerView.Adapter<ApplicantsAdapter.ViewHolder?>()
    {
        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder
        {
            val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.portfolio_row_view_applicant_temp, viewGroup, false)
            return ViewHolder(v)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            try
            {
                val getSet = listItem[position]
                if (position == 0)
                {
                    holder.txtAssestType.setTypeface(AppUtils.getTypefaceRegular(activity))
                    holder.txtAssestType.setTextColor(ContextCompat.getColor(activity, R.color.black))
                    holder.txtCurrent.setTypeface(AppUtils.getTypefaceRegular(activity))
                    holder.txtCurrent.setTextColor(ContextCompat.getColor(activity, R.color.black))
                    holder.txtXIRR.setTypeface(AppUtils.getTypefaceRegular(activity))
                    holder.txtXIRR.setTextColor(ContextCompat.getColor(activity, R.color.black))
                }
                else if (position == listItem.size - 1)
                {
                    holder.txtAssestType.setTypeface(AppUtils.getTypefaceBold(activity))
                    holder.txtAssestType.setTextColor(ContextCompat.getColor(activity, R.color.black))
                    holder.txtCurrent.setTypeface(AppUtils.getTypefaceBold(activity))
                    holder.txtCurrent.setTextColor(ContextCompat.getColor(activity, R.color.black))
                    holder.txtXIRR.setTypeface(AppUtils.getTypefaceBold(activity))
                    holder.txtXIRR.setTextColor(ContextCompat.getColor(activity, R.color.black))
                }
                else
                {
                    holder.txtAssestType.setTypeface(AppUtils.getTypefaceRegular(activity))
                    holder.txtAssestType.setTextColor(ContextCompat.getColor(activity, R.color.black))
                    holder.txtCurrent.setTypeface(AppUtils.getTypefaceRegular(activity))
                    holder.txtCurrent.setTextColor(ContextCompat.getColor(activity, R.color.black))
                    holder.txtXIRR.setTypeface(AppUtils.getTypefaceRegular(activity))
                    holder.txtXIRR.setTextColor(ContextCompat.getColor(activity, R.color.black))
                }
                holder.txtAssestType.text = AppUtils.toDisplayCase(getSet.asset_class)
                holder.txtCurrent.text = getSet.allocation
                holder.txtXIRR.text = getSet.expected_return

                if (position == listItem.size - 1)
                {
                    holder.viewLineBottom.visibility = View.GONE
                }
                else
                {
                    holder.viewLineBottom.visibility = View.VISIBLE
                }
            }
            catch (e: Resources.NotFoundException)
            {
                e.printStackTrace()
            }
        }

        override fun getItemCount(): Int
        {
            return listItem.size
        }

        inner class ViewHolder internal constructor(convertView: View) : RecyclerView.ViewHolder(convertView)
        {
            val txtAssestType: TextView = convertView.findViewById(R.id.txtAssestType)
            val txtCurrent: TextView = convertView.findViewById(R.id.txtCurrent)
            val txtXIRR: TextView = convertView.findViewById(R.id.txtXIRR)
            val viewLineBottom: View = convertView.findViewById(R.id.viewLineBottom)

        }
    }

    private fun getReturnOfRisk()
    {
        if (isOnline())
        {
            apiService.getReturnOfRiskList(sessionManager.userId).enqueue(object : Callback<ReturnOfRiskListResponse>
            {
                override fun onFailure(call: Call<ReturnOfRiskListResponse>, t: Throwable)
                {
                    apiFailedToast()
                    loader.dismiss()
                }

                override fun onResponse(call: Call<ReturnOfRiskListResponse>, response: Response<ReturnOfRiskListResponse>)
                {
                    if (response.isSuccessful)
                    {

                        if (response.body()!!.success == 1)
                        {
                            listReturnofRisk.clear()
                            listReturnofRisk.addAll(response.body()!!.return_of_risk)
                            if (listReturnofRisk.isNotEmpty())
                            {
                                binding.rvReturnofRisk.adapter = ReturnOfRiskAdapter(listReturnofRisk)
                                var entryOne: MutableList<BarEntry> = mutableListOf()
                                var entryTwo: MutableList<BarEntry> = mutableListOf()
                                var entryThree: MutableList<BarEntry> = mutableListOf()
                                var labels: MutableList<String> = mutableListOf()
                                for (i in listReturnofRisk.indices){
                                    entryOne.add(BarEntry(i.toFloat(),listReturnofRisk[i].one_year.replace("%","").toFloat()))
                                    entryTwo.add(BarEntry((i.toFloat()),listReturnofRisk[i].three_year.replace("%","").toFloat()))
                                    entryThree.add(BarEntry((i.toFloat()),listReturnofRisk[i].five_year.replace("%","").toFloat()))
                                    if (i == 0)
                                    {
                                        labels.add("1 Year")
                                    }
                                    else if (i == 1)
                                    {
                                        labels.add("3 Year")
                                    }
                                    else if (i == 2)
                                    {
                                        labels.add("5 Year")
                                    }
                                }
                                groupChartBar(entryOne,entryTwo,labels,entryThree)


                            }
                            else
                            {
                                loader.dismiss()
                            }
                        }
                        else
                        {
                            loader.dismiss()
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

    fun groupChartBar(entryOne: MutableList<BarEntry>, entryTwo: MutableList<BarEntry>, xAaxLabels: MutableList<String>, entryThree: MutableList<BarEntry>)
    {
        val dataSetOne = BarDataSet(entryOne,"High")
        dataSetOne.color = Color.parseColor("#ee4849")
        val dataSetTwo = BarDataSet(entryTwo,"Average")
        dataSetTwo.color = Color.parseColor("#FFAE2B")
        val dataSetThree = BarDataSet(entryThree,"Low")
        dataSetThree.color = Color.parseColor("#008000")

        binding.barChart.description.isEnabled = false

        val groupSpace = 0.09f
        val barSpace = 0.02f
        val barWidth = 0.25f

        val barData = BarData(dataSetOne,dataSetTwo,dataSetThree)
        barData.barWidth = barWidth
        binding.barChart.data = barData
        binding.barChart.groupBars(0f,groupSpace,barSpace)

        val xAxis = binding.barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(xAaxLabels)
        binding.barChart.axisLeft.axisMinimum = -50f
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.barChart.axisLeft.setDrawGridLines(false)
        binding.barChart.axisRight.setDrawGridLines(false)
        binding.barChart.xAxis.setDrawGridLines(false)

        /*val formatter: IndexAxisValueFormatter =
            object : IndexAxisValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase): String {
                    return "${value.toInt()}%"
                }
            }*/

        xAxis.setLabelCount(3, false);

        val yAxis: YAxis = binding.barChart.axisLeft
        yAxis.valueFormatter = object : IAxisValueFormatter
        {
            override fun getFormattedValue(value: Float, axis: AxisBase?): String
            {
                return "${value.toInt()}%"
            }
        }

        val rightAxis : YAxis = binding.barChart.axisRight
        rightAxis.isEnabled = false
        binding.barChart.invalidate()
        binding.barChart.refreshDrawableState()
        loader.dismiss()
    }

    inner class ReturnOfRiskAdapter(var listItem: MutableList<ReturnOfRiskListResponse.ReturnOfRisk>) : RecyclerView.Adapter<ReturnOfRiskAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder
        {
            val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.fin_plan_return_of_risk, viewGroup, false)
            return ViewHolder(v)
        }

        override fun getItemCount(): Int
        {
            return listItem.size
        }

        inner class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView)

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            try
            {
                val getSet = listItem[position]
                holder.itemView.tvRangeOfReturn.text = getSet.range_of_return
                holder.itemView.tvOneYear.text = getSet.one_year
                holder.itemView.tvThreeYear.text = getSet.three_year
                holder.itemView.tvFiveYear.text = getSet.five_year
            }
            catch (e: Resources.NotFoundException)
            {
                e.printStackTrace()
            }
        }
    }
}