package com.application.alphacapital.superapp.pms.activity;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.alphacapital.superapp.R;
import com.application.alphacapital.superapp.pms.beans.ApplicationFundSchemesResponseModel;
import com.application.alphacapital.superapp.pms.beans.Last30DaysDataResponse;
import com.application.alphacapital.superapp.pms.fragment.AllocationSchemeFundFragment;
import com.application.alphacapital.superapp.pms.network.PortfolioApiClient;
import com.application.alphacapital.superapp.pms.network.PortfolioApiInterface;
import com.application.alphacapital.superapp.pms.utils.ApiUtils;
import com.application.alphacapital.superapp.pms.utils.AppUtils;
import com.application.alphacapital.superapp.pms.utils.SessionManager;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimationType;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SchemeFundListActivity extends AppCompatActivity
{
    private Activity activity;
    private SessionManager sessionManager;
    private LinearLayout llBack;
    private View llLoading, llNoInternet, llNoData;
    private CardView llScheme,llFund;
    private RecyclerView rvAllocationScheme, rvAllocationFund;
    private PortfolioApiInterface apiService;
    private ArrayList<ApplicationFundSchemesResponseModel.FundSchemeListItem> listScheme =  new ArrayList<>();
    private ArrayList<ApplicationFundSchemesResponseModel.FundSchemesItem> listFund =  new ArrayList<>();

    private String isFor = "";

    @Override protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.portfolio_activity_scheme_fund);
            activity = SchemeFundListActivity.this;
            apiService = PortfolioApiClient.getClient().create(PortfolioApiInterface.class);
            isFor = getIntent().getStringExtra("isFor");
            initView();
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

            ImageView imgPageIcon = findViewById(R.id.imgPageIcon);
            imgPageIcon.setImageResource(R.drawable.portfolio_ic_sip_stp);
            llBack = findViewById(R.id.llBack);
            llLoading = findViewById(R.id.llLoading);
            llNoInternet = findViewById(R.id.llNoInternet);
            llNoData = findViewById(R.id.llNoData);
            llScheme = findViewById(R.id.llScheme);
            llFund = findViewById(R.id.llFund);

            rvAllocationScheme = findViewById(R.id.rvAllocationScheme);
            rvAllocationScheme.setLayoutManager(new LinearLayoutManager(activity));

            rvAllocationFund = findViewById(R.id.rvAllocationFund);
            rvAllocationFund.setLayoutManager(new LinearLayoutManager(activity));
            
            if(isFor.equals("Scheme"))
            {
                txtTitle.setText("Scheme Allocation");
                llScheme.setVisibility(View.VISIBLE);
                llFund.setVisibility(View.GONE);
            }
            else
            {
                txtTitle.setText("Fund House Allocation");
                llScheme.setVisibility(View.GONE);
                llFund.setVisibility(View.VISIBLE);
            }
            
            if (AppUtils.isOnline())
            {
                llNoInternet.setVisibility(View.GONE);
                getFundSchemeApi();
            }
            else
            {
                llNoInternet.setVisibility(View.VISIBLE);
            }
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

    private void getFundSchemeApi()
    {
        llLoading.setVisibility(View.VISIBLE);
        apiService.getFundSchemes(ApiUtils.END_USER_ID).enqueue(new Callback<ApplicationFundSchemesResponseModel>()
        {
            @Override public void onResponse(Call<ApplicationFundSchemesResponseModel> call, Response<ApplicationFundSchemesResponseModel> response)
            {
                if (response.isSuccessful())
                {
                    if(response.body().getSuccess() ==1)
                    {
                        llNoData.setVisibility(View.GONE);
                        listScheme.addAll(response.body().getFund_scheme_list());
                        ApplicationFundSchemesResponseModel.FundSchemeListItem schemeTotal = new ApplicationFundSchemesResponseModel.FundSchemeListItem();
                        schemeTotal.setCurrentValue(String.valueOf(response.body().getFund_scheme_list_total().getPresentAmountTotal()));
                        schemeTotal.setAllocationTotal(response.body().getFund_scheme_list_total().getAllocationTotal());
                        schemeTotal.setTotal(true);
                        listScheme.add(schemeTotal);

                        listFund.addAll(response.body().getFund_schemes());
                        ApplicationFundSchemesResponseModel.FundSchemesItem fundTotal =  new ApplicationFundSchemesResponseModel.FundSchemesItem();
                        fundTotal.setPresentAmountTotal(response.body().getFund_schemes_total().getPresentAmountTotal());
                        fundTotal.setAllocationTotal(response.body().getFund_schemes_total().getAllocationTotal());
                        fundTotal.setTotal(true);
                        listFund.add(fundTotal);

                        if(listScheme != null && listScheme.size() > 0)
                        {
                            llNoData.setVisibility(View.GONE);

                            AllocationSchemeAdapter allocationSchemeAdapter = new AllocationSchemeAdapter(listScheme);
                            rvAllocationScheme.setAdapter(allocationSchemeAdapter);

                            AllocationFundAdapter allocationFundAdapter = new AllocationFundAdapter(listFund);
                            rvAllocationFund.setAdapter(allocationFundAdapter);
                        }
                        else
                        {
                            llNoData.setVisibility(View.VISIBLE);
                        }
                    }
                    else 
                    {
                        llNoData.setVisibility(View.VISIBLE);
                    }

                    llLoading.setVisibility(View.GONE);
                }
                else
                {
                    llLoading.setVisibility(View.GONE);
                    llNoData.setVisibility(View.VISIBLE);
                }
            }

            @Override public void onFailure(Call<ApplicationFundSchemesResponseModel> call, Throwable t)
            {
                AppUtils.printLog("FAIL 3", t.getMessage());
                llLoading.setVisibility(View.GONE);
                llNoData.setVisibility(View.VISIBLE);
            }
        });
    }

    private class AllocationSchemeAdapter extends RecyclerView.Adapter<AllocationSchemeAdapter.ViewHolder>
    {
        ArrayList<ApplicationFundSchemesResponseModel.FundSchemeListItem> listItems;

        AllocationSchemeAdapter(ArrayList<ApplicationFundSchemesResponseModel.FundSchemeListItem> list)
        {
            this.listItems = list;
        }

        @Override
        public AllocationSchemeAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.portfolio_row_view_allocation_scheme, viewGroup, false);
            return new AllocationSchemeAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final AllocationSchemeAdapter.ViewHolder holder, final int position)
        {
            try
            {
                final ApplicationFundSchemesResponseModel.FundSchemeListItem getSet = listItems.get(position);

                if (position % 2 == 0)
                {
                    holder.llMainAllocation.setBackgroundResource(R.color.white);
                }
                else
                {
                    holder.llMainAllocation.setBackgroundResource(R.color.light_gray_new);
                }


                if(getSet.isTotal())
                {
                    holder.llMainAllocation.setVisibility(View.GONE);
                    holder.llTotalAllocation.setVisibility(View.VISIBLE);
                    holder.txtTotalCurrentValue.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToCommaSeperatedValue(getSet.getCurrentValue()));
                    holder.txtTotalAllocation.setText(String.valueOf(Math.round(getSet.getAllocationTotal()) + "%"));
                }
                else
                {
                    holder.llMainAllocation.setVisibility(View.VISIBLE);
                    holder.llTotalAllocation.setVisibility(View.GONE);

                    holder.txtAllocationSchemeName.setText(AppUtils.toDisplayCase(getSet.getSchemeName()));
                    holder.txtAllocationType.setText(getSet.getCategory());
                    holder.txtAllocationValue.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToCommaSeperatedValue(String.valueOf(AppUtils.getValidAPIStringResponse(getSet.getCurrentValue()))));
                    holder.txtAllocation.setText(String.valueOf(Math.round(getSet.getAllocation()))+ "%");
                }
            }
            catch (Resources.NotFoundException e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount()
        {
            return listItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            private TextView txtAllocationSchemeName, txtAllocationType, txtAllocationValue, txtAllocation, txtTotalCurrentValue, txtTotalAllocation;
            private LinearLayout llMainAllocation, llTotalAllocation;

            ViewHolder(View convertView)
            {
                super(convertView);
                txtAllocationSchemeName = convertView.findViewById(R.id.txtAllocationSchemeName);
                txtAllocationType = convertView.findViewById(R.id.txtAllocationType);
                txtAllocationValue = convertView.findViewById(R.id.txtAllocationValue);
                txtAllocation = convertView.findViewById(R.id.txtAllocation);
                txtTotalCurrentValue = convertView.findViewById(R.id.txtTotalCurrentValue);
                txtTotalAllocation = convertView.findViewById(R.id.txtTotalAllocation);
                llMainAllocation = convertView.findViewById(R.id.llMainAllocation);
                llTotalAllocation = convertView.findViewById(R.id.llTotalAllocation);
            }
        }
    }

    private class AllocationFundAdapter extends RecyclerView.Adapter<AllocationFundAdapter.ViewHolder>
    {
        ArrayList<ApplicationFundSchemesResponseModel.FundSchemesItem> listItems;

        AllocationFundAdapter(ArrayList<ApplicationFundSchemesResponseModel.FundSchemesItem> list)
        {
            this.listItems = list;
        }

        @Override
        public AllocationFundAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.portfolio_row_view_allocation_fund, viewGroup, false);
            return new AllocationFundAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final AllocationFundAdapter.ViewHolder holder, final int position)
        {
            try
            {
                final ApplicationFundSchemesResponseModel.FundSchemesItem getSet = listItems.get(position);

                if (position % 2 == 0)
                {
                    holder.llMainAllocationFund.setBackgroundResource(R.color.white);
                }
                else
                {
                    holder.llMainAllocationFund.setBackgroundResource(R.color.light_gray_new);
                }

                if(getSet.isTotal())
                {
                    holder.llMainAllocationFund.setVisibility(View.GONE);
                    holder.llTotalAllocationFund.setVisibility(View.VISIBLE);
                    holder.txtTotalCurrentFund.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToCommaSeperatedValue(String.valueOf(getSet.getPresentAmountTotal())));
                    holder.txtTotalAllocationFund.setText(String.valueOf(Math.round(getSet.getAllocationTotal()) + "%"));
                }
                else
                {
                    holder.llMainAllocationFund.setVisibility(View.VISIBLE);
                    holder.llTotalAllocationFund.setVisibility(View.GONE);
                    holder.txtAllocationFundName.setText(AppUtils.toDisplayCase(getSet.getFundName()));
                    holder.txtCurrentValue.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToCommaSeperatedValue(String.valueOf(getSet.getCurrentValue())));
                    holder.txtFundAllocation.setText(String.valueOf(Math.round(getSet.getAllocation())) + "%");
                }
            }
            catch (Resources.NotFoundException e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount()
        {
            return listItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            private TextView txtAllocationFundName, txtCurrentValue, txtTotalAllocationFund,txtFundAllocation,txtTotalCurrentFund;
            private LinearLayout llMainAllocationFund, llTotalAllocationFund;

            ViewHolder(View convertView)
            {
                super(convertView);
                txtAllocationFundName = convertView.findViewById(R.id.txtAllocationFundName);
                txtCurrentValue = convertView.findViewById(R.id.txtCurrentValue);
                txtTotalAllocationFund = convertView.findViewById(R.id.txtTotalAllocationFund);
                txtTotalCurrentFund = convertView.findViewById(R.id.txtTotalCurrentFund);
                llMainAllocationFund = convertView.findViewById(R.id.llMainAllocationFund);
                llTotalAllocationFund = convertView.findViewById(R.id.llTotalAllocationFund);
                txtFundAllocation = convertView.findViewById(R.id.txtFundAllocation);
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
