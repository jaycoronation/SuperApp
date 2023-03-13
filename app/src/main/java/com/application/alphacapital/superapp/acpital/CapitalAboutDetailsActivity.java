package com.application.alphacapital.superapp.acpital;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.mit.mitsutils.MitsUtils;
import com.application.alphacapital.superapp.R;
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

public class CapitalAboutDetailsActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener
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
    private String content = "", bannerImage = "",menuId="",title="",descriptionImage = "",subTitle = "";

    private boolean isLoading = false;
    private boolean isLoadingPending = false;
    private boolean isStatusBarHidden = false;

    private ImageView ivDescriptionImage;
    private LinearLayout llReadMore;
    TextView tvReadMore,tvSubTitle,tvInnerTitle;
    private int MAX_LINE = 10,MAX_CHARACTERS = 500;

    private ViewPager pagerVision;
    private ArrayList<String> listVision = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        try {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            isStatusBarHidden = true;
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

        setContentView(R.layout.capital_activity_about_details);

        setupViews();

        setupToolbar();

        onClickEvents();

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

        llReadMore = (LinearLayout) findViewById(R.id.llReadMore);
        tvReadMore = (TextView) findViewById(R.id.tvReadMore);

        ivDescriptionImage = (ImageView) findViewById(R.id.ivDescriptionImage);
        pagerVision = (ViewPager) findViewById(R.id.pagerVision);
        tvSubTitle = (TextView) findViewById(R.id.tvSubTitle);
        tvInnerTitle = (TextView) findViewById(R.id.tvInnerTitle);

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

            if(subTitle.equals(""))
            {
                tvSubTitle.setVisibility(View.GONE);
            }
            else
            {
                if(menuId.equals("33") || menuId.equals("35"))
                {
                    tvInnerTitle.setVisibility(View.VISIBLE);
                    tvInnerTitle.setText(subTitle);
                    tvSubTitle.setVisibility(View.GONE);
                }
                else
                {
                    tvInnerTitle.setVisibility(View.GONE);
                    tvSubTitle.setVisibility(View.VISIBLE);
                    tvSubTitle.setText(subTitle);
                }
            }

            if(descriptionImage.length() > 0)
            {
                ivDescriptionImage.setVisibility(View.VISIBLE);
                Glide.with(activity)
                        .load(descriptionImage)
                        .into(ivDescriptionImage);
            }
            else
            {
                ivDescriptionImage.setVisibility(View.GONE);
            }

            if(descriptionImage.equals(""))
            {
                MAX_LINE = 18;
            }
            else
            {
                MAX_LINE = 10;
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
              //  txtContent.setHtml(content, new HtmlHttpImageGetter(txtContent, true, customHeight));

                txtContent.setHtml(content, new HtmlHttpImageGetter(txtContent, content, true));


                Log.e("Get Line Counts", "setupData: " +  txtContent.getLineCount());

                if(!menuId.equals("23"))
                {
                    if (!txtContent.getText().toString().equals(""))
                    {
                        if(txtContent.getText().toString().length() > MAX_LINE)
                        {
                            llReadMore.setVisibility(View.VISIBLE);
                            //txtContent.setText(txtContent.getText().toString());
                        //    txtContent.setHtml(content, new HtmlHttpImageGetter(txtContent, true, customHeight));
                            txtContent.setHtml(content, new HtmlHttpImageGetter(txtContent, content, true));
                            if(descriptionImage.equals(""))
                            {
                                txtContent.setMaxLines(18);
                            }
                            else
                            {
                                txtContent.setMaxLines(10);
                            }
                        }
                        else
                        {
                            llReadMore.setVisibility(View.GONE);
                            //txtContent.setText(txtContent.getText().toString());
                          //  txtContent.setHtml(content, new HtmlHttpImageGetter(txtContent, true, customHeight));
                            txtContent.setHtml(content, new HtmlHttpImageGetter(txtContent, content, true));
                        }

                    }
                    else
                    {
                        txtContent.setVisibility(View.GONE);
                    }


                    if(descriptionImage.equals(""))
                    {
                        if(txtContent.getLineCount()<=18)
                        {
                            llReadMore.setVisibility(View.GONE);
                        }
                    }
                    else
                    {
                        if(txtContent.getLineCount()<=10)
                        {
                            llReadMore.setVisibility(View.GONE);
                        }
                    }

                    llReadMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            try
                            {
                                if(tvReadMore.getText().toString().equalsIgnoreCase("READ MORE"))
                                {
                                    tvReadMore.setText("READ LESS");
                                    ObjectAnimator animation = ObjectAnimator.ofInt(txtContent, "maxLines", txtContent.getLineCount());
                                    animation.setDuration(200).start();
                                }
                                else
                                {
                                    tvReadMore.setText("READ MORE");
                                    ObjectAnimator animation = ObjectAnimator.ofInt(txtContent, "maxLines", MAX_LINE);
                                    animation.setDuration(200).start();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
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
                private boolean isSuccess = false,visionSuccess = false;

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
                    getVision();
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
                            subTitle =  AppUtils.getValidAPIStringResponse(jsonObject.getString("SubTitle"));
                            bannerImage = AppUtils.getValidAPIStringResponse(jsonObject.getString("BannerImage"));
                            content = AppUtils.getValidAPIStringResponse(jsonObject.getString("Content"));
                            descriptionImage = AppUtils.getValidAPIStringResponse(jsonObject.getString("ContentImagePath"));
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

                private void getVision()
                {
                    try
                    {
                        String response = "";
                        String URL = AppAPIUtils.GET_OUR_VISION;

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("ApiTokenId", AppAPIUtils.TOKEN_ID);

                        response = MitsUtils.readJSONServiceUsingPOST(URL, hashMap);

                        listVision = new ArrayList<String>();

                        JSONObject jsonObject = new JSONObject(response);
                        String status = AppUtils.getValidAPIStringResponse(jsonObject.getString("status"));
                        if(status.equalsIgnoreCase("success"))
                        {
                            visionSuccess = true;

                            JSONArray array = jsonObject.getJSONArray("OurVision");
                            if(array.length()>0)
                            {
                                for (int i = 0; i < array.length(); i++)
                                {
                                    JSONObject obj = (JSONObject) array.get(i);
                                    listVision.add(AppUtils.getValidAPIStringResponse(obj.getString("OurVisionText")));
                                }
                            }
                        }
                        else
                        {
                            visionSuccess = false;
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

                        if(menuId.equals("23"))
                        {
                            pagerVision.setVisibility(View.VISIBLE);
                            if(visionSuccess)
                            {
                                VisionAdapter adapter = new VisionAdapter(listVision);
                                pagerVision.setAdapter(adapter);
                            }
                        }
                        else
                        {
                            pagerVision.setVisibility(View.GONE);
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

    private class VisionAdapter extends PagerAdapter
    {
        ArrayList<String> listItems;
        LayoutInflater mLayoutInflater;

        public VisionAdapter(ArrayList<String> list)
        {
            this.listItems = list;
            mLayoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount()
        {
            return listItems.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position)
        {
            View itemView = null;

            itemView = mLayoutInflater.inflate(R.layout.capital_rowview_vision, container, false);

            HtmlTextView tvVisionText = (HtmlTextView) itemView.findViewById(R.id.tvVisionText);
            ImageView ivLeft,ivRight;
            ivLeft = (ImageView) itemView.findViewById(R.id.ivLeft);
            ivRight = (ImageView) itemView.findViewById(R.id.ivRight);

            //tvVisionText.setHtml(listItems.get(position));

            int customHeight = (int) AppUtils.pxFromDp(activity, 14);
            tvVisionText.setTypeface(AppUtils.getTypefaceMedium(activity));
            //tvVisionText.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8.0f,  getResources().getDisplayMetrics()), 1.0f);
            //tvVisionText.setHtml(listItems.get(position), new HtmlHttpImageGetter(tvVisionText, true, customHeight));
            tvVisionText.setHtml(listItems.get(position));

            ivLeft.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        if(position != 0 )
                        {
                            pagerVision.setCurrentItem(position-1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            ivRight.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try {
                        if(position != listItems.size()-1)
                        {
                            pagerVision.setCurrentItem(position+1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
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
