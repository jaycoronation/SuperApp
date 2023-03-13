package com.application.alphacapital.superapp.acpital;

import android.app.Activity;
import android.content.Intent;
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

import org.json.JSONObject;
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class CapitalReportsActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener
{

    private Activity activity;
    private SessionManager sessionManager;

    private ImageView ivHeader;

    // loading
    private View llLoading, llRetry, llNoData;
    private ProgressBar pbLoading;
    private TextView txtLoading;

    private View cvContent;
    private HtmlTextView txtContent;
    private String content = "", bannerImage = "";

    private boolean isLoading = false;
    private boolean isLoadingPending = false;
    private boolean isStatusBarHidden = false;

    // intent data
    private String menuId = "", title = "Reports & Analysis";

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

        //menuId = AppUtils.getValidAPIStringResponse(getIntent().getStringExtra("menuId"));
        //title = AppUtils.getValidAPIStringResponse(getIntent().getStringExtra("title"));

        setContentView(R.layout.capital_activity_reports);

        setupViews();

        setupToolbar();

        onClickEvents();

        setCustomData();
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

        cvContent = findViewById(R.id.cvContent);

        txtContent = (HtmlTextView) findViewById(R.id.txtContent);
        txtContent.setTypeface(AppUtils.getTypefaceMedium(activity));
        txtContent.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8.0f,  getResources().getDisplayMetrics()), 1.0f);

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
    }

    private void setupData()
    {
        try {
            isLoadingPending = false;

            if(bannerImage.length() > 0)
            {
                Glide.with(activity)
                        .load(bannerImage)
                        .into(ivHeader);
            }

            if(content.length() == 0)
            {
                llNoData.setVisibility(View.VISIBLE);
                llLoading.setVisibility(View.GONE);
                txtLoading.setText(activity.getResources().getString(R.string.no_data));
                pbLoading.setVisibility(View.GONE);
                llRetry.setVisibility(View.GONE);
                cvContent.setVisibility(View.GONE);
            }
            else
            {
                llNoData.setVisibility(View.GONE);
                llLoading.setVisibility(View.GONE);
                cvContent.setVisibility(View.VISIBLE);

                int customHeight = (int) AppUtils.pxFromDp(activity, 14);

//                String data = "<p style=\"line-height: 2;\"> Tax considerations affect virtually every financial decision your family makes. Fo that reason, our team-based approach is designed to integrate professional tax planning with all aspects of your financial picture. </p> <p> We can help you identify the values you wish to perpatiate and then translate then into an effective, long-term philanthropic strategy. Our advice administrative support can enable you to : </p> <p> <img src=\"http://demo1.coronation.in/alphacapitalapp/images/alpha_icon.png\" style=\"width: 5%; float: left;\" /> <span style=\"display:inline-block; padding: 0 0 0 3%; width: 91%;\">Assist in selecting and establishing many different types of philanthropic entities, such as family foundations, donor-advised funds, supporting organizations, charitable trusts and community foundations</span> </p> <p> <img src=\"http://demo1.coronation.in/alphacapitalapp/images/alpha_icon.png\" style=\"width: 5%; float: left;\" /> <span style=\"display:inline-block; padding: 0 0 0 3%; width: 91%;\">Identity and articulate the values you wish to perpatuate</span> </p> <p> <img src=\"http://demo1.coronation.in/alphacapitalapp/images/alpha_icon.png\" style=\"width: 5%; float: left;\" /> <span style=\"display:inline-block; padding: 0 0 0 3%; width: 91%;\">Identity and research charitable organizations that fit your interests and priorities</span> </p> <p> <img src=\"http://demo1.coronation.in/alphacapitalapp/images/alpha_icon.png\" style=\"width: 5%; float: left;\" /> <span style=\"display:inline-block; padding: 0 0 0 3%; width: 91%;\">Design effective philanthropic structures for your unique tax, financial and family situation and interests</span> </p> <p> <img src=\"http://demo1.coronation.in/alphacapitalapp/images/alpha_icon.png\" style=\"width: 5%; float: left;\" /> <span style=\"display:inline-block; padding: 0 0 0 3%; width: 91%;\">Develop family programs that involve your chaildren and educate them about your chaitable activities and all aspects of charitable giving</span> </p> <p> <img src=\"http://demo1.coronation.in/alphacapitalapp/images/alpha_icon.png\" style=\"width: 5%; float: left;\" /> <span style=\"display:inline-block; padding: 0 0 0 3%; width: 91%;\">Maximize the social and economic benefits of your philanthropic efforts</span> </p>";

               // txtContent.setHtml(content, new HtmlHttpImageGetter(txtContent, true, customHeight));

                txtContent.setHtml(content, new HtmlHttpImageGetter(txtContent, content, true));

                /*Glide.with(activity).load(bannerImage).asBitmap().into(new SimpleTarget<Bitmap>()
                {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation)
                    {
                        try {
                            Drawable drawable = new BitmapDrawable(activity.getResources(), bitmap);
                            toolbar.setBackground(drawable);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getDetailsAsync()
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
                    try
                    {
                        String response = "";
                        String URL = AppAPIUtils.GET_DETAILS_BY_MENU_ID;

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("ApiTokenId", AppAPIUtils.TOKEN_ID);
                        hashMap.put("MenuId", menuId);

                        response = MitsUtils.readJSONServiceUsingPOST(URL, hashMap);

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

    private void setCustomData()
    {
        MainPojo pojo = new MainPojo();
        pojo.setTitle("Market Analysis & Outlook");
        pojo.setMenuId("1");

        listAbout.add(pojo);

        MainPojo pojo2 = new MainPojo();
        pojo2.setTitle("What We Are Reading");
        pojo2.setMenuId("2");
        listAbout.add(pojo2);

        MainPojo pojo3 = new MainPojo();
        pojo3.setTitle("Union Budget");
        pojo3.setMenuId("3");
        listAbout.add(pojo3);

        MainPojo pojo4 = new MainPojo();
        pojo4.setTitle("Others");
        pojo4.setMenuId("4");
        listAbout.add(pojo4);


        AboutAdapter aboutAdapter = new AboutAdapter(listAbout);
        rvAbout.setAdapter(aboutAdapter);
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

            /*if(position == (items.size() - 1))
            {
                holder.viewLineBottom.setVisibility(View.GONE);
            }
            else
            {
                holder.viewLineBottom.setVisibility(View.VISIBLE);
            }*/

            holder.tvTitle.setText(pojo.getTitle());

            mAnimator.onSpringItemBind(holder.itemView, position);

            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        Intent intent = null;
                        intent = new Intent(activity, CapitalResearchActivity.class);
                        intent.putExtra("menuId", pojo.getMenuId());
                        intent.putExtra("title", pojo.getTitle());
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.capital_activity_open_translate,R.anim.capital_activity_close_scale);
                        /*Intent intent = new Intent(activity, AboutDetailsActivity.class);
                        intent.putExtra("menuId", pojo.getMenuId());
                        intent.putExtra("title", pojo.getTitle());
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
        try {
            if(isConnected)
            {
                if(isLoadingPending && !isLoading)
                {
                    getDetailsAsync();
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
