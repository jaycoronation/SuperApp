package com.application.alphacapital.superapp.pms.activity;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.alphacapital.superapp.R;
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


public class PortfolioActivity extends AppCompatActivity
{
    private Activity activity;

    private SessionManager sessionManager;

    private LinearLayout llBack;

    private ProgressBar pbToolbar;
    private FrameLayout flSpinner;
    private Spinner spnrYears;

    private View llLoading, llNoInternet, llNoData;

    private RecyclerView rvPortfolio;

    private TextView txtGrandToalInvested, txtGrandTotalCurrent, txtGrandTotalProfit;

    private ArrayList<PortfoliaResponseModel.PortfolioDetailsItem> listPortfolioDetails;

    private TinyDB tinyDB;

    private ArrayList<AppicantiListResponseModel.ApplicantsListItem> listApplicants;

    private String strCID = "";

    private PortfolioApiInterface apiService;

    private String grandTotalInvest = "", grandTotalCurrent = "", grandTotalProfit = "", title = "", subCategory = "";
    String SchemeName = "", Joint1Name = "", Joint2Name = "", Nominee = "", Joint1PAN = "", Joint2PAN = "", RemainingUnits = "";


    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.portfolio_activity_portfolio);

        activity = PortfolioActivity.this;
        tinyDB = new TinyDB(activity);
        sessionManager = new SessionManager(activity);
        apiService = PortfolioApiClient.getClient().create(PortfolioApiInterface.class);
        initViews();

        if (AppUtils.isOnline())
        {
            llNoInternet.setVisibility(View.GONE);
            getYears();
        }
        else
        {
            llNoInternet.setVisibility(View.VISIBLE);
        }

        onClicks();
    }

    private void initViews()
    {
        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("Portfolio");

        ImageView imgPageIcon = findViewById(R.id.imgPageIcon);
        imgPageIcon.setImageResource(R.drawable.portfolio_ic_portfolio_white);

        llLoading = findViewById(R.id.llLoading);
        llNoInternet = findViewById(R.id.llNoInternet);
        llNoData = findViewById(R.id.llNoData);

        llBack = findViewById(R.id.llBack);

        pbToolbar = findViewById(R.id.pbToolbar);

        flSpinner = findViewById(R.id.flSpinner);
        flSpinner.setVisibility(View.VISIBLE);

        spnrYears = findViewById(R.id.spnrYears);

        rvPortfolio = findViewById(R.id.rvPortfolio);
        rvPortfolio.setLayoutManager(new LinearLayoutManager(activity));

        txtGrandToalInvested = findViewById(R.id.txtGrandToalInvested);
        txtGrandTotalCurrent = findViewById(R.id.txtGrandTotalCurrent);
        txtGrandTotalProfit = findViewById(R.id.txtGrandTotalProfit);
    }

    private void onClicks()
    {
        llBack.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
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
        });

        spnrYears.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
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

    /**
     * call on server to bind the years to get the year wise data
     */
    private void getYears()
    {
        pbToolbar.setVisibility(View.VISIBLE);
        llLoading.setVisibility(View.VISIBLE);
        listApplicants = new ArrayList<>();
        apiService.getAppicantsAPI(ApiUtils.END_USER_ID).enqueue(new Callback<AppicantiListResponseModel>()
        {
            @Override public void onResponse(Call<AppicantiListResponseModel> call, Response<AppicantiListResponseModel> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().getSuccess() == 1)
                    {
                        listApplicants.addAll(response.body().getApplicants_list());

                        pbToolbar.setVisibility(View.GONE);
                        YearsAdapter yearsAdapter = new YearsAdapter(activity, listApplicants);
                        spnrYears.setAdapter(yearsAdapter);
                        spnrYears.setSelection(getIndex(listApplicants, ApiUtils.CID));
                    }
                    else
                    {

                    }
                }
                else
                {

                }
            }

            @Override public void onFailure(Call<AppicantiListResponseModel> call, Throwable t)
            {

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
        if (llLoading.getVisibility() == View.GONE)
        {
            llLoading.setVisibility(View.VISIBLE);
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

                        llLoading.setVisibility(View.GONE);

                        if (listPortfolioDetails != null && listPortfolioDetails.size() > 0)
                        {
                            llNoData.setVisibility(View.GONE);
                            PortfolioAdapter portfolioAdapter = new PortfolioAdapter(listPortfolioDetails);
                            rvPortfolio.setAdapter(portfolioAdapter);
                        }
                        else
                        {
                            llNoData.setVisibility(View.VISIBLE);
                        }

                        txtGrandToalInvested.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToCommaSeperatedValue(grandTotalInvest));
                        txtGrandTotalCurrent.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToCommaSeperatedValue(grandTotalCurrent));
                        txtGrandTotalProfit.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToCommaSeperatedValue(grandTotalProfit));
                    }
                    else
                    {
                        llNoData.setVisibility(View.VISIBLE);
                        llLoading.setVisibility(View.GONE);
                    }
                }
                else
                {
                    llNoData.setVisibility(View.VISIBLE);
                    llLoading.setVisibility(View.GONE);
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

        @Override public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.portfolio_row_view_portfolio, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override public void onBindViewHolder(final ViewHolder holder, int position)
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

        @Override public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.portfolio_row_view_portfolio_value, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override public void onBindViewHolder(final ViewHolder holder, final int position)
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

        @Override public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.portfolio_row_view_portfolio_sub_cat, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override public void onBindViewHolder(final ViewHolder holder, final int position)
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

       /* new AsyncTask<Void, Void, Void>()
        {
            int status = -1;

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... voids)
            {
                try
                {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("FolioNo", getSet.getFolioNo());
                    hashMap.put("SCode", getSet.getSCode());
                    hashMap.put("SchemeName", getSet.getSchemeName());
                    hashMap.put("user_id", ApiUtils.END_USER_ID);

                    String serverResponse = MitsUtils.readJSONServiceUsingPOST(ApiUtils.GET_PORTFOLIO_DETAILS, hashMap);

                    AppUtils.printLog("PortfolioDetail", serverResponse);
                    AppUtils.printLog("hashMap", hashMap.toString());

                    JSONObject jsonObject = new JSONObject(serverResponse);

                    status = AppUtils.getValidAPIIntegerResponse(jsonObject.optString("success"));

                    if (status == 1)
                    {
                        JSONObject portfolioObj = jsonObject.optJSONObject("portfolioDetails");

                        SchemeName = portfolioObj.optString("SchemeName");
                        Joint1Name = portfolioObj.optString("Joint1Name");
                        Joint2Name = portfolioObj.optString("Joint2Name");
                        Nominee = portfolioObj.optString("Nominee");
                        Joint1PAN = portfolioObj.optString("Joint1PAN");
                        Joint2PAN = portfolioObj.optString("Joint2PAN");
                        RemainingUnits = portfolioObj.optString("RemainingUnits");
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid)
            {
                super.onPostExecute(aVoid);

                txtDialogSchemeName.setText(AppUtils.toDisplayCase(SchemeName));
                txtDialogHolderOneName.setText(AppUtils.toDisplayCase(Joint1Name));
                txtDialogHolderTwoName.setText(AppUtils.toDisplayCase(Joint2Name));
                txtDialogNominee.setText(AppUtils.toDisplayCase(Nominee));
                txtDialogHolderOnePan.setText(AppUtils.toDisplayCase(Joint1PAN));
                txtDialogHolderTwoPan.setText(AppUtils.toDisplayCase(Joint2PAN));
                txtDialogRemainingUnits.setText(AppUtils.convertToTwoDecimalValue(RemainingUnits));
            }
        }.execute();*/
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
