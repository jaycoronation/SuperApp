package com.application.alphacapital.superapp.pms.fragment;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.alphacapital.superapp.R;
import com.application.alphacapital.superapp.pms.beans.ApplicationFundSchemesResponseModel;
import com.application.alphacapital.superapp.pms.utils.AppUtils;
import com.application.alphacapital.superapp.pms.utils.SessionManager;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimationType;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimator;

import java.util.ArrayList;

public class AllocationSchemeFundFragment extends Fragment
{
    private Activity activity ;

    private SessionManager sessionManager ;

    private View llLoading,llNoInternet,llNoData, llScheme, llSchemeSection, llFund, llFundSection;

    private ImageView imgScheme, imgFund;

    private RecyclerView rvAllocationScheme, rvAllocationFund;

    private ArrayList<ApplicationFundSchemesResponseModel.FundSchemeListItem> listScheme;
    private ArrayList<ApplicationFundSchemesResponseModel.FundSchemesItem> listFund;

    public AllocationSchemeFundFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.portfolio_fragment_allocation_scheme_fund, container, false);

        activity = getActivity() ;
        sessionManager = new SessionManager(activity);

        if(getArguments() != null)
        {
            listScheme = getArguments().getParcelableArrayList("data1");
            listFund = getArguments().getParcelableArrayList("data2");
        }

        initViews(rootView);

        setData();

        onClicks();

        return rootView;
    }

    private void initViews(View rootView)
    {
        llLoading = rootView.findViewById(R.id.llLoading);
        llNoInternet = rootView.findViewById(R.id.llNoInternet);
        llNoData = rootView.findViewById(R.id.llNoData);

        llScheme = rootView.findViewById(R.id.llScheme);
        llSchemeSection = rootView.findViewById(R.id.llSchemeSection);
        llFund = rootView.findViewById(R.id.llFund);
        llFundSection = rootView.findViewById(R.id.llFundSection);

        imgScheme = rootView.findViewById(R.id.imgScheme);
        imgFund = rootView.findViewById(R.id.imgFund);

        rvAllocationScheme = rootView.findViewById(R.id.rvAllocationScheme);
        rvAllocationScheme.setLayoutManager(new LinearLayoutManager(activity));

        rvAllocationFund = rootView.findViewById(R.id.rvAllocationFund);
        rvAllocationFund.setLayoutManager(new LinearLayoutManager(activity));
    }

    private void setData()
    {
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

    private void onClicks()
    {
        llScheme.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(llFundSection != null && llFundSection.getVisibility() == View.VISIBLE)
                {
                    AppUtils.collapse(llFundSection, imgFund);
                }

                if(llSchemeSection.getVisibility() == View.VISIBLE)
                {
                    AppUtils.collapse(llSchemeSection, imgScheme);
                }
                else
                {
                    AppUtils.expand(llSchemeSection, imgScheme);
                }
            }
        });

        llFund.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(llSchemeSection != null && llSchemeSection.getVisibility() == View.VISIBLE)
                {
                    AppUtils.collapse(llSchemeSection, imgScheme);
                }

                if(llFundSection.getVisibility() == View.VISIBLE)
                {
                    AppUtils.collapse(llFundSection, imgFund);
                }
                else
                {
                    AppUtils.expand(llFundSection, imgFund);
                }
            }
        });
    }

    private class AllocationSchemeAdapter extends RecyclerView.Adapter<AllocationSchemeAdapter.ViewHolder>
    {
        ArrayList<ApplicationFundSchemesResponseModel.FundSchemeListItem> listItems;
        private SpringyAdapterAnimator mAnimator;

        AllocationSchemeAdapter(ArrayList<ApplicationFundSchemesResponseModel.FundSchemeListItem> list)
        {
            this.listItems = list;
            mAnimator = new SpringyAdapterAnimator(rvAllocationScheme);
            mAnimator.setSpringAnimationType(SpringyAdapterAnimationType.SLIDE_FROM_BOTTOM);
            mAnimator.addConfig(85, 15);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.portfolio_row_view_allocation_scheme, viewGroup, false);
            mAnimator.onSpringItemCreate(v);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
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

                    holder.txtTotalCurrentValue.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToTwoDecimalValue(getSet.getCurrentValue()));
                    holder.txtTotalAllocation.setText(getSet.getAllocationTotal() + "%");
                }
                else
                {
                    holder.llMainAllocation.setVisibility(View.VISIBLE);
                    holder.llTotalAllocation.setVisibility(View.GONE);

                    holder.txtAllocationSchemeName.setText(AppUtils.toDisplayCase(getSet.getSchemeName()));
                    holder.txtAllocationType.setText(getSet.getCategory());
                    holder.txtAllocationValue.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToTwoDecimalValue(String.valueOf(AppUtils.getValidAPIStringResponse(getSet.getCurrentValue()))));
                    holder.txtAllocation.setText(AppUtils.convertToTwoDecimalValue(String.valueOf(getSet.getAllocation())));
                }

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
        private SpringyAdapterAnimator mAnimator;

        AllocationFundAdapter(ArrayList<ApplicationFundSchemesResponseModel.FundSchemesItem> list)
        {
            this.listItems = list;
            mAnimator = new SpringyAdapterAnimator(rvAllocationFund);
            mAnimator.setSpringAnimationType(SpringyAdapterAnimationType.SLIDE_FROM_BOTTOM);
            mAnimator.addConfig(85, 15);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.portfolio_row_view_allocation_fund, viewGroup, false);
            mAnimator.onSpringItemCreate(v);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
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

                    holder.txtTotalAllocationFund.setText(AppUtils.convertToTwoDecimalValue(String.valueOf(getSet.getCurrentValue())));
                }
                else
                {
                    holder.llMainAllocationFund.setVisibility(View.VISIBLE);
                    holder.llTotalAllocationFund.setVisibility(View.GONE);

                    holder.txtAllocationFundName.setText(AppUtils.toDisplayCase(getSet.getFundName()));
                    holder.txtFundAllocation.setText(AppUtils.convertToTwoDecimalValue(String.valueOf(getSet.getCurrentValue())));
                }

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
            private TextView txtAllocationFundName, txtFundAllocation, txtTotalAllocationFund;
            private LinearLayout llMainAllocationFund, llTotalAllocationFund;

            ViewHolder(View convertView)
            {
                super(convertView);
                txtAllocationFundName = convertView.findViewById(R.id.txtAllocationFundName);
                txtFundAllocation = convertView.findViewById(R.id.txtFundAllocation);
                txtTotalAllocationFund = convertView.findViewById(R.id.txtTotalAllocationFund);
                llMainAllocationFund = convertView.findViewById(R.id.llMainAllocationFund);
                llTotalAllocationFund = convertView.findViewById(R.id.llTotalAllocationFund);
            }
        }
    }
}
