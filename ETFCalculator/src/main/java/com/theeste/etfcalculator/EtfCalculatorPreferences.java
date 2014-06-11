package com.theeste.etfcalculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Ryan on 6/11/2014.
 */
public class EtfCalculatorPreferences {
    private static final String USE_CONTRACT_START_DATE = "use_contract_begin_date";
    private static final String INCLUDE_TMOBILE = "include_tmobile";

    private static EtfCalculatorPreferences instance;

    private SharedPreferences mPreferences;

    public static EtfCalculatorPreferences getPreferences(Context context) {
        if (instance == null) {
            instance = new EtfCalculatorPreferences(context);
        }
        return instance;
    }

    private EtfCalculatorPreferences(Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }

    public void setUseContractStartDate(boolean useContractStartDate) {
        mPreferences
                .edit()
                .putBoolean(USE_CONTRACT_START_DATE, useContractStartDate)
                .apply();
    }

    public boolean useContractStartDate() {
        return mPreferences
                .getBoolean(USE_CONTRACT_START_DATE, false);
    }

    public void setEnableTmobile(boolean include) {
        mPreferences.edit().putBoolean(INCLUDE_TMOBILE, include).apply();
    }

    public boolean enableTmobile() {
        return mPreferences.getBoolean(INCLUDE_TMOBILE, false);
    }
}
