package com.application.alphacapital.superapp.pms.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import com.application.alphacapital.superapp.R;
import com.application.alphacapital.superapp.pms.beans.LoginResponseModel;
import com.application.alphacapital.superapp.pms.network.PortfolioApiClient;
import com.application.alphacapital.superapp.pms.network.PortfolioApiInterface;
import com.application.alphacapital.superapp.pms.utils.AppUtils;
import com.application.alphacapital.superapp.pms.utils.SessionManager;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PortfolioLoginActivity extends AppCompatActivity
{
    private Activity activity;
    private SessionManager sessionManager;
    private LinearLayout llLoading;
    private LinearLayout llMain;
    private TextInputLayout inputEmail;
    private AppCompatEditText edtEmail;
    private TextInputLayout inputPassword;
    private AppCompatEditText edtPassword;
    private Button btnSignIn;
    private PortfolioApiInterface apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.portfolio_activity_login);

            activity = PortfolioLoginActivity.this;
            sessionManager = new SessionManager(activity);
            apiService = PortfolioApiClient.getClient().create(PortfolioApiInterface.class);
            setStatusBarGradiant(activity);
            initView();

            onclicks();

            try
            {
                if(PortfolioSplashActivity.isVersionMismatch)
                {
                    AppUtils.showVersionMismatchDialog(activity);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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


    private void initView()
    {
        try
        {
            llLoading = findViewById(R.id.llLoading);
            llMain = findViewById(R.id.llMain);
            inputEmail = findViewById(R.id.inputEmail);
            edtEmail =  findViewById(R.id.edtEmail);
            inputPassword =  findViewById(R.id.inputPassword);
            edtPassword =  findViewById(R.id.edtPassword);
            btnSignIn =  findViewById(R.id.btnSignIn);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void onclicks()
    {
        try
        {
            btnSignIn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (checkValidation())
                    {
                        if (AppUtils.isOnline())
                        {
                            AppUtils.hideKeyboard(edtPassword, activity);
                            signInAsync(edtEmail.getText().toString().trim(), edtPassword.getText().toString().trim());
                        }
                        else
                        {
                            AppUtils.showSnackBar(llLoading, getString(R.string.no_internet_message), activity);
                        }
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private String message = "", userId = "",userName = "", userEmail = "",userPassword = "", firstName = "",lastName = "",admin_id = "";
    private int status = 0;
    /**
     * call on server to check the user authentication
     * @param email pass the email/username
     * @param password pass the registered user password
     */
    private void signInAsync(final String email, final String password)
    {
        try
        {
            llLoading.setVisibility(View.VISIBLE);
            apiService.loginAPI(email,password).enqueue(new Callback<LoginResponseModel>() {
                @Override
                public void onResponse(@NotNull Call<LoginResponseModel> call, @NotNull Response<LoginResponseModel> response) {
                    llLoading.setVisibility(View.GONE);
                    if (response.isSuccessful())
                    {
                        if (response.body().getSuccess() == 1)
                        {
                            try
                            {
                                edtEmail.setText("");
                                edtPassword.setText("");

                                userName = response.body().getUsers().getUser_name();
                                userPassword = response.body().getUsers().getPassword();
                                firstName = response.body().getUsers().getFirst_name();
                                lastName = response.body().getUsers().getLast_name();
                                userEmail = response.body().getUsers().getEmail();
                                admin_id = response.body().getUsers().getAdmin_id();
                                Log.e("<><>ADMIN ID == ", response.body().getUsers().getAdmin_id());

                                sessionManager.createLoginSession(userId, admin_id,userName, firstName, lastName, userEmail);

                                finish();
                                Intent i = new Intent(activity, AllUsersActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(i);
                                AppUtils.startActivityAnimation(activity);
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            AppUtils.showSnackBar(llLoading, response.body().getMessage(), activity);
                        }
                    }
                    else
                    {
                        AppUtils.showSnackBar(llLoading, response.body().getMessage(), activity);
                    }
                }

                @Override
                public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                    AppUtils.showSnackBar(llLoading, activity.getString(R.string.api_failed_message), activity);
                    llLoading.setVisibility(View.GONE);
                }
            });

            /*new AsyncTask<Void, Void, Void>()
            {
                @Override
                protected void onPreExecute()
                {

                    super.onPreExecute();
                }

                @Override
                protected Void doInBackground(Void... params)
                {
                    try
                    {
                        HashMap<String, String> hashmap = new HashMap<String, String>();
                        hashmap.put("email", email);
                        hashmap.put("password", password);

                        Log.e("SIGNIN PARA", hashmap.toString());

                        String serverResponse = MitsUtils.readJSONServiceUsingPOST(ApiUtils.LOGIN, hashmap);

                        Log.e("SIGNIN Response", serverResponse + "");

                        try
                        {
                            JSONObject jsonObject = new JSONObject(serverResponse);

                            *//*{
                                "success": 1,
                                "message": "Successfully logged in",
                                "users":
                                {
                                "user_id": "50",
                                "user_name": "AlphaCapital",
                                "password": "aaAAjg842$@*TT",
                                "first_name": "Alpha",
                                "last_name": "Capital",
                                "email": "sanket@coronation.in"
                                }
                            }*//*

                            status = AppUtils.getValidAPIIntegerResponse(jsonObject.optString("success"));
                            message = AppUtils.getValidAPIStringResponse(jsonObject.optString("message"));

                            if (status == 1)
                            {
                                JSONObject object = jsonObject.getJSONObject("users");

                                userId = AppUtils.getValidAPIStringResponse(object.optString("user_id"));
                                userName = AppUtils.getValidAPIStringResponse(object.optString("user_name"));
                                userPassword = AppUtils.getValidAPIStringResponse(object.optString("password"));
                                firstName = AppUtils.getValidAPIStringResponse(object.optString("first_name"));
                                lastName = AppUtils.getValidAPIStringResponse(object.optString("last_name"));
                                userEmail = AppUtils.getValidAPIStringResponse(object.optString("email"));
                                admin_id = AppUtils.getValidAPIStringResponse(object.optString("admin_id"));
                            }
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
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
                    if (status == 1)
                    {

                    }
                    else
                    {

                    }

                }
            }.execute();*/
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private boolean checkValidation()
    {
        boolean isValid = true;

        if (edtEmail.getText().toString().length() == 0)
        {
            AppUtils.showSnackBar(edtEmail, activity.getString(R.string.enter_email), activity);
            isValid = false;
        }
        else if (edtPassword.getText().toString().trim().length() == 0)
        {
            AppUtils.showSnackBar(edtEmail, activity.getString(R.string.enter_password), activity);
            isValid = false;
        }

        return isValid;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            try
            {
                activity.finish();
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
