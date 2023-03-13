package com.application.alphacapital.superapp.pms.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.application.alphacapital.superapp.R;
import com.application.alphacapital.superapp.pms.beans.PerfomanceResponseModel;
import com.application.alphacapital.superapp.pms.fragment.PerforanceFundsFragment;
import com.application.alphacapital.superapp.pms.fragment.PerformanceOverallFragment;
import com.application.alphacapital.superapp.pms.fragment.SinceInceptionFragment;
import com.application.alphacapital.superapp.pms.network.PortfolioApiClient;
import com.application.alphacapital.superapp.pms.network.PortfolioApiInterface;
import com.application.alphacapital.superapp.pms.utils.ApiUtils;
import com.application.alphacapital.superapp.pms.utils.AppUtils;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PerformanceActivity extends AppCompatActivity
{
    private Activity activity;
    private TabLayout tabs;
    private ViewPager pager;
    private String[] tabsTitle = {"Since Inception", "Overall","XIRR Funds"};
    private TabAdapter mAdapter;

    private LinearLayout llBack ;

    private View llLoading,llNoInternet,llNoData;

    private ArrayList<PerfomanceResponseModel.SinceInceptionDetailsItem> listSinceInception ;
    private ArrayList<PerfomanceResponseModel.PerformanceDetailsItem> listPerformanceOverall;
    private ArrayList<PerfomanceResponseModel.PerformanceDetailsCateItem> listXIRR;

    public static String AmountInvested = "" , CurrentValue = "" , HoldingAmount = "" , TotalDays = "" , Gain = "" , Abreturn = "" , Xirr = "" ;
    public static String PerformanceAmountInvested = "" , PerformanceCurrentValue = "" , PerformanceHoldingAmount = "" , PerformanceTotalDays = "" , PerformanceGain = "" , PerformanceAbreturn = "" , PerformanceXirr = "" ;

    private PortfolioApiInterface apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.portfolio_activity_performance_tabs);

            activity = PerformanceActivity.this;
            apiService = PortfolioApiClient.getClient().create(PortfolioApiInterface.class);
            initView();

            if(AppUtils.isOnline())
            {
                llNoInternet.setVisibility(View.GONE);
                getPerformanceActivityData();
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
            txtTitle.setText("Performance");

            ImageView imgPageIcon = findViewById(R.id.imgPageIcon);
            imgPageIcon.setImageResource(R.drawable.portfolio_ic_performance_white);

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
        llBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
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
        });
    }

    /**
     * Call on server to get the performance section data
     */
    private void getPerformanceActivityData()
    {
        llLoading.setVisibility(View.VISIBLE);
        listSinceInception = new ArrayList<>();
        listPerformanceOverall = new ArrayList<>();
        listXIRR = new ArrayList<>();
        apiService.getPerfomaceAPI(ApiUtils.END_USER_ID).enqueue(new Callback<PerfomanceResponseModel>() {
            @Override
            public void onResponse(Call<PerfomanceResponseModel> call, Response<PerfomanceResponseModel> response) {
                if (response.isSuccessful())
                {
                    if (response.body().getSuccess() == 1)
                    {
                        listSinceInception.addAll(response.body().getSince_inception().getSince_inception_details());

                        AmountInvested = AppUtils.getValidAPIStringResponse(String.valueOf(response.body().getSince_inception().getGrand_total().getAmountInvested()));
                        CurrentValue = AppUtils.getValidAPIStringResponse(String.valueOf(response.body().getSince_inception().getGrand_total().getCurrentValue()));
                        HoldingAmount = AppUtils.getValidAPIStringResponse(String.valueOf(response.body().getSince_inception().getGrand_total().getHolding_amount()));
                        TotalDays = AppUtils.getValidAPIStringResponse(String.valueOf(response.body().getSince_inception().getGrand_total().getTotal_days()));
                        Gain = AppUtils.getValidAPIStringResponse(String.valueOf(response.body().getSince_inception().getGrand_total().getGain()));
                        Abreturn = AppUtils.getValidAPIStringResponse(String.valueOf(response.body().getSince_inception().getGrand_total().getAbreturn()));
                        Xirr = AppUtils.getValidAPIStringResponse(String.valueOf(response.body().getSince_inception().getGrand_total().getXirr()));

                        listPerformanceOverall.addAll(response.body().getPerformance_details());
                        PerformanceAmountInvested = AppUtils.getValidAPIStringResponse(String.valueOf(response.body().getPerformance_details_total().getAmountInvested()));
                        PerformanceCurrentValue = AppUtils.getValidAPIStringResponse(String.valueOf(response.body().getPerformance_details_total().getCurrentValue()));
                        PerformanceHoldingAmount = AppUtils.getValidAPIStringResponse(String.valueOf(response.body().getPerformance_details_total().getHolding_amount()));
                        PerformanceTotalDays = AppUtils.getValidAPIStringResponse(String.valueOf(response.body().getPerformance_details_total().getTotal_days()));
                        PerformanceGain = AppUtils.getValidAPIStringResponse(String.valueOf(response.body().getPerformance_details_total().getGain()));
                        PerformanceAbreturn = AppUtils.getValidAPIStringResponse(String.valueOf(response.body().getPerformance_details_total().getAbreturn()));
                        PerformanceXirr = AppUtils.getValidAPIStringResponse(String.valueOf(response.body().getPerformance_details_total().getXirr()));

                        listXIRR.addAll(response.body().getPerformance_details_cate());

                        llLoading.setVisibility(View.GONE);

                        setupPagerData();
                    }
                    else
                    {
                        llLoading.setVisibility(View.GONE);
                        llNoData.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    llLoading.setVisibility(View.GONE);
                    llNoData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<PerfomanceResponseModel> call, Throwable t) {
                AppUtils.printLog("Failur", t.getMessage());
                llLoading.setVisibility(View.GONE);
                llNoData.setVisibility(View.VISIBLE);
            }
        });

       /* new AsyncTask<Void, Void, Void>()
        {
            int status = -1;

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                llLoading.setVisibility(View.VISIBLE);
                listSinceInception = new ArrayList<>();
                listPerformanceOverall = new ArrayList<>();
                listXIRR = new ArrayList<>();
            }

            @Override
            protected Void doInBackground(Void... voids)
            {
                try
                {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("user_id", ApiUtils.END_USER_ID);

                    String serverResponse = MitsUtils.readJSONServiceUsingPOST(ApiUtils.GET_PERFORMANCE, hashMap);

                    AppUtils.printLog("Performance",serverResponse);

                    JSONObject jsonObject = new JSONObject(serverResponse);

                    status = AppUtils.getValidAPIIntegerResponse(jsonObject.optString("success"));

                    if (status == 1)
                    {
                        JSONObject sinceInceptionObj = jsonObject.optJSONObject("since_inception");
                        JSONArray sinceInceptionArray = sinceInceptionObj.optJSONArray("since_inception_details");
                        if(sinceInceptionArray.length() > 0)
                        {
                            for (int i = 0; i < sinceInceptionArray.length(); i++)
                            {
                                JSONObject sinceInceptionDataObj = sinceInceptionArray.getJSONObject(i);

                                SinceInceptionBean sinceInceptionBean = new SinceInceptionBean();
                                sinceInceptionBean.setAmountInvested(AppUtils.getValidAPIDoubleResponse(sinceInceptionDataObj.optString("AmountInvested")));
                                sinceInceptionBean.setHoldingValue(AppUtils.getValidAPIDoubleResponse(sinceInceptionDataObj.optString("HoldingValue")));
                                sinceInceptionBean.setAsset_type(AppUtils.getValidAPIStringResponse(sinceInceptionDataObj.optString("asset_type")));
                                sinceInceptionBean.setCurrentValue(AppUtils.getValidAPIIntegerResponse(sinceInceptionDataObj.optString("CurrentValue")));
                                sinceInceptionBean.setTotal_days(AppUtils.getValidAPIDoubleResponse(sinceInceptionDataObj.optString("total_days")));
                                sinceInceptionBean.setGain(AppUtils.getValidAPIDoubleResponse(sinceInceptionDataObj.optString("gain")));
                                sinceInceptionBean.setAbreturn(AppUtils.getValidAPIDoubleResponse(sinceInceptionDataObj.optString("Abreturn")));
                                sinceInceptionBean.setXirr(AppUtils.getValidAPIDoubleResponse(sinceInceptionDataObj.optString("xirr")));

                                listSinceInception.add(sinceInceptionBean);
                            }
                        }

                        JSONObject sinceInceptionTotalObj = sinceInceptionObj.optJSONObject("grand_total");
                        AmountInvested = AppUtils.getValidAPIStringResponse(sinceInceptionTotalObj.optString("AmountInvested"));
                        CurrentValue = AppUtils.getValidAPIStringResponse(sinceInceptionTotalObj.optString("CurrentValue"));
                        HoldingAmount = AppUtils.getValidAPIStringResponse(sinceInceptionTotalObj.optString("holding_amount"));
                        TotalDays = AppUtils.getValidAPIStringResponse(sinceInceptionTotalObj.optString("total_days"));
                        Gain = AppUtils.getValidAPIStringResponse(sinceInceptionTotalObj.optString("gain"));
                        Abreturn = AppUtils.getValidAPIStringResponse(sinceInceptionTotalObj.optString("Abreturn"));
                        Xirr = AppUtils.getValidAPIStringResponse(sinceInceptionTotalObj.optString("xirr"));

                        JSONArray performanceDataArray = jsonObject.optJSONArray("performance_details");
                        if(performanceDataArray.length() > 0)
                        {
                            for (int i = 0; i < performanceDataArray.length(); i++)
                            {
                                JSONObject performanceOverallDataObj = performanceDataArray.getJSONObject(i);

                                PerformanceOverallBean performanceOverallBean = new PerformanceOverallBean();
                                performanceOverallBean.setPresentAmount(AppUtils.getValidAPIStringResponse(performanceOverallDataObj.optString("PresentAmount")));
                                performanceOverallBean.setSub_category(AppUtils.getValidAPIStringResponse(performanceOverallDataObj.optString("sub_category")));
                                performanceOverallBean.setCategory(AppUtils.getValidAPIStringResponse(performanceOverallDataObj.optString("category")));
                                performanceOverallBean.setSchemeName(AppUtils.getValidAPIStringResponse(performanceOverallDataObj.optString("SchemeName")));
                                performanceOverallBean.setTranDate(AppUtils.getValidAPIStringResponse(performanceOverallDataObj.optString("TranDate")));
                                performanceOverallBean.setFolioNo(AppUtils.getValidAPIStringResponse(performanceOverallDataObj.optString("FolioNo")));
                                performanceOverallBean.setAmount(AppUtils.getValidAPIStringResponse(performanceOverallDataObj.optString("Amount")));
                                performanceOverallBean.setHolder(AppUtils.getValidAPIStringResponse(performanceOverallDataObj.optString("Holder")));
                                performanceOverallBean.setStatus(AppUtils.getValidAPIStringResponse(performanceOverallDataObj.optString("Status")));
                                performanceOverallBean.setUnits(AppUtils.getValidAPIStringResponse(performanceOverallDataObj.optString("Units")));
                                performanceOverallBean.setNav(AppUtils.getValidAPIStringResponse(performanceOverallDataObj.optString("Nav")));
                                performanceOverallBean.setTranTimestamp(AppUtils.getValidAPIIntegerResponse(performanceOverallDataObj.optString("TranTimestamp")));
                                performanceOverallBean.setHoldingDays(AppUtils.getValidAPIIntegerResponse(performanceOverallDataObj.optString("holdingDays")));
                                performanceOverallBean.setHoldingValue(AppUtils.getValidAPIDoubleResponse(performanceOverallDataObj.optString("HoldingValue")));

                                listPerformanceOverall.add(performanceOverallBean);
                            }
                        }

                        JSONObject performanceOverallTotalObj = jsonObject.optJSONObject("performance_details_total");
                        PerformanceAmountInvested = AppUtils.getValidAPIStringResponse(performanceOverallTotalObj.optString("AmountInvested"));
                        PerformanceCurrentValue = AppUtils.getValidAPIStringResponse(performanceOverallTotalObj.optString("CurrentValue"));
                        PerformanceHoldingAmount = AppUtils.getValidAPIStringResponse(performanceOverallTotalObj.optString("holding_amount"));
                        PerformanceTotalDays = AppUtils.getValidAPIStringResponse(performanceOverallTotalObj.optString("total_days"));
                        PerformanceGain = AppUtils.getValidAPIStringResponse(performanceOverallTotalObj.optString("gain"));
                        PerformanceAbreturn = AppUtils.getValidAPIStringResponse(performanceOverallTotalObj.optString("Abreturn"));
                        PerformanceXirr = AppUtils.getValidAPIStringResponse(performanceOverallTotalObj.optString("xirr"));

                        JSONArray xirrFundArray = jsonObject.optJSONArray("performance_details_cate");
                        if(xirrFundArray.length() > 0)
                        {
                            for (int i = 0; i < xirrFundArray.length(); i++)
                            {
                                JSONObject xirrFundObj = xirrFundArray.getJSONObject(i);

                                PerformanceXIRRFundsBean performanceXIRRFundsBean = new PerformanceXIRRFundsBean();

                                performanceXIRRFundsBean.setPortfolio_key(AppUtils.getValidAPIStringResponse(xirrFundObj.optString("portfolio_key")));

                                JSONArray xirrFundValueArray = xirrFundObj.optJSONArray("portfolio_value");
                                if(xirrFundValueArray.length() > 0)
                                {
                                    ArrayList<PerformanceXIRRFundsBean.PortfolioValueGetSet> listXirrValue = new ArrayList<>();
                                    for (int j = 0; j < xirrFundValueArray.length(); j++)
                                    {
                                        JSONObject xirrFundValueObj = xirrFundValueArray.getJSONObject(j);

                                        PerformanceXIRRFundsBean.PortfolioValueGetSet portfolioValueGetSet = new PerformanceXIRRFundsBean.PortfolioValueGetSet();

                                        portfolioValueGetSet.setPresentAmount(AppUtils.getValidAPIStringResponse(xirrFundValueObj.optString("PresentAmount")));
                                        portfolioValueGetSet.setSub_category(AppUtils.getValidAPIStringResponse(xirrFundValueObj.optString("sub_category")));
                                        portfolioValueGetSet.setCategory(AppUtils.getValidAPIStringResponse(xirrFundValueObj.optString("category")));
                                        portfolioValueGetSet.setSchemeName(AppUtils.getValidAPIStringResponse(xirrFundValueObj.optString("SchemeName")));
                                        portfolioValueGetSet.setTranDate(AppUtils.getValidAPIStringResponse(xirrFundValueObj.optString("TranDate")));
                                        portfolioValueGetSet.setFolioNo(AppUtils.getValidAPIStringResponse(xirrFundValueObj.optString("FolioNo")));
                                        portfolioValueGetSet.setAmount(AppUtils.getValidAPIStringResponse(xirrFundValueObj.optString("Amount")));
                                        portfolioValueGetSet.setHolder(AppUtils.getValidAPIStringResponse(xirrFundValueObj.optString("Holder")));
                                        portfolioValueGetSet.setStatus(AppUtils.getValidAPIStringResponse(xirrFundValueObj.optString("Status")));
                                        portfolioValueGetSet.setUnits(AppUtils.getValidAPIStringResponse(xirrFundValueObj.optString("Units")));
                                        portfolioValueGetSet.setNav(AppUtils.getValidAPIStringResponse(xirrFundValueObj.optString("Nav")));
                                        portfolioValueGetSet.setTranTimestamp(AppUtils.getValidAPIIntegerResponse(xirrFundValueObj.optString("TranTimestamp")));
                                        portfolioValueGetSet.setHoldingDays(AppUtils.getValidAPIIntegerResponse(xirrFundValueObj.optString("holdingDays")));
                                        portfolioValueGetSet.setHoldingValue(AppUtils.getValidAPIDoubleResponse(xirrFundValueObj.optString("HoldingValue")));

                                        listXirrValue.add(portfolioValueGetSet);
                                    }

                                    performanceXIRRFundsBean.setPortfolio_value(listXirrValue);
                                }

                                JSONObject performanceValueTotalObj = xirrFundObj.optJSONObject("portfolio_total");

                                PerformanceXIRRFundsBean.PortfolioTotalGetSet portfolioTotalGetSet = new PerformanceXIRRFundsBean.PortfolioTotalGetSet();
                                portfolioTotalGetSet.setCurrentValue(AppUtils.getValidAPIIntegerResponse(performanceValueTotalObj.optString("CurrentValue")));
                                portfolioTotalGetSet.setAmountInvested(AppUtils.getValidAPIDoubleResponse(performanceValueTotalObj.optString("AmountInvested")));
                                portfolioTotalGetSet.setHoldingValue(AppUtils.getValidAPIDoubleResponse(performanceValueTotalObj.optString("HoldingValue")));
                                portfolioTotalGetSet.setGain(AppUtils.getValidAPIDoubleResponse(performanceValueTotalObj.optString("gain")));
                                portfolioTotalGetSet.setTotal_days(AppUtils.getValidAPIDoubleResponse(performanceValueTotalObj.optString("total_days")));
                                portfolioTotalGetSet.setAbreturn(AppUtils.getValidAPIDoubleResponse(performanceValueTotalObj.optString("Abreturn")));
                                portfolioTotalGetSet.setXirr(AppUtils.getValidAPIDoubleResponse(performanceValueTotalObj.optString("xirr")));
                                performanceXIRRFundsBean.setPortfolio_total(portfolioTotalGetSet);

                                listXIRR.add(performanceXIRRFundsBean);
                            }
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

        @Override
        public Fragment getItem(int position)
        {

            if(position == 0)
            {
                // AllCoinsFragment
                Fragment fragment = new SinceInceptionFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("data",listSinceInception);
                fragment.setArguments(bundle);
                return fragment;
            }
            else if(position == 1)
            {
                // AllCoinsFragment
                Fragment fragment = new PerformanceOverallFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("data",listPerformanceOverall);
                fragment.setArguments(bundle);
                return fragment;
            }
            else
            {
                Fragment fragment = new PerforanceFundsFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("data",listXIRR);
                fragment.setArguments(bundle);
                return fragment;
            }
        }

        @Override
        public int getCount()
        {
            return tabsTitle.length;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
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
