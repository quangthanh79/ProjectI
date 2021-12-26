package com.pqt.phamquangthanh.projecti.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pqt.phamquangthanh.projecti.R;
import com.pqt.phamquangthanh.projecti.model.TransactionGroup;

import java.util.Calendar;
import java.util.Date;

public class SelectDateSE_Activity extends AppCompatActivity implements View.OnClickListener{

    LinearLayout linearLayoutStart,linearLayoutEnd;
    TextView textViewStart,textViewEnd,txtOK;
    LinearLayout linearlayoutBack;
    long date_start,date_end;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_select_date_se);
        mapView();
        linearLayoutStart.setOnClickListener(this);
        linearLayoutEnd.setOnClickListener(this);
        txtOK.setOnClickListener(this);
        linearlayoutBack.setOnClickListener(this);
    }
    private void mapView(){
        linearLayoutEnd   = (LinearLayout) findViewById(R.id.linearLayoutEnd);
        linearLayoutStart = (LinearLayout) findViewById(R.id.linearLayoutStart);
        textViewStart     = (TextView) findViewById(R.id.textViewStart);
        textViewEnd       = (TextView) findViewById(R.id.textViewEnd);
        txtOK             = (TextView) findViewById(R.id.txtOK);
        linearlayoutBack  = (LinearLayout) findViewById(R.id.linearlayoutBack);
    }
    private ActivityResultLauncher<Intent> sactivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        int year       = data.getIntExtra("year",0);
                        int month      = data.getIntExtra("month",0);
                        int dayOfMonth = data.getIntExtra("dayOfMonth",0);
                        String day_start = dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth + "";
                        String form_date1 = day_start + "/"+(month+1) +"/"+year;
                        textViewStart.setText(form_date1);
                        Calendar calendar= Calendar.getInstance();
                        calendar.set(Calendar.YEAR,year);
                        calendar.set(Calendar.MONTH,month);
                        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        calendar.set(Calendar.HOUR_OF_DAY,0);
                        calendar.set(Calendar.MINUTE,0);
                        calendar.set(Calendar.SECOND,0);
                        date_start = calendar.getTimeInMillis();
                    }
                }
            });
    private ActivityResultLauncher<Intent> eactivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        int year       = data.getIntExtra("year",0);
                        int month      = data.getIntExtra("month",0);
                        int dayOfMonth = data.getIntExtra("dayOfMonth",0);
                        String day_end = dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth + "";
                        String form_date2 = day_end + "/"+(month+1) +"/"+year;
                        textViewEnd.setText(form_date2);
                        Calendar calendar= Calendar.getInstance();
                        calendar.set(Calendar.YEAR,year);
                        calendar.set(Calendar.MONTH,month);
                        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        calendar.set(Calendar.HOUR_OF_DAY,23);
                        calendar.set(Calendar.MINUTE,59);
                        calendar.set(Calendar.SECOND,59);
                        date_end = calendar.getTimeInMillis();
                    }
                }
            });

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.linearLayoutStart:
                Intent intent1 = new Intent(SelectDateSE_Activity.this,SelectDateStartActivity.class);
                sactivityResultLauncher.launch(intent1);
                break;
            case R.id.linearLayoutEnd:
                Intent intent2 = new Intent(SelectDateSE_Activity.this,SelectDateStartActivity.class);
                eactivityResultLauncher.launch(intent2);
                break;
            case R.id.txtOK:
                if(date_start >= date_end){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                    alertDialog.setTitle("Thông báo");
                    alertDialog.setMessage("Ngày kết thúc không thể trước ngày bắt đầu!");
                    alertDialog.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    alertDialog.show();
                }else{
                    Intent intent = new Intent();
                    intent.putExtra("start",date_start);
                    intent.putExtra("end",date_end);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }
                break;
            case R.id.linearlayoutBack:
                Intent intent_cancel = new Intent();
                setResult(Activity.RESULT_CANCELED,intent_cancel);
                finish();
                break;
        }
    }
}