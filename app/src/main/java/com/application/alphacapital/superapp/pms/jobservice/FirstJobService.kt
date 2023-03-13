package com.application.alphacapital.superapp.pms.jobservice

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Message
import android.util.Log
import android.view.View
import com.application.alphacapital.superapp.pms.activity.ChartActivity
import com.application.alphacapital.superapp.pms.beans.*
import com.application.alphacapital.superapp.pms.beans.AppicantiListResponseModel.ApplicantsListItem
import com.application.alphacapital.superapp.pms.beans.BSMovementResponseModel.GraphDataItem
import com.application.alphacapital.superapp.pms.beans.PortfoliaResponseModel.PortfolioDetailsItem
import com.application.alphacapital.superapp.pms.fragment.DashBoardFragment
import com.application.alphacapital.superapp.pms.network.PortfolioApiClient
import com.application.alphacapital.superapp.pms.network.PortfolioApiInterface
import com.application.alphacapital.superapp.pms.utils.ApiUtils
import com.application.alphacapital.superapp.pms.utils.AppUtils
import com.application.alphacapital.superapp.pms.utils.SessionManager
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FirstJobService() : JobService() {

    companion object {
        private const val TAG = "ExampleJobService"
        private var years = ""
        private const val TIME_SLEEP_MILLISECONDS: Long = 1000
    }

    private var jobCanceled: Boolean = false
    private var listPerformance = ArrayList<PerformanceTemp>()
    private val listNextYearData = ArrayList<PerformanceTemp>()
    private val listPreviousYearData = ArrayList<PerformanceTemp>()
    private var listGraphData = ArrayList<GraphDataItem>()
    private var strlistTableData = ""
    private var apiService: PortfolioApiInterface? = null
    private var pmsSession: SessionManager? = null

    private var listPortfolioDetails = ArrayList<PortfolioDetailsItem>()
    private var listApplicants = ArrayList<ApplicantsListItem>()

    private fun doBackgroundWork(p0: JobParameters?) {
        Thread {
            kotlin.run {
                /*for (i: Int in 0 until 9) {
                    Log.e(TAG, "run: $i")

                    if (jobCanceled) {
                        return@run
                    }

                    Thread.sleep(TIME_SLEEP_MILLISECONDS)01C00276
                }
                Log.e(TAG, "Job finish")*/
                pmsSession = SessionManager(this)
                apiService = PortfolioApiClient.getClient().create(PortfolioApiInterface::class.java)
                Log.e(TAG, "getPerformanceActivityData")
                getPerformanceActivityData()
                getYears()
                getBSMovementData()
                Log.e(TAG, "Job finish")
                jobFinished(p0, false)
            }
        }.start()
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        Log.e(TAG, "Job Started")
        doBackgroundWork(p0)
        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        Log.e(TAG, "Job canceled before completion${p0?.jobId}")
        jobCanceled = true
        return true
    }

    private fun getYears()
    {
        apiService!!.getAppicantsAPI(ApiUtils.END_USER_ID).enqueue(object : Callback<AppicantiListResponseModel>
        {
            override fun onResponse(call: Call<AppicantiListResponseModel>, response: Response<AppicantiListResponseModel>)
            {
                if (response.isSuccessful)
                {
                    if (response.body()!!.success == 1)
                    {
                        if (response.body()!!.applicants_list != null && response.body()!!.applicants_list.size > 0)
                        {
                            years = response.body()!!.applicants_list[response.body()!!.applicants_list.size - 1].cid
                            getDataForPortfolio()
                        }
                        else
                        {
                        }
                    }
                    else
                    {
                    }
                }
                else
                {
                }
            }

            override fun onFailure(call: Call<AppicantiListResponseModel>, t: Throwable)
            {
            }
        })
    }

    private fun getDataForPortfolio()
    {
        listPortfolioDetails = ArrayList<PortfolioDetailsItem>()
        apiService!!.getPortFolioAPI(ApiUtils.END_USER_ID, years).enqueue(object : Callback<PortfoliaResponseModel>
        {
            override fun onResponse(call: Call<PortfoliaResponseModel>, response: Response<PortfoliaResponseModel>)
            {
                if (response.isSuccessful)
                {
                    if (response.body()!!.success == 1)
                    {
                        listPortfolioDetails.addAll(response.body()!!.portfolio_details)


                        if(listPortfolioDetails.size > 0)
                        {
                            pmsSession?.savePortfolioList(listPortfolioDetails)

                            if (DashBoardFragment.handler != null)
                            {
                                Log.e(TAG, listPortfolioDetails.size.toString() + " Performance IN IF <><>")
                                val message = Message.obtain()
                                message.what = 111
                                message.obj = listPortfolioDetails
                                DashBoardFragment.handler.sendMessage(message)
                            }
                            else
                            {
                                Log.e(TAG, listPortfolioDetails.size.toString() + " Performance In ELSE <><>")
                            }
                        }
                        val grandTotalCurrent = AppUtils.getValidAPIStringResponse(response.body()!!.grand_total.currentValue.toString())
                        val grandTotalInvest = AppUtils.getValidAPIStringResponse(response.body()!!.grand_total.initialValue.toString())
                        val grandTotalProfit = AppUtils.getValidAPIStringResponse(response.body()!!.grand_total.gain.toString())
                        pmsSession?.savePortfolioTitle(grandTotalCurrent,grandTotalInvest, grandTotalProfit)

                    }
                    else
                    {
                    }
                }
                else
                {
                }
            }

            override fun onFailure(call: Call<PortfoliaResponseModel>, t: Throwable)
            {
            }
        })
    }

    private fun getPerformanceActivityData()
    {
        listPerformance = ArrayList()
        apiService?.getPerfomaceAPI(ApiUtils.END_USER_ID)?.enqueue(object :
            Callback<PerfomanceResponseModel>
        {
            override fun onResponse(call: Call<PerfomanceResponseModel>, response: Response<PerfomanceResponseModel>)
            {
                if (response.isSuccessful)
                {
                    if (response.body()!!.success == 1)
                    {
                        try
                        {
                            if (response.body()!!.since_inception.since_inception_details.size > 0)
                            {
                                val performanceTemp1 = PerformanceTemp()
                                performanceTemp1.assetName = "Assets"
                                performanceTemp1.invested = "Invested"
                                performanceTemp1.current = "Current"
                                performanceTemp1.gain = "Gain"
                                performanceTemp1.xirr = "XIRR"
                                listPerformance.add(performanceTemp1)
                                for (i in response.body()!!.since_inception.since_inception_details.indices)
                                {
                                    val performanceTemp = PerformanceTemp()
                                    performanceTemp.assetName = response.body()!!.since_inception.since_inception_details[i].asset_type
                                    performanceTemp.invested = AppUtils.convertToCommaSeperatedValue(response.body()!!.since_inception.since_inception_details[i].amountInvested.toString())
                                    performanceTemp.current = AppUtils.convertToCommaSeperatedValue(response.body()!!.since_inception.since_inception_details[i].currentValue.toString())
                                    performanceTemp.gain = AppUtils.convertToCommaSeperatedValue(response.body()!!.since_inception.since_inception_details[i].gain.toString())
                                    performanceTemp.xirr = String.format("%.2f", response.body()!!.since_inception.since_inception_details[i].xirr)
                                    listPerformance.add(performanceTemp)
                                }
                                val performanceTemp2 = PerformanceTemp()
                                performanceTemp2.assetName = "Overall"
                                performanceTemp2.invested = AppUtils.convertToCommaSeperatedValue(response.body()!!.since_inception.grand_total.amountInvested.toString())
                                performanceTemp2.current = AppUtils.convertToCommaSeperatedValue(response.body()!!.since_inception.grand_total.currentValue.toString())
                                performanceTemp2.gain = AppUtils.convertToCommaSeperatedValue(response.body()!!.since_inception.grand_total.gain.toString())
                                performanceTemp2.xirr = String.format("%.2f", response.body()!!.since_inception.grand_total.xirr) + ""
                                listPerformance.add(performanceTemp2)

                                if(listPerformance.size > 0)
                                {
                                    pmsSession?.savePerformanceList(listPerformance)

                                    if (DashBoardFragment.handler != null)
                                    {
                                        Log.e(TAG, listPerformance.size.toString() + " Performance IN IF <><>")
                                        val message = Message.obtain()
                                        message.what = 111
                                        message.obj = listPerformance
                                        DashBoardFragment.handler.sendMessage(message)
                                    }
                                    else
                                    {
                                        Log.e(TAG, listPerformance.size.toString() + " Performance In ELSE <><>")
                                    }
                                }
                            }
                        }
                        catch (e: Exception)
                        {
                            e.printStackTrace()
                        }
                    }
                    else
                    {
                        Log.e(TAG, "OnFailure Performance Else")
                    }

                    getXIRRData()
                }
            }

            override fun onFailure(call: Call<PerfomanceResponseModel>, t: Throwable)
            {
                Log.e(TAG, "OnFailure Performance")
                getXIRRData()
            }
        })
    }

    private fun getXIRRData()
    {
        if (listNextYearData.size > 0)
        {
            listNextYearData.clear()
        }
        apiService!!.getXIRRAPI(ApiUtils.END_USER_ID).enqueue(object : Callback<XIRRResponseModel>
        {
            override fun onResponse(call: Call<XIRRResponseModel>, response: Response<XIRRResponseModel>)
            {
                if (response.isSuccessful)
                {
                    if (response.body()!!.success == 1)
                    {
                        try
                        {
                            if (response.body()!!.since_inception.since_inception_details.size > 0)
                            {
                                val performanceTemp1 = PerformanceTemp()
                                performanceTemp1.assetName = "Assets"
                                performanceTemp1.invested = "Invested"
                                performanceTemp1.current = "Current"
                                performanceTemp1.gain = "Gain"
                                performanceTemp1.xirr = "XIRR"
                                listNextYearData.add(performanceTemp1)
                                for (i in response.body()!!.since_inception.since_inception_details.indices)
                                {
                                    val performanceTemp = PerformanceTemp()
                                    performanceTemp.assetName = response.body()!!.since_inception.since_inception_details[i].asset_type
                                    performanceTemp.invested = AppUtils.convertToCommaSeperatedValue(response.body()!!.since_inception.since_inception_details[i].amountInvested.toString())
                                    performanceTemp.current = AppUtils.convertToCommaSeperatedValue(response.body()!!.since_inception.since_inception_details[i].currentValue.toString())
                                    performanceTemp.gain = AppUtils.convertToCommaSeperatedValue(response.body()!!.since_inception.since_inception_details[i].gain.toString())
                                    performanceTemp.xirr = String.format("%.2f", response.body()!!.since_inception.since_inception_details[i].xirr)
                                    listNextYearData.add(performanceTemp)
                                }
                                val performanceTemp2 = PerformanceTemp()
                                performanceTemp2.assetName = "Overall"
                                performanceTemp2.invested = AppUtils.convertToCommaSeperatedValue(response.body()!!.since_inception.grand_total.amountInvested.toString())
                                performanceTemp2.current = AppUtils.convertToCommaSeperatedValue(response.body()!!.since_inception.grand_total.currentValue.toString())
                                performanceTemp2.gain = AppUtils.convertToCommaSeperatedValue(response.body()!!.since_inception.grand_total.gain.toString())
                                performanceTemp2.xirr = String.format("%.2f", response.body()!!.since_inception.grand_total.xirr)
                                listNextYearData.add(performanceTemp2)

                                Log.e(TAG, listNextYearData.size.toString() + " NextYear <><>")

                                if(listNextYearData.size > 0)
                                {
                                    pmsSession?.saveNextYearList(listNextYearData)
                                    if (DashBoardFragment.handler != null)
                                    {
                                        Log.e(TAG, listNextYearData.size.toString() + " NextYearData IN IF <><>")
                                        val message = Message.obtain()
                                        message.what = 222
                                        message.obj = listNextYearData
                                        DashBoardFragment.handler.sendMessage(message)
                                    }
                                    else
                                    {
                                        Log.e(TAG, listNextYearData.size.toString() + " NextYearData In ELSE <><>")
                                    }
                                }
                            }
                        }
                        catch (e: Exception)
                        {
                            e.printStackTrace()
                        }
                    }
                    else
                    {
                        Log.e(TAG, "OnFailure NextYear ELSE")
                    }
                    getXIRRPreviousData()
                }
            }

            override fun onFailure(call: Call<XIRRResponseModel>, t: Throwable)
            {
                Log.e(TAG, "OnFailure NextYear")
                getXIRRPreviousData()
            }
        })
    }

    private fun getXIRRPreviousData()
    {
        if (listPreviousYearData.size > 0)
        {
            listPreviousYearData.clear()
        }
        apiService!!.getxirrPreviousApi(ApiUtils.END_USER_ID).enqueue(object :
            Callback<XIRRResponseModel>
        {
            override fun onResponse(call: Call<XIRRResponseModel>, response: Response<XIRRResponseModel>)
            {
                if (response.isSuccessful)
                {
                    if (response.body()!!.success == 1)
                    {
                        try
                        {
                            if (response.body()!!.since_inception.since_inception_details.size > 0)
                            {
                                val performanceTemp1 = PerformanceTemp()
                                performanceTemp1.assetName = "Assets"
                                performanceTemp1.invested = "Invested"
                                performanceTemp1.current = "Current"
                                performanceTemp1.gain = "Gain"
                                performanceTemp1.xirr = "XIRR"
                                listPreviousYearData.add(performanceTemp1)
                                for (i in response.body()!!.since_inception.since_inception_details.indices)
                                {
                                    val performanceTemp = PerformanceTemp()
                                    performanceTemp.assetName = response.body()!!.since_inception.since_inception_details[i].asset_type
                                    performanceTemp.invested = AppUtils.convertToCommaSeperatedValue(response.body()!!.since_inception.since_inception_details[i].amountInvested.toString())
                                    performanceTemp.current = AppUtils.convertToCommaSeperatedValue(response.body()!!.since_inception.since_inception_details[i].currentValue.toString())
                                    performanceTemp.gain = AppUtils.convertToCommaSeperatedValue(response.body()!!.since_inception.since_inception_details[i].gain.toString())
                                    performanceTemp.xirr = String.format("%.2f", response.body()!!.since_inception.since_inception_details[i].xirr)
                                    listPreviousYearData.add(performanceTemp)
                                }
                                val performanceTemp2 = PerformanceTemp()
                                performanceTemp2.assetName = "Overall"
                                performanceTemp2.invested = AppUtils.convertToCommaSeperatedValue(response.body()!!.since_inception.grand_total.amountInvested.toString())
                                performanceTemp2.current = AppUtils.convertToCommaSeperatedValue(response.body()!!.since_inception.grand_total.currentValue.toString())
                                performanceTemp2.gain = AppUtils.convertToCommaSeperatedValue(response.body()!!.since_inception.grand_total.gain.toString())
                                performanceTemp2.xirr = String.format("%.2f", response.body()!!.since_inception.grand_total.xirr)
                                listPreviousYearData.add(performanceTemp2)
                                Log.e(TAG, listPreviousYearData.size.toString() + " Previous <><>")


                                if(listPreviousYearData.size > 0)
                                {
                                    pmsSession?.savePreviousYearList(listPreviousYearData)
                                    if (DashBoardFragment.handler != null)
                                    {
                                        Log.e(TAG, listPreviousYearData.size.toString() + " PreviousYearData IN IF <><>")
                                        val message = Message.obtain()
                                        message.what = 333
                                        message.obj = listPreviousYearData
                                        DashBoardFragment.handler.sendMessage(message)
                                    }
                                    else
                                    {
                                        Log.e(TAG, listPreviousYearData.size.toString() + " PreviousYearData In ELSE <><>")
                                    }
                                }
                            }
                        }
                        catch (e: Exception)
                        {
                            e.printStackTrace()
                        }
                    }
                    else
                    {
                        Log.e(TAG, "OnFailure PreviousYear ELSE")
                    }
                }
            }

            override fun onFailure(call: Call<XIRRResponseModel>, t: Throwable)
            {
                Log.e(TAG, "OnFailure PreviousYear")
            }
        })
    }

    private fun getBSMovementData()
    {
        try
        {
            listGraphData = ArrayList<GraphDataItem>()
            apiService!!.getBSMovementAPI(ApiUtils.END_USER_ID, "monthly").enqueue(object : Callback<BSMovementResponseModel>
            {
                override fun onResponse(call: Call<BSMovementResponseModel>, response: Response<BSMovementResponseModel>)
                {
                    if (response.isSuccessful)
                    {
                        if (response.body()!!.success == 1)
                        {
                            try
                            {
                                if (response.body()!!.graph_data.size > 0)
                                {
                                    listGraphData.addAll(response.body()!!.graph_data)
                                    pmsSession?.saveBsMovementList(listGraphData);

                                    if (ChartActivity.handler != null)
                                    {
                                        Log.e(TAG, listGraphData.size.toString() + " BS MOVEMENT IN IF <><>")
                                        val message = Message.obtain()
                                        message.what = 100
                                        message.obj = listGraphData
                                        ChartActivity.handler.sendMessage(message)
                                    }
                                    else
                                    {
                                        Log.e(TAG, listGraphData.size.toString() + " BS MOVEMENT In ELSE <><>")
                                    }

                                }
                                if (response.body()!!.sheet_data.size > 0)
                                {
                                    val gson = Gson()
                                    strlistTableData = gson.toJson(response.body()!!.sheet_data)

                                    if (ChartActivity.handler != null)
                                    {
                                        pmsSession?.saveBsMovementTableData(strlistTableData);
                                        Log.e(TAG, strlistTableData.length.toString() + " BS MOVEMENT IN IF <><>")
                                        val message = Message.obtain()
                                        message.what = 111
                                        message.obj = strlistTableData
                                        ChartActivity.handler.sendMessage(message)
                                    }
                                    else
                                    {
                                        Log.e(TAG, strlistTableData.length.toString() + " BS MOVEMENT In ELSE <><>")
                                    }

                                }
                                else
                                {
                                }
                            }
                            catch (e: Exception)
                            {
                                e.printStackTrace()
                            }
                        }
                        else
                        {
                        }
                    }
                    else
                    {
                    }
                }

                override fun onFailure(call: Call<BSMovementResponseModel>, t: Throwable) {
                    AppUtils.printLog("FALi", t.message)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}