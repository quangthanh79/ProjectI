package com.pqt.phamquangthanh.projecti.fragment;

//import android.app.Fragment;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.pqt.phamquangthanh.projecti.R;

public class WarningFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transaction_fragment,container,false);
        return view;
        }
}
