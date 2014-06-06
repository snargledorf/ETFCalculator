package com.theeste.etfcalculator;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.FrameLayout;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        // Create the AdRequest
        AdRequest adRequest = new AdRequest();

        // Get the list of test devices for the AdRequest
        String[] testDeviceIDs = getResources().getStringArray(R.array.admob_test_devices);
        List<String> testDeviceIDList = Arrays.asList(testDeviceIDs);
        Set<String> testDeviceSet = new HashSet<String>(testDeviceIDList);
        adRequest.setTestDevices(testDeviceSet);

        // Create the AdView
        AdView adView = new AdView(this, AdSize.SMART_BANNER, getString(R.string.admob_id));
        adView.loadAd(adRequest);

        // Add the AdView to the Ad FrameLayout
        FrameLayout adFrame = (FrameLayout) findViewById(R.id.ad_frame);
        adFrame.addView(adView);
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
