package com.application.alphacapital.superapp.pms.fragment;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.application.alphacapital.superapp.R;
import com.application.alphacapital.superapp.databinding.NetworthFragmentBinding;
import com.application.alphacapital.superapp.pms.activity.DashboardPortfolioActivity;
import com.application.alphacapital.superapp.pms.beans.NetworthResponseModel;
import com.application.alphacapital.superapp.pms.beans.NetworthTempData;
import com.application.alphacapital.superapp.pms.network.PortfolioApiClient;
import com.application.alphacapital.superapp.pms.network.PortfolioApiInterface;
import com.application.alphacapital.superapp.pms.utils.ApiUtils;
import com.application.alphacapital.superapp.pms.utils.AppUtils;
import com.application.alphacapital.superapp.pms.utils.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetWorthFragment extends Fragment
{
    private Activity activity ;
    private NetworthFragmentBinding binding;
    private SessionManager sessionManager ;
    private PortfolioApiInterface apiService;
    private ArrayList<NetworthResponseModel.AssetsData.AssetsDetails.DebtItem> listNetworth;

    public NetWorthFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activity = getActivity() ;
        sessionManager = new SessionManager(activity);
        apiService = PortfolioApiClient.getClient().create(PortfolioApiInterface.class);
        binding = DataBindingUtil.inflate(inflater,R.layout.networth_fragment, container, false);
        initViews();
        return binding.getRoot();
    }

    private void initViews()
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
    }

    private void getNetworthData()
    {
        try
        {
            binding.loading.llLoading.setVisibility(View.VISIBLE);
            listNetworth = new ArrayList<>();
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
                                if (response.body().getAssetsData().getAssetsDetails().getDebt() != null)
                                {
                                    for(int i=0;i<response.body().getAssetsData().getAssetsDetails().getDebt().size();i++)
                                    {
                                        NetworthResponseModel.AssetsData.AssetsDetails.DebtItem item =  new NetworthResponseModel.AssetsData.AssetsDetails.DebtItem();
                                        item.setAssetsTitle("Debt");
                                        item.setAmount(response.body().getAssetsData().getAssetsDetails().getDebt().get(i).getAmount());
                                        item.setHoldingPercentage(response.body().getAssetsData().getAssetsDetails().getDebt().get(i).getHoldingPercentage());
                                        item.setObjective(response.body().getAssetsData().getAssetsDetails().getDebt().get(i).getObjective());
                                        item.setTotal(false);
                                        listNetworth.add(item);
                                    }

                                    NetworthResponseModel.AssetsData.AssetsDetails.DebtItem itemDebt =  new NetworthResponseModel.AssetsData.AssetsDetails.DebtItem();
                                    itemDebt.setAssetsTitle("Debt");
                                    itemDebt.setAmount("");
                                    itemDebt.setHoldingPercentage("");
                                    itemDebt.setObjective("");
                                    itemDebt.setTotal(true);
                                    itemDebt.setTotalAmount(String.valueOf(response.body().getAssetsData().getAssetsDetailsTotal().getDebt().getTotalAmount()));
                                    itemDebt.setTotalAllocation(String.valueOf(response.body().getAssetsData().getAssetsDetailsTotal().getDebt().getPercentageAmount()));
                                    listNetworth.add(itemDebt);
                                }


                                if (response.body().getAssetsData().getAssetsDetails().getEquity() != null)
                                {
                                    for(int i=0;i<response.body().getAssetsData().getAssetsDetails().getEquity().size();i++)
                                    {
                                        NetworthResponseModel.AssetsData.AssetsDetails.DebtItem item =  new NetworthResponseModel.AssetsData.AssetsDetails.DebtItem();
                                        item.setAssetsTitle("Equity");
                                        item.setAmount(response.body().getAssetsData().getAssetsDetails().getEquity().get(i).getAmount());
                                        item.setHoldingPercentage(response.body().getAssetsData().getAssetsDetails().getEquity().get(i).getHoldingPercentage());
                                        item.setObjective(response.body().getAssetsData().getAssetsDetails().getEquity().get(i).getObjective());
                                        item.setTotal(false);
                                        listNetworth.add(item);
                                    }

                                    NetworthResponseModel.AssetsData.AssetsDetails.DebtItem itemEquity =  new NetworthResponseModel.AssetsData.AssetsDetails.DebtItem();
                                    itemEquity.setAssetsTitle("Equity");
                                    itemEquity.setAmount("");
                                    itemEquity.setHoldingPercentage("");
                                    itemEquity.setObjective("");
                                    itemEquity.setTotal(true);
                                    itemEquity.setTotalAmount(String.valueOf(response.body().getAssetsData().getAssetsDetailsTotal().getEquity().getTotalAmount()));
                                    itemEquity.setTotalAllocation(String.valueOf(response.body().getAssetsData().getAssetsDetailsTotal().getEquity().getPercentageAmount()));
                                    listNetworth.add(itemEquity);
                                }

                                if (response.body().getAssetsData().getAssetsDetails().getHybrid() != null)
                                {
                                    for(int i=0;i<response.body().getAssetsData().getAssetsDetails().getHybrid().size();i++)
                                    {
                                        NetworthResponseModel.AssetsData.AssetsDetails.DebtItem item =  new NetworthResponseModel.AssetsData.AssetsDetails.DebtItem();
                                        item.setAssetsTitle("Hybrid");
                                        item.setAmount(response.body().getAssetsData().getAssetsDetails().getHybrid().get(i).getAmount());
                                        item.setHoldingPercentage(response.body().getAssetsData().getAssetsDetails().getHybrid().get(i).getHoldingPercentage());
                                        item.setObjective(response.body().getAssetsData().getAssetsDetails().getHybrid().get(i).getObjective());
                                        item.setTotal(false);
                                        listNetworth.add(item);
                                    }

                                    NetworthResponseModel.AssetsData.AssetsDetails.DebtItem itemHybrid =  new NetworthResponseModel.AssetsData.AssetsDetails.DebtItem();
                                    itemHybrid.setAssetsTitle("Hybrid");
                                    itemHybrid.setAmount("");
                                    itemHybrid.setHoldingPercentage("");
                                    itemHybrid.setObjective("");
                                    itemHybrid.setTotal(true);
                                    itemHybrid.setTotalAmount(String.valueOf(response.body().getAssetsData().getAssetsDetailsTotal().getHybrid().getTotalAmount()));
                                    itemHybrid.setTotalAllocation(String.valueOf(response.body().getAssetsData().getAssetsDetailsTotal().getHybrid().getPercentageAmount()));
                                    listNetworth.add(itemHybrid);
                                }


                                if(listNetworth != null && listNetworth.size() > 0)
                                {
                                    binding.noData.llNoData.setVisibility(View.GONE);
                                    NetworthAssestAdapter networthAssestAdapter = new NetworthAssestAdapter(listNetworth);
                                    binding.rvNetworth.setAdapter(networthAssestAdapter);
                                }
                                else
                                {
                                    binding.noData.llNoData.setVisibility(View.VISIBLE);
                                }

                                binding.txtTotalAmount.setText("₹ "+AppUtils.convertToTwoDecimalValue(String.valueOf(response.body().getAssetsData().getAssetsDetailsGrandtotal().getGrandTotalAmount())));
                                binding.txtTotalAllocation.setText(response.body().getAssetsData().getAssetsDetailsGrandtotal().getGrandPercentageAmount() + "%");
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                            Log.e("Networth List Size === ",String.valueOf(listNetworth.size()));

                            binding.loading.llLoading.setVisibility(View.GONE);
                        }
                        else
                        {
                            binding.noData.llNoData.setVisibility(View.VISIBLE);
                            binding.loading.llLoading.setVisibility(View.GONE);
                        }
                    }
                    else
                    {
                        binding.noData.llNoData.setVisibility(View.VISIBLE);
                        binding.loading.llLoading.setVisibility(View.GONE);
                    }
                }

                @Override public void onFailure(Call<NetworthTempData> call, Throwable t)
                {
                    binding.noData.llNoData.setVisibility(View.VISIBLE);
                    binding.loading.llLoading.setVisibility(View.GONE);
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
}
