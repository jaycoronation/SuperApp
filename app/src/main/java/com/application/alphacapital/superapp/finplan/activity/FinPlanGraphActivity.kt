@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.application.alphacapital.superapp.finplan.activity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.alphafinancialplanning.model.*
import com.application.alphacapital.superapp.finplan.utils.AppConstant
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.CAME_FROM
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.GRAPH_BALANCES_SHEET
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.GRAPH_EXISTING_ASSET_ALLOCATION_MACRO
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.GRAPH_EXISTING_ASSET_ALLOCATION_MICRO
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.GRAPH_NEED_GAP_CURRENT
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.GRAPH_NEED_GAP_FUTURE
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.GRAPH_RANGE_RETURN_RISK
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.GRAPH_RECOMMENDED_ASSET_ALLOCATION
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.GRAPH_RISK_PROFILE_ALLOCATION
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.GRAPH_VARIANCE_MACRO_STRATEGIC
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.GRAPH_VARIANCE_MACRO_TACTICAL
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.GRAPH_VARIANCE_MICRO_STRATEGIC
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.GRAPH_VARIANCE_MICRO_TACTICAL
import com.application.alphacapital.superapp.finplan.utils.AppConstant.camefrom.GRAPH_WEALTH_REQUIRED
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.finplan.model.AssetAllocationMicroListResponse
import com.application.alphacapital.superapp.finplan.model.RecommendedAssetAllocationListResponse
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fin_plan_activity_graph.*
import kotlinx.android.synthetic.main.fin_plan_toolbar.*
import org.jetbrains.anko.startActivity
import com.github.mikephil.charting.components.AxisBase


class FinPlanGraphActivity : FinPlanBaseActivity() {

    private var cameFrom = ""

    private var listForAssetAllocationMicro: MutableList<AssetAllocationMicroListResponse.AssetAllocationMicro.AssetAllocationMicroList> = mutableListOf()
    private var listForAssetAllocationMacro: MutableList<AssetAllocationMacroListResponse.AssetAllocationMacro.AssetAllocationMacroList> = mutableListOf()

    private var listForVarianceAnalysisMicroStrategic: MutableList<VarianceAnalysisMicroListResponse.VarianceAnalysisMicro.VarianceAnalysis> = mutableListOf()
    private var listForVarianceAnalysisMicroTactical: MutableList<VarianceAnalysisMicroListResponse.VarianceAnalysisMicro.VarianceAnalysis> = mutableListOf()

    private var listForVarianceAnalysisMacroTactical: MutableList<VarianceAnalysisMacroListResponse.VarianceAnalysisMacro.VarianceAnalysis> = mutableListOf()
    private var listForVarianceAnalysisMacroStrategic: MutableList<VarianceAnalysisMacroListResponse.VarianceAnalysisMacro.VarianceAnalysis> = mutableListOf()

    private var listForRecommendedAssetAllocation : MutableList<RecommendedAssetAllocationListResponse.RecommendedAssetAllocation.AssetList> = mutableListOf()

    private var listForReturnRisk : MutableList<ReturnOfRiskListResponse.ReturnOfRisk> = mutableListOf()

    private var listForBalanceSheet : MutableList<BalanceSheetMovementListResponse.BalanceSheetMovement> = mutableListOf()

    private var listForWealthRequired : MutableList<WealthRequiredListResponse.WealthRequired> = mutableListOf()

    private var listForRiskProfileAllocation : MutableList<RiskProfileAllocationResponse.RiskProfileAllocation> = mutableListOf()

    private var total_wealth:Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fin_plan_activity_graph)

        if(intent.hasExtra(CAME_FROM)){
            cameFrom = intent.getStringExtra(CAME_FROM).toString()
        }
        initViews()

        onClicks()
    }

    private fun initViews() {
        tvTitle.text = cameFrom
        ivBack.backNav()

        if(intent.hasExtra("data")){
            when(cameFrom){
                GRAPH_EXISTING_ASSET_ALLOCATION_MICRO ->{
                    listForAssetAllocationMicro = Gson().fromJson(intent.getStringExtra("data"),Array<AssetAllocationMicroListResponse.AssetAllocationMicro.AssetAllocationMicroList>::class.java).toMutableList()
                    existingAssetAllocationMicro()
                }
                GRAPH_EXISTING_ASSET_ALLOCATION_MACRO ->{
                    listForAssetAllocationMacro = Gson().fromJson(intent.getStringExtra("data"),Array<AssetAllocationMacroListResponse.AssetAllocationMacro.AssetAllocationMacroList>::class.java).toMutableList()
                    existingAssetAllocationMacro()
                }
                GRAPH_RECOMMENDED_ASSET_ALLOCATION ->{
                    listForRecommendedAssetAllocation = Gson().fromJson(intent.getStringExtra("data"),Array<RecommendedAssetAllocationListResponse.RecommendedAssetAllocation.AssetList>::class.java).toMutableList()
                    recommendedAssetAllocation()
                }
                GRAPH_VARIANCE_MICRO_STRATEGIC ->{
                    listForVarianceAnalysisMicroStrategic = Gson().fromJson(intent.getStringExtra("data"),Array<VarianceAnalysisMicroListResponse.VarianceAnalysisMicro.VarianceAnalysis>::class.java).toMutableList()
                    var entryOne: MutableList<BarEntry> = mutableListOf()
                    var entryTwo: MutableList<BarEntry> = mutableListOf()
                    var labels: MutableList<String> = mutableListOf()
                    for (i in listForVarianceAnalysisMicroStrategic.indices){
                        entryOne.add(BarEntry(i.toFloat(),listForVarianceAnalysisMicroStrategic[i].existing_allocation.replace("%","").toFloat()))
                        entryTwo.add(BarEntry((i.toFloat()),listForVarianceAnalysisMicroStrategic[i].recommended_allocation.replace("%","").toFloat()))
                        labels.add(listForVarianceAnalysisMicroStrategic[i].asset_type)
                    }
                    groupChartBar(entryOne,entryTwo,labels)
                }
                GRAPH_VARIANCE_MICRO_TACTICAL ->{
                    listForVarianceAnalysisMicroTactical = Gson().fromJson(intent.getStringExtra("data"),Array<VarianceAnalysisMicroListResponse.VarianceAnalysisMicro.VarianceAnalysis>::class.java).toMutableList()

                    var entryOne: MutableList<BarEntry> = mutableListOf()
                    var entryTwo: MutableList<BarEntry> = mutableListOf()
                    var labels: MutableList<String> = mutableListOf()
                    for (i in listForVarianceAnalysisMicroTactical.indices){
                        entryOne.add(BarEntry(i.toFloat(),listForVarianceAnalysisMicroTactical[i].existing_allocation.replace("%","").toFloat()))
                        entryTwo.add(BarEntry((i.toFloat()),listForVarianceAnalysisMicroTactical[i].recommended_allocation.replace("%","").toFloat()))
                        labels.add(listForVarianceAnalysisMicroTactical[i].asset_type)
                    }
                    groupChartBar(entryOne,entryTwo,labels)
                }
                GRAPH_VARIANCE_MACRO_STRATEGIC ->{
                    listForVarianceAnalysisMacroStrategic = Gson().fromJson(intent.getStringExtra("data"),Array<VarianceAnalysisMacroListResponse.VarianceAnalysisMacro.VarianceAnalysis>::class.java).toMutableList()

                    var entryOne: MutableList<BarEntry> = mutableListOf()
                    var entryTwo: MutableList<BarEntry> = mutableListOf()
                    var labels: MutableList<String> = mutableListOf()
                    for (i in listForVarianceAnalysisMacroStrategic.indices){
                        entryOne.add(BarEntry(i.toFloat(),listForVarianceAnalysisMacroStrategic[i].existing_allocation.replace("%","").toFloat()))
                        entryTwo.add(BarEntry((i.toFloat()),listForVarianceAnalysisMacroStrategic[i].recommended_allocation.replace("%","").toFloat()))
                        labels.add(listForVarianceAnalysisMacroStrategic[i].asset_class)
                    }
                    groupChartBar(entryOne,entryTwo,labels)
                }
                GRAPH_VARIANCE_MACRO_TACTICAL ->{
                    listForVarianceAnalysisMacroTactical = Gson().fromJson(intent.getStringExtra("data"),Array<VarianceAnalysisMacroListResponse.VarianceAnalysisMacro.VarianceAnalysis>::class.java).toMutableList()

                    var entryOne: MutableList<BarEntry> = mutableListOf()
                    var entryTwo: MutableList<BarEntry> = mutableListOf()
                    var labels: MutableList<String> = mutableListOf()
                    for (i in listForVarianceAnalysisMacroTactical.indices){
                        entryOne.add(BarEntry(i.toFloat(),listForVarianceAnalysisMacroTactical[i].existing_allocation.replace("%","").toFloat()))
                        entryTwo.add(BarEntry((i.toFloat()),listForVarianceAnalysisMacroTactical[i].recommended_allocation.replace("%","").toFloat()))
                        labels.add(listForVarianceAnalysisMacroTactical[i].asset_class)
                    }
                    groupChartBar(entryOne,entryTwo,labels)
                }
                GRAPH_RANGE_RETURN_RISK ->{
                    listForReturnRisk = Gson().fromJson(intent.getStringExtra("data"),Array<ReturnOfRiskListResponse.ReturnOfRisk>::class.java).toMutableList()

                    var entryOne: MutableList<BarEntry> = mutableListOf()
                    var entryTwo: MutableList<BarEntry> = mutableListOf()
                    var entryThree: MutableList<BarEntry> = mutableListOf()
                    var labels: MutableList<String> = mutableListOf("1 Year","3 Year","5 Year")

                    /*entryOne.add(Entry(0f,listForReturnRisk[0].one_year.replace("%","").toFloat()))
                    entryOne.add(Entry(1f,listForReturnRisk[0].three_year.replace("%","").toFloat()))
                    entryOne.add(Entry(2f,listForReturnRisk[0].five_year.replace("%","").toFloat()))

                    entryThree.add(Entry(0f,listForReturnRisk[2].one_year.replace("%","").toFloat()))
                    entryThree.add(Entry(1f,listForReturnRisk[2].three_year.replace("%","").toFloat()))
                    entryThree.add(Entry(2f,listForReturnRisk[2].five_year.replace("%","").toFloat()))

                    entryTwo.add(Entry(0f,listForReturnRisk[1].one_year.replace("%","").toFloat()))
                    entryTwo.add(Entry(1f,listForReturnRisk[1].three_year.replace("%","").toFloat()))
                    entryTwo.add(Entry(2f,listForReturnRisk[1].five_year.replace("%","").toFloat()))*/

                    entryOne.add(BarEntry(0f,listForReturnRisk[0].one_year.replace("%","").toFloat()))
                    entryOne.add(BarEntry(1f,listForReturnRisk[0].three_year.replace("%","").toFloat()))
                    entryOne.add(BarEntry(2f,listForReturnRisk[0].five_year.replace("%","").toFloat()))

                    entryThree.add(BarEntry(0f,listForReturnRisk[2].one_year.replace("%","").toFloat()))
                    entryThree.add(BarEntry(1f,listForReturnRisk[2].three_year.replace("%","").toFloat()))
                    entryThree.add(BarEntry(2f,listForReturnRisk[2].five_year.replace("%","").toFloat()))

                    entryTwo.add(BarEntry(0f,listForReturnRisk[1].one_year.replace("%","").toFloat()))
                    entryTwo.add(BarEntry(1f,listForReturnRisk[1].three_year.replace("%","").toFloat()))
                    entryTwo.add(BarEntry(2f,listForReturnRisk[1].five_year.replace("%","").toFloat()))

                    groupChartBarForReturnRisk(entryOne,entryTwo,entryThree,labels)
                }
                GRAPH_BALANCES_SHEET ->{
                    listForBalanceSheet = Gson().fromJson(intent.getStringExtra("data"),Array<BalanceSheetMovementListResponse.BalanceSheetMovement>::class.java).toMutableList()

                    balanceSheetGraph()
                }
                GRAPH_WEALTH_REQUIRED ->{
                    llWealthRequired.visibility = View.VISIBLE
                    listForWealthRequired = Gson().fromJson(intent.getStringExtra("data"),Array<WealthRequiredListResponse.WealthRequired>::class.java).toMutableList()
                    total_wealth = intent.getDoubleExtra("total_wealth",0.0)
                    wealthRequiredGraph()
                }
                GRAPH_RISK_PROFILE_ALLOCATION ->{
                    listForRiskProfileAllocation = Gson().fromJson(intent.getStringExtra("data"),Array<RiskProfileAllocationResponse.RiskProfileAllocation>::class.java).toMutableList()

                    var entryOne: MutableList<BarEntry> = mutableListOf()
                    var entryTwo: MutableList<BarEntry> = mutableListOf()
                    var labels: MutableList<String> = mutableListOf()
                    for (i in listForRiskProfileAllocation.indices){
                        entryOne.add(BarEntry(i.toFloat(),listForRiskProfileAllocation[i].allocation.replace("%","").toFloat()))
                        entryTwo.add(BarEntry((i.toFloat()),listForRiskProfileAllocation[i].expected_return.replace("%","").toFloat()))
                        labels.add(listForRiskProfileAllocation[i].asset_class)
                    }
                    groupChartBar(entryOne,entryTwo,labels)
                }
                GRAPH_NEED_GAP_CURRENT->{
                    listForWealthRequired = Gson().fromJson(intent.getStringExtra("data"),Array<WealthRequiredListResponse.WealthRequired>::class.java).toMutableList()
                    total_wealth = intent.getDoubleExtra("total_wealth",0.0)
                    needGapCurrent()
                }
                GRAPH_NEED_GAP_FUTURE->{
                    listForWealthRequired = Gson().fromJson(intent.getStringExtra("data"),Array<WealthRequiredListResponse.WealthRequired>::class.java).toMutableList()
                    total_wealth = intent.getDoubleExtra("total_wealth",0.0)
                    needGapFuture()
                }
            }
        }
    }

    private fun onClicks(){
        tvNeedGapCurrent.setOnClickListener {
            startActivity<FinPlanGraphActivity>(
                AppConstant.camefrom.CAME_FROM to AppConstant.camefrom.GRAPH_NEED_GAP_CURRENT,
                "data" to Gson().toJson(listForWealthRequired),
                "total_wealth" to total_wealth
            )
        }

        tvNeedGapFuture.setOnClickListener {
            startActivity<FinPlanGraphActivity>(
                AppConstant.camefrom.CAME_FROM to AppConstant.camefrom.GRAPH_NEED_GAP_FUTURE,
                "data" to Gson().toJson(listForWealthRequired),
                "total_wealth" to total_wealth
            )
        }
    }

    private fun existingAssetAllocationMicro(){
        val xvalues = ArrayList<PieEntry>()
        for(getSet in listForAssetAllocationMicro){
            xvalues.add(PieEntry(getSet.allocation.replace("%","").toFloat(),getSet.asset_type))
        }
        loadPieChart(xvalues)
    }

    private fun existingAssetAllocationMacro(){
        val xvalues = ArrayList<PieEntry>()
        for(getSet in listForAssetAllocationMacro){
            xvalues.add(PieEntry(getSet.allocation.replace("%","").toFloat(),getSet.asset_class))
        }
        loadPieChart(xvalues)
    }

    private fun recommendedAssetAllocation(){
        val xvalues = ArrayList<PieEntry>()
        for(getSet in listForRecommendedAssetAllocation){
            xvalues.add(PieEntry(getSet.allocation.replace("%","").toFloat(),getSet.asset_class))
        }
        loadPieChart(xvalues)
    }

    fun loadPieChart(valueList : ArrayList<PieEntry>){
        pieChart.visibility = View.VISIBLE
        pieChart.setUsePercentValues(true)
        val dataSet = PieDataSet(valueList, "")
        dataSet.valueTypeface = appUtils.getTypeface(activity)
        dataSet.valueTextColor = Color.parseColor("#FFFFFF")
        dataSet.colors = CUSTOM_COLORS.asList()
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())

        pieChart.data = data
        pieChart.description.text = cameFrom
        pieChart.isDrawHoleEnabled = true
        data.setValueTextSize(13f)
        chartDetails(pieChart)
    }

    fun chartDetails(mChart: PieChart) {
        mChart.description.isEnabled = false
        mChart.centerText = ""
        mChart.setNoDataTextTypeface(appUtils.getTypeface(activity))
        mChart.setEntryLabelTypeface(appUtils.getTypeface(activity))
        mChart.setCenterTextSize(10F)
        mChart.setCenterTextColor(R.color.fin_plan_colorPrimaryDark)
        mChart.setCenterTextTypeface(appUtils.getTypeface(activity))
        val l = mChart.legend
        mChart.legend.isWordWrapEnabled = true
        mChart.legend.isEnabled = true
        mChart.legend.typeface = appUtils.getTypeface(activity)
        //l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        //l.formSize = 20F
        mChart.legend.formToTextSpace = 10f
        mChart.legend.form = Legend.LegendForm.SQUARE
        mChart.legend.textSize = 12f
        mChart.legend.orientation = Legend.LegendOrientation.HORIZONTAL

        mChart.setTouchEnabled(true)
        mChart.setDrawEntryLabels(true)
        mChart.legend.isWordWrapEnabled = true

        mChart.setExtraOffsets(20f, 0f, 20f, 0f)
        mChart.setUsePercentValues(true)
        // mChart.rotationAngle = 0f
        mChart.setDrawCenterText(true)
        mChart.description.isEnabled = true
        mChart.isRotationEnabled = true
    }

    val CUSTOM_COLORS = intArrayOf(
        rgb("#f44336"), rgb("#9C27B0"), rgb("#673AB7"), rgb("#2196F3"),
        rgb("#00BCD4"), rgb("#009688"), rgb("#8BC34A"), rgb("#CDDC39"),
        rgb("#FFA000"), rgb("#F57C00"), rgb("#F4511E"), rgb("#2979FF"),
        rgb("#f44336"), rgb("#9C27B0"), rgb("#673AB7"), rgb("#2196F3")
    )

    /**
     * Converts the given hex-color-string to rgb.
     *
     * @param hex
     * @return
     */
    fun rgb(hex: String): Int {
        val color = hex.replace("#", "").toLong(16).toInt()
        val r = color shr 16 and 0xFF
        val g = color shr 8 and 0xFF
        val b = color shr 0 and 0xFF
        return Color.rgb(r, g, b)
    }

    fun groupChartBar(entryOne: MutableList<BarEntry>,entryTwo: MutableList<BarEntry>,xAaxLabels: MutableList<String>){

        barChart.visibility = View.VISIBLE
        val dataSetOne = BarDataSet(entryOne,"Existing")
        dataSetOne.color = Color.parseColor("#ee4849")
        val dataSetTwo = BarDataSet(entryTwo,"Recommended")
        dataSetTwo.color = Color.parseColor("#FFAE2B")
        barChart.description.isEnabled = false
        val groupSpace = 0.09f
        val barSpace = 0.02f // x2 dataset
        val barWidth = 0.35f // x2 dataset

        val barData = BarData(dataSetOne,dataSetTwo)
        barData.barWidth = barWidth
        barChart.data = barData
        barChart.groupBars(0f,groupSpace,barSpace)
        barChart.invalidate()


        val xAxis = barChart.xAxis

        xAxis.valueFormatter = IndexAxisValueFormatter(xAaxLabels)
        barChart.axisLeft.axisMinimum = 0F
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f

        xAxis.isGranularityEnabled = true


        /*val formatter: ValueFormatter =
            object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase): String {
                    return "$value%"
                }
            }*/

        val yAxis: YAxis = barChart.axisLeft
        yAxis.valueFormatter = IndexAxisValueFormatter()

        val rightAxis : YAxis = barChart.axisRight
        rightAxis.isEnabled = false

        barChart.axisLeft.setDrawGridLines(false)
        barChart.axisRight.setDrawGridLines(false)
        barChart.xAxis.setDrawGridLines(false)


    }

    fun groupChartBarForReturnRisk(entryOne: MutableList<BarEntry>,entryTwo: MutableList<BarEntry>,entryThree: MutableList<BarEntry>,xAaxLabels: MutableList<String>){

        barChart.visibility = View.VISIBLE
        val dataSetOne = BarDataSet(entryOne,"High")
        dataSetOne.color = Color.parseColor("#ee4849")
        val dataSetTwo = BarDataSet(entryTwo,"Average")
        dataSetTwo.color = Color.parseColor("#FFAE2B")
        val dataSetThree = BarDataSet(entryThree,"Low")
        dataSetThree.color = Color.parseColor("#008000")

        barChart.description.isEnabled = false

        val groupSpace = 0.09f
        val barSpace = 0.02f
        val barWidth = 0.25f

        val barData = BarData(dataSetOne,dataSetTwo,dataSetThree)
        barData.barWidth = barWidth
        barChart.data = barData
        barChart.groupBars(0f,groupSpace,barSpace)

        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(xAaxLabels)
        barChart.axisLeft.axisMinimum = -50f
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChart.axisLeft.setDrawGridLines(false)
        barChart.axisRight.setDrawGridLines(false)
        barChart.xAxis.setDrawGridLines(false)

        /*val formatter: IndexAxisValueFormatter =
            object : IndexAxisValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase): String {
                    return "${value.toInt()}%"
                }
            }*/


        xAxis.setLabelCount(3, false);

        val yAxis: YAxis = barChart.axisLeft
        yAxis.valueFormatter = object : IAxisValueFormatter
        {
            override fun getFormattedValue(value: Float, axis: AxisBase?): String
            {
                return "${value.toInt()}%"
            }
        }

        val rightAxis : YAxis = barChart.axisRight
        rightAxis.isEnabled = false

        /*Line*/
        /*val dataSetOne = LineDataSet(entryOne,"High")
        dataSetOne.color = Color.parseColor("#ee4849")
        dataSetOne.setCircleColor(dataSetOne.color)
        val dataSetTwo = LineDataSet(entryTwo,"Average")
        dataSetTwo.color = Color.parseColor("#FFAE2B")
        dataSetTwo.setCircleColor(dataSetTwo.color)
        val dataSetThree = LineDataSet(entryThree,"Low")
        dataSetThree.color = Color.parseColor("#008000")
        dataSetThree.setCircleColor(dataSetThree.color)

        val barData = LineData(dataSetOne,dataSetTwo,dataSetThree)
        lineChart.data = barData

        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(xAaxLabels)

        lineChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener{
            override fun onNothingSelected() {

            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                Log.e(">>>>>>>","   "+e?.x)
                Log.e(">>>>>>>","   "+e?.y?.toInt())

                e?.y?.toFloat()?.let { showToast("$it%") }

            }

        })

        lineChart.xAxis.setDrawGridLines(false)*/

    }

    fun balanceSheetGraph(){

        lineChart.visibility = View.VISIBLE

        lineChart.description.isEnabled = false
        val listEntry: MutableList<Entry> = mutableListOf()
        val listYear: MutableList<String> = mutableListOf()
        for(i  in listForBalanceSheet.indices){
            listYear.add(listForBalanceSheet[i].year)
            listEntry.add(Entry(i.toFloat(),listForBalanceSheet[i].closing_balance.replace(activity.getString(R.string.rs),"").trim().toFloat()))
        }
        var lineDataSet = LineDataSet(listEntry,"")
        lineDataSet.setDrawValues(false)


        /*val formatter: ValueFormatter =
            object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase): String {
                    return listYear[value.toInt()]
                }
            }*/

        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        xAxis.granularity = 1f
        xAxis.valueFormatter = object : IAxisValueFormatter
        {
            override fun getFormattedValue(value: Float, axis: AxisBase?): String
            {
                return listYear[value.toInt()]
            }

        }

        val yAxisRight = lineChart.axisRight
        yAxisRight.isEnabled = false

        val yAxisLeft = lineChart.axisLeft
        yAxisLeft.granularity = 1f

        val data = LineData(lineDataSet)
        lineChart.data = data
        lineChart.animateX(2500)

        lineChart.setDrawMarkers(true)

        lineChart.isHighlightPerTapEnabled = true

        /*val marker: IMarker = CustomMarkerView(activity,R.layout.graph_content,listForBalanceSheet)
        lineChart.marker = marker*/

        lineChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener{
            override fun onNothingSelected() {

            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                Log.e(">>>>>>>","   "+e?.x)
                Log.e(">>>>>>>","   "+e?.y?.toInt())

                e?.y?.toInt()?.let { showToast(activity.getString(R.string.rs)+it.toString()) }

            }

        })

        lineChart.invalidate()
    }

    open class CustomMarkerView(context: Context?, layoutResource: Int,var list : MutableList<BalanceSheetMovementListResponse.BalanceSheetMovement>) :
        MarkerView(context, layoutResource) {
        var tvContent: TextView = findViewById(R.id.tvContent)

        // callbacks everytime the MarkerView is redrawn, can be used to update the
        // content (user-interface)
        @SuppressLint("SetTextI18n")
        override fun refreshContent(
            e: Entry,
            highlight: Highlight?
        ) {
            try {
                tvContent.text = "" + list[e.x.toInt()].closing_balance
            } catch (e: Exception) {
            }

            super.refreshContent(e, highlight)
        }

        // this will center the marker-view horizontally
        val xOffset: Int
            get() =// this will center the marker-view horizontally
                -(width / 2)

        // this will cause the marker-view to be above the selected value
        val yOffset: Int
            get() =// this will cause the marker-view to be above the selected value
                -height
    }

    fun wealthRequiredGraph(){

        lineChart.visibility = View.VISIBLE

        lineChart.description.isEnabled = false
        val listExistingWealth: MutableList<Entry> = mutableListOf()
        val listFutureWealth: MutableList<Entry> = mutableListOf()
        val listHumanValue: MutableList<Entry> = mutableListOf()
        val listYear: MutableList<String> = mutableListOf()
        for(i  in listForWealthRequired.indices){
            listYear.add(listForWealthRequired[i].year)
            listExistingWealth.add(Entry(i.toFloat(),listForWealthRequired[i].exisiting_wealth.replace(activity.getString(R.string.rs),"").replace(",","").trim().toFloat()))
            listFutureWealth.add(Entry(i.toFloat(),listForWealthRequired[i].future_wealth.replace(activity.getString(R.string.rs),"").replace(",","").trim().toFloat()))
            listHumanValue.add(Entry(i.toFloat(),listForWealthRequired[i].human_value.replace(activity.getString(R.string.rs),"").replace(",","").trim().toFloat()))
        }

        var set1 = LineDataSet(listExistingWealth,"Existing Wealth")
        set1.setDrawValues(false)
        set1.color = Color.parseColor("#94279C")
        set1.setCircleColor(Color.parseColor("#94279C"))

        var set2 = LineDataSet(listFutureWealth,"Future Wealth")
        set2.setDrawValues(false)
        set2.color = Color.parseColor("#008000")
        set2.setCircleColor(Color.parseColor("#008000"))

        var set3 = LineDataSet(listHumanValue,"Human Value")
        set3.setDrawValues(false)
        set3.color = Color.parseColor("#FFAE2B")
        set3.setCircleColor(Color.parseColor("#FFAE2B"))

        /*val formatter: ValueFormatter =
            object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase): String {
                    return listYear[value.toInt()]
                }
            }*/

        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        xAxis.granularity = 1f
        xAxis.valueFormatter = object : IAxisValueFormatter
        {
            override fun getFormattedValue(value: Float, axis: AxisBase?): String
            {
                return listYear[value.toInt()]
            }
        }

        val yAxisRight = lineChart.axisRight
        yAxisRight.isEnabled = false

        val yAxisLeft = lineChart.axisLeft
        yAxisLeft.granularity = 1f

        val data = LineData(set1,set2,set3)
        lineChart.data = data
        lineChart.animateX(2500)

        //lineChart.setDrawMarkers(true)

        lineChart.isHighlightPerTapEnabled = true

        lineChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener{
            override fun onNothingSelected() {

            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                Log.e(">>>>>>>","   "+e?.x)
                Log.e(">>>>>>>","   "+e?.y?.toInt())

                e?.y?.toInt()?.let { showToast(activity.getString(R.string.rs)+it.toString()) }


            }

        })

       /* val marker: IMarker = CustomMarkerView(activity,R.layout.graph_content,listForBalanceSheet)
        lineChart.marker = marker*/

        lineChart.invalidate()
    }

    fun needGapCurrent(){
        barChart.visibility = View.VISIBLE

        var entryOne: MutableList<BarEntry> = mutableListOf()
        var xAaxLabels: MutableList<String> = mutableListOf()

        entryOne.add(BarEntry(0.toFloat(),listForWealthRequired[0].wealth_required.toFloat()))
        entryOne.add(BarEntry(1.toFloat(),listForWealthRequired[0].exisiting_wealth.toFloat()))

        xAaxLabels.add(0,"Wealth Required")
        xAaxLabels.add(1,"Existing Wealth")

        val dataSetOne = BarDataSet(entryOne,"Need Gap-Current")
        dataSetOne.colors = CUSTOM_COLORS.asList()
        val barData:BarData = BarData(dataSetOne)
        barChart.data = barData



        val xAxis = barChart.xAxis

        xAxis.valueFormatter = IndexAxisValueFormatter(xAaxLabels)
        barChart.axisLeft.axisMinimum = 0F
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f

        xAxis.isGranularityEnabled = true

        val rightAxis : YAxis = barChart.axisRight
        rightAxis.isEnabled = false

        barChart.description.text = ""

        barChart.axisLeft.setDrawGridLines(false)
        barChart.axisRight.setDrawGridLines(false)
        barChart.xAxis.setDrawGridLines(false)

    }

    fun needGapFuture(){
        barChart.visibility = View.VISIBLE

        var entryOne: MutableList<BarEntry> = mutableListOf()
        var xAaxLabels: MutableList<String> = mutableListOf()

        entryOne.add(BarEntry(0.toFloat(),total_wealth.toFloat()))
        entryOne.add(BarEntry(1.toFloat(),listForWealthRequired[0].wealth_required.toFloat()))

        xAaxLabels.add(0,"Total Wealth")
        xAaxLabels.add(1,"Wealth Required")

        val dataSetOne = BarDataSet(entryOne,"Need Gap-Future")
        dataSetOne.colors = CUSTOM_COLORS.asList()
        val barData:BarData = BarData(dataSetOne)
        barChart.data = barData



        val xAxis = barChart.xAxis

        xAxis.valueFormatter = IndexAxisValueFormatter(xAaxLabels)
        barChart.axisLeft.axisMinimum = 0F
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f

        xAxis.isGranularityEnabled = true

        val rightAxis : YAxis = barChart.axisRight
        rightAxis.isEnabled = false

        barChart.description.text = ""

        barChart.axisLeft.setDrawGridLines(false)
        barChart.axisRight.setDrawGridLines(false)
        barChart.xAxis.setDrawGridLines(false)

    }
}
