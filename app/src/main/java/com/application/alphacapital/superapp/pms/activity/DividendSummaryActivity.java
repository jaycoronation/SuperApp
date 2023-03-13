package com.application.alphacapital.superapp.pms.activity;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.alphacapital.superapp.R;
import com.application.alphacapital.superapp.pms.beans.DividendSummaryResponseModel;
import com.application.alphacapital.superapp.pms.network.PortfolioApiClient;
import com.application.alphacapital.superapp.pms.network.PortfolioApiInterface;
import com.application.alphacapital.superapp.pms.utils.ApiUtils;
import com.application.alphacapital.superapp.pms.utils.AppUtils;
import com.application.alphacapital.superapp.pms.utils.SessionManager;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimationType;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DividendSummaryActivity extends AppCompatActivity
{
    private Activity activity ;

    private SessionManager sessionManager ;

    private Toolbar toolbar;

    private LinearLayout llLogo, llSearch, llShare;

    private View llLoading,llNoInternet,llNoData,llBack ;

    private RecyclerView rvDividendSummary;

    private ArrayList<DividendSummaryResponseModel.DividendSummaryItem> listDividendSummary;

    private DividendSummaryAdapter dividendSummaryAdapter;

    private PortfolioApiInterface apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.portfolio_activity_dividend_summary);

            activity = DividendSummaryActivity.this ;
            sessionManager = new SessionManager(activity);
            apiService = PortfolioApiClient.getClient().create(PortfolioApiInterface.class);
            initViews();
            if(AppUtils.isOnline())
            {
                llNoInternet.setVisibility(View.GONE);
                getDividendSummaryData();
            }
            else
            {
                llNoInternet.setVisibility(View.VISIBLE);
            }

            onClicks();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private void initViews()
    {
        try
        {
            toolbar = (Toolbar) findViewById(R.id.toolbar);

            TextView txtTitle = toolbar.findViewById(R.id.txtTitle);
            txtTitle.setText("Dividend Summary");

            ImageView imgPageIcon = findViewById(R.id.imgPageIcon);
            imgPageIcon.setImageResource(R.drawable.portfolio_ic_dividend_summary_white);

            llLoading = findViewById(R.id.llLoading);
            llNoInternet = findViewById(R.id.llNoInternet);
            llNoData = findViewById(R.id.llNoData);
            llBack = toolbar.findViewById(R.id.llBack);
            llSearch = toolbar.findViewById(R.id.llSearch);
            llSearch.setVisibility(View.GONE);
            llShare = toolbar.findViewById(R.id.llShare);
            llShare.setVisibility(View.GONE);
            llLogo = toolbar.findViewById(R.id.llLogo);

            rvDividendSummary = findViewById(R.id.rvDividendSummary);
            rvDividendSummary.setLayoutManager(new LinearLayoutManager(activity));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void onClicks()
    {
        llBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
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
    }

    /**
     * call on server to get the DvidendSummary section data
     */
    private void getDividendSummaryData()
    {
        try
        {
            llLoading.setVisibility(View.VISIBLE);
            listDividendSummary = new ArrayList<>();
            apiService.getDividendAPI(ApiUtils.END_USER_ID).enqueue(new Callback<DividendSummaryResponseModel>() {
                @Override
                public void onResponse(Call<DividendSummaryResponseModel> call, Response<DividendSummaryResponseModel> response) {
                    if (response.isSuccessful())
                    {
                        if (response.body().getSuccess() == 1)
                        {
                            listDividendSummary.addAll(response.body().getDividend_summary());
                            llLoading.setVisibility(View.GONE);

                            if(listDividendSummary != null && listDividendSummary.size() > 0)
                            {
                                llNoData.setVisibility(View.GONE);

                                dividendSummaryAdapter = new DividendSummaryAdapter(listDividendSummary);
                                rvDividendSummary.setAdapter(dividendSummaryAdapter);
                            }
                            else
                            {
                                llNoData.setVisibility(View.VISIBLE);
                            }
                        }
                        else
                        {

                        }
                    }
                    else
                    {}
                }

                @Override
                public void onFailure(Call<DividendSummaryResponseModel> call, Throwable t) {

                }
            });

            /*new AsyncTask<Void, Void, Void>()
            {
                int status = -1;

                @Override
                protected void onPreExecute()
                {
                    super.onPreExecute();

                    llLoading.setVisibility(View.VISIBLE);
                    listDividendSummary = new ArrayList<>();
                }

                @Override
                protected Void doInBackground(Void... voids)
                {
                    try
                    {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("user_id", ApiUtils.END_USER_ID);

                        String serverResponse = MitsUtils.readJSONServiceUsingPOST(ApiUtils.GET_DIVIDEND_SUMMARY, hashMap);

                        AppUtils.printLog("DividendResponse",serverResponse);

                        JSONObject jsonObject = new JSONObject(serverResponse);

                        status = AppUtils.getValidAPIIntegerResponse(jsonObject.optString("success"));

                        if (status == 1)
                        {
                            JSONArray dividendSummaryArray = jsonObject.optJSONArray("dividend_summary");

                            if(dividendSummaryArray.length() > 0)
                            {
                                for (int i = 0; i < dividendSummaryArray.length(); i++)
                                {
                                    JSONObject dividendSummaryObj = dividendSummaryArray.getJSONObject(i);

                                    DividendSummaryBean dividendSummaryBean = new DividendSummaryBean();
                                    dividendSummaryBean.setApplicantName(AppUtils.getValidAPIStringResponse(dividendSummaryObj.optString("ApplicantName")));
                                    dividendSummaryBean.setGrandTotal(AppUtils.getValidAPIStringResponse(dividendSummaryObj.optString("GrandTotal")));

                                    JSONArray userArray = dividendSummaryObj.optJSONArray("DividendSummaryDetail");

                                    if(userArray.length() > 0)
                                    {
                                        ArrayList<DividendSummaryBean.DividendSummaryDetailGetSet> listDividendDetails = new ArrayList<>();

                                        for (int j = 0; j < userArray.length(); j++)
                                        {
                                            JSONObject userObj =  userArray.getJSONObject(j);

                                            DividendSummaryBean.DividendSummaryDetailGetSet dividendSummaryDetailGetSet = new DividendSummaryBean.DividendSummaryDetailGetSet();

                                            dividendSummaryDetailGetSet.setFolioNo(userObj.optString("FolioNo"));
                                            dividendSummaryDetailGetSet.setTotal(AppUtils.getValidAPIStringResponse(userObj.optString("Total")));
                                            dividendSummaryDetailGetSet.setSchemeName(AppUtils.getValidAPIStringResponse(userObj.optString("SchemeName")));
                                            dividendSummaryDetailGetSet.setDivPayout(AppUtils.getValidAPIStringResponse(userObj.optString("DivPayout")));
                                            dividendSummaryDetailGetSet.setDivReinvest(AppUtils.getValidAPIStringResponse(userObj.optString("DivReinvest")));

                                            listDividendDetails.add(dividendSummaryDetailGetSet);
                                        }

                                        dividendSummaryBean.setDividendSummaryDetail(listDividendDetails);
                                    }

                                    listDividendSummary.add(dividendSummaryBean);
                                }
                            }

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

                    llLoading.setVisibility(View.GONE);

                    if(listDividendSummary != null && listDividendSummary.size() > 0)
                    {
                        llNoData.setVisibility(View.GONE);

                        dividendSummaryAdapter = new DividendSummaryAdapter(listDividendSummary);
                        rvDividendSummary.setAdapter(dividendSummaryAdapter);
                    }
                    else
                    {
                        llNoData.setVisibility(View.VISIBLE);
                    }
                }
            }.execute();*/
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private class DividendSummaryAdapter extends RecyclerView.Adapter<DividendSummaryAdapter.ViewHolder>
    {
        ArrayList<DividendSummaryResponseModel.DividendSummaryItem> listItems;

        DividendSummaryAdapter(ArrayList<DividendSummaryResponseModel.DividendSummaryItem> list)
        {
            this.listItems = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.portfolio_row_view_dividend_summary, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            try
            {
                final DividendSummaryResponseModel.DividendSummaryItem getSet = listItems.get(position);


                holder.txtDividendTitle.setText(AppUtils.toDisplayCase(getSet.getApplicantName()));

                if(getSet.getDividendSummaryDetail() != null && getSet.getDividendSummaryDetail().size() > 0)
                {
                    DividendSummaryItemAdapter dividendSummaryItemAdapter = new DividendSummaryItemAdapter(getSet.getDividendSummaryDetail());
                    holder.rvDividendSummaryItem.setAdapter(dividendSummaryItemAdapter);
                }

                holder.txtDividendGrandTotal.setText(getResources().getString(R.string.rupees) + " "  + AppUtils.convertToTwoDecimalValue(getSet.getGrandTotal()));
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
            private TextView txtDividendTitle,txtDividendGrandTotal;
            private RecyclerView rvDividendSummaryItem ;

            ViewHolder(View convertView)
            {
                super(convertView);
                try
                {
                    txtDividendTitle = convertView.findViewById(R.id.txtDividendTitle);
                    txtDividendGrandTotal = convertView.findViewById(R.id.txtDividendGrandTotal);

                    rvDividendSummaryItem = convertView.findViewById(R.id.rvDividendSummaryItem);
                    rvDividendSummaryItem.setLayoutManager(new LinearLayoutManager(activity));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private class DividendSummaryItemAdapter extends RecyclerView.Adapter<DividendSummaryItemAdapter.ViewHolder>
    {
        ArrayList<DividendSummaryResponseModel.DividendSummaryDetailItem> listItems;
        private SpringyAdapterAnimator mAnimator;

        DividendSummaryItemAdapter(ArrayList<DividendSummaryResponseModel.DividendSummaryDetailItem> list)
        {
            this.listItems = list;
            mAnimator = new SpringyAdapterAnimator(rvDividendSummary);
            mAnimator.setSpringAnimationType(SpringyAdapterAnimationType.SLIDE_FROM_BOTTOM);
            mAnimator.addConfig(85, 15);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.portfolio_row_view_dividend_summary_items, viewGroup, false);
            mAnimator.onSpringItemCreate(v);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            try
            {
                final DividendSummaryResponseModel.DividendSummaryDetailItem getSet = listItems.get(position);

                if (position % 2 == 0)
                {
                    holder.llMainDividend.setBackgroundResource(R.color.white);
                }
                else
                {
                    holder.llMainDividend.setBackgroundResource(R.color.light_gray_new);
                }

                holder.txtDividendSchemeName.setText(AppUtils.toDisplayCase(getSet.getSchemeName()));
                holder.txtDividentFolioNo.setText(getSet.getFolioNo());
                holder.txtDividendPayout.setText(getSet.getDivPayout());
                holder.txtDividendInvestment.setText(getResources().getString(R.string.rupees) + " "  + AppUtils.convertToTwoDecimalValue(getSet.getDivReinvest()));
                holder.txtDividendTotal.setText(getResources().getString(R.string.rupees) + " "  + AppUtils.convertToTwoDecimalValue(getSet.getTotal()));

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
            private TextView txtDividendSchemeName, txtDividentFolioNo, txtDividendPayout,txtDividendInvestment,txtDividendTotal;
            private LinearLayout llMainDividend ;

            ViewHolder(View convertView)
            {
                super(convertView);
                txtDividendSchemeName = convertView.findViewById(R.id.txtDividendSchemeName);
                txtDividentFolioNo = convertView.findViewById(R.id.txtDividentFolioNo);
                txtDividendPayout = convertView.findViewById(R.id.txtDividendPayout);
                txtDividendInvestment = convertView.findViewById(R.id.txtDividendInvestment);
                txtDividendTotal = convertView.findViewById(R.id.txtDividendTotal);

                llMainDividend = convertView.findViewById(R.id.llMainDividend);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
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
