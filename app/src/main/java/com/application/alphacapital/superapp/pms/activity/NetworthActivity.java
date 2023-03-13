package com.application.alphacapital.superapp.pms.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.application.alphacapital.superapp.R;
import com.application.alphacapital.superapp.pms.beans.ApplicantListResponseModel;
import com.application.alphacapital.superapp.pms.beans.ApplicationFundSchemesResponseModel;
import com.application.alphacapital.superapp.pms.beans.MyJourneyResponseModel;
import com.application.alphacapital.superapp.pms.beans.NetworthResponseModel;
import com.application.alphacapital.superapp.pms.fragment.AllocationSchemeFundFragment;
import com.application.alphacapital.superapp.pms.fragment.AssetFragment;
import com.application.alphacapital.superapp.pms.fragment.JourneyFragment;
import com.application.alphacapital.superapp.pms.fragment.MacroAssetFragment;
import com.application.alphacapital.superapp.pms.fragment.MicroAssetFragment;
import com.application.alphacapital.superapp.pms.fragment.NetworthApplicantFragment;
import com.application.alphacapital.superapp.pms.network.PortfolioApiClient;
import com.application.alphacapital.superapp.pms.network.PortfolioApiInterface;
import com.application.alphacapital.superapp.pms.utils.ApiUtils;
import com.application.alphacapital.superapp.pms.utils.AppUtils;
import com.application.alphacapital.superapp.pms.utils.SessionManager;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworthActivity extends AppCompatActivity
{
    private Activity activity;
    private SessionManager sessionManager;
    private TabLayout tabs;
    private ViewPager pager;
    private String[] tabsTitle = {"Assets", "Micro Asset", "Macro Asset", "Allocation", "Journey", "Applicants"};
    private TabAdapter mAdapter;
    private LinearLayout llBack;
    private View llLoading, llNoInternet, llNoData;
    private PortfolioApiInterface apiService;
    private ArrayList<NetworthResponseModel.AssetsData.AssetsDetails.DebtItem> listNetworth;
    private ArrayList<ApplicantListResponseModel.ApplicantsItem> listAllApplicants;
    private ArrayList<NetworthResponseModel.MicroAssetsStrategicItem> listMicroStrategic;
    private ArrayList<NetworthResponseModel.MicroTactical.MicroAssetsTacticalItem> listMicroTactical;
    private ArrayList<NetworthResponseModel.MacroAssetsStrategicItem> listMacroStrategic;
    private ArrayList<NetworthResponseModel.MacroTactical.MacroAssetsTacticalItem> listMacroTactical;
    private ArrayList<ApplicationFundSchemesResponseModel.FundSchemeListItem> listScheme;
    private ArrayList<ApplicationFundSchemesResponseModel.FundSchemesItem> listFund;
    private ArrayList<ApplicantListResponseModel.ApplicantsList> listApplicants;
    private ApplicantListResponseModel.ApplicantsList objectApplicants;

    public static String purchase = "", switchIn = "", switchOut = "", sell = "", currentVal = "", XIRR = "", netInvestment = "", totalGain = "", InitialVal = "", CurrentVal = "", Gain = "", DividendValue = "", AbsoluteReturn = "", CAGR = "", WeightedDays = "";

    @Override protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.portfolio_activity_networth_tabs);

            activity = NetworthActivity.this;
            apiService = PortfolioApiClient.getClient().create(PortfolioApiInterface.class);
            initView();
            //  setStatusBarGray(activity);
            if (AppUtils.isOnline())
            {
                llNoInternet.setVisibility(View.GONE);
                getNetworthData();
            }
            else
            {
                llNoInternet.setVisibility(View.VISIBLE);
            }

            onClicks();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private void initView()
    {
        try
        {
            TextView txtTitle = findViewById(R.id.txtTitle);
            txtTitle.setText("Networth");

            ImageView imgPageIcon = findViewById(R.id.imgPageIcon);
            imgPageIcon.setImageResource(R.drawable.portfolio_ic_networth_white);

            llBack = findViewById(R.id.llBack);

            llLoading = findViewById(R.id.llLoading);
            llNoInternet = findViewById(R.id.llNoInternet);
            llNoData = findViewById(R.id.llNoData);

            tabs = (TabLayout) findViewById(R.id.tabs);
            pager = (ViewPager) findViewById(R.id.pager);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void onClicks()
    {
        llBack.setOnClickListener(v ->
        {
            try
            {
                finish();
                AppUtils.finishActivityAnimation(activity);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });
    }

    /**
     * call on server to get the Networth section data
     */
    private void getNetworthData()
    {
        try
        {
            llLoading.setVisibility(View.VISIBLE);
            listNetworth = new ArrayList<>();
            listAllApplicants = new ArrayList<>();
            listMicroStrategic = new ArrayList<>();
            listMicroTactical = new ArrayList<>();
            listMacroStrategic = new ArrayList<>();
            listMacroTactical = new ArrayList<>();
            listScheme = new ArrayList<>();
            listFund = new ArrayList<>();
            listApplicants = new ArrayList<>();
            apiService.getNetworthAPI(ApiUtils.END_USER_ID).enqueue(new Callback<NetworthResponseModel>()
            {
                @Override public void onResponse(Call<NetworthResponseModel> call, Response<NetworthResponseModel> response)
                {
                    if (response.isSuccessful())
                    {
                        if (response.body().getSuccess() == 1)
                        {
                            for(int i=0;i<response.body().getAssets_data().getAssets_details().getDebt().size();i++)
                            {
                                NetworthResponseModel.AssetsData.AssetsDetails.DebtItem item =  new NetworthResponseModel.AssetsData.AssetsDetails.DebtItem();
                                item.setAssetsTitle("Debt");
                                item.setAmount(response.body().getAssets_data().getAssets_details().getDebt().get(i).getAmount());
                                item.setHoldingPercentage(response.body().getAssets_data().getAssets_details().getDebt().get(i).getHoldingPercentage());
                                item.setObjective(response.body().getAssets_data().getAssets_details().getDebt().get(i).getObjective());
                                item.setTotal(false);
                                listNetworth.add(item);
                            }

                            NetworthResponseModel.AssetsData.AssetsDetails.DebtItem itemDebt =  new NetworthResponseModel.AssetsData.AssetsDetails.DebtItem();
                            itemDebt.setAssetsTitle("Debt");
                            itemDebt.setAmount("");
                            itemDebt.setHoldingPercentage("");
                            itemDebt.setObjective("");
                            itemDebt.setTotal(true);
                            itemDebt.setTotalAmount(String.valueOf(response.body().getAssets_data().getAssets_details_total().getDebt().getTotal_amount()));
                            itemDebt.setTotalAllocation(String.valueOf(response.body().getAssets_data().getAssets_details_total().getDebt().getPercentage_amount()));
                            listNetworth.add(itemDebt);

                            for(int i=0;i<response.body().getAssets_data().getAssets_details().getEquity().size();i++)
                            {
                                NetworthResponseModel.AssetsData.AssetsDetails.DebtItem item =  new NetworthResponseModel.AssetsData.AssetsDetails.DebtItem();
                                item.setAssetsTitle("Equity");
                                item.setAmount(response.body().getAssets_data().getAssets_details().getEquity().get(i).getAmount());
                                item.setHoldingPercentage(response.body().getAssets_data().getAssets_details().getEquity().get(i).getHoldingPercentage());
                                item.setObjective(response.body().getAssets_data().getAssets_details().getEquity().get(i).getObjective());
                                item.setTotal(false);
                                listNetworth.add(item);
                            }

                            NetworthResponseModel.AssetsData.AssetsDetails.DebtItem itemEquity =  new NetworthResponseModel.AssetsData.AssetsDetails.DebtItem();
                            itemEquity.setAssetsTitle("Equity");
                            itemEquity.setAmount("");
                            itemEquity.setHoldingPercentage("");
                            itemEquity.setObjective("");
                            itemEquity.setTotal(true);
                            itemEquity.setTotalAmount(String.valueOf(response.body().getAssets_data().getAssets_details_total().getEquity().getTotal_amount()));
                            itemEquity.setTotalAllocation(String.valueOf(response.body().getAssets_data().getAssets_details_total().getEquity().getPercentage_amount()));
                            listNetworth.add(itemEquity);

                            for(int i=0;i<response.body().getAssets_data().getAssets_details().getHybrid().size();i++)
                            {
                                NetworthResponseModel.AssetsData.AssetsDetails.DebtItem item =  new NetworthResponseModel.AssetsData.AssetsDetails.DebtItem();
                                item.setAssetsTitle("Hybrid");
                                item.setAmount(response.body().getAssets_data().getAssets_details().getHybrid().get(i).getAmount());
                                item.setHoldingPercentage(response.body().getAssets_data().getAssets_details().getHybrid().get(i).getHoldingPercentage());
                                item.setObjective(response.body().getAssets_data().getAssets_details().getHybrid().get(i).getObjective());
                                item.setTotal(false);
                                listNetworth.add(item);
                            }

                            NetworthResponseModel.AssetsData.AssetsDetails.DebtItem itemHybrid =  new NetworthResponseModel.AssetsData.AssetsDetails.DebtItem();
                            itemHybrid.setAssetsTitle("Hybrid");
                            itemHybrid.setAmount("");
                            itemHybrid.setHoldingPercentage("");
                            itemHybrid.setObjective("");
                            itemHybrid.setTotal(true);
                            itemHybrid.setTotalAmount(String.valueOf(response.body().getAssets_data().getAssets_details_total().getHybrid().getTotal_amount()));
                            itemHybrid.setTotalAllocation(String.valueOf(response.body().getAssets_data().getAssets_details_total().getHybrid().getPercentage_amount()));
                            listNetworth.add(itemHybrid);

                            listMicroStrategic.addAll(response.body().getMicro_strategic().getMicro_assets_strategic());
                            listMicroStrategic.add(response.body().getMicro_strategic().getMicro_assets_strategic_grandtotal());
                            listMicroTactical.addAll(response.body().getMicro_tactical().getMicro_assets_tactical());
                            listMicroTactical.add(response.body().getMicro_tactical().getMicro_assets_tactical_grandtotal());
                            listMacroStrategic.addAll(response.body().getMacro_strategic().getMacro_assets_strategic());
                            listMacroStrategic.add(response.body().getMacro_strategic().getMacro_assets_strategic_grandtotal());
                            listMacroTactical.addAll(response.body().getMacro_tactical().getMacro_assets_tactical());
                            listMacroTactical.add(response.body().getMacro_tactical().getMacro_assets_tactical_grandtotal());
                            //listApplicants.addAll(response.body().getApplicants_assets_details().getApplicant_details());
                            getApplicantDataApi();
                        }
                        else
                        {
                            llLoading.setVisibility(View.GONE);
                        }
                    }
                    else
                    {
                        llLoading.setVisibility(View.GONE);
                    }
                }

                @Override public void onFailure(Call<NetworthResponseModel> call, Throwable t)
                {
                    AppUtils.printLog("FAIL 1", t.getMessage());
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        /*new AsyncTask<Void, Void, Void>()
        {
            int status = -1;

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                llLoading.setVisibility(View.VISIBLE);
                listNetworth = new ArrayList<>();
                listAllApplicants = new ArrayList<>();
                listMicroStrategic = new ArrayList<>();
                listMicroTactical = new ArrayList<>();
                listMacroStrategic = new ArrayList<>();
                listMacroTactical = new ArrayList<>();
                listScheme = new ArrayList<>();
                listFund = new ArrayList<>();
                listApplicants = new ArrayList<>();

            }

            @Override
            protected Void doInBackground(Void... voids)
            {
                try
                {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("user_id", ApiUtils.END_USER_ID);

                    String serverResponse = MitsUtils.readJSONServiceUsingPOST(ApiUtils.GET_NETWORTH, hashMap);

                    AppUtils.printLog("ServerResponseNetworth","Params : " + hashMap.toString() + "Response: " + serverResponse);
                    AppUtils.printLog("Params","Params : " + hashMap.toString() );

                    JSONObject jsonObject = new JSONObject(serverResponse);

                    status = AppUtils.getValidAPIIntegerResponse(jsonObject.optString("success"));

                    if (status == 1)
                    {
                        JSONObject assetsObj = jsonObject.optJSONObject("assets_data");
                        JSONObject assetDetailObj = assetsObj.optJSONObject("assets_details");

                        if(assetDetailObj.has("Debt"))
                        {
                            JSONArray debtArray = assetDetailObj.optJSONArray("Debt");

                            if(debtArray.length() > 0)
                            {
                                for (int i = 0; i < debtArray.length(); i++)
                                {
                                    JSONObject debtObj = debtArray.getJSONObject(i);

                                    NetworthAssetsBean networthAssetsBean = new NetworthAssetsBean();
                                    networthAssetsBean.setAssetsTitle("Debt");
                                    networthAssetsBean.setObjective(debtObj.optString("Objective"));
                                    networthAssetsBean.setAmount(debtObj.optString("Amount"));
                                    networthAssetsBean.setHoldingPercentage(debtObj.optString("HoldingPercentage"));
                                    networthAssetsBean.setTotal(false);
                                    networthAssetsBean.setTotalAmount("");
                                    networthAssetsBean.setTotalAllocation("");

                                    listNetworth.add(networthAssetsBean);
                                }
                            }
                        }


                        JSONObject assetTotal = assetsObj.optJSONObject("assets_details_total");
                        JSONObject assetDebtObj = assetTotal.optJSONObject("Debt");
                        NetworthAssetsBean totalDebtNetworthBean = new NetworthAssetsBean();
                        totalDebtNetworthBean.setAssetsTitle("Debt");
                        totalDebtNetworthBean.setObjective("");
                        totalDebtNetworthBean.setAmount("");
                        totalDebtNetworthBean.setHoldingPercentage("");
                        totalDebtNetworthBean.setTotal(true);
                        totalDebtNetworthBean.setTotalAmount(assetDebtObj.optString("total_amount"));
                        totalDebtNetworthBean.setTotalAllocation(assetDebtObj.optString("percentage_amount"));
                        listNetworth.add(totalDebtNetworthBean);

                        if(assetDetailObj.has("Equity"))
                        {
                            JSONArray equityArray = assetDetailObj.optJSONArray("Equity");
                            if(equityArray.length() > 0)
                            {
                                for (int i = 0; i < equityArray.length(); i++)
                                {
                                    JSONObject equityObj = equityArray.getJSONObject(i);

                                    NetworthAssetsBean networthAssetsBean = new NetworthAssetsBean();
                                    networthAssetsBean.setAssetsTitle("Equity");
                                    networthAssetsBean.setObjective(equityObj.optString("Objective"));
                                    networthAssetsBean.setAmount(equityObj.optString("Amount"));
                                    networthAssetsBean.setHoldingPercentage(equityObj.optString("HoldingPercentage"));
                                    networthAssetsBean.setTotal(false);
                                    networthAssetsBean.setTotalAmount("");
                                    networthAssetsBean.setTotalAllocation("");

                                    listNetworth.add(networthAssetsBean);
                                }
                            }
                        }


                        JSONObject assestEquityObj = assetTotal.optJSONObject("Equity");
                        NetworthAssetsBean totalEquityNetworthBean = new NetworthAssetsBean();
                        totalEquityNetworthBean.setAssetsTitle("Equity");
                        totalEquityNetworthBean.setObjective("");
                        totalEquityNetworthBean.setAmount("");
                        totalEquityNetworthBean.setHoldingPercentage("");
                        totalEquityNetworthBean.setTotal(true);
                        totalEquityNetworthBean.setTotalAmount(assestEquityObj.optString("total_amount"));
                        totalEquityNetworthBean.setTotalAllocation(assestEquityObj.optString("percentage_amount"));
                        listNetworth.add(totalEquityNetworthBean);

                        if(assetDetailObj.has("Hybrid"))
                        {
                            JSONArray hybridArray = assetDetailObj.optJSONArray("Hybrid");
                            if(hybridArray.length() > 0)
                            {
                                for (int i = 0; i < hybridArray.length(); i++)
                                {
                                    JSONObject hybridObj = hybridArray.getJSONObject(i);

                                    NetworthAssetsBean networthAssetsBean = new NetworthAssetsBean();
                                    networthAssetsBean.setAssetsTitle("Hybrid");
                                    networthAssetsBean.setObjective(hybridObj.optString("Objective"));
                                    networthAssetsBean.setAmount(hybridObj.optString("Amount"));
                                    networthAssetsBean.setHoldingPercentage(hybridObj.optString("HoldingPercentage"));
                                    networthAssetsBean.setTotal(false);
                                    networthAssetsBean.setTotalAmount("");
                                    networthAssetsBean.setTotalAllocation("");

                                    listNetworth.add(networthAssetsBean);
                                }
                            }
                        }

                        if(assetTotal.has("Hybrid"))
                        {
                            JSONObject assestHybridObj = assetTotal.optJSONObject("Hybrid");
                            NetworthAssetsBean totalHybridNetworthBean = new NetworthAssetsBean();
                            totalHybridNetworthBean.setAssetsTitle("Hybrid");
                            totalHybridNetworthBean.setObjective("");
                            totalHybridNetworthBean.setAmount("");
                            totalHybridNetworthBean.setHoldingPercentage("");
                            totalHybridNetworthBean.setTotal(true);
                            totalHybridNetworthBean.setTotalAmount(assestHybridObj.optString("total_amount"));
                            totalHybridNetworthBean.setTotalAllocation(assestHybridObj.optString("percentage_amount"));
                            listNetworth.add(totalHybridNetworthBean);
                        }


                        //Parsing Networth Applicant List
                        String networthApplicantResponse = MitsUtils.readJSONServiceUsingPOST(ApiUtils.GET_NETWORTH_APPLICANTS_LIST,hashMap);
                        AppUtils.printLog("Applicant Response",networthApplicantResponse);

                        JSONObject networthApplicationObj = new JSONObject(networthApplicantResponse);

                        status = AppUtils.getValidAPIIntegerResponse(networthApplicationObj.optString("success"));

                        if(status == 1)
                        {
                            JSONObject applicantListObj = networthApplicationObj.optJSONObject("applicants_list");
                            JSONArray applicantListArray = applicantListObj.optJSONArray("applicants");

                            if(applicantListArray.length() > 0)
                            {

                                for (int i = 0; i < applicantListArray.length(); i++)
                                {
                                    JSONObject applicantObj = applicantListArray.getJSONObject(i);

                                    NetworthApplicantListBean applicantsGetSet = new NetworthApplicantListBean();

                                    applicantsGetSet.setApplicantName(AppUtils.getValidAPIStringResponse(AppUtils.toDisplayCase(applicantObj.optString("ApplicantName"))));
                                    applicantsGetSet.setCid(AppUtils.getValidAPIStringResponse(applicantObj.optString("Cid")));
                                    applicantsGetSet.setGroupLeader(AppUtils.getValidAPIStringResponse(applicantObj.optString("GroupLeader")));
                                    applicantsGetSet.setInitialVal(AppUtils.getValidAPIStringResponse(applicantObj.optString("InitialVal")));
                                    applicantsGetSet.setCurrentVal(AppUtils.getValidAPIStringResponse(applicantObj.optString("CurrentVal")));
                                    applicantsGetSet.setDividendValue(AppUtils.getValidAPIStringResponse(applicantObj.optString("DividendValue")));
                                    applicantsGetSet.setGain(AppUtils.getValidAPIStringResponse(applicantObj.optString("Gain")));
                                    applicantsGetSet.setAbsoluteReturn(AppUtils.getValidAPIStringResponse(applicantObj.optString("AbsoluteReturn")));
                                    applicantsGetSet.setWeightedDays(AppUtils.getValidAPIStringResponse(applicantObj.optString("WeightedDays")));
                                    applicantsGetSet.setCAGR(AppUtils.getValidAPIStringResponse(applicantObj.optString("CAGR")));

                                    listAllApplicants.add(applicantsGetSet);
                                }
                            }

                            JSONObject applicantTotalObj = applicantListObj.optJSONObject("total");
                            InitialVal = String.valueOf(AppUtils.getValidAPIDoubleResponse(applicantTotalObj.optString("InitialVal")));
                            CurrentVal = String.valueOf(AppUtils.getValidAPIDoubleResponse(applicantTotalObj.optString("CurrentVal")));
                            Gain = String.valueOf(AppUtils.getValidAPIDoubleResponse(applicantTotalObj.optString("Gain")));
                            DividendValue = String.valueOf(AppUtils.getValidAPIDoubleResponse(applicantTotalObj.optString("DividendValue")));
                            AbsoluteReturn = AppUtils.getValidAPIStringResponse(applicantTotalObj.optString("AbsoluteReturn"));
                            CAGR = AppUtils.getValidAPIStringResponse(applicantTotalObj.optString("CAGR"));
                            WeightedDays = String.valueOf(AppUtils.getValidAPIIntegerResponse(applicantTotalObj.optString("WeightedDays")));
                        }


                        //Parsing MicroStrategic
                        JSONObject microObj = jsonObject.optJSONObject("micro_strategic");
                        JSONArray microArray = microObj.optJSONArray("micro_assets_strategic");
                        if(microArray.length() > 0)
                        {
                            for (int i = 0; i < microArray.length(); i++)
                            {
                                JSONObject microAssetObj = microArray.getJSONObject(i);

                                MicroStrategicBean microStrategicBean = new MicroStrategicBean();
                                microStrategicBean.setAsset_class(microAssetObj.optString("asset_class"));
                                microStrategicBean.setActual_amount(microAssetObj.optDouble("actual_amount"));
                                microStrategicBean.setActual_percentage(microAssetObj.optDouble("actual_percentage"));
                                microStrategicBean.setPolicy(microAssetObj.optString("policy"));
                                microStrategicBean.setVariance(microAssetObj.optInt("variance"));
                                microStrategicBean.setTotalAmount("");
                                microStrategicBean.setTotalAmountPercentage("");
                                microStrategicBean.setTotalPolicyPercentage("");
                                microStrategicBean.setTotalVariance("");
                                microStrategicBean.setTotal(false);

                                listMicroStrategic.add(microStrategicBean);
                            }
                        }

                        JSONObject assetMicroObj = microObj.optJSONObject("micro_assets_strategic_grandtotal");
                        MicroStrategicBean microStrategicBean = new MicroStrategicBean();
                        microStrategicBean.setAsset_class("");
                        microStrategicBean.setActual_amount(0);
                        microStrategicBean.setActual_percentage(0);
                        microStrategicBean.setPolicy("");
                        microStrategicBean.setVariance(0);
                        microStrategicBean.setTotalAmount(assetMicroObj.optString("grand_total_amount"));
                        microStrategicBean.setTotalAmountPercentage(assetMicroObj.optString("grand_percentage_amount"));
                        microStrategicBean.setTotalPolicyPercentage(assetMicroObj.optString("micro_assets_strategic_policy"));
                        microStrategicBean.setTotalVariance(assetMicroObj.optString("micro_assets_strategic_variance_total"));
                        microStrategicBean.setTotal(true);

                        listMicroStrategic.add(microStrategicBean);

                        JSONObject microTacticalObj = jsonObject.optJSONObject("micro_tactical");
                        JSONArray microTacticalArray = microTacticalObj.optJSONArray("micro_assets_tactical");
                        if(microTacticalArray.length() > 0)
                        {
                            for (int i = 0; i < microTacticalArray.length(); i++)
                            {
                                JSONObject microTacticalAssetObj = microTacticalArray.getJSONObject(i);

                                MicroTacticalBean microTacticalBean = new MicroTacticalBean();
                                microTacticalBean.setAsset_class(microTacticalAssetObj.optString("asset_class"));
                                microTacticalBean.setActual_percentage(microTacticalAssetObj.optString("actual_percentage"));
                                microTacticalBean.setPolicy(microTacticalAssetObj.optString("policy"));
                                microTacticalBean.setVariance(microTacticalAssetObj.optInt("variance"));
                                microTacticalBean.setTotalPercentage("");
                                microTacticalBean.setTotalPolicyPercentage("");
                                microTacticalBean.setToalVariance("");
                                microTacticalBean.setTotal(false);

                                listMicroTactical.add(microTacticalBean);
                            }
                        }

                        JSONObject assetMicroTacticalObj = microTacticalObj.optJSONObject("micro_assets_tactical_grandtotal");
                        MicroTacticalBean microTacticalBean = new MicroTacticalBean();
                        microTacticalBean.setAsset_class("");
                        microTacticalBean.setActual_percentage("");
                        microTacticalBean.setPolicy("");
                        microTacticalBean.setVariance(0);
                        microTacticalBean.setTotalPercentage(assetMicroTacticalObj.optString("grand_percentage_amount"));
                        microTacticalBean.setTotalPolicyPercentage(assetMicroTacticalObj.optString("micro_assets_tactical_policy"));
                        microTacticalBean.setToalVariance(assetMicroTacticalObj.optString("micro_assets_tactical_variance_total"));
                        microTacticalBean.setTotal(true);

                        listMicroTactical.add(microTacticalBean);

                        //Parsing MacroStrategic
                        JSONObject macroObj = jsonObject.optJSONObject("macro_strategic");
                        JSONArray macroArray = macroObj.optJSONArray("macro_assets_strategic");
                        if(macroArray.length() > 0)
                        {
                            for (int i = 0; i < macroArray.length(); i++)
                            {
                                JSONObject microAssetObj = macroArray.getJSONObject(i);

                                MicroStrategicBean macroStrategicBean = new MicroStrategicBean();
                                macroStrategicBean.setAsset_class(microAssetObj.optString("asset_class"));
                                macroStrategicBean.setActual_amount(microAssetObj.optDouble("actual_amount"));
                                macroStrategicBean.setActual_percentage(microAssetObj.optDouble("actual_percentage"));
                                macroStrategicBean.setPolicy(microAssetObj.optString("policy"));
                                macroStrategicBean.setVariance(microAssetObj.optInt("variance"));
                                macroStrategicBean.setTotalAmount("");
                                macroStrategicBean.setTotalAmountPercentage("");
                                macroStrategicBean.setTotalPolicyPercentage("");
                                macroStrategicBean.setTotalVariance("");
                                macroStrategicBean.setTotal(false);

                                listMacroStrategic.add(macroStrategicBean);
                            }
                        }

                        JSONObject assetMacroObj = macroObj.optJSONObject("macro_assets_strategic_grandtotal");
                        MicroStrategicBean macroStrategicBean = new MicroStrategicBean();
                        macroStrategicBean.setAsset_class("");
                        macroStrategicBean.setActual_amount(0);
                        macroStrategicBean.setActual_percentage(0);
                        macroStrategicBean.setPolicy("");
                        macroStrategicBean.setVariance(0);
                        macroStrategicBean.setTotalAmount(assetMacroObj.optString("grand_total_amount"));
                        macroStrategicBean.setTotalAmountPercentage(assetMacroObj.optString("grand_percentage_amount"));
                        macroStrategicBean.setTotalPolicyPercentage(assetMacroObj.optString("macro_assets_strategic_policy"));
                        macroStrategicBean.setTotalVariance(assetMacroObj.optString("macro_assets_strategic_variance"));
                        macroStrategicBean.setTotal(true);

                        listMacroStrategic.add(macroStrategicBean);

                        JSONObject macroTacticalObj = jsonObject.optJSONObject("macro_tactical");
                        JSONArray macroTacticalArray = macroTacticalObj.optJSONArray("macro_assets_tactical");
                        if(macroTacticalArray.length() > 0)
                        {
                            for (int i = 0; i < macroTacticalArray.length(); i++)
                            {
                                JSONObject microTacticalAssetObj = macroTacticalArray.getJSONObject(i);

                                MicroTacticalBean macroTacticalBean = new MicroTacticalBean();
                                macroTacticalBean.setAsset_class(microTacticalAssetObj.optString("asset_class"));
                                macroTacticalBean.setActual_percentage(microTacticalAssetObj.optString("actual_percentage"));
                                macroTacticalBean.setPolicy(microTacticalAssetObj.optString("policy"));
                                macroTacticalBean.setVariance(microTacticalAssetObj.optInt("variance"));
                                macroTacticalBean.setTotalPercentage("");
                                macroTacticalBean.setTotalPolicyPercentage("");
                                macroTacticalBean.setToalVariance("");
                                macroTacticalBean.setTotal(false);

                                listMacroTactical.add(macroTacticalBean);
                            }
                        }

                        JSONObject assetMacroTacticalObj = macroTacticalObj.optJSONObject("macro_assets_tactical_grandtotal");
                        MicroTacticalBean macroTacticalBean = new MicroTacticalBean();
                        macroTacticalBean.setAsset_class("");
                        macroTacticalBean.setActual_percentage("");
                        macroTacticalBean.setPolicy("");
                        macroTacticalBean.setVariance(0);
                        macroTacticalBean.setTotalPercentage(assetMacroTacticalObj.optString("grand_percentage_amount"));
                        macroTacticalBean.setTotalPolicyPercentage(assetMacroTacticalObj.optString("macro_assets_tactical_policy"));
                        macroTacticalBean.setToalVariance(assetMacroTacticalObj.optString("macro_assets_tactical_variance"));
                        macroTacticalBean.setTotal(true);

                        listMacroTactical.add(macroTacticalBean);

                        //Parsing AllocationScheme
                        String allocationServerResponse = MitsUtils.readJSONServiceUsingPOST(ApiUtils.GET_ALLOCATION_FUND_SCHEMES, hashMap);

                        JSONObject allocationJsonObject = new JSONObject(allocationServerResponse);
                        status = AppUtils.getValidAPIIntegerResponse(allocationJsonObject.optString("success"));

                        if(status == 1)
                        {
                            JSONArray schemeArray = allocationJsonObject.optJSONArray("fund_scheme_list");
                            if(schemeArray.length() > 0)
                            {
                                for (int i = 0; i < schemeArray.length(); i++)
                                {
                                    JSONObject schemeObj = schemeArray.getJSONObject(i);

                                    AllocationSchemesBean allocationSchemesBean = new AllocationSchemesBean();
                                    allocationSchemesBean.setFundName(schemeObj.optString("FundName"));
                                    allocationSchemesBean.setSchemeName(schemeObj.optString("SchemeName"));
                                    allocationSchemesBean.setCurrentValue(schemeObj.optString("CurrentValue"));
                                    allocationSchemesBean.setCategory(schemeObj.optString("category"));
                                    allocationSchemesBean.setAllocation(schemeObj.optInt("allocation"));
                                    allocationSchemesBean.setTotalCurrentValue("");
                                    allocationSchemesBean.setTotalAllocation("");
                                    allocationSchemesBean.setTotal(false);

                                    listScheme.add(allocationSchemesBean);
                                }
                            }

                            JSONObject schemeAssetObj = allocationJsonObject.optJSONObject("fund_scheme_list_total");
                            AllocationSchemesBean allocationSchemesBean = new AllocationSchemesBean();
                            allocationSchemesBean.setFundName("");
                            allocationSchemesBean.setSchemeName("");
                            allocationSchemesBean.setCurrentValue("");
                            allocationSchemesBean.setCategory("");
                            allocationSchemesBean.setAllocation(0);
                            allocationSchemesBean.setTotalCurrentValue(schemeAssetObj.optString("PresentAmountTotal"));
                            allocationSchemesBean.setTotalAllocation(schemeAssetObj.optString("AllocationTotal"));
                            allocationSchemesBean.setTotal(true);

                            listScheme.add(allocationSchemesBean);

                            JSONArray fundArray = allocationJsonObject.optJSONArray("fund_schemes");
                            if(fundArray.length() > 0)
                            {
                                for (int i = 0; i < fundArray.length(); i++)
                                {
                                    JSONObject fundObj = fundArray.getJSONObject(i);

                                    AllocationFundBean allocationFundBean = new AllocationFundBean();
                                    allocationFundBean.setFundName(fundObj.optString("FundName"));
                                    allocationFundBean.setCurrentValue(fundObj.optDouble("CurrentValue"));
                                    allocationFundBean.setTotalCurrentValue("");
                                    allocationFundBean.setTotal(false);

                                    listFund.add(allocationFundBean);
                                }
                            }

                            JSONObject fundAssetObj = allocationJsonObject.optJSONObject("fund_schemes_total");
                            AllocationFundBean allocationFundBean = new AllocationFundBean();
                            allocationFundBean.setFundName("");
                            allocationFundBean.setCurrentValue(0);
                            allocationFundBean.setTotalCurrentValue(fundAssetObj.optString("PresentAmountTotal"));
                            allocationFundBean.setTotal(true);

                            listFund.add(allocationFundBean);
                        }

                        //Jouney
                        String journeyServerResponse = MitsUtils.readJSONServiceUsingPOST(ApiUtils.GET_MY_JOURNEY, hashMap);

                        JSONObject journeyJsonObject = new JSONObject(journeyServerResponse);
                        status = AppUtils.getValidAPIIntegerResponse(journeyJsonObject.optString("success"));

                        if(status == 1)
                        {
                            JSONArray journeyDetailArray = journeyJsonObject.optJSONArray("journey_details");
                            for (int i = 0; i < journeyDetailArray.length(); i++)
                            {
                                JSONObject object = journeyDetailArray.getJSONObject(i);

                                purchase = object.optString("Purchase");
                                switchIn = object.optString("SwitchIn");
                                switchOut = object.optString("Switchout");
                                sell = object.optString("Sell");
                                currentVal = object.optString("CurrentVal");
                                XIRR = object.optString("XIIR");
                                netInvestment = object.optString("NetInvestment");
                                totalGain = object.optString("TotalGain");
                            }
                        }

                        //ApplicantWise Scheme Details
                        JSONObject applicantObj = jsonObject.optJSONObject("applicants_list");
                        JSONArray applicantArray = applicantObj.optJSONArray("applicants");
                        for (int i = 0; i < applicantArray.length(); i++)
                        {
                            JSONObject applicantDataObj = applicantArray.getJSONObject(i);

                            ApplicantDetailsNetworthBean applicantListBean = new ApplicantDetailsNetworthBean();
                            applicantListBean.setApplicant_name(AppUtils.getValidAPIStringResponse(applicantDataObj.optString("applicant_name")));

                            JSONArray applicantAssetsArray = applicantDataObj.optJSONArray("apcnt_assets_details");

                            ArrayList<ApplicantDetailsNetworthBean.ApcntAssetsDetailsGetSet> listApplicantDetails = new ArrayList<>();
                            for (int j = 0; j < applicantAssetsArray.length(); j++)
                            {
                                JSONObject applicantAssetsObj = applicantAssetsArray.getJSONObject(j);

                                ApplicantDetailsNetworthBean.ApcntAssetsDetailsGetSet apcntAssetsDetailsGetSet = new ApplicantDetailsNetworthBean.ApcntAssetsDetailsGetSet();

                                apcntAssetsDetailsGetSet.setObjective(AppUtils.getValidAPIStringResponse(applicantAssetsObj.optString("Objective")));
                                apcntAssetsDetailsGetSet.setAmount(AppUtils.getValidAPIIntegerResponse(applicantAssetsObj.optString("Amount")));
                                apcntAssetsDetailsGetSet.setHoldingPercentage(AppUtils.getValidAPIDoubleResponse(applicantAssetsObj.optString("HoldingPercentage")));

                                listApplicantDetails.add(apcntAssetsDetailsGetSet);
                            }

                            JSONObject assetsTotalObj = applicantDataObj.optJSONObject("apcnt_assets_total");
                            ApplicantDetailsNetworthBean.ApcntAssetsTotalGetSet apcntAssetsTotalGetSet = new ApplicantDetailsNetworthBean.ApcntAssetsTotalGetSet();
                            apcntAssetsTotalGetSet.setTotalAmount(AppUtils.getValidAPIStringResponse(assetsTotalObj.optString("TotalAmount")));
                            apcntAssetsTotalGetSet.setTotalHoldingPercentage(AppUtils.getValidAPIDoubleResponse(assetsTotalObj.optString("TotalHoldingPercentage")));

                            applicantListBean.setApcnt_assets_details(listApplicantDetails);
                            applicantListBean.setApcnt_assets_total(apcntAssetsTotalGetSet);
                            listApplicants.add(applicantListBean);
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid)
            {
                super.onPostExecute(aVoid);
                llLoading.setVisibility(View.GONE);

                setupPagerData();
            }
        }.execute();*/
    }

    private void getApplicantDataApi()
    {
        apiService.getAppicantListAPI(ApiUtils.END_USER_ID).enqueue(new Callback<ApplicantListResponseModel>()
        {
            @Override public void onResponse(Call<ApplicantListResponseModel> call, Response<ApplicantListResponseModel> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().getSuccess() == 1)
                    {
                        listAllApplicants.addAll(response.body().getApplicants_list().getApplicants());

                        InitialVal = String.valueOf(AppUtils.getValidAPIDoubleResponse(String.valueOf(response.body().getApplicants_list().getTotal().getInitialVal())));
                        CurrentVal = String.valueOf(AppUtils.getValidAPIDoubleResponse(String.valueOf(response.body().getApplicants_list().getTotal().getCurrentVal())));
                        Gain = String.valueOf(AppUtils.getValidAPIDoubleResponse(String.valueOf(response.body().getApplicants_list().getTotal().getGain())));
                        DividendValue = String.valueOf(AppUtils.getValidAPIDoubleResponse(String.valueOf(response.body().getApplicants_list().getTotal().getDividendValue())));
                        AbsoluteReturn = AppUtils.getValidAPIStringResponse(response.body().getApplicants_list().getTotal().getAbsoluteReturn());
                        CAGR = AppUtils.getValidAPIStringResponse(response.body().getApplicants_list().getTotal().getCAGR());
                        WeightedDays = String.valueOf(AppUtils.getValidAPIIntegerResponse(String.valueOf(response.body().getApplicants_list().getTotal().getWeightedDays())));

                        getFundSchemeApi();
                    }
                    else
                    {
                        llLoading.setVisibility(View.GONE);
                    }
                }
                else
                {
                    llLoading.setVisibility(View.GONE);
                }
            }

            @Override public void onFailure(Call<ApplicantListResponseModel> call, Throwable t)
            {
                AppUtils.printLog("FAIL 2", t.getMessage());
            }
        });
    }

    private void getFundSchemeApi()
    {
        apiService.getFundSchemes(ApiUtils.END_USER_ID).enqueue(new Callback<ApplicationFundSchemesResponseModel>()
        {
            @Override public void onResponse(Call<ApplicationFundSchemesResponseModel> call, Response<ApplicationFundSchemesResponseModel> response)
            {
                if (response.isSuccessful())
                {
                    listScheme.addAll(response.body().getFund_scheme_list());
                    listScheme.add(response.body().getFund_scheme_list_total());
                    listFund.addAll(response.body().getFund_schemes());
                    listFund.add(response.body().getFund_schemes_total());
                    getJourneyApi();
                }
                else
                {
                    llLoading.setVisibility(View.GONE);
                }
            }

            @Override public void onFailure(Call<ApplicationFundSchemesResponseModel> call, Throwable t)
            {
                AppUtils.printLog("FAIL 3", t.getMessage());
            }
        });
    }

    private void getJourneyApi()
    {
        apiService.getJournerAPI(ApiUtils.END_USER_ID).enqueue(new Callback<MyJourneyResponseModel>()
        {
            @Override public void onResponse(Call<MyJourneyResponseModel> call, Response<MyJourneyResponseModel> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().getSuccess() == 1)
                    {
                        for (int i = 0; i < response.body().getJourney_details().size(); i++)
                        {
                            purchase = response.body().getJourney_details().get(i).getPurchase();
                            switchIn = response.body().getJourney_details().get(i).getSwitchIn();
                            switchOut = response.body().getJourney_details().get(i).getSwitchout();
                            sell = response.body().getJourney_details().get(i).getSell();
                            currentVal = response.body().getJourney_details().get(i).getCurrentVal();
                            XIRR = response.body().getJourney_details().get(i).getXIIR();
                            netInvestment = response.body().getJourney_details().get(i).getNetInvestment();
                            totalGain = response.body().getJourney_details().get(i).getTotalGain();
                        }
                        getApplicantListApi();


                    }
                    else
                    {
                        llLoading.setVisibility(View.GONE);
                    }
                }
                else
                {
                    llLoading.setVisibility(View.GONE);
                }
            }

            @Override public void onFailure(Call<MyJourneyResponseModel> call, Throwable t)
            {
                AppUtils.printLog("FAIL 4", t.getMessage());
            }
        });
    }

    private void getApplicantListApi()
    {
        apiService.getApplicantListAPI(ApiUtils.END_USER_ID).enqueue(new Callback<ApplicantListResponseModel>()
        {
            @Override public void onResponse(Call<ApplicantListResponseModel> call, Response<ApplicantListResponseModel> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().getSuccess() == 1)
                    {
                        listApplicants.add(response.body().getApplicants_list());
                        objectApplicants = response.body().getApplicants_list();
                        setupPagerData();
                        llLoading.setVisibility(View.GONE);
                        Log.e("<><>TOTOAL INVESTED", String.valueOf(response.body().getApplicants_list().getTotal().getInitialVal()));
                    }
                    else
                    {
                        llLoading.setVisibility(View.GONE);
                    }
                }
                else
                {
                    llLoading.setVisibility(View.GONE);
                }
            }

            @Override public void onFailure(Call<ApplicantListResponseModel> call, Throwable t)
            {
                AppUtils.printLog("Failure Applicant", t.getMessage());
            }
        });
    }

    private void setupPagerData()
    {
        try
        {
            tabs.setVisibility(View.VISIBLE);
            mAdapter = new TabAdapter();
            pager.setAdapter(mAdapter);
            tabs.setupWithViewPager(pager);
            pager.setOffscreenPageLimit(6);
            setupTabIcons();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setupTabIcons()
    {
        try
        {
            for (int i = 0; i < tabsTitle.length; i++)
            {
                final View tabOne = LayoutInflater.from(activity).inflate(R.layout.tab_row_view, null);
                TextView txtTabOne = (TextView) tabOne.findViewById(R.id.txtTab);
                txtTabOne.setText(tabsTitle[i]);
                tabs.getTabAt(i).setCustomView(tabOne);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private class TabAdapter extends FragmentPagerAdapter
    {
        public TabAdapter()
        {
            super(getSupportFragmentManager());
        }

        @Override public Fragment getItem(int position)
        {
            if (position == 0)
            {
                Fragment fragment = new AssetFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("data", listNetworth);
                fragment.setArguments(bundle);
                return fragment;
            }
            else if (position == 1)
            {
                Fragment fragment = new MicroAssetFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("data1", listMicroStrategic);
                bundle.putParcelableArrayList("data2", listMicroTactical);
                fragment.setArguments(bundle);
                return fragment;
            }
            else if (position == 2)
            {
                Fragment fragment = new MacroAssetFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("data1", listMacroStrategic);
                bundle.putParcelableArrayList("data2", listMacroTactical);
                fragment.setArguments(bundle);
                return fragment;
            }
            else if (position == 3)
            {
                Fragment fragment = new AllocationSchemeFundFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("data1", listScheme);
                bundle.putParcelableArrayList("data2", listFund);
                fragment.setArguments(bundle);
                return fragment;
            }
            else if (position == 4)
            {
                Fragment fragment = new JourneyFragment();
                return fragment;
            }
            else
            {
                Fragment fragment = new NetworthApplicantFragment();
                Bundle bundle = new Bundle();
                //bundle.putParcelableArrayList("data",listApplicants);
                bundle.putParcelable("data1", objectApplicants);
                fragment.setArguments(bundle);
                return fragment;
            }
        }

        @Override public int getCount()
        {
            return tabsTitle.length;
        }

        @Override public CharSequence getPageTitle(int position)
        {
            return null;
        }
    }

    @Override public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            try
            {
                finish();
                AppUtils.finishActivityAnimation(activity);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
