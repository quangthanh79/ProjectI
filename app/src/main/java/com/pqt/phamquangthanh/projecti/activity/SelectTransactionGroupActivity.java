package com.pqt.phamquangthanh.projecti.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pqt.phamquangthanh.projecti.R;
import com.pqt.phamquangthanh.projecti.adapter.TransactionGroupAdapter;
import com.pqt.phamquangthanh.projecti.model.TransactionGroup;
import com.pqt.phamquangthanh.projecti.util.SQLiteUtil;

import java.util.ArrayList;

public class SelectTransactionGroupActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvCancelTransaction,tvIncoming,tvOutgoing;
    RecyclerView rvGroup;
    ArrayList<TransactionGroup> data;
    SQLiteUtil sqLiteUtil = new SQLiteUtil(SelectTransactionGroupActivity.this);
    TransactionGroupAdapter adapter;
    FloatingActionButton fbAddTransacsionGroup;
    int type = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_select_transaction_group);
        mapView();
        init();
    }
    private void mapView(){
        tvCancelTransaction = (TextView) findViewById(R.id.btnCancelTransaction);
        tvIncoming          = (TextView) findViewById(R.id.btnIncoming);
        tvOutgoing          = (TextView) findViewById(R.id.btnOutgoing);
        rvGroup             = (RecyclerView)  findViewById(R.id.rvTransactionGroup);
        fbAddTransacsionGroup = (FloatingActionButton)  findViewById(R.id.fbAddTransacsionGroup);
    }
    public void init(){
        fbAddTransacsionGroup.setOnClickListener(this);
        tvCancelTransaction.setOnClickListener(this);
        tvIncoming.setOnClickListener(this);
        tvOutgoing.setOnClickListener(this);

        data = sqLiteUtil.getAllGroup(1);
//        Toast.makeText(SelectTransactionGroupActivity.this, data.size()+"", Toast.LENGTH_SHORT).show();
        adapter = new TransactionGroupAdapter(this,data);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvGroup.setLayoutManager(linearLayoutManager);
        rvGroup.setAdapter(adapter);
        adapter.setOnItemClickListener(new TransactionGroupAdapter.OnItemCLickListener() {
            @Override
            public void onClick(TransactionGroup transactionGroup) {
                Intent intent= new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("transaction_group",transactionGroup);
                intent.putExtra("dulieu",bundle);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnCancelTransaction:
                Intent return_intent= new Intent();
                setResult(Activity.RESULT_CANCELED,return_intent);
                finish();
                break;
            case R.id.btnIncoming:
                tvIncoming.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tvIncoming.setTextColor(getResources().getColor(R.color.colorWhite));

                tvOutgoing.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                tvOutgoing.setTextColor(getResources().getColor(R.color.colorBlack));
                data.clear();
                ArrayList<TransactionGroup> data_1 = sqLiteUtil.getAllGroup(1);
                for (TransactionGroup transactionGroup : data_1){
                    data.add(transactionGroup);
                }
                adapter.notifyDataSetChanged();
                tvOutgoing.setBackgroundResource(R.drawable.bg_bordered_1);
                type = 1;
                break;
            case R.id.btnOutgoing:
                tvOutgoing.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tvOutgoing.setTextColor(getResources().getColor(R.color.colorWhite));

                tvIncoming.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                tvIncoming.setTextColor(getResources().getColor(R.color.colorBlack));
                data.clear();
                ArrayList<TransactionGroup> data_0 = sqLiteUtil.getAllGroup(0);
                for (TransactionGroup transactionGroup : data_0){
                    data.add(transactionGroup);
                }
                adapter.notifyDataSetChanged();
                tvIncoming.setBackgroundResource(R.drawable.bg_bordered_1);
                type = 0;
                break;
            case R.id.fbAddTransacsionGroup:
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();


                final Dialog login= new Dialog(SelectTransactionGroupActivity.this);
                login.requestWindowFeature(Window.FEATURE_NO_TITLE);
                login.setContentView(R.layout.custom_dialog_add_transaction_group);
                lp.copyFrom(login.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                login.show();
                login.getWindow().setAttributes(lp);

                Button btnCancel = (Button) login.findViewById(R.id.buttonCancel);
                Button btnAdd    = (Button) login.findViewById(R.id.buttonAdd);
                final EditText name_transaction_group = (EditText) login.findViewById(R.id.name_transaction_group);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        login.dismiss();
                    }
                });
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(name_transaction_group.getText().toString().length() == 0){
                            Toast.makeText(SelectTransactionGroupActivity.this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_LONG).show();
                        }else{
                            String group = name_transaction_group.getText().toString();
                            sqLiteUtil.insertIntogroup(group,type);
                            login.dismiss();
                            data.clear();
                            ArrayList<TransactionGroup> data_2 = sqLiteUtil.getAllGroup(type);
                            for (TransactionGroup transactionGroup : data_2){
                                data.add(transactionGroup);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                break;
        }
    }
}