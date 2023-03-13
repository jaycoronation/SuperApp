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
import com.application.alphacapital.superapp.pms.beans.NetworthResponseModel;
import com.application.alphacapital.superapp.pms.utils.AppUtils;
import com.application.alphacapital.superapp.pms.utils.SessionManager;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimationType;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimator;

import java.util.ArrayList;

public class MicroAssetFragment extends Fragment
{
    private Activity activity ;

    private SessionManager sessionManager ;

    private View llLoading,llNoInternet,llNoData,llMicroStrategic,llMicroStrategicSection,llMicroTactical,llMicroTacticalSection ;

    private ImageView imgMicroStrategic,imgMicroTactical ;

    private RecyclerView rvMicroStrategic,rvMicroTactical;

    private ArrayList<NetworthResponseModel.MicroAssetsStrategicItem> listMicroStrategic;
    private ArrayList<NetworthResponseModel.MicroTactical.MicroAssetsTacticalItem> listMicroTactical;

    public MicroAssetFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.portfolio_fragment_micro_asset, container, false);

        activity = getActivity() ;
        sessionManager = new SessionManager(activity);

        if(getArguments() != null)
        {
            listMicroStrategic = getArguments().getParcelableArrayList("data1");
            listMicroTactical = getArguments().getParcelableArrayList("data2");
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

        llMicroStrategic = rootView.findViewById(R.id.llMicroStrategic);
        llMicroStrategicSection = rootView.findViewById(R.id.llMicroStrategicSection);
        llMicroTactical = rootView.findViewById(R.id.llMicroTactical);
        llMicroTacticalSection = rootView.findViewById(R.id.llMicroTacticalSection);

        imgMicroStrategic = rootView.findViewById(R.id.imgMicroStrategic);
        imgMicroTactical = rootView.findViewById(R.id.imgMicroTactical);

        rvMicroStrategic = rootView.findViewById(R.id.rvMicroStrategic);
        rvMicroStrategic.setLayoutManager(new LinearLayoutManager(activity));

        rvMicroTactical = rootView.findViewById(R.id.rvMicroTactical);
        rvMicroTactical.setLayoutManager(new LinearLayoutManager(activity));
    }

    private void setData()
    {
        if(listMicroStrategic != null && listMicroStrategic.size() > 0)
        {
            llNoData.setVisibility(View.GONE);

            MicroStrategicAdapter microStrategicAdapter = new MicroStrategicAdapter(listMicroStrategic);
            rvMicroStrategic.setAdapter(microStrategicAdapter);

            MicroTacticalAdapter microTacticalAdapter = new MicroTacticalAdapter(listMicroTactical);
            rvMicroTactical.setAdapter(microTacticalAdapter);
        }
        else
        {
            llNoData.setVisibility(View.VISIBLE);
        }
    }

    private void onClicks()
    {
        llMicroStrategic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(llMicroTacticalSection != null && llMicroTacticalSection.getVisibility() == View.VISIBLE)
                {
                    AppUtils.collapse(llMicroTacticalSection,imgMicroTactical);
                }

                if(llMicroStrategicSection.getVisibility() == View.VISIBLE)
                {
                    AppUtils.collapse(llMicroStrategicSection,imgMicroStrategic);
                }
                else
                {
                    AppUtils.expand(llMicroStrategicSection,imgMicroStrategic);
                }
            }
        });

        llMicroTactical.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(llMicroStrategicSection != null && llMicroStrategicSection.getVisibility() == View.VISIBLE)
                {
                    AppUtils.collapse(llMicroStrategicSection,imgMicroStrategic);
                }

                if(llMicroTacticalSection.getVisibility() == View.VISIBLE)
                {
                    AppUtils.collapse(llMicroTacticalSection,imgMicroTactical);
                }
                else
                {
                    AppUtils.expand(llMicroTacticalSection,imgMicroTactical);
                }
            }
        });
    }

    private class MicroStrategicAdapter extends RecyclerView.Adapter<MicroStrategicAdapter.ViewHolder>
    {
        ArrayList<NetworthResponseModel.MicroAssetsStrategicItem> listItems;
        private SpringyAdapterAnimator mAnimator;

        MicroStrategicAdapter(ArrayList<NetworthResponseModel.MicroAssetsStrategicItem> list)
        {
            this.listItems = list;
            mAnimator = new SpringyAdapterAnimator(rvMicroStrategic);
            mAnimator.setSpringAnimationType(SpringyAdapterAnimationType.SLIDE_FROM_BOTTOM);
            mAnimator.addConfig(85, 15);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.portfolio_row_view_micro_strategic, viewGroup, false);
            mAnimator.onSpringItemCreate(v);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            try
            {
                final NetworthResponseModel.MicroAssetsStrategicItem getSet = listItems.get(position);

                if (position % 2 == 0)
                {
                    holder.llMainMicroStrategic.setBackgroundResource(R.color.white);
                }
                else
                {
                    holder.llMainMicroStrategic.setBackgroundResource(R.color.light_gray_new);
                }

                if(getSet.isTotal())
                {
                    holder.llMainMicroStrategic.setVisibility(View.GONE);
                    holder.llTotalMicroStrategic.setVisibility(View.VISIBLE);

                    holder.txtTotalAmount.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToTwoDecimalValue(getSet.getTotalAmount()));
                    holder.txtTotalPercentage.setText(getSet.getTotalAmountPercentage() + "%");
                    holder.txtTotalPolicyPercentage.setText(getSet.getTotalPolicyPercentage());
                    holder.txtTotalVariance.setText(getSet.getTotalVariance());
                }
                else
                {
                    holder.llMainMicroStrategic.setVisibility(View.VISIBLE);
                    holder.llTotalMicroStrategic.setVisibility(View.GONE);

                    holder.txtAssetClass.setText(AppUtils.toDisplayCase(getSet.getAsset_class()));
                    holder.txtActualAmount.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToTwoDecimalValue(String.valueOf(getSet.getActual_amount())));
                    holder.txtActualPercentage.setText(AppUtils.convertToTwoDecimalValue(String.valueOf(getSet.getActual_percentage())));
                    holder.txtPolicyPercentage.setText(AppUtils.convertToTwoDecimalValue(String.valueOf(AppUtils.getValidAPIStringResponse(getSet.getPolicy()))));
                    holder.txtVariance.setText(getSet.getVariance() + "%");
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
            private TextView txtAssetClass, txtActualAmount, txtActualPercentage, txtPolicyPercentage, txtVariance,
                    txtTotalAmount, txtTotalPercentage,txtTotalPolicyPercentage,txtTotalVariance;
            private LinearLayout llMainMicroStrategic, llTotalMicroStrategic;

            ViewHolder(View convertView)
            {
                super(convertView);
                txtAssetClass = convertView.findViewById(R.id.txtAssetClass);
                txtActualAmount = convertView.findViewById(R.id.txtActualAmount);
                txtActualPercentage = convertView.findViewById(R.id.txtActualPercentage);
                txtPolicyPercentage = convertView.findViewById(R.id.txtPolicyPercentage);
                txtVariance = convertView.findViewById(R.id.txtVariance);
                txtTotalAmount = convertView.findViewById(R.id.txtTotalAmount);
                txtTotalPercentage = convertView.findViewById(R.id.txtTotalPercentage);
                txtTotalPolicyPercentage = convertView.findViewById(R.id.txtTotalPolicyPercentage);
                txtTotalVariance = convertView.findViewById(R.id.txtTotalVariance);
                llMainMicroStrategic = convertView.findViewById(R.id.llMainMicroStrategic);
                llTotalMicroStrategic = convertView.findViewById(R.id.llTotalMicroStrategic);
            }
        }
    }

    private class MicroTacticalAdapter extends RecyclerView.Adapter<MicroTacticalAdapter.ViewHolder>
    {
        ArrayList<NetworthResponseModel.MicroTactical.MicroAssetsTacticalItem> listItems;
        private SpringyAdapterAnimator mAnimator;

        MicroTacticalAdapter(ArrayList<NetworthResponseModel.MicroTactical.MicroAssetsTacticalItem> list)
        {
            this.listItems = list;
            mAnimator = new SpringyAdapterAnimator(rvMicroTactical);
            mAnimator.setSpringAnimationType(SpringyAdapterAnimationType.SLIDE_FROM_BOTTOM);
            mAnimator.addConfig(85, 15);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.portfolio_row_view_micro_tactical, viewGroup, false);
            mAnimator.onSpringItemCreate(v);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            try
            {
                final NetworthResponseModel.MicroTactical.MicroAssetsTacticalItem getSet = listItems.get(position);

                if (position % 2 == 0)
                {
                    holder.llMainMicroTactical.setBackgroundResource(R.color.white);
                }
                else
                {
                    holder.llMainMicroTactical.setBackgroundResource(R.color.light_gray_new);
                }

                if(getSet.isTotal())
                {
                    holder.llMainMicroTactical.setVisibility(View.GONE);
                    holder.llTotalMicroTactical.setVisibility(View.VISIBLE);

                    holder.txtTacticalTotalPercentage.setText(AppUtils.convertToTwoDecimalValue(getSet.getTotalPercentage()));
                    holder.txtTacticalTotalPolicyPercentage.setText(getSet.getTotalPolicyPercentage() + "%");
                    holder.txtTacticalTotalVariance.setText(getSet.getToalVariance());
                }
                else
                {
                    holder.llMainMicroTactical.setVisibility(View.VISIBLE);
                    holder.llTotalMicroTactical.setVisibility(View.GONE);

                    holder.txtTacticalAssetClass.setText(AppUtils.toDisplayCase(getSet.getAsset_class()));
                    holder.txtTacticalActualPercentage.setText(AppUtils.convertToTwoDecimalValue(String.valueOf(getSet.getActual_percentage())));
                    holder.txtTacticalPolicyPercentage.setText(AppUtils.convertToTwoDecimalValue(String.valueOf(getSet.getPolicy())));
                    holder.txtTacticalVariance.setText(getSet.getVariance() + "%");
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
            private TextView txtTacticalAssetClass, txtTacticalActualPercentage, txtTacticalPolicyPercentage, txtTacticalVariance,
                    txtTacticalTotalPercentage, txtTacticalTotalPolicyPercentage, txtTacticalTotalVariance;
            private LinearLayout llMainMicroTactical, llTotalMicroTactical;

            ViewHolder(View convertView)
            {
                super(convertView);
                txtTacticalAssetClass = convertView.findViewById(R.id.txtTacticalAssetClass);
                txtTacticalActualPercentage = convertView.findViewById(R.id.txtTacticalActualPercentage);
                txtTacticalPolicyPercentage = convertView.findViewById(R.id.txtTacticalPolicyPercentage);
                txtTacticalVariance = convertView.findViewById(R.id.txtTacticalVariance);
                txtTacticalTotalPercentage = convertView.findViewById(R.id.txtTacticalTotalPercentage);
                txtTacticalTotalPolicyPercentage = convertView.findViewById(R.id.txtTacticalTotalPolicyPercentage);
                txtTacticalTotalVariance = convertView.findViewById(R.id.txtTacticalTotalVariance);
                llMainMicroTactical = convertView.findViewById(R.id.llMainMicroTactical);
                llTotalMicroTactical = convertView.findViewById(R.id.llTotalMicroTactical);
            }
        }
    }
}
