package com.application.alphacapital.superapp.acpital;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.application.alphacapital.superapp.R;
import com.application.alphacapital.superapp.acpital.utils.AppUtils;
import com.bumptech.glide.Glide;

public class CapitalLocationDetailsActivity extends AppCompatActivity
{
    private Activity activity;

    private ImageView ivHeader;

    // loading
    private View llNoData;

    private View llAddress, llPhone, llFax;
    private TextView txtAddress, txtPhone, txtFax;

    private boolean isStatusBarHidden = false;

    // intent data
    private String title = "", address = "", phone = "", fax = "", bannerImage = "";

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
        catch (Exception e)
        {
            e.printStackTrace();
        }

        try
        {
            super.onCreate(savedInstanceState);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        activity = this;

        title = AppUtils.getValidAPIStringResponse(getIntent().getStringExtra("title"));
        address = AppUtils.getValidAPIStringResponse(getIntent().getStringExtra("address"));
        phone = AppUtils.getValidAPIStringResponse(getIntent().getStringExtra("phone"));
        fax = AppUtils.getValidAPIStringResponse(getIntent().getStringExtra("fax"));
        bannerImage = AppUtils.getValidAPIStringResponse(getIntent().getStringExtra("bannerImage"));

        setContentView(R.layout.capital_activity_location_details);

        setupViews();

        setupToolbar();

        setupData();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        try
        {
            super.onRestoreInstanceState(savedInstanceState);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setupViews()
    {
        llNoData = findViewById(R.id.llNoData);
        llAddress = findViewById(R.id.llAddress);
        llPhone = findViewById(R.id.llPhone);
        llFax = findViewById(R.id.llFax);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        txtPhone = (TextView) findViewById(R.id.txtPhone);
        txtFax = (TextView) findViewById(R.id.txtFax);
        txtAddress.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8.0f, getResources().getDisplayMetrics()), 1.0f);
        findViewById(R.id.ivContactUs).setVisibility(View.GONE);
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
            if (isStatusBarHidden)
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
                    }
                    catch (Exception e)
                    {
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
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setupData()
    {
        try
        {
            if (bannerImage.length() > 0)
            {
                Glide.with(activity).load(bannerImage).into(ivHeader);
            }

            if (address.length() == 0 && phone.length() == 0 && fax.length() == 0)
            {
                llNoData.setVisibility(View.VISIBLE);
            }
            else
            {
                llNoData.setVisibility(View.GONE);

                if (address.length() > 0)
                {
                    llAddress.setVisibility(View.VISIBLE);
                    txtAddress.setText(address);
                }
                else
                {
                    llAddress.setVisibility(View.GONE);
                }

                if (phone.length() > 0)
                {
                    llPhone.setVisibility(View.VISIBLE);
                    txtPhone.setText(phone);
                }
                else
                {
                    llPhone.setVisibility(View.GONE);
                }

                if (fax.length() > 0)
                {
                    llFax.setVisibility(View.VISIBLE);
                    txtFax.setText(fax);
                }
                else
                {
                    llFax.setVisibility(View.GONE);
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
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
