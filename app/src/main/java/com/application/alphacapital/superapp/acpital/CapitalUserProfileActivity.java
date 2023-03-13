package com.application.alphacapital.superapp.acpital;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.application.alphacapital.superapp.R;
import com.application.alphacapital.superapp.acpital.utils.AppUtils;
import com.application.alphacapital.superapp.finplan.activity.FinPlanLoginActivity;
import com.application.alphacapital.superapp.pms.activity.OneDayChangeActivity;
import com.application.alphacapital.superapp.vault.activity.VaultHomeActivity;
import com.google.android.material.textfield.TextInputLayout;


public class CapitalUserProfileActivity extends Activity {

    Activity activity;
    ImageView logout,ivLogo;
    LinearLayout llMessage;
    String uid,password;
    private WebView browser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capital_activity_user_profile);

        activity = CapitalUserProfileActivity.this;
        setStatusBarGradiant(activity);
        llMessage = (LinearLayout) findViewById(R.id.llMessage);
        ivLogo = findViewById(R.id.ivLogo);
        llMessage.setVisibility(View.GONE);
        llMessage.setOnClickListener(v -> showMessageDialog());

        AppUtils.gotoContactUs(activity,findViewById(R.id.ivContactUs));

        logout = (ImageView) findViewById(R.id.logout);
        logout.setOnClickListener(view -> {
            final SharedPreferences preferences =getSharedPreferences(CapitalPref.PREF_NAME,MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();

            uid = "";
            password = "";

            startActivity(new Intent(getBaseContext(), CapitalPortfolioActivity.class));
            finish();
        });

        ivLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        browser = (WebView) findViewById(R.id.userPortfolio);
        browser.getSettings().setJavaScriptEnabled(true);

        browser.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {

                CapitalUserProfileActivity.this.setProgress(progress * 5000);
            }
        });
        browser.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                // startActivity(new Intent(getBaseContext(),ErrorMessage.class));
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();

            }
        });
        Bundle extras = getIntent().getExtras();
        if(extras !=null){
            String loginUrl =  extras.getString("loginScuccess");
            if(loginUrl!=null)
                browser.loadUrl(loginUrl);

            Log.e("LOGIN URL",loginUrl);
        }

        LinearLayout back = (LinearLayout)findViewById(R.id.llBackNavigation);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getBaseContext(), CapitalMainActivity.class));
                finish();
            }
        });
    }

    void setStatusBarGradiant(Activity activity)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(activity,android.R.color.transparent));
            window.setNavigationBarColor(ContextCompat.getColor(activity,android.R.color.black));
            window.setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.vault_bg_gradient));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void showMessageDialog()
    {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.Animation_AppCompat_DropDownUp;

        dialog.setContentView(R.layout.capital_dialog_message);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        final EditText edtMessage = (EditText) dialog.findViewById(R.id.edtMessage);
        final TextInputLayout inputMessage = (TextInputLayout) dialog.findViewById(R.id.inputMessage);

        LinearLayout btnNo = (LinearLayout) dialog.findViewById(R.id.llDialogNo);
        LinearLayout btnYes = (LinearLayout) dialog.findViewById(R.id.llDialogYes);

        btnNo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                dialog.cancel();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                try
                {
                    boolean isValid = true;
                    if (edtMessage.getText().toString().length() == 0)
                    {
                        inputMessage.setError("Please enter your message.");
                        isValid = false;
                    }

                    AppUtils.removeError(edtMessage,inputMessage);

                    if(isValid)
                    {
                        //Api call here

                        dialog.dismiss();
                        dialog.cancel();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        dialog.show();
    }
}
