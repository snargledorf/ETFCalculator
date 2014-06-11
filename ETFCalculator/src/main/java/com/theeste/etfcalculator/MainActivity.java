package com.theeste.etfcalculator;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;

import org.joda.time.LocalDate;

public class MainActivity extends FragmentActivity implements
        CalendarDatePickerDialog.OnDateSetListener,
        CalculatorFragment.CalculatorFragmentCallbacks {

    private static final String TAG_CALENDAR_DATE_PICKER = "CalendarDatePickerDialog";

    private static final String TAG_CALCULATOR = "Calculator";

    static {
        FastDateTimeZoneProvider.init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new CalculatorFragment(), TAG_CALCULATOR)
                    .commit();
        }
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
    public void onContractDateButtonClicked(CalculatorFragment calculatorFragment) {
        LocalDate contractDate = calculatorFragment.getContractDate();

        CalendarDatePickerDialog calendarDatePickerDialog =
                CalendarDatePickerDialog
                        .newInstance(
                                this,
                                contractDate.getYear(),
                                contractDate.getMonthOfYear() - 1, // CalendarDatePicker uses 0 based month
                                contractDate.getDayOfMonth());

        calendarDatePickerDialog.show(getSupportFragmentManager(), TAG_CALENDAR_DATE_PICKER);
    }

    @Override
    public void onCalculatorSettingsClicked(CalculatorFragment calculatorFragment) {
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_container, new CalculatorSettingsFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    @Override
    public void onDateSet(CalendarDatePickerDialog calendarDatePickerDialog, int year, int month, int day) {
        setContractEndDate(year, month + 1, day); // CalendarDatePicker uses 0 based month
    }

    private void setContractEndDate(int year, int month, int day) {
        CalculatorFragment calculatorFragment =
                (CalculatorFragment) getSupportFragmentManager().findFragmentByTag(TAG_CALCULATOR);
        if (calculatorFragment == null)
            return;
        calculatorFragment.setContractEndDate(year, month, day);
    }
}
