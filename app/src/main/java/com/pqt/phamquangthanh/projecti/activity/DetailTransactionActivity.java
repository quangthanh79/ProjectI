package com.pqt.phamquangthanh.projecti.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.pqt.phamquangthanh.projecti.R;
import com.pqt.phamquangthanh.projecti.model.Transaction;
import com.pqt.phamquangthanh.projecti.util.DateUtil;
import com.pqt.phamquangthanh.projecti.util.SQLiteUtil;

import java.text.DecimalFormat;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailTransactionActivity extends AppCompatActivity implements View.OnClickListener{
    SQLiteUtil sqLiteUtil;
    Transaction transaction;
    TextView btnCancelUpdateTransaction,btnSaveUpdateTransaction,tvSelectedGroupUpdate,tvDatePickerValueUpdate;
    EditText etAmountOfMoneyUpdate,etTransactionNoteUpdate;
    CircleImageView ivTransGroupIconUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail_transaction);
        mapView();
        sqLiteUtil = new SQLiteUtil(DetailTransactionActivity.this);
        Intent intent = getIntent();
        int id = intent.getIntExtra("send",0);
        transaction = sqLiteUtil.getTransactionById(id);
        long amountOfMoney = transaction.getAmountOfMoney();
        DecimalFormat decimalFormat = new DecimalFormat("##,###,###");
        if(amountOfMoney<= 0){
            amountOfMoney = 0 - amountOfMoney;
        }

        ivTransGroupIconUpdate.setImageResource(transaction.getTransactionGroup().getIcon());
        etTransactionNoteUpdate.setText(transaction.getNote());
        tvSelectedGroupUpdate.setText(transaction.getTransactionGroup().getGroupName());
        tvDatePickerValueUpdate.setText(DateUtil.formatDate(transaction.getDate()));
        etAmountOfMoneyUpdate.setText(decimalFormat.format(amountOfMoney));
        etAmountOfMoneyUpdate.setSelection(etAmountOfMoneyUpdate.getText().length());

        btnCancelUpdateTransaction.setOnClickListener(this);
    }

    private void mapView(){
        btnCancelUpdateTransaction = (TextView) findViewById(R.id.btnCancelUpdateTransaction);
        btnSaveUpdateTransaction   = (TextView) findViewById(R.id.btnSaveUpdateTransaction);
        tvSelectedGroupUpdate      = (TextView) findViewById(R.id.tvSelectedGroupUpdate);
        tvDatePickerValueUpdate    = (TextView) findViewById(R.id.tvDatePickerValueUpdate);
        etAmountOfMoneyUpdate      = (EditText) findViewById(R.id.etAmountOfMoneyUpdate);
        etTransactionNoteUpdate    = (EditText) findViewById(R.id.etTransactionNoteUpdate);
        ivTransGroupIconUpdate     = (CircleImageView) findViewById(R.id.ivTransGroupIconUpdate);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnCancelUpdateTransaction:
                finish();
                break;
        }
    }
}