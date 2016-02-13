package com.example.konrad.coursehub;


import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewSemesterFragment extends DialogFragment {
    String mSeason;
    int mYear;

    DatePicker mDatePicker;
    EditText mSeasonEditText;
    UUID mSemesterId;


    public static final String EXTRA_YEAR = "com.example.konrad.coursehub.year";
    public static final String EXTRA_SEASON = "com.example.konrad.coursehub.season";
    public static final String EXTRA_SEMESTER_ID = "com.example.konrad.coursehub.semesterId";

    public NewSemesterFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_new_semester, null);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mSemesterId = (UUID)arguments.getSerializable(EXTRA_SEMESTER_ID);
            mSeason = arguments.getString(EXTRA_SEASON);
            mYear = arguments.getInt(EXTRA_YEAR);
        }

        mDatePicker = (DatePicker)v.findViewById(R.id.datePicker);
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year;
        if (mSemesterId != null) {
            year = mYear;
        }
        else {
            year = cal.get(Calendar.YEAR);
        }

        mYear = year;
        mDatePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mYear = year;
                // IN CASE OF ROTATION
                if(getArguments() != null) {
                    getArguments().putInt(EXTRA_YEAR, mYear);
                }
            }
        });
        findAndHideField(mDatePicker, "mDaySpinner");
        findAndHideField(mDatePicker, "mMonthSpinner");

        mSeasonEditText = (EditText)v.findViewById(R.id.seasonEditText);
        if (mSeason != null) {
            mSeasonEditText.setText(mSeason);
        }
        mSeasonEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSeason = s.toString();
                if (getArguments() != null) {
                    getArguments().putString(EXTRA_SEASON, mSeason);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return new AlertDialog.Builder(getActivity())
                .setTitle("SEMESTER INFO")
                .setView(v)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // here have to figure out how to get these two fragments communicating
                        Academics.get(getActivity()).saveSemesters();
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .create();
    }

    public void sendResult(int resultCode) {
        if (getTargetFragment() == null) {
            return;
        }
        Fragment targetFragment = getTargetFragment(); // most likely SemesterListFragment
        Intent i = new Intent();
        i.putExtra(EXTRA_SEASON, mSeason);
        i.putExtra(EXTRA_YEAR, mYear);
        if (mSemesterId != null) {
            i.putExtra(EXTRA_SEMESTER_ID, mSemesterId);
        }
        targetFragment.onActivityResult(getTargetRequestCode(), resultCode, i);
    }

    private void findAndHideField(DatePicker datePicker, String name) {
        try {
            Field field = DatePicker.class.getDeclaredField(name);
            field.setAccessible(true);
            View fieldInstance = (View)field.get(datePicker);
            fieldInstance.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static NewSemesterFragment editSemesterInstance(UUID semesterId, String season, int year) {
        NewSemesterFragment fragment = new NewSemesterFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_SEMESTER_ID, semesterId);
        args.putString(EXTRA_SEASON, season);
        args.putInt(EXTRA_YEAR, year);
        fragment.setArguments(args);
        return fragment;
    }

}
