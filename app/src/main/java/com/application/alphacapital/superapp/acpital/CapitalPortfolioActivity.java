package com.application.alphacapital.superapp.acpital;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.application.alphacapital.superapp.R;
import com.application.alphacapital.superapp.acpital.classes.JSONParser;
import com.application.alphacapital.superapp.acpital.model.LoginResponseModel;
import com.application.alphacapital.superapp.acpital.network.CapitalApiClient;
import com.application.alphacapital.superapp.acpital.network.CapitalApiInterface;
import com.application.alphacapital.superapp.acpital.utils.AppUtils;
import com.application.alphacapital.superapp.acpital.utils.SessionManager;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CapitalPortfolioActivity extends AppCompatActivity
{
    private Activity activity;
    private SessionManager sessionManager;

    private ImageView ivIcon;
    // loading
    private View llLoading, llRetry;
    private ProgressBar pbLoading;
    private TextView txtLoading;

    private View llLogin, llSignUp, llLoginFields, llSignUpFields, ivFacebook, ivGPlus, llSubmit, llForgotPassword;
    private TextInputLayout inputEmail, inputPassword, inputName, inputSignUpEmail, inputContactNumber;
    private EditText edtEmail, edtPassword, edtName, edtSignUpEmail, edtContactNumber;
    private CheckBox cbTerms, cbRemember;

    private boolean isStatusBarHidden = false;

    // intent data
    private String menuId = "", title = "", menuIcon = "";

    String username, password;
    String name, email, contact;
    String uid, bid, flag;
    ProgressDialog progressDialog;

    CapitalApiInterface apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            isStatusBarHidden = true;
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

        sessionManager = new SessionManager(activity);
        apiService = Objects.requireNonNull(CapitalApiClient.INSTANCE.getClient()).create(CapitalApiInterface.class);

        if (getIntent() != null)
        {
            try
            {
                menuId = AppUtils.getValidAPIStringResponse(getIntent().getStringExtra("menuId"));
                title = AppUtils.getValidAPIStringResponse(getIntent().getStringExtra("title"));
                menuIcon = AppUtils.getValidAPIStringResponse(getIntent().getStringExtra("menuIcon"));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }


        setContentView(R.layout.capital_activity_portfolio);
        logincheck();

        setupViews();

        setupToolbar();

        onClickEvents();
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
        llLoading = findViewById(R.id.llLoading);
        llRetry = findViewById(R.id.llRetry);

        llForgotPassword = findViewById(R.id.llForgotPassword);
        llForgotPassword.setVisibility(View.GONE);

        pbLoading = findViewById(R.id.pbLoading);

        txtLoading = findViewById(R.id.txtLoading);
        txtLoading.setText(activity.getResources().getString(R.string.loading_progress_message));

        llLogin = findViewById(R.id.llLogin);
        llSignUp = findViewById(R.id.llSignUp);
        llLoginFields = findViewById(R.id.llLoginFields);
        llSignUpFields = findViewById(R.id.llSignUpFields);
        ivFacebook = findViewById(R.id.ivFacebook);
        ivGPlus = findViewById(R.id.ivGPlus);
        llSubmit = findViewById(R.id.llSubmit);

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputName = findViewById(R.id.inputName);
        inputSignUpEmail = findViewById(R.id.inputSignUpEmail);
        inputContactNumber = findViewById(R.id.inputContactNumber);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtName = findViewById(R.id.edtName);
        edtSignUpEmail = findViewById(R.id.edtSignUpEmail);
        edtContactNumber = findViewById(R.id.edtContactNumber);

        cbTerms = findViewById(R.id.cbTerms);
        cbTerms.setTypeface(AppUtils.getTypefaceRegular(activity));

        cbRemember = findViewById(R.id.checkBox);
        cbRemember.setTypeface(AppUtils.getTypefaceRegular(activity));

        edtPassword.setTypeface(AppUtils.getTypefaceRegular(activity));
        edtPassword.setTransformationMethod(new PasswordTransformationMethod());

        llLogin.setSelected(true);
        llSignUp.setSelected(false);

        llLoginFields.setVisibility(View.VISIBLE);
        llSignUpFields.setVisibility(View.GONE);

        AppUtils.gotoContactUs(activity, findViewById(R.id.ivContactUs));
    }

    private void setupToolbar()
    {
        try
        {
            final Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("");
            setSupportActionBar(toolbar);

            if (isStatusBarHidden)
            {
                toolbar.findViewById(R.id.viewStatusBar).setVisibility(View.INVISIBLE);
            }
            else
            {
                toolbar.findViewById(R.id.viewStatusBar).setVisibility(View.GONE);
            }

            toolbar.findViewById(R.id.ivLogo).setVisibility(View.GONE);
            toolbar.findViewById(R.id.llNotification).setVisibility(View.GONE);

            final TextView txtTitle = toolbar.findViewById(R.id.txtTitle);
            txtTitle.setText(title);

            final View llBackNavigation = toolbar.findViewById(R.id.llBackNavigation);
            llBackNavigation.setVisibility(View.VISIBLE);

            ivIcon = toolbar.findViewById(R.id.ivIcon);
            ivIcon.setVisibility(View.VISIBLE);

            try
            {
                Glide.with(activity).load(menuIcon).into(ivIcon);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            llBackNavigation.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        AppUtils.hideKeyboard(toolbar, activity);
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

    private void onClickEvents()
    {
        llLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!llLogin.isSelected())
                {
                    llLogin.setSelected(true);
                    llSignUp.setSelected(false);
                    llLoginFields.setVisibility(View.VISIBLE);
                    llSignUpFields.setVisibility(View.GONE);
                }
            }
        });

        llSignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!llSignUp.isSelected())
                {
                    llLogin.setSelected(false);
                    llSignUp.setSelected(true);

                    llLoginFields.setVisibility(View.GONE);
                    llSignUpFields.setVisibility(View.VISIBLE);
                }
            }
        });

        ivFacebook.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });

        ivGPlus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });

        llSubmit.setOnClickListener(v -> {
            AppUtils.hideKeyboard(edtEmail, activity);

            username = edtEmail.getText().toString().replace(" ", "%20");
            password = edtPassword.getText().toString().replace(" ", "%20");
            name = edtName.getText().toString().replace(" ", "%20");
            email = edtSignUpEmail.getText().toString().replace(" ", "%20");
            contact = edtContactNumber.getText().toString().replace(" ", "%20");


            if (llLogin.isSelected() == true)
            {
                if (checkValidationForLogin())
                {
                    System.out.println("@@@@" + username + "\n" + password);
                    //new LoginData().execute("http://m.investwell.in/hc/logincheck.jsp?bid=" + getString(R.string.bid) + "&user=" + username + "&password=" + password);
                    loginAPI();
                }
            }
            else
            {
                if (checkValidationForSignup())
                {
                    Log.e("<><>URL","http://m.investwell.in/hc/setuser.jsp?bid=" + getString(R.string.bid) + "&name=" + name + "&mobile=" + contact + "&email=" + email);
                    new RegisterData().execute("http://m.investwell.in/hc/setuser.jsp?bid=" + getString(R.string.bid) + "&name=" + name + "&mobile=" + contact + "&email=" + email);
                    //signUpApi();
                }
            }
        });

        llForgotPassword.setOnClickListener(v -> showForgotPasswordDialog());
    }

    private boolean checkValidationForLogin()
    {
        boolean isValid = true;
        if (edtEmail.getText().toString().length() == 0)
        {
            inputEmail.setError("Please enter user Id.");
            isValid = false;
        }
        /*else if (!AppUtils.validateEmail(edtEmail.getText().toString().trim()))
        {
            inputEmail.setError("Please enter valid user Id.");
            isValid = false;
        }*/
        else if (edtPassword.getText().toString().length() == 0)
        {
            inputPassword.setError("Please enter password.");
            isValid = false;
        }

        AppUtils.removeError(edtEmail, inputEmail);
        AppUtils.removeError(edtPassword, inputPassword);

        return isValid;
    }

    private boolean checkValidationForSignup()
    {
        boolean isValid = true;
        if (edtName.getText().toString().length() == 0)
        {
            inputName.setError("Please enter name.");
            isValid = false;
        }
        else if (edtSignUpEmail.getText().toString().length() == 0)
        {
            inputSignUpEmail.setError("Please enter email Id.");
            isValid = false;
        }
        else if (!AppUtils.validateEmail(edtSignUpEmail.getText().toString().trim()))
        {
            inputSignUpEmail.setError("Please enter valid email Id.");
            isValid = false;
        }
        else if (edtContactNumber.getText().toString().length() != 10)
        {
            inputContactNumber.setError("Please enter valid contact number.");
            isValid = false;
        }

        AppUtils.removeError(edtName, inputName);
        AppUtils.removeError(edtSignUpEmail, inputSignUpEmail);
        AppUtils.removeError(edtContactNumber, inputContactNumber);

        return isValid;
    }

    private void setupData()
    {
        try
        {

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


    public void loginAPI()
    {
        progressDialog = new ProgressDialog(CapitalPortfolioActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Map<String, String> params = new HashMap<String, String>();
        params.put("user", username);
        params.put("password", password);
        apiService.login(params).enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                if (response.isSuccessful())
                {
                    if (response.body() != null)
                    {
                        try {
                            uid = response.body().getUid();
                            bid = response.body().getBid();
                            flag = response.body().getFlag();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Login Failed....", Toast.LENGTH_LONG).show();
                    }

                    if (progressDialog.isShowing())
                    {
                        progressDialog.dismiss();
                    }

                    if (flag.equalsIgnoreCase("y"))
                    {
                        if (edtEmail.getText().toString().equals(uid) && edtPassword.getText().toString().equals(password))
                        {
                            cbRemember.setTypeface(AppUtils.getTypefaceRegular(activity));
                            if (cbRemember.isChecked())
                            {
                                //show logout activity
                                CapitalPref.putString(CapitalPortfolioActivity.this, "uid", uid);
                                CapitalPref.putString(CapitalPortfolioActivity.this, "password", password);
                                CapitalPref.putString(CapitalPortfolioActivity.this, "bid", bid);
                            }
                        }
                        Toast.makeText(getApplicationContext(), "Login Successfully....", Toast.LENGTH_LONG).show();
                        Intent in = new Intent(CapitalPortfolioActivity.this, CapitalUserProfileActivity.class);
                        in.putExtra("loginScuccess", "http://m.investwell.in/hcver8/pages/iwapplogin.jsp?bid=" + getString(R.string.bid) + "&user=" + uid + "&password=" + password);
                        startActivity(in);
                        finish();

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Invalid credentials....", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                AppUtils.apiFailedToast(activity);
                if (progressDialog.isShowing())
                {
                    progressDialog.dismiss();
                }
                Log.e("<><>MESSAGE", t.getMessage());
            }
        });
    }

    class LoginData extends AsyncTask<String, Void, Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CapitalPortfolioActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params)
        {
            JSONParser jsonParser = new JSONParser();

            String data = jsonParser.getJSON(params[0]);

            if (data != null)
            {

                try
                {
                    JSONObject obj = new JSONObject(data);

                    uid = obj.getString("uid");
                    bid = obj.getString("bid");
                    flag = obj.getString("flag");
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Login Failed....", Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            if (progressDialog.isShowing())
            {
                progressDialog.dismiss();
            }

            if (flag.equalsIgnoreCase("y"))
            {
                if (edtEmail.getText().toString().equals(uid) && edtPassword.getText().toString().equals(password))
                {
                    cbRemember.setTypeface(AppUtils.getTypefaceRegular(activity));
                    if (cbRemember.isChecked())
                    {
                        //show logout activity
                        CapitalPref.putString(CapitalPortfolioActivity.this, "uid", uid);
                        CapitalPref.putString(CapitalPortfolioActivity.this, "password", password);
                        CapitalPref.putString(CapitalPortfolioActivity.this, "bid", bid);
                    }
                }
                Toast.makeText(getApplicationContext(), "Login Successfully ....", Toast.LENGTH_LONG).show();
                Intent in = new Intent(CapitalPortfolioActivity.this, CapitalUserProfileActivity.class);
                in.putExtra("loginScuccess", "http://m.investwell.in/hcver8/pages/iwapplogin.jsp?bid=" + getString(R.string.bid) + "&user=" + uid + "&password=" + password);
                startActivity(in);
                finish();

            }
            else
            {
                Toast.makeText(getApplicationContext(), "Invalid credentials....", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void signUpApi()
    {

    }

    class RegisterData extends AsyncTask<String, Void, Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            progressDialog = new ProgressDialog(CapitalPortfolioActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(String... params)
        {
            JSONParser jsonParser = new JSONParser();

            String data = jsonParser.getJSON(params[0]);

            if (data != null)
            {

                try
                {
                    JSONObject obj = new JSONObject(data);

                    uid = obj.getString("uid");
                    bid = obj.getString("bid");
                    flag = obj.getString("flag");

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Registration Failed....", Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            if (progressDialog.isShowing())
            {
                progressDialog.dismiss();
            }

            if (flag.equalsIgnoreCase("y"))
            {
                Toast.makeText(getApplicationContext(), " Your request has been sent....", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getBaseContext(), CapitalPortfolioActivity.class));
                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(), " Registration Failed....", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void logincheck()
    {

        if (!TextUtils.isEmpty(CapitalPref.getString(getBaseContext(), "uid")) && !TextUtils.isEmpty(CapitalPref.getString(getBaseContext(), "password")))
        {

            if (!TextUtils.isEmpty(CapitalPref.getString(getBaseContext(), "bid")))
            {

                System.out.println("@@@@" + CapitalPref.getString(getBaseContext(), "uid") + "\n" + CapitalPref.getString(getBaseContext(), "password") + "\n" + CapitalPref.getString(getBaseContext(), "bid"));

                Intent in = new Intent(getBaseContext(), CapitalUserProfileActivity.class);

                // Intent in = new Intent(MainActivity.this,UserProfile.class);
                in.putExtra("loginScuccess", "http://m.investwell.in/hcver8/pages/iwapplogin.jsp?bid=" + (CapitalPref.getString(getBaseContext(), "bid") + "&user=" + CapitalPref.getString(getBaseContext(), "uid") + "&password=" + (CapitalPref.getString(getBaseContext(), "password"))));
                startActivity(in);
                //finish();
            }
        }

    }

    public void showForgotPasswordDialog()
    {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.Animation_AppCompat_DropDownUp;

        dialog.setContentView(R.layout.capital_layout_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        final EditText edtForgotEmail = dialog.findViewById(R.id.edtForgotEmail);
        final TextInputLayout inputForgotPassword = dialog.findViewById(R.id.inputForgotEmail);

        LinearLayout btnNo = dialog.findViewById(R.id.llDialogNo);
        LinearLayout btnYes = dialog.findViewById(R.id.llDialogYes);

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
                    if (edtForgotEmail.getText().toString().length() == 0)
                    {
                        inputForgotPassword.setError("Please enter email Id.");
                        isValid = false;
                    }
                    else if (!AppUtils.validateEmail(edtForgotEmail.getText().toString().trim()))
                    {
                        inputForgotPassword.setError("Please enter valid email Id.");
                        isValid = false;
                    }

                    AppUtils.removeError(edtForgotEmail, inputForgotPassword);

                    if (isValid)
                    {
                        //Forgot Password api will call here

                        Log.e("Validation OKay ", "onClick: ");

                        dialog.dismiss();
                        dialog.cancel();
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        dialog.show();
    }
}

