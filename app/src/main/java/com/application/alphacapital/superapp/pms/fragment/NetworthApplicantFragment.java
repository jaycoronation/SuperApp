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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.alphacapital.superapp.R;
import com.application.alphacapital.superapp.pms.beans.ApplicantListResponseModel;
import com.application.alphacapital.superapp.pms.utils.AppUtils;
import com.application.alphacapital.superapp.pms.utils.SessionManager;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimationType;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimator;

import java.util.ArrayList;


public class NetworthApplicantFragment extends Fragment
{
    private Activity activity ;

    private SessionManager sessionManager ;

    private View llLoading,llNoInternet,llNoData;

    private RecyclerView rvApplicant;

    private TextView txtTotalAmountInvested,txtTotalGain,txtTotalWeightsDays,txtTotalCurrentValue,txtTotalDivedent;

    private ArrayList<ApplicantListResponseModel.ApplicantsList> listNetworthApplicants;
    private ApplicantListResponseModel.ApplicantsList objectApplicants;

    public NetworthApplicantFragment()
    {
        //Required empty public constructor-
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
        View rootView = inflater.inflate(R.layout.portfolio_fragment_networth_applicant, container, false);

        activity = getActivity();
        sessionManager = new SessionManager(activity);

        if(getArguments() !=null)
        {
            //listNetworthApplicants = getArguments().getParcelableArrayList("data");
            objectApplicants = getArguments().getParcelable("data1");
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
        txtTotalAmountInvested = rootView.findViewById(R.id.txtTotalAmountInvested);
        txtTotalGain = rootView.findViewById(R.id.txtTotalGain);
        txtTotalWeightsDays = rootView.findViewById(R.id.txtTotalWeightsDays);
        txtTotalCurrentValue = rootView.findViewById(R.id.txtTotalCurrentValue);
        txtTotalDivedent = rootView.findViewById(R.id.txtTotalDivedent);

        rvApplicant = rootView.findViewById(R.id.rvApplicant);
        rvApplicant.setLayoutManager(new LinearLayoutManager(activity));
    }

    private void setData()
    {
        try
        {
            if(objectApplicants.getApplicants() != null && objectApplicants.getApplicants().size() > 0)
            {
                llNoData.setVisibility(View.GONE);
                NetworthApplicantDataAdapter applicantDataAdapter = new NetworthApplicantDataAdapter(objectApplicants.getApplicants());
                rvApplicant.setAdapter(applicantDataAdapter);
            }
            else
            {
                llNoData.setVisibility(View.VISIBLE);
            }
            Log.e("<><>TOTAL INVESTED", String.valueOf(objectApplicants.getTotal().getInitialVal()));
            txtTotalAmountInvested.setText(getResources().getString(R.string.rupees) + " "  + AppUtils.convertToCommaSeperatedValue(String.valueOf(objectApplicants.getTotal().getInitialVal())));
            txtTotalGain.setText(getResources().getString(R.string.rupees) + " "  + AppUtils.convertToCommaSeperatedValue(String.valueOf(objectApplicants.getTotal().getGain())));
            txtTotalWeightsDays.setText(AppUtils.convertToCommaSeperatedValue(String.valueOf(objectApplicants.getTotal().getWeightedDays())));
            txtTotalCurrentValue.setText(getResources().getString(R.string.rupees) + " "  + AppUtils.convertToCommaSeperatedValue(String.valueOf(objectApplicants.getTotal().getCurrentVal())));
            txtTotalDivedent.setText(getResources().getString(R.string.rupees) + " "  + AppUtils.convertToCommaSeperatedValue(String.valueOf(objectApplicants.getTotal().getDividendValue())));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private class NetworthApplicantAdapter extends RecyclerView.Adapter<NetworthApplicantAdapter.ViewHolder>
    {
        ArrayList<ApplicantListResponseModel.ApplicantsList> listItems;
        private SpringyAdapterAnimator mAnimator;

        NetworthApplicantAdapter(ArrayList<ApplicantListResponseModel.ApplicantsList> list)
        {
            this.listItems = list;
            mAnimator = new SpringyAdapterAnimator(rvApplicant);
            mAnimator.setSpringAnimationType(SpringyAdapterAnimationType.SLIDE_FROM_BOTTOM);
            mAnimator.addConfig(85, 15);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.portfolio_row_view_applicant, viewGroup, false);
            mAnimator.onSpringItemCreate(v);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            try
            {
                final ApplicantListResponseModel.ApplicantsList getSet = listItems.get(position);

                //holder.txtApplicantName.setText(AppUtils.toDisplayCase(getSet.getApplicant_name()));

                if(getSet.getApplicants() != null && getSet.getApplicants().size() > 0)
                {
                    NetworthApplicantDataAdapter applicantDataAdapter = new NetworthApplicantDataAdapter(getSet.getApplicants());
                    holder.rvApplicantAssetsData.setAdapter(applicantDataAdapter);
                }

                //holder.txtApplicantTotalAmount.setText(getResources().getString(R.string.rupees) + " "  + AppUtils.convertToTwoDecimalValue(String.valueOf(getSet.getApcnt_assets_total().getTotalAmount())));
                //holder.txtApplicantTotalHoldingPercentage.setText(getSet.getApcnt_assets_total().getTotalHoldingPercentage() + "%");

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
            private TextView txtApplicantName;
            private TextView txtApplicantTotalAmount;
            private TextView txtApplicantTotalHoldingPercentage;
            private RecyclerView rvApplicantAssetsData ;

            ViewHolder(View convertView)
            {
                super(convertView);
                try
                {
                    txtApplicantName = convertView.findViewById(R.id.txtApplicantName);
                    txtApplicantTotalAmount = convertView.findViewById(R.id.txtApplicantTotalAmount);
                    txtApplicantTotalHoldingPercentage = convertView.findViewById(R.id.txtApplicantTotalHoldingPercentage);

                    rvApplicantAssetsData = convertView.findViewById(R.id.rvApplicantAssetsData);
                    rvApplicantAssetsData.setLayoutManager(new LinearLayoutManager(activity));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private class NetworthApplicantDataAdapter extends RecyclerView.Adapter<NetworthApplicantDataAdapter.ViewHolder>
    {
        ArrayList<ApplicantListResponseModel.ApplicantsItem> listItems;
        private SpringyAdapterAnimator mAnimator;

        NetworthApplicantDataAdapter(ArrayList<ApplicantListResponseModel.ApplicantsItem> list)
        {
            this.listItems = list;
            mAnimator = new SpringyAdapterAnimator(rvApplicant);
            mAnimator.setSpringAnimationType(SpringyAdapterAnimationType.SLIDE_FROM_BOTTOM);
            mAnimator.addConfig(85, 15);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.portfolio_row_view_applicant_data, viewGroup, false);
            mAnimator.onSpringItemCreate(v);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            try
            {
                final ApplicantListResponseModel.ApplicantsItem getSet = listItems.get(position);

                /*if (position % 2 == 0)
                {
                    holder.llApplicantData.setBackgroundResource(R.drawable.cool_blue_gradient);
                }
                else
                {
                    holder.llApplicantData.setBackgroundResource(R.drawable.white_gradient);
                }*/


                holder.txtApplicantName.setText(AppUtils.toDisplayCase(getSet.getApplicantName()));
                holder.txtAmountInvested.setText(getResources().getString(R.string.rupees) + " "  + AppUtils.convertToTwoDecimalValue(String.valueOf(AppUtils.getValidAPIIntegerResponse(String.valueOf(getSet.getInitialVal())))));
                holder.txtDividend.setText(getResources().getString(R.string.rupees) + " "  + AppUtils.getValidAPIDoubleResponse(String.valueOf(getSet.getDividendValue())) + "%");
                holder.txtWeightedDays.setText(AppUtils.convertToCommaSeperatedValue(String.valueOf(getSet.getWeightedDays())));
                holder.txtCurrentAmount.setText(getResources().getString(R.string.rupees) + " "  + AppUtils.convertToCommaSeperatedValue(String.valueOf(getSet.getCurrentVal())));
                holder.txtAbsoluteReturn.setText(String.valueOf(getSet.getAbsoluteReturn()) + "%");
                holder.txtGain.setText(getResources().getString(R.string.rupees) + " "  + AppUtils.convertToCommaSeperatedValue(String.valueOf(getSet.getGain())));
                holder.txtCAGR.setText(String.valueOf(getSet.getCAGR()) + "%");

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
            private TextView txtApplicantName;
            private LinearLayout llApplicantData;
            private TextView txtAmountInvested;
            private TextView txtDividend;
            private TextView txtWeightedDays;
            private TextView txtCurrentAmount;
            private TextView txtAbsoluteReturn;
            private TextView txtGain;
            private TextView txtCAGR;

            ViewHolder(View convertView)
            {
                super(convertView);
                try
                {
                    txtApplicantName = convertView.findViewById(R.id.txtApplicantName);
                    llApplicantData = convertView.findViewById(R.id.llApplicantData);
                    txtAmountInvested = convertView.findViewById(R.id.txtAmountInvested);
                    txtDividend = convertView.findViewById(R.id.txtDividend);
                    txtWeightedDays = convertView.findViewById(R.id.txtWeightedDays);
                    txtCurrentAmount = convertView.findViewById(R.id.txtCurrentAmount);
                    txtAbsoluteReturn = convertView.findViewById(R.id.txtAbsoluteReturn);
                    txtGain = convertView.findViewById(R.id.txtGain);
                    txtCAGR = convertView.findViewById(R.id.txtCAGR);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
