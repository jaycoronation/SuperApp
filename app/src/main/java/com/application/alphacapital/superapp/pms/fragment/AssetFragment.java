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
import com.application.alphacapital.superapp.pms.beans.NetworthResponseModel;
import com.application.alphacapital.superapp.pms.utils.AppUtils;
import com.application.alphacapital.superapp.pms.utils.SessionManager;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimationType;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimator;

import java.util.ArrayList;

public class AssetFragment extends Fragment
{
    private Activity activity ;

    private SessionManager sessionManager ;

    private View llLoading,llNoInternet,llNoData;

    private RecyclerView rvNetworth;

    private ArrayList<NetworthResponseModel.AssetsData.AssetsDetails.DebtItem> listNetworth;

    public AssetFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.portfolio_fragment_asset, container, false);

        activity = getActivity() ;
        sessionManager = new SessionManager(activity);

        if(getArguments() != null)
        {
            listNetworth = getArguments().getParcelableArrayList("data");
        }

        initViews(rootView);

        setData();

        return rootView;
    }

    private void initViews(View rootView)
    {
        llLoading = rootView.findViewById(R.id.llLoading);
        llNoInternet = rootView.findViewById(R.id.llNoInternet);
        llNoData = rootView.findViewById(R.id.llNoData);

        rvNetworth = rootView.findViewById(R.id.rvNetworth);
        rvNetworth.setLayoutManager(new LinearLayoutManager(activity));
    }

    private void setData()
    {
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
    }

    String assetTitle = "" ;
    private class NetworthAssestAdapter extends RecyclerView.Adapter<NetworthAssestAdapter.ViewHolder>
    {
        ArrayList<NetworthResponseModel.AssetsData.AssetsDetails.DebtItem> listItems;
        private SpringyAdapterAnimator mAnimator;

        NetworthAssestAdapter(ArrayList<NetworthResponseModel.AssetsData.AssetsDetails.DebtItem> list)
        {
            this.listItems = list;
            mAnimator = new SpringyAdapterAnimator(rvNetworth);
            mAnimator.setSpringAnimationType(SpringyAdapterAnimationType.SLIDE_FROM_BOTTOM);
            mAnimator.addConfig(85, 15);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.portfolio_row_view_networth_assest, viewGroup, false);
            mAnimator.onSpringItemCreate(v);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
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
