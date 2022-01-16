package com.pqt.phamquangthanh.projecti.fragment;

import android.app.AlertDialog;
//import android.app.Fragment;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.pqt.phamquangthanh.projecti.R;

import java.text.DecimalFormat;
import java.util.logging.Logger;

public class MoreFragment extends Fragment {
    LinearLayout more_warning,more_password;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    DecimalFormat decimalFormat = new DecimalFormat("##,###,###");

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.more_fragment, container, false);
        mapView(view);
        more_warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View alertLayout = inflater.inflate(R.layout.layout_custom_warning, null);
                EditText etWarning = (EditText) alertLayout.findViewById(R.id.et_Warning);
                etWarning.setText(decimalFormat.format(sharedPreferences.getLong("warning",0))+"VND");
                etWarning.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        etWarning.removeTextChangedListener(this);

                        String text = editable.toString();
                        if(text.length() != 0) {
                            if (text.contains(",")) {
                                text = text.replaceAll(",", "");
                            }
                            if (text.contains("VND")) {
                                text = text.replaceAll("VND", "");
                            }
                            Long val = Long.parseLong(text);

                            etWarning.setText(decimalFormat.format(val)+"VND");
                            etWarning.setSelection(etWarning.getText().length()-3);
                        }
                        etWarning.addTextChangedListener(this);

                    }
                });
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setView(alertLayout);
                alert.setCancelable(false);
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "Cancel clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String textWarning = etWarning.getText().toString();
                        if(textWarning.length() != 0) {
                            if (textWarning.contains(",")) {
                                textWarning = textWarning.replaceAll(",", "");
                            }
                            if (textWarning.contains("VND")) {
                                textWarning = textWarning.replaceAll("VND", "");
                            }
                        }
                        Long val = Long.parseLong(textWarning);
                        editor.putLong("warning",val);
                        editor.commit();
                        Toast.makeText(getActivity(),val+"", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();
            }
        });
        more_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChangeCLickListener.onClick();
                Log.d("myTag", "This is my message");
            }
        });

        return view;
    }
    private void mapView(View view){
        more_warning  = view.findViewById(R.id.more_warning);
        more_password = view.findViewById(R.id.more_password);
        sharedPreferences = getActivity().getSharedPreferences("dulieucanhbao", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putLong("warning",4000000);
        editor.commit();

    }
    public interface OnChangeCLickListener{
        void onClick();
    }
    public MoreFragment.OnChangeCLickListener onChangeCLickListener;

    public void setOnChangeCLickListener(OnChangeCLickListener onChangeCLickListener) {
        this.onChangeCLickListener = onChangeCLickListener;
    }


}
