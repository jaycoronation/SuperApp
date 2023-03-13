package com.application.alphacapital.superapp.acpital;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mit.mitsutils.MitsUtils;
import com.application.alphacapital.superapp.R;
import com.application.alphacapital.superapp.acpital.pojo.MainPojo;
import com.application.alphacapital.superapp.acpital.service.ConnectivityReceiver;
import com.application.alphacapital.superapp.acpital.utils.AppAPIUtils;
import com.application.alphacapital.superapp.acpital.utils.AppUtils;
import com.application.alphacapital.superapp.acpital.utils.SessionManager;
import com.application.alphacapital.superapp.vault.utils.MyApplication;
import com.bumptech.glide.Glide;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimationType;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class CapitalNewsActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener
{
    private Activity activity;
    private SessionManager sessionManager;

    private ImageView ivHeader,ivIcon;

    // loading
    private View llLoading, llRetry, llNoData;
    private ProgressBar pbLoading;
    private TextView txtLoading;

    private RecyclerView rvAwards;

    private ArrayList<MainPojo> listAwards = new ArrayList<>();
    private AwardRecyclerAdapter awardRecyclerAdapter;
    private String bannerImage = "";

    private boolean isLoading = false;
    private boolean isLoadingPending = false;
    private boolean isStatusBarHidden = false;

    // intent data
    private String menuId ="", title = "",menuIcon = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            {
                Window w = getWindow();
                w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                isStatusBarHidden = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            super.onCreate(savedInstanceState);
        } catch (Exception e) {
            e.printStackTrace();
        }

        activity = this;
        sessionManager = new SessionManager(activity);

        if(getIntent()!=null)
        {
            try
            {
                menuId = AppUtils.getValidAPIStringResponse(getIntent().getStringExtra("menuId"));
                title = AppUtils.getValidAPIStringResponse(getIntent().getStringExtra("title"));
                menuIcon = AppUtils.getValidAPIStringResponse(getIntent().getStringExtra("menuIcon"));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        setContentView(R.layout.capital_activity_news);

        setupViews();

        setupToolbar();

        onClickEvents();

        if(sessionManager.isNetworkAvailable())
        {
            getAwardsAsync();
        }
        else
        {
            isLoadingPending = true;
            txtLoading.setText(activity.getResources().getString(R.string.network_failed_message));
            pbLoading.setVisibility(View.GONE);
            llRetry.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        try {
            super.onRestoreInstanceState(savedInstanceState);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setupViews()
    {
        llLoading = findViewById(R.id.llLoading);
        llRetry = findViewById(R.id.llRetry);
        llNoData = findViewById(R.id.llNoData);

        pbLoading = (ProgressBar) findViewById(R.id.pbLoading);

        txtLoading = (TextView) findViewById(R.id.txtLoading);
        txtLoading.setText(activity.getResources().getString(R.string.loading_progress_message));

        rvAwards = (RecyclerView) findViewById(R.id.rvAwards);
        rvAwards.setLayoutManager(new LinearLayoutManager(activity));

        AppUtils.gotoContactUs(activity,findViewById(R.id.ivContactUs));
    }

    private void setupToolbar()
    {
        try {
            final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            /*toolbar.setNavigationIcon(R.drawable.ic_back_nav);*/

            int height = 56;
            if(isStatusBarHidden)
            {
                height = 56 + 25;
                toolbar.findViewById(R.id.viewStatusBar).setVisibility(View.INVISIBLE);
            }
            else
            {
                toolbar.findViewById(R.id.viewStatusBar).setVisibility(View.GONE);
            }

            final TextView txtTitle = (TextView) toolbar.findViewById(R.id.txtTitle);
            txtTitle.setText(title);

            ivHeader = (ImageView) toolbar.findViewById(R.id.ivHeader);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ivHeader.getLayoutParams();
            params.height = (int) AppUtils.pxFromDp(activity, height);
            ivHeader.setLayoutParams(params);

            ivHeader.setImageResource(R.drawable.capital_bg_toolbar_main);

            ivIcon = (ImageView) toolbar.findViewById(R.id.ivIcon);
            ivIcon.setVisibility(View.VISIBLE);

            try {
                Glide.with(activity)
                        .load(menuIcon)
                        .into(ivIcon);
            } catch (Exception e) {
                e.printStackTrace();
            }

            final View llBackNavigation = toolbar.findViewById(R.id.llBackNavigation);

            llBackNavigation.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        activity.finish();
                        activity.overridePendingTransition(R.anim.capital_activity_fade_in, R.anim.capital_activity_fade_out);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            toolbar.setNavigationOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onClickEvents()
    {
        llRetry.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try {
                    if(sessionManager.isNetworkAvailable())
                    {
                        getAwardsAsync();
                    }
                    else
                    {
                        txtLoading.setText(activity.getResources().getString(R.string.network_failed_message));
                        pbLoading.setVisibility(View.GONE);
                        llRetry.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setupData()
    {
        try {
            isLoadingPending = false;

            /*if(bannerImage.length() > 0)
            {
                Glide.with(activity)
                        .load(bannerImage)
                        .crossFade(1000)
                        .into(ivHeader);
            }*/

            awardRecyclerAdapter = new AwardRecyclerAdapter(listAwards);
            rvAwards.setAdapter(awardRecyclerAdapter);

            if(listAwards.size() == 0)
            {
                llNoData.setVisibility(View.VISIBLE);
                llLoading.setVisibility(View.GONE);
                txtLoading.setText(activity.getResources().getString(R.string.no_award_data));
                pbLoading.setVisibility(View.GONE);
                llRetry.setVisibility(View.GONE);
                rvAwards.setVisibility(View.GONE);
            }
            else
            {
                llNoData.setVisibility(View.GONE);
                llLoading.setVisibility(View.GONE);
                rvAwards.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAwardsAsync()
    {
        try
        {
            new AsyncTask<Void, Void, Void>()
            {
                private boolean isSuccess = false;

                @Override
                protected void onPreExecute()
                {
                    try {
                        listAwards = new ArrayList<>();
                        isLoading = true;
                        llRetry.setVisibility(View.GONE);
                        llLoading.setVisibility(View.VISIBLE);
                        txtLoading.setText(activity.getResources().getString(R.string.loading_progress_message));
                        pbLoading.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    super.onPreExecute();
                }

                @Override
                protected Void doInBackground(Void... params)
                {
                    try
                    {
                        String response = "";
                        String URL = AppAPIUtils.GET_ALL_AWARDS;

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("ApiTokenId", AppAPIUtils.TOKEN_ID);
                        hashMap.put("MenuId", menuId);

                        Log.e("News Req ", "doInBackground: " + hashMap.toString());

                        response = MitsUtils.readJSONServiceUsingPOST(URL, hashMap);

                        Log.e("News Res ", "doInBackground: " + response);

                        JSONObject jsonObject = new JSONObject(response);
                        String status = AppUtils.getValidAPIStringResponse(jsonObject.getString("status"));
                        if(status.equalsIgnoreCase("success"))
                        {
                            isSuccess = true;

                            bannerImage = AppUtils.getValidAPIStringResponse(jsonObject.getString("BannerImage"));

                            JSONArray jsonArray = jsonObject.getJSONArray("AwardList");
                            for(int i=0; i<jsonArray.length(); i++)
                            {
                                JSONObject jsonObjectAward = jsonArray.getJSONObject(i);

                                String awardTitle = AppUtils.getValidAPIStringResponse(jsonObjectAward.getString("AwardTitle"));
                                String awardImage = AppUtils.getValidAPIStringResponse(jsonObjectAward.getString("AwardImage"));

                                MainPojo mainPojo = new MainPojo();
                                mainPojo.setTitle(awardTitle);
                                mainPojo.setIcon(awardImage);
                                listAwards.add(mainPojo);
                            }
                        }
                        else
                        {
                            isSuccess = false;
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void result)
                {
                    super.onPostExecute(result);
                    try
                    {
                        isLoading = false;

                        setupData();
                    }
                    catch (Exception e1)
                    {
                        e1.printStackTrace();
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void)null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private class AwardRecyclerAdapter extends RecyclerView.Adapter<AwardRecyclerAdapter.ViewHolder>
    {
        ArrayList<MainPojo> items;
        private SpringyAdapterAnimator mAnimator;

        AwardRecyclerAdapter(ArrayList<MainPojo> list)
        {
            this.items = list;
            mAnimator = new SpringyAdapterAnimator(rvAwards);
            mAnimator.setSpringAnimationType(SpringyAdapterAnimationType.SLIDE_FROM_BOTTOM);
            mAnimator.addConfig(85,15);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.capital_rowview_news, viewGroup, false);
            mAnimator.onSpringItemCreate(v);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            final MainPojo mainPojo = items.get(position);

            holder.txtAward.setText(mainPojo.getTitle());
            holder.txtAward.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8.0f,  getResources().getDisplayMetrics()), 1.0f);

            mAnimator.onSpringItemBind(holder.itemView, position);

            if(mainPojo.getIcon().length() > 0)
            {
                holder.ivAward.setVisibility(View.VISIBLE);

                Glide.with(activity)
                        .load(mainPojo.getIcon())
                        .into(holder.ivAward);
            }
            else
            {
                holder.ivAward.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            final ImageView ivAward;
            final TextView txtAward;

            ViewHolder(View convertView)
            {
                super(convertView);
                ivAward = (ImageView) convertView.findViewById(R.id.ivAward);
                txtAward = (TextView) convertView.findViewById(R.id.txtAward);
            }
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Objects.requireNonNull(MyApplication.Companion.getInstance()).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected)
    {
        try {
            if(isConnected)
            {
                if(isLoadingPending && !isLoading)
                {
                    getAwardsAsync();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            try
            {
                activity.finish();
                activity.overridePendingTransition(R.anim.capital_activity_fade_in, R.anim.capital_activity_fade_out);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
