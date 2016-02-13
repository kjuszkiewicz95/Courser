package com.example.konrad.coursehub;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

/**
 * Created by Konrad on 12/23/2015.
 */
public class DatePickerFragment extends DialogFragment {
    DatePicker mDatePicker;
    int mMonth;
    int mDay;
    int mYear;

    public static final String EXTRA_YEAR = "com.example.konrad.coursehub.year";
    public static final String EXTRA_MONTH = "com.example.konrad.coursehub.month";
    public static final String EXTRA_DAY = "com.example.konrad.coursehub.day";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_date, null);

        Bundle args = getArguments();
        mYear = args.getInt(EXTRA_YEAR);
        mMonth = args.getInt(EXTRA_MONTH);
        mDay = args.getInt(EXTRA_DAY);

        mDatePicker = (DatePicker)v.findViewById(R.id.datePicker);
        mDatePicker.init(mYear, mMonth, mDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                mYear = i;
                mMonth = i1;
                mDay = i2;
            }
        });
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .create();
    }

    public void sendResult (int resultCode) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent i = new Intent();
        i.putExtra(EXTRA_YEAR, mYear);
        i.putExtra(EXTRA_MONTH, mMonth);
        i.putExtra(EXTRA_DAY, mDay);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
    }

    public static DatePickerFragment newInstance(int year, int month, int day) {
        DatePickerFragment fragment = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_YEAR, year);
        args.putInt(EXTRA_MONTH, month);
        args.putInt(EXTRA_DAY, day);
        fragment.setArguments(args);
        return fragment;
    }


}
