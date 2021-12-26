package com.pqt.phamquangthanh.projecti.fragment;

import android.app.Activity;
//import androidx.fragment.app.Fragment;
import android.app.Fragment;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
    TextView tvPreviousPage,tvCurrentPage,tvNextPage,tvNoTransaction;
    ArrayList<Object> data;
    TransactionListViewAdapter adapter;
    RecyclerView rvTransaction;
    ImageView imgView;
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
        adapter.setOnTransactionItemClickListener(new TransactionListViewAdapter.OnTransactionItemClickListener() {
            @Override
            public void onTransactionItemClick(int transactionId) {
                Intent intent = new Intent(getActivity(), DetailTransactionActivity.class);
                intent.putExtra("send",transactionId);
                startActivity(intent);
            }
        });

        updatePageTitle();
        fetchAndFillData();
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
        fbAddTransacsion  = (FloatingActionButton) view.findViewById(R.id.fbAddTransacsion);
        tvPreviousPage    = (TextView) view.findViewById(R.id.tvPreviousPage);
        tvCurrentPage     = (TextView) view.findViewById(R.id.tvCurrentPage);
        tvNextPage        = (TextView) view.findViewById(R.id.tvNextPage);
        rvTransaction     = (RecyclerView) view.findViewById(R.id.rvTransaction);
        imgView           = (ImageView) view.findViewById(R.id.image_more);
        tvNoTransaction   = (TextView) view.findViewById(R.id.tvNoTransaction);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_TRANSACTION_RESULT_CODE && resultCode == Activity.RESULT_OK) {
            Transaction transaction = (Transaction) data.getSerializableExtra("result_add_transaction");
            sqLiteUtil.insertIntoTransaction(transaction);
            fetchAndFillData();
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
                }
                break;
            case R.id.tvNextPage:
                if(type != "") {
                    btnNextPageClick();
                    updatePageTitle();
                    fetchAndFillData();
                }
                break;
            case R.id.image_more:
                onItemCLickListener.onClick();
                break;
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
    public OnItemCLickListener onItemCLickListener;
    public OnTransactionFragmentClickListener onTransactionFragmentClickListener;
    public void setOnItemClickListener(OnItemCLickListener onItemCLickListener){
        this.onItemCLickListener = onItemCLickListener;
    }

    public void setOnTransactionFragmentClickListener(OnTransactionFragmentClickListener onTransactionFragmentClickListener) {
        this.onTransactionFragmentClickListener = onTransactionFragmentClickListener;
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
