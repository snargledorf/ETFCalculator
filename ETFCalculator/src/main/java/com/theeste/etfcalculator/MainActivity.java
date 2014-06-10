package com.theeste.etfcalculator;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;

import org.joda.time.LocalDate;

public class MainActivity extends FragmentActivity implements
        CalendarDatePickerDialog.OnDateSetListener,
        CalculatorFragment.CalculatorFragmentCallbacks {

    private static final String TAG_CALENDAR_DATE_PICKER = "CalendarDatePickerDialog";

    static {
        FastDateTimeZoneProvider.init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        setCalendarDatePickerOnDateSetListener();
    }

    private void setCalendarDatePickerOnDateSetListener() {
        CalendarDatePickerDialog calendarDatePickerDialog =
                (CalendarDatePickerDialog)getSupportFragmentManager()
                        .findFragmentByTag(TAG_CALENDAR_DATE_PICKER);
        if (calendarDatePickerDialog != null)
            calendarDatePickerDialog.setOnDateSetListener(this);
    }

    @Override
    public void setContractEndDateButtonClicked(CalculatorFragment calculatorFragment) {
        LocalDate contractEndDate = calculatorFragment.getContractEndDate();

        CalendarDatePickerDialog calendarDatePickerDialog =
                CalendarDatePickerDialog
                        .newInstance(
                                this,
                                contractEndDate.getYear(),
                                contractEndDate.getMonthOfYear() - 1, // CalendarDatePicker uses 0 based month
                                contractEndDate.getDayOfMonth());

        calendarDatePickerDialog.show(getSupportFragmentManager(), TAG_CALENDAR_DATE_PICKER);
    }

    @Override
    public void onDateSet(CalendarDatePickerDialog calendarDatePickerDialog, int year, int month, int day) {
        setContractEndDate(year, month + 1, day); // CalendarDatePicker uses 0 based month
    }

    private void setContractEndDate(int year, int month, int day) {
        CalculatorFragment calculatorFragment =
                (CalculatorFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_calculator);
        if (calculatorFragment == null)
            return;
        calculatorFragment.setContractEndDate(year, month, day);
    }
}
