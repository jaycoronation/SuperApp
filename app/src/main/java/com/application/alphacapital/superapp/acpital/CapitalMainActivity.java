package com.application.alphacapital.superapp.acpital;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.alphacapital.superapp.supermain.activity.MainActivity;
import com.application.alphacapital.superapp.R;
import com.application.alphacapital.superapp.acpital.model.MenuTabResponseModel;
import com.application.alphacapital.superapp.acpital.network.CapitalApiClient;
import com.application.alphacapital.superapp.acpital.network.CapitalApiInterface;
import com.application.alphacapital.superapp.acpital.pojo.MainPojo;
import com.application.alphacapital.superapp.acpital.service.ConnectivityReceiver;
import com.application.alphacapital.superapp.acpital.utils.AppAPIUtils;
import com.application.alphacapital.superapp.acpital.utils.AppUtils;
import com.application.alphacapital.superapp.acpital.utils.SessionManager;
import com.application.alphacapital.superapp.vault.utils.MyApplication;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimationType;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimator;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CapitalMainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private Activity activity;
    private SessionManager sessionManager;
    CapitalApiInterface apiService;
    private ImageView ivHeader, ivUser, ivLogout;

    // loading
    private View llLoading, llRetry, llNoData;
    private ProgressBar pbLoading;
    private TextView txtLoading;
    private BottomSheetDialog bottomSheetDialog;
    private RecyclerView rvMain;
    public static Handler handler;
    private ArrayList<MainPojo> listMainMenu = new ArrayList<>();

    private boolean isLoading = false;
    private boolean isLoadingPending = false;
    private boolean isStatusBarHidden = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            isStatusBarHidden = true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        try {
            super.onCreate(savedInstanceState);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        activity = this;

        sessionManager = new SessionManager(activity);
        apiService = Objects.requireNonNull(CapitalApiClient.INSTANCE.getClient()).create(CapitalApiInterface.class);
        setContentView(R.layout.capital_activity_main);

        setupViews();

        setupToolbar();

        onClickEvents();

        if (sessionManager.isNetworkAvailable()) {
            getMenuAsync();
        }
        else {
            isLoadingPending = true;
            txtLoading.setText(activity.getResources().getString(R.string.network_failed_message));
            pbLoading.setVisibility(View.GONE);
            llRetry.setVisibility(View.VISIBLE);
        }

        handler = new Handler(msg -> {
            try {
                //Reload All Feeds
                if (msg.what == 100) {
                    if (sessionManager.isLoggedIn()) {
                        ivUser.setVisibility(View.GONE);
                        ivLogout.setVisibility(View.VISIBLE);

                        if (sessionManager.isNetworkAvailable()) {
                            getMenuAsync();
                        }
                        else {
                            isLoadingPending = true;
                            txtLoading.setText(activity.getResources().getString(R.string.network_failed_message));
                            pbLoading.setVisibility(View.GONE);
                            llRetry.setVisibility(View.VISIBLE);
                        }
                    }
                    else {
                        ivUser.setVisibility(View.VISIBLE);
                        ivLogout.setVisibility(View.GONE);
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        });
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        try {
            super.onRestoreInstanceState(savedInstanceState);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupViews() {
        ivUser = (ImageView) findViewById(R.id.ivUser);
        ivLogout = (ImageView) findViewById(R.id.ivLogout);
        llLoading = findViewById(R.id.llLoading);
        llRetry = findViewById(R.id.llRetry);
        llNoData = findViewById(R.id.llNoData);
        pbLoading = (ProgressBar) findViewById(R.id.pbLoading);
        txtLoading = (TextView) findViewById(R.id.txtLoading);
        txtLoading.setText(activity.getResources().getString(R.string.loading_progress_message));
        rvMain = (RecyclerView) findViewById(R.id.rvMain);
        //rvMain.setLayoutManager(new GridLayoutManager(activity, 2));
        rvMain.setLayoutManager(new LinearLayoutManager(activity));
        if (sessionManager.isLoggedIn()) {
            ivUser.setVisibility(View.GONE);
            ivLogout.setVisibility(View.VISIBLE);
        }
        else {
            ivUser.setVisibility(View.VISIBLE);
            ivLogout.setVisibility(View.GONE);
        }
    }

    private void setupToolbar() {
        try {
            final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            /*toolbar.setNavigationIcon(R.drawable.ic_back_nav);*/


            int height = 56;
            if (isStatusBarHidden)
            {
                height = 56 + 25;
                toolbar.findViewById(R.id.viewStatusBar).setVisibility(View.INVISIBLE);
            }
            else
                {
                toolbar.findViewById(R.id.viewStatusBar).setVisibility(View.GONE);
            }

            ivHeader = (ImageView) toolbar.findViewById(R.id.ivHeader);

            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ivHeader.getLayoutParams();

            params.height = (int) AppUtils.pxFromDp(activity, height);

            ivHeader.setLayoutParams(params);

            ivHeader.setImageResource(R.drawable.capital_bg_toolbar_main);

            final View llBackNavigation = toolbar.findViewById(R.id.llBackNavigation);

            /*llBackNavigation.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        activity.finish();
                        activity.overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            */

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onClickEvents() {
        llRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (sessionManager.isNetworkAvailable()) {
                        getMenuAsync();
                    }
                    else {
                        txtLoading.setText(activity.getResources().getString(R.string.network_failed_message));
                        pbLoading.setVisibility(View.GONE);
                        llRetry.setVisibility(View.VISIBLE);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sessionManager.isLoggedIn()) {
                    AppUtils.showLogoutDialog(activity, sessionManager);
                }

            }
        });

        ivUser.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(activity, CapitalPortfolioActivity.class);
                startActivity(intent);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void setupData() {
        try {
            isLoadingPending = false;

            /*menuRecyclerAdapter = new MenuRecyclerAdapter(listMainMenu);
            AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(menuRecyclerAdapter);
            rvMain.setAdapter(alphaInAnimationAdapter);*/

            if (listMainMenu.size() > 0) {
                for (int i = 0; i < listMainMenu.size(); i++) {
                    final MainPojo mainPojo = listMainMenu.get(i);

                    if (mainPojo.getTitle().equalsIgnoreCase("Contact Us") || mainPojo.getTitle().equalsIgnoreCase("Locate Us")) {
                        sessionManager.setContactMenuId(mainPojo.getMenuId());
                        sessionManager.setContactMenuImage(mainPojo.getIcon());
                        sessionManager.setContactMenuTitle(mainPojo.getTitle());
                    }
                }
            }

            NewMenuAdapter adapter = new NewMenuAdapter(listMainMenu);
            rvMain.setAdapter(adapter);

            if (listMainMenu.size() == 0) {
                llNoData.setVisibility(View.VISIBLE);
                llLoading.setVisibility(View.GONE);
                txtLoading.setText(activity.getResources().getString(R.string.no_data));
                pbLoading.setVisibility(View.GONE);
                llRetry.setVisibility(View.GONE);
                rvMain.setVisibility(View.GONE);
            }
            else {
                llNoData.setVisibility(View.GONE);
                llLoading.setVisibility(View.GONE);
                rvMain.setVisibility(View.VISIBLE);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean isSuccess = false;

    private void getMenuAsync()
    {
        try
        {
            listMainMenu = new ArrayList<>();

            isLoading = true;
            llRetry.setVisibility(View.GONE);
            llLoading.setVisibility(View.VISIBLE);
            txtLoading.setText(activity.getResources().getString(R.string.loading_progress_message));
            pbLoading.setVisibility(View.VISIBLE);
            apiService.getMenuTabs(AppAPIUtils.TOKEN_ID,"0").enqueue(new Callback<MenuTabResponseModel>() {
                @Override
                public void onResponse(Call<MenuTabResponseModel> call, Response<MenuTabResponseModel> response) {
                    if (response.isSuccessful())
                    {
                        if (response.body().getStatus().equals("success"))
                        {
                            isSuccess = true;
                            for (int i = 0; i < response.body().getMainTab().size(); i++) {
                                MainPojo mainPojo = new MainPojo();
                                mainPojo.setMenuId(String.valueOf(response.body().getMainTab().get(i).getMenuId()));
                                mainPojo.setTitle(response.body().getMainTab().get(i).getMenuTitle());
                                mainPojo.setIcon(response.body().getMainTab().get(i).getMenuIcon());
                                mainPojo.setDescription(response.body().getMainTab().get(i).getMenuDescription());
                                listMainMenu.add(mainPojo);
                            }

                            try {
                                isLoading = false;

                                MainPojo mainPojo = new MainPojo();
                                mainPojo.setMenuId("00");
                                mainPojo.setTitle("Alpha Vault");
                                mainPojo.setIcon("");
                                mainPojo.setDescription("Smooth Succession Planning");
                                listMainMenu.add(mainPojo);

                                setupData();
                            }
                            catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                        else
                        {
                            isSuccess = false;
                        }
                    }
                    else
                    {
                        isSuccess = false;
                    }
                }

                @Override
                public void onFailure(Call<MenuTabResponseModel> call, Throwable t) {
                    AppUtils.apiFailedToast(activity);
                    Log.e("<><> FAILURE", t.getMessage());
                }
            });

            /*new AsyncTask<Void, Void, Void>() {

                @Override
                protected void onPreExecute() {
                    try {
                        listMainMenu = new ArrayList<>();

                        isLoading = true;
                        llRetry.setVisibility(View.GONE);
                        llLoading.setVisibility(View.VISIBLE);
                        txtLoading.setText(activity.getResources().getString(R.string.loading_progress_message));
                        pbLoading.setVisibility(View.VISIBLE);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    super.onPreExecute();
                }

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        String response = "";

                        String URL = AppAPIUtils.GET_MENU_TAB_BY_ID;

                        HashMap<String, String> hashMap = new HashMap<>();

                        hashMap.put("ApiTokenId", AppAPIUtils.TOKEN_ID);

                        hashMap.put("Id", "0");

                        Log.e("Menu URL ", "doInBackground: " + URL.toString());
                        Log.e("Menu Request ", "doInBackground: " + hashMap.toString());

                        response = MitsUtils.readJSONServiceUsingPOST(URL, hashMap);

                        Log.e("Menu Response ", "doInBackground: " + response);

                        JSONObject jsonObject = new JSONObject(response);

                        String status = AppUtils.getValidAPIStringResponse(jsonObject.getString("status"));

                        if (status.equalsIgnoreCase("success")) {
                            isSuccess = true;

                            JSONArray jsonArray = jsonObject.getJSONArray("MainTab");

                            for (int i = 0; i < jsonArray.length(); i++) {
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
                                listMainMenu.add(mainPojo);
                            }
                        }
                        else {
                            isSuccess = false;
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    super.onPostExecute(result);
                    try {
                        isLoading = false;

                        MainPojo mainPojo = new MainPojo();
                        mainPojo.setMenuId("00");
                        mainPojo.setTitle("Alpha Vault");
                        mainPojo.setIcon("");
                        mainPojo.setDescription("Smooth Succession Planning");
                        listMainMenu.add(mainPojo);

                        setupData();
                    }
                    catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }

            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);*/
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private class NewMenuAdapter extends RecyclerView.Adapter<NewMenuAdapter.ViewHolder> {
        ArrayList<MainPojo> items;
        private SpringyAdapterAnimator mAnimator;

        NewMenuAdapter(ArrayList<MainPojo> list) {
            this.items = list;
            mAnimator = new SpringyAdapterAnimator(rvMain);
            mAnimator.setSpringAnimationType(SpringyAdapterAnimationType.SLIDE_FROM_BOTTOM);
            mAnimator.addConfig(85, 15);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.capital_rowview_menu, viewGroup, false);
            mAnimator.onSpringItemCreate(v);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            final MainPojo mainPojo = items.get(position);

            /*if(mainPojo.getMenuId().equals("6"))
            {
                sessionManager.setContactMenuId("6");
                sessionManager.setContactMenuImage(mainPojo.getIcon());
                sessionManager.setContactMenuTitle(mainPojo.getTitle());
            }*/


            if (position == (items.size() - 1)) {
                holder.viewLineBottom.setVisibility(View.GONE);
                holder.ivIndicator.setVisibility(View.GONE);
            }
            else {
                holder.viewLineBottom.setVisibility(View.VISIBLE);
                holder.ivIndicator.setVisibility(View.VISIBLE);
            }

            holder.tvTitle.setText(mainPojo.getTitle());

            holder.txtDescription.setTypeface(AppUtils.getTypefaceMedium(activity));

            holder.txtDescription.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5.0f, getResources().getDisplayMetrics()), 1.0f);

            if (!mainPojo.getDescription().equals("")) {
                holder.txtDescription.setVisibility(View.VISIBLE);
                holder.txtDescription.setHtml(mainPojo.getDescription());
            }
            else {
                holder.txtDescription.setVisibility(View.GONE);
            }

            if (mainPojo.getIcon().length() > 0) {
                holder.ivMenu.setVisibility(View.VISIBLE);
                Glide.with(activity).load(mainPojo.getIcon()).into(holder.ivMenu);
            }
            else {
                holder.ivMenu.setVisibility(View.VISIBLE);
            }

            mAnimator.onSpringItemBind(holder.itemView, position);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    redirect(mainPojo, position);
                }
            });
            holder.txtDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    redirect(mainPojo, position);
                }
            });
        }

        private void redirect(final MainPojo mainPojo, final int position) {
            try {
                Intent intent = null;
                if (mainPojo.getMenuId().equalsIgnoreCase("1"))//about
                {
                    intent = new Intent(activity, CapitalAboutUsActivity.class);
                }
                else if (mainPojo.getMenuId().equalsIgnoreCase("3"))//award
                {
                    intent = new Intent(activity, CapitalAwardsActivity.class);
                }
                else if (mainPojo.getMenuId().equalsIgnoreCase("22"))//news
                {
                    intent = new Intent(activity, CapitalNewsActivity.class);
                }
                else if (mainPojo.getMenuId().equalsIgnoreCase("2"))//capabilities
                {
                    //intent = new Intent(activity, CapabilitiesActivity.class);
                    intent = new Intent(activity, CapitalAboutUsActivity.class);
                }
                else if (mainPojo.getMenuId().equalsIgnoreCase("5"))//insights
                {
                    intent = new Intent(activity, CapitalAboutUsActivity.class);
                    //intent = new Intent(activity, ResearchActivity.class);
                }
                else if (mainPojo.getMenuId().equalsIgnoreCase("6"))//contact us
                {
                    intent = new Intent(activity, CapitalLocateUsActivity.class);
                }
                else if (mainPojo.getMenuId().equalsIgnoreCase("4"))//portfolio
                {
                    intent = new Intent(activity, CapitalPortfolioActivity.class);
                }
                else if (mainPojo.getMenuId().equalsIgnoreCase("20"))//multi family office
                {
                    intent = new Intent(activity, CapitalAboutUsActivity.class);
                }
                else if (mainPojo.getMenuId().equalsIgnoreCase("00"))//multi family office
                {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.alphaestatevault"));
                    startActivity(i);
                }
                else {
                    intent = null;
                }

                if (intent != null) {
                    intent.putExtra("menuId", mainPojo.getMenuId());
                    intent.putExtra("title", mainPojo.getTitle());
                    intent.putExtra("menuIcon", mainPojo.getIcon());
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.capital_activity_open_translate, R.anim.capital_activity_close_scale);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            final TextView tvTitle;
            final HtmlTextView txtDescription;
            final ImageView ivMenu, ivIndicator;
            final View viewLineBottom;

            ViewHolder(View convertView) {
                super(convertView);
                tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
                txtDescription = (HtmlTextView) convertView.findViewById(R.id.txtDescription);
                ivMenu = (ImageView) convertView.findViewById(R.id.ivMenu);
                ivIndicator = (ImageView) convertView.findViewById(R.id.ivIndicator);
                viewLineBottom = convertView.findViewById(R.id.viewLineBottom);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Objects.requireNonNull(MyApplication.Companion.getInstance()).setConnectivityListener(this);

        try {
            if (CapitalSplashActivity.isVersionMismatch && !CapitalSplashActivity.isForcefulUpdateDialogOpen) {
                CapitalSplashActivity.isForcefulUpdateDialogOpen = true;
                AppUtils.showVersionMismatchDialog(activity);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        try {
            if (isConnected) {
                if (isLoadingPending && !isLoading) {
                    getMenuAsync();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            try {
                if (doubleBackToExitPressedOnce) {
                    activity.finishAffinity();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    activity.overridePendingTransition(R.anim.capital_activity_fade_in, R.anim.capital_activity_fade_out);
                }
                else {
                    doubleBackToExitPressedOnce = true;

                    Toast.makeText(this, "Press back again to exit the app", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            doubleBackToExitPressedOnce = false;
                        }
                    }, 2000);

                    return false;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        return super.onKeyDown(keyCode, event);
    }
}
