package com.application.alphacapital.superapp.pms.activity;

import android.app.Activity;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.alphacapital.superapp.R;
import com.application.alphacapital.superapp.pms.beans.BSMovementResponseModel;
import com.application.alphacapital.superapp.pms.beans.BsYearsResponseModel;
import com.application.alphacapital.superapp.pms.network.PortfolioApiClient;
import com.application.alphacapital.superapp.pms.network.PortfolioApiInterface;
import com.application.alphacapital.superapp.pms.utils.ApiUtils;
import com.application.alphacapital.superapp.pms.utils.AppUtils;
import com.application.alphacapital.superapp.pms.utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimationType;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimator;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BSMovementActivity extends AppCompatActivity
{
    private Activity activity;

    private SessionManager sessionManager;

    private View llLoading, llNoInternet, llNoData;

    private LinearLayout llBack;

    private RecyclerView rvBsMovement;

    private ArrayList<BSMovementResponseModel.SheetDataItem> listData;

    private PortfolioApiInterface apiService;

    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.portfolio_activity_bs_movement);

        activity = BSMovementActivity.this;
        sessionManager = new SessionManager(activity);
        apiService = PortfolioApiClient.getClient().create(PortfolioApiInterface.class);

        try
        {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<BSMovementResponseModel.SheetDataItem>>() {}.getType();
            listData = gson.fromJson(getIntent().getStringExtra("data"), type);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        initViews();

        onClicks();
    }


    private void initViews()
    {
        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("BS Movement");
        ImageView imgPageIcon = findViewById(R.id.imgPageIcon);
        imgPageIcon.setImageResource(R.drawable.portfolio_ic_bs_movement_white);
        llBack = findViewById(R.id.llBack);
        llLoading = findViewById(R.id.llLoading);
        llNoInternet = findViewById(R.id.llNoInternet);
        llNoData = findViewById(R.id.llNoData);
        rvBsMovement = findViewById(R.id.rvBsMovement);
        rvBsMovement.setLayoutManager(new LinearLayoutManager(activity));
        if (listData != null && listData.size() > 0)
        {
            llNoData.setVisibility(View.GONE);
            BsMovementDataAdapter bsMovementDataAdapter = new BsMovementDataAdapter(listData);
            rvBsMovement.setAdapter(bsMovementDataAdapter);
        }
        else
        {
            llNoData.setVisibility(View.VISIBLE);
        }
    }

    private void onClicks()
    {
        llBack.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
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
        });
    }

    private class BsMovementDataAdapter extends RecyclerView.Adapter<BsMovementDataAdapter.ViewHolder>
    {
        ArrayList<BSMovementResponseModel.SheetDataItem> listItems;

        BsMovementDataAdapter(ArrayList<BSMovementResponseModel.SheetDataItem> list)
        {
            this.listItems = list;
        }

        @Override public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.portfolio_row_view_bs_movement_data, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            try
            {
                final BSMovementResponseModel.SheetDataItem getSet = listItems.get(position);

                if (position % 2 == 0)
                {
                    holder.llBsMain.setBackgroundResource(R.color.light_gray_new);
                }
                else
                {
                    holder.llBsMain.setBackgroundResource(R.color.white);
                }

                holder.txtBSMonthDate.setText(getSet.getTimestamp());
                holder.txtBsTotal.setText(getResources().getString(R.string.rupees) + " " + AppUtils.convertToTwoDecimalValue(String.valueOf(getSet.getTotal())));
            }
            catch (Resources.NotFoundException e)
            {
                e.printStackTrace();
            }
        }

        @Override public int getItemCount()
        {
            return listItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            private TextView txtBSMonthDate, txtBsTotal;
            private LinearLayout llBsMain;

            ViewHolder(View convertView)
            {
                super(convertView);
                txtBSMonthDate = convertView.findViewById(R.id.txtBSMonthDate);
                txtBsTotal = convertView.findViewById(R.id.txtBsTotal);
                llBsMain = convertView.findViewById(R.id.llBsMain);
            }
        }
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
