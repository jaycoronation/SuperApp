package com.application.alphacapital.superapp.pms.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.alphacapital.superapp.R;
import com.application.alphacapital.superapp.acpital.CapitalPref;
import com.application.alphacapital.superapp.acpital.CapitalUserProfileActivity;
import com.application.alphacapital.superapp.finplan.activity.FinPlanLoginActivity;
import com.application.alphacapital.superapp.pms.beans.FolioNumberResponseModel;
import com.application.alphacapital.superapp.pms.beans.OneDayChangeResponseModel;
import com.application.alphacapital.superapp.pms.network.PortfolioApiClient;
import com.application.alphacapital.superapp.pms.network.PortfolioApiInterface;
import com.application.alphacapital.superapp.pms.utils.ApiUtils;
import com.application.alphacapital.superapp.pms.utils.AppUtils;
import com.application.alphacapital.superapp.pms.utils.SessionManager;
import com.application.alphacapital.superapp.vault.activity.VaultHomeActivity;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimationType;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OneDayChangeActivity extends NewBaseClass {
    private Activity activity;

    private SessionManager sessionManager;

    private Toolbar toolbar;

    TextView txtTitle;

    private ImageView ivBack;

    private EditText edtSearch;

    private LinearLayout llLogo, llSearch, llShare;

    private View llLoading, llNoInternet, llNoData, llBack;

    private RecyclerView rvAllUsers;

    private ArrayList<OneDayChangeResponseModel.OneDayChangeDetailItem> listOneDayChanges;
    private ArrayList<FolioNumberResponseModel.FolioNosDetailsItem> listFolioNumbers;

    private OneDayChangeAdapter oneDayChangeAdapter;
    private PortfolioApiInterface apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.portfolio_activity_day_change);

        activity = OneDayChangeActivity.this;
        sessionManager = new SessionManager(activity);
        apiService = PortfolioApiClient.getClient().create(PortfolioApiInterface.class);
        setStatusBarGradiant(activity);
        ApiUtils.END_USER_ID = sessionManager.getUserId();
        initViews();

        txtTitle = findViewById(R.id.txtTitle);

        txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        setUpViewDrawer(activity);

        if (AppUtils.isOnline()) {
            llNoInternet.setVisibility(View.GONE);
            getOneDayChangeByUser();
        }
        else {
            llNoInternet.setVisibility(View.VISIBLE);
        }

        onClicks();
    }


    void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(activity, android.R.color.transparent));
            window.setNavigationBarColor(ContextCompat.getColor(activity, android.R.color.black));
            window.setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.vault_bg_gradient));
        }
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        llLoading = findViewById(R.id.llLoading);
        llNoInternet = findViewById(R.id.llNoInternet);
        llNoData = findViewById(R.id.llNoData);
        llBack = toolbar.findViewById(R.id.llBack);
        ivBack = toolbar.findViewById(R.id.ivBack);
        edtSearch = toolbar.findViewById(R.id.edtSearch);
        llSearch = toolbar.findViewById(R.id.llSearch);
        llSearch.setVisibility(View.GONE);
        llShare = toolbar.findViewById(R.id.llShare);
        llShare.setVisibility(View.GONE);
        llLogo = toolbar.findViewById(R.id.llLogo);

        rvAllUsers = findViewById(R.id.rvAllUsers);
        rvAllUsers.setLayoutManager(new LinearLayoutManager(activity));
    }

    private void onClicks() {
        llBack.setOnClickListener(v -> {
            if (ivBack.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.portfolio_ic_cross_white).getConstantState()) {
                edtSearch.setText("");
                edtSearch.setHint("Search...");
                edtSearch.setVisibility(View.GONE);
                ivBack.setImageResource(R.drawable.portfolio_ic_menu_white);
                llSearch.setVisibility(View.VISIBLE);
                llShare.setVisibility(View.VISIBLE);
                llLogo.setVisibility(View.VISIBLE);
                AppUtils.hideKeyboard(edtSearch, activity);
            }
            else {
                openDrawer();
            }
        });

        llSearch.setOnClickListener(v -> {
            if (edtSearch.getVisibility() == View.VISIBLE) {
                edtSearch.setVisibility(View.GONE);
                ivBack.setImageResource(R.drawable.portfolio_ic_menu_white);
                llSearch.setVisibility(View.VISIBLE);
                llShare.setVisibility(View.VISIBLE);
                llLogo.setVisibility(View.VISIBLE);
                AppUtils.hideKeyboard(edtSearch, activity);
            }
            else {
                ivBack.setImageResource(R.drawable.portfolio_ic_cross_white);
                edtSearch.setVisibility(View.VISIBLE);
                llSearch.setVisibility(View.VISIBLE);
                llShare.setVisibility(View.GONE);
                llLogo.setVisibility(View.GONE);
                edtSearch.requestFocus();
                AppUtils.showKeyboard(edtSearch, activity);
            }
        });
    }

    /**
     * call on server to get the userwise OneDayChange section data
     */
    private void getOneDayChangeByUser() {

        try {
            llLoading.setVisibility(View.VISIBLE);
            listOneDayChanges = new ArrayList<>();
            apiService.getOneDayChangeAPI(ApiUtils.END_USER_ID).enqueue(new Callback<OneDayChangeResponseModel>() {
                @Override
                public void onResponse(Call<OneDayChangeResponseModel> call, Response<OneDayChangeResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess() == 1) {
                            try {
                                llLoading.setVisibility(View.GONE);
                                listOneDayChanges.addAll(response.body().getOneDayChangeDetail());
                                if (listOneDayChanges != null && listOneDayChanges.size() > 0) {
                                    llNoData.setVisibility(View.GONE);

                                    oneDayChangeAdapter = new OneDayChangeAdapter(listOneDayChanges);
                                    rvAllUsers.setAdapter(oneDayChangeAdapter);
                                }
                                else {
                                    llNoData.setVisibility(View.VISIBLE);
                                }

                                getFolioNumber();
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            AppUtils.showSnackBar(llLoading, "No Data Found", activity);
                            llNoData.setVisibility(View.VISIBLE);
                            llLoading.setVisibility(View.GONE);
                        }
                    }
                    else {
                        llNoData.setVisibility(View.VISIBLE);
                        llLoading.setVisibility(View.GONE);
                        AppUtils.showSnackBar(llLoading, "No Data Found", activity);
                    }
                }

                @Override
                public void onFailure(Call<OneDayChangeResponseModel> call, Throwable t) {
                    llNoData.setVisibility(View.VISIBLE);
                    llLoading.setVisibility(View.GONE);
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }

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

                    String serverResponse = MitsUtils.readJSONServiceUsingPOST(ApiUtils.GET_ONE_DAY_CHANGE, hashMap);

                    AppUtils.printLog("OneDayResponse",serverResponse);
                    AppUtils.printLog("hashMap",hashMap.toString());

                    JSONObject jsonObject = new JSONObject(serverResponse);

                    status = AppUtils.getValidAPIIntegerResponse(jsonObject.optString("success"));
                    ApiUtils.CID = AppUtils.getValidAPIStringResponse(jsonObject.optString("Cid"));

                    if (status == 1)
                    {
                        JSONArray userArray = jsonObject.optJSONArray("OneDayChangeDetail");

                        for (int i = 0; i < userArray.length(); i++)
                        {
                            JSONObject userObj =  userArray.getJSONObject(i);

                            OneDayChangeBean oneDayChangeBean = new OneDayChangeBean();

                            oneDayChangeBean.setFolioNo("Loading...");
                            oneDayChangeBean.setCode(AppUtils.getValidAPIStringResponse(userObj.optString("Code")));
                            oneDayChangeBean.setSchemename(AppUtils.getValidAPIStringResponse(userObj.optString("Schemename")));
                            oneDayChangeBean.setChange(AppUtils.getValidAPIStringResponse(userObj.optString("Change")));
                            oneDayChangeBean.setNAV(AppUtils.getValidAPIStringResponse(userObj.optString("NAV")));
                            oneDayChangeBean.setNAVDate(AppUtils.getValidAPIStringResponse(userObj.optString("NAVDate")));

                            listOneDayChanges.add(oneDayChangeBean);
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



            }
        }.execute();*/
    }

    private void getFolioNumber() {
        try {
            listFolioNumbers = new ArrayList<>();
            apiService.getFolioNumberAPI(ApiUtils.END_USER_ID).enqueue(new Callback<FolioNumberResponseModel>() {
                @Override
                public void onResponse(Call<FolioNumberResponseModel> call, Response<FolioNumberResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess() == 1) {
                            try {
                                try {
                                    listFolioNumbers.addAll(response.body().getFolioNosDetails());
                                    for (int i = 0; i < listFolioNumbers.size(); i++) {
                                        for (int j = 0; j < listOneDayChanges.size(); j++) {
                                            if (listFolioNumbers.get(i).getSchemename().equalsIgnoreCase(listOneDayChanges.get(j).getSchemename())) {
                                                OneDayChangeResponseModel.OneDayChangeDetailItem bean = listOneDayChanges.get(j);
                                                bean.setFolioNo(listFolioNumbers.get(i).getFolioNo());
                                                listOneDayChanges.set(j, bean);
                                            }
                                        }
                                    }
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (oneDayChangeAdapter != null && oneDayChangeAdapter.getItemCount() > 0) {
                                    oneDayChangeAdapter.notifyDataSetChanged();
                                }
                                else {
                                    oneDayChangeAdapter = new OneDayChangeAdapter(listOneDayChanges);
                                    rvAllUsers.setAdapter(oneDayChangeAdapter);
                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            AppUtils.showSnackBar(llLoading, "No Data Found", activity);
                            llNoData.setVisibility(View.VISIBLE);
                            llLoading.setVisibility(View.GONE);
                        }
                    }
                    else {
                        AppUtils.showSnackBar(llLoading, "No Data Found", activity);
                        llNoData.setVisibility(View.VISIBLE);
                        llLoading.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<FolioNumberResponseModel> call, Throwable t) {

                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }

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
                    hashMap.put("user_id", ApiUtils.END_USER_ID);

                    String serverResponse = MitsUtils.readJSONServiceUsingPOST(ApiUtils.GET_FOLIO_NUMBER, hashMap);

                    AppUtils.printLog("FolioNumber",serverResponse);

                    JSONObject jsonObject = new JSONObject(serverResponse);

                    status = AppUtils.getValidAPIIntegerResponse(jsonObject.optString("success"));

                    if (status == 1)
                    {
                        JSONArray userArray = jsonObject.optJSONArray("FolioNosDetails");

                        for (int i = 0; i < userArray.length(); i++)
                        {
                            JSONObject userObj =  userArray.getJSONObject(i);

                            FolioNumberBean folioNumberBean = new FolioNumberBean();

                            folioNumberBean.setFolioNo(AppUtils.getValidAPIStringResponse(userObj.optString("FolioNo")));
                            folioNumberBean.setSchemename(AppUtils.getValidAPIStringResponse(userObj.optString("Schemename")));

                            listFolioNumbers.add(folioNumberBean);
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


            }
        }.execute();*/
    }

    private class OneDayChangeAdapter extends RecyclerView.Adapter<OneDayChangeAdapter.ViewHolder> {
        ArrayList<OneDayChangeResponseModel.OneDayChangeDetailItem> listItems;
        private SpringyAdapterAnimator mAnimator;

        OneDayChangeAdapter(ArrayList<OneDayChangeResponseModel.OneDayChangeDetailItem> list) {
            this.listItems = list;
            mAnimator = new SpringyAdapterAnimator(rvAllUsers);
            mAnimator.setSpringAnimationType(SpringyAdapterAnimationType.SLIDE_FROM_BOTTOM);
            mAnimator.addConfig(85, 15);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.portfolio_row_view_one_day_change, viewGroup, false);
            mAnimator.onSpringItemCreate(v);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            try {
                final OneDayChangeResponseModel.OneDayChangeDetailItem getSet = listItems.get(position);

                if (position % 2 == 0) {
                    holder.llMainDayChange.setBackgroundResource(R.drawable.portfolio_cool_blue_gradient);
                }
                else {
                    holder.llMainDayChange.setBackgroundResource(R.drawable.portfolio_white_gradient);
                }

                holder.txtSchemeName.setText(AppUtils.toDisplayCase(getSet.getSchemename()));
                holder.txtFolioNo.setText(getSet.getFolioNo());
                holder.txtDayChg.setText(getSet.getChange());

                mAnimator.onSpringItemBind(holder.itemView, position);
            }
            catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return listItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView txtSchemeName, txtFolioNo, txtDayChg;
            private LinearLayout llMainDayChange;

            ViewHolder(View convertView) {
                super(convertView);
                txtSchemeName = convertView.findViewById(R.id.txtSchemeName);
                txtFolioNo = convertView.findViewById(R.id.txtFolioNo);
                txtDayChg = convertView.findViewById(R.id.txtDayChg);
                llMainDayChange = convertView.findViewById(R.id.llMainDayChange);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            try {
                finish();
                AppUtils.finishActivityAnimation(activity);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
