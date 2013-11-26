package com.theeste.etfcalculator2;

/**
 * Created by ryan on 11/24/13.
 */

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import org.joda.time.LocalDate;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener, DatePicker.OnDateChangedListener {

    private DatePickerFragmentCallbacks mDatePickerFragmentCallbacks;

    public DatePickerFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mDatePickerFragmentCallbacks = (DatePickerFragmentCallbacks)activity;
    }

    String CREATE_DIALOG = "create_dialog";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getDialog() != null)
            return super.onCreateView(inflater, container, savedInstanceState);

        DatePicker datePicker = new DatePicker(getActivity());
        datePicker.setCalendarViewShown(false);
        datePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

        LocalDate date = mDatePickerFragmentCallbacks.getDefaultDate();
        datePicker.init(date.getYear(),
                date.getMonthOfYear() - 1,
                date.getDayOfMonth(),
                this);

        return datePicker;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LocalDate date = mDatePickerFragmentCallbacks.getDefaultDate();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                this,
                date.getYear(),
                date.getMonthOfYear() - 1,
                date.getDayOfMonth());

        datePickerDialog.getDatePicker().setCalendarViewShown(false);
        datePickerDialog.getDatePicker()
                .setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        onDateChanged(new LocalDate(year, month + 1, day));
    }

    @Override
    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
        onDateChanged(new LocalDate(year, month + 1, day));
    }

    private void onDateChanged(LocalDate date) {
        mDatePickerFragmentCallbacks.onDateChanged(date);
    }

    public static interface DatePickerFragmentCallbacks {
        LocalDate getDefaultDate();
        void onDateChanged(LocalDate date);
    }
}