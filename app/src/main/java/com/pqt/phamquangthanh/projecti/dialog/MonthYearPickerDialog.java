package com.pqt.phamquangthanh.projecti.dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.pqt.phamquangthanh.projecti.R;
import com.pqt.phamquangthanh.projecti.fragment.FollowFragment;

import java.util.Calendar;

public class MonthYearPickerDialog extends DialogFragment {

    private static final int MIN_YEAR = 1970;
    private DatePickerDialog.OnDateSetListener listener;
    private int year;
    private int month;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        Calendar cal = Calendar.getInstance();

        View dialog = inflater.inflate(R.layout.month_year_picker_dialog, null);
        final NumberPicker monthPicker = (NumberPicker) dialog.findViewById(R.id.picker_month);
        final NumberPicker yearPicker = (NumberPicker) dialog.findViewById(R.id.picker_year);

        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setValue(cal.get(Calendar.MONTH));
        monthPicker.computeScroll();

        int year = cal.get(Calendar.YEAR);
        yearPicker.setMinValue(1970);
        yearPicker.setMaxValue(year);
        yearPicker.setValue(year);

        builder.setView(dialog)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        OnClickOK.onClick(monthPicker.getValue(),yearPicker.getValue());
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }
    public interface setOnClickOK{
        public void onClick(int month, int year);
    }
    private setOnClickOK OnClickOK;

    public void setOnClickOK(setOnClickOK onClickOK) {
        OnClickOK = onClickOK;
    }
}
