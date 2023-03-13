package com.application.alphacapital.superapp.acpital;

import android.app.Activity;
import android.content.Context;
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
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.android.mit.mitsutils.MitsUtils;
import com.application.alphacapital.superapp.R;
import com.application.alphacapital.superapp.acpital.classes.CircleImageView;
import com.application.alphacapital.superapp.acpital.classes.NonScrollExpandableListView;
import com.application.alphacapital.superapp.acpital.pojo.MTPojo;
import com.application.alphacapital.superapp.acpital.service.ConnectivityReceiver;
import com.application.alphacapital.superapp.acpital.utils.AppAPIUtils;
import com.application.alphacapital.superapp.acpital.utils.AppUtils;
import com.application.alphacapital.superapp.acpital.utils.SessionManager;
import com.application.alphacapital.superapp.vault.utils.MyApplication;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class CapitalManagementTeamActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener
{
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

    String bannerImage="",content="";
    private HtmlTextView txtContent;

    // intent data
    private String menuId = "", title = "";

    private NonScrollExpandableListView expFaq;
    private ArrayList<MTPojo> listMT = new ArrayList<>();

    private  int lastExpandedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        activity = this;
        sessionManager = new SessionManager(activity);

        menuId = AppUtils.getValidAPIStringResponse(getIntent().getStringExtra("menuId"));
        title = AppUtils.getValidAPIStringResponse(getIntent().getStringExtra("title"));

        setContentView(R.layout.capital_activity_management_team);

        setupViews();

        setupToolbar();

        onClickEvents();

        //setCustomData();

        if(sessionManager.isNetworkAvailable())
        {
            getDetailsAsync();
        }
        else
        {
            isLoadingPending = true;
            txtLoading.setText(activity.getResources().getString(R.string.network_failed_message));
            pbLoading.setVisibility(View.GONE);
            llRetry.setVisibility(View.VISIBLE);
        }
    }

    private void getDetailsAsync()
    {
        try
        {
            new AsyncTask<Void, Void, Void>()
            {
                private boolean isSuccess = false, mtSuccess = false;

                @Override
                protected void onPreExecute()
                {
                    try {
                        content = "";
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
                    getMenu();
                    getTeamData();
                    return null;
                }

                private void getMenu()
                {
                    try
                    {
                        String response = "";
                        String URL = AppAPIUtils.GET_DETAILS_BY_MENU_ID;

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("ApiTokenId", AppAPIUtils.TOKEN_ID);
                        hashMap.put("MenuId", menuId);

                        response = MitsUtils.readJSONServiceUsingPOST(URL, hashMap);

                        Log.e("Menu Details Response ", "getMenu: " + response );

                        JSONObject jsonObject = new JSONObject(response);
                        String status = AppUtils.getValidAPIStringResponse(jsonObject.getString("status"));
                        if(status.equalsIgnoreCase("success"))
                        {
                            isSuccess = true;

                            bannerImage = AppUtils.getValidAPIStringResponse(jsonObject.getString("BannerImage"));
                            content = AppUtils.getValidAPIStringResponse(jsonObject.getString("Content"));
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
                }

                private void getTeamData()
                {
                    try
                    {
                        String response = "";
                        String URL = AppAPIUtils.GET_MANAGEMENT_TEAM;

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("ApiTokenId", AppAPIUtils.TOKEN_ID);

                        response = MitsUtils.readJSONServiceUsingPOST(URL, hashMap);

                        listMT = new ArrayList<MTPojo>();

                        JSONObject jsonObject = new JSONObject(response);
                        String status = AppUtils.getValidAPIStringResponse(jsonObject.getString("status"));
                        if(status.equalsIgnoreCase("success"))
                        {
                            mtSuccess = true;

                            JSONArray array = jsonObject.getJSONArray("ManagementTeam");
                            if(array.length()>0)
                            {
                                for (int i = 0; i < array.length(); i++)
                                {
                                    JSONObject obj = (JSONObject) array.get(i);

                                    MTPojo pojo = new MTPojo();
                                    pojo.setName(AppUtils.getValidAPIStringResponse(obj.getString("TeamName")));
                                    pojo.setImage(AppUtils.getValidAPIStringResponse(obj.getString("TeamImage")));
                                    pojo.setDegree(AppUtils.getValidAPIStringResponse(obj.getString("TeamDegree")));
                                    pojo.setAbout(AppUtils.getValidAPIStringResponse(obj.getString("TeamDesc")));
                                    listMT.add(pojo);
                                }
                            }
                        }
                        else
                        {
                            mtSuccess = false;
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                protected void onPostExecute(Void result)
                {
                    super.onPostExecute(result);
                    try
                    {
                        isLoading = false;

                        setupData();

                        if(mtSuccess)
                        {
                            MyExpandableListAdapter adapter = new MyExpandableListAdapter(activity, listMT);
                            expFaq.setAdapter(adapter);
                        }
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

        expFaq = (NonScrollExpandableListView) findViewById(R.id.expFaq);
        txtContent = (HtmlTextView) findViewById(R.id.txtContent);
        txtContent.setTypeface(AppUtils.getTypefaceMedium(activity));
        txtContent.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8.0f,  getResources().getDisplayMetrics()), 1.0f);

        AppUtils.gotoContactUs(activity,findViewById(R.id.ivContactUs));
    }

    private void setupToolbar()
    {
        try
        {
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
                        getDetailsAsync();
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

        expFaq.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition)
            {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expFaq.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });
    }

    private void setupData()
    {
        try
        {
            isLoadingPending = false;

            llLoading.setVisibility(View.GONE);

            if(bannerImage.length() > 0)
            {
                Glide.with(activity)
                        .load(bannerImage)
                        .into(ivHeader);
            }

            int customHeight = (int) AppUtils.pxFromDp(activity, 14);
           // txtContent.setHtml(content, new HtmlHttpImageGetter(txtContent, true, customHeight));

            txtContent.setHtml(content, new HtmlHttpImageGetter(txtContent, content, true));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setCustomData()
    {
        MTPojo pojo = new MTPojo();
        pojo.setName("DR. MUKESH JINDAL");
        pojo.setDegree("(CFA,CA,PH.D)");
        pojo.setImage("https://alphacapital.in/images/about/mukesh.png");
        pojo.setAbout("Mukesh brings with him the expertise of setting up and managing Family Office for affluent families. In his previous assignment, he was a Partner with Client Associates.\n" +
                "\n" +
                "He did his Masters in Finance from University of Delhi after completing Engineering in Computer Science. He is a CFA Charter holder from CFA Institute, US and a CAIA Charter holder from CAIA Association, US. He is also a CFPCM from Financial Planning Standards Board (FPSB).\n" +
                "\n" +
                "Mukesh completed his Doctorate in Finance and attained Ph.D. degree. His research paper ‘Relationship of Asset Allocation Policy and Portfolio Performance’ has been published in esteemed research journals.\n" +
                "\n" +
                "He is an Associate Faculty with Department of Financial Studies, University of Delhi. He is passionate about teaching and sharing his knowledge with others.\n" +
                "\n" +
                "Mukesh has been recognized as a Leading Global Financial Advisor by City Wealth, London.\n" +
                "\n" +
                "He specializes in designing macroeconomic models and investment strategies.");

        listMT.add(pojo);

        MTPojo pojo1 = new MTPojo();
        pojo1.setName("AKHIL BHARDWAJ");
        pojo1.setImage("https://alphacapital.in/images/about/akhil.png");
        pojo1.setDegree("");
        pojo1.setAbout("Akhil has years of experience in Private Wealth Management. Prior to this assignment, he was working with Client Associates as a Consultant.\n" +
                "\n" +
                "Akhilhas done his MBA in finance from ICFAI Business School after completing B.Sc. with specialization in Maths from Delhi University. Presently he is an AFP from Financial Planning Standards Board (FPSB).\n" +
                "\n" +
                "Akhil is also in the Board of Directors of BDS Institute of Management & Technology, where he heads the finance committee of non-profitable society - DKS SikshaPrasarsamiti. He also aspires to bring financial competence in Tier 2 cities, where awareness about investments is limited.");

        listMT.add(pojo1);

        MTPojo pojo2 = new MTPojo();
        pojo2.setName("KUNAL DHILLON");
        pojo2.setImage("https://alphacapital.in/images/about/kunal.png");
        pojo2.setDegree("");
        pojo2.setAbout("Kunal brings with him a healthy real estate experience which he has gathered from his association with Aditya Birla Retail where he was the Zonal Head - North India.\n" +
                "\n" +
                "Kunal has done his MBA from IIMM, Pune after completing B.Com from Delhi University. Before his association with ABRL, Kunal has worked with Standard Chartered Bank and Met Life insurance in New Delhi.\n" +
                "\n" +
                "Kunal specializes in real estate solutions encompassing residential, commercial & retail leasing solutions.\n" +
                "\n" +
                "Kunal is also a member of the Board of Directors of The Dhilson Education & Social Development Society, where he is a treasurer. He also aspires to provide world class education infrastructure to the underprivileged children of the rural India on a non-profit basis.");

        listMT.add(pojo2);

        MTPojo pojo3 = new MTPojo();
        pojo3.setName("NIKHIL VIKAMSEY");
        pojo3.setImage("https://alphacapital.in/images/about/nikhil.png");
        pojo3.setDegree("");
        pojo3.setAbout("Nikhil Vikamsey is a qualified Chartered Accountant and LLB. Nikhil has over a decade of professional experience, working with companies like Ernst & Young & Citibank NA.\n" +
                "\n" +
                "He has served as CFO of Mount Housing & infrastructure Limited, involved in real estate development and also listed on stock exchange.\n" +
                "\n" +
                "Given his passion about finance and clubbed with professional experience, Nikhil specializes in Investment banking activities, which broadly includes raising debt, infusion of equity, M & A activities both buy and sell side, IPO advisory and financial consultancy for Corporate.\n" +
                "\n" +
                "Nikhil is also active member of Tie Coimbatore and in process of becoming a Rotarian. He is also passionate about net-working and travelling.\n" +
                "\n" +
                "Nikhil invests leisure time in Agriculture activities by working on-fields and plays Golf occasionally.");

        listMT.add(pojo3);


        MyExpandableListAdapter adapter = new MyExpandableListAdapter(activity, listMT);
        expFaq.setAdapter(adapter);
    }

    class MyExpandableListAdapter extends BaseExpandableListAdapter
    {

        private Activity context;
        private ArrayList<MTPojo> items;

        public MyExpandableListAdapter(Activity context, ArrayList<MTPojo> listFaq) {
            this.context = context;
            this.items = listFaq;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition)
        {
            return items.get(groupPosition).getAbout();
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
        {
            final MTPojo details = items.get(groupPosition);

            if (convertView == null) {
                //LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.capital_rowview_child_exp_mteam, null);
            }

            HtmlTextView tvAbout = (HtmlTextView) convertView.findViewById(R.id.tvAbout);

            tvAbout.setTypeface(AppUtils.getTypefaceMedium(activity));
            tvAbout.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8.0f,  getResources().getDisplayMetrics()), 1.0f);
            tvAbout.setHtml(details.getAbout());

            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition)
        {
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition)
        {
            return items.get(groupPosition).getName();
        }

        @Override
        public int getGroupCount() {
            return items.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent)
        {
            final MTPojo details = items.get(groupPosition);
            if (convertView == null)
            {
                LayoutInflater inf = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inf.inflate(R.layout.capital_rowview_group_exp_mteam, null);
            }

            TextView tvName= (TextView) convertView.findViewById(R.id.tvName);
            TextView tvDegree= (TextView) convertView.findViewById(R.id.tvDegree);
            ImageView ivIndicator = (ImageView) convertView.findViewById(R.id.ivIndicator);
            CircleImageView ivImage = (CircleImageView) convertView.findViewById(R.id.ivImage);
            CardView cardMain = (CardView) convertView.findViewById(R.id.cardMain);
            LinearLayout llMain = (LinearLayout) convertView.findViewById(R.id.llMain);

            Glide.with(activity)
                    .load(details.getImage())
                    .into(ivImage);

            tvName.setText(details.getName());

            if(!details.getDegree().equals(""))
            {
                tvDegree.setVisibility(View.VISIBLE);
                tvDegree.setText(details.getDegree());
            }
            else
            {
                tvDegree.setVisibility(View.GONE);
            }

            if(groupPosition == items.size()-1)
            {
                llMain.setBackgroundColor(ContextCompat.getColor(activity,R.color.white));
            }
            else
            {
                llMain.setBackgroundColor(ContextCompat.getColor(activity,R.color.bg_main_light));
            }

            if(isExpanded)
            {
                ivIndicator.setImageResource(R.drawable.capital_ic_minus);
                cardMain.setElevation(0);
                llMain.setBackgroundColor(ContextCompat.getColor(activity,R.color.white));
            }
            else
            {
                ivIndicator.setImageResource(R.drawable.capital_ic_plus);
                cardMain.setElevation(2);
                llMain.setBackgroundColor(ContextCompat.getColor(activity,R.color.bg_main_light));
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
                    //getDetailsAsync();
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
