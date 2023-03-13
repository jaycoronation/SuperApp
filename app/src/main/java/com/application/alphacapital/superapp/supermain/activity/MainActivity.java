package com.application.alphacapital.superapp.supermain.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.application.alphacapital.superapp.supermain.model.FeedResponseModel;
import com.application.alphacapital.superapp.supermain.model.Item;
import com.application.alphacapital.superapp.supermain.model.VideoResponseModel;
import com.application.alphacapital.superapp.vault.utils.VaultSessionManager;
import com.application.alphacapital.superapp.R;
import com.application.alphacapital.superapp.acpital.CapitalPref;
import com.application.alphacapital.superapp.acpital.CapitalUserProfileActivity;
import com.application.alphacapital.superapp.databinding.ActivityMainBinding;
import com.application.alphacapital.superapp.finplan.activity.FinPlanMainActivity;
import com.application.alphacapital.superapp.finplan.activity.FinPlanSplashActivity;
import com.application.alphacapital.superapp.pms.activity.DashboardPortfolioActivity;
import com.application.alphacapital.superapp.pms.utils.AppUtils;
import com.application.alphacapital.superapp.pms.utils.SessionManager;
import com.application.alphacapital.superapp.vault.activity.VaultHomeActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity
{
    ActivityMainBinding binding;
    private SessionManager sessionManager;
    private VaultSessionManager vaultSessionManager;
    private com.application.alphacapital.superapp.finplan.utils.SessionManager finPlanSession;
    ArrayList<Item> feedItemList = new ArrayList<>();
    ArrayList<VideoResponseModel.Item> videoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        sessionManager = new SessionManager(this);
        vaultSessionManager = new VaultSessionManager(this);
        finPlanSession = new com.application.alphacapital.superapp.finplan.utils.SessionManager(this);
        binding.txtTitle.setText("Hi " + sessionManager.getFirstName() + " " + sessionManager.getLastName());
        onClick();
        getFeedJSON();
        getVideoList();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void onClick()
    {
        binding.ivMenu.setOnClickListener(view -> {
            Intent intent = new Intent(getBaseContext(), ProfileActivity.class);
            startActivity(intent);
        });

        binding.cvAlphaCapital.setOnClickListener(v ->
        {
            Animation anim = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.scale_animation_new);
            anim.reset();
            v.clearAnimation();
            //v.startAnimation(anim);

            Intent in = new Intent(getBaseContext(), CapitalUserProfileActivity.class);
            in.putExtra("loginScuccess", "http://m.investwell.in/hcver8/pages/iwapplogin.jsp?bid=" + (CapitalPref.getString(getBaseContext(), "bid") + "&user=" + CapitalPref.getString(getBaseContext(), "uid") + "&password=" + (CapitalPref.getString(getBaseContext(), "password"))));
            startActivity(in);

            anim.setAnimationListener(new Animation.AnimationListener()
            {
                @Override
                public void onAnimationStart(Animation animation)
                {
                }

                @Override
                public void onAnimationEnd(Animation animation)
                {
                    if (AppUtils.isOnline())
                    {
                        Intent in = new Intent(getBaseContext(), CapitalUserProfileActivity.class);
                        in.putExtra("loginScuccess", "http://m.investwell.in/hcver8/pages/iwapplogin.jsp?bid=" + (CapitalPref.getString(getBaseContext(), "bid") + "&user=" + CapitalPref.getString(getBaseContext(), "uid") + "&password=" + (CapitalPref.getString(getBaseContext(), "password"))));
                        startActivity(in);
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, R.string.no_internet_message, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation)
                {
                }
            });
        });

        binding.cvFinancialPlanning.setOnClickListener(v ->
        {
            /*Animation anim = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.scale_animation_new);
            anim.reset();
            v.clearAnimation();*/

            if (AppUtils.isOnline())
            {
                Intent intent = new Intent(getBaseContext(), FinPlanMainActivity.class);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(MainActivity.this, R.string.no_internet_message, Toast.LENGTH_SHORT).show();
            }

            /*anim.setAnimationListener(new Animation.AnimationListener()
            {
                @Override
                public void onAnimationStart(Animation animation)
                {
                }

                @Override
                public void onAnimationEnd(Animation animation)
                {
                    if (AppUtils.isOnline())
                    {
                        Intent intent = new Intent(getBaseContext(), FinPlanSplashActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, R.string.no_internet_message, Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onAnimationRepeat(Animation animation)
                {
                }
            });*/
        });

        binding.cvPMS.setOnClickListener(v ->
        {

            Intent intent = new Intent(MainActivity.this, DashboardPortfolioActivity.class);
            MainActivity.this.startActivity(intent);

           /* Animation anim = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.scale_animation_new);
            anim.reset();
            v.clearAnimation();*/
            //v.startAnimation(anim);

            /*anim.setAnimationListener(new Animation.AnimationListener()
            {
                @Override
                public void onAnimationStart(Animation animation)
                {
                }

                @Override
                public void onAnimationEnd(Animation animation)
                {
                }

                @Override
                public void onAnimationRepeat(Animation animation)
                {
                }
            });*/
        });

        binding.cvVault.setOnClickListener(v ->
        {
            Animation anim = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.scale_animation_new);
            anim.reset();
            v.clearAnimation();
            //v.startAnimation(anim);

            Intent intent = new Intent(MainActivity.this, VaultHomeActivity.class);
            startActivity(intent);

            anim.setAnimationListener(new Animation.AnimationListener()
            {
                @Override
                public void onAnimationStart(Animation animation)
                {
                }

                @Override
                public void onAnimationEnd(Animation animation)
                {

                }

                @Override
                public void onAnimationRepeat(Animation animation)
                {
                }
            });
        });

        binding.imgFixedMeeting.setOnClickListener(v ->
        {
            Animation anim = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.scale_animation_new);
            anim.reset();
            v.clearAnimation();
            //v.startAnimation(anim);

            Intent intent = new Intent(getBaseContext(), WebViewActivity.class);
            intent.putExtra("isFor", "meeting");
            startActivity(intent);

            anim.setAnimationListener(new Animation.AnimationListener()
            {
                @Override
                public void onAnimationStart(Animation animation)
                {
                }

                @Override
                public void onAnimationEnd(Animation animation)
                {

                }

                @Override
                public void onAnimationRepeat(Animation animation)
                {
                }
            });
        });

        binding.imgContact.setOnClickListener(v ->
        {
            Animation anim = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.scale_animation_new);
            anim.reset();
            v.clearAnimation();
            //v.startAnimation(anim);

            Intent intent = new Intent(getBaseContext(), ContactActivity.class);
            startActivity(intent);

            anim.setAnimationListener(new Animation.AnimationListener()
            {
                @Override
                public void onAnimationStart(Animation animation)
                {
                }

                @Override
                public void onAnimationEnd(Animation animation)
                {
                }

                @Override
                public void onAnimationRepeat(Animation animation)
                {
                }
            });
        });

        binding.ivLogout.setOnClickListener(v ->
        {
            openLogoutBottomSheet();
        });

        binding.ivBlog.setOnClickListener(v ->
        {
           /* Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("isFor", "blog");
            startActivity(intent);*/

             Intent intent = new Intent(this, FeedActivity.class);
             intent.putExtra("data", new Gson().toJson(feedItemList));
            startActivity(intent);

        });

        binding.ivVideo.setOnClickListener(v ->
        {
            Intent intent = new Intent(this, VideoActivity.class);
            intent.putExtra("data", new Gson().toJson(videoList));
            startActivity(intent);
        });
    }

    private void openLogoutBottomSheet()
    {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BaseBottomSheetDialog);
        bottomSheetDialog.setContentView(R.layout.capital_dialog_logout);

        bottomSheetDialog.setCanceledOnTouchOutside(true);

        LinearLayout llLogout = bottomSheetDialog.findViewById(R.id.llLogout);
        LinearLayout llCancel = bottomSheetDialog.findViewById(R.id.llCancel);

        llCancel.setOnClickListener(v -> bottomSheetDialog.dismiss());

        llLogout.setOnClickListener(v ->
        {
            vaultSessionManager.logoutUser();
            sessionManager.logoutUserFromNotification();
            finPlanSession.logoutUser();
            finishAffinity();
            Intent intent = new Intent(MainActivity.this, MainLoginActivity.class);
            startActivity(intent);
        });

        bottomSheetDialog.show();
    }

    private void getFeedJSON()
    {
        apiService.getFeedJsonData("1").enqueue(new Callback<FeedResponseModel>() {
            @Override
            public void onResponse(Call<FeedResponseModel> call, Response<FeedResponseModel> response) {
                FeedResponseModel getSet = new FeedResponseModel();
                getSet.setFeed_url(response.body().getFeed_url());
                getSet.setDescription(response.body().getDescription());
                getSet.setHome_page_url(response.body().getHome_page_url());
                getSet.setIcon(response.body().getIcon());
                getSet.setItems(response.body().getItems());
                getSet.setLanguage(response.body().getLanguage());
                getSet.setNext_url(response.body().getNext_url());
                getSet.setTitle(response.body().getTitle());
                getSet.setUser_comment(response.body().getUser_comment());
                getSet.setVersion(response.body().getVersion());
                feedItemList.addAll(response.body().getItems());
            }

            @Override
            public void onFailure(Call<FeedResponseModel> call, Throwable t) {
                apiFailedToast();
            }
        });
    }

    private void getVideoList()
    {
        apiService.getVideos().enqueue(new Callback<VideoResponseModel>() {
            @Override
            public void onResponse(Call<VideoResponseModel> call, Response<VideoResponseModel> response) {
                videoList.addAll(response.body().getItems());
            }

            @Override
            public void onFailure(Call<VideoResponseModel> call, Throwable t) {
                apiFailedToast();
            }
        });
    }
}