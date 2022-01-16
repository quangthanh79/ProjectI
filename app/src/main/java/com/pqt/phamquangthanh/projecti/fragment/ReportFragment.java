package com.pqt.phamquangthanh.projecti.fragment;

import static com.github.mikephil.charting.data.PieDataSet.*;

import android.app.Activity;
//import android.app.Fragment;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.fonts.Font;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.pqt.phamquangthanh.projecti.R;
import com.pqt.phamquangthanh.projecti.activity.MainActivity;
import com.pqt.phamquangthanh.projecti.activity.SelectDateSE_Activity;
import com.pqt.phamquangthanh.projecti.model.Transaction;
import com.pqt.phamquangthanh.projecti.util.ConversionUtil;
import com.pqt.phamquangthanh.projecti.util.DateUtil;
import com.pqt.phamquangthanh.projecti.util.SQLiteUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ReportFragment extends Fragment {
    PieChart pieChart1,pieChart2;
    LinearLayout getInRange;
    public static TextView txtInRange,begin_money_key,end_money_key;
    TextView money_out,money_in;
    // ngày đầu, ngày cuối
    public long Day_Start,Day_End;
    public long amount_in,amount_out;
    SQLiteUtil sqLiteUtil;
    public void setDay_Start(long day_Start) {
        Day_Start = day_Start;
    }

    public void setDay_End(long day_End) {
        Day_End = day_End;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.report_fragment, container, false);
        sqLiteUtil  = new SQLiteUtil(getActivity());
        mapView(view);
        updateTime();
        fetchChart();
        getInRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemCLickListener.onClick();
            }
        });


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
//        Log.i("fragmentReport", "fragmentReport: onCreate");
        super.onCreate(savedInstanceState);
    }
    public void updateTime(){
        Date firstDay,lastDay;
        firstDay = ConversionUtil.timestampToDate(Day_Start);
        lastDay = ConversionUtil.timestampToDate(Day_End);

        this.txtInRange.setText(DateUtil.formatDateBaseOnCustom(firstDay,lastDay));

    }
    private void mapView(View view){
        pieChart1 = view.findViewById(R.id.piechart1);
        pieChart2 = view.findViewById(R.id.piechart2);
        getInRange = view.findViewById(R.id.getInRange);
        txtInRange = view.findViewById(R.id.txtInRange);
        begin_money_key = view.findViewById(R.id.begin_money_key);
        end_money_key   = view.findViewById(R.id.end_money_key);
        money_in = view.findViewById(R.id.money_in);
        money_out = view.findViewById(R.id.money_out);
    }
    public void fetchChart(){
        Date firstDay,lastDay;
        firstDay = ConversionUtil.timestampToDate(Day_Start);
        lastDay = ConversionUtil.timestampToDate(Day_End);
        long firstDayMoney = sqLiteUtil.getMoneyAmountInSpecificDay(ConversionUtil.timestampToDate(DateUtil.getStartDayTime(firstDay)));
        long lastDayMoney = sqLiteUtil.getMoneyAmountInSpecificDay(ConversionUtil.timestampToDate(DateUtil.getEndDayTime(lastDay)));
        begin_money_key.setText(ConversionUtil.longToString(firstDayMoney));
        end_money_key.setText(ConversionUtil.longToString(lastDayMoney));
        drawChart(pieChart1,1);
        drawChart(pieChart2,0);

    }
    private void drawChart(PieChart pieChart,int type) {
        PieDataSet pieDataSet = new PieDataSet(getData(type),"");
        pieDataSet.setDrawIcons(true);
        pieDataSet.setSliceSpace(1f);// space giữa các phần
        pieDataSet.setDrawValues(false);
        pieDataSet.setSelectionShift(2f);
        int [] color={ Color.rgb(100,221,23), Color.rgb(128,0,128), Color.rgb(255,136,0),
                Color.rgb(255,0,0), Color.rgb(255,127,80), Color.rgb(47,95,255)
        };
        pieDataSet.setColors(color);
        PieData pieData = new PieData(pieDataSet);
        pieChart.getDescription().setEnabled(false);
        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setTextSize(8f);
        l.setEnabled(true);

        l.setDrawInside(false);

        pieChart.setData(pieData);
        pieChart.invalidate();
        pieChart.setDrawEntryLabels(false);//thêm label
        pieChart.setHoleRadius(35f);
        pieChart.setDrawHoleEnabled(true);

    }
    private ArrayList<PieEntry> getData(int type){
        HashMap<Integer, Long> hashmap = new HashMap<Integer, Long> ();
        Date firstDay,lastDay;
        firstDay = ConversionUtil.timestampToDate(Day_Start);
        lastDay = ConversionUtil.timestampToDate(Day_End);
        if(type == 0){
            hashmap = sqLiteUtil.getTransactionInRangeFollowGroupId(firstDay,lastDay,0);
        }else{
            hashmap = sqLiteUtil.getTransactionInRangeFollowGroupId(firstDay,lastDay,1);
        }

        ArrayList<PieEntry> entries = new ArrayList<>();
        if(type == 0){
            amount_out =0;
        }
        else{
            amount_in =0;
        }


        hashmap.forEach((id,amount) -> {
            long amount_new = amount;
            if(amount <0 ){
                amount_new = 0 -amount;
                amount_out += amount;
            }else{
                amount_in += amount;
            }
            entries.add(new PieEntry((float) amount_new, sqLiteUtil.getGroupNameById(id)));
        });
        money_in.setText(ConversionUtil.longToString(amount_in));
        money_out.setText(ConversionUtil.longToString(amount_out));

        return entries;
    }

    public interface OnItemCLickListener{
        void onClick();
    }
    public TransactionFragment.OnItemCLickListener onItemCLickListener;

    public void setOnItemClickListener(TransactionFragment.OnItemCLickListener onItemCLickListener){
        this.onItemCLickListener = onItemCLickListener;
    }

}
