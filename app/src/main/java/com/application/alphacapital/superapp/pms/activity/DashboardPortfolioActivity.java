package com.application.alphacapital.superapp.pms.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import com.application.alphacapital.superapp.R;
import com.application.alphacapital.superapp.acpital.CapitalPref;
import com.application.alphacapital.superapp.acpital.CapitalUserProfileActivity;
import com.application.alphacapital.superapp.databinding.DashboardPortfolioActivityBinding;
import com.application.alphacapital.superapp.finplan.activity.FinPlanSplashActivity;
import com.application.alphacapital.superapp.pms.fragment.ContactFragment;
import com.application.alphacapital.superapp.pms.fragment.DashBoardFragment;
import com.application.alphacapital.superapp.pms.fragment.NetWorthFragment;
import com.application.alphacapital.superapp.pms.fragment.PortfolioFragment;
import com.application.alphacapital.superapp.pms.network.PortfolioApiClient;
import com.application.alphacapital.superapp.pms.network.PortfolioApiInterface;
import com.application.alphacapital.superapp.pms.utils.ApiUtils;
import com.application.alphacapital.superapp.pms.utils.AppUtils;
import com.application.alphacapital.superapp.pms.utils.SessionManager;
import com.application.alphacapital.superapp.vault.activity.VaultHomeActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;

public class DashboardPortfolioActivity extends NewBaseClass
{
    private DashboardPortfolioActivityBinding binding;
    private Activity activity;
    private SessionManager sessionManager;
    private PortfolioApiInterface apiService;
    //private String[] tabsTitle = {"Dashboard", "Networth", "Portfolio", "Contact"};
    private String[] tabsTitle = {"Dashboard", "Networth", "Portfolio"};
    private int[] tabsTitleIcon = {R.drawable.ic_tab_home,R.drawable.ic_tab_networth,R.drawable.ic_tab_portfolio,R.drawable.ic_tab_contact};
    private TabAdapter mAdapter;
    public static  ProgressBar pbToolbar;
    public static FrameLayout flSpinner;
    public static Spinner spnrYears;

    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activity = DashboardPortfolioActivity.this;
        binding = DataBindingUtil.setContentView(activity, R.layout.dashboard_portfolio_activity);
        sessionManager = new SessionManager(activity);
        apiService = PortfolioApiClient.getClient().create(PortfolioApiInterface.class);
        ApiUtils.END_USER_ID = sessionManager.getUserId();
        pbToolbar = binding.pbToolbar;
        flSpinner = binding.flSpinner;
        spnrYears = binding.spnrYears;
        initViews();
        setUpViewDrawer(activity);
        onClicks();
    }

    private void initViews()
    {
        try
        {
            binding.txtTitle.setText("Consolidated Portfolio");
            setupPagerData();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setupPagerData()
    {
        try
        {
            binding.tabs.setVisibility(View.VISIBLE);
            mAdapter = new TabAdapter();
            binding.pager.setAdapter(mAdapter);
            binding.tabs.setupWithViewPager(binding.pager);
            binding.pager.setOffscreenPageLimit(3);
            setupTabIcons();

            binding.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
            {
                @Override public void onTabSelected(TabLayout.Tab tab)
                {
                    if(tab.getPosition() == 0)
                    {
                        binding.txtTitle.setText("Hi, " + sessionManager.getFirstName() + " " + sessionManager.getLastName());
                        binding.flSpinner.setVisibility(View.GONE);
                    }
                    else if(tab.getPosition() == 1)
                    {
                        binding.txtTitle.setText("Networth");
                        binding.flSpinner.setVisibility(View.GONE);
                    }
                    else if(tab.getPosition() == 2)
                    {
                        binding.txtTitle.setText("Portfolio");
                        binding.flSpinner.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        binding.txtTitle.setText("Contact");
                        binding.flSpinner.setVisibility(View.GONE);
                    }

                    AppUtils.hideKeyboard(binding.tabs,activity);
                }

                @Override public void onTabUnselected(TabLayout.Tab tab)
                {
                }

                @Override public void onTabReselected(TabLayout.Tab tab)
                {
                }
            });


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setupTabIcons()
    {
        try
        {
            for (int i = 0; i < tabsTitle.length; i++)
            {
                final View tabOne = LayoutInflater.from(activity).inflate(R.layout.tab_row_view_home, null);
                TextView txtTabOne = (TextView) tabOne.findViewById(R.id.txtTab);
                ImageView ivTab = (ImageView) tabOne.findViewById(R.id.ivTab);
                ivTab.setImageResource(tabsTitleIcon[i]);
                txtTabOne.setText(tabsTitle[i]);
                binding.tabs.getTabAt(i).setCustomView(tabOne);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private class TabAdapter extends FragmentPagerAdapter
    {
        public TabAdapter()
        {
            super(getSupportFragmentManager());
        }

        @Override public Fragment getItem(int position)
        {
            if (position == 0)
            {
                return new DashBoardFragment();
            }
            else if (position == 1)
            {
                return new NetWorthFragment();
            }
            else if (position == 2)
            {
                return new PortfolioFragment();
            }
            else
            {
                return new ContactFragment();
            }
        }

        @Override public int getCount()
        {
            return tabsTitle.length;
        }

        @Override public CharSequence getPageTitle(int position)
        {
            return null;
        }
    }

    private void onClicks()
    {
        binding.llBack.setOnClickListener(v -> showBottomSheetDialog());

        binding.llTitle.setOnClickListener(v -> showSortPopup());
    }

    private void showBottomSheetDialog()
    {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.portfolio_layout_navigation);

        LinearLayout llBsMovement = bottomSheetDialog.findViewById(R.id.llBsMovement);
        LinearLayout llLatestTransactions = bottomSheetDialog.findViewById(R.id.llLatestTransactions);
        LinearLayout llLatestSIP = bottomSheetDialog.findViewById(R.id.llLatestSIP);
        LinearLayout llSchemeAllocation = bottomSheetDialog.findViewById(R.id.llSchemeAllocation);
        LinearLayout llFundHouseAllocation = bottomSheetDialog.findViewById(R.id.llFundHouseAllocation);
        LinearLayout llCapitalGain = bottomSheetDialog.findViewById(R.id.llCapitalGain);

        llBsMovement.setOnClickListener(v -> {
            try
            {
                Intent intent = new Intent(activity,ChartActivity.class);
                startActivity(intent);
                AppUtils.startActivityAnimation(activity);
                finish();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        llLatestTransactions.setOnClickListener(view ->
        {
            try
            {
                //openDrawer();
                Intent intent = new Intent(activity,LatestTransactionsList.class);
                startActivity(intent);
                AppUtils.startActivityAnimation(activity);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        llLatestSIP.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    Intent intent = new Intent(activity,SIPSTPActivity.class);
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
            @Override
            public void onClick(View v)
            {
                try
                {
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
            @Override
            public void onClick(View v)
            {
                try
                {
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

        llCapitalGain.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    Intent intent = new Intent(activity,CapitalGainActivity.class);
                    startActivity(intent);
                    AppUtils.startActivityAnimation(activity);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        bottomSheetDialog.show();
    }

    private void showSortPopup()
    {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.dialog_apps, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAsDropDown(binding.txtTitle);

        View container = (View) popupWindow.getContentView().getParent();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.3f;
        wm.updateViewLayout(container, p);

        TextView tvTitle = popupView.findViewById(R.id.tvTitle);
        TextView tvAlphaCapital = popupView.findViewById(R.id.tvAlphaCapital);
        TextView tvFinacialPlanning = popupView.findViewById(R.id.tvFinacialPlanning);
        TextView tvPMS = popupView.findViewById(R.id.tvPMS);
        TextView tvVault = popupView.findViewById(R.id.tvVault);
        tvTitle.setText("Consolidated Portfolio");

        tvAlphaCapital.setOnClickListener(view -> {
            Intent i = new Intent(getBaseContext(), CapitalUserProfileActivity.class);
            i.putExtra("loginScuccess","http://m.investwell.in/hcver8/pages/iwapplogin.jsp?bid=" + (CapitalPref.getString(getBaseContext(), "bid") + "&user=" + CapitalPref.getString(getBaseContext(), "uid") + "&password=" + CapitalPref.getString(getBaseContext(), "password")));
            startActivity(i);
            finish();
        });

        tvFinacialPlanning.setOnClickListener(view -> {
            popupWindow.dismiss();
        });

        tvPMS.setOnClickListener(v -> {
            Intent i = new Intent(getBaseContext(), FinPlanSplashActivity.class);
            startActivity(i);
            finish();
        });

        tvVault.setOnClickListener(v -> {
            Intent i = new Intent(getBaseContext(), VaultHomeActivity.class);
            startActivity(i);
            finish();
        });
    }

    @Override public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            try
            {
                finish();
                AppUtils.finishActivityAnimation(activity);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
