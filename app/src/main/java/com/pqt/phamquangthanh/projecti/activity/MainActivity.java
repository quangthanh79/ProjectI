package com.pqt.phamquangthanh.projecti.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.Fragment;

import android.app.Activity;
//import androidx.fragment.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.FragmentManager;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.pqt.phamquangthanh.projecti.R;
import com.pqt.phamquangthanh.projecti.fragment.FollowFragment;
import com.pqt.phamquangthanh.projecti.fragment.MoreFragment;
import com.pqt.phamquangthanh.projecti.fragment.ReportFragment;
import com.pqt.phamquangthanh.projecti.fragment.TransactionFragment;
import com.pqt.phamquangthanh.projecti.fragment.TransactionFragment;
import com.pqt.phamquangthanh.projecti.model.TransactionGroup;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {


    FragmentManager fm = getFragmentManager();
    TransactionFragment transactionFragment = new TransactionFragment();
    ReportFragment reportFragment = new ReportFragment();
    FollowFragment followFragment = new FollowFragment();
    MoreFragment moreFragment = new MoreFragment();
    Fragment activeFragment;
    TextView txtGetTime,txtGetDate,txtGetWeek,txtGetMonth,txtGetQuarter,txtGetYear,txtGetAll,txtGetCustom,txtCancel;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mapView();
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        fm.beginTransaction().add(R.id.frag_container,transactionFragment,"1").commit();
        fm.beginTransaction().add(R.id.frag_container,reportFragment,"2").hide(reportFragment).commit();
        fm.beginTransaction().add(R.id.frag_container,followFragment,"3").hide(followFragment).commit();
        fm.beginTransaction().add(R.id.frag_container,moreFragment,"4").hide(moreFragment).commit();
        activeFragment = transactionFragment;
        transactionFragmentUnit();
        reportFragmentUnit();


    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch(menuItem.getItemId()){
                case R.id.nav_action_transaction:
                    fm.beginTransaction().hide(activeFragment).show(transactionFragment).commit();
                    activeFragment = transactionFragment;
                    return true;
                case R.id.nav_action_report:
                    fm.beginTransaction().hide(activeFragment).show(reportFragment).commit();
                    activeFragment = reportFragment;
                    return true;
                case R.id.nav_action_follow:
                    fm.beginTransaction().hide(activeFragment).show(followFragment).commit();
                    activeFragment = followFragment;
                    return true;
                case R.id.nav_action_bonus:
                    fm.beginTransaction().hide(activeFragment).show(moreFragment).commit();
                    activeFragment = moreFragment;
                    return true;

            }

            return false;
        }
    };
    private ActivityResultLauncher<Intent> mactivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        transactionFragment.setType("");
                        long date_start = data.getLongExtra("start",0);
                        long date_end   = data.getLongExtra("end",0);
                        transactionFragment.setDay_Start(date_start);
                        transactionFragment.setDay_End(date_end);
                        transactionFragment.updatePageTitle();
                        transactionFragment.fetchAndFillData();

                    }
                }
            });
    private ActivityResultLauncher<Intent> nactivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        long date_start = data.getLongExtra("start",0);
                        long date_end   = data.getLongExtra("end",0);
                        reportFragment.setDay_Start(date_start);
                        reportFragment.setDay_End(date_end);
                        reportFragment.updateTime();
                        reportFragment.fetchChart();
                    }
                }
            });
    private void mapView(){
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
    }
    private void reportFragmentUnit(){
        reportFragment.setOnItemClickListener(new TransactionFragment.OnItemCLickListener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(MainActivity.this,SelectDateSE_Activity.class);
                nactivityResultLauncher.launch(intent);
            }
        });
    }
    private void transactionFragmentUnit(){
        transactionFragment.setOnItemClickListener(new TransactionFragment.OnItemCLickListener() {
            @Override
            public void onClick() {

                View viewDialog = getLayoutInflater().inflate(R.layout.layout_bottom_sheet,null);
                View viewDialogGetTime = getLayoutInflater().inflate(R.layout.bottom_sheet_get_time,null);

                txtGetTime    = viewDialog.findViewById(R.id.getTime);
                txtGetDate    = viewDialogGetTime.findViewById(R.id.getDate);
                txtGetWeek    = viewDialogGetTime.findViewById(R.id.getWeek);
                txtGetMonth   = viewDialogGetTime.findViewById(R.id.getMonth);
                txtGetQuarter = viewDialogGetTime.findViewById(R.id.getQuarter);
                txtGetYear    = viewDialogGetTime.findViewById(R.id.getYear);
                txtGetAll     = viewDialogGetTime.findViewById(R.id.getAll);
                txtGetCustom  = viewDialogGetTime.findViewById(R.id.getCustom);
                txtCancel     = viewDialogGetTime.findViewById(R.id.Cancel);

                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this,R.style.SheetDialog);
                final BottomSheetDialog bottomSheetDialogGetTime = new BottomSheetDialog(MainActivity.this,R.style.SheetDialog);

                bottomSheetDialog.setContentView(viewDialog);
                bottomSheetDialogGetTime.setContentView(viewDialogGetTime);



                bottomSheetDialog.show();
                txtGetCustom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialogGetTime.dismiss();
                        Intent intent = new Intent(MainActivity.this,SelectDateSE_Activity.class);
                        mactivityResultLauncher.launch(intent);
                    }
                });
                txtGetDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        transactionFragment.setType("date");
                        transactionFragment.setCurrentDate(Calendar.getInstance().getTime());
                        transactionFragment.updatePageTitle();
                        transactionFragment.fetchAndFillData();
                        bottomSheetDialogGetTime.dismiss();
                    }
                });
                txtGetWeek.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        transactionFragment.setType("week");
                        transactionFragment.setCurrentDate(Calendar.getInstance().getTime());
                        transactionFragment.updatePageTitle();
                        transactionFragment.fetchAndFillData();
                        bottomSheetDialogGetTime.dismiss();
                    }
                });
                txtGetMonth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        transactionFragment.setType("month");
                        transactionFragment.setCurrentDate(Calendar.getInstance().getTime());
                        transactionFragment.updatePageTitle();
                        transactionFragment.fetchAndFillData();
                        bottomSheetDialogGetTime.dismiss();
                    }
                });
                txtGetQuarter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        transactionFragment.setType("quarter");
                        transactionFragment.setCurrentDate(Calendar.getInstance().getTime());
                        transactionFragment.updatePageTitle();
                        transactionFragment.fetchAndFillData();
                        bottomSheetDialogGetTime.dismiss();
                    }
                });
                txtGetYear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        transactionFragment.setType("year");
                        transactionFragment.setCurrentDate(Calendar.getInstance().getTime());
                        transactionFragment.updatePageTitle();
                        transactionFragment.fetchAndFillData();
                        bottomSheetDialogGetTime.dismiss();
                    }
                });

                txtCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialogGetTime.dismiss();
                    }
                });
                txtGetTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                        bottomSheetDialogGetTime.show();
                    }
                });
            }
        });
        transactionFragment.setOnTransactionFragmentClickListener(new TransactionFragment.OnTransactionFragmentClickListener() {
            @Override
            public void OnTransactionFragmentClick() {
                fm.beginTransaction().hide(activeFragment).show(reportFragment).commit();
                activeFragment = reportFragment;
                reportFragment.setDay_Start(transactionFragment.getDay_Start());
                reportFragment.setDay_End(transactionFragment.getDay_End());
                reportFragment.updateTime();
                reportFragment.fetchChart();
                bottomNavigationView.setSelectedItemId(R.id.nav_action_report);
            }
        });
    }

}