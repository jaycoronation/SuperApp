package com.application.alphacapital.superapp.pms.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.job.JobInfo;
import android.content.ComponentName;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.application.alphacapital.superapp.R;
import com.application.alphacapital.superapp.databinding.DashboardFragmentBinding;
import com.application.alphacapital.superapp.pms.activity.DashboardPortfolioActivity;
import com.application.alphacapital.superapp.pms.beans.ApplicantListResponseModel;
import com.application.alphacapital.superapp.pms.beans.ApplicantsTemp;
import com.application.alphacapital.superapp.pms.beans.AssetsAllocationTemp;
import com.application.alphacapital.superapp.pms.beans.NetworthTempData;
import com.application.alphacapital.superapp.pms.beans.PerfomanceResponseModel;
import com.application.alphacapital.superapp.pms.beans.PerformanceTemp;
import com.application.alphacapital.superapp.pms.beans.XIRRResponseModel;
import com.application.alphacapital.superapp.pms.jobservice.FirstJobService;
import com.application.alphacapital.superapp.pms.network.PortfolioApiClient;
import com.application.alphacapital.superapp.pms.network.PortfolioApiInterface;
import com.application.alphacapital.superapp.pms.utils.ApiUtils;
import com.application.alphacapital.superapp.pms.utils.AppUtils;
import com.application.alphacapital.superapp.pms.utils.SessionManager;
import com.application.alphacapital.superapp.vault.model.GetPercentageResponse;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashBoardFragment extends Fragment
{
    private Activity activity;
    private DashboardFragmentBinding binding;
    private SessionManager sessionManager;
    private PortfolioApiInterface apiService;
    private ArrayList<AssetsAllocationTemp> listAssetAllocation = new ArrayList<AssetsAllocationTemp>();
    private ArrayList<ApplicantsTemp> listApplicants = new ArrayList<ApplicantsTemp>();
    private ArrayList<PerformanceTemp> listPerformance = new ArrayList<PerformanceTemp>();
    private ArrayList<PerformanceTemp> listNextYearData = new ArrayList<PerformanceTemp>();
    private ArrayList<PerformanceTemp> listPreviousYearData = new ArrayList<PerformanceTemp>();
    private ArrayList<AssetsAllocationTemp> listMicroStrategic = new ArrayList<AssetsAllocationTemp>();
    private ArrayList<AssetsAllocationTemp> listMacroStrategic = new ArrayList<AssetsAllocationTemp>();
    private ArrayList<AssetsAllocationTemp> listMicroTactical = new ArrayList<AssetsAllocationTemp>();
    private ArrayList<AssetsAllocationTemp> listMacroTactical = new ArrayList<AssetsAllocationTemp>();

    public static Handler handler;

    public DashBoardFragment()
    {
        // Required empty public constructor
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        sessionManager = new SessionManager(activity);
        apiService = PortfolioApiClient.getClient().create(PortfolioApiInterface.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.dashboard_fragment, container, false);
        initViews();

        handler = new Handler(message -> {
            try
            {
                if (message.what == 111)
                {
                    listPerformance = (ArrayList<PerformanceTemp>) message.obj;
                    setUpPerformanceData();
                    Log.e("ExampleJobServiceFF  : ", listPerformance.size() + " Performance <><>");
                }
                else if (message.what == 222)
                {
                    listNextYearData = (ArrayList<PerformanceTemp>) message.obj;
                    setUpNextYearData();
                    Log.e("ExampleJobServiceFF  : ", listNextYearData.size() + " NextYear<><>");
                }
                else if (message.what == 333)
                {
                    listPreviousYearData = (ArrayList<PerformanceTemp>) message.obj;
                    setUpPreviousYearData();
                    Log.e("ExampleJobServiceFF  : ", listPreviousYearData.size() + " PreviousYear<><>");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return false;
        });

        return binding.getRoot();
    }

    @SuppressLint("SimpleDateFormat")
    private void initViews()
    {
        try
        {
            if (AppUtils.isOnline())
            {
                binding.noInternet.llNoInternet.setVisibility(View.GONE);
                getNetworthData();
            }
            else
            {
                binding.noInternet.llNoInternet.setVisibility(View.VISIBLE);
            }

            DateFormat dateFormat = new SimpleDateFormat("MM");
            Date date = new Date();
            int currentMonth = Integer.parseInt(dateFormat.format(date));

            DateFormat dateFormatYear = new SimpleDateFormat("yyyy");

            int currentYear = Integer.parseInt(dateFormatYear.format(date));

            String currentFinYear = "";
            String pervFinYear = "";
            if (currentMonth > 3)
            {
                currentFinYear = currentYear + "-" +  AppUtils.universalDateConvert(String.valueOf(currentYear + 1),"yyyy","yy");
                pervFinYear = currentYear - 1 + "-" + AppUtils.universalDateConvert(String.valueOf(currentYear),"yyyy","yy");
            }
            else
            {
                currentFinYear = (currentYear - 1) + "-" + AppUtils.universalDateConvert(String.valueOf(currentYear),"yyyy","yy");
                pervFinYear = (currentYear - 2) + "-" + AppUtils.universalDateConvert(String.valueOf(currentYear - 1),"yyyy","yy");
            }

            Log.e("Current Year ===== ", currentFinYear);
            Log.e("Current Year ===== ", pervFinYear);

            binding.txtNextYear.setText(currentFinYear);
            binding.txtPreviousYear.setText(pervFinYear);

            setUpPerformanceData();
            setUpNextYearData();
            setUpPreviousYearData();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void getNetworthData()
    {
        try
        {
            listAssetAllocation = new ArrayList<>();
            binding.pbNetworthTotal.setVisibility(View.VISIBLE);
            binding.pbAssetAllocation.setVisibility(View.VISIBLE);
            binding.pbAssetAllocationStrategic.setVisibility(View.VISIBLE);
            binding.pbAssetAllocationTactical.setVisibility(View.VISIBLE);

            apiService.getNetworthNewAPIData(ApiUtils.END_USER_ID).enqueue(new Callback<NetworthTempData>()
            {
                @Override public void onResponse(Call<NetworthTempData> call, Response<NetworthTempData> response)
                {
                    if (response.isSuccessful())
                    {
                        if (response.body().getSuccess() == 1)
                        {
                            try
                            {
                                binding.txtTotalNetworth.setVisibility(View.VISIBLE);
                                binding.txtTotalNetworth.setText(getString(R.string.rs) + " " + AppUtils.convertToCommaSeperatedValue(String.valueOf(response.body().getAssetsData().getAssetsDetailsGrandtotal().getGrandTotalAmount())));

                                //Asset Allocation
                                if (response.body().getMacroTactical().getMacroAssetsTactical().size() > 0)
                                {
                                    AssetsAllocationTemp assetsAllocationTemp = new AssetsAllocationTemp();
                                    assetsAllocationTemp.setAssetName("Assets Class");
                                    assetsAllocationTemp.setActual_amount("Actual Amount");
                                    assetsAllocationTemp.setActual_percentage("Allocation %");
                                    assetsAllocationTemp.setPolicy("Policy %");
                                    assetsAllocationTemp.setVariance("Variance");
                                    listAssetAllocation.add(assetsAllocationTemp);

                                    for (int i = 0; i < response.body().getMacroTactical().getMacroAssetsTactical().size(); i++)
                                    {
                                        AssetsAllocationTemp assetsAllocationTemp1 = new AssetsAllocationTemp();
                                        assetsAllocationTemp1.setAssetName(response.body().getMacroTactical().getMacroAssetsTactical().get(i).getAssetClass());
                                        assetsAllocationTemp1.setActual_amount(AppUtils.convertToCommaSeperatedValue(String.valueOf(response.body().getMacroTactical().getMacroAssetsTactical().get(i).getActualAmount())));
                                        assetsAllocationTemp1.setActual_percentage(Math.round(response.body().getMacroTactical().getMacroAssetsTactical().get(i).getActualPercentage()) + "");
                                        assetsAllocationTemp1.setPolicy(Math.round(response.body().getMacroTactical().getMacroAssetsTactical().get(i).getPolicy()) + " %");
                                        assetsAllocationTemp1.setVariance(Math.round(response.body().getMacroTactical().getMacroAssetsTactical().get(i).getVariance()) + " %");
                                        listAssetAllocation.add(assetsAllocationTemp1);
                                    }

                                    AssetsAllocationTemp assetsAllocationTemp2 = new AssetsAllocationTemp();
                                    assetsAllocationTemp2.setAssetName("Total");
                                    assetsAllocationTemp2.setActual_amount(AppUtils.convertToCommaSeperatedValue(String.valueOf(response.body().getMacroTactical().getMacroAssetsTacticalGrandtotal().getGrandTotalAmount())));
                                    assetsAllocationTemp2.setActual_percentage(Math.round(response.body().getMacroTactical().getMacroAssetsTacticalGrandtotal().getGrandPercentageAmount()) + "");
                                    assetsAllocationTemp2.setPolicy(Math.round(response.body().getMacroTactical().getMacroAssetsTacticalGrandtotal().getMacroAssetsTacticalPolicy()) + " %");
                                    assetsAllocationTemp2.setVariance(Math.round(response.body().getMacroTactical().getMacroAssetsTacticalGrandtotal().getMacroAssetsTacticalVariance()) + " %");
                                    listAssetAllocation.add(assetsAllocationTemp2);

                                }

                                if (listAssetAllocation.size() > 0)
                                {
                                    binding.llMainAssetAllocation.setVisibility(View.VISIBLE);
                                    binding.cvAssetAllocation.setVisibility(View.GONE);
                                    pieChartDataSetUp("Asset Allocation", binding.pieAssetAllocation);
                                }
                                else
                                {
                                    binding.llMainAssetAllocation.setVisibility(View.GONE);
                                }

                                //Asset Allocation - Strategic Macro
                                if (response.body().getMacroStrategic().getMacroAssetsStrategic().size() > 0)
                                {
                                    AssetsAllocationTemp assetsAllocationTemp = new AssetsAllocationTemp();
                                    assetsAllocationTemp.setAssetName("Assets");
                                    assetsAllocationTemp.setActual_amount("Actual Amount");
                                    assetsAllocationTemp.setActual_percentage("Actual %");
                                    assetsAllocationTemp.setPolicy("Policy %");
                                    assetsAllocationTemp.setVariance("Variance");
                                    listMacroStrategic.add(assetsAllocationTemp);

                                    for (int i = 0; i < response.body().getMacroStrategic().getMacroAssetsStrategic().size(); i++)
                                    {
                                        AssetsAllocationTemp assetsAllocationTemp1 = new AssetsAllocationTemp();
                                        assetsAllocationTemp1.setAssetName(response.body().getMacroStrategic().getMacroAssetsStrategic().get(i).getAssetClass());
                                        assetsAllocationTemp1.setActual_amount(AppUtils.convertToCommaSeperatedValue(String.valueOf(response.body().getMacroStrategic().getMacroAssetsStrategic().get(i).getActualAmount())));
                                        assetsAllocationTemp1.setActual_percentage(Math.round(response.body().getMacroStrategic().getMacroAssetsStrategic().get(i).getActualPercentage()) + "");
                                        assetsAllocationTemp1.setPolicy(Math.round(response.body().getMacroStrategic().getMacroAssetsStrategic().get(i).getPolicy()) + " %");
                                        assetsAllocationTemp1.setVariance(Math.round(response.body().getMacroStrategic().getMacroAssetsStrategic().get(i).getVariance()) + " %");
                                        listMacroStrategic.add(assetsAllocationTemp1);
                                    }

                                    AssetsAllocationTemp assetsAllocationTemp2 = new AssetsAllocationTemp();
                                    assetsAllocationTemp2.setAssetName("Total");
                                    assetsAllocationTemp2.setActual_amount(AppUtils.convertToCommaSeperatedValue(String.valueOf(response.body().getMacroStrategic().getMacroAssetsStrategicGrandtotal().getGrandTotalAmount())));
                                    assetsAllocationTemp2.setActual_percentage(Math.round(response.body().getMacroStrategic().getMacroAssetsStrategicGrandtotal().getGrandPercentageAmount()) + "");
                                    assetsAllocationTemp2.setPolicy(Math.round(response.body().getMacroStrategic().getMacroAssetsStrategicGrandtotal().getMacroAssetsStrategicPolicy()) + " %");
                                    assetsAllocationTemp2.setVariance(Math.round(response.body().getMacroStrategic().getMacroAssetsStrategicGrandtotal().getMacroAssetsStrategicVariance()) + " %");
                                    listMacroStrategic.add(assetsAllocationTemp2);

                                }

                                if (listMacroStrategic.size() > 0)
                                {
                                    binding.rvAssetAllocationStrategic.setAdapter(new AssetAllocationAdapter(listMacroStrategic));
                                }

                                //Asset Allocation - Strategic Micro
                                if (response.body().getMicroStrategic().getMicroAssetsStrategic().size() > 0)
                                {
                                    AssetsAllocationTemp assetsAllocationTemp = new AssetsAllocationTemp();
                                    assetsAllocationTemp.setAssetName("Assets");
                                    assetsAllocationTemp.setActual_amount("Actual Amount");
                                    assetsAllocationTemp.setActual_percentage("Actual %");
                                    assetsAllocationTemp.setPolicy("Policy %");
                                    assetsAllocationTemp.setVariance("Variance");
                                    listMicroStrategic.add(assetsAllocationTemp);

                                    for (int i = 0; i < response.body().getMicroStrategic().getMicroAssetsStrategic().size(); i++)
                                    {
                                        AssetsAllocationTemp assetsAllocationTemp1 = new AssetsAllocationTemp();
                                        assetsAllocationTemp1.setAssetName(response.body().getMicroStrategic().getMicroAssetsStrategic().get(i).getAssetClass());
                                        assetsAllocationTemp1.setActual_amount(AppUtils.convertToCommaSeperatedValue(String.valueOf(response.body().getMicroStrategic().getMicroAssetsStrategic().get(i).getActualAmount())));
                                        assetsAllocationTemp1.setActual_percentage(Math.round(response.body().getMicroStrategic().getMicroAssetsStrategic().get(i).getActualPercentage()) + "");
                                        assetsAllocationTemp1.setPolicy(Math.round(response.body().getMicroStrategic().getMicroAssetsStrategic().get(i).getPolicy()) + " %");
                                        assetsAllocationTemp1.setVariance(Math.round(response.body().getMicroStrategic().getMicroAssetsStrategic().get(i).getVariance()) + " %");
                                        listMicroStrategic.add(assetsAllocationTemp1);
                                    }

                                    AssetsAllocationTemp assetsAllocationTemp2 = new AssetsAllocationTemp();
                                    assetsAllocationTemp2.setAssetName("Total");
                                    assetsAllocationTemp2.setActual_amount(AppUtils.convertToCommaSeperatedValue(String.valueOf(response.body().getMicroStrategic().getMicroAssetsStrategicGrandtotal().getGrandTotalAmount())));
                                    assetsAllocationTemp2.setActual_percentage(Math.round(response.body().getMicroStrategic().getMicroAssetsStrategicGrandtotal().getGrandPercentageAmount()) + "");
                                    assetsAllocationTemp2.setPolicy(Math.round(response.body().getMicroStrategic().getMicroAssetsStrategicGrandtotal().getMicroAssetsStrategicPolicy()) + " %");
                                    assetsAllocationTemp2.setVariance(Math.round(response.body().getMicroStrategic().getMicroAssetsStrategicGrandtotal().getMicroAssetsStrategicVarianceTotal()) + " %");
                                    listMicroStrategic.add(assetsAllocationTemp2);

                                }
                                else
                                {
                                    binding.llAssetStrategic.setVisibility(View.GONE);
                                }

                                //Asset Allocation - Tactical Macro
                                if (response.body().getMacroTactical().getMacroAssetsTactical().size() > 0)
                                {
                                    AssetsAllocationTemp assetsAllocationTemp = new AssetsAllocationTemp();
                                    assetsAllocationTemp.setAssetName("Assets");
                                    assetsAllocationTemp.setActual_amount("Actual Amount");
                                    assetsAllocationTemp.setActual_percentage("Actual %");
                                    assetsAllocationTemp.setPolicy("Policy %");
                                    assetsAllocationTemp.setVariance("Variance");
                                    listMacroTactical.add(assetsAllocationTemp);

                                    for (int i = 0; i < response.body().getMacroTactical().getMacroAssetsTactical().size(); i++)
                                    {
                                        AssetsAllocationTemp assetsAllocationTemp1 = new AssetsAllocationTemp();
                                        assetsAllocationTemp1.setAssetName(response.body().getMacroTactical().getMacroAssetsTactical().get(i).getAssetClass());
                                        assetsAllocationTemp1.setActual_amount(AppUtils.convertToCommaSeperatedValue(String.valueOf(response.body().getMacroTactical().getMacroAssetsTactical().get(i).getActualAmount())));
                                        assetsAllocationTemp1.setActual_percentage(Math.round(response.body().getMacroTactical().getMacroAssetsTactical().get(i).getActualPercentage()) + "");
                                        assetsAllocationTemp1.setPolicy(Math.round(response.body().getMacroTactical().getMacroAssetsTactical().get(i).getPolicy()) + " %");
                                        assetsAllocationTemp1.setVariance(Math.round(response.body().getMacroTactical().getMacroAssetsTactical().get(i).getVariance()) + " %");
                                        listMacroTactical.add(assetsAllocationTemp1);
                                    }

                                    AssetsAllocationTemp assetsAllocationTemp2 = new AssetsAllocationTemp();
                                    assetsAllocationTemp2.setAssetName("Total");
                                    assetsAllocationTemp2.setActual_amount(AppUtils.convertToCommaSeperatedValue(String.valueOf(response.body().getMacroTactical().getMacroAssetsTacticalGrandtotal().getGrandTotalAmount())));
                                    assetsAllocationTemp2.setActual_percentage(Math.round(response.body().getMacroTactical().getMacroAssetsTacticalGrandtotal().getGrandPercentageAmount()) + "");
                                    assetsAllocationTemp2.setPolicy(Math.round(response.body().getMacroTactical().getMacroAssetsTacticalGrandtotal().getMacroAssetsTacticalPolicy()) + " %");
                                    assetsAllocationTemp2.setVariance(Math.round(response.body().getMacroTactical().getMacroAssetsTacticalGrandtotal().getMacroAssetsTacticalVariance()) + " %");
                                    listMacroTactical.add(assetsAllocationTemp2);
                                }

                                if (listMacroTactical.size() > 0)
                                {
                                    binding.rvAssetAllocationTactical.setAdapter(new AssetAllocationAdapter(listMacroTactical));
                                }

                                //Asset Allocation - Tactical Micro
                                if (response.body().getMicroTactical().getMicroAssetsTactical().size() > 0)
                                {
                                    AssetsAllocationTemp assetsAllocationTemp = new AssetsAllocationTemp();
                                    assetsAllocationTemp.setAssetName("Assets");
                                    assetsAllocationTemp.setActual_amount("Actual Amount");
                                    assetsAllocationTemp.setActual_percentage("Actual %");
                                    assetsAllocationTemp.setPolicy("Policy %");
                                    assetsAllocationTemp.setVariance("Variance");
                                    listMicroTactical.add(assetsAllocationTemp);

                                    for (int i = 0; i < response.body().getMicroTactical().getMicroAssetsTactical().size(); i++)
                                    {
                                        AssetsAllocationTemp assetsAllocationTemp1 = new AssetsAllocationTemp();
                                        assetsAllocationTemp1.setAssetName(response.body().getMicroTactical().getMicroAssetsTactical().get(i).getAssetClass());
                                        assetsAllocationTemp1.setActual_amount(AppUtils.convertToCommaSeperatedValue(String.valueOf(response.body().getMicroTactical().getMicroAssetsTactical().get(i).getActualAmount())));
                                        assetsAllocationTemp1.setActual_percentage(Math.round(response.body().getMicroTactical().getMicroAssetsTactical().get(i).getActualPercentage()) + "");
                                        assetsAllocationTemp1.setPolicy(Math.round(response.body().getMicroTactical().getMicroAssetsTactical().get(i).getPolicy()) + " %");
                                        assetsAllocationTemp1.setVariance(Math.round(response.body().getMicroTactical().getMicroAssetsTactical().get(i).getVariance()) + " %");
                                        listMicroTactical.add(assetsAllocationTemp1);
                                    }

                                    AssetsAllocationTemp assetsAllocationTemp2 = new AssetsAllocationTemp();
                                    assetsAllocationTemp2.setAssetName("Total");
                                    assetsAllocationTemp2.setActual_amount(AppUtils.convertToCommaSeperatedValue(String.valueOf(response.body().getMicroTactical().getMicroAssetsTacticalGrandtotal().getGrandTotalAmount())));
                                    assetsAllocationTemp2.setActual_percentage(Math.round(response.body().getMicroTactical().getMicroAssetsTacticalGrandtotal().getGrandPercentageAmount()) + "");
                                    assetsAllocationTemp2.setPolicy(Math.round(response.body().getMicroTactical().getMicroAssetsTacticalGrandtotal().getMicroAssetsTacticalPolicy()) + " %");
                                    assetsAllocationTemp2.setVariance(Math.round(response.body().getMicroTactical().getMicroAssetsTacticalGrandtotal().getMicroAssetsTacticalVarianceTotal()) + " %");
                                    listMicroTactical.add(assetsAllocationTemp2);

                                }
                                else
                                {
                                    binding.llAssetTactical.setVisibility(View.GONE);
                                }

                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                            binding.pbNetworthTotal.setVisibility(View.GONE);
                            binding.pbAssetAllocation.setVisibility(View.GONE);
                            binding.pbAssetAllocationStrategic.setVisibility(View.GONE);
                            binding.pbAssetAllocationTactical.setVisibility(View.GONE);
                        }
                        else
                        {
                            binding.pbNetworthTotal.setVisibility(View.GONE);
                            binding.pbAssetAllocation.setVisibility(View.GONE);
                            binding.pbAssetAllocationStrategic.setVisibility(View.GONE);
                            binding.pbAssetAllocationTactical.setVisibility(View.GONE);
                        }
                    }

                    setUpClickNetworth();

                    if (AppUtils.isOnline())
                    {
                        binding.noInternet.llNoInternet.setVisibility(View.GONE);
                        getPercentageData();
                        getApplicantListApi();
                        //  getPerformanceActivityData();

                    }
                    else
                    {
                        binding.noInternet.llNoInternet.setVisibility(View.VISIBLE);
                    }
                }

                @Override public void onFailure(Call<NetworthTempData> call, Throwable t)
                {
                    AppUtils.printLog("FAIL 1", t.getMessage());

                    if (AppUtils.isOnline())
                    {
                        binding.noInternet.llNoInternet.setVisibility(View.GONE);
                        //  getPerformanceActivityData();
                    }
                    else
                    {
                        binding.noInternet.llNoInternet.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void getApplicantListApi()
    {
        binding.pbApplicantsDetails.setVisibility(View.VISIBLE);

        apiService.getApplicantListAPI(ApiUtils.END_USER_ID).enqueue(new Callback<ApplicantListResponseModel>()
        {
            @Override public void onResponse(Call<ApplicantListResponseModel> call, Response<ApplicantListResponseModel> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().getSuccess() == 1)
                    {
                        binding.pbApplicantsDetails.setVisibility(View.GONE);

                        if (response.body().getApplicants_list().getApplicants().size() > 0)
                        {
                            binding.llApplicantsDetails.setVisibility(View.VISIBLE);

                            ApplicantsTemp applicantsTemp = new ApplicantsTemp();
                            applicantsTemp.setName("Name");
                            applicantsTemp.setAmount_invested("Amount Invested");
                            applicantsTemp.setCurrent_amount("Current Amount");
                            applicantsTemp.setGain("Gain");
                            applicantsTemp.setAbs_return("Absolute Return");
                            applicantsTemp.setCagr("Allocation %");
                            listApplicants.add(applicantsTemp);

                            for (int i = 0; i < response.body().getApplicants_list().getApplicants().size(); i++)
                            {
                                ApplicantsTemp applicantsTem1 = new ApplicantsTemp();
                                applicantsTem1.setName(response.body().getApplicants_list().getApplicants().get(i).getApplicantName());
                                applicantsTem1.setAmount_invested(AppUtils.convertToCommaSeperatedValue(String.valueOf(response.body().getApplicants_list().getApplicants().get(i).getInitialVal())));
                                applicantsTem1.setCurrent_amount(AppUtils.convertToCommaSeperatedValue(String.valueOf(response.body().getApplicants_list().getApplicants().get(i).getCurrentVal())) + "");
                                applicantsTem1.setGain(AppUtils.convertToCommaSeperatedValue(String.valueOf(response.body().getApplicants_list().getApplicants().get(i).getGain())));
                                applicantsTem1.setAbs_return(Math.round(Float.parseFloat(response.body().getApplicants_list().getApplicants().get(i).getAbsoluteReturn())) + " %");
                                applicantsTem1.setCagr(Math.round(Float.parseFloat(String.valueOf(response.body().getApplicants_list().getApplicants().get(i).getPercentage()))) + "");
                                listApplicants.add(applicantsTem1);
                            }

                            ApplicantsTemp applicantsTem2 = new ApplicantsTemp();
                            applicantsTem2.setName("Total");
                            applicantsTem2.setAmount_invested(AppUtils.convertToCommaSeperatedValue(String.valueOf(response.body().getApplicants_list().getTotal().getInitialVal())));
                            applicantsTem2.setCurrent_amount(AppUtils.convertToCommaSeperatedValue(String.valueOf(response.body().getApplicants_list().getTotal().getCurrentVal())) + "");
                            applicantsTem2.setGain(AppUtils.convertToCommaSeperatedValue(String.valueOf(response.body().getApplicants_list().getTotal().getGain())));

                            if (response.body().getApplicants_list().getTotal().getAbsoluteReturn().length() > 0)
                            {
                                applicantsTem2.setAbs_return(Math.round(Float.parseFloat(response.body().getApplicants_list().getTotal().getAbsoluteReturn())) + " %");
                            }
                            else
                            {
                                applicantsTem2.setAbs_return("");
                            }

                            if (response.body().getApplicants_list().getTotal().getAbsoluteReturn().length() > 0)
                            {
                                applicantsTem2.setCagr(Math.round(Float.parseFloat(response.body().getApplicants_list().getTotal().getCAGR())) + " %");
                            }
                            else
                            {
                                applicantsTem2.setCagr("");
                            }

                            listApplicants.add(applicantsTem2);

                        }

                        if (listApplicants.size() > 0)
                        {
                            binding.cvApplicantsDetails.setVisibility(View.GONE);
                            pieChartDataSetUp("Applicants Allocation", binding.pieApplicantsDetails);
                            setUpClickApplicant();
                        }
                    }
                    else
                    {
                        binding.pbApplicantsDetails.setVisibility(View.GONE);
                        binding.llApplicantsDetails.setVisibility(View.GONE);
                    }
                }
                else
                {
                    binding.pbApplicantsDetails.setVisibility(View.GONE);
                    binding.llApplicantsDetails.setVisibility(View.GONE);
                }
            }

            @Override public void onFailure(Call<ApplicantListResponseModel> call, Throwable t)
            {
                AppUtils.printLog("Failure Applicant", t.getMessage());
                binding.pbApplicantsDetails.setVisibility(View.GONE);
                binding.llApplicantsDetails.setVisibility(View.GONE);
            }
        });
    }

    private void getPercentageData()
    {
        apiService.getPercentage().enqueue(new Callback<GetPercentageResponse>()
        {
            @Override public void onResponse(Call<GetPercentageResponse> call, Response<GetPercentageResponse> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().getSuccess() == 1)
                    {
                        try
                        {
                            if (response.body().getMaster_market_percentage().length() > 0)
                            {
                                binding.txtAllocationTacticalDescription.setVisibility(View.VISIBLE);
                                binding.txtAllocationTacticalDescription.setText("*Equity market is overvalued by " + response.body().getMaster_market_percentage().toString() + "%");
                            }
                            else
                            {
                                binding.txtAllocationTacticalDescription.setVisibility(View.GONE);
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override public void onFailure(Call<GetPercentageResponse> call, Throwable t)
            {
                AppUtils.printLog("Failur getPercentage ", t.getMessage());
            }
        });
    }

    private void setUpClickNetworth()
    {
        binding.txtGraphAssetAllocation.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View view)
            {
                try
                {
                    if (binding.txtGraphAssetAllocation.getText().equals("Graph"))
                    {
                        binding.txtGraphAssetAllocation.setText("Table");
                        binding.cvAssetAllocation.setVisibility(View.GONE);
                        binding.pieAssetAllocation.setVisibility(View.VISIBLE);
                        pieChartDataSetUp("Asset Allocation", binding.pieAssetAllocation);
                    }
                    else
                    {
                        binding.txtGraphAssetAllocation.setText("Graph");
                        binding.cvAssetAllocation.setVisibility(View.VISIBLE);
                        binding.pieAssetAllocation.setVisibility(View.GONE);
                        if (listAssetAllocation.size() > 0)
                        {
                            binding.rvAssetAllocation.setAdapter(new AssetAllocationMainAdapter(listAssetAllocation));
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        binding.llMicroStrategic.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View view)
            {
                try
                {
                    binding.txtMicroStrategic.setTextColor(ContextCompat.getColor(activity, R.color.blue_new));
                    binding.viewLineMicroStrategic.setVisibility(View.VISIBLE);

                    binding.txtMacroStrategic.setTextColor(ContextCompat.getColor(activity, R.color.black));
                    binding.viewLineMacroStrategic.setVisibility(View.GONE);

                    if (listMicroStrategic.size() > 0)
                    {
                        binding.rvAssetAllocationStrategic.setAdapter(new AssetAllocationAdapter(listMicroStrategic));
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        binding.llMacroStrategic.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View view)
            {
                try
                {
                    binding.txtMacroStrategic.setTextColor(ContextCompat.getColor(activity, R.color.blue_new));
                    binding.viewLineMacroStrategic.setVisibility(View.VISIBLE);

                    binding.txtMicroStrategic.setTextColor(ContextCompat.getColor(activity, R.color.black));
                    binding.viewLineMicroStrategic.setVisibility(View.GONE);

                    if (listMacroStrategic.size() > 0)
                    {
                        binding.rvAssetAllocationStrategic.setAdapter(new AssetAllocationAdapter(listMacroStrategic));
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


            }
        });


        binding.llMicroTactical.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View view)
            {
                try
                {
                    binding.txtMicroTactical.setTextColor(ContextCompat.getColor(activity, R.color.blue_new));
                    binding.viewLineMicroTactical.setVisibility(View.VISIBLE);

                    binding.txtMacroTactical.setTextColor(ContextCompat.getColor(activity, R.color.black));
                    binding.viewLineMacroTactical.setVisibility(View.GONE);

                    if (listMicroTactical.size() > 0)
                    {
                        binding.rvAssetAllocationTactical.setAdapter(new AssetAllocationAdapter(listMicroTactical));
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        binding.llMacroTactical.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View view)
            {
                try
                {
                    binding.txtMacroTactical.setTextColor(ContextCompat.getColor(activity, R.color.blue_new));
                    binding.viewLineMacroTactical.setVisibility(View.VISIBLE);

                    binding.txtMicroTactical.setTextColor(ContextCompat.getColor(activity, R.color.black));
                    binding.viewLineMicroTactical.setVisibility(View.GONE);

                    if (listMacroTactical.size() > 0)
                    {
                        binding.rvAssetAllocationTactical.setAdapter(new AssetAllocationAdapter(listMacroTactical));
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setUpClickApplicant()
    {
        binding.txtApplicantsDetails.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View view)
            {
                try
                {
                    if (binding.txtApplicantsDetails.getText().equals("Graph"))
                    {
                        binding.txtApplicantsDetails.setText("Table");
                        binding.cvApplicantsDetails.setVisibility(View.GONE);
                        binding.pieApplicantsDetails.setVisibility(View.VISIBLE);
                        pieChartDataSetUp("Applicants Allocation", binding.pieApplicantsDetails);
                    }
                    else
                    {
                        binding.txtApplicantsDetails.setText("Graph");
                        binding.cvApplicantsDetails.setVisibility(View.VISIBLE);
                        binding.pieApplicantsDetails.setVisibility(View.GONE);
                        if (listApplicants.size() > 0)
                        {
                            binding.rvApplicantsDetails.setAdapter(new ApplicantsAdapter(listApplicants));
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    private class AssetAllocationMainAdapter extends RecyclerView.Adapter<AssetAllocationMainAdapter.ViewHolder>
    {
        ArrayList<AssetsAllocationTemp> listItem;

        AssetAllocationMainAdapter(ArrayList<AssetsAllocationTemp> listData)
        {
            this.listItem = listData;
        }

        @Override public AssetAllocationMainAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.portfolio_row_view_asset_allocation_temp, viewGroup, false);
            return new AssetAllocationMainAdapter.ViewHolder(v);
        }

        @Override public void onBindViewHolder(final AssetAllocationMainAdapter.ViewHolder holder, final int position)
        {
            try
            {
                final AssetsAllocationTemp getSet = listItem.get(position);

                if (position == 0)
                {
                    holder.txtAssestType.setTypeface(AppUtils.getTypefaceBold(activity));
                    holder.txtAssestType.setTextColor(ContextCompat.getColor(activity, R.color.black));

                    holder.txtInvested.setTypeface(AppUtils.getTypefaceBold(activity));
                    holder.txtInvested.setTextColor(ContextCompat.getColor(activity, R.color.black));

                    holder.txtCurrent.setTypeface(AppUtils.getTypefaceBold(activity));
                    holder.txtCurrent.setTextColor(ContextCompat.getColor(activity, R.color.black));

                }
                else if (position == listItem.size() - 1)
                {
                    holder.txtAssestType.setTypeface(AppUtils.getTypefaceBold(activity));
                    holder.txtAssestType.setTextColor(ContextCompat.getColor(activity, R.color.black));

                    holder.txtInvested.setTypeface(AppUtils.getTypefaceBold(activity));
                    holder.txtInvested.setTextColor(ContextCompat.getColor(activity, R.color.black));

                    holder.txtCurrent.setTypeface(AppUtils.getTypefaceBold(activity));
                    holder.txtCurrent.setTextColor(ContextCompat.getColor(activity, R.color.black));
                }
                else
                {
                    holder.txtAssestType.setTypeface(AppUtils.getTypefaceBold(activity));
                    holder.txtAssestType.setTextColor(ContextCompat.getColor(activity, R.color.black));

                    holder.txtInvested.setTypeface(AppUtils.getTypefaceRegular(activity));
                    holder.txtInvested.setTextColor(ContextCompat.getColor(activity, R.color.black));

                    holder.txtCurrent.setTypeface(AppUtils.getTypefaceRegular(activity));
                    holder.txtCurrent.setTextColor(ContextCompat.getColor(activity, R.color.black));
                }

                holder.txtAssestType.setText(AppUtils.toDisplayCase(getSet.getAssetName()));
                holder.txtInvested.setText(getSet.getActual_amount());

                if (position == 0)
                {
                    holder.txtCurrent.setText(getSet.getActual_percentage());
                }
                else
                {
                    holder.txtCurrent.setText(getSet.getActual_percentage() + " %");
                }

                if (position == listItem.size() - 1)
                {
                    holder.viewLineBottom.setVisibility(View.GONE);
                }
                else
                {
                    holder.viewLineBottom.setVisibility(View.VISIBLE);
                }

            }
            catch (Resources.NotFoundException e)
            {
                e.printStackTrace();
            }
        }

        @Override public int getItemCount()
        {
            return listItem.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            private TextView txtAssestType, txtInvested, txtCurrent;
            private View viewLineBottom;

            ViewHolder(View convertView)
            {
                super(convertView);
                txtAssestType = convertView.findViewById(R.id.txtAssestType);
                txtInvested = convertView.findViewById(R.id.txtInvested);
                txtCurrent = convertView.findViewById(R.id.txtCurrent);
                viewLineBottom = convertView.findViewById(R.id.viewLineBottom);
            }
        }
    }

    private class AssetAllocationAdapter extends RecyclerView.Adapter<AssetAllocationAdapter.ViewHolder>
    {
        ArrayList<AssetsAllocationTemp> listItem;

        AssetAllocationAdapter(ArrayList<AssetsAllocationTemp> listData)
        {
            this.listItem = listData;
        }

        @Override public AssetAllocationAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.portfolio_row_view_performance_temp, viewGroup, false);
            return new AssetAllocationAdapter.ViewHolder(v);
        }

        @Override public void onBindViewHolder(final AssetAllocationAdapter.ViewHolder holder, final int position)
        {
            try
            {
                final AssetsAllocationTemp getSet = listItem.get(position);

                if (position == 0)
                {
                    holder.txtAssestType.setTypeface(AppUtils.getTypefaceBold(activity));
                    holder.txtAssestType.setTextColor(ContextCompat.getColor(activity, R.color.black));

                    holder.txtInvested.setTypeface(AppUtils.getTypefaceBold(activity));
                    holder.txtInvested.setTextColor(ContextCompat.getColor(activity, R.color.black));

                    holder.txtCurrent.setTypeface(AppUtils.getTypefaceBold(activity));
                    holder.txtCurrent.setTextColor(ContextCompat.getColor(activity, R.color.black));

                    holder.txtGain.setTypeface(AppUtils.getTypefaceBold(activity));
                    holder.txtGain.setTextColor(ContextCompat.getColor(activity, R.color.black));

                    holder.txtXIRR.setTypeface(AppUtils.getTypefaceBold(activity));
                    holder.txtXIRR.setTextColor(ContextCompat.getColor(activity, R.color.black));


                }
                else if (position == listItem.size() - 1)
                {
                    holder.txtAssestType.setTypeface(AppUtils.getTypefaceBold(activity));
                    holder.txtAssestType.setTextColor(ContextCompat.getColor(activity, R.color.black));

                    holder.txtInvested.setTypeface(AppUtils.getTypefaceBold(activity));
                    holder.txtInvested.setTextColor(ContextCompat.getColor(activity, R.color.black));

                    holder.txtCurrent.setTypeface(AppUtils.getTypefaceBold(activity));
                    holder.txtCurrent.setTextColor(ContextCompat.getColor(activity, R.color.black));

                    holder.txtGain.setTypeface(AppUtils.getTypefaceBold(activity));
                    holder.txtGain.setTextColor(ContextCompat.getColor(activity, R.color.black));

                    holder.txtXIRR.setTypeface(AppUtils.getTypefaceBold(activity));
                    holder.txtXIRR.setTextColor(ContextCompat.getColor(activity, R.color.black));

                }
                else
                {
                    holder.txtAssestType.setTypeface(AppUtils.getTypefaceBold(activity));
                    holder.txtAssestType.setTextColor(ContextCompat.getColor(activity, R.color.black));

                    holder.txtInvested.setTypeface(AppUtils.getTypefaceRegular(activity));
                    holder.txtInvested.setTextColor(ContextCompat.getColor(activity, R.color.black));

                    holder.txtCurrent.setTypeface(AppUtils.getTypefaceRegular(activity));
                    holder.txtCurrent.setTextColor(ContextCompat.getColor(activity, R.color.black));

                    holder.txtGain.setTypeface(AppUtils.getTypefaceRegular(activity));
                    holder.txtGain.setTextColor(ContextCompat.getColor(activity, R.color.black));

                    holder.txtXIRR.setTypeface(AppUtils.getTypefaceRegular(activity));
                    holder.txtXIRR.setTextColor(ContextCompat.getColor(activity, R.color.black));

                }

                holder.txtAssestType.setText(AppUtils.toDisplayCase(getSet.getAssetName()));
                holder.txtInvested.setText(getSet.getActual_amount());

                if (position == 0)
                {
                    holder.txtCurrent.setText(getSet.getActual_percentage());
                }
                else
                {
                    holder.txtCurrent.setText(getSet.getActual_percentage() + " %");
                }

                holder.txtGain.setText(getSet.getPolicy());
                holder.txtXIRR.setText(getSet.getVariance());

                if (position == listItem.size() - 1)
                {
                    holder.viewLineBottom.setVisibility(View.GONE);
                }
                else
                {
                    holder.viewLineBottom.setVisibility(View.VISIBLE);
                }

            }
            catch (Resources.NotFoundException e)
            {
                e.printStackTrace();
            }
        }

        @Override public int getItemCount()
        {
            return listItem.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            private TextView txtAssestType, txtInvested, txtCurrent, txtGain, txtXIRR;
            private View viewLineBottom;

            ViewHolder(View convertView)
            {
                super(convertView);
                txtAssestType = convertView.findViewById(R.id.txtAssestType);
                txtInvested = convertView.findViewById(R.id.txtInvested);
                txtCurrent = convertView.findViewById(R.id.txtCurrent);
                txtGain = convertView.findViewById(R.id.txtGain);
                txtXIRR = convertView.findViewById(R.id.txtXIRR);
                viewLineBottom = convertView.findViewById(R.id.viewLineBottom);
            }
        }
    }

    private class ApplicantsAdapter extends RecyclerView.Adapter<ApplicantsAdapter.ViewHolder>
    {
        ArrayList<ApplicantsTemp> listItem;

        ApplicantsAdapter(ArrayList<ApplicantsTemp> listData)
        {
            this.listItem = listData;
        }

        @Override public ApplicantsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.portfolio_row_view_applicant_temp, viewGroup, false);
            return new ApplicantsAdapter.ViewHolder(v);
        }

        @Override public void onBindViewHolder(final ApplicantsAdapter.ViewHolder holder, final int position)
        {
            try
            {
                final ApplicantsTemp getSet = listItem.get(position);

                if (position == 0)
                {
                    holder.txtAssestType.setTypeface(AppUtils.getTypefaceBold(activity));
                    holder.txtAssestType.setTextColor(ContextCompat.getColor(activity, R.color.black));

                    holder.txtCurrent.setTypeface(AppUtils.getTypefaceBold(activity));
                    holder.txtCurrent.setTextColor(ContextCompat.getColor(activity, R.color.black));

                    holder.txtXIRR.setTypeface(AppUtils.getTypefaceBold(activity));
                    holder.txtXIRR.setTextColor(ContextCompat.getColor(activity, R.color.black));

                }
                else if (position == listItem.size() - 1)
                {
                    holder.txtAssestType.setTypeface(AppUtils.getTypefaceBold(activity));
                    holder.txtAssestType.setTextColor(ContextCompat.getColor(activity, R.color.black));

                    holder.txtCurrent.setTypeface(AppUtils.getTypefaceBold(activity));
                    holder.txtCurrent.setTextColor(ContextCompat.getColor(activity, R.color.black));

                    holder.txtXIRR.setTypeface(AppUtils.getTypefaceBold(activity));
                    holder.txtXIRR.setTextColor(ContextCompat.getColor(activity, R.color.black));
                }
                else
                {
                    holder.txtAssestType.setTypeface(AppUtils.getTypefaceBold(activity));
                    holder.txtAssestType.setTextColor(ContextCompat.getColor(activity, R.color.black));

                    holder.txtCurrent.setTypeface(AppUtils.getTypefaceRegular(activity));
                    holder.txtCurrent.setTextColor(ContextCompat.getColor(activity, R.color.black));

                    holder.txtXIRR.setTypeface(AppUtils.getTypefaceRegular(activity));
                    holder.txtXIRR.setTextColor(ContextCompat.getColor(activity, R.color.black));
                }

                holder.txtAssestType.setText(AppUtils.toDisplayCase(getSet.getName()));
                holder.txtCurrent.setText(getSet.getCurrent_amount());
                if (position == 0)
                {
                    holder.txtXIRR.setText(getSet.getCagr());
                }
                else
                {
                    if (getSet.getPercentage() > 0)
                    {
                        holder.txtXIRR.setText(getSet.getPercentage() + " %");
                    }
                    else
                    {
                        holder.txtXIRR.setText("");
                    }

                }

                if (position == listItem.size() - 1)
                {
                    holder.viewLineBottom.setVisibility(View.GONE);
                }
                else
                {
                    holder.viewLineBottom.setVisibility(View.VISIBLE);
                }

            }
            catch (Resources.NotFoundException e)
            {
                e.printStackTrace();
            }
        }

        @Override public int getItemCount()
        {
            return listItem.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            private TextView txtAssestType, txtInvested, txtCurrent, txtGain, txtXIRR;
            private View viewLineBottom;

            ViewHolder(View convertView)
            {
                super(convertView);
                txtAssestType = convertView.findViewById(R.id.txtAssestType);
                txtInvested = convertView.findViewById(R.id.txtInvested);
                txtCurrent = convertView.findViewById(R.id.txtCurrent);
                txtGain = convertView.findViewById(R.id.txtGain);
                txtXIRR = convertView.findViewById(R.id.txtXIRR);
                viewLineBottom = convertView.findViewById(R.id.viewLineBottom);
            }
        }
    }

    private class PerformanceAdapter extends RecyclerView.Adapter<PerformanceAdapter.ViewHolder>
    {
        ArrayList<PerformanceTemp> listItem;

        PerformanceAdapter(ArrayList<PerformanceTemp> listData)
        {
            this.listItem = listData;
        }

        @Override public PerformanceAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.portfolio_row_view_performance_temp_new, viewGroup, false);
            return new PerformanceAdapter.ViewHolder(v);
        }

        @Override public void onBindViewHolder(final PerformanceAdapter.ViewHolder holder, final int position)
        {
            try
            {
                final PerformanceTemp getSet = listItem.get(position);

                if (position == 0)
                {
                    holder.txtAssestType.setTypeface(AppUtils.getTypefaceBold(activity));
                    holder.txtAssestType.setTextColor(ContextCompat.getColor(activity, R.color.black));

                    holder.txtInvested.setTypeface(AppUtils.getTypefaceBold(activity));
                    holder.txtInvested.setTextColor(ContextCompat.getColor(activity, R.color.black));

                    holder.txtCurrent.setTypeface(AppUtils.getTypefaceBold(activity));
                    holder.txtCurrent.setTextColor(ContextCompat.getColor(activity, R.color.black));

                    holder.txtGain.setTypeface(AppUtils.getTypefaceBold(activity));
                    holder.txtGain.setTextColor(ContextCompat.getColor(activity, R.color.black));

                    holder.txtXIRR.setTypeface(AppUtils.getTypefaceBold(activity));
                    holder.txtXIRR.setTextColor(ContextCompat.getColor(activity, R.color.black));


                }
                else if (position == listItem.size() - 1)
                {
                    holder.txtAssestType.setTypeface(AppUtils.getTypefaceBold(activity));
                    holder.txtAssestType.setTextColor(ContextCompat.getColor(activity, R.color.black));

                    holder.txtInvested.setTypeface(AppUtils.getTypefaceBold(activity));
                    holder.txtInvested.setTextColor(ContextCompat.getColor(activity, R.color.black));

                    holder.txtCurrent.setTypeface(AppUtils.getTypefaceBold(activity));
                    holder.txtCurrent.setTextColor(ContextCompat.getColor(activity, R.color.black));

                    holder.txtGain.setTypeface(AppUtils.getTypefaceBold(activity));
                    holder.txtGain.setTextColor(ContextCompat.getColor(activity, R.color.black));

                    holder.txtXIRR.setTypeface(AppUtils.getTypefaceBold(activity));
                    holder.txtXIRR.setTextColor(ContextCompat.getColor(activity, R.color.black));

                }
                else
                {
                    holder.txtAssestType.setTypeface(AppUtils.getTypefaceBold(activity));
                    holder.txtAssestType.setTextColor(ContextCompat.getColor(activity, R.color.black));

                    holder.txtInvested.setTypeface(AppUtils.getTypefaceRegular(activity));
                    holder.txtInvested.setTextColor(ContextCompat.getColor(activity, R.color.black));

                    holder.txtCurrent.setTypeface(AppUtils.getTypefaceRegular(activity));
                    holder.txtCurrent.setTextColor(ContextCompat.getColor(activity, R.color.black));

                    holder.txtGain.setTypeface(AppUtils.getTypefaceRegular(activity));
                    holder.txtGain.setTextColor(ContextCompat.getColor(activity, R.color.black));

                    holder.txtXIRR.setTypeface(AppUtils.getTypefaceRegular(activity));
                    holder.txtXIRR.setTextColor(ContextCompat.getColor(activity, R.color.black));

                }

                holder.txtAssestType.setText(AppUtils.toDisplayCase(getSet.getAssetName()));
                holder.txtInvested.setText(getSet.getInvested());
                holder.txtCurrent.setText(getSet.getCurrent());
                holder.txtGain.setText(getSet.getGain());

                if (position == 0)
                {
                    holder.txtXIRR.setText(getSet.getXIRR());
                }
                else
                {
                    holder.txtXIRR.setText(getSet.getXIRR() + " %");
                }

                if (position == listItem.size() - 1)
                {
                    holder.viewLineBottom.setVisibility(View.GONE);
                }
                else
                {
                    holder.viewLineBottom.setVisibility(View.VISIBLE);
                }
            }
            catch (Resources.NotFoundException e)
            {
                e.printStackTrace();
            }
        }

        @Override public int getItemCount()
        {
            return listItem.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            private TextView txtAssestType, txtInvested, txtCurrent, txtGain, txtXIRR;
            private View viewLineBottom;

            ViewHolder(View convertView)
            {
                super(convertView);
                txtAssestType = convertView.findViewById(R.id.txtAssestType);
                txtInvested = convertView.findViewById(R.id.txtInvested);
                txtCurrent = convertView.findViewById(R.id.txtCurrent);
                txtGain = convertView.findViewById(R.id.txtGain);
                txtXIRR = convertView.findViewById(R.id.txtXIRR);
                viewLineBottom = convertView.findViewById(R.id.viewLineBottom);
            }
        }
    }

    private void pieChartDataSetUp(String chartTitle, PieChart pieChart)
    {
        try
        {
            pieChart.setVisibility(View.VISIBLE);
            pieChart.setUsePercentValues(false);
            pieChart.getDescription().setEnabled(false);
            pieChart.setExtraOffsets(5, 5, 5, 5);
            pieChart.setDragDecelerationFrictionCoef(0.95f);
            pieChart.setCenterTextTypeface(AppUtils.getTypefaceBold(activity));
            pieChart.setCenterText(generateCenterSpannableText(chartTitle));
            pieChart.setDrawHoleEnabled(true);
            pieChart.setHoleColor(Color.WHITE);
            pieChart.setTransparentCircleColor(Color.WHITE);
            pieChart.setTransparentCircleAlpha(110);
            pieChart.setHoleRadius(58f);
            pieChart.setTransparentCircleRadius(61f);
            pieChart.setDrawCenterText(true);
            pieChart.setRotationAngle(0);
            // enable rotation of the pieChart by touch
            pieChart.setRotationEnabled(true);
            pieChart.setHighlightPerTapEnabled(true);
            // pieChart.setUnit(" €");
            // pieChart.setDrawUnitsInChart(true);

            // add a selection listener
            //  pieChart.setOnChartValueSelectedListener(this);

            //  pieChart.animateY(1400, Easing.EaseInOutQuad);
            // pieChart.spin(2000, 0, 360);

            Legend l = pieChart.getLegend();
            l.setForm(Legend.LegendForm.CIRCLE);
            l.setWordWrapEnabled(true);
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setDrawInside(false);
            l.setXEntrySpace(5f);
            l.setYEntrySpace(5f);
            l.setXOffset(5f);
            l.setYOffset(5f);

            // entry label styling
            pieChart.setEntryLabelColor(ContextCompat.getColor(activity, R.color.white));
            pieChart.setEntryLabelTypeface(AppUtils.getTypefaceRegular(activity));
            pieChart.setEntryLabelTextSize(12f);

            ArrayList<PieEntry> entries = new ArrayList<>();
            ArrayList<Integer> colors = new ArrayList<>();

            if (chartTitle.equals("Asset Allocation"))
            {
                for (int i = 0; i < listAssetAllocation.size(); i++)
                {
                    if (i != 0 && i != listAssetAllocation.size() - 1 && Float.parseFloat(listAssetAllocation.get(i).getActual_percentage()) > 0.0F)
                    {
                        entries.add(new PieEntry(Float.parseFloat(listAssetAllocation.get(i).getActual_percentage()), listAssetAllocation.get(i).getAssetName()));
                    }
                }

                colors.add(ContextCompat.getColor(activity, R.color.chart_color1));
                colors.add(ContextCompat.getColor(activity, R.color.chart_color2));
                colors.add(ContextCompat.getColor(activity, R.color.chart_color3));
                colors.add(ContextCompat.getColor(activity, R.color.chart_color4));
                colors.add(ContextCompat.getColor(activity, R.color.chart_color5));
                colors.add(ContextCompat.getColor(activity, R.color.chart_color6));
                colors.add(ContextCompat.getColor(activity, R.color.chart_color7));
                colors.add(ContextCompat.getColor(activity, R.color.chart_color8));
                colors.add(ContextCompat.getColor(activity, R.color.chart_color9));
                colors.add(ContextCompat.getColor(activity, R.color.chart_color10));
            }
            else if (chartTitle.equals("Applicants Allocation"))
            {
                for (int i = 0; i < listApplicants.size(); i++)
                {
                    if (i != 0 && i != listApplicants.size() - 1 && Float.parseFloat(listApplicants.get(i).getCagr()) > 0.0F)
                    {
                        entries.add(new PieEntry(Float.parseFloat(listApplicants.get(i).getCagr()), listApplicants.get(i).getName()));
                    }
                }

                colors.add(ContextCompat.getColor(activity, R.color.chart_color7));
                colors.add(ContextCompat.getColor(activity, R.color.chart_color9));
                colors.add(ContextCompat.getColor(activity, R.color.chart_color5));
                colors.add(ContextCompat.getColor(activity, R.color.chart_color4));
                colors.add(ContextCompat.getColor(activity, R.color.chart_color1));
                colors.add(ContextCompat.getColor(activity, R.color.chart_color2));
                colors.add(ContextCompat.getColor(activity, R.color.chart_color3));
                colors.add(ContextCompat.getColor(activity, R.color.chart_color6));
                colors.add(ContextCompat.getColor(activity, R.color.chart_color8));
                colors.add(ContextCompat.getColor(activity, R.color.chart_color10));
            }

            // NOTE: The order of the entries when being added to the entries array determines their position around the center of
            // the pieChart.

            PieDataSet dataSet = new PieDataSet(entries, "");
            dataSet.setDrawIcons(false);

            dataSet.setSliceSpace(3f);
            dataSet.setIconsOffset(new MPPointF(0, 40));
            dataSet.setSelectionShift(5f);

            dataSet.setColors(colors);
            //dataSet.setSelectionShift(0f);

            PieData data = new PieData(dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(11f);
            data.setValueTextColor(ContextCompat.getColor(activity, R.color.white));
            data.setValueTypeface(AppUtils.getTypefaceBold(activity));
            pieChart.setData(data);
            pieChart.setDrawEntryLabels(false);

            // undo all highlights
            pieChart.highlightValues(null);
            pieChart.invalidate();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setUpPerformanceData()
    {
        if (sessionManager.getPerformanceList().size() > 0)
        {
            binding.llPerformance.setVisibility(View.VISIBLE);
            listPerformance = sessionManager.getPerformanceList();
        }

        if (listPerformance.size() > 0)
        {
            binding.llSinceInception.setOnClickListener(new View.OnClickListener()
            {
                @Override public void onClick(View view)
                {
                    try
                    {
                        binding.txtSinceInception.setTextColor(ContextCompat.getColor(activity, R.color.blue_new));
                        binding.viewLineSinceInception.setVisibility(View.VISIBLE);

                        binding.txtNextYear.setTextColor(ContextCompat.getColor(activity, R.color.black));
                        binding.viewLineNextYear.setVisibility(View.GONE);

                        binding.txtPreviousYear.setTextColor(ContextCompat.getColor(activity, R.color.black));
                        binding.viewLinePreviousYear.setVisibility(View.GONE);

                        if (listPerformance.size() > 0)
                        {
                            binding.rvPerformance.setAdapter(new PerformanceAdapter(listPerformance));
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

            binding.llSinceInception.performClick();
        }

    }

    private void setUpNextYearData()
    {
        if (sessionManager.getNextYearList().size() > 0)
        {
            binding.llPerformance.setVisibility(View.VISIBLE);
            listNextYearData = sessionManager.getNextYearList();
        }

        if (listNextYearData.size() > 0)
        {
            binding.llNextYear.setOnClickListener(new View.OnClickListener()
            {
                @Override public void onClick(View view)
                {
                    try
                    {
                        binding.txtNextYear.setTextColor(ContextCompat.getColor(activity, R.color.blue_new));
                        binding.viewLineNextYear.setVisibility(View.VISIBLE);

                        binding.txtSinceInception.setTextColor(ContextCompat.getColor(activity, R.color.black));
                        binding.viewLineSinceInception.setVisibility(View.GONE);

                        binding.txtPreviousYear.setTextColor(ContextCompat.getColor(activity, R.color.black));
                        binding.viewLinePreviousYear.setVisibility(View.GONE);

                        if (listNextYearData.size() > 0)
                        {
                            binding.rvPerformance.setAdapter(new PerformanceAdapter(listNextYearData));
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void setUpPreviousYearData()
    {
        if (sessionManager.getPreviousYearList().size() > 0)
        {
            binding.llPerformance.setVisibility(View.VISIBLE);
            listPreviousYearData = sessionManager.getPreviousYearList();
        }

        if (listPreviousYearData.size() > 0)
        {
            binding.llPreviousYear.setOnClickListener(new View.OnClickListener()
            {
                @Override public void onClick(View view)
                {
                    try
                    {
                        binding.txtPreviousYear.setTextColor(ContextCompat.getColor(activity, R.color.blue_new));
                        binding.viewLinePreviousYear.setVisibility(View.VISIBLE);

                        binding.txtSinceInception.setTextColor(ContextCompat.getColor(activity, R.color.black));
                        binding.viewLineSinceInception.setVisibility(View.GONE);

                        binding.txtNextYear.setTextColor(ContextCompat.getColor(activity, R.color.black));
                        binding.viewLineNextYear.setVisibility(View.GONE);

                        if (listPreviousYearData.size() > 0)
                        {
                            binding.rvPerformance.setAdapter(new PerformanceAdapter(listPreviousYearData));
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private SpannableString generateCenterSpannableText(String valueText)
    {
        SpannableString s = new SpannableString(valueText);
        s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(activity, R.color.blue_new)), 0, s.length(), 0);
        return s;
    }

}
