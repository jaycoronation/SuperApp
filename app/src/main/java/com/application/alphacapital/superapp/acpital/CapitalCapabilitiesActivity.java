package com.application.alphacapital.superapp.acpital;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mit.mitsutils.MitsUtils;
import com.application.alphacapital.superapp.R;
import com.application.alphacapital.superapp.acpital.animationadapters.AlphaInAnimationAdapter;
import com.application.alphacapital.superapp.acpital.pojo.MainPojo;
import com.application.alphacapital.superapp.acpital.service.ConnectivityReceiver;
import com.application.alphacapital.superapp.acpital.utils.AppAPIUtils;
import com.application.alphacapital.superapp.acpital.utils.AppUtils;
import com.application.alphacapital.superapp.acpital.utils.SessionManager;
import com.application.alphacapital.superapp.vault.utils.MyApplication;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class CapitalCapabilitiesActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener
{
    private Activity activity;
    private SessionManager sessionManager;

    private ImageView ivHeader;

    // loading
    private View llLoading, llRetry, llNoData;
    private ProgressBar pbLoading;
    private TextView txtLoading;

    private TextView txtDescription;
    private View viewLineBottom;
    private NestedScrollView nsvMain;
    private RecyclerView rvCapabilities;

    private ArrayList<MainPojo> listCapabilities = new ArrayList<>();
    private CapabilitiesRecyclerAdapter capabilitiesRecyclerAdapter;

    private boolean isLoading = false;
    private boolean isLoadingPending = false;
    private boolean isStatusBarHidden = false;

    private String description = "";
    // intent data
    private String menuId = "", title = "";

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

        menuId = AppUtils.getValidAPIStringResponse(getIntent().getStringExtra("menuId"));
        title = AppUtils.getValidAPIStringResponse(getIntent().getStringExtra("title"));

        setContentView(R.layout.capital_activity_capabilities);

        setupViews();

        setupToolbar();

        onClickEvents();

        if(sessionManager.isNetworkAvailable())
        {
            getCapabilitiesAsync();
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

        txtDescription = (TextView) findViewById(R.id.txtDescription);

        viewLineBottom = findViewById(R.id.viewLineBottom);

        nsvMain = (NestedScrollView) findViewById(R.id.nsvMain);

        rvCapabilities = (RecyclerView) findViewById(R.id.rvCapabilities);
        rvCapabilities.setLayoutManager(new GridLayoutManager(activity, 2));

        ViewCompat.setNestedScrollingEnabled(rvCapabilities, false);

        AppUtils.gotoContactUs(activity,findViewById(R.id.ivContactUs));
    }

    private void setupToolbar()
    {
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("");
            setSupportActionBar(toolbar);

            if(isStatusBarHidden)
            {
                toolbar.findViewById(R.id.viewStatusBar).setVisibility(View.INVISIBLE);
            }
            else
            {
                toolbar.findViewById(R.id.viewStatusBar).setVisibility(View.GONE);
            }

            toolbar.findViewById(R.id.ivLogo).setVisibility(View.GONE);
            toolbar.findViewById(R.id.llNotification).setVisibility(View.GONE);

            final TextView txtTitle = (TextView) toolbar.findViewById(R.id.txtTitle);
            txtTitle.setText(title);

            final View llBackNavigation = toolbar.findViewById(R.id.llBackNavigation);
            llBackNavigation.setVisibility(View.VISIBLE);

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
                        getCapabilitiesAsync();
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

            txtDescription.setText(description);

            capabilitiesRecyclerAdapter = new CapabilitiesRecyclerAdapter(listCapabilities);
            AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(capabilitiesRecyclerAdapter);
            rvCapabilities.setAdapter(animationAdapter);

            if(listCapabilities.size() == 0)
            {
                llNoData.setVisibility(View.VISIBLE);
                llLoading.setVisibility(View.GONE);
                txtLoading.setText(activity.getResources().getString(R.string.no_capability_data));
                pbLoading.setVisibility(View.GONE);
                llRetry.setVisibility(View.GONE);
                nsvMain.setVisibility(View.GONE);
            }
            else
            {
                llNoData.setVisibility(View.GONE);
                llLoading.setVisibility(View.GONE);
                nsvMain.setVisibility(View.VISIBLE);

                if(listCapabilities.size() % 2 == 0 && listCapabilities.size() > 1)
                {
                    viewLineBottom.setVisibility(View.VISIBLE);
                }
                else
                {
                    viewLineBottom.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCapabilitiesAsync()
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
                        description = "";
                        listCapabilities = new ArrayList<>();
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

                        response = MitsUtils.readJSONServiceUsingPOST(URL, hashMap);

                        JSONObject jsonObject = new JSONObject(response);
                        String status = AppUtils.getValidAPIStringResponse(jsonObject.getString("status"));
                        if(status.equalsIgnoreCase("success"))
                        {
                            isSuccess = true;

                            description = AppUtils.getValidAPIStringResponse(jsonObject.getString("Discription"));

                            JSONArray jsonArray = jsonObject.getJSONArray("MainTab");
                            for(int i=0; i<jsonArray.length(); i++)
                            {
                                JSONObject jsonObjectMenu = jsonArray.getJSONObject(i);
                                String menuId = AppUtils.getValidAPIStringResponse(jsonObjectMenu.getString("MenuId"));
                                String menuTitle = AppUtils.getValidAPIStringResponse(jsonObjectMenu.getString("MenuTitle"));
                                String menuIcon = AppUtils.getValidAPIStringResponse(jsonObjectMenu.getString("MenuIcon"));

                                MainPojo mainPojo = new MainPojo();
                                mainPojo.setMenuId(menuId);
                                mainPojo.setTitle(menuTitle);
                                mainPojo.setIcon(menuIcon);
                                listCapabilities.add(mainPojo);
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

    private class CapabilitiesRecyclerAdapter extends RecyclerView.Adapter<CapabilitiesRecyclerAdapter.ViewHolder>
    {
        ArrayList<MainPojo> items;
//        private SpringyAdapterAnimator mAnimator;

        CapabilitiesRecyclerAdapter(ArrayList<MainPojo> list)
        {
            this.items = list;
            /*mAnimator = new SpringyAdapterAnimator(rvCapabilities);
            mAnimator.setSpringAnimationType(SpringyAdapterAnimationType.SLIDE_FROM_BOTTOM);
            mAnimator.addConfig(85,15);*/
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.capital_rowview_capabilities, viewGroup, false);
            /*mAnimator.onSpringItemCreate(v);*/
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            final MainPojo mainPojo = items.get(position);

            String title = mainPojo.getTitle();

            Log.e("txtCapability >>> ", "onBindViewHolder: " + title );

            try {
                if(title.contains(" "))
                {
                    String[] arr = title.split(" ");
                    if(arr.length == 1)
                    {
                        title = mainPojo.getTitle();
                    }
                    else if(arr.length == 2)
                    {
                        title = arr[0] + "\n" + arr[1];
                    }
                    else
                    {
                        String str = arr[0] + " " + arr[1];
                        String strNew = arr[0] + " " + arr[1] + "\n";
                        title = title.replace(str, strNew);
                    }
                }
            } catch (Exception e) {
                title = mainPojo.getTitle();
                e.printStackTrace();
            }

            Log.e("txtCapability  >>> ", "onBindViewHolder: " + title );

            holder.txtCapability.setText(title);
            holder.txtCapability.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6.0f,  getResources().getDisplayMetrics()), 1.0f);

            /*mAnimator.onSpringItemBind(holder.itemView, position);*/

            if(position == 0 || position == 1)
            {
                holder.viewLineTopFirst.setVisibility(View.VISIBLE);
                holder.viewLineTop.setVisibility(View.GONE);
            }
            else
            {
                holder.viewLineTopFirst.setVisibility(View.GONE);
                holder.viewLineTop.setVisibility(View.VISIBLE);
            }

            try {
                if(items.size() % 2 != 0 || items.size() < 2)
                {
                    holder.viewLineBottomLast.setVisibility(View.GONE);
                }
                else
                {
                    if(position == (items.size() - 1) || position == (items.size() - 2))
                    {
                        holder.viewLineBottomLast.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        holder.viewLineBottomLast.setVisibility(View.GONE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(mainPojo.getIcon().length() > 0)
            {
                holder.ivCapability.setVisibility(View.VISIBLE);

                Glide.with(activity)
                        .load(mainPojo.getIcon())
                        .into(holder.ivCapability);
            }
            else
            {
                holder.ivCapability.setVisibility(View.INVISIBLE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {

                        Intent intent = new Intent(activity, CapitalAboutDetailsActivity.class);
                        intent.putExtra("menuId", mainPojo.getMenuId());
                        intent.putExtra("title", mainPojo.getTitle());
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.capital_activity_open_translate,R.anim.capital_activity_close_scale);

                        /*Intent intent = new Intent(activity, AboutUsActivity.class);
                        intent.putExtra("menuId", mainPojo.getMenuId());
                        intent.putExtra("title", mainPojo.getTitle());
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);*/
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
            final ImageView ivCapability;
            final TextView txtCapability;
            final View viewLineTopFirst, viewLineTop, viewLineBottomLast;

            ViewHolder(View convertView)
            {
                super(convertView);
                ivCapability = (ImageView) convertView.findViewById(R.id.ivCapability);
                txtCapability = (TextView) convertView.findViewById(R.id.txtCapability);
                viewLineTopFirst = convertView.findViewById(R.id.viewLineTopFirst);
                viewLineTop = convertView.findViewById(R.id.viewLineTop);
                viewLineBottomLast = convertView.findViewById(R.id.viewLineBottomLast);
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
                    getCapabilitiesAsync();
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
