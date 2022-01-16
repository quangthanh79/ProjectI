package com.pqt.phamquangthanh.projecti.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pqt.phamquangthanh.projecti.R;
import com.pqt.phamquangthanh.projecti.util.SQLiteUtil;
import com.pqt.phamquangthanh.projecti.model.User;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    EditText edtPassword;
    Button btnLogin;
    SQLiteUtil sqLiteUtil;
    User user;
    String password;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView textViewError;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        mapView();


        sqLiteUtil      = new SQLiteUtil(LoginActivity.this);
        btnLogin.setOnClickListener(this);
        sharedPreferences = getSharedPreferences("dulieudangnhap", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if(sharedPreferences.getString("password","").equals("")){
            editor.putString("password","66668888");
            editor.commit();
        }

    }
    public void mapView(){
        edtPassword   = findViewById(R.id.editPassword);
        btnLogin      = findViewById(R.id.btnLogin);
        textViewError = findViewById(R.id.textViewError);
    }

    @Override
    public void onClick(View view) {
        password = edtPassword.getText().toString().trim();
       if( password.equals(sharedPreferences.getString("password",""))){
           Intent intent = new Intent(LoginActivity.this,MainActivity.class);
           startActivity(intent);
       }
       else{
           textViewError.setVisibility(View.VISIBLE);
       }

    }
}