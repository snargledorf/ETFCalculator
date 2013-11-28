package com.theeste.etfcalculator;

/**
 * Created by ryan on 11/24/13.
 */

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;

public class DatePickerFragment
        extends DialogFragment
        implements DatePicker.OnDateChangedListener {

    private static final String YEAR = "year";
    private static final String MONTH = "month";
    private static final String DAY = "day";

    private String mTitle;

    public String getTitle() {
        return mTitle;
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view;
        DatePicker datePicker;
        DatePicker.OnDateChangedListener onDateChangedListener;
        Bundle args = getArguments();
        int year = args.getInt(YEAR);
        int month = args.getInt(MONTH);
        int day = args.getInt(DAY);

        // If this fragment is being displayed as a dialog
        // then use the dialog layout, otherwise just return a DatePicker
        if (getShowsDialog()) {
            view = inflater.inflate(R.layout.date_picker_dialog, container, false);
            datePicker = (DatePicker) view.findViewById(R.id.datePicker);

            // We only want the date to update once the user dismisses the dialog
            onDateChangedListener = null;

            if (TextUtils.isEmpty(mTitle)) {
                getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            } else {
                getDialog().setTitle(mTitle);
            }

            Button okButton = (Button) view.findViewById(R.id.okButton);
            okButton.setText(getString(R.string.ok));
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatePicker datePicker1 = (DatePicker) getDialog().findViewById(R.id.datePicker);
                    if (datePicker1 != null) {
                        int year = datePicker1.getYear();
                        int month = datePicker1.getMonth();
                        int day = datePicker1.getDayOfMonth();
                        mOnDateChangeListener.onDateChanged(year, month, day);
                    }
                    getDialog().dismiss();
                }
            });
        } else {
            datePicker = new DatePicker(getActivity());
            view = datePicker;

            // Update the date as the user changes dates
            onDateChangedListener = this;
        }

        datePicker.setCalendarViewShown(false);
        datePicker.init(year, month, day, onDateChangedListener);

        return view;
    }

    @Override
    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
        mOnDateChangeListener.onDateChanged(year, month, day);
    }

    public static interface OnDateChangeListener {
        void onDateChanged(int year, int month, int day);
    }
}