package com.pqt.phamquangthanh.projecti.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.pqt.phamquangthanh.projecti.R;
import com.pqt.phamquangthanh.projecti.activity.MainActivity;
import com.pqt.phamquangthanh.projecti.dialog.MonthYearPickerDialog;
import com.pqt.phamquangthanh.projecti.util.ConversionUtil;
import com.pqt.phamquangthanh.projecti.util.SQLiteUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class FollowFragment extends Fragment implements View.OnClickListener {
    LinearLayout get_follow_content,getTimeStart,getTimeEnd;
    MonthYearPickerDialog pd1,pd2;
    int month_start,year_start;
    TextView txtTimeStart,txtAmountMonth,txtfollow_content;
    Button btn_OK;
    BarChart barChart;
    int index;
    int type = 1,amountMonth=6;
    SQLiteUtil sqLiteUtil;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.follow_fragment, container, false);
        mapView(view);
        get_follow_content.setOnClickListener(this);
        getTimeStart.setOnClickListener(this);
        getTimeEnd.setOnClickListener(this);
        btn_OK.setOnClickListener(this);
        pd1 = new MonthYearPickerDialog();
        pd2 = new MonthYearPickerDialog();
        sqLiteUtil  = new SQLiteUtil(getActivity());
        pd1.setOnClickOK(new MonthYearPickerDialog.setOnClickOK() {
            @Override
            public void onClick(int month, int year) {
                pd1.dismiss();
                month_start = month;
                year_start  = year;
                txtTimeStart.setText("T"+month_start+"/"+year_start);
            }
        });


        return view;
    }
    private void mapView(View view){
        get_follow_content      = (LinearLayout) view.findViewById(R.id.get_follow_content);
        getTimeStart            = (LinearLayout) view.findViewById(R.id.getTimeStart);
        getTimeEnd              = (LinearLayout) view.findViewById(R.id.getTimeEnd);
        txtTimeStart            = (TextView) view.findViewById(R.id.txtTimeStart);
        txtAmountMonth          = (TextView) view.findViewById(R.id.txtAmountMonth);
        txtfollow_content       = (TextView) view.findViewById(R.id.txtfollow_content);
        btn_OK                  = (Button) view.findViewById(R.id.btn_OK);
        barChart           = (BarChart) view.findViewById(R.id.barchart);
    }
    private void check(){
        Calendar calendar = Calendar.getInstance();
        int month_now = calendar.get(Calendar.MONTH)+1;
        int year_now  = calendar.get(Calendar.YEAR);
        if(year_now == year_start && month_now < month_start){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setTitle("Thông báo");
            alertDialog.setMessage("Vui lòng chọn lại thời gian bắt đầu!");
            alertDialog.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            alertDialog.show();
        }else{
            drawChart();
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.get_follow_content:
                showAlertDialog1();
                break;
            case R.id.getTimeStart:
                pd1.show(getFragmentManager(), "MonthYearPickerDialog");
                break;
            case R.id.getTimeEnd:
                showAlertDialog2();
                break;
            case R.id.btn_OK:
                check();
                break;
        }
    };
    private void showAlertDialog1() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Bạn cần thông kê khoản nào?");
        String[] items = {"Khoản Thu","Khoản Chi"};
        alertDialog.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                index = i;
            }
        });
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                txtfollow_content.setText(items[index]);
                if(index == 0){
                    type =1;
                }else{
                    type =0;
                }
                Toast.makeText(getActivity(), type+"", Toast.LENGTH_SHORT).show();

            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alert = alertDialog.create();

        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }
    private void showAlertDialog2() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Số tháng cần thống kê");
        String[] items = {"3 tháng","6 tháng","9 tháng","12 tháng"};
        alertDialog.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                index = i;
            }
        });
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                txtAmountMonth.setText(items[index]);
                amountMonth = (index+1)*3;
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alert = alertDialog.create();

        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }
    private void drawChart(){
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        ArrayList<String> label = new ArrayList<String>();

        Calendar calendar = Calendar.getInstance();
        int month_now = calendar.get(Calendar.MONTH)+1;
        int year_now  = calendar.get(Calendar.YEAR);
        int count =1;

        int m = month_start;
        int y = year_start;

        while(count<= amountMonth){
            if(month_start == 12){
                entries.add(new BarEntry(count,sqLiteUtil.getTotalAmountInMonthFollowType(type,m,y)));
                String lb = "T" + m+"/"+y;
                label.add(lb);
                m = 1;
                y = year_start;
            }else{
                entries.add(new BarEntry(count,sqLiteUtil.getTotalAmountInMonthFollowType(type,m,y)));
                String lb = "T" + m;
                label.add(lb);
                m+=1;
            }
            if(y == year_now && m>month_now ){
                break;
            }
            count++;
        }

        YAxis rightYAxis = barChart.getAxisRight();
        rightYAxis.setEnabled(false);

        barChart.getXAxis().setDrawLabels(true);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setDrawAxisLine(true);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(label));
        barChart.animateY(500);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setCenterAxisLabels(true);
        barChart.getXAxis().setLabelCount(label.size());
        BarDataSet dataSet = new BarDataSet(entries,"");
        BarData data = new BarData(dataSet);
        if(type ==0 ) {
            dataSet.setColors(Color.rgb(204, 0, 0));
        }
        else{
            dataSet.setColors(Color.rgb(0, 205, 254));
        }
        barChart.setData(data);

        barChart.getDescription().setEnabled(false);



    }


}
