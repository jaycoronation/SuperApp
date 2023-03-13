package com.application.alphacapital.superapp.pms.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.application.alphacapital.superapp.R;
import com.application.alphacapital.superapp.pms.beans.BSMovementResponseModel;
import com.application.alphacapital.superapp.pms.beans.BSMovementResponseModel.GraphDataItem;
import com.application.alphacapital.superapp.pms.network.PortfolioApiClient;
import com.application.alphacapital.superapp.pms.network.PortfolioApiInterface;
import com.application.alphacapital.superapp.pms.utils.ApiUtils;
import com.application.alphacapital.superapp.pms.utils.AppUtils;
import com.application.alphacapital.superapp.pms.utils.MyMarkerView;
import com.application.alphacapital.superapp.pms.utils.SessionManager;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChartActivity extends AppCompatActivity
{
    private Activity activity;

    private SessionManager sessionManager;

    private View llLoading, llNoInternet, llNoData;

    private LinearLayout llBack,llTableData ;

    private LineChart lineChart;

    private ArrayList<BSMovementResponseModel.GraphDataItem> listGraphData = new ArrayList<>();

    private List<Entry> entryArrayList;
    LineDataSet dataSet;
    ArrayList<String> labelList;

    private PortfolioApiInterface apiService;

    private String strListTableData = "";

    public static Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.portfolio_activity_chart);
        activity = ChartActivity.this;
        sessionManager = new SessionManager(activity);
        apiService = PortfolioApiClient.getClient().create(PortfolioApiInterface.class);

        handler = new Handler(message -> {
            try {
                if (message.what == 100)
                {
                    listGraphData = (ArrayList<GraphDataItem>) message.obj;
                    getBSMovementData();
                    Log.e("ExampleJobServiceFF  : ", listGraphData.size() + " BS LIST DATA <><>");
                }
                else if (message.what == 111)
                {
                    strListTableData = (String) message.obj;
                    getBSMovementData();
                    Log.e("ExampleJobServiceFF  : ", strListTableData.length() + " BS TABLE DATA <><>");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return false;
        });
        initViews();
        onClicks();
    }


    private void initViews()
    {
        llBack = findViewById(R.id.llBack);
        llTableData = findViewById(R.id.llTableData);
        ImageView imgPageIcon = findViewById(R.id.imgPageIcon);
        imgPageIcon.setVisibility(View.GONE);
        llLoading = findViewById(R.id.llLoading);
        llNoInternet = findViewById(R.id.llNoInternet);
        llNoData = findViewById(R.id.llNoData);
        lineChart = findViewById(R.id.chart);

        Log.e("<><><>GRAPH LIST SIZE", String.valueOf(sessionManager.getBsMovementData().size()));

        if (sessionManager.getBsMovementData().size() > 0)
        {
            listGraphData = sessionManager.getBsMovementData();
            fillLineChartData();

            if (!sessionManager.getBsMovementTableData().isEmpty())
            {
                llTableData.setVisibility(View.VISIBLE);
                strListTableData = sessionManager.getBsMovementTableData();
            }
            else
            {
                llTableData.setVisibility(View.GONE);
                strListTableData = "";
            }
        }
        else
        {
            if (AppUtils.isOnline())
            {
                llNoInternet.setVisibility(View.GONE);
                getBSMovementData();
            }
            else
            {
                llNoInternet.setVisibility(View.VISIBLE);
            }
        }
    }

    private void getBSMovementData()
    {
        if (listGraphData.isEmpty())
        {
            try
            {
                llLoading.setVisibility(View.VISIBLE);
                listGraphData = new ArrayList<>();
                apiService.getBSMovementAPI(ApiUtils.END_USER_ID, "monthly").enqueue(new Callback<BSMovementResponseModel>()
                {
                    @Override public void onResponse(Call<BSMovementResponseModel> call, Response<BSMovementResponseModel> response)
                    {
                        if (response.isSuccessful())
                        {
                            if (response.body().getSuccess() == 1)
                            {
                                try
                                {
                                    if (response.body().getGraph_data().size() > 0)
                                    {
                                        listGraphData.addAll(response.body().getGraph_data());
                                        sessionManager.saveBsMovementList(listGraphData);
                                        fillLineChartData();
                                    }

                                    if (response.body().getSheet_data().size() > 0)
                                    {
                                        llTableData.setVisibility(View.VISIBLE);
                                        Gson gson = new Gson();
                                        strListTableData = gson.toJson(response.body().getSheet_data());
                                        sessionManager.saveBsMovementTableData(strListTableData);
                                    }
                                    else
                                    {
                                        llTableData.setVisibility(View.GONE);
                                    }

                                    llLoading.setVisibility(View.GONE);
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                llNoData.setVisibility(View.VISIBLE);
                                llLoading.setVisibility(View.GONE);
                            }
                        }
                        else
                        {
                            llNoData.setVisibility(View.VISIBLE);
                            llLoading.setVisibility(View.GONE);
                        }

                    }

                    @Override public void onFailure(Call<BSMovementResponseModel> call, Throwable t)
                    {
                        AppUtils.printLog("FALi", t.getMessage());
                        llNoData.setVisibility(View.VISIBLE);
                        llLoading.setVisibility(View.GONE);
                    }
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            fillLineChartData();

            if (!strListTableData.isEmpty())
            {
                llTableData.setVisibility(View.VISIBLE);
            }
            else
            {
                llTableData.setVisibility(View.GONE);
            }
        }
    }

    private void onClicks()
    {
        llBack.setOnClickListener(v -> {
            try
            {
                finish();
                AppUtils.finishActivityAnimation(activity);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        llTableData.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View view)
            {
                Intent intent = new Intent(activity, BSMovementActivity.class);
                intent.putExtra("data", strListTableData);
                startActivity(intent);
                AppUtils.startActivityAnimation(activity);
            }
        });
    }

    int[] colors;
    private void fillLineChartData()
    {
        try
        {
            colors = new int[]{ContextCompat.getColor(activity, R.color.portfolio_colorPrimaryDark), ContextCompat.getColor(activity, R.color.portfolio_colorAccent)};
            entryArrayList = new ArrayList<>();
            for (int i = 0; i < listGraphData.size(); i++)
            {
                float value = 0;
                try
                {
                    value = Float.parseFloat(String.valueOf(listGraphData.get(i).getTotal()));
                }
                catch (NumberFormatException e)
                {
                    e.printStackTrace();
                }
                entryArrayList.add(new Entry(i, value));
            }

            dataSet = new LineDataSet(entryArrayList, "");

            XAxis xAxis = lineChart.getXAxis();
            xAxis.setValueFormatter(new MyValueFormatterForAxis());

            ArrayList<ILineDataSet> lines = new ArrayList<ILineDataSet>();
            lines.add(dataSet);

            for (ILineDataSet iSet : lines)
            {
                LineDataSet set = (LineDataSet) iSet;
                set.setDrawValues(true);
            }

            final LineData data = new LineData(lines);
            data.setValueFormatter(new MyValueFormatter());
            lineChart.setData(data);

            dataSet.setDrawFilled(true);
            dataSet.setColor(ContextCompat.getColor(activity, R.color.portfolio_colorPrimaryDark)); // Line color
            dataSet.setCircleColor(ContextCompat.getColor(activity, R.color.portfolio_colorPrimaryDark));
            dataSet.setCircleRadius(3f);
            dataSet.setDrawHighlightIndicators(true); // HighLight line indicator
            dataSet.setDrawCircleHole(false); // circle inside point
            dataSet.setHighLightColor(ContextCompat.getColor(activity, R.color.portfolio_colorPrimaryDark)); // Color of HighLight line
            dataSet.setValueTextColor(ContextCompat.getColor(activity, R.color.portfolio_colorPrimaryDark)); // HighLight label color
            dataSet.setDrawCircles(true); //Remove circle
            dataSet.setDrawValues(false); //display value on point

            /*For getting X Axis and Y Axis*/
            XAxis x = lineChart.getXAxis();
            YAxis y = lineChart.getAxisLeft();
            YAxis yRight = lineChart.getAxisRight();

            x.setTextColor(ContextCompat.getColor(activity, R.color.portfolio_colorPrimaryDark));
            y.setTextColor(ContextCompat.getColor(activity, R.color.portfolio_colorPrimaryDark));

            x.setDrawGridLines(true); // Hide gridLines for X Axis
            y.setDrawGridLines(true); // Hide gridLines for Y Axis
            x.setTypeface(Typeface.createFromAsset(activity.getAssets(), AppUtils.mediumFonts));

            x.setPosition(XAxis.XAxisPosition.BOTTOM);
            y.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
            yRight.setEnabled(false);

            y.setStartAtZero(false);
            y.setTypeface(Typeface.createFromAsset(activity.getAssets(), AppUtils.mediumFonts));

            // enable scaling and dragging
            lineChart.setDragEnabled(true);
            lineChart.setScaleEnabled(true);
            lineChart.setPinchZoom(true);
            lineChart.setTouchEnabled(true);
            lineChart.setAutoScaleMinMaxEnabled(true);
            lineChart.getLegend().setEnabled(false);
            lineChart.animateX(2500);
            dataSet.setDrawFilled(true);  // fill below color to line or not
            dataSet.setFillColor(ContextCompat.getColor(activity, R.color.portfolio_colorAccent));  // Fill color below the line
            dataSet.setLineWidth(3); // line width

            Description description = new Description();
            description.setText("");
            lineChart.setTouchEnabled(true);
            lineChart.setDescription(description);
            // enable touch gestures

            // enable scaling and dragging
            lineChart.setDragEnabled(true);
            lineChart.setScaleEnabled(true);

            MyMarkerView mv = new MyMarkerView(activity, R.layout.portfolio_custom_marker_view);
            mv.setChartView(lineChart); // For bounds control
            lineChart.setMarker(mv); // Set the marker to the chart
            lineChart.notifyDataSetChanged();
            lineChart.invalidate();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public class MyValueFormatterForAxis implements IAxisValueFormatter
    {
        private DecimalFormat mFormat;

        @Override
        public String getFormattedValue(float value, AxisBase axis)
        {
            // write your logic here
            return listGraphData.get((int) value).getTimestamp(); // e.g. append a dollar-sign
        }
    }

    public class MyValueFormatter implements IValueFormatter
    {
        private DecimalFormat mFormat;

        public MyValueFormatter()
        {
            mFormat = new DecimalFormat("##,###,##0.00"); // use one decimal
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler)
        {
            // write your logic here
            return mFormat.format(value); // e.g. append a dollar-sign
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
