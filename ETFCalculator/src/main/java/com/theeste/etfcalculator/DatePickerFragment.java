package com.theeste.etfcalculator;

/**
 * Created by ryan on 11/24/13.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;

import org.joda.time.LocalDate;

public class DatePickerFragment extends DialogFragment
        implements DatePicker.OnDateChangedListener {

    private static final String YEAR = "year";
    private static final String MONTH = "month";
    private static final String DAY = "day";
    private static final String TITLE = "title";

    private String mTitle;

    public void setTitle(String title) {
        mTitle = title;
    }

    private OnDateChangeListener mOnDateChangeListener;

    public static DatePickerFragment newInstance(int year, int month, int day) {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putInt(YEAR, year);
        args.putInt(MONTH, month);
        args.putInt(DAY, day);
        datePickerFragment.setArguments(args);
        return datePickerFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mOnDateChangeListener = (OnDateChangeListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnDateChangeListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TITLE, mTitle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view;
        DatePicker dp;
        DatePicker.OnDateChangedListener onDateChangedListener;

        int year;
        int month;
        int day;

        Bundle args = getArguments();
        if (args != null) {
            year = args.getInt(YEAR);
            month = args.getInt(MONTH);
            day = args.getInt(DAY);
        } else {
            LocalDate ld = LocalDate.now();
            year = ld.getYear();
            month = ld.getMonthOfYear();
            day = ld.getDayOfMonth();
        }

        if (savedInstanceState != null) {
            mTitle = savedInstanceState.getString(TITLE);
        }

        // If this fragment is being displayed as a dialog
        // then use the dialog layout, otherwise just return a DatePicker
        if (getShowsDialog()) {
            view = inflater.inflate(R.layout.fragment_date_picker_dialog, container, false);
            dp = (DatePicker) view.findViewById(R.id.datePicker);

            // We only want the date to update once the user dismisses the dialog
            onDateChangedListener = null;

            if (TextUtils.isEmpty(mTitle)) {
                getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            } else {
                getDialog().setTitle(mTitle);
            }

            setupOkButton(view);
        } else {
            view = inflater.inflate(R.layout.fragment_date_picker, container, false);
            dp = (DatePicker) view;

            // Update the date as the user changes dates
            onDateChangedListener = this;
        }

        dp.init(year, month, day, onDateChangedListener);

        return view;
    }

    @Override
    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
        // Android DatePicker month is 0 based
        mOnDateChangeListener.onDateChanged(year, month + 1, day);
    }

    public void setupOkButton(View view) {
        Button okButton = (Button) view.findViewById(R.id.okButton);
        okButton.setText(getString(R.string.ok));
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePicker dp = (DatePicker) getDialog().findViewById(R.id.datePicker);
                if (dp != null) {
                    int year = dp.getYear();
                    int month = dp.getMonth() + 1; // Android DatePicker month is 0 based
                    int day = dp.getDayOfMonth();
                    mOnDateChangeListener.onDateChanged(year, month, day);
                }
                getDialog().dismiss();
            }
        });
    }

    public static interface OnDateChangeListener {
        void onDateChanged(int year, int month, int day);
    }
}