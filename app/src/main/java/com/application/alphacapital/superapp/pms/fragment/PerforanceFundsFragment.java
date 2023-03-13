package com.application.alphacapital.superapp.pms.fragment;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.alphacapital.superapp.R;
import com.application.alphacapital.superapp.pms.beans.PerfomanceResponseModel;
import com.application.alphacapital.superapp.pms.utils.AppUtils;
import com.application.alphacapital.superapp.pms.utils.SessionManager;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimationType;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimator;

import java.util.ArrayList;


public class PerforanceFundsFragment extends Fragment
{
    private Activity activity ;

    private SessionManager sessionManager ;

    private View llLoading,llNoInternet,llNoData;

    private RecyclerView rvXirrFunds;

    private ArrayList<PerfomanceResponseModel.PerformanceDetailsCateItem> listXIRR;

    public PerforanceFundsFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.portfolio_fragment_xirr_funds, container, false);

        activity = getActivity();
        sessionManager = new SessionManager(activity);

        if(getArguments() != null)
        {
            listXIRR = getArguments().getParcelableArrayList("data");
        }

        initViews(rootView);

        setData();

        return rootView ;
    }

    private void initViews(View rootView)
    {
        llLoading = rootView.findViewById(R.id.llLoading);
        llNoInternet = rootView.findViewById(R.id.llNoInternet);
        llNoData = rootView.findViewById(R.id.llNoData);

        rvXirrFunds = rootView.findViewById(R.id.rvXirrFunds);
        rvXirrFunds.setLayoutManager(new LinearLayoutManager(activity));
    }

    private void setData()
    {
        if(listXIRR != null && listXIRR.size() > 0)
        {
            llNoData.setVisibility(View.GONE);

            XIRRFundsAdapter XIRRFundsAdapter = new XIRRFundsAdapter(listXIRR);
            rvXirrFunds.setAdapter(XIRRFundsAdapter);
        }
        else
        {
            llNoData.setVisibility(View.VISIBLE);
        }
    }

    private class XIRRFundsAdapter extends RecyclerView.Adapter<XIRRFundsAdapter.ViewHolder>
    {
        ArrayList<PerfomanceResponseModel.PerformanceDetailsCateItem> listItems;

        XIRRFundsAdapter(ArrayList<PerfomanceResponseModel.PerformanceDetailsCateItem> list)
        {
            this.listItems = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.portfolio_row_view_performance_funds, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            try
            {
                final PerfomanceResponseModel.PerformanceDetailsCateItem getSet = listItems.get(position);

                holder.txtXirrKey.setText(AppUtils.toDisplayCase(getSet.getPortfolio_key()));

                if(getSet.getPortfolio_value() != null && getSet.getPortfolio_value().size() > 0)
                {
                   XIRRFundsValueAdapter xirrFundsValueAdapter = new XIRRFundsValueAdapter(getSet.getPortfolio_value());
                   holder.rvXirrFundsValue.setAdapter(xirrFundsValueAdapter);
                }

                holder.txtXirrTotalAmount.setText(getResources().getString(R.string.rupees) + " "  + AppUtils.convertToTwoDecimalValue(String.valueOf(getSet.getPortfolio_total().getAmountInvested())));
                holder.txtXirrTotalCurrentValue.setText(getResources().getString(R.string.rupees) + " "  + AppUtils.convertToTwoDecimalValue(String.valueOf(getSet.getPortfolio_total().getCurrentValue())));
                holder.txtXirrTotalDays.setText(AppUtils.convertToTwoDecimalValue(String.valueOf(getSet.getPortfolio_total().getTotal_days())));
                holder.txtXirrTotalGain.setText(getResources().getString(R.string.rupees) + " "  + AppUtils.convertToTwoDecimalValue(String.valueOf(getSet.getPortfolio_total().getGain())));
                holder.txtXirrTotalReturn.setText(AppUtils.convertToTwoDecimalValue(String.valueOf(getSet.getPortfolio_total().getAbreturn())) + "%");
                holder.txtXirrTotalXirr.setText(AppUtils.convertToTwoDecimalValue(String.valueOf(getSet.getPortfolio_total().getXirr())) + "%");
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
            private TextView txtXirrKey;
            private RecyclerView rvXirrFundsValue;
            private TextView txtXirrTotalAmount;
            private TextView txtXirrTotalReturn;
            private TextView txtXirrTotalCurrentValue;
            private TextView txtXirrTotalDays;
            private TextView txtXirrTotalGain;
            private TextView txtXirrTotalXirr;

            ViewHolder(View convertView)
            {
                super(convertView);
                try
                {
                    txtXirrKey = convertView.findViewById(R.id.txtXirrKey);
                    rvXirrFundsValue = convertView.findViewById(R.id.rvXirrFundsValue);
                    rvXirrFundsValue.setLayoutManager(new LinearLayoutManager(activity));
                    txtXirrTotalAmount = convertView.findViewById(R.id.txtXirrTotalAmount);
                    txtXirrTotalReturn = convertView.findViewById(R.id.txtXirrTotalReturn);
                    txtXirrTotalCurrentValue = convertView.findViewById(R.id.txtXirrTotalCurrentValue);
                    txtXirrTotalDays = convertView.findViewById(R.id.txtXirrTotalDays);
                    txtXirrTotalGain = convertView.findViewById(R.id.txtXirrTotalGain);
                    txtXirrTotalXirr = convertView.findViewById(R.id.txtXirrTotalXirr);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private class XIRRFundsValueAdapter extends RecyclerView.Adapter<XIRRFundsValueAdapter.ViewHolder>
    {
        ArrayList<PerfomanceResponseModel.PerformanceDetailsCateItem.PortfolioValueItem> listItems;
        private SpringyAdapterAnimator mAnimator;

        XIRRFundsValueAdapter(ArrayList<PerfomanceResponseModel.PerformanceDetailsCateItem.PortfolioValueItem> list)
        {
            this.listItems = list;
            mAnimator = new SpringyAdapterAnimator(rvXirrFunds);
            mAnimator.setSpringAnimationType(SpringyAdapterAnimationType.SLIDE_FROM_BOTTOM);
            mAnimator.addConfig(85, 15);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.portfolio_row_view_performance_fund_value, viewGroup, false);
            mAnimator.onSpringItemCreate(v);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            try
            {
                final PerfomanceResponseModel.PerformanceDetailsCateItem.PortfolioValueItem getSet = listItems.get(position);

                holder.txtXirrFundName.setText(AppUtils.toDisplayCase(getSet.getSchemeName()));
                holder.txtXirrFolioNo.setText(getSet.getFolioNo());
                holder.txtXirrHolder.setText(getSet.getHolder());
                holder.txtXirrDate.setText(getSet.getTranDate());

                if(getSet.getStatus().equalsIgnoreCase("Purchase"))
                {
                    holder.txtXirrStatus.setTextColor(ContextCompat.getColor(activity,R.color.portfolio_down_red));
                }
                else if(getSet.getStatus().equalsIgnoreCase("Sell"))
                {
                    holder.txtXirrStatus.setTextColor(ContextCompat.getColor(activity,R.color.portfolio_up_green));
                }
                else
                {
                    holder.txtXirrStatus.setTextColor(ContextCompat.getColor(activity,R.color.portfolio_other_status));
                }

                holder.txtXirrStatus.setText(getSet.getStatus());
                holder.txtXirrAmount.setText(getResources().getString(R.string.rupees) + " "  + AppUtils.convertToTwoDecimalValue(getSet.getAmount()));

                mAnimator.onSpringItemBind(holder.itemView, position);
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
            private TextView txtXirrFundName;
            private LinearLayout llSinceInception;
            private TextView txtXirrFolioNo;
            private TextView txtXirrHolder;
            private TextView txtXirrDate;
            private TextView txtXirrStatus;
            private TextView txtXirrAmount;


            ViewHolder(View convertView)
            {
                super(convertView);
                try
                {
                    txtXirrFundName = convertView.findViewById(R.id.txtXirrFundName);
                    llSinceInception = convertView.findViewById(R.id.llSinceInception);
                    txtXirrFolioNo = convertView.findViewById(R.id.txtXirrFolioNo);
                    txtXirrHolder = convertView.findViewById(R.id.txtXirrHolder);
                    txtXirrDate = convertView.findViewById(R.id.txtXirrDate);
                    txtXirrStatus = convertView.findViewById(R.id.txtXirrStatus);
                    txtXirrAmount = convertView.findViewById(R.id.txtXirrAmount);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
