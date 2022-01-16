package com.pqt.phamquangthanh.projecti.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.Fragment;

import android.app.Activity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
//import android.app.FragmentManager;
//import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.pqt.phamquangthanh.projecti.R;
import com.pqt.phamquangthanh.projecti.fragment.ChangePasswordFragment;
import com.pqt.phamquangthanh.projecti.fragment.FollowFragment;
import com.pqt.phamquangthanh.projecti.fragment.MoreFragment;
import com.pqt.phamquangthanh.projecti.fragment.ReportFragment;
import com.pqt.phamquangthanh.projecti.fragment.TransactionFragment;
import com.pqt.phamquangthanh.projecti.fragment.TransactionFragment;
import com.pqt.phamquangthanh.projecti.model.TransactionGroup;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


//    FragmentManager fm = getFragmentManager();
    FragmentManager fm = getSupportFragmentManager();

    TransactionFragment transactionFragment = new TransactionFragment();
    public static ReportFragment reportFragment = new ReportFragment();
    FollowFragment followFragment = new FollowFragment();
    MoreFragment moreFragment = new MoreFragment();
    ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
    Fragment activeFragment;
    TextView txtGetTime,txtGetDate,txtGetWeek,txtGetMonth,txtGetQuarter,txtGetYear,txtGetAll,txtGetCustom,txtCancel;
    BottomNavigationView bottomNavigationView;
    private final static String CHANNEL_ID_1 = "NOTIFICACION1";
    private final static String CHANNEL_ID_2 = "NOTIFICACION1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mapView();

        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        fm.beginTransaction().add(R.id.frag_container,transactionFragment,"1").commit();
//        fm.beginTransaction().add(R.id.frag_container,reportFragment,"2").hide(reportFragment).commit();
//        fm.beginTransaction().add(R.id.frag_container,followFragment,"3").hide(followFragment).commit();
//        fm.beginTransaction().add(R.id.frag_container,moreFragment,"4").hide(moreFragment).commit();


        activeFragment = transactionFragment;
        transactionFragmentUnit();
        reportFragmentUnit();
        moreFragmentUnit();
        changePasswordUnit();

    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch(menuItem.getItemId()){
                case R.id.nav_action_transaction:
                    fm.beginTransaction().replace(R.id.frag_container,transactionFragment)
                                         .addToBackStack(null)
                                         .commit();


//                    fm.beginTransaction().hide(activeFragment).show(transactionFragment).commit();
//                    activeFragment = transactionFragment;


                    return true;
                case R.id.nav_action_report:
                    fm.beginTransaction().replace(R.id.frag_container,reportFragment)
                                         .addToBackStack(null)
                                         .commit();


//                    fm.beginTransaction().hide(activeFragment).show(reportFragment).commit();
//                    activeFragment = reportFragment;


                    return true;
                case R.id.nav_action_follow:
                    fm.beginTransaction().replace(R.id.frag_container,followFragment)
                                         .addToBackStack(null)
                                         .commit();


//                    fm.beginTransaction().hide(activeFragment).show(followFragment).commit();
//                    activeFragment = followFragment;


                    return true;
                case R.id.nav_action_bonus:
                    fm.beginTransaction().replace(R.id.frag_container,moreFragment)
                                         .addToBackStack(null)
                                         .commit();


//                    fm.beginTransaction().hide(activeFragment).show(moreFragment).commit();
//                    activeFragment = moreFragment;


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
                        transactionFragment.fetchWarning();
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
    private int getNotificationId(){
        return (int) new Date().getTime();
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
                        transactionFragment.fetchWarning();
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
                        transactionFragment.fetchWarning();
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
                        transactionFragment.fetchWarning();
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
                        transactionFragment.fetchWarning();
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
                        transactionFragment.fetchWarning();
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

//                fm.beginTransaction().hide(activeFragment).show(reportFragment).commit();
//                activeFragment = reportFragment;
                reportFragment.setDay_Start(transactionFragment.getDay_Start());
                reportFragment.setDay_End(transactionFragment.getDay_End());

                fm.beginTransaction().replace(R.id.frag_container,reportFragment)
                        .addToBackStack(null)
                        .commit();

                bottomNavigationView.setSelectedItemId(R.id.nav_action_report);
            }
        });
        transactionFragment.setOnFetchNotification(new TransactionFragment.OnFetchNotification() {
            @Override
            public void createNotificationChannel() {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    AudioAttributes attributes = new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .build();
                    NotificationManager notificationManager = (NotificationManager) getSystemService(NotificationManager.class);
                    //config chanel 1
                    CharSequence name1 = "chanel 1";
                    NotificationChannel notificationChannel1 = new NotificationChannel(CHANNEL_ID_1, name1, NotificationManager.IMPORTANCE_HIGH);
                    notificationManager.createNotificationChannel(notificationChannel1);
                    notificationChannel1.setSound(uri,attributes);
                    //config chanel 2
                    CharSequence name2 = "chanel 2";
                    NotificationChannel notificationChannel2 = new NotificationChannel(CHANNEL_ID_2, name2, NotificationManager.IMPORTANCE_HIGH);
                    notificationManager.createNotificationChannel(notificationChannel2);
                    notificationChannel2.setSound(uri,attributes);
                }
            }

            @Override
            public void createNotification(int month, int year) {
                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Notification builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID_1)
                        .setSmallIcon(R.drawable.ic_baseline_warning_24)
                        .setContentTitle("Cảnh báo")
                        .setContentText("Bạn đang chi vượt mức tháng " + month +"/" + year + "!")
                        .setColor(Color.RED)
                        .setSound(uri)
                        .setPriority(NotificationManager.IMPORTANCE_HIGH)
                        .build();
                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                notificationManagerCompat.notify(getNotificationId(), builder);
            }
        });
    }
    private void moreFragmentUnit(){
        moreFragment.setOnChangeCLickListener(new MoreFragment.OnChangeCLickListener() {
            @Override
            public void onClick() {
                activeFragment = changePasswordFragment;

                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in,  // enter
                                R.anim.slide_out  // popExit
                        )
                        .add(R.id.frag_container, changePasswordFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });
    }
    private void changePasswordUnit(){
        changePasswordFragment.setOnChangePasswordFragmentClickListener(new ChangePasswordFragment.OnChangePasswordFragmentClickListener() {
            @Override
            public void onChangePasswordFragmentClickListener() {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in,  // enter
                                R.anim.slide_out  // popExit
                        )
                        .replace(R.id.frag_container, moreFragment)
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void showDialog() {



                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Thông báo");
                builder.setMessage("Thay đổi mật khẩu  thành công.");
                builder.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }
}