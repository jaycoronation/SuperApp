package com.application.alphacapital.superapp.pms.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.application.alphacapital.superapp.R;
import com.application.alphacapital.superapp.pms.beans.UserListResponseModel;
import com.application.alphacapital.superapp.pms.network.PortfolioApiClient;
import com.application.alphacapital.superapp.pms.network.PortfolioApiInterface;
import com.application.alphacapital.superapp.pms.utils.ApiUtils;
import com.application.alphacapital.superapp.pms.utils.AppUtils;
import com.application.alphacapital.superapp.pms.utils.EndlessRecyclerViewScrollListener;
import com.application.alphacapital.superapp.pms.utils.SessionManager;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimationType;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllUsersActivity extends AppCompatActivity
{
    private Activity activity ;

    private SessionManager sessionManager ;

    private LinearLayout llSearch,llBack,llLogo ;

    private EditText edtSearch;

    private View llLoading,llNoInternet,llNoData ;

    private RecyclerView rvAllUsers ;

    private ArrayList<UserListResponseModel.UsersItem> listAllUsers ;

    private AllUsersAdapter allUsersAdapter ;

    private PortfolioApiInterface apiService;

    //paging
    private int pageIndex = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    private SwipeRefreshLayout swipeView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.protfolio_activity_all_users);

        activity = AllUsersActivity.this ;
        sessionManager = new SessionManager(activity);
        apiService = PortfolioApiClient.getClient().create(PortfolioApiInterface.class);
        initViews();

        if(AppUtils.isOnline())
        {
            llNoInternet.setVisibility(View.GONE);
            getAllUsers(true,"",false);
        }
        else
        {
            llNoInternet.setVisibility(View.VISIBLE);
        }
        setStatusBarGradiant(activity);
        onClicks();
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

    private void initViews()
    {
        llSearch = findViewById(R.id.llSearch);
        llSearch.setVisibility(View.VISIBLE);
        llBack = findViewById(R.id.llBack);
        llLogo = findViewById(R.id.llLogo);
        edtSearch = findViewById(R.id.edtSearch);

        ImageView imgPageIcon = findViewById(R.id.imgPageIcon);
        imgPageIcon.setVisibility(View.GONE);

        llLoading = findViewById(R.id.llLoading);
        llNoInternet = findViewById(R.id.llNoInternet);
        llNoData = findViewById(R.id.llNoData);

        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipeView);
        swipeView.setColorSchemeColors(ContextCompat.getColor(activity,R.color.portfolio_colorPrimary) , ContextCompat.getColor(activity,R.color.portfolio_colorPrimaryDark),
                ContextCompat.getColor(activity,R.color.portfolio_colorPrimaryDark), ContextCompat.getColor(activity,R.color.portfolio_colorAccent));
        swipeView.setDistanceToTriggerSync(20);// in dips
        swipeView.setSize(SwipeRefreshLayout.DEFAULT);

        rvAllUsers = findViewById(R.id.rvAllUsers);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        rvAllUsers.setLayoutManager(linearLayoutManager);

        rvAllUsers.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager)
        {
            @Override
            public void onLoadMore(int page, int totalItemsCount)
            {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                try
                {
                    if(!isLoading && !isLastPage)
                    {
                        if(AppUtils.isOnline())
                        {
                            getAllUsers(false,"",true);
                        }
                        else
                        {
                            AppUtils.showSnackBar(rvAllUsers,"No internet connection!",activity);
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    private void onClicks()
    {
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                if (AppUtils.isOnline())
                {
                    pageIndex = 1 ;
                    llNoInternet.setVisibility(View.GONE);
                    swipeView.setRefreshing(true);
                    getAllUsers(true,"",false);
                }
                else
                {
                    llNoInternet.setVisibility(View.VISIBLE);
                }
            }
        });

        llBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                edtSearch.setText("");
                edtSearch.setHint("Search the user...");
                edtSearch.setVisibility(View.GONE);
                llSearch.setVisibility(View.VISIBLE);
                llLogo.setVisibility(View.VISIBLE);
                AppUtils.hideKeyboard(edtSearch, activity);
            }
        });

        llSearch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (edtSearch.getVisibility() == View.VISIBLE)
                {
                    pageIndex = 1 ;
                    AppUtils.hideKeyboard(edtSearch, activity);
                    getAllUsers(true,edtSearch.getText().toString().trim(),true);
                    edtSearch.setVisibility(View.GONE);
                    edtSearch.setText("");
                    edtSearch.setHint("Search the user...");
                    llSearch.setVisibility(View.VISIBLE);
                    llLogo.setVisibility(View.VISIBLE);
                }
                else
                {
                    edtSearch.setVisibility(View.VISIBLE);
                    llSearch.setVisibility(View.VISIBLE);
                    llLogo.setVisibility(View.GONE);
                    edtSearch.requestFocus();
                    AppUtils.showKeyboard(edtSearch, activity);
                }
            }
        });
    }

    /**
     * Call on server to get the all registered user list
     * @param isFirstTime pass flag to method data is loading first time or load on scroll
     * @param strSearch pass search string if search operation is perform by user
     * @param isSearch pass flag true|false if user perform search operation or not
     */
    private void getAllUsers(final boolean isFirstTime,final String strSearch,final boolean isSearch)
    {
        try
        {
            isLoading = true;

            if (swipeView.isRefreshing())
            {
                llLoading.setVisibility(View.GONE);
                listAllUsers = new ArrayList<>();
            }
            else
            {
                if(isFirstTime)
                {
                    llLoading.setVisibility(View.VISIBLE);
                    listAllUsers = new ArrayList<>();
                }
            }
            apiService.getAllUserAPI(strSearch,sessionManager.getAdminId()).enqueue(new Callback<UserListResponseModel>() {
                @Override
                public void onResponse(Call<UserListResponseModel> call, Response<UserListResponseModel> response) {
                    if (response.isSuccessful())
                    {
                        if (response.body().getSuccess() == 1)
                        {
                            isLoading = false;
                            swipeView.setRefreshing(false);

                            if(isFirstTime)
                            {
                                listAllUsers.addAll(response.body().getUsers());
                                llLoading.setVisibility(View.GONE);
                                if(listAllUsers != null && listAllUsers.size() > 0)
                                {
                                    llNoData.setVisibility(View.GONE);

                                    allUsersAdapter = new AllUsersAdapter(listAllUsers);
                                    rvAllUsers.setAdapter(allUsersAdapter);
                                }
                                else
                                {
                                    llNoData.setVisibility(View.VISIBLE);
                                }
                            }
                            else
                            {
                                if(allUsersAdapter == null)
                                {
                                    return;
                                }

                                allUsersAdapter.notifyDataSetChanged();
                            }
                        }
                        else
                        {
                            AppUtils.showSnackBar(llLoading,"No Data Found",activity);
                        }
                    }
                    else
                    {
                        AppUtils.showSnackBar(llLoading,"No Data Found",activity);
                    }
                }

                @Override
                public void onFailure(Call<UserListResponseModel> call, Throwable t) {

                }
            });

            /*new AsyncTask<Void, Void, Void>()
            {
                int status = -1;

                @Override
                protected void onPreExecute()
                {
                    super.onPreExecute();


                }

                @Override
                protected Void doInBackground(Void... voids)
                {
                    try
                    {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("search_string", strSearch);
                        hashMap.put("admin_id", sessionManager.getAdminId());

                        if(!isSearch)
                        {
                            hashMap.put("page", String.valueOf(pageIndex));
                            hashMap.put("limit", String.valueOf(30));
                        }

                        AppUtils.printLog("Params",hashMap + "");

                        String serverResponse = MitsUtils.readJSONServiceUsingPOST(ApiUtils.GET_ALL_USERS, hashMap);

                        AppUtils.printLog("AllUsers",serverResponse);

                        JSONObject jsonObject = new JSONObject(serverResponse);

                        status = AppUtils.getValidAPIIntegerResponse(jsonObject.optString("success"));

                        if (status == 1)
                        {
                            pageIndex = pageIndex + 1;

                            JSONArray userArray = jsonObject.optJSONArray("users");

                            if(userArray.length() == 0 || userArray.length() % 30 != 0)
                            {
                                isLastPage = true;
                            }

                            for (int i = 0; i < userArray.length(); i++)
                            {
                                JSONObject userObj =  userArray.getJSONObject(i);

                                UserBean userBean = new UserBean();

                                userBean.setFirst_name(AppUtils.getValidAPIStringResponse(userObj.optString("first_name")));
                                userBean.setLast_name(AppUtils.getValidAPIStringResponse(userObj.optString("last_name")));
                                userBean.setUser_name(AppUtils.getValidAPIStringResponse(userObj.optString("user_name")));
                                userBean.setUser_id(AppUtils.getValidAPIStringResponse(userObj.optString("user_id")));
                                userBean.setEmail(AppUtils.getValidAPIStringResponse(userObj.optString("email")));

                                listAllUsers.add(userBean);
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid)
                {
                    super.onPostExecute(aVoid);


                }
            }.execute();*/
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private class AllUsersAdapter extends RecyclerView.Adapter<AllUsersAdapter.ViewHolder>
    {
        ArrayList<UserListResponseModel.UsersItem> listItems;
        private SpringyAdapterAnimator mAnimator;

        AllUsersAdapter(ArrayList<UserListResponseModel.UsersItem> list)
        {
            this.listItems = list;
            mAnimator = new SpringyAdapterAnimator(rvAllUsers);
            mAnimator.setSpringAnimationType(SpringyAdapterAnimationType.SLIDE_FROM_BOTTOM);
            mAnimator.addConfig(85, 15);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.portfolio_row_view_all_users, viewGroup, false);
            mAnimator.onSpringItemCreate(v);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            try
            {
                final UserListResponseModel.UsersItem getSet = listItems.get(position);

                if (position % 2 == 0)
                {
                    holder.llMainUsers.setBackgroundResource(R.drawable.portfolio_cool_blue_gradient);
                }
                else
                {
                    holder.llMainUsers.setBackgroundResource(R.drawable.portfolio_white_gradient);
                }

                holder.txtName.setText(AppUtils.toDisplayCase(getSet.getFirst_name()) + " " + AppUtils.toDisplayCase(getSet.getLast_name()));
                holder.txtUsername.setText(AppUtils.toDisplayCase(getSet.getUser_name()));

                if(getSet.getEmail() != null && getSet.getEmail().trim().length() > 0)
                {
                    holder.llEmail.setVisibility(View.VISIBLE);
                    holder.txtEmail.setText(getSet.getEmail());
                }
                else
                {
                    holder.llEmail.setVisibility(View.GONE);
                }

                mAnimator.onSpringItemBind(holder.itemView, position);

                holder.llView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        ApiUtils.END_USER_ID = getSet.getUser_id();
                        Intent intent = new Intent(activity,OneDayChangeActivity.class);
                        startActivity(intent);
                        AppUtils.startActivityAnimation(activity);
                    }
                });
            }
            catch (Resources.NotFoundException e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount()
        {
            return listItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            private TextView txtUsername, txtName, txtEmail;
            private LinearLayout llView,llEmail,llMainUsers;

            ViewHolder(View convertView)
            {
                super(convertView);
                txtUsername = convertView.findViewById(R.id.txtUsername);
                txtName = convertView.findViewById(R.id.txtName);
                txtEmail = convertView.findViewById(R.id.txtEmail);

                llView = convertView.findViewById(R.id.llView);
                llEmail = convertView.findViewById(R.id.llEmail);
                llMainUsers = convertView.findViewById(R.id.llMainUsers);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
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
