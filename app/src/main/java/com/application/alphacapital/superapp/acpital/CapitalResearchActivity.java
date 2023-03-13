package com.application.alphacapital.superapp.acpital;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mit.mitsutils.MitsUtils;
import com.application.alphacapital.superapp.R;
import com.application.alphacapital.superapp.acpital.pojo.ResearchPojo;
import com.application.alphacapital.superapp.acpital.service.ConnectivityReceiver;
import com.application.alphacapital.superapp.acpital.utils.AppAPIUtils;
import com.application.alphacapital.superapp.acpital.utils.AppUtils;
import com.application.alphacapital.superapp.acpital.utils.SessionManager;
import com.application.alphacapital.superapp.vault.utils.MyApplication;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimationType;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class CapitalResearchActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private Activity activity;
    private SessionManager sessionManager;

    // loading
    private View llLoading, llRetry, llNoData;
    private ProgressBar pbLoading;
    private TextView txtLoading;

    private RecyclerView rvResearch;

    private ArrayList<ResearchPojo> listResearch = new ArrayList<>();
    private ResearchRecyclerAdapter researchRecyclerAdapter;

    private boolean isLoading = false;
    private boolean isLoadingPending = false;
    private boolean isStatusBarHidden = false;

    // intent data
    private String menuId = "", title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Window w = getWindow();
                w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                isStatusBarHidden = true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        try {
            super.onCreate(savedInstanceState);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        activity = this;
        sessionManager = new SessionManager(activity);

        menuId = AppUtils.getValidAPIStringResponse(getIntent().getStringExtra("menuId"));
        title = AppUtils.getValidAPIStringResponse(getIntent().getStringExtra("title"));

        setContentView(R.layout.capital_activity_research);

        setupViews();

        setupToolbar();

        onClickEvents();

        if (sessionManager.isNetworkAvailable()) {
            getResearchAsync();
        }
        else {
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
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupViews() {
        llLoading = findViewById(R.id.llLoading);
        llRetry = findViewById(R.id.llRetry);
        llNoData = findViewById(R.id.llNoData);

        pbLoading = (ProgressBar) findViewById(R.id.pbLoading);

        txtLoading = (TextView) findViewById(R.id.txtLoading);
        txtLoading.setText(activity.getResources().getString(R.string.loading_progress_message));

        rvResearch = (RecyclerView) findViewById(R.id.rvResearch);
        rvResearch.setLayoutManager(new LinearLayoutManager(activity));

        AppUtils.gotoContactUs(activity, findViewById(R.id.ivContactUs));
    }

    private void setupToolbar() {
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("");
            setSupportActionBar(toolbar);

            if (isStatusBarHidden) {
                toolbar.findViewById(R.id.viewStatusBar).setVisibility(View.INVISIBLE);
            }
            else {
                toolbar.findViewById(R.id.viewStatusBar).setVisibility(View.GONE);
            }

            toolbar.findViewById(R.id.ivLogo).setVisibility(View.GONE);
            toolbar.findViewById(R.id.llNotification).setVisibility(View.GONE);

            final TextView txtTitle = (TextView) toolbar.findViewById(R.id.txtTitle);
            txtTitle.setText(title);

            final View llBackNavigation = toolbar.findViewById(R.id.llBackNavigation);
            llBackNavigation.setVisibility(View.VISIBLE);

            llBackNavigation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        activity.finish();
                        activity.overridePendingTransition(R.anim.capital_activity_fade_in, R.anim.capital_activity_fade_out);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onClickEvents() {
        llRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (sessionManager.isNetworkAvailable()) {
                        getResearchAsync();
                    }
                    else {
                        txtLoading.setText(activity.getResources().getString(R.string.network_failed_message));
                        pbLoading.setVisibility(View.GONE);
                        llRetry.setVisibility(View.VISIBLE);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setupData() {
        try {
            isLoadingPending = false;

            researchRecyclerAdapter = new ResearchRecyclerAdapter(listResearch);
            rvResearch.setAdapter(researchRecyclerAdapter);

            if (listResearch.size() == 0) {
                llNoData.setVisibility(View.VISIBLE);
                llLoading.setVisibility(View.GONE);
                txtLoading.setText(activity.getResources().getString(R.string.no_research_data));
                pbLoading.setVisibility(View.GONE);
                llRetry.setVisibility(View.GONE);
                rvResearch.setVisibility(View.GONE);
            }
            else {
                llNoData.setVisibility(View.GONE);
                llLoading.setVisibility(View.GONE);
                rvResearch.setVisibility(View.VISIBLE);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getResearchAsync() {
        try {
            new AsyncTask<Void, Void, Void>() {
                private boolean isSuccess = false;

                @Override
                protected void onPreExecute() {
                    try {
                        listResearch = new ArrayList<>();
                        isLoading = true;
                        llRetry.setVisibility(View.GONE);
                        llLoading.setVisibility(View.VISIBLE);
                        txtLoading.setText(activity.getResources().getString(R.string.loading_progress_message));
                        pbLoading.setVisibility(View.VISIBLE);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    super.onPreExecute();
                }

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        String response = "";
                        String URL = AppAPIUtils.GET_ALL_RESEARCH;

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("ApiTokenId", AppAPIUtils.TOKEN_ID);
                        hashMap.put("MenuId", menuId);

                        Log.e("Research Req ", "doInBackground: " + hashMap.toString());

                        response = MitsUtils.readJSONServiceUsingPOST(URL, hashMap);

                        Log.e("Research Res ", "doInBackground: " + response);

                        JSONObject jsonObject = new JSONObject(response);
                        String status = AppUtils.getValidAPIStringResponse(jsonObject.getString("status"));
                        if (status.equalsIgnoreCase("success")) {
                            isSuccess = true;

                            JSONArray jsonArray = jsonObject.getJSONArray("ResearchList");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectMenu = jsonArray.getJSONObject(i);
                                String researchTitle = AppUtils.getValidAPIStringResponse(jsonObjectMenu.getString("ResearchTitle"));
                                String filePath = AppUtils.getValidAPIStringResponse(jsonObjectMenu.getString("PDFFilePath"));

                                ResearchPojo researchPojo = new ResearchPojo();
                                researchPojo.setResearchTitle(researchTitle);
                                researchPojo.setFilePath(filePath);
                                listResearch.add(researchPojo);
                            }
                        }
                        else {
                            isSuccess = false;
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    super.onPostExecute(result);
                    try {
                        isLoading = false;

                        setupData();
                    }
                    catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ResearchRecyclerAdapter extends RecyclerView.Adapter<ResearchRecyclerAdapter.ViewHolder> {
        ArrayList<ResearchPojo> items;
        private SpringyAdapterAnimator mAnimator;

        ResearchRecyclerAdapter(ArrayList<ResearchPojo> list) {
            this.items = list;
            mAnimator = new SpringyAdapterAnimator(rvResearch);
            mAnimator.setSpringAnimationType(SpringyAdapterAnimationType.SLIDE_FROM_BOTTOM);
            mAnimator.addConfig(85, 15);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.capital_rowview_research, viewGroup, false);
            mAnimator.onSpringItemCreate(v);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final ResearchPojo researchPojo = items.get(position);

            if (position == (items.size() - 1)) {
                holder.viewLineBottom.setVisibility(View.GONE);
            }
            else {
                holder.viewLineBottom.setVisibility(View.VISIBLE);
            }

            String title = researchPojo.getResearchTitle();
            holder.txtResearch.setText(title);
            holder.txtResearch.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6.0f, getResources().getDisplayMetrics()), 1.0f);

            mAnimator.onSpringItemBind(holder.itemView, position);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (researchPojo.getFilePath().length() > 0 && researchPojo.getFilePath().toLowerCase(Locale.getDefault()).startsWith("http")) {
                            selectedFilePath = researchPojo.getFilePath();
                            checkStoragePermission();
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            final TextView txtResearch;
            final View viewLineBottom;

            ViewHolder(View convertView) {
                super(convertView);
                txtResearch = (TextView) convertView.findViewById(R.id.txtResearch);
                viewLineBottom = convertView.findViewById(R.id.viewLineBottom);
            }
        }
    }

    private String selectedFilePath = "";
    final int PERMISSION_REQUEST_CODE_STORAGE = 11;

    private void checkStoragePermission() {
        try {
            int result;
            result = ContextCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (result == PackageManager.PERMISSION_GRANTED) {
                addFileToDownloadQueue();
            }
            else {
                ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE_STORAGE);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addFileToDownloadQueue() {
        try {
            File dirtostoreFile = new File(Environment.getExternalStorageDirectory() + "/Alpha Capital/Research/");
            if (!dirtostoreFile.exists()) {
                dirtostoreFile.mkdirs();
            }
            String[] pathArray = selectedFilePath.split("/");
            String name = pathArray[pathArray.length - 1];
            File file = new File(Environment.getExternalStorageDirectory() + "/Alpha Capital/Research/" + name);
            if (file.exists()) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                return;
            }
            DownloadManager dm = (DownloadManager) activity.getSystemService(Activity.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(selectedFilePath));
            request.setDestinationUri(Uri.fromFile(file));
            request.setVisibleInDownloadsUi(true);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            long queueid = dm.enqueue(request);
            Log.e("Download request id", queueid + " %%% PATH = " + file.getAbsolutePath());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE_STORAGE) {
            try {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    addFileToDownloadQueue();
                }
                else {
                    Toast.makeText(activity, "Permissions Denied, we cannot proceed next without it.", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(MyApplication.Companion.getInstance()).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        try
        {
            if (isConnected)
            {
                if (isLoadingPending && !isLoading)
                {
                    getResearchAsync();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            try {
                activity.finish();
                activity.overridePendingTransition(R.anim.capital_activity_fade_in, R.anim.capital_activity_fade_out);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
