package com.pqt.phamquangthanh.projecti.fragment;

import android.animation.ObjectAnimator;
import android.app.Activity;
import androidx.fragment.app.Fragment;
import android.app.AlertDialog;
//import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pqt.phamquangthanh.projecti.R;
import com.pqt.phamquangthanh.projecti.activity.AddTransaction;
import com.pqt.phamquangthanh.projecti.activity.DetailTransactionActivity;
import com.pqt.phamquangthanh.projecti.adapter.TransactionListViewAdapter;
import com.pqt.phamquangthanh.projecti.model.Transaction;
import com.pqt.phamquangthanh.projecti.model.TransactionDate;
import com.pqt.phamquangthanh.projecti.model.TransactionStatistic;
import com.pqt.phamquangthanh.projecti.util.ConversionUtil;
import com.pqt.phamquangthanh.projecti.util.DateUtil;
import com.pqt.phamquangthanh.projecti.util.SQLiteUtil;

import java.util.Date;

public class TransactionFragment extends Fragment implements View.OnClickListener{

    public static final int ADD_TRANSACTION_RESULT_CODE =1;

    SQLiteUtil sqLiteUtil;
    FloatingActionButton fbAddTransacsion;
    TextView tvPreviousPage,tvCurrentPage,tvNextPage,tvNoTransaction,tvTotalMoney;
    ArrayList<Object> data;
    TransactionListViewAdapter adapter;
    RecyclerView rvTransaction;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageView imgView,image_warning;
    public static Date currentDate;
    public static String type = "month";
    public long Day_Start,Day_End;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transaction_fragment,container,false);
        mapView(view);

        sqLiteUtil  = new SQLiteUtil(getActivity());
        currentDate = Calendar.getInstance().getTime();
        fbAddTransacsion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(getActivity(), AddTransaction.class);
                startActivityForResult(intent,ADD_TRANSACTION_RESULT_CODE);
            }
        });
        tvPreviousPage.setOnClickListener(this);
        tvNextPage.setOnClickListener(this);
        imgView.setOnClickListener(this);
        data = new ArrayList<>();
        adapter = new TransactionListViewAdapter(getActivity(),data);
        rvTransaction.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvTransaction.setAdapter(adapter);
        sharedPreferences = getActivity().getSharedPreferences("dulieucanhbao", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
            adapter.setOnTransactionItemClickListener(new TransactionListViewAdapter.OnTransactionItemClickListener() {
            @Override
            public void onTransactionItemClick(int transactionId) {
                Intent intent = new Intent(getActivity(), DetailTransactionActivity.class);
                intent.putExtra("send",transactionId);
                startActivity(intent);
            }
        });
            // create chanel notification
        onFetchNotification.createNotificationChannel();
        updateTotalMoney();
        updatePageTitle();
        fetchAndFillData();
        fetchWarning();
        adapter.setOnTransactionHeaderClickListener(new TransactionListViewAdapter.OnTransactionHeaderClickListener() {
            @Override
            public void onTransactionHeaderClick(int pot) {
                int size = data.size();
                pot++;
                while(pot <size && (data.get(pot) instanceof Transaction) ){
                    if(((Transaction) data.get(pot)).getIsShow() == 0){
                        ((Transaction) data.get(pot)).setIsShow(1);
                    }
                    else
                        ((Transaction) data.get(pot)).setIsShow(0);

                    pot++;
                }
                adapter.notifyDataSetChanged();
            }
        });
        adapter.setOnTransactionStaticClickListener(new TransactionListViewAdapter.OnTransactionStaticClickListener() {
            @Override
            public void OnTransactionStaticClick() {
                onTransactionFragmentClickListener.OnTransactionFragmentClick();
            }
        });

        return view;

    }

    private void mapView(View view){
        fbAddTransacsion   = (FloatingActionButton) view.findViewById(R.id.fbAddTransacsion);
        tvPreviousPage     = (TextView) view.findViewById(R.id.tvPreviousPage);
        tvCurrentPage      = (TextView) view.findViewById(R.id.tvCurrentPage);
        tvNextPage         = (TextView) view.findViewById(R.id.tvNextPage);
        rvTransaction      = (RecyclerView) view.findViewById(R.id.rvTransaction);
        imgView            = (ImageView) view.findViewById(R.id.image_more);
        tvNoTransaction    = (TextView) view.findViewById(R.id.tvNoTransaction);
        tvTotalMoney       = (TextView) view.findViewById(R.id.tvTotalMoney);
        image_warning      = (ImageView) view.findViewById(R.id.image_warning);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_TRANSACTION_RESULT_CODE && resultCode == Activity.RESULT_OK) {
            Transaction transaction = (Transaction) data.getSerializableExtra("result_add_transaction");
            sqLiteUtil.insertIntoTransaction(transaction);
            fetchAndFillData();
            if(transaction.getTransactionGroup().getTypeGroup() == 0){
                fetchWarning();
                fetchWarningAfterAddTransaction(transaction.getDate());
            }
            updateTotalMoney();
        }
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.tvPreviousPage:
                if(type != "") {
                    btnPreviousPageClick();
                    updatePageTitle();
                    fetchAndFillData();
                    fetchWarning();
                }
                break;
            case R.id.tvNextPage:
                if(type != "") {
                    btnNextPageClick();
                    updatePageTitle();
                    fetchAndFillData();
                    fetchWarning();
                }
                break;
            case R.id.image_more:
                onItemCLickListener.onClick();
                break;
        }
    }


    public void fetchWarning(){
        Calendar calendar_current = Calendar.getInstance();
        calendar_current.setTime(currentDate);
        int month_current = calendar_current.get(Calendar.MONTH)+1;
        int year_current  = calendar_current.get(Calendar.YEAR);
        long val_warning = sharedPreferences.getLong("warning",0);
        long total_warning = 0-sqLiteUtil.getTotalAmountInMonthFollowType(0,month_current,year_current);

        if(type == "date") {

                if(val_warning < total_warning){
                    image_warning.setVisibility(View.VISIBLE);
                }
                else {
                    image_warning.setVisibility(View.INVISIBLE);
                }
        } else if(type == "week"){

            Calendar calendar_1 = Calendar.getInstance();
            calendar_1.setTime(currentDate);
            calendar_1.set(Calendar.DAY_OF_WEEK,2);
            int month_1 = calendar_1.get(Calendar.MONTH)+1;
            int year_1  = calendar_1.get(Calendar.YEAR);

            Calendar calendar_2 = Calendar.getInstance();
            calendar_2.setTime(currentDate);
            calendar_2.set(Calendar.DAY_OF_WEEK,7);
            calendar_2.add(Calendar.DAY_OF_YEAR,1);
            int month_2 = calendar_2.get(Calendar.MONTH)+1;
            int year_2  = calendar_2.get(Calendar.YEAR);
            if(month_1 == month_2){
                if(val_warning < total_warning){
                    image_warning.setVisibility(View.VISIBLE);
                }
                else {
                    image_warning.setVisibility(View.INVISIBLE);
                }
            }else{
                long total_warning_1 = 0-sqLiteUtil.getTotalAmountInMonthFollowType(0,month_1,year_1);
                long total_warning_2 = 0-sqLiteUtil.getTotalAmountInMonthFollowType(0,month_2,year_2);
                if(val_warning < total_warning_1 || val_warning < total_warning_2){
                    image_warning.setVisibility(View.VISIBLE);
                }
                else {
                    image_warning.setVisibility(View.INVISIBLE);
                }
            }
        }
        else if(type == "month"){
                if(val_warning < total_warning){
                    image_warning.setVisibility(View.VISIBLE);
                }else{
                    image_warning.setVisibility(View.INVISIBLE);
                }
        }
        else if(type == "quarter"){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            int month = calendar.get(Calendar.MONTH)+1;
            int year  = calendar.get(Calendar.YEAR);
            int quarter = (month-1)/3+1;
//            DateUtil dateUtil
            int month1 = (quarter-1)*3+1;
            int month2 = (quarter-1)*3+2;
            int month3 = (quarter-1)*3+3;

            long total_warning_1 = 0-sqLiteUtil.getTotalAmountInMonthFollowType(0,month1,year);
            long total_warning_2 = 0-sqLiteUtil.getTotalAmountInMonthFollowType(0,month2,year);
            long total_warning_3 = 0-sqLiteUtil.getTotalAmountInMonthFollowType(0,month3,year);
            Toast.makeText(getActivity(),total_warning_1+"/"+total_warning_2+"/"+total_warning_3,Toast.LENGTH_SHORT).show();

            if(val_warning < total_warning_1 || val_warning < total_warning_2 || val_warning < total_warning_3){
                image_warning.setVisibility(View.VISIBLE);
            }else{
                image_warning.setVisibility(View.INVISIBLE);
            }
        }
        else if(type == "year"){
            // kiểm tra trọn vẹn 12 tháng
            // khi add thêm một transaction, kiểm tra tháng đó có vượt định mức ko, nếu có thì xuất Di           // content "Bạn vừa chi vượt định mức chi tiêu của tháng M/YYYY"
            // kiểm tra xem tháng vừa thêm giao dịch có nằm trong currentDate ko, nếu có xuất image Warning
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);

            int year  = calendar.get(Calendar.YEAR);

            long total_warning_1  = 0-sqLiteUtil.getTotalAmountInMonthFollowType(0,1,year);
            long total_warning_2  = 0-sqLiteUtil.getTotalAmountInMonthFollowType(0,2,year);
            long total_warning_3  = 0-sqLiteUtil.getTotalAmountInMonthFollowType(0,3,year);
            long total_warning_4  = 0-sqLiteUtil.getTotalAmountInMonthFollowType(0,4,year);
            long total_warning_5  = 0-sqLiteUtil.getTotalAmountInMonthFollowType(0,5,year);
            long total_warning_6  = 0-sqLiteUtil.getTotalAmountInMonthFollowType(0,6,year);
            long total_warning_7  = 0-sqLiteUtil.getTotalAmountInMonthFollowType(0,7,year);
            long total_warning_8  = 0-sqLiteUtil.getTotalAmountInMonthFollowType(0,8,year);
            long total_warning_9  = 0-sqLiteUtil.getTotalAmountInMonthFollowType(0,9,year);
            long total_warning_10 = 0-sqLiteUtil.getTotalAmountInMonthFollowType(0,10,year);
            long total_warning_11 = 0-sqLiteUtil.getTotalAmountInMonthFollowType(0,11,year);
            long total_warning_12 = 0-sqLiteUtil.getTotalAmountInMonthFollowType(0,12,year);

            boolean check1  = val_warning < total_warning_1;
            boolean check2  = val_warning < total_warning_2;
            boolean check3  = val_warning < total_warning_3;
            boolean check4  = val_warning < total_warning_4;
            boolean check5  = val_warning < total_warning_5;
            boolean check6  = val_warning < total_warning_6;
            boolean check7  = val_warning < total_warning_7;
            boolean check8  = val_warning < total_warning_8;
            boolean check9  = val_warning < total_warning_9;
            boolean check10 = val_warning < total_warning_10;
            boolean check11 = val_warning < total_warning_11;
            boolean check12 = val_warning < total_warning_12;

            if(check1 || check2 || check3 || check4 || check5 || check6
                    || check7 || check8 || check9 || check10 || check11 || check12){
                image_warning.setVisibility(View.VISIBLE);
            }else{
                image_warning.setVisibility(View.INVISIBLE);
            }
        }else if(type == ""){
            Date firstDay,lastDay;
            firstDay = ConversionUtil.timestampToDate(Day_Start);
            lastDay = ConversionUtil.timestampToDate(Day_End);

            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(firstDay);

            boolean check_warning = false,check_warning_item;

            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(lastDay);

            int month1 = calendar1.get(Calendar.MONTH)+1;
            int year1  = calendar1.get(Calendar.YEAR);
            int month2 = calendar2.get(Calendar.MONTH)+1;
            int year2  = calendar2.get(Calendar.YEAR);
            long total_warning_custom;
            if(year1 == year2){
                for(int m=month1; m<= month2; m++){
                    total_warning_custom  = 0-sqLiteUtil.getTotalAmountInMonthFollowType(0,m,year1);
                    Toast.makeText(getActivity(),m+"/"+total_warning_custom,Toast.LENGTH_SHORT).show();
                    check_warning_item    = val_warning < total_warning_custom;
                    check_warning         = check_warning || check_warning_item;
                }
            }else{
                for(int i=year1; i<=year2;i++){
                    if(i == year1){
                        for(int m=month1; m<=12; m++){
                            total_warning_custom  = 0-sqLiteUtil.getTotalAmountInMonthFollowType(0,m,i);
                            check_warning_item    = val_warning < total_warning_custom;
                            check_warning         = check_warning || check_warning_item;
                        }
                    }else if(i == year2){
                        for(int m=1; m<=month2; m++){
                            total_warning_custom  = 0-sqLiteUtil.getTotalAmountInMonthFollowType(0,m,i);
                            check_warning_item    = val_warning < total_warning_custom;
                            check_warning         = check_warning || check_warning_item;
                        }
                    }else{
                        for(int m=1; m<=12; m++){
                            total_warning_custom  = 0-sqLiteUtil.getTotalAmountInMonthFollowType(0,m,i);
                            check_warning_item    = val_warning < total_warning_custom;
                            check_warning         = check_warning || check_warning_item;
                        }
                    }
                }
            }
            if(check_warning){
                image_warning.setVisibility(View.VISIBLE);
            }else{
                image_warning.setVisibility(View.INVISIBLE);
            }

        }
    }
    public void fetchWarningAfterAddTransaction(Date date){
        Calendar calendar_add = Calendar.getInstance();
        calendar_add.setTime(date);
        int month_add = calendar_add.get(Calendar.MONTH)+1;
        int year_add  = calendar_add.get(Calendar.YEAR);


        long val_warning = sharedPreferences.getLong("warning",0);
        long total_warning = 0-sqLiteUtil.getTotalAmountInMonthFollowType(0,month_add,year_add);


                if(val_warning < total_warning){
                    image_warning.setVisibility(View.VISIBLE);
                    onFetchNotification.createNotification(month_add,year_add);
                }

    }

    public void fetchAndFillData(){
        Date firstDay;
        Date lastDay;
        if(type == ""){
            firstDay = ConversionUtil.timestampToDate(Day_Start);
            lastDay = ConversionUtil.timestampToDate(Day_End);
        }else {
            firstDay  = DateUtil.getFirstDay(currentDate, type);
            lastDay   = DateUtil.getLastDay(currentDate, type);
            Day_Start = DateUtil.getDayTime(firstDay);
            Day_End   = DateUtil.getDayTime(lastDay);
        }
        ArrayList<Transaction> databases = sqLiteUtil.getTransactionInRange(firstDay,lastDay);
        long firstDayMoney = sqLiteUtil.getMoneyAmountInSpecificDay(ConversionUtil.timestampToDate(DateUtil.getStartDayTime(firstDay)));
        long lastDayMoney = sqLiteUtil.getMoneyAmountInSpecificDay(ConversionUtil.timestampToDate(DateUtil.getEndDayTime(lastDay)));
        data.clear();
        data.add(new TransactionStatistic(firstDayMoney,lastDayMoney));
        int currentDay = -1;
        if(databases.size() != 0){
            rvTransaction.setVisibility(View.VISIBLE);
            tvNoTransaction.setVisibility(View.GONE);
            for(Transaction database : databases){
                if(DateUtil.getDateOfYear(database.getDate()) != currentDay){
                    currentDay = DateUtil.getDateOfYear(database.getDate());
                    data.add(new TransactionDate(database.getDate(),sqLiteUtil.getMoneyInADay(database.getDate())));
                    data.add(database);
                }
                else{
                    data.add(database);
                }
            }
        }else{
            rvTransaction.setVisibility(View.GONE);
            tvNoTransaction.setVisibility(View.VISIBLE);
        }

        adapter.notifyDataSetChanged();
    }

    public void btnNextPageClick(){

        Calendar calendar1 =Calendar.getInstance();
        Calendar calendar2 =Calendar.getInstance();
        calendar2.setTime(currentDate);

        if(type == "date") {
            currentDate = DateUtil.getNextDate(currentDate);
        }
        else if(type == "week"){
            if(calendar1.get(Calendar.WEEK_OF_YEAR) == calendar2.get(Calendar.WEEK_OF_YEAR) && calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)){
                return;
            }
            currentDate = DateUtil.getNextWeek(currentDate);
        }
        else if(type == "month"){
            if(calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) && calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)){
                return;
            }
            currentDate = DateUtil.getNextMonth(currentDate);
        }
        else if(type == "quarter"){
            if(calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) && calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)){
                return;
            }
            currentDate = DateUtil.getNextQuarter(currentDate);
        }
        else if(type == "year"){
            if(calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)){
                return;
            }
            currentDate = DateUtil.getNextYear(currentDate);
        }
    }
    public void btnPreviousPageClick(){


        if(type == "date") {
            currentDate = DateUtil.getPrevDate(currentDate);
        }
        else if(type == "week"){
            currentDate = DateUtil.getPrevWeek(currentDate);
        }
        else if(type == "month"){
            currentDate = DateUtil.getPrevMonth(currentDate);
        }
        else if(type == "quarter"){
            currentDate = DateUtil.getPrevQuarter(currentDate);
        }
        else if(type == "year"){
            currentDate = DateUtil.getPrevYear(currentDate);
        }
    }
    public void updateTotalMoney(){
        Calendar calendar = Calendar.getInstance();
        long totalMoney = sqLiteUtil.getMoneyAmountInSpecificDay(ConversionUtil.timestampToDate(DateUtil.getEndDayTime(calendar.getTime())));

        String txtTotalMoney = ConversionUtil.longToString(totalMoney);
        if (txtTotalMoney.contains("+")) {
            txtTotalMoney = txtTotalMoney.replaceAll("'+'", "");
        }
        tvTotalMoney.setText(txtTotalMoney);
    }
    public void updatePageTitle(){
        String titleNextPage ,titlePrevPage,titleCurrentPage;
        Date firstDay,lastDay;
        if(type == ""){
            firstDay = ConversionUtil.timestampToDate(Day_Start);
            lastDay = ConversionUtil.timestampToDate(Day_End);
            titleNextPage = "";
            titlePrevPage = "";
            titleCurrentPage = DateUtil.formatDateBaseOnCustom(firstDay,lastDay);
        }else {
            if (type == "date") {
                Date nextDate = DateUtil.getNextDate(currentDate);
                Date prevdate = DateUtil.getPrevDate(currentDate);
                titleNextPage = DateUtil.formatDateBaseOnDate(nextDate);
                titlePrevPage = DateUtil.formatDateBaseOnDate(prevdate);
                titleCurrentPage = DateUtil.formatDateBaseOnDate(currentDate);
            } else if (type == "week") {
                Date nextWeek = DateUtil.getNextWeek(currentDate);
                Date prevWeek = DateUtil.getPrevWeek(currentDate);
                titleNextPage = DateUtil.formatDateBaseOnWeek(nextWeek);
                titlePrevPage = DateUtil.formatDateBaseOnWeek(prevWeek);
                titleCurrentPage = DateUtil.formatDateBaseOnWeek(currentDate);
            } else if (type == "month") {
                Date nextMonth = DateUtil.getNextMonth(currentDate);
                Date prevMonth = DateUtil.getPrevMonth(currentDate);

                titleNextPage = DateUtil.formatDateBaseOnMonth(nextMonth);
                titlePrevPage = DateUtil.formatDateBaseOnMonth(prevMonth);
                titleCurrentPage = DateUtil.formatDateBaseOnMonth(currentDate);
            } else if (type == "quarter") {
                Date nextQuarter = DateUtil.getNextQuarter(currentDate);
                Date prevQuarter = DateUtil.getPrevQuarter(currentDate);
                titleNextPage = DateUtil.formatDateBaseOnQuarter(nextQuarter);
                titlePrevPage = DateUtil.formatDateBaseOnQuarter(prevQuarter);
                titleCurrentPage = DateUtil.formatDateBaseOnQuarter(currentDate);
            } else {
                Date nextYear = DateUtil.getNextYear(currentDate);
                Date prevYear = DateUtil.getPrevYear(currentDate);
                titleNextPage = DateUtil.formatDateBaseOnYear(nextYear);
                titlePrevPage = DateUtil.formatDateBaseOnYear(prevYear);
                titleCurrentPage = DateUtil.formatDateBaseOnYear(currentDate);
            }
        }
        tvNextPage.setText(titleNextPage);
        tvPreviousPage.setText(titlePrevPage);
        tvCurrentPage.setText(titleCurrentPage);
    }

    public interface OnItemCLickListener{
        void onClick();
    }

    public interface OnTransactionFragmentClickListener {
        void OnTransactionFragmentClick();
    }
    public interface OnFetchNotification {
        void createNotificationChannel();
        void createNotification(int month, int year);
    }
    public OnItemCLickListener onItemCLickListener;
    public OnTransactionFragmentClickListener onTransactionFragmentClickListener;
    public OnFetchNotification onFetchNotification;

    public void setOnItemClickListener(OnItemCLickListener onItemCLickListener){
        this.onItemCLickListener = onItemCLickListener;
    }

    public void setOnTransactionFragmentClickListener(OnTransactionFragmentClickListener onTransactionFragmentClickListener) {
        this.onTransactionFragmentClickListener = onTransactionFragmentClickListener;
    }

    public void setOnFetchNotification(OnFetchNotification onFetchNotification) {
        this.onFetchNotification = onFetchNotification;
    }

    public static void setType(String type) {
        TransactionFragment.type = type;
    }

    public void setDay_Start(long day_Start) {
        Day_Start = day_Start;
    }

    public void setDay_End(long day_End) {
        Day_End = day_End;
    }

    public static void setCurrentDate(Date currentDate) {
        TransactionFragment.currentDate = currentDate;
    }

    public long getDay_Start() {
        return Day_Start;
    }

    public long getDay_End() {
        return Day_End;
    }
}
