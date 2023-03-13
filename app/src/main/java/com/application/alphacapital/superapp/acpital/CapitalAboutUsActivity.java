package com.application.alphacapital.superapp.acpital;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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

public class CapitalAboutUsActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener
{
    private Activity activity;
    private SessionManager sessionManager;

    private ImageView ivHeader,ivIcon;

    // loading
    private View llLoading, llRetry, llNoData;
    private ProgressBar pbLoading;
    private TextView txtLoading;

    private String content = "", bannerImage = "";

    private boolean isLoading = false;
    private boolean isLoadingPending = false;
    private boolean isStatusBarHidden = false;

    // intent data
    private String menuId = "", title = "",menuIcon = "";

    private ArrayList<MainPojo> listSubMenu = new ArrayList<>();
    private RecyclerView rvAbout;
    private ArrayList<MainPojo> listAbout = new ArrayList<>();

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

        setContentView(R.layout.capital_activity_aboutus);

        setupViews();

        setupToolbar();

        onClickEvents();
        
        //setCustomData();

        if(sessionManager.isNetworkAvailable())
        {
            //getDetailsAsync();
            getSubMenuAsync();
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
        
        rvAbout = (RecyclerView) findViewById(R.id.rvAbout);
        rvAbout.setLayoutManager(new LinearLayoutManager(activity));

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
                        //getDetailsAsync();
                        getSubMenuAsync();
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
        try
        {
            isLoadingPending = false;

            if(bannerImage.length() > 0)
            {
                Glide.with(activity)
                    .load(bannerImage)
                    .into(ivHeader);
            }

            Log.e("listSubMenu Size ", "setupData: " + listSubMenu.size() );
            llLoading.setVisibility(View.GONE);
            if(listSubMenu.size()>0)
            {
                llNoData.setVisibility(View.GONE);
                AboutAdapter aboutAdapter = new AboutAdapter(listSubMenu);
                rvAbout.setAdapter(aboutAdapter);
            }
            else
            {
                llNoData.setVisibility(View.VISIBLE);
                txtLoading.setText(activity.getResources().getString(R.string.no_data));
                pbLoading.setVisibility(View.GONE);
                llRetry.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getSubMenuAsync()
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
                        listSubMenu = new ArrayList<>();

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
                        String URL = AppAPIUtils.GET_MENU_TAB_BY_ID;

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("ApiTokenId", AppAPIUtils.TOKEN_ID);
                        hashMap.put("Id", menuId);

                        Log.e("Menu Request ", "doInBackground: " + hashMap.toString() );

                        response = MitsUtils.readJSONServiceUsingPOST(URL, hashMap);

                        Log.e("Menu Response  ", "doInBackground: " + response );

                        JSONObject jsonObject = new JSONObject(response);
                        String status = AppUtils.getValidAPIStringResponse(jsonObject.getString("status"));
                        if(status.equalsIgnoreCase("success"))
                        {
                            isSuccess = true;

                            JSONArray jsonArray = jsonObject.getJSONArray("MainTab");
                            for(int i=0; i<jsonArray.length(); i++)
                            {
                                JSONObject jsonObjectMenu = jsonArray.getJSONObject(i);
                                String menuId = AppUtils.getValidAPIStringResponse(jsonObjectMenu.getString("MenuId"));
                                String menuTitle = AppUtils.getValidAPIStringResponse(jsonObjectMenu.getString("MenuTitle"));
                                String menuIcon = AppUtils.getValidAPIStringResponse(jsonObjectMenu.getString("MenuIcon"));
                                String menuDescription = AppUtils.getValidAPIStringResponse(jsonObjectMenu.getString("MenuDescription"));

                                MainPojo mainPojo = new MainPojo();
                                mainPojo.setMenuId(menuId);
                                mainPojo.setTitle(menuTitle);
                                mainPojo.setIcon(menuIcon);
                                mainPojo.setDescription(menuDescription);
                                listSubMenu.add(mainPojo);
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

    private class AboutAdapter extends RecyclerView.Adapter<AboutAdapter.ViewHolder>
    {
        ArrayList<MainPojo> items;
        private SpringyAdapterAnimator mAnimator;

        AboutAdapter(ArrayList<MainPojo> list)
        {
            this.items = list;
            mAnimator = new SpringyAdapterAnimator(rvAbout);
            mAnimator.setSpringAnimationType(SpringyAdapterAnimationType.SLIDE_FROM_BOTTOM);
            mAnimator.addConfig(85,15);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.capital_rowview_about_us, viewGroup, false);
            mAnimator.onSpringItemCreate(v);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            final MainPojo pojo = items.get(position);

            if(position == (items.size() - 1))
            {
                holder.viewLineBottom.setVisibility(View.GONE);
            }
            else
            {
                holder.viewLineBottom.setVisibility(View.VISIBLE);
            }
            
            holder.tvTitle.setText(pojo.getTitle());

            mAnimator.onSpringItemBind(holder.itemView, position);

            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {

                        if(pojo.getTitle().equalsIgnoreCase("management team"))
                        {
                            Intent intent = new Intent(activity, CapitalManagementTeamActivity.class);
                            intent.putExtra("menuId", pojo.getMenuId());
                            intent.putExtra("title", pojo.getTitle());
                            activity.startActivity(intent);
                            activity.overridePendingTransition(R.anim.capital_activity_open_translate,R.anim.capital_activity_close_scale);
                        }
                        else if(pojo.getTitle().equalsIgnoreCase("faq"))
                        {
                            Intent intent = new Intent(activity, CapitalFAQActivity.class);
                            activity.startActivity(intent);
                            activity.overridePendingTransition(R.anim.capital_activity_open_translate,R.anim.capital_activity_close_scale);
                        }
                        else if(pojo.getTitle().equalsIgnoreCase("our capabilities") || pojo.getMenuId().equalsIgnoreCase("38"))
                        {
                            Intent intent = new Intent(activity, CapitalCapabilitiesActivity.class);
                            intent.putExtra("menuId", pojo.getMenuId());
                            intent.putExtra("title", pojo.getTitle());
                            activity.startActivity(intent);
                            activity.overridePendingTransition(R.anim.capital_activity_open_translate,R.anim.capital_activity_close_scale);
                        }
                        else if(title.equalsIgnoreCase("insights"))
                        {
                            Intent intent = null;
                            intent = new Intent(activity, CapitalResearchActivity.class);
                            intent.putExtra("menuId", pojo.getMenuId());
                            intent.putExtra("title", pojo.getTitle());
                            activity.startActivity(intent);
                            activity.overridePendingTransition(R.anim.capital_activity_open_translate,R.anim.capital_activity_close_scale);
                        }
                        else
                        {
                            Intent intent = new Intent(activity, CapitalAboutDetailsActivity.class);
                            intent.putExtra("menuId", pojo.getMenuId());
                            intent.putExtra("title", pojo.getTitle());
                            activity.startActivity(intent);
                            activity.overridePendingTransition(R.anim.capital_activity_open_translate,R.anim.capital_activity_close_scale);
                        }
                    } 
                    catch (Exception e) 
                    {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            final TextView tvTitle;
            final View viewLineBottom;

            ViewHolder(View convertView)
            {
                super(convertView);
                tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
                viewLineBottom = convertView.findViewById(R.id.viewLineBottom);
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
        try
        {
            if(isConnected)
            {
                if(isLoadingPending && !isLoading)
                {
                    //getDetailsAsync();
                    getSubMenuAsync();
                }
            }
        }
        catch (Exception e)
        {
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
