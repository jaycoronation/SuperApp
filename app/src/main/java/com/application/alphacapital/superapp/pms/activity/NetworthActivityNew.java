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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.application.alphacapital.superapp.R;
import com.application.alphacapital.superapp.pms.beans.NetworthResponseModel;
import com.application.alphacapital.superapp.pms.network.PortfolioApiClient;
import com.application.alphacapital.superapp.pms.network.PortfolioApiInterface;
import com.application.alphacapital.superapp.pms.utils.ApiUtils;
import com.application.alphacapital.superapp.pms.utils.AppUtils;
import com.application.alphacapital.superapp.pms.utils.SessionManager;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworthActivityNew extends AppCompatActivity
{
    private Activity activity;
    private SessionManager sessionManager;
    private LinearLayout llBack;
    private View llLoading, llNoInternet, llNoData;
    private RecyclerView rvNetworth;
    private TextView txtTotalAmount,txtTotalAllocation;
    private PortfolioApiInterface apiService;
    private ArrayList<NetworthResponseModel.AssetsData.AssetsDetails.DebtItem> listNetworth;

    @Override protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.portfolio_activity_networth_new);
            activity = NetworthActivityNew.this;
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
            txtTitle.setText("Networth");
            ImageView imgPageIcon = findViewById(R.id.imgPageIcon);
            imgPageIcon.setImageResource(R.drawable.portfolio_ic_networth_white);

            llBack = findViewById(R.id.llBack);
            llLoading = findViewById(R.id.llLoading);
            llNoInternet = findViewById(R.id.llNoInternet);
            llNoData = findViewById(R.id.llNoData);
            rvNetworth = findViewById(R.id.rvNetworth);
            txtTotalAmount = findViewById(R.id.txtTotalAmount);
            txtTotalAllocation = findViewById(R.id.txtTotalAllocation);
            rvNetworth.setLayoutManager(new LinearLayoutManager(activity));
            if (AppUtils.isOnline())
            {
                llNoInternet.setVisibility(View.GONE);
                getNetworthData();
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

    private void getNetworthData()
    {
        try
        {
            llLoading.setVisibility(View.VISIBLE);
            listNetworth = new ArrayList<>();
            apiService.getNetworthAPI(ApiUtils.END_USER_ID).enqueue(new Callback<NetworthResponseModel>()
            {
                @Override public void onResponse(Call<NetworthResponseModel> call, Response<NetworthResponseModel> response)
                {
                    if (response.isSuccessful())
                    {
                        if (response.body().getSuccess() == 1)
                        {
                            try
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

                                if(listNetworth != null && listNetworth.size() > 0)
                                {
                                    llNoData.setVisibility(View.GONE);
                                    NetworthAssestAdapter networthAssestAdapter = new NetworthAssestAdapter(listNetworth);
                                    rvNetworth.setAdapter(networthAssestAdapter);
                                }
                                else
                                {
                                    llNoData.setVisibility(View.VISIBLE);
                                }

                                txtTotalAmount.setText("₹ "+AppUtils.convertToTwoDecimalValue(String.valueOf(response.body().getAssets_data().getAssets_details_grandtotal().getGrand_total_amount())));
                                txtTotalAllocation.setText(String.valueOf(response.body().getAssets_data().getAssets_details_grandtotal().getGrand_percentage_amount())+ "%");
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                            llLoading.setVisibility(View.GONE);
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
                    llLoading.setVisibility(View.GONE);
                    AppUtils.printLog("FAIL 1", t.getMessage());
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    String assetTitle = "" ;
    private class NetworthAssestAdapter extends RecyclerView.Adapter<NetworthAssestAdapter.ViewHolder>
    {
        ArrayList<NetworthResponseModel.AssetsData.AssetsDetails.DebtItem> listItems;

        NetworthAssestAdapter(ArrayList<NetworthResponseModel.AssetsData.AssetsDetails.DebtItem> list)
        {
            this.listItems = list;
        }

        @Override
        public NetworthAssestAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.portfolio_row_view_networth_assest, viewGroup, false);
            return new NetworthAssestAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final NetworthAssestAdapter.ViewHolder holder, final int position)
        {
            try
            {
                final NetworthResponseModel.AssetsData.AssetsDetails.DebtItem  getSet = listItems.get(position);

                if (position % 2 == 0)
                {
                    holder.llMain.setBackgroundResource(R.color.white);
                }
                else
                {
                    holder.llMain.setBackgroundResource(R.color.light_gray_new);
                }

                if(!assetTitle.equalsIgnoreCase(AppUtils.getValidAPIStringResponse(getSet.getAssetsTitle())))
                {
                    assetTitle = getSet.getAssetsTitle();
                    holder.txtAssestTitle.setVisibility(View.VISIBLE);
                    holder.txtAssestTitle.setText(AppUtils.toDisplayCase(getSet.getAssetsTitle()));
                }
                else
                {
                    holder.txtAssestTitle.setVisibility(View.GONE);
                }

                if(getSet.isTotal())
                {
                    holder.llMain.setVisibility(View.GONE);
                    holder.llTotal.setVisibility(View.VISIBLE);
                    holder.txtFinalTotalAmount.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToTwoDecimalValue(getSet.getTotalAmount()));
                    holder.txtFinalTotalAllocation.setText(getSet.getTotalAllocation() + "%");
                }
                else
                {
                    holder.llMain.setVisibility(View.VISIBLE);
                    holder.llTotal.setVisibility(View.GONE);
                    holder.txtAssestType.setText(getSet.getObjective());
                    holder.txtApplicantOne.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToTwoDecimalValue(AppUtils.getValidAPIStringResponse(getSet.getAmount())));
                    holder.txtTotalAmount.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToTwoDecimalValue(AppUtils.getValidAPIStringResponse(getSet.getAmount())));
                    holder.txtAllocation.setText(getSet.getHoldingPercentage() + "%");
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
            private TextView txtAssestTitle, txtAssestType, txtApplicantOne,txtTotalAmount,txtAllocation,txtFinalTotalAmount,txtFinalTotalAllocation;
            private LinearLayout llMain,llTotal;

            ViewHolder(View convertView)
            {
                super(convertView);
                txtAssestTitle = convertView.findViewById(R.id.txtAssestTitle);
                txtAssestType = convertView.findViewById(R.id.txtAssestType);
                txtApplicantOne = convertView.findViewById(R.id.txtApplicantOne);
                txtTotalAmount = convertView.findViewById(R.id.txtTotalAmount);
                txtAllocation = convertView.findViewById(R.id.txtAllocation);
                txtFinalTotalAmount = convertView.findViewById(R.id.txtFinalTotalAmount);
                txtFinalTotalAllocation = convertView.findViewById(R.id.txtFinalTotalAllocation);
                llMain = convertView.findViewById(R.id.llMain);
                llTotal = convertView.findViewById(R.id.llTotal);
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
