package com.pqt.phamquangthanh.projecti.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
//import android.app.Fragment;
import androidx.fragment.app.Fragment;


import com.google.android.material.textfield.TextInputEditText;
import com.pqt.phamquangthanh.projecti.R;

public class ChangePasswordFragment extends Fragment {
    ImageView imageView;
    TextInputEditText etPasswordOld,etPasswordNew,etPasswordNew2;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    TextView txtRsCheckOld,txtRsCheckNew,txtRsCheckNew21,txtRsCheckNew22;
    public ChangePasswordFragment.OnChangePasswordFragmentClickListener onChangePasswordFragmentClickListener;
    boolean checkInputOld = false, checkInputNew = false,checkInputNew2 = false;
    Button buttonUpdate;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.change_password_fragment, container, false);
        mapView(view);
        sharedPreferences = getActivity().getSharedPreferences("dulieudangnhap", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChangePasswordFragmentClickListener.onChangePasswordFragmentClickListener();
            }
        });
        check();
        return view;
    }
    private void mapView(View view){
        imageView      = view.findViewById(R.id.imageViewBack);
        etPasswordOld  = view.findViewById(R.id.etPasswordOld);
        etPasswordNew  = view.findViewById(R.id.etPasswordNew);
        etPasswordNew2 = view.findViewById(R.id.etPasswordNew2);
        txtRsCheckOld  = view.findViewById(R.id.txtRsCheckOld);
        txtRsCheckNew  = view.findViewById(R.id.txtRsCheckNew);
        txtRsCheckNew21 = view.findViewById(R.id.txtRsCheckNew21);
        txtRsCheckNew22 = view.findViewById(R.id.txtRsCheckNew22);
        buttonUpdate    = view.findViewById(R.id.buttonUpdate);
    }
    private void check(){
        String password = sharedPreferences.getString("password","");
        etPasswordOld.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String rsPasswordOld = etPasswordOld.getText().toString();
                if(!rsPasswordOld.equals(password) ){
                    txtRsCheckOld.setVisibility(View.VISIBLE);
                    checkInputOld = false;
                }else{
                    checkInputOld = true;
                    txtRsCheckOld.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String rsPasswordOld = etPasswordOld.getText().toString();
                if(!rsPasswordOld.equals(password) ){
                    txtRsCheckOld.setVisibility(View.VISIBLE);
                    checkInputOld = false;
                }else{
                    checkInputOld = true;
                    txtRsCheckOld.setVisibility(View.GONE);
                }
            }
        });
        etPasswordNew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String rsPasswordNew  = etPasswordNew.getText().toString();
                if(rsPasswordNew.length() <8 ){
                    txtRsCheckNew.setVisibility(View.VISIBLE);
                    checkInputNew = false;
                }else{
                    txtRsCheckNew.setVisibility(View.GONE);
                    checkInputNew = true;
                }
            }
        });
        etPasswordNew2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String rsPasswordNew2  = etPasswordNew2.getText().toString();
                if(!rsPasswordNew2.equals(etPasswordNew.getText().toString())){
                    txtRsCheckNew22.setVisibility(View.GONE);
                    txtRsCheckNew21.setVisibility(View.VISIBLE);
                    checkInputNew2 = false;
                }else{
                    txtRsCheckNew21.setVisibility(View.GONE);
                    txtRsCheckNew22.setVisibility(View.VISIBLE);
                    checkInputNew2 = true;
                }
            }
        });


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkInputOld && checkInputNew && checkInputNew2) {
                    onChangePasswordFragmentClickListener.onChangePasswordFragmentClickListener();
                    editor.putString("password",etPasswordNew.getText().toString());
                    editor.commit();
                    onChangePasswordFragmentClickListener.showDialog();
                }
            }
        });

    }

    public interface OnChangePasswordFragmentClickListener {
        void onChangePasswordFragmentClickListener();
        void showDialog();
    }

    public void setOnChangePasswordFragmentClickListener(OnChangePasswordFragmentClickListener onChangePasswordFragmentClickListener) {
        this.onChangePasswordFragmentClickListener = onChangePasswordFragmentClickListener;
    }
}
