package com.application.alphacapital.superapp.acpital;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mit.mitsutils.MitsUtils;
import com.application.alphacapital.superapp.R;
import com.application.alphacapital.superapp.acpital.pojo.LocationPojo;
import com.application.alphacapital.superapp.acpital.service.ConnectivityReceiver;
import com.application.alphacapital.superapp.acpital.utils.AppAPIUtils;
import com.application.alphacapital.superapp.acpital.utils.AppUtils;
import com.application.alphacapital.superapp.acpital.utils.SessionManager;
import com.application.alphacapital.superapp.vault.utils.MyApplication;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimationType;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class CapitalLocateUsActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener
{
    private Activity activity;
    private SessionManager sessionManager;

    private ImageView ivHeader,ivIcon;

    // loading
    private View llLoading, llRetry, llNoData;
    private ProgressBar pbLoading;
    private TextView txtLoading;

    private View llContactMain, llEmail, llWeb, viewLine;
    private TextView txtEmail, txtWeb;
    private RecyclerView rvLocations;

    private TextInputLayout inputName,inputEmail,inputContact,inputMessage;
    private EditText edtName,edtEmail,edtContact,edtMessage;
    private TextView tvSubmit,tvCall;
    private LinearLayout llCall;

    private ArrayList<LocationPojo> listLocations = new ArrayList<>();
    private LocationRecyclerAdapter locationRecyclerAdapter;
    private String bannerImage = "", email = "", webLink = "",contactNumber = "9891857434";

    private boolean isLoading = false;
    private boolean isLoadingPending = false;
    private boolean isStatusBarHidden = false;

    // intent data
    private String menuId = "", title = "",menuIcon="";

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

        if(getIntent()!=null)
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

        setContentView(R.layout.capital_activity_locateus);

        setupViews();

        setupToolbar();

        onClickEvents();

        if(sessionManager.isNetworkAvailable())
        {
            getLocationsAsync();
        }
        else
        {
            isLoadingPending = true;
            txtLoading.setText(activity.getResources().getString(R.string.network_failed_message));
            pbLoading.setVisibility(View.GONE);
            llRetry.setVisibility(View.VISIBLE);
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
        findViewById(R.id.ivContactUs).setVisibility(View.GONE);

        llLoading = findViewById(R.id.llLoading);
        llRetry = findViewById(R.id.llRetry);
        llNoData = findViewById(R.id.llNoData);

        pbLoading = findViewById(R.id.pbLoading);

        txtLoading = findViewById(R.id.txtLoading);
        txtLoading.setText(activity.getResources().getString(R.string.loading_progress_message));

        llContactMain = findViewById(R.id.llContactMain);
        llEmail = findViewById(R.id.llEmail);
        llWeb = findViewById(R.id.llWeb);
        viewLine = findViewById(R.id.viewLine);

        txtEmail = findViewById(R.id.txtEmail);
        txtWeb = findViewById(R.id.txtWeb);

        inputName = findViewById(R.id.inputName);
        inputEmail = findViewById(R.id.inputEmail);
        inputContact = findViewById(R.id.inputContact);
        inputMessage = findViewById(R.id.inputMessage);

        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtContact = findViewById(R.id.edtContact);
        edtMessage = findViewById(R.id.edtMessage);

        tvSubmit = findViewById(R.id.tvSubmit);
        tvCall = findViewById(R.id.txtCall);

        AppUtils.removeError(edtContact,inputContact);
        AppUtils.removeError(edtEmail,inputEmail);
        AppUtils.removeError(edtMessage,inputMessage);
        AppUtils.removeError(edtName,inputName);


        rvLocations = findViewById(R.id.rvLocations);

        final GridLayoutManager layoutManager = new GridLayoutManager(activity, 2);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
        {
            @Override
            public int getSpanSize(int position)
            {
                try {
                    if (locationRecyclerAdapter != null)
                    {
                        switch (locationRecyclerAdapter.getItemViewType(position))
                        {
                            case 1:
                                return 1;
                            case 0:
                                return 2; //number of columns of the grid
                            default:
                                return -1;
                        }
                    }
                    else
                    {
                        return -1;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return -1;
                }
            }
        });

        rvLocations.setLayoutManager(layoutManager);
    }

    private void setupToolbar()
    {
        try {
            final Toolbar toolbar = findViewById(R.id.toolbar);
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

            final TextView txtTitle = toolbar.findViewById(R.id.txtTitle);
            txtTitle.setText(title);

            ivHeader = toolbar.findViewById(R.id.ivHeader);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ivHeader.getLayoutParams();
            params.height = (int) AppUtils.pxFromDp(activity, height);
            ivHeader.setLayoutParams(params);

            ivIcon = toolbar.findViewById(R.id.ivIcon);
            ivIcon.setVisibility(View.VISIBLE);

            try {
                Glide.with(activity)
                        .load(menuIcon)
                        .into(ivIcon);
            } catch (Exception e) {
                e.printStackTrace();
            }


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
                        getLocationsAsync();
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

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(sessionManager.isNetworkAvailable())
                {
                    if(edtName.getText().toString().trim().length()==0)
                    {
                        inputName.setError("Please enter your name.");
                    }
                    else if(edtEmail.getText().toString().trim().length()==0)
                    {
                        inputEmail.setError("Please enter email Id.");
                    }
                    else if(!AppUtils.validateEmail(edtEmail.getText().toString().trim()))
                    {
                        inputEmail.setError("Please enter valid email Id.");
                    }
                    else if(edtContact.getText().toString().trim().length()==0)
                    {
                        inputContact.setError("Please enter contact number.");
                    }
                    else if(edtContact.getText().toString().trim().length()!=10)
                    {
                        inputContact.setError("Please enter valid contact number.");
                    }
                    else if(edtMessage.getText().toString().trim().length()==0)
                    {
                        inputMessage.setError("Please enter your message.");
                    }
                    else
                    {
                        AppUtils.hideKeyboard(edtMessage,activity);

                        submitContactUs(edtName.getText().toString().trim(),
                                edtContact.getText().toString().trim(),
                                edtEmail.getText().toString().trim(),
                                edtMessage.getText().toString().trim());
                    }

                }
                else
                {
                    Toast.makeText(activity, activity.getString(R.string.network_failed_message), Toast.LENGTH_SHORT).show();
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

            if(email.length() == 0 && webLink.length() == 0)
            {
                llContactMain.setVisibility(View.GONE);
                viewLine.setVisibility(View.GONE);
            }
            else
            {
                llContactMain.setVisibility(View.VISIBLE);
                viewLine.setVisibility(View.VISIBLE);

                if(email.length() > 0)
                {
                    llEmail.setVisibility(View.VISIBLE);
                    txtEmail.setText(email);
                }
                else
                {
                    llEmail.setVisibility(View.GONE);
                }

                tvCall.setText(contactNumber);

                if(webLink.length() > 0)
                {
                    llWeb.setVisibility(View.VISIBLE);
                    if(!webLink.toLowerCase(Locale.getDefault()).startsWith("http"))
                    {
                        webLink = "http://" + webLink;
                    }
                    txtWeb.setText(webLink);
                }
                else
                {
                    llWeb.setVisibility(View.GONE);
                }
            }

            locationRecyclerAdapter = new LocationRecyclerAdapter(listLocations);
            rvLocations.setAdapter(locationRecyclerAdapter);

            if(listLocations.size() == 0)
            {
                llNoData.setVisibility(View.VISIBLE);
                llLoading.setVisibility(View.GONE);
                txtLoading.setText(activity.getResources().getString(R.string.no_location_data));
                pbLoading.setVisibility(View.GONE);
                llRetry.setVisibility(View.GONE);
                rvLocations.setVisibility(View.GONE);
            }
            else
            {
                llNoData.setVisibility(View.GONE);
                llLoading.setVisibility(View.GONE);
                rvLocations.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getLocationsAsync()
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
                        listLocations = new ArrayList<>();
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
                        String URL = AppAPIUtils.GET_ALL_LOCATIONS;

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("ApiTokenId", AppAPIUtils.TOKEN_ID);
                        hashMap.put("MenuId", menuId);

                        response = MitsUtils.readJSONServiceUsingPOST(URL, hashMap);

                        Log.e("Contact Response? ", "doInBackground: " + response);

                        JSONObject jsonObject = new JSONObject(response);
                        String status = AppUtils.getValidAPIStringResponse(jsonObject.getString("status"));
                        if(status.equalsIgnoreCase("success"))
                        {
                            isSuccess = true;

                            bannerImage = AppUtils.getValidAPIStringResponse(jsonObject.getString("BannerImage"));
                            email = AppUtils.getValidAPIStringResponse(jsonObject.getString("Email"));
                            webLink = AppUtils.getValidAPIStringResponse(jsonObject.getString("Website"));
                            contactNumber = AppUtils.getValidAPIStringResponse(jsonObject.getString("ContactNumber"));

                            JSONArray jsonArray = jsonObject.getJSONArray("ContactList");
                            for(int i=0; i<jsonArray.length(); i++)
                            {
                                JSONObject jsonObjectContact = jsonArray.getJSONObject(i);

                                String cityName = AppUtils.getValidAPIStringResponse(jsonObjectContact.getString("CityName"));
                                String address = AppUtils.getValidAPIStringResponse(jsonObjectContact.getString("Address"));
                                String phone = AppUtils.getValidAPIStringResponse(jsonObjectContact.getString("Phone"));
                                String fax = AppUtils.getValidAPIStringResponse(jsonObjectContact.getString("Fax"));

                                LocationPojo locationPojo = new LocationPojo();
                                locationPojo.setCityName(cityName);
                                locationPojo.setAddress(address);
                                locationPojo.setPhone(phone);
                                locationPojo.setFax(fax);
                                listLocations.add(locationPojo);
                            }
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

    private class LocationRecyclerAdapter extends RecyclerView.Adapter<LocationRecyclerAdapter.ViewHolder>
    {
        ArrayList<LocationPojo> items;
        private final SpringyAdapterAnimator mAnimator;

        LocationRecyclerAdapter(ArrayList<LocationPojo> list)
        {
            this.items = list;
            mAnimator = new SpringyAdapterAnimator(rvLocations);
            mAnimator.setSpringAnimationType(SpringyAdapterAnimationType.SLIDE_FROM_BOTTOM);
            mAnimator.addConfig(85,15);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.capital_rowview_location, viewGroup, false);
            mAnimator.onSpringItemCreate(v);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            final LocationPojo locationPojo = items.get(position);

            if(position % 2 == 0)
            {
                holder.viewLineLeft.setVisibility(View.GONE);
            }
            else
            {
                holder.viewLineLeft.setVisibility(View.VISIBLE);
            }

            if(position == 0 || position == 1)
            {
                holder.viewLineTop.setVisibility(View.GONE);
            }
            else
            {
                holder.viewLineTop.setVisibility(View.VISIBLE);
            }

            holder.txtLocation.setText(locationPojo.getCityName());
            holder.txtLocation.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8.0f,  getResources().getDisplayMetrics()), 1.0f);

            mAnimator.onSpringItemBind(holder.itemView, position);

            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try {
                        Intent intent = new Intent(activity, CapitalLocationDetailsActivity.class);
                        intent.putExtra("title", locationPojo.getCityName());
                        intent.putExtra("bannerImage", bannerImage);
                        intent.putExtra("address", locationPojo.getAddress());
                        intent.putExtra("phone", locationPojo.getPhone());
                        intent.putExtra("fax", locationPojo.getFax());
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.capital_activity_open_translate,R.anim.capital_activity_close_scale);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public int getItemViewType(int position)
        {
            try {
                if(position == (items.size() - 1))
                {
                    // If the item is last, `itemViewType` will be 0
                    return  0;
                }
                else
                {
                    return 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 1;
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            final View viewLineLeft, viewLineTop;
            final TextView txtLocation;

            ViewHolder(View convertView)
            {
                super(convertView);
                viewLineLeft = convertView.findViewById(R.id.viewLineLeft);
                viewLineTop = convertView.findViewById(R.id.viewLineTop);
                txtLocation = convertView.findViewById(R.id.txtLocation);
            }
        }
    }

    private void submitContactUs(final String name,final String mobile,final String email,final String comment)
    {
        new AsyncTask<Void,Void,Void>()
        {
            String status = "";
            String message = "";

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                llLoading.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... voids)
            {
                try {
                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("ApiTokenId", AppAPIUtils.TOKEN_ID);
                    hashMap.put("name",name);
                    hashMap.put("contactnumber",mobile);
                    hashMap.put("email",email);
                    hashMap.put("message",comment);

                    Log.e("Req", "doInBackground:       "+hashMap.toString() );
                    String serverResponse = MitsUtils.readJSONServiceUsingPOST(AppAPIUtils.CONTACT_SUBMIT,hashMap);
                    Log.e("Resp", "doInBackground:       "+serverResponse );

                    JSONObject jsonObject = new JSONObject(serverResponse);

                    status = AppUtils.getValidAPIStringResponse(jsonObject.optString("status"));
                    message = AppUtils.getValidAPIStringResponse(jsonObject.optString("msg"));

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid)
            {
                super.onPostExecute(aVoid);
                llLoading.setVisibility(View.GONE);

                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

                if(status.equalsIgnoreCase("success"))
                {
                    edtContact.setText("");
                    edtEmail.setText("");
                    edtName.setText("");
                    edtMessage.setText("");
                }
            }
        }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,(Void)null);
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
                    getLocationsAsync();
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
