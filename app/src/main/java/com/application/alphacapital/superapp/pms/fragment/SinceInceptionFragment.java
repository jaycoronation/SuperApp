package com.application.alphacapital.superapp.pms.fragment;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.alphacapital.superapp.R;
import com.application.alphacapital.superapp.pms.activity.PerformanceActivity;
import com.application.alphacapital.superapp.pms.beans.PerfomanceResponseModel;
import com.application.alphacapital.superapp.pms.utils.AppUtils;
import com.application.alphacapital.superapp.pms.utils.SessionManager;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimationType;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimator;

import java.util.ArrayList;


public class SinceInceptionFragment extends Fragment
{
    private Activity activity ;

    private SessionManager sessionManager ;

    private View llLoading,llNoInternet,llNoData;

    private RecyclerView rvSinceInception ;
    private TextView txtSITotalAmount;
    private TextView txtSITotalReturn;
    private TextView txtSITotalCurrentValue;
    private TextView txtSITotalDays;
    private TextView txtSITotalGain;
    private TextView txtSITotalXirr;

    private ArrayList<PerfomanceResponseModel.SinceInceptionDetailsItem> listSinceInception ;

    public SinceInceptionFragment()
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
        View rootView = inflater.inflate(R.layout.portfolio_fragment_since_inception, container, false);

        activity = getActivity();
        sessionManager = new SessionManager(activity);

        if(getArguments() != null)
        {
            listSinceInception = getArguments().getParcelableArrayList("data");
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

        rvSinceInception = rootView.findViewById(R.id.rvSinceInception);
        rvSinceInception.setLayoutManager(new LinearLayoutManager(activity));

        txtSITotalAmount = rootView.findViewById(R.id.txtSITotalAmount);
        txtSITotalReturn = rootView.findViewById(R.id.txtSITotalReturn);
        txtSITotalCurrentValue = rootView.findViewById(R.id.txtSITotalCurrentValue);
        txtSITotalDays = rootView.findViewById(R.id.txtSITotalDays);
        txtSITotalGain = rootView.findViewById(R.id.txtSITotalGain);
        txtSITotalXirr = rootView.findViewById(R.id.txtSITotalXirr);
    }

    private void setData()
    {
        if(listSinceInception != null && listSinceInception.size() > 0)
        {
            llNoData.setVisibility(View.GONE);

            SinceInceptionAdapter sinceInceptionAdapter = new SinceInceptionAdapter(listSinceInception);
            rvSinceInception.setAdapter(sinceInceptionAdapter);
        }
        else
        {
            llNoData.setVisibility(View.VISIBLE);
        }

        txtSITotalAmount.setText(getResources().getString(R.string.rupees) + " "  + AppUtils.convertToTwoDecimalValue(PerformanceActivity.AmountInvested));
        txtSITotalCurrentValue.setText(getResources().getString(R.string.rupees) + " "  + AppUtils.convertToTwoDecimalValue(PerformanceActivity.CurrentValue));
        txtSITotalDays.setText(AppUtils.convertToTwoDecimalValue(PerformanceActivity.TotalDays));
        txtSITotalGain.setText(getResources().getString(R.string.rupees) + " "  + AppUtils.convertToTwoDecimalValue(PerformanceActivity.Gain));
        txtSITotalReturn.setText(AppUtils.convertToTwoDecimalValue(PerformanceActivity.Abreturn) + "%");
        txtSITotalXirr.setText(AppUtils.convertToTwoDecimalValue(PerformanceActivity.Xirr) + "%");
    }

    private class SinceInceptionAdapter extends RecyclerView.Adapter<SinceInceptionAdapter.ViewHolder>
    {
        ArrayList<PerfomanceResponseModel.SinceInceptionDetailsItem> listItems;
        private SpringyAdapterAnimator mAnimator;

        SinceInceptionAdapter(ArrayList<PerfomanceResponseModel.SinceInceptionDetailsItem> list)
        {
            this.listItems = list;
            mAnimator = new SpringyAdapterAnimator(rvSinceInception);
            mAnimator.setSpringAnimationType(SpringyAdapterAnimationType.SLIDE_FROM_BOTTOM);
            mAnimator.addConfig(85, 15);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.portfolio_row_view_since_inception, viewGroup, false);
            mAnimator.onSpringItemCreate(v);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            try
            {
                final PerfomanceResponseModel.SinceInceptionDetailsItem getSet = listItems.get(position);

                holder.txtSinceInceptionAsset.setText(AppUtils.toDisplayCase(getSet.getAsset_type()));
                holder.txtSIAmount.setText(getResources().getString(R.string.rupees) + " "  + AppUtils.convertToTwoDecimalValue(String.valueOf(getSet.getAmountInvested())));
                holder.txtSICurrentValue.setText(getResources().getString(R.string.rupees) + " "  + AppUtils.convertToTwoDecimalValue(String.valueOf(getSet.getCurrentValue())));
                holder.txtSIDays.setText(AppUtils.convertToTwoDecimalValue(String.valueOf(getSet.getTotal_days())));
                holder.txtSIGain.setText(getResources().getString(R.string.rupees) + " "  + AppUtils.convertToTwoDecimalValue(String.valueOf(getSet.getGain())));
                holder.txtSIReturn.setText(AppUtils.convertToTwoDecimalValue(String.valueOf(getSet.getAbreturn())) + "%");
                holder.txtSIXirr.setText(AppUtils.convertToTwoDecimalValue(String.valueOf(getSet.getXirr())) + "%");

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
            private TextView txtSinceInceptionAsset;
            private LinearLayout llSinceInception;
            private TextView txtSIAmount;
            private TextView txtSIReturn;
            private TextView txtSICurrentValue;
            private TextView txtSIDays;
            private TextView txtSIGain;
            private TextView txtSIXirr;

            ViewHolder(View convertView)
            {
                super(convertView);
                try
                {
                    txtSinceInceptionAsset = convertView.findViewById(R.id.txtSinceInceptionAsset);
                    llSinceInception = convertView.findViewById(R.id.llSinceInception);
                    txtSIAmount = convertView.findViewById(R.id.txtSIAmount);
                    txtSIReturn = convertView.findViewById(R.id.txtSIReturn);
                    txtSICurrentValue = convertView.findViewById(R.id.txtSICurrentValue);
                    txtSIDays = convertView.findViewById(R.id.txtSIDays);
                    txtSIGain = convertView.findViewById(R.id.txtSIGain);
                    txtSIXirr = convertView.findViewById(R.id.txtSIXirr);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

}
