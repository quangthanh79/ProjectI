package com.pqt.phamquangthanh.projecti.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.pqt.phamquangthanh.projecti.R;
import com.pqt.phamquangthanh.projecti.model.Transaction;
import com.pqt.phamquangthanh.projecti.model.TransactionGroup;
import com.pqt.phamquangthanh.projecti.util.DateUtil;
import com.pqt.phamquangthanh.projecti.util.SQLiteUtil;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddTransaction extends AppCompatActivity implements View.OnClickListener{

    CircleImageView clImg;
    TextView btnCancelTransaction,btnSaveTransaction,tvSelectedGroup,tvDatePickerValue;
    EditText etAmountOfMoney,etTransactionNote;
    DatePickerDialog datePickerDialog;
    Date selectedDate;
    SQLiteUtil sqLiteUtil;
    TransactionGroup transactionGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_transaction);
        mapView();
        init();

    }
    private void mapView(){
        btnCancelTransaction = (TextView) findViewById(R.id.btnCancelTransaction);
        btnSaveTransaction   = (TextView) findViewById(R.id.btnSaveTransaction);
        tvSelectedGroup      = (TextView) findViewById(R.id.tvSelectedGroup);
        clImg                = (CircleImageView) findViewById(R.id.ivTransGroupIcon);
        tvDatePickerValue    = (TextView) findViewById(R.id.tvDatePickerValue);

        etAmountOfMoney      = (EditText) findViewById(R.id.etAmountOfMoney);
        etTransactionNote    = (EditText) findViewById(R.id.etTransactionNote);
    }
    private void init(){
        btnCancelTransaction.setOnClickListener(this);
        btnSaveTransaction.setOnClickListener(this);
        etAmountOfMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
            etAmountOfMoney.removeTextChangedListener(this);

            String text = editable.toString();
            if(text.length() != 0) {
                if (text.contains(",")) {
                    text = text.replaceAll(",", "");
                }
                Long val = Long.parseLong(text);
                DecimalFormat decimalFormat = new DecimalFormat("##,###,###");
                etAmountOfMoney.setText(decimalFormat.format(val));
                etAmountOfMoney.setSelection(etAmountOfMoney.getText().length());
            }
            etAmountOfMoney.addTextChangedListener(this);

            }
        });

        tvDatePickerValue.setOnClickListener(this);
        tvSelectedGroup.setOnClickListener(this);

        selectedDate = Calendar.getInstance().getTime();

        tvDatePickerValue.setText(DateUtil.formatDate(selectedDate));

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.tvDatePickerValue:
                final Calendar calendar = Calendar.getInstance();
                int nam = calendar.get(Calendar.YEAR);
                int thang = calendar.get(Calendar.MONTH);
                int ngay  = calendar.get(Calendar.DATE);
                datePickerDialog = new DatePickerDialog(AddTransaction.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendar.set(i,i1,i2);
                        selectedDate = calendar.getTime();
                        tvDatePickerValue.setText(DateUtil.formatDate(selectedDate));
                    }
                },nam,thang,ngay);
                datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                datePickerDialog.show();

                break;
            case R.id.tvSelectedGroup:
                Intent intent = new Intent(AddTransaction.this,SelectTransactionGroupActivity.class);
                mactivityResultLauncher.launch(intent);
                break;
            case R.id.btnCancelTransaction:
                Intent return_intent_1= new Intent();
                setResult(Activity.RESULT_CANCELED,return_intent_1);
                finish();
                break;
            case R.id.btnSaveTransaction:
                if(etAmountOfMoney.getText().toString().equals("") || tvSelectedGroup.getText().toString().equals("")
                        || etTransactionNote.getText().toString().equals("")){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                    alertDialog.setTitle("Thông báo");
                    alertDialog.setMessage("Vui lòng nhập đủ thông tin");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    alertDialog.show();
                } else{
                    String text = etAmountOfMoney.getText().toString();
                    if(text.contains(",")){
                        text = text.replaceAll(",","");
                    }
                    Long AmountOfMoney = Long.parseLong(text);
                    String note = etTransactionNote.getText().toString();
                    Date date = selectedDate;

                    Intent return_intent_2 = new Intent();
                    return_intent_2.putExtra("result_add_transaction",new Transaction(transactionGroup,AmountOfMoney,note,date,0));
                    setResult(Activity.RESULT_OK,return_intent_2);
                    finish();
                }



                break;
        }
    }
    private ActivityResultLauncher<Intent> mactivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        Bundle bundle= data.getBundleExtra("dulieu");
                        transactionGroup = (TransactionGroup) bundle.getSerializable("transaction_group");
                        clImg.setImageResource(transactionGroup.getIcon());
                        tvSelectedGroup.setText(transactionGroup.getGroupName());
                    }
                }
            });
}
