package com.application.alphacapital.superapp.pms.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.android.mit.mitsutils.MitsUtils;
import com.application.alphacapital.superapp.R;
import com.application.alphacapital.superapp.pms.utils.AppUtils;
import com.application.alphacapital.superapp.pms.utils.SessionManager;

public class NewBaseClass extends AppCompatActivity
{
    public static Activity activity;
    private SessionManager sessionManager;
    public DrawerLayout mDrawerLayout;
    private LinearLayout llToolBarBG;

    private TextView txtNetworth,txtPortfolio,txtTransaction,txtSIP,txtPerformance,txtXIRR,txtCapitalGain,txtMovement,txtDividend,txtPersonalDetail;
    private LinearLayout llLatestTransactions,llLatestSIP,llSchemeAllocation,llFundHouseAllocation;

    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            activity = this;
            sessionManager = new SessionManager(activity);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected void setUpViewDrawer(final Activity activity)
    {
        try
        {
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
            setupViews();
            mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener()
            {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset)
                {
                }

                @Override
                public void onDrawerOpened(View drawerView)
                {
                    llToolBarBG.setBackground(ContextCompat.getDrawable(activity,R.drawable.tool_bar_bg_open));
                    MitsUtils.hideKeyboard(activity);
                }

                @Override
                public void onDrawerClosed(View drawerView)
                {
                    llToolBarBG.setBackground(ContextCompat.getDrawable(activity,R.drawable.tool_bar_bg));
                }

                @Override
                public void onDrawerStateChanged(int newState)
                {
                }
            });

            onClickListener();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setupViews()
    {
        try
        {
            llToolBarBG = findViewById(R.id.llToolBarBG);
            txtNetworth = findViewById(R.id.txtNetworth);
            txtPortfolio = findViewById(R.id.txtPortfolio);
            txtTransaction = findViewById(R.id.txtTransaction);
            txtSIP = findViewById(R.id.txtSIP);
            txtPerformance = findViewById(R.id.txtPerformance);
            txtXIRR = findViewById(R.id.txtXIRR);
            txtCapitalGain = findViewById(R.id.txtCapitalGain);
            txtMovement = findViewById(R.id.txtMovement);
            txtDividend = findViewById(R.id.txtDividend);
            txtPersonalDetail = findViewById(R.id.txtPersonalDetail);

            llLatestTransactions = findViewById(R.id.llLatestTransactions);
            llLatestSIP = findViewById(R.id.llLatestSIP);
            llSchemeAllocation = findViewById(R.id.llSchemeAllocation);
            llFundHouseAllocation = findViewById(R.id.llFundHouseAllocation);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void onClickListener()
    {
        try
        {
            txtNetworth.setOnClickListener(v -> {
                try
                {
                    openDrawer();

                    Intent intent = new Intent(activity,NetworthActivityNew.class);
                    startActivity(intent);
                    AppUtils.startActivityAnimation(activity);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            });

            txtPortfolio.setOnClickListener(v -> {
                try
                {
                    openDrawer();

                    Intent intent = new Intent(activity,PortfolioActivity.class);
                    startActivity(intent);
                    AppUtils.startActivityAnimation(activity);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            });

            txtTransaction.setOnClickListener(v -> {
                try
                {
                    openDrawer();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            });

            txtSIP.setOnClickListener(v -> {
                try
                {
                    openDrawer();
                    Intent intent = new Intent(activity,SIPSTPActivity.class);
                    startActivity(intent);
                    AppUtils.startActivityAnimation(activity);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            });

            txtPerformance.setOnClickListener(v -> {
                try
                {
                    openDrawer();
                    Intent intent = new Intent(activity,PerformanceActivity.class);
                    startActivity(intent);
                    AppUtils.startActivityAnimation(activity);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            });

            txtXIRR.setOnClickListener(v -> {
                try
                {
                    openDrawer();

                    Intent intent = new Intent(activity,XIRRActivity.class);
                    startActivity(intent);
                    AppUtils.startActivityAnimation(activity);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            });

            txtCapitalGain.setOnClickListener(v -> {
                try
                {
                    openDrawer();

                    Intent intent = new Intent(activity,CapitalGainActivity.class);
                    startActivity(intent);
                    AppUtils.startActivityAnimation(activity);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            });

            txtMovement.setOnClickListener(v -> {
                try
                {
                    openDrawer();
                    Intent intent = new Intent(activity,ChartActivity.class);
                    startActivity(intent);
                    AppUtils.startActivityAnimation(activity);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            });

            txtDividend.setOnClickListener(v -> {
                try
                {
                    openDrawer();

                    Intent intent = new Intent(activity,DividendSummaryActivity.class);
                    startActivity(intent);
                    AppUtils.startActivityAnimation(activity);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            });

            txtPersonalDetail.setOnClickListener(v -> {
                try
                {
                    openDrawer();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            });

            llLatestSIP.setOnClickListener(new View.OnClickListener()
            {
                @Override public void onClick(View view)
                {
                    try
                    {
                        openDrawer();
                        Intent intent = new Intent(activity,LatestSIPList.class);
                        startActivity(intent);
                        AppUtils.startActivityAnimation(activity);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

            llLatestTransactions.setOnClickListener(new View.OnClickListener()
            {
                @Override public void onClick(View view)
                {
                    try
                    {
                        openDrawer();
                        Intent intent = new Intent(activity,LatestTransactionsList.class);
                        startActivity(intent);
                        AppUtils.startActivityAnimation(activity);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

            llSchemeAllocation.setOnClickListener(new View.OnClickListener()
            {
                @Override public void onClick(View view)
                {
                    try
                    {
                        openDrawer();
                        Intent intent = new Intent(activity,SchemeFundListActivity.class);
                        intent.putExtra("isFor","Scheme");
                        startActivity(intent);
                        AppUtils.startActivityAnimation(activity);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

            llFundHouseAllocation.setOnClickListener(new View.OnClickListener()
            {
                @Override public void onClick(View view)
                {
                    try
                    {
                        openDrawer();
                        Intent intent = new Intent(activity,SchemeFundListActivity.class);
                        intent.putExtra("isFor","Fund");
                        startActivity(intent);
                        AppUtils.startActivityAnimation(activity);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // Header Open Drawer Button Click
    public void openDrawer()
    {
        try
        {
            if (mDrawerLayout.isDrawerOpen(Gravity.LEFT))
            {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }
            else
            {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
