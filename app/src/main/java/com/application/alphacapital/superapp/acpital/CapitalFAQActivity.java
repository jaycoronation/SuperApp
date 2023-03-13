package com.application.alphacapital.superapp.acpital;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.mit.mitsutils.MitsUtils;
import com.application.alphacapital.superapp.R;
import com.application.alphacapital.superapp.acpital.pojo.FAQPojo;
import com.application.alphacapital.superapp.acpital.service.ConnectivityReceiver;
import com.application.alphacapital.superapp.acpital.utils.AppAPIUtils;
import com.application.alphacapital.superapp.acpital.utils.AppUtils;
import com.application.alphacapital.superapp.acpital.utils.SessionManager;
import com.application.alphacapital.superapp.vault.utils.MyApplication;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class CapitalFAQActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private Activity activity;
    private SessionManager sessionManager;

    private ImageView ivHeader;

    // loading
    private View llLoading, llRetry, llNoData;
    private ProgressBar pbLoading;
    private TextView txtLoading;

    private boolean isLoading = false;
    private boolean isLoadingPending = false;
    private boolean isStatusBarHidden = false;

    String bannerImage = "";

    // intent data
    private String menuId = "";
    private final String title = "FAQ - Multi family office";

    private ExpandableListView expFaq;
    private ArrayList<FAQPojo> listFaq = new ArrayList<>();

    int lastExpandedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Window w = getWindow();
                w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                isStatusBarHidden = true;
            }
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

        menuId = AppUtils.getValidAPIStringResponse(getIntent().getStringExtra("menuId"));
        //title = AppUtils.getValidAPIStringResponse(getIntent().getStringExtra("title"));

        setContentView(R.layout.capital_activity_faq);

        setupViews();

        setupToolbar();

        onClickEvents();

        //setCustomData();

        if (sessionManager.isNetworkAvailable()) {
            getDetailsAsync();
        }
        else {
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
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupViews() {
        llLoading = findViewById(R.id.llLoading);
        llRetry = findViewById(R.id.llRetry);
        llNoData = findViewById(R.id.llNoData);

        pbLoading = findViewById(R.id.pbLoading);

        txtLoading = findViewById(R.id.txtLoading);
        txtLoading.setText(activity.getResources().getString(R.string.loading_progress_message));

        expFaq = findViewById(R.id.expFaq);

        AppUtils.gotoContactUs(activity, findViewById(R.id.ivContactUs));
    }

    private void setupToolbar() {
        try {
            final Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            /*toolbar.setNavigationIcon(R.drawable.ic_back_nav);*/

            int height = 56;
            if (isStatusBarHidden) {
                height = 56 + 25;
                toolbar.findViewById(R.id.viewStatusBar).setVisibility(View.INVISIBLE);
            }
            else {
                toolbar.findViewById(R.id.viewStatusBar).setVisibility(View.GONE);
            }

            final TextView txtTitle = toolbar.findViewById(R.id.txtTitle);
            txtTitle.setText(title);

            ivHeader = toolbar.findViewById(R.id.ivHeader);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ivHeader.getLayoutParams();
            params.height = (int) AppUtils.pxFromDp(activity, height);
            ivHeader.setLayoutParams(params);

            ivHeader.setImageResource(R.drawable.capital_bg_toolbar_main);

            final View llBackNavigation = toolbar.findViewById(R.id.llBackNavigation);

            llBackNavigation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        activity.finish();
                        activity.overridePendingTransition(R.anim.capital_activity_fade_in, R.anim.capital_activity_fade_out);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
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
                        getDetailsAsync();
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


        expFaq.setOnGroupExpandListener(groupPosition -> {
            if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition) {
                expFaq.collapseGroup(lastExpandedPosition);
            }
            lastExpandedPosition = groupPosition;
        });
    }

    private void setupData() {
        try {
            isLoadingPending = false;

            llLoading.setVisibility(View.GONE);

            if (bannerImage.length() > 0) {
                Glide.with(activity).load(bannerImage).into(ivHeader);
            }

            if (listFaq.size() > 0) {
                llNoData.setVisibility(View.GONE);

                MyExpandableListAdapter adapter = new MyExpandableListAdapter(activity, listFaq);
                expFaq.setAdapter(adapter);
            }
            else {
                llNoData.setVisibility(View.VISIBLE);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    class MyExpandableListAdapter extends BaseExpandableListAdapter {

        private final Activity context;
        private final ArrayList<FAQPojo> items;

        public MyExpandableListAdapter(Activity context, ArrayList<FAQPojo> listFaq) {
            this.context = context;
            this.items = listFaq;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return items.get(groupPosition).getDescription();
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            final FAQPojo details = items.get(groupPosition);

            if (convertView == null) {
                //LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.capital_rowview_child_exp_faq, null);
            }

            HtmlTextView tvDescription = convertView.findViewById(R.id.tvDescription);
            tvDescription.setTypeface(AppUtils.getTypefaceMedium(activity));
            tvDescription.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8.0f, getResources().getDisplayMetrics()), 1.0f);
            tvDescription.setHtml(details.getDescription());

            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return items.get(groupPosition).getTitle();
        }

        @Override
        public int getGroupCount() {
            return items.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
            final FAQPojo details = items.get(groupPosition);
            if (convertView == null) {
                LayoutInflater inf = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inf.inflate(R.layout.capital_rowview_group_exp_faq, null);
            }

            TextView tvTitle = convertView.findViewById(R.id.tvTitle);
            ImageView ivIndicator = convertView.findViewById(R.id.ivIndicator);
            View viewLineBottom = convertView.findViewById(R.id.viewLineBottom);

            tvTitle.setText(details.getTitle());

            if (groupPosition == items.size() - 1) {
                viewLineBottom.setVisibility(View.GONE);
            }
            else {
                viewLineBottom.setVisibility(View.VISIBLE);
            }

            if (isExpanded) {
                ivIndicator.setImageResource(R.drawable.capital_ic_arrow_down);
                viewLineBottom.setVisibility(View.GONE);
            }
            else {
                ivIndicator.setImageResource(R.drawable.capital_ic_arrow);
                viewLineBottom.setVisibility(View.VISIBLE);
            }

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {

            return true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(MyApplication.Companion.getInstance()).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        try {
            if (isConnected) {
                if (isLoadingPending && !isLoading) {
                    //getDetailsAsync();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            try {
                activity.finish();
                activity.overridePendingTransition(R.anim.capital_activity_fade_in, R.anim.capital_activity_fade_out);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void getDetailsAsync() {
        try {
            new AsyncTask<Void, Void, Void>() {
                private boolean isSuccess = false;

                @Override
                protected void onPreExecute() {
                    try {
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
                        String URL = AppAPIUtils.GET_MULTI_FAMILY_FAQ;

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("ApiTokenId", AppAPIUtils.TOKEN_ID);

                        response = MitsUtils.readJSONServiceUsingPOST(URL, hashMap);

                        JSONObject jsonObject = new JSONObject(response);
                        String status = AppUtils.getValidAPIStringResponse(jsonObject.getString("status"));
                        if (status.equalsIgnoreCase("success")) {
                            listFaq = new ArrayList<FAQPojo>();
                            JSONArray array = jsonObject.getJSONArray("MultiFamilyFAQ");
                            if (array.length() > 0) {
                                isSuccess = true;

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = (JSONObject) array.get(i);
                                    FAQPojo pojo = new FAQPojo();
                                    pojo.setTitle(AppUtils.getValidAPIStringResponse(object.getString("Title")));
                                    pojo.setDescription(AppUtils.getValidAPIStringResponse(object.getString("Description")));
                                    listFaq.add(pojo);
                                }
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

                        setupData();
                    }
                    catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


}
