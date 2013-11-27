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

    private final int mYear;
    private final int mMonth;
    private final int mDay;
    private String mTitle;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    private OnDateChangeListener mOnDateChangeListener;

    public static DatePickerFragment newInstance(int year, int month, int day) {
        return new DatePickerFragment(year, month, day);
    }

    private DatePickerFragment(int year, int month, int day) {
        mYear = year;
        mMonth = month;
        mDay = day;
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

        datePicker.init(mYear, mMonth, mDay, onDateChangedListener);

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