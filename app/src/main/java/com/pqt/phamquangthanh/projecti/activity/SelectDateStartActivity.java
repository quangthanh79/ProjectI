package com.pqt.phamquangthanh.projecti.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pqt.phamquangthanh.projecti.R;

import java.util.Calendar;
import java.util.Date;

public class SelectDateStartActivity extends AppCompatActivity {

    private  static final String TAG = "CalendarActivity";
    private CalendarView mCalendarView;
    LinearLayout linearLayoutBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_select_date_start);
        Calendar calendar = Calendar.getInstance();
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
        mCalendarView.setMaxDate(calendar.getTimeInMillis());
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView CalendarView, int year, int month, int dayOfMonth) {
//                Toast.makeText(SelectDateStartActivity.this, dayOfMonth+"/"+month+"/"+year, Toast.LENGTH_SHORT).show();
                  Calendar calendar_start = Calendar.getInstance();
                  calendar_start.set(Calendar.YEAR,year);
                  calendar_start.set(Calendar.MONTH,month);
                  calendar_start.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                  Date date = calendar_start.getTime();

                  Intent return_intent_1= new Intent();
                  return_intent_1.putExtra("year",year);
                  return_intent_1.putExtra("month",month);
                  return_intent_1.putExtra("dayOfMonth",dayOfMonth);
                  setResult(Activity.RESULT_OK,return_intent_1);
                  finish();
            }
        });
        mapView();
        linearLayoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent return_intent_1= new Intent();
                setResult(Activity.RESULT_CANCELED,return_intent_1);
                finish();
            }
        });
    }
    private void mapView(){
        linearLayoutBack = (LinearLayout) findViewById(R.id.back);
    }
}