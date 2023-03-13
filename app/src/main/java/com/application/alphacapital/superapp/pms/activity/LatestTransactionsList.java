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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.application.alphacapital.superapp.R;
import com.application.alphacapital.superapp.pms.beans.Last30DaysDataResponse;
import com.application.alphacapital.superapp.pms.network.PortfolioApiClient;
import com.application.alphacapital.superapp.pms.network.PortfolioApiInterface;
import com.application.alphacapital.superapp.pms.utils.ApiUtils;
import com.application.alphacapital.superapp.pms.utils.AppUtils;
import com.application.alphacapital.superapp.pms.utils.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LatestTransactionsList extends AppCompatActivity
{
    private Activity activity;
    private SessionManager sessionManager;
    private LinearLayout llBack;
    private View llLoading, llNoInternet, llNoData;
    private RecyclerView rvSPIData;
    private TextView txtTotalAmount,txtTotalAllocation;
    private PortfolioApiInterface apiService;
    private ArrayList<Last30DaysDataResponse.TransactionDetailsItem> listData = new ArrayList<Last30DaysDataResponse.TransactionDetailsItem>();

    @Override protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.portfolio_activity_latest_sip);
            activity = LatestTransactionsList.this;
            apiService = PortfolioApiClient.getClient().create(PortfolioApiInterface.class);
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
            txtTitle.setText("Latest Transactions - Last 30 days");
            ImageView imgPageIcon = findViewById(R.id.imgPageIcon);
            imgPageIcon.setImageResource(R.drawable.portfolio_ic_transaction);
            llBack = findViewById(R.id.llBack);
            llLoading = findViewById(R.id.llLoading);
            llNoInternet = findViewById(R.id.llNoInternet);
            llNoData = findViewById(R.id.llNoData);
            rvSPIData = findViewById(R.id.rvSPIData);
            txtTotalAmount = findViewById(R.id.txtTotalAmount);
            txtTotalAllocation = findViewById(R.id.txtTotalAllocation);
            if (AppUtils.isOnline())
            {
                llNoInternet.setVisibility(View.GONE);
                getApiData();
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

    private void getApiData()
    {
        listData =  new ArrayList<>();
        llLoading.setVisibility(View.VISIBLE);
        apiService.getLast30DaysData(ApiUtils.END_USER_ID).enqueue(new Callback<Last30DaysDataResponse>()
        {
            @Override public void onResponse(Call<Last30DaysDataResponse> call, Response<Last30DaysDataResponse> response)
            {
                if (response.isSuccessful())
                {
                    if(response.body().getSuccess() == 1)
                    {
                        try
                        {
                            if (response.body().getSipStpDetails().size() > 0)
                            {
                                listData.addAll(response.body().getSipTransactionDetails());

                                if(listData.size() > 1)
                                {
                                    llNoData.setVisibility(View.GONE);
                                    rvSPIData.setAdapter(new SIPDataAdapter(listData));
                                }
                                else
                                {
                                    llNoData.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
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

            @Override public void onFailure(Call<Last30DaysDataResponse> call, Throwable t)
            {
                AppUtils.printLog("Failur", t.getMessage());
                llNoData.setVisibility(View.VISIBLE);
                llLoading.setVisibility(View.GONE);
            }
        });
    }

    private class SIPDataAdapter extends RecyclerView.Adapter<SIPDataAdapter.ViewHolder>
    {
        ArrayList<Last30DaysDataResponse.TransactionDetailsItem> listItem;

        SIPDataAdapter(ArrayList<Last30DaysDataResponse.TransactionDetailsItem> listData)
        {
            this.listItem = listData;
        }

        @Override public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.portfolio_row_view_sip_data_temp, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            try
            {
                final Last30DaysDataResponse.TransactionDetailsItem getSet = listItem.get(position);

                if (position % 2 == 0)
                {
                    holder.llMain.setBackgroundResource(R.color.white);
                }
                else
                {
                    holder.llMain.setBackgroundResource(R.color.light_gray_new);
                }

                holder.txtFundName.setText(AppUtils.toDisplayCase(getSet.getSchemeName()));
                holder.txtFolioNo.setText(getSet.getFolioNo());
                holder.txtTranDate.setText(getSet.getTranDate());
                holder.txtAmount.setText(AppUtils.convertToCommaSeperatedValue(getSet.getAmount()));

                if (position == listItem.size() - 1)
                {
                    holder.txtFundName.setTypeface(AppUtils.getTypefaceBold(activity));

                    holder.txtFolioNo.setTypeface(AppUtils.getTypefaceBold(activity));

                    holder.txtTranDate.setTypeface(AppUtils.getTypefaceBold(activity));

                    holder.txtAmount.setTypeface(AppUtils.getTypefaceBold(activity));
                }
                else
                {
                    holder.txtFundName.setTypeface(AppUtils.getTypefaceRegular(activity));

                    holder.txtFolioNo.setTypeface(AppUtils.getTypefaceRegular(activity));

                    holder.txtTranDate.setTypeface(AppUtils.getTypefaceRegular(activity));

                    holder.txtAmount.setTypeface(AppUtils.getTypefaceRegular(activity));
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
            private TextView txtFundName, txtFolioNo, txtTranDate, txtAmount;
            private LinearLayout llMain;

            ViewHolder(View convertView)
            {
                super(convertView);
                txtFundName = convertView.findViewById(R.id.txtFundName);
                txtFolioNo = convertView.findViewById(R.id.txtFolioNo);
                txtTranDate = convertView.findViewById(R.id.txtTranDate);
                txtAmount = convertView.findViewById(R.id.txtAmount);
                llMain = convertView.findViewById(R.id.llMain);
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
