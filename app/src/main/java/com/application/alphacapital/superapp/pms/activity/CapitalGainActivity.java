package com.application.alphacapital.superapp.pms.activity;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.alphacapital.superapp.R;
import com.application.alphacapital.superapp.pms.beans.BsYearsResponseModel;
import com.application.alphacapital.superapp.pms.beans.CapitalGainReportResponseModel;
import com.application.alphacapital.superapp.pms.beans.CapitalGainResponseModel;
import com.application.alphacapital.superapp.pms.network.PortfolioApiClient;
import com.application.alphacapital.superapp.pms.network.PortfolioApiInterface;
import com.application.alphacapital.superapp.pms.utils.ApiUtils;
import com.application.alphacapital.superapp.pms.utils.AppUtils;
import com.application.alphacapital.superapp.pms.utils.SessionManager;
import com.application.alphacapital.superapp.pms.utils.TinyDB;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CapitalGainActivity extends AppCompatActivity
{
    private Activity activity;

    private SessionManager sessionManager;

    private ProgressBar pbToolbar;
    private FrameLayout flSpinner;
    private Spinner spnrYears;

    private LinearLayout llBack;

    private View llLoading, llNoInternet, llNoData, llApplicantsSummary;

    private RecyclerView rvCapitalGainSummaryList;

    private ArrayList<CapitalGainReportResponseModel.PortfolioListItem> listCapitalGain;

    private ArrayList<CapitalGainResponseModel.SaleValuesItem> listCapitalGainSummary = new ArrayList<CapitalGainResponseModel.SaleValuesItem>();

    private TinyDB tinyDB;

    private ArrayList<String> listYears;

    private String strYear = "";

    private TextView txtSTCGAmountGrand;
    private TextView txtLTCGAmountGrand;
    private TextView txtTotalAmountGrand;

    private PortfolioApiInterface apiService;

    String GrandCapitalGainTotal = "", Grand_STCL_total = "", Grand_STCG_total = "", Grand_LTCL_total = "", Grand_LTCG_total = "", GrandPurchaseValTotal = "", GrandSellValTotal = "";

    //summary
    String CapitalGain_GrandTotal = "", LTCG_GrandTotal = "", STCL_GrandTotal = "", STCG_GrandTotal = "", LTCL_GrandTotal = "";

    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.portfolio_activity_capital_gain);

        activity = CapitalGainActivity.this;
        tinyDB = new TinyDB(activity);
        sessionManager = new SessionManager(activity);
        apiService = PortfolioApiClient.getClient().create(PortfolioApiInterface.class);

        initViews();

        if (AppUtils.isOnline())
        {
            llNoInternet.setVisibility(View.GONE);
            getYears();
        }
        else
        {
            llNoInternet.setVisibility(View.VISIBLE);
        }

        onClicks();
    }

    private void initViews()
    {
        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("Capital Gain");

        ImageView imgPageIcon = findViewById(R.id.imgPageIcon);
        imgPageIcon.setImageResource(R.drawable.portfolio_ic_capital_gain_white);

        llBack = findViewById(R.id.llBack);

        pbToolbar = findViewById(R.id.pbToolbar);

        flSpinner = findViewById(R.id.flSpinner);
        flSpinner.setVisibility(View.VISIBLE);

        spnrYears = findViewById(R.id.spnrYears);

        llLoading = findViewById(R.id.llLoading);
        llNoInternet = findViewById(R.id.llNoInternet);
        llNoData = findViewById(R.id.llNoData);

        llApplicantsSummary = findViewById(R.id.llApplicantsSummary);
        rvCapitalGainSummaryList = findViewById(R.id.rvCapitalGainSummaryList);
        rvCapitalGainSummaryList.setLayoutManager(new LinearLayoutManager(activity));
        txtSTCGAmountGrand = (TextView) findViewById(R.id.txtSTCGAmountGrand);
        txtLTCGAmountGrand = (TextView) findViewById(R.id.txtLTCGAmountGrand);
        txtTotalAmountGrand = (TextView) findViewById(R.id.txtTotalAmountGrand);
    }

    private void onClicks()
    {
        llBack.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
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

        spnrYears.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                // On selecting a spinner item
                strYear = listYears.get(position);
                getDataForCapitalGain();
            }

            @Override public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
    }

    /**
     * call on server to bind the years to get the year wise data
     */
    private void getYears()
    {
        pbToolbar.setVisibility(View.VISIBLE);
        listYears = new ArrayList<>();
        apiService.getCommonYearAPI().enqueue(new Callback<BsYearsResponseModel>()
        {
            @Override public void onResponse(Call<BsYearsResponseModel> call, Response<BsYearsResponseModel> response)
            {
                if (response.isSuccessful())
                {
                    listYears.addAll(response.body().getYears());
                    pbToolbar.setVisibility(View.GONE);
                    YearsAdapter yearsAdapter = new YearsAdapter(activity, listYears);
                    spnrYears.setAdapter(yearsAdapter);
                }
                else
                {

                }
            }

            @Override public void onFailure(Call<BsYearsResponseModel> call, Throwable t)
            {

            }
        });
    }

    public class YearsAdapter extends BaseAdapter
    {
        ArrayList<String> allSpCoinslist;
        Activity activity;

        public YearsAdapter(Activity activity, ArrayList<String> allSpCoinslist)
        {
            this.activity = activity;
            this.allSpCoinslist = allSpCoinslist;
        }

        @Override public int getCount()
        {
            return allSpCoinslist.size();
        }

        @Override public Object getItem(int position)
        {
            return allSpCoinslist.get(position);
        }

        @Override public long getItemId(int position)
        {
            return position;
        }

        @Override public View getView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowview = inflater.inflate(R.layout.portfolio_row_view_spinner_allcoins, parent, false);
            TextView text = (TextView) rowview.findViewById(R.id.tvOption);
            text.setText(allSpCoinslist.get(position));
            return rowview;
        }

        public View getDropDownView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowview = inflater.inflate(R.layout.portfolio_row_view_spinner_dropdown, parent, false);
            TextView text = (TextView) rowview.findViewById(R.id.tvOption);
            text.setText(allSpCoinslist.get(position));
            return rowview;
        }
    }

    /**
     * call on server to get the CapitalGain section data
     */
    private void getDataForCapitalGain()
    {
        llLoading.setVisibility(View.VISIBLE);
        listCapitalGainSummary = new ArrayList<CapitalGainResponseModel.SaleValuesItem>();
        apiService.getCapitalGainAPI(ApiUtils.END_USER_ID, strYear).enqueue(new Callback<CapitalGainResponseModel>()
        {
            @Override public void onResponse(Call<CapitalGainResponseModel> call, Response<CapitalGainResponseModel> response)
            {
                try
                {
                    if (response.isSuccessful())
                    {
                        if (response.body().getSuccess() == 1)
                        {
                            listCapitalGainSummary.addAll(response.body().getSale_values());

                            CapitalGain_GrandTotal = String.valueOf(AppUtils.getValidAPIDoubleResponse(String.valueOf(response.body().getGrand_total().getCapitalGain_GrandTotal())));
                            LTCG_GrandTotal = String.valueOf(AppUtils.getValidAPIDoubleResponse(String.valueOf(response.body().getGrand_total().getLTCGGrandTotal())));
                            STCL_GrandTotal = String.valueOf(AppUtils.getValidAPIDoubleResponse(String.valueOf(response.body().getGrand_total().getSTCLGrandTotal())));
                            STCG_GrandTotal = String.valueOf(AppUtils.getValidAPIDoubleResponse(String.valueOf(response.body().getGrand_total().getSTCGGrandTotal())));
                            LTCL_GrandTotal = String.valueOf(AppUtils.getValidAPIDoubleResponse(String.valueOf(response.body().getGrand_total().getLTCLGrandTotal())));

                            if (response.body().getSuccess() == 1)
                            {
                                if (listCapitalGainSummary != null && listCapitalGainSummary.size() > 0)
                                {
                                    AppUtils.printLog("<><> Size listCapitalGainSummary :", listCapitalGainSummary.size() + "");
                                    llApplicantsSummary.setVisibility(View.VISIBLE);
                                    CapitalGainApplicantSummaryAdapter capitalGainApplicantSummaryAdapter = new CapitalGainApplicantSummaryAdapter(listCapitalGainSummary);
                                    rvCapitalGainSummaryList.setAdapter(capitalGainApplicantSummaryAdapter);
                                    txtSTCGAmountGrand.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToCommaSeperatedValue(String.valueOf(STCG_GrandTotal))+"\n"+getResources().getString(R.string.rupees) + " " + AppUtils.convertToCommaSeperatedValue(String.valueOf(STCL_GrandTotal)));
                                    txtLTCGAmountGrand.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToCommaSeperatedValue(String.valueOf(LTCG_GrandTotal)) + "\n"+getResources().getString(R.string.rupees) + " " + AppUtils.convertToCommaSeperatedValue(String.valueOf(LTCL_GrandTotal)));
                                    txtTotalAmountGrand.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToCommaSeperatedValue(String.valueOf(CapitalGain_GrandTotal)));
                                }
                                else
                                {
                                    llApplicantsSummary.setVisibility(View.GONE);
                                }
                            }

                            if (listCapitalGainSummary.size() == 0 && listCapitalGain.size() == 0)
                            {
                                llNoData.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                llNoData.setVisibility(View.GONE);
                            }
                        }
                        else
                        {
                            llNoData.setVisibility(View.VISIBLE);
                            llLoading.setVisibility(View.GONE);
                            AppUtils.showSnackBar(llLoading, "No Data Found", activity);
                        }

                        llLoading.setVisibility(View.GONE);
                    }
                    else
                    {
                        llLoading.setVisibility(View.GONE);
                        llNoData.setVisibility(View.VISIBLE);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override public void onFailure(Call<CapitalGainResponseModel> call, Throwable t)
            {
                Log.e("<><> On FAilur : ",t.getMessage() + " <><> ");
                llLoading.setVisibility(View.GONE);
                llNoData.setVisibility(View.VISIBLE);
            }
        });


       /* listCapitalGain = new ArrayList<>();
        apiService.getCapitalGainSingle(ApiUtils.END_USER_ID, strYear).enqueue(new Callback<CapitalGainReportResponseModel>()
        {
            @Override public void onResponse(Call<CapitalGainReportResponseModel> call, Response<CapitalGainReportResponseModel> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().getSuccess() == 1)
                    {
                        listCapitalGain.addAll(response.body().getSale_values().getPortfolio_list());
                        if (listCapitalGain != null && listCapitalGain.size() > 0)
                        {
                            AppUtils.printLog("<><> Size listCapitalGain :", listCapitalGain.size() + "");
                            rvCapitalGain.setVisibility(View.VISIBLE);
                            CapitalGainAdapter capitalGainAdapter = new CapitalGainAdapter(listCapitalGain);
                            rvCapitalGain.setAdapter(capitalGainAdapter);
                        }
                        else
                        {
                            rvCapitalGain.setVisibility(View.GONE);
                        }
                    }
                    else
                    {
                        llLoading.setVisibility(View.GONE);
                        llNoData.setVisibility(View.VISIBLE);
                    }
                    llLoading.setVisibility(View.GONE);
                    Log.e("<><><>LIST === <><><>", listCapitalGain.toString());
                }
                else
                {
                    llLoading.setVisibility(View.GONE);
                    llNoData.setVisibility(View.VISIBLE);
                }
            }

            @Override public void onFailure(Call<CapitalGainReportResponseModel> call, Throwable t)
            {
                AppUtils.printLog("FAILURE", t.getMessage());
            }
        });*/
    }

    private class CapitalGainApplicantSummaryAdapter extends RecyclerView.Adapter<CapitalGainApplicantSummaryAdapter.ViewHolder>
    {
        ArrayList<CapitalGainResponseModel.SaleValuesItem> listItems;
        private SpringyAdapterAnimator mAnimator;

        CapitalGainApplicantSummaryAdapter(ArrayList<CapitalGainResponseModel.SaleValuesItem> list)
        {
            this.listItems = list;
        }

        @Override public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.portfolio_row_view_capital_gain_applicants_summary, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            try
            {
                final CapitalGainResponseModel.SaleValuesItem getSet = listItems.get(position);
                holder.txtApplicantName.setText(AppUtils.toDisplayCase(getSet.getApplicant()));
                holder.txtSTCGAmount.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToCommaSeperatedValue(String.valueOf(getSet.getSchemeTotal().getSTCGTotal()))+"\n"+getResources().getString(R.string.rupees) + " " + AppUtils.convertToCommaSeperatedValue(String.valueOf(getSet.getSchemeTotal().getSTCLTotal())));
                holder.txtLTCGAmount.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToCommaSeperatedValue(String.valueOf(getSet.getSchemeTotal().getLTCGTotal()))+"\n"+getResources().getString(R.string.rupees) + " " + AppUtils.convertToCommaSeperatedValue(String.valueOf(getSet.getSchemeTotal().getLTCLTotal())));
                holder.txtTotalAmount.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToCommaSeperatedValue(String.valueOf(getSet.getSchemeTotal().getCapitalGainTotal())));
                AppUtils.printLog("GetValue Size", getSet.getValue().size() + "Aacbd");
                if (getSet.getValue() != null && getSet.getValue().size() > 0)
                {
                    CapitalGainApplicantSummaryValueAdapter capitalGainApplicantSummaryValueAdapter = new CapitalGainApplicantSummaryValueAdapter(getSet.getValue());
                    holder.rvApplicantValue.setAdapter(capitalGainApplicantSummaryValueAdapter);
                }
            }
            catch (Resources.NotFoundException e)
            {
                e.printStackTrace();
            }
        }

        @Override public int getItemCount()
        {
            return listItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            private TextView txtApplicantName;
            private RecyclerView rvApplicantValue;
            private TextView txtSTCGAmount;
            private TextView txtLTCGAmount;
            private TextView txtTotalAmount;

            ViewHolder(View convertView)
            {
                super(convertView);

                txtApplicantName = (TextView) convertView.findViewById(R.id.txtApplicantName);
                rvApplicantValue = (RecyclerView) convertView.findViewById(R.id.rvApplicantValue);
                txtSTCGAmount = (TextView) convertView.findViewById(R.id.txtSTCGAmount);
                txtLTCGAmount = (TextView) convertView.findViewById(R.id.txtLTCGAmount);
                txtTotalAmount = (TextView) convertView.findViewById(R.id.txtTotalAmount);
                rvApplicantValue.setLayoutManager(new LinearLayoutManager(activity));

            }
        }
    }

    private class CapitalGainApplicantSummaryValueAdapter extends RecyclerView.Adapter<CapitalGainApplicantSummaryValueAdapter.ViewHolder>
    {
        ArrayList<CapitalGainResponseModel.ValueItem> listItems;
        private SpringyAdapterAnimator mAnimator;

        CapitalGainApplicantSummaryValueAdapter(ArrayList<CapitalGainResponseModel.ValueItem> list)
        {
            this.listItems = list;
        }

        @Override public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.portfolio_row_view_capital_gain_applicants_summary_value, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            try
            {
                final CapitalGainResponseModel.ValueItem getSet = listItems.get(position);

                if (position % 2 == 0)
                {
                    holder.llCGMainItems.setBackgroundResource(R.color.white);
                }
                else
                {
                    holder.llCGMainItems.setBackgroundResource(R.color.light_gray_new);
                }

                holder.txtAssetTypeTitle.setText(getSet.getCategory_key());
                holder.txtSTCGAmount.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToCommaSeperatedValue(String.valueOf(getSet.getSTCGTotal()))+"\n"+getResources().getString(R.string.rupees) + " " + AppUtils.convertToCommaSeperatedValue(String.valueOf(getSet.getSTCLTotal())));
                holder.txtLTCGAmount.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToCommaSeperatedValue(String.valueOf(getSet.getLTCGTotal()))+"\n"+getResources().getString(R.string.rupees) + " " + AppUtils.convertToCommaSeperatedValue(String.valueOf(getSet.getLTCLTotal())));
                holder.txtTotalAmount.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToCommaSeperatedValue(String.valueOf(getSet.getCapital_gain())));
            }
            catch (Resources.NotFoundException e)
            {
                e.printStackTrace();
            }
        }

        @Override public int getItemCount()
        {
            return listItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            private TextView txtAssetTypeTitle;
            private TextView txtSTCGAmount;
            private TextView txtLTCGAmount;
            private TextView txtTotalAmount;
            private LinearLayout llCGMainItems;

            ViewHolder(View convertView)
            {
                super(convertView);
                txtAssetTypeTitle = (TextView) convertView.findViewById(R.id.txtAssetTypeTitle);
                txtSTCGAmount = (TextView) convertView.findViewById(R.id.txtSTCGAmount);
                txtLTCGAmount = (TextView) convertView.findViewById(R.id.txtLTCGAmount);
                txtTotalAmount = (TextView) convertView.findViewById(R.id.txtTotalAmount);
                llCGMainItems = (LinearLayout) convertView.findViewById(R.id.llCGMainItems);
            }
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
