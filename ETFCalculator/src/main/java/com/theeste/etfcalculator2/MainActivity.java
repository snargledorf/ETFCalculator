package com.theeste.etfcalculator2;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import org.joda.time.LocalDate;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.app.ActionBar.Tab;

public class MainActivity extends Activity implements
        DatePickerFragment.DatePickerFragmentCallbacks {

    private final String CURRENT_TAB = "current_tab";

    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.setProperty("org.joda.time.DateTimeZone.Provider",
                "com.theeste.etfcalculator2.FastDateTimeZoneProvider");

        setContentView(R.layout.activity_main);

        setupAds();

        mTitle = getTitle();

        setupActionBar(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_TAB, getActionBar().getSelectedNavigationIndex());
    }

    private void setupActionBar(Bundle savedInstanceState) {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            setupActionBarTabs();
            if (savedInstanceState != null) {
                int selectedTab = savedInstanceState.getInt(CURRENT_TAB, 0);
                actionBar.setSelectedNavigationItem(selectedTab);
            }
            actionBar.setTitle(mTitle);
        }
    }

    private void setupActionBarTabs() {
        setupActionBarNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        addCalculatorTab();
        addMyDevicesTab();
    }

    private void setupActionBarNavigationMode(int navigationMode) {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setNavigationMode(navigationMode);
        }
    }

    private void addCalculatorTab() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            Tab tab = actionBar.newTab()
                    .setText(R.string.calculator)
                    .setTabListener(new TabListener<CalculatorFragment>(
                            this, CalculatorFragment.TAG, CalculatorFragment.class));
            actionBar.addTab(tab);
        }
    }

    private void addMyDevicesTab() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            Tab tab = actionBar.newTab()
                    .setText(R.string.my_devices_title)
                    .setTabListener(new TabListener<MyDevicesFragment>(
                            this, MyDevicesFragment.TAG, MyDevicesFragment.class));
            actionBar.addTab(tab);
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
    public LocalDate getDefaultDate() {
        CalculatorFragment calculatorFragment =
                (CalculatorFragment) getFragmentManager().findFragmentByTag(CalculatorFragment.TAG);
        if (calculatorFragment == null)
            return LocalDate.now();
        return calculatorFragment.getContractEndDate();
    }

    @Override
    public void onDateChanged(LocalDate date) {
        CalculatorFragment calculatorFragment =
                (CalculatorFragment) getFragmentManager().findFragmentByTag(CalculatorFragment.TAG);
        if (calculatorFragment == null)
            return;
        calculatorFragment.setContractEndDate(date);
    }
}
