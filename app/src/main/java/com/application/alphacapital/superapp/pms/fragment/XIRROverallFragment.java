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
import com.application.alphacapital.superapp.pms.activity.XIRRActivity;
import com.application.alphacapital.superapp.pms.beans.XIRRResponseModel;
import com.application.alphacapital.superapp.pms.utils.AppUtils;
import com.application.alphacapital.superapp.pms.utils.SessionManager;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimationType;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimator;

import java.util.ArrayList;


public class XIRROverallFragment extends Fragment
{
    private Activity activity ;

    private SessionManager sessionManager ;

    private View llLoading,llNoInternet,llNoData;

    private RecyclerView rvOverall;
    private TextView txtOverallTotalAmount;
    private TextView txtOverallTotalReturn;
    private TextView txtOverallTotalCurrentValue;
    private TextView txtOverallTotalDays;
    private TextView txtOverallTotalGain;
    private TextView txtOverallTotalXirr;
    private TextView txtOpeningBalance;

    private ArrayList<XIRRResponseModel.PerformanceDetailsItem> listPerformanceOverall;

    public XIRROverallFragment()
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
        View rootView = inflater.inflate(R.layout.portfolio_fragment_xirr_overall, container, false);

        activity = getActivity();
        sessionManager = new SessionManager(activity);

        if(getArguments() !=null)
        {
            listPerformanceOverall = getArguments().getParcelableArrayList("data");
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

        rvOverall = rootView.findViewById(R.id.rvOverall);
        rvOverall.setLayoutManager(new LinearLayoutManager(activity));

        txtOverallTotalAmount = rootView.findViewById(R.id.txtOverallTotalAmount);
        txtOverallTotalReturn = rootView.findViewById(R.id.txtOverallTotalReturn);
        txtOverallTotalCurrentValue = rootView.findViewById(R.id.txtOverallTotalCurrentValue);
        txtOverallTotalDays = rootView.findViewById(R.id.txtOverallTotalDays);
        txtOverallTotalGain = rootView.findViewById(R.id.txtOverallTotalGain);
        txtOverallTotalXirr = rootView.findViewById(R.id.txtOverallTotalXirr);
        txtOpeningBalance = rootView.findViewById(R.id.txtOpeningBalance);
    }

    private void setData()
    {
        if(listPerformanceOverall != null && listPerformanceOverall.size() > 0)
        {
            llNoData.setVisibility(View.GONE);

            PerformanceOverallAdapter performanceOverallAdapter = new PerformanceOverallAdapter(listPerformanceOverall);
            rvOverall.setAdapter(performanceOverallAdapter);
        }
        else
        {
            llNoData.setVisibility(View.VISIBLE);
        }

        txtOpeningBalance.setText("Opening Balance on " + XIRRActivity.Date + " : " + getResources().getString(R.string.rupees) + " " + AppUtils.convertToTwoDecimalValue(XIRRActivity.OpeningBalance));
        txtOverallTotalAmount.setText(getResources().getString(R.string.rupees) + " "  + AppUtils.convertToTwoDecimalValue(XIRRActivity.XIRRAmountInvested));
        txtOverallTotalCurrentValue.setText(getResources().getString(R.string.rupees) + " "  + AppUtils.convertToTwoDecimalValue(XIRRActivity.XIRRCurrentValue));
        txtOverallTotalDays.setText(AppUtils.convertToTwoDecimalValue(XIRRActivity.XIRRTotalDays));
        txtOverallTotalGain.setText(getResources().getString(R.string.rupees) + " "  + AppUtils.convertToTwoDecimalValue(XIRRActivity.XIRRGain));
        txtOverallTotalReturn.setText(AppUtils.convertToTwoDecimalValue(XIRRActivity.XIRRAbreturn) + "%");
        txtOverallTotalXirr.setText(AppUtils.convertToTwoDecimalValue(XIRRActivity.XIRROverall) + "%");
    }

    private class PerformanceOverallAdapter extends RecyclerView.Adapter<PerformanceOverallAdapter.ViewHolder>
    {
        ArrayList<XIRRResponseModel.PerformanceDetailsItem> listItems;
        private SpringyAdapterAnimator mAnimator;

        PerformanceOverallAdapter(ArrayList<XIRRResponseModel.PerformanceDetailsItem> list)
        {
            this.listItems = list;
            mAnimator = new SpringyAdapterAnimator(rvOverall);
            mAnimator.setSpringAnimationType(SpringyAdapterAnimationType.SLIDE_FROM_BOTTOM);
            mAnimator.addConfig(85, 15);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.portfolio_row_view_performance_overall, viewGroup, false);
            mAnimator.onSpringItemCreate(v);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            try
            {
                final XIRRResponseModel.PerformanceDetailsItem getSet = listItems.get(position);

                holder.txtOverallFundName.setText(AppUtils.toDisplayCase(getSet.getSchemeName()));
                holder.txtOverallFolioNo.setText(getSet.getFolioNo());
                holder.txtOverallHolder.setText(getSet.getHolder());
                holder.txtOverallDate.setText(getSet.getTranDate());
                holder.txtOverallStatus.setText(getSet.getStatus());

                System.out.println("Status Data" + getSet.getStatus() + " abcd ");

                if(getSet.getStatus().equalsIgnoreCase("Purchase"))
                {
                    holder.txtOverallStatus.setTextColor(ContextCompat.getColor(activity,R.color.portfolio_down_red));
                }
                else if(getSet.getStatus().equalsIgnoreCase("Sell"))
                {
                    holder.txtOverallStatus.setTextColor(ContextCompat.getColor(activity,R.color.portfolio_up_green));
                }
                else
                {
                    holder.txtOverallStatus.setTextColor(ContextCompat.getColor(activity,R.color.portfolio_other_status));
                }

                holder.txtOverallAmount.setText(getResources().getString(R.string.rupees) + " "  + AppUtils.convertToTwoDecimalValue(getSet.getAmount()));

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
            private TextView txtOverallFundName;
            private LinearLayout llSinceInception;
            private TextView txtOverallFolioNo;
            private TextView txtOverallHolder;
            private TextView txtOverallDate;
            private TextView txtOverallStatus;
            private TextView txtOverallAmount;

            ViewHolder(View convertView)
            {
                super(convertView);
                try
                {
                    txtOverallFundName = convertView.findViewById(R.id.txtOverallFundName);
                    llSinceInception = convertView.findViewById(R.id.llSinceInception);
                    txtOverallFolioNo = convertView.findViewById(R.id.txtOverallFolioNo);
                    txtOverallHolder = convertView.findViewById(R.id.txtOverallHolder);
                    txtOverallDate = convertView.findViewById(R.id.txtOverallDate);
                    txtOverallStatus = convertView.findViewById(R.id.txtOverallStatus);
                    txtOverallAmount = convertView.findViewById(R.id.txtOverallAmount);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
