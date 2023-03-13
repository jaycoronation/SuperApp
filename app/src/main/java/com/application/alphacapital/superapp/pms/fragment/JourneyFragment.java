package com.application.alphacapital.superapp.pms.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.application.alphacapital.superapp.R;
import com.application.alphacapital.superapp.pms.activity.NetworthActivity;
import com.application.alphacapital.superapp.pms.beans.DividendSummaryResponseModel;
import com.application.alphacapital.superapp.pms.network.PortfolioApiClient;
import com.application.alphacapital.superapp.pms.network.PortfolioApiInterface;
import com.application.alphacapital.superapp.pms.utils.ApiUtils;
import com.application.alphacapital.superapp.pms.utils.AppUtils;
import com.application.alphacapital.superapp.pms.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class JourneyFragment extends Fragment
{
    private Activity activity ;

    private SessionManager sessionManager ;

    private View llLoading,llNoInternet,llNoData;

    private TextView txtPurchase,txtSwitchIn,txtSwitchOut,txtSell,txtDividend,txtNetInvestment,txtPresentAmount,txtTotalGain,txtXIRR;

    private PortfolioApiInterface apiService;

    public JourneyFragment()
    {
        //Required empty public constructor
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
        View rootView = inflater.inflate(R.layout.portfolio_fragment_journey, container, false);

        activity = getActivity();

        sessionManager = new SessionManager(activity);

        apiService = PortfolioApiClient.getClient().create(PortfolioApiInterface.class);

        initViews(rootView);

        setData();

        return rootView ;
    }

    private void initViews(View rootView)
    {
        llLoading = rootView.findViewById(R.id.llLoading);
        llNoInternet = rootView.findViewById(R.id.llNoInternet);
        llNoData = rootView.findViewById(R.id.llNoData);

        txtPurchase = rootView.findViewById(R.id.txtPurchase);
        txtSwitchIn = rootView.findViewById(R.id.txtSwitchIn);
        txtSwitchOut = rootView.findViewById(R.id.txtSwitchOut);
        txtSell = rootView.findViewById(R.id.txtSell);
        txtDividend = rootView.findViewById(R.id.txtDividend);
        txtNetInvestment = rootView.findViewById(R.id.txtNetInvestment);
        txtPresentAmount = rootView.findViewById(R.id.txtPresentAmount);
        txtTotalGain = rootView.findViewById(R.id.txtTotalGain);
        txtPurchase = rootView.findViewById(R.id.txtPurchase);
        txtXIRR = rootView.findViewById(R.id.txtXIRR);
    }

    private void setData()
    {
        txtPurchase.setText(AppUtils.convertToTwoDecimalValue(NetworthActivity.purchase));
        txtSwitchIn.setText(AppUtils.convertToTwoDecimalValue(NetworthActivity.switchIn));
        txtSwitchOut.setText(AppUtils.convertToTwoDecimalValue(NetworthActivity.switchOut));
        txtSell.setText(AppUtils.convertToTwoDecimalValue(NetworthActivity.sell));
        txtDividend.setText("Loading...");
        txtNetInvestment.setText(AppUtils.convertToTwoDecimalValue(NetworthActivity.netInvestment));
        txtPresentAmount.setText(AppUtils.convertToTwoDecimalValue(NetworthActivity.currentVal));
        txtTotalGain.setText(AppUtils.convertToTwoDecimalValue(NetworthActivity.totalGain));
        txtXIRR.setText(AppUtils.convertToTwoDecimalValue(NetworthActivity.XIRR));

        getDividendSummary();
    }

    private void getDividendSummary()
    {

        llLoading.setVisibility(View.VISIBLE);
        apiService.getDividendAPI(ApiUtils.END_USER_ID).enqueue(new Callback<DividendSummaryResponseModel>() {
            @Override
            public void onResponse(Call<DividendSummaryResponseModel> call, Response<DividendSummaryResponseModel> response) {
                if (response.isSuccessful())
                {
                    if (response.body().getSuccess() == 1)
                    {
                        txtDividend.setText(String.valueOf(response.body().getDividend_grand_total()));
                        llLoading.setVisibility(View.GONE);
                    }
                    else
                    {
                        llLoading.setVisibility(View.GONE);
                    }
                }
                else
                {
                    llLoading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<DividendSummaryResponseModel> call, Throwable t) {
                AppUtils.printLog("FAIL",t.getMessage());
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

                    String serverResponse = MitsUtils.readJSONServiceUsingPOST(ApiUtils.GET_DIVIDEND_SUMMARY, hashMap);

                    AppUtils.printLog("ServerResponseNetworth",serverResponse);

                    JSONObject jsonObject = new JSONObject(serverResponse);

                    status = AppUtils.getValidAPIIntegerResponse(jsonObject.optString("success"));

                    if (status == 1)
                    {
                        divAmt = jsonObject.optString("dividend_grand_total");
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

            }
        }.execute();*/
    }
}
