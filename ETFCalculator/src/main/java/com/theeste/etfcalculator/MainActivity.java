package com.theeste.etfcalculator;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.google.android.gms.ads.*;

public class MainActivity extends FragmentActivity implements
        DatePickerFragment.OnDateChangeListener,
        CalculatorFragment.CalculatorFragmentCallbacks {

    private CharSequence mTitle;

    static {
        FastDateTimeZoneProvider.init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setupAds();

        mTitle = getTitle();

        setupActionBar();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.setTitle(mTitle);
            }
        }
    }

    private void setupAds() {
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice(getString(R.string.nexus5_test_device_id))
                .build();

        AdView adView = (AdView)findViewById(R.id.ad_view);
        adView.loadAd(adRequest);
    }

    @Override
    public void onDateChanged(int year, int month, int day) {
        CalculatorFragment calculatorFragment =
                (CalculatorFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_calculator);
        if (calculatorFragment == null)
            return;
        calculatorFragment.setContractEndDate(year, month, day);
    }

    @Override
    public void setContractEndDateButtonClicked(int year, int month, int day) {
        DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(year, month, day);
        datePickerFragment.setTitle("Contract End Date");
        datePickerFragment.show(getSupportFragmentManager(), "date_picker");
    }
}
