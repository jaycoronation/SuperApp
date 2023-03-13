package com.application.alphacapital.superapp.pms.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.application.alphacapital.superapp.R;
import com.application.alphacapital.superapp.databinding.PortfolioFragmentBinding;
import com.application.alphacapital.superapp.pms.activity.DashboardPortfolioActivity;
import com.application.alphacapital.superapp.pms.beans.AppicantiListResponseModel;
import com.application.alphacapital.superapp.pms.beans.PortfoliaResponseModel;
import com.application.alphacapital.superapp.pms.beans.PortfolioDetailsResponseModel;
import com.application.alphacapital.superapp.pms.network.PortfolioApiClient;
import com.application.alphacapital.superapp.pms.network.PortfolioApiInterface;
import com.application.alphacapital.superapp.pms.utils.ApiUtils;
import com.application.alphacapital.superapp.pms.utils.AppUtils;
import com.application.alphacapital.superapp.pms.utils.SessionManager;
import com.application.alphacapital.superapp.pms.utils.TinyDB;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PortfolioFragment extends Fragment
{
    private Activity activity ;
    private PortfolioFragmentBinding binding;
    private SessionManager sessionManager ;
    private PortfolioApiInterface apiService;
    private TinyDB tinyDB;
    private ArrayList<PortfoliaResponseModel.PortfolioDetailsItem> listPortfolioDetails = new ArrayList<>();
    private ArrayList<AppicantiListResponseModel.ApplicantsListItem> listApplicants =    new ArrayList<>();
    private String strCID = "";
    private String grandTotalInvest = "", grandTotalCurrent = "", grandTotalProfit = "", title = "", subCategory = "";
    String SchemeName = "", Joint1Name = "", Joint2Name = "", Nominee = "", Joint1PAN = "", Joint2PAN = "", RemainingUnits = "";

    public static Handler handler;

    public PortfolioFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activity = getActivity() ;
        tinyDB = new TinyDB(activity);
        sessionManager = new SessionManager(activity);
        apiService = PortfolioApiClient.getClient().create(PortfolioApiInterface.class);
        binding = DataBindingUtil.inflate(inflater,R.layout.portfolio_fragment, container, false);
        initViews();
        setUpPortfolioData();

        handler = new Handler(message -> {
            try
            {
                if (message.what == 111)
                {
                    listPortfolioDetails = (ArrayList<PortfoliaResponseModel.PortfolioDetailsItem>) message.obj;
                    setUpPortfolioData();
                    Log.e("ExampleJobServiceFF  : ", listPortfolioDetails.size() + " Performance <><>");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return false;
        });

        return binding.getRoot();
    }

    private void setUpPortfolioData() {
        if (sessionManager.getPortfolioList().size() > 0)
        {
            binding.noData.llNoData.setVisibility(View.GONE);
            listPortfolioDetails = sessionManager.getPortfolioList();
            PortfolioAdapter portfolioAdapter = new PortfolioAdapter(listPortfolioDetails);
            binding.rvPortfolio.setAdapter(portfolioAdapter);

            binding.txtGrandToalInvested.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToCommaSeperatedValue(sessionManager.getInitalValue()));
            binding.txtGrandTotalCurrent.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToCommaSeperatedValue(sessionManager.getCurrentValue()));
            binding.txtGrandTotalProfit.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToCommaSeperatedValue(sessionManager.getGain()));
        }
    }

    private void initViews()
    {

        if (AppUtils.isOnline())
        {
            binding.noInternet.llNoInternet.setVisibility(View.GONE);
            getYears();
        }
        else
        {
            binding.noInternet.llNoInternet.setVisibility(View.VISIBLE);
        }

        onClicks();
    }

    private void onClicks()
    {
        DashboardPortfolioActivity.spnrYears.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                // On selecting a spinner item
                strCID = listApplicants.get(position).getCid();
                getDataForPortfolio();
            }

            @Override public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    private void getYears()
    {
        DashboardPortfolioActivity.pbToolbar.setVisibility(View.VISIBLE);
        //binding.loading.llLoading.setVisibility(View.VISIBLE);
        listApplicants = new ArrayList<>();
        apiService.getAppicantsAPI(ApiUtils.END_USER_ID).enqueue(new Callback<AppicantiListResponseModel>()
        {
            @Override public void onResponse(Call<AppicantiListResponseModel> call, Response<AppicantiListResponseModel> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().getSuccess() == 1)
                    {
                        if(response.body().getApplicants_list() !=null && response.body().getApplicants_list().size() >0)
                        {
                            listApplicants.addAll(response.body().getApplicants_list());
                            DashboardPortfolioActivity.pbToolbar.setVisibility(View.GONE);
                            YearsAdapter yearsAdapter = new YearsAdapter(activity, listApplicants);
                            DashboardPortfolioActivity.spnrYears.setAdapter(yearsAdapter);
                            DashboardPortfolioActivity.spnrYears.setSelection(getIndex(listApplicants, ApiUtils.CID));
                        }
                        else
                        {
                            DashboardPortfolioActivity.pbToolbar.setVisibility(View.GONE);
                        }
                    }
                    else
                    {
                        DashboardPortfolioActivity.pbToolbar.setVisibility(View.GONE);
                    }
                }
                else
                {
                    DashboardPortfolioActivity.pbToolbar.setVisibility(View.GONE);
                }
            }

            @Override public void onFailure(Call<AppicantiListResponseModel> call, Throwable t)
            {
                DashboardPortfolioActivity.pbToolbar.setVisibility(View.GONE);
            }
        });
    }

    private int getIndex(ArrayList<AppicantiListResponseModel.ApplicantsListItem> list, String myString)
    {
        for (int i = 0; i < list.size(); i++)
        {
            if (list.get(i).getCid().equalsIgnoreCase(myString))
            {
                return i;
            }
            else
            {
                continue;
            }
        }
        return 0;
    }

    public class YearsAdapter extends BaseAdapter
    {
        ArrayList<AppicantiListResponseModel.ApplicantsListItem> allSpCoinslist;
        Activity activity;

        public YearsAdapter(Activity activity, ArrayList<AppicantiListResponseModel.ApplicantsListItem> allSpCoinslist)
        {
            this.activity = activity;
            this.allSpCoinslist = allSpCoinslist;
        }

        @Override public int getCount()
        {
            return allSpCoinslist.size();
        }

        @Override public Object getItem(int position)
        {
            return allSpCoinslist.get(position);
        }

        @Override public long getItemId(int position)
        {
            return position;
        }

        @Override public View getView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowview = inflater.inflate(R.layout.portfolio_row_view_spinner_allcoins, parent, false);
            TextView text = (TextView) rowview.findViewById(R.id.tvOption);
            text.setText(AppUtils.toDisplayCase(allSpCoinslist.get(position).getApplicantName()));
            return rowview;
        }

        public View getDropDownView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowview = inflater.inflate(R.layout.portfolio_row_view_spinner_dropdown, parent, false);
            TextView text = (TextView) rowview.findViewById(R.id.tvOption);
            text.setText(AppUtils.toDisplayCase(allSpCoinslist.get(position).getApplicantName()));
            return rowview;
        }
    }

    /**
     * call on server to get the Portfolio section data
     */
    private void getDataForPortfolio()
    {
        if (sessionManager.getPortfolioList().size() <= 0)
        {
            binding.loading.llLoading.setVisibility(View.VISIBLE);
        }
        listPortfolioDetails = new ArrayList<>();
        apiService.getPortFolioAPI(ApiUtils.END_USER_ID, strCID).enqueue(new Callback<PortfoliaResponseModel>()
        {
            @Override public void onResponse(Call<PortfoliaResponseModel> call, Response<PortfoliaResponseModel> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().getSuccess() == 1)
                    {
                        listPortfolioDetails.addAll(response.body().getPortfolio_details());
                        grandTotalCurrent = AppUtils.getValidAPIStringResponse(String.valueOf(response.body().getGrand_total().getCurrentValue()));
                        grandTotalInvest = AppUtils.getValidAPIStringResponse(String.valueOf(response.body().getGrand_total().getInitialValue()));
                        grandTotalProfit = AppUtils.getValidAPIStringResponse(String.valueOf(response.body().getGrand_total().getGain()));

                        binding.loading.llLoading.setVisibility(View.GONE);

                        if (listPortfolioDetails != null && listPortfolioDetails.size() > 0)
                        {
                            binding.noData.llNoData.setVisibility(View.GONE);
                            PortfolioAdapter portfolioAdapter = new PortfolioAdapter(listPortfolioDetails);
                            binding.rvPortfolio.setAdapter(portfolioAdapter);
                        }
                        else
                        {
                            binding.noData.llNoData.setVisibility(View.VISIBLE);
                        }

                        binding.txtGrandToalInvested.setText(activity.getString(R.string.rupees) + " " + AppUtils.convertToCommaSeperatedValue(grandTotalInvest));
                        binding.txtGrandTotalCurrent.setText(activity.getString(R.string.rupees) + " " + AppUtils.convertToCommaSeperatedValue(grandTotalCurrent));
                        binding.txtGrandTotalProfit.setText(activity.getString(R.string.rupees) + " " + AppUtils.convertToCommaSeperatedValue(grandTotalProfit));
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

            @Override public void onFailure(Call<PortfoliaResponseModel> call, Throwable t)
            {

            }
        });
    }

    private class PortfolioAdapter extends RecyclerView.Adapter<PortfolioAdapter.ViewHolder>
    {
        ArrayList<PortfoliaResponseModel.PortfolioDetailsItem> listItems;
        private SpringyAdapterAnimator mAnimator;

        PortfolioAdapter(ArrayList<PortfoliaResponseModel.PortfolioDetailsItem> list)
        {
            this.listItems = list;
        }

        @Override public PortfolioAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.portfolio_row_view_portfolio, viewGroup, false);
            return new PortfolioAdapter.ViewHolder(v);
        }

        @Override public void onBindViewHolder(final PortfolioAdapter.ViewHolder holder, int position)
        {
            try
            {
                /*holder.pos = position;*/
                final PortfoliaResponseModel.PortfolioDetailsItem getSet = listItems.get(holder.getAdapterPosition());
                holder.txtPortfolioTitle.setText(AppUtils.toDisplayCase(getSet.getPortfolio_key()));
                holder.txtCatAmount.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToCommaSeperatedValue(String.valueOf(getSet.getTotal_value().getInitialValue())));
                holder.txtCatCurrent.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToCommaSeperatedValue(String.valueOf(getSet.getTotal_value().getCurrentValue())));
                holder.txtCatProfit.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToCommaSeperatedValue(String.valueOf(getSet.getTotal_value().getGain())));

                if (getSet.getPortfolio_value() != null && getSet.getPortfolio_value().size() > 0)
                {
                    PortfolioValueAdapter portfolioValueAdapter = new PortfolioValueAdapter(getSet.getPortfolio_value());
                    holder.rvPortfolioValue.setAdapter(portfolioValueAdapter);
                }
            }
            catch (Resources.NotFoundException e)
            {
                e.printStackTrace();
            }
        }

        @Override public int getItemCount()
        {
            return listItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            private TextView txtPortfolioTitle, txtCatAmount, txtCatCurrent, txtCatProfit;
            private RecyclerView rvPortfolioValue;
            private LinearLayout llCatTotalPortfolio;
            private int pos = -1;

            ViewHolder(View convertView)
            {
                super(convertView);
                txtPortfolioTitle = convertView.findViewById(R.id.txtPortfolioTitle);
                txtCatAmount = convertView.findViewById(R.id.txtCatAmount);
                txtCatCurrent = convertView.findViewById(R.id.txtCatCurrent);
                txtCatProfit = convertView.findViewById(R.id.txtCatProfit);
                rvPortfolioValue = convertView.findViewById(R.id.rvPortfolioValue);
                rvPortfolioValue.setLayoutManager(new LinearLayoutManager(activity));
                llCatTotalPortfolio = convertView.findViewById(R.id.llCatTotalPortfolio);
            }
        }
    }

    private class PortfolioValueAdapter extends RecyclerView.Adapter<PortfolioValueAdapter.ViewHolder>
    {
        ArrayList<PortfoliaResponseModel.PortfolioValueItem> listItems;
        private SpringyAdapterAnimator mAnimator;

        PortfolioValueAdapter(ArrayList<PortfoliaResponseModel.PortfolioValueItem> list)
        {
            this.listItems = list;
        }

        @Override public PortfolioValueAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.portfolio_row_view_portfolio_value, viewGroup, false);
            return new PortfolioValueAdapter.ViewHolder(v);
        }

        @Override public void onBindViewHolder(final PortfolioValueAdapter.ViewHolder holder, final int position)
        {
            try
            {
                final PortfoliaResponseModel.PortfolioValueItem getSet = listItems.get(position);
                holder.txtSubTitle.setText(AppUtils.toDisplayCase(getSet.getSub_cat_key()));
                holder.txtSubCatAmount.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToCommaSeperatedValue(String.valueOf(getSet.getPortfolio_total().getInitialValue())));
                holder.txtSubCatCurrent.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToCommaSeperatedValue(String.valueOf(getSet.getPortfolio_total().getCurrentValue())));
                holder.txtSubCatProfit.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToCommaSeperatedValue(String.valueOf(getSet.getPortfolio_total().getGain())));

                if (getSet.getSub_cat() != null && getSet.getSub_cat().size() > 0)
                {
                    PortfolioSubCatAdapter portfolioSubCatAdapter = new PortfolioSubCatAdapter(getSet.getSub_cat());
                    holder.rvPortfolioSubCat.setAdapter(portfolioSubCatAdapter);
                }

            }
            catch (Resources.NotFoundException e)
            {
                e.printStackTrace();
            }
        }

        @Override public int getItemCount()
        {
            return listItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            private TextView txtSubTitle, txtSubCatAmount, txtSubCatCurrent, txtSubCatProfit;
            private RecyclerView rvPortfolioSubCat;
            private LinearLayout llSubCatTotalPortfolio;

            ViewHolder(View convertView)
            {
                super(convertView);
                txtSubTitle = convertView.findViewById(R.id.txtSubTitle);
                txtSubCatAmount = convertView.findViewById(R.id.txtSubCatAmount);
                txtSubCatCurrent = convertView.findViewById(R.id.txtSubCatCurrent);
                txtSubCatProfit = convertView.findViewById(R.id.txtSubCatProfit);
                rvPortfolioSubCat = convertView.findViewById(R.id.rvPortfolioSubCat);
                rvPortfolioSubCat.setLayoutManager(new LinearLayoutManager(activity));
                llSubCatTotalPortfolio = convertView.findViewById(R.id.llSubCatTotalPortfolio);
            }
        }
    }

    private class PortfolioSubCatAdapter extends RecyclerView.Adapter<PortfolioSubCatAdapter.ViewHolder>
    {
        ArrayList<PortfoliaResponseModel.PortfolioValueItem.SubCatItem> listItems;
        private SpringyAdapterAnimator mAnimator;

        PortfolioSubCatAdapter(ArrayList<PortfoliaResponseModel.PortfolioValueItem.SubCatItem> list)
        {
            this.listItems = list;
        }

        @Override public PortfolioSubCatAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.portfolio_row_view_portfolio_sub_cat, viewGroup, false);
            return new PortfolioSubCatAdapter.ViewHolder(v);
        }

        @Override public void onBindViewHolder(final PortfolioSubCatAdapter.ViewHolder holder, final int position)
        {
            try
            {
                final PortfoliaResponseModel.PortfolioValueItem.SubCatItem getSet = listItems.get(position);


                if (position % 2 == 0)
                {
                    holder.llMainPortfolio.setBackgroundResource(R.color.white);
                }
                else
                {
                    holder.llMainPortfolio.setBackgroundResource(R.color.light_gray_new);
                }

                holder.txtPortfolioFundName.setText(AppUtils.toDisplayCase(getSet.getSchemeName()));
                holder.txtPortfolioAmountInvest.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToCommaSeperatedValue(getSet.getInitialValue()));
                holder.txtPortfolioCurrentValue.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToCommaSeperatedValue(String.valueOf(getSet.getCurrentValue())));
                holder.txtPortfolioProfit.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToCommaSeperatedValue(String.valueOf(getSet.getGain())) + "\n" +getSet.getCAGR() + "%");

                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override public void onClick(View v)
                    {
                        portfolioDetailsDialog(getSet);
                    }
                });
            }
            catch (Resources.NotFoundException e)
            {
                e.printStackTrace();
            }
        }

        @Override public int getItemCount()
        {
            return listItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            private TextView txtPortfolioFundName, txtPortfolioAmountInvest, txtPortfolioCurrentValue, txtPortfolioProfit;
            private LinearLayout llMainPortfolio;

            ViewHolder(View convertView)
            {
                super(convertView);
                txtPortfolioFundName = convertView.findViewById(R.id.txtPortfolioFundName);
                txtPortfolioAmountInvest = convertView.findViewById(R.id.txtPortfolioAmountInvest);
                txtPortfolioCurrentValue = convertView.findViewById(R.id.txtPortfolioCurrentValue);
                txtPortfolioProfit = convertView.findViewById(R.id.txtPortfolioProfit);
                llMainPortfolio = convertView.findViewById(R.id.llMainPortfolio);
            }
        }
    }

    private void portfolioDetailsDialog(PortfoliaResponseModel.PortfolioValueItem.SubCatItem getSet)
    {
        BottomSheetDialog listDialog = new BottomSheetDialog(activity, R.style.MaterialDialogSheetTemp);
        final View sheetView = activity.getLayoutInflater().inflate(R.layout.portfolio_dialog_portfolio_details, null);
        listDialog.setContentView(sheetView);

        TextView txtDialogSchemeName = listDialog.findViewById(R.id.txtDialogSchemeName);
        TextView txtDialogHolderOneName = listDialog.findViewById(R.id.txtDialogHolderOneName);
        TextView txtDialogHolderTwoName = listDialog.findViewById(R.id.txtDialogHolderTwoName);
        TextView txtDialogNominee = listDialog.findViewById(R.id.txtDialogNominee);
        TextView txtDialogHolderOnePan = listDialog.findViewById(R.id.txtDialogHolderOnePan);
        TextView txtDialogHolderTwoPan = listDialog.findViewById(R.id.txtDialogHolderTwoPan);
        TextView txtDialogRemainingUnits = listDialog.findViewById(R.id.txtDialogRemainingUnits);

        getNetworthData(getSet, txtDialogSchemeName, txtDialogHolderOneName, txtDialogHolderTwoName, txtDialogNominee, txtDialogHolderOnePan, txtDialogHolderTwoPan, txtDialogRemainingUnits);

        listDialog.show();
    }

    private void getNetworthData(final PortfoliaResponseModel.PortfolioValueItem.SubCatItem getSet, final TextView txtDialogSchemeName, final TextView txtDialogHolderOneName, final TextView txtDialogHolderTwoName, final TextView txtDialogNominee, final TextView txtDialogHolderOnePan, final TextView txtDialogHolderTwoPan, final TextView txtDialogRemainingUnits)
    {

        apiService.getPortfolioDetailsAPI(getSet.getFolioNo(), getSet.getSCode(), getSet.getSchemeName(), ApiUtils.END_USER_ID).enqueue(new Callback<PortfolioDetailsResponseModel>()
        {
            @Override public void onResponse(Call<PortfolioDetailsResponseModel> call, Response<PortfolioDetailsResponseModel> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().getSuccess() == 1)
                    {
                        SchemeName = response.body().getPortfolioDetails().getSchemeName();
                        Joint1Name = response.body().getPortfolioDetails().getJoint1Name();
                        Joint2Name = response.body().getPortfolioDetails().getJoint2Name();
                        Nominee = response.body().getPortfolioDetails().getNominee();
                        Joint1PAN = response.body().getPortfolioDetails().getJoint1PAN();
                        Joint2PAN = response.body().getPortfolioDetails().getJoint2PANv();
                        RemainingUnits = response.body().getPortfolioDetails().getRemainingUnits();

                        txtDialogSchemeName.setText(AppUtils.toDisplayCase(SchemeName));
                        txtDialogHolderOneName.setText(AppUtils.toDisplayCase(Joint1Name));
                        txtDialogHolderTwoName.setText(AppUtils.toDisplayCase(Joint2Name));
                        txtDialogNominee.setText(AppUtils.toDisplayCase(Nominee));
                        txtDialogHolderOnePan.setText(AppUtils.toDisplayCase(Joint1PAN));
                        txtDialogHolderTwoPan.setText(AppUtils.toDisplayCase(Joint2PAN));
                        txtDialogRemainingUnits.setText(AppUtils.convertToTwoDecimalValue(RemainingUnits));
                    }
                    else
                    {

                    }
                }
                else
                {
                }
            }

            @Override public void onFailure(Call<PortfolioDetailsResponseModel> call, Throwable t)
            {

            }
        });
    }
}
