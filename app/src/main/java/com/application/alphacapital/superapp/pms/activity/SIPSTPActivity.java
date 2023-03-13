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
import com.application.alphacapital.superapp.pms.beans.SIPSTPResponseModel;
import com.application.alphacapital.superapp.pms.network.PortfolioApiClient;
import com.application.alphacapital.superapp.pms.network.PortfolioApiInterface;
import com.application.alphacapital.superapp.pms.utils.ApiUtils;
import com.application.alphacapital.superapp.pms.utils.AppUtils;
import com.application.alphacapital.superapp.pms.utils.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimationType;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SIPSTPActivity extends AppCompatActivity
{
    private Activity activity;

    private SessionManager sessionManager;

    private Toolbar toolbar;

    private LinearLayout llFilter;

    private LinearLayout llLogo, llSearch, llShare;

    private View llLoading, llNoInternet, llNoData, llBack;

    private RecyclerView rvSipStp;

    private ArrayList<SIPSTPResponseModel.SipStpDetailsItem> listSIPSTP;

    private DividendSummaryAdapter dividendSummaryAdapter;

    private String folioType = "non_zero";

    private PortfolioApiInterface apiService;

    @Override protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.portfolio_activity_sip_stp);

            activity = SIPSTPActivity.this;
            sessionManager = new SessionManager(activity);
            apiService = PortfolioApiClient.getClient().create(PortfolioApiInterface.class);
            initViews();
            if (AppUtils.isOnline())
            {
                llNoInternet.setVisibility(View.GONE);
                getSIPSTPData(folioType);
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
            toolbar = findViewById(R.id.toolbar);

            TextView txtTitle = toolbar.findViewById(R.id.txtTitle);
            txtTitle.setText("SIP & STP");

            ImageView imgPageIcon = findViewById(R.id.imgPageIcon);
            imgPageIcon.setImageResource(R.drawable.portfolio_ic_sip_stp_white);

            llFilter = toolbar.findViewById(R.id.llFilter);
            llFilter.setVisibility(View.VISIBLE);

            llLoading = findViewById(R.id.llLoading);
            llNoInternet = findViewById(R.id.llNoInternet);
            llNoData = findViewById(R.id.llNoData);
            llBack = toolbar.findViewById(R.id.llBack);
            llSearch = toolbar.findViewById(R.id.llSearch);
            llSearch.setVisibility(View.GONE);
            llShare = toolbar.findViewById(R.id.llShare);
            llShare.setVisibility(View.GONE);
            llLogo = toolbar.findViewById(R.id.llLogo);

            rvSipStp = findViewById(R.id.rvSipStp);
            rvSipStp.setLayoutManager(new LinearLayoutManager(activity));
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

        llFilter.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                filtetSIPSTPDialog();
            }
        });
    }

    private void filtetSIPSTPDialog()
    {
        final BottomSheetDialog listDialog = new BottomSheetDialog(activity, R.style.MaterialDialogSheetTemp);
        final View sheetView = activity.getLayoutInflater().inflate(R.layout.portfolio_dialog_sip_stp_filter, null);
        listDialog.setContentView(sheetView);

        LinearLayout llNonZero = listDialog.findViewById(R.id.llNonZero);
        LinearLayout llAllFolio = listDialog.findViewById(R.id.llAllFolio);
        TextView txtSelectedNonZero = listDialog.findViewById(R.id.txtSelectedNonZero);
        TextView txtSelectedAll = listDialog.findViewById(R.id.txtSelectedAll);

        if (folioType.equalsIgnoreCase("non_zero"))
        {
            txtSelectedNonZero.setVisibility(View.VISIBLE);
            txtSelectedAll.setVisibility(View.GONE);
        }
        else
        {
            txtSelectedNonZero.setVisibility(View.GONE);
            txtSelectedAll.setVisibility(View.VISIBLE);
        }

        llNonZero.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                folioType = "non_zero";
                getSIPSTPData(folioType);
                listDialog.dismiss();
            }
        });

        llAllFolio.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                folioType = "all";
                getSIPSTPData(folioType);
                listDialog.dismiss();
            }
        });

        listDialog.show();
    }

    /**
     * call on server to get the SIPSTP section data
     *
     * @param folioType pass the folioType (non_zero|all) to filter the SIPSTP data
     */
    private void getSIPSTPData(final String folioType)
    {
        try
        {
            llLoading.setVisibility(View.VISIBLE);
            listSIPSTP = new ArrayList<>();
            apiService.getSIPSTPAPI(ApiUtils.END_USER_ID, folioType).enqueue(new Callback<SIPSTPResponseModel>()
            {
                @Override public void onResponse(Call<SIPSTPResponseModel> call, Response<SIPSTPResponseModel> response)
                {
                    if (response.isSuccessful())
                    {
                        if (response.body().getSuccess() == 1)
                        {
                            listSIPSTP.addAll(response.body().getSip_stp_details());

                            if (listSIPSTP != null && listSIPSTP.size() > 0)
                            {
                                llNoData.setVisibility(View.GONE);
                                dividendSummaryAdapter = new DividendSummaryAdapter(listSIPSTP);
                                rvSipStp.setAdapter(dividendSummaryAdapter);
                            }
                            else
                            {
                                llNoData.setVisibility(View.VISIBLE);
                            }
                        }
                        else
                        {
                            llNoData.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {
                        llNoData.setVisibility(View.VISIBLE);
                    }
                    llLoading.setVisibility(View.GONE);
                }

                @Override public void onFailure(Call<SIPSTPResponseModel> call, Throwable t)
                {
                    llLoading.setVisibility(View.GONE);
                    llNoData.setVisibility(View.VISIBLE);
                }
            });

            /*new AsyncTask<Void, Void, Void>()
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
                        hashMap.put("user_id", ApiUtils.END_USER_ID);
                        hashMap.put("folio_type", folioType);

                        String serverResponse = MitsUtils.readJSONServiceUsingPOST(ApiUtils.GET_SIP_STP, hashMap);

                        AppUtils.printLog("SIPSTP",serverResponse);
                        AppUtils.printLog("hashMap",hashMap.toString());

                        JSONObject jsonObject = new JSONObject(serverResponse);

                        status = AppUtils.getValidAPIIntegerResponse(jsonObject.optString("success"));

                        if (status == 1)
                        {
                            JSONArray sipstpArray = jsonObject.optJSONArray("sip_stp_details");

                            if(sipstpArray.length() > 0)
                            {
                                for (int i = 0; i < sipstpArray.length(); i++)
                                {
                                    JSONObject sipstpObj = sipstpArray.getJSONObject(i);

                                    SIPSTPBean sipstpBean = new SIPSTPBean();
                                    sipstpBean.setSip_stp_key(AppUtils.getValidAPIStringResponse(sipstpObj.optString("sip_stp_key")));

                                    JSONArray sipstpValueArray = sipstpObj.optJSONArray("sip_stp_value");

                                    if(sipstpValueArray.length() > 0)
                                    {
                                        ArrayList<SIPSTPBean.SipStpValueGetSet> listSipStpValues = new ArrayList<>();
                                        for (int j = 0; j < sipstpValueArray.length(); j++)
                                        {
                                            JSONObject sipstpValueObj = sipstpValueArray.getJSONObject(j);

                                            SIPSTPBean.SipStpValueGetSet sipStpValueGetSet = new SIPSTPBean.SipStpValueGetSet();
                                            sipStpValueGetSet.setCurrentNav(AppUtils.getValidAPIStringResponse(sipstpValueObj.optString("CurrentNav")));
                                            sipStpValueGetSet.setPresentAmount(AppUtils.getValidAPIStringResponse(sipstpValueObj.optString("PresentAmount")));
                                            sipStpValueGetSet.setTranDate(AppUtils.getValidAPIStringResponse(sipstpValueObj.optString("TranDate")));
                                            sipStpValueGetSet.setTranTimestamp(AppUtils.getValidAPIIntegerResponse(sipstpValueObj.optString("TranTimestamp")));
                                            sipStpValueGetSet.setApplicant(AppUtils.getValidAPIStringResponse(sipstpValueObj.optString("Applicant")));
                                            sipStpValueGetSet.setSchemeName(AppUtils.getValidAPIStringResponse(sipstpValueObj.optString("SchemeName")));
                                            sipStpValueGetSet.setType(AppUtils.getValidAPIStringResponse(sipstpValueObj.optString("Type")));
                                            sipStpValueGetSet.setNature(AppUtils.getValidAPIStringResponse(sipstpValueObj.optString("Nature")));
                                            sipStpValueGetSet.setFolioNo(AppUtils.getValidAPIStringResponse(sipstpValueObj.optString("FolioNo")));
                                            sipStpValueGetSet.setPurchasedNav(AppUtils.getValidAPIStringResponse(sipstpValueObj.optString("PurchasedNav")));
                                            sipStpValueGetSet.setUnits(AppUtils.getValidAPIStringResponse(sipstpValueObj.optString("Units")));
                                            sipStpValueGetSet.setAmount(AppUtils.getValidAPIIntegerResponse(sipstpValueObj.optString("Amount")));
                                            sipStpValueGetSet.setFCode(AppUtils.getValidAPIStringResponse(sipstpValueObj.optString("FCode")));
                                            sipStpValueGetSet.setSCode(AppUtils.getValidAPIStringResponse(sipstpValueObj.optString("SCode")));
                                            sipStpValueGetSet.setProduct(AppUtils.getValidAPIStringResponse(sipstpValueObj.optString("Product")));
                                            sipStpValueGetSet.setFirstHolder(AppUtils.getValidAPIStringResponse(sipstpValueObj.optString("FirstHolder")));
                                            sipStpValueGetSet.setSecondHolder(AppUtils.getValidAPIStringResponse(sipstpValueObj.optString("SecondHolder")));
                                            sipStpValueGetSet.setHoldingDays(AppUtils.getValidAPIIntegerResponse(sipstpValueObj.optString("holdingDays")));
                                            sipStpValueGetSet.setHoldingValue(AppUtils.getValidAPIIntegerResponse(sipstpValueObj.optString("HoldingValue")));
                                            sipStpValueGetSet.setGainLoss(AppUtils.getValidAPIIntegerResponse(sipstpValueObj.optString("GainLoss")));
                                            sipStpValueGetSet.setAbsoluteReturn(AppUtils.getValidAPIIntegerResponse(sipstpValueObj.optString("absoluteReturn")));
                                            sipStpValueGetSet.setCAGR(AppUtils.getValidAPIIntegerResponse(sipstpValueObj.optString("CAGR")));

                                            listSipStpValues.add(sipStpValueGetSet);
                                        }

                                        sipstpBean.setSip_stp_value(listSipStpValues);
                                    }

                                    JSONObject sipstpTotalObj = sipstpObj.optJSONObject("sip_stp_total");

                                    SIPSTPBean.SipStpTotalGetSet sipStpTotalGetSet = new SIPSTPBean.SipStpTotalGetSet();
                                    sipStpTotalGetSet.setAmount(AppUtils.getValidAPIIntegerResponse(sipstpTotalObj.optString("Amount")));
                                    sipStpTotalGetSet.setUnits(AppUtils.getValidAPIIntegerResponse(sipstpTotalObj.optString("Units")));
                                    sipStpTotalGetSet.setNav(AppUtils.getValidAPIIntegerResponse(sipstpTotalObj.optString("Nav")));
                                    sipStpTotalGetSet.setGainLoss(AppUtils.getValidAPIIntegerResponse(sipstpTotalObj.optString("GainLoss")));
                                    sipStpTotalGetSet.setCAGR(AppUtils.getValidAPIIntegerResponse(sipstpTotalObj.optString("CAGR")));
                                    sipStpTotalGetSet.setPresentAmount(AppUtils.getValidAPIIntegerResponse(sipstpTotalObj.optString("PresentAmount")));
                                    sipStpTotalGetSet.setHoldingValue(AppUtils.getValidAPIIntegerResponse(sipstpTotalObj.optString("HoldingValue")));
                                    sipStpTotalGetSet.setCurrentNav(AppUtils.getValidAPIIntegerResponse(sipstpTotalObj.optString("CurrentNav")));
                                    sipStpTotalGetSet.setHoldingDays(AppUtils.getValidAPIIntegerResponse(sipstpTotalObj.optString("holdingDays")));
                                    sipStpTotalGetSet.setAbsoluteReturn(AppUtils.getValidAPIIntegerResponse(sipstpTotalObj.optString("absoluteReturn")));
                                    sipStpTotalGetSet.setPurchasedNav(AppUtils.getValidAPIIntegerResponse(sipstpTotalObj.optString("PurchasedNav")));

                                    sipstpBean.setSip_stp_total(sipStpTotalGetSet);
                                    listSIPSTP.add(sipstpBean);
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

                    if(listSIPSTP != null && listSIPSTP.size() > 0)
                    {
                        llNoData.setVisibility(View.GONE);

                        dividendSummaryAdapter = new DividendSummaryAdapter(listSIPSTP);
                        rvSipStp.setAdapter(dividendSummaryAdapter);
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
        ArrayList<SIPSTPResponseModel.SipStpDetailsItem> listItems;

        DividendSummaryAdapter(ArrayList<SIPSTPResponseModel.SipStpDetailsItem> list)
        {
            this.listItems = list;
        }

        @Override public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.portfolio_row_view_sip_stp_summary, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            try
            {
                final SIPSTPResponseModel.SipStpDetailsItem getSet = listItems.get(position);

                holder.txtSipStpTitle.setText(AppUtils.toDisplayCase(getSet.getSip_stp_key()));

                if (getSet.getSip_stp_value() != null && getSet.getSip_stp_value().size() > 0)
                {
                    DividendSummaryItemAdapter dividendSummaryItemAdapter = new DividendSummaryItemAdapter(getSet.getSip_stp_value());
                    holder.rvSipStpItem.setAdapter(dividendSummaryItemAdapter);
                }

                holder.txtSSTotalAmountInvested.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToTwoDecimalValue(String.valueOf(getSet.getSip_stp_total().getAmount())));
                holder.txtSSTotalPresentValue.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToTwoDecimalValue(String.valueOf(getSet.getSip_stp_total().getPresentAmount())));
                holder.txtSSTotalGainLoss.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToTwoDecimalValue(String.valueOf(getSet.getSip_stp_total().getGainLoss())));
                holder.txtSSTotalHoldingValue.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToTwoDecimalValue(String.valueOf(getSet.getSip_stp_total().getHoldingValue())));
                holder.txtSSTotalUnitsAlloted.setText(String.valueOf(getSet.getSip_stp_total().getUnits()));
                holder.txtSSTotalNav.setText(String.valueOf(getSet.getSip_stp_total().getPurchasedNav()));
                holder.txtSSTotalCurrentNav.setText(String.valueOf(getSet.getSip_stp_total().getCurrentNav()));
                holder.txtSSTotalNoOfDays.setText(String.valueOf(getSet.getSip_stp_total().getHoldingDays()));
                holder.txtSSTotalAbsoluteReturn.setText(getSet.getSip_stp_total().getAbsoluteReturn() + "%");
                holder.txtSSTotalAnnualisedReturn.setText(getSet.getSip_stp_total().getCAGR() + "%");
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
            private TextView txtSipStpTitle;
            private LinearLayout llSipStpTotal;
            private TextView txtSSTotalAmountInvested;
            private TextView txtSSTotalCurrentNav;
            private TextView txtSSTotalNoOfDays;
            private TextView txtSSTotalHoldingValue;
            private TextView txtSSTotalUnitsAlloted;
            private TextView txtSSTotalPresentValue;
            private TextView txtSSTotalAbsoluteReturn;
            private TextView txtSSTotalNav;
            private TextView txtSSTotalGainLoss;
            private TextView txtSSTotalAnnualisedReturn;
            private RecyclerView rvSipStpItem;

            ViewHolder(View convertView)
            {
                super(convertView);
                try
                {
                    txtSipStpTitle = convertView.findViewById(R.id.txtSipStpTitle);

                    rvSipStpItem = convertView.findViewById(R.id.rvSipStpItem);
                    rvSipStpItem.setLayoutManager(new LinearLayoutManager(activity));

                    llSipStpTotal = convertView.findViewById(R.id.llSipStpTotal);
                    txtSSTotalAmountInvested = convertView.findViewById(R.id.txtSSTotalAmountInvested);
                    txtSSTotalCurrentNav = convertView.findViewById(R.id.txtSSTotalCurrentNav);
                    txtSSTotalNoOfDays = convertView.findViewById(R.id.txtSSTotalNoOfDays);
                    txtSSTotalHoldingValue = convertView.findViewById(R.id.txtSSTotalHoldingValue);
                    txtSSTotalUnitsAlloted = convertView.findViewById(R.id.txtSSTotalUnitsAlloted);
                    txtSSTotalPresentValue = convertView.findViewById(R.id.txtSSTotalPresentValue);
                    txtSSTotalAbsoluteReturn = convertView.findViewById(R.id.txtSSTotalAbsoluteReturn);
                    txtSSTotalNav = convertView.findViewById(R.id.txtSSTotalNav);
                    txtSSTotalGainLoss = convertView.findViewById(R.id.txtSSTotalGainLoss);
                    txtSSTotalAnnualisedReturn = convertView.findViewById(R.id.txtSSTotalAnnualisedReturn);
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
        ArrayList<SIPSTPResponseModel.SipStpValueItem> listItems;
        private final SpringyAdapterAnimator mAnimator;

        DividendSummaryItemAdapter(ArrayList<SIPSTPResponseModel.SipStpValueItem> list)
        {
            this.listItems = list;
            mAnimator = new SpringyAdapterAnimator(rvSipStp);
            mAnimator.setSpringAnimationType(SpringyAdapterAnimationType.SLIDE_FROM_BOTTOM);
            mAnimator.addConfig(85, 15);
        }

        @Override public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.portfolio_row_view_sip_stp_scheme_value, viewGroup, false);
            mAnimator.onSpringItemCreate(v);
            return new ViewHolder(v);
        }

        @Override public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            try
            {
                final SIPSTPResponseModel.SipStpValueItem getSet = listItems.get(position);

                if (position % 2 == 0)
                {
                    holder.llSipStpMain.setBackgroundResource(R.color.white);
                }
                else
                {
                    holder.llSipStpMain.setBackgroundResource(R.color.light_gray_new);
                }

                holder.txtSSFolioNo.setText(getSet.getFolioNo());
                holder.txtSSType.setText(getSet.getType());
                holder.txtSSNav.setText(getSet.getPurchasedNav());
                holder.txtSSGainLoss.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToTwoDecimalValue(String.valueOf(getSet.getGainLoss())));
                holder.txtSSAnnualisedReturn.setText(getSet.getCAGR() + "%");
                holder.txtSSSecondHolder.setText(getSet.getSecondHolder());
                holder.txtSSDateofInvestment.setText(getSet.getTranDate());
                holder.txtSSAmountInvested.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToTwoDecimalValue(String.valueOf(getSet.getAmount())));
                holder.txtSSCurrentNav.setText(String.valueOf(getSet.getCurrentNav()));
                holder.txtSSNoOfDays.setText(AppUtils.convertToTwoDecimalValue(String.valueOf(getSet.getHoldingDays())));
                holder.txtSSApplicant.setText(getSet.getApplicant());
                holder.txtSSHoldingValue.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToTwoDecimalValue(String.valueOf(getSet.getHoldingValue())));
                holder.txtSSNature.setText(getSet.getNature());
                holder.txtSSUnitsAlloted.setText(getSet.getUnits());
                holder.txtSSPresentValue.setText(String.valueOf(getSet.getPresentAmount()));
                holder.txtSSAbsoluteReturn.setText(getSet.getAbsoluteReturn() + "%");
                holder.txtSSFirstHolder.setText(getSet.getFirstHolder());

                mAnimator.onSpringItemBind(holder.itemView, position);
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
            private final LinearLayout llSipStpMain;
            private final TextView txtSSFolioNo;
            private final TextView txtSSType;
            private final TextView txtSSNav;
            private final TextView txtSSGainLoss;
            private final TextView txtSSAnnualisedReturn;
            private final TextView txtSSSecondHolder;
            private final TextView txtSSDateofInvestment;
            private final TextView txtSSAmountInvested;
            private final TextView txtSSCurrentNav;
            private final TextView txtSSNoOfDays;
            private final TextView txtSSApplicant;
            private final TextView txtSSHoldingValue;
            private final TextView txtSSNature;
            private final TextView txtSSUnitsAlloted;
            private final TextView txtSSPresentValue;
            private final TextView txtSSAbsoluteReturn;
            private final TextView txtSSFirstHolder;

            ViewHolder(View convertView)
            {
                super(convertView);

                llSipStpMain = convertView.findViewById(R.id.llSipStpMain);
                txtSSFolioNo = convertView.findViewById(R.id.txtSSFolioNo);
                txtSSType = convertView.findViewById(R.id.txtSSType);
                txtSSNav = convertView.findViewById(R.id.txtSSNav);
                txtSSGainLoss = convertView.findViewById(R.id.txtSSGainLoss);
                txtSSAnnualisedReturn = convertView.findViewById(R.id.txtSSAnnualisedReturn);
                txtSSSecondHolder = convertView.findViewById(R.id.txtSSSecondHolder);
                txtSSDateofInvestment = convertView.findViewById(R.id.txtSSDateofInvestment);
                txtSSAmountInvested = convertView.findViewById(R.id.txtSSAmountInvested);
                txtSSCurrentNav = convertView.findViewById(R.id.txtSSCurrentNav);
                txtSSNoOfDays = convertView.findViewById(R.id.txtSSNoOfDays);
                txtSSApplicant = convertView.findViewById(R.id.txtSSApplicant);
                txtSSHoldingValue = convertView.findViewById(R.id.txtSSHoldingValue);
                txtSSNature = convertView.findViewById(R.id.txtSSNature);
                txtSSUnitsAlloted = convertView.findViewById(R.id.txtSSUnitsAlloted);
                txtSSPresentValue = convertView.findViewById(R.id.txtSSPresentValue);
                txtSSAbsoluteReturn = convertView.findViewById(R.id.txtSSAbsoluteReturn);
                txtSSFirstHolder = convertView.findViewById(R.id.txtSSFirstHolder);
            }
        }
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
