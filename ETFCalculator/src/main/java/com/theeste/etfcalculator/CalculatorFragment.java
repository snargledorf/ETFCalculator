package com.theeste.etfcalculator;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.joda.time.LocalDate;

/**
 * Created by Ryan on 11/23/13.
 */
public class CalculatorFragment extends Fragment {

    public static final String TAG = "calculator";

    private static final String SMARTPHONE_TOGGLE_STATE = "smartphone";
    private static final String CONTRACT_END_DATE = "contract_end_date";

    private TextView mETFLabel;
    private Button mContractDateButton;

    private boolean mIsSmartphone;
    private LocalDate mContractDate;

    private CalculatorCallbacks mCallbacks;
    private RadioToggleButtonGroupTableLayout mCarrierButtonsContainer;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (CalculatorCallbacks) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            mContractDate = LocalDate.now();
            return;
        }

        String contractEndDateString = savedInstanceState.getString(CONTRACT_END_DATE);
        mContractDate = LocalDate.parse(contractEndDateString);

        mIsSmartphone = savedInstanceState.getBoolean(SMARTPHONE_TOGGLE_STATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);

        setupSmartPhoneToggle(view);
        setupETFLabel(view);
        setupCarrierButtons(view);
        setupContractDateButton(view);
        setupContractDateLabel(view);
        setupSettingsButton(view);

        updateETFLabel();

        return view;
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        int visibility = View.VISIBLE;
        if (!EtfCalculatorPreferences.getPreferences(getActivity()).enableTmobile()) {
            visibility = View.GONE;
            if (mCarrierButtonsContainer.getCheckedViewId() == R.id.radio_tmobile) {
                mCarrierButtonsContainer.check(R.id.radio_att);
            }
        }

        getView().findViewById(R.id.radio_tmobile).setVisibility(visibility);
    }

    private void setupSettingsButton(View view) {
        view.findViewById(R.id.button_settings).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCallbacks != null)
                    mCallbacks.onCalculatorSettingsClicked(CalculatorFragment.this);
            }
        });
    }

    private void setupContractDateLabel(View view) {
        TextView contractDateLabel = (TextView)view.findViewById(R.id.label_contract_date);
        boolean useContractStartDate =
                EtfCalculatorPreferences.getPreferences(getActivity()).useContractStartDate();
        if (useContractStartDate) {
            contractDateLabel.setText(R.string.contract_date_label_start);
        } else {
            contractDateLabel.setText(R.string.contract_date_label_end);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SMARTPHONE_TOGGLE_STATE, mIsSmartphone);
        outState.putString(CONTRACT_END_DATE, mContractDate.toString());
    }

    private void setupETFLabel(View view) {
        mETFLabel = (TextView) view.findViewById(R.id.label_etf);
    }

    private void setupContractDateButton(View view) {

        mContractDateButton = (Button)view.findViewById(R.id.button_contract_date);

        if (mContractDateButton == null)
            return;

        mContractDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCallbacks != null) {
                    mCallbacks.onContractDateButtonClicked(CalculatorFragment.this);
                }
            }
        });

        updateContractDateButton();
    }

    private void setupCarrierButtons(View view) {
        mCarrierButtonsContainer =
                (RadioToggleButtonGroupTableLayout)view.findViewById(R.id.carrier_buttons_container);

        mCarrierButtonsContainer.setOnCheckChangedListener(new RadioToggleButtonGroupTableLayout.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioToggleButtonGroupTableLayout view, int viewId) {
                updateETFLabel();
            }
        });
    }

    private Carrier getSelectedCarrier() {
        switch (mCarrierButtonsContainer.getCheckedViewId()) {
            case R.id.radio_att:
                return Carrier.ATT;
            case R.id.radio_verizon:
                return Carrier.VERIZON;
            case R.id.radio_sprint:
                return Carrier.SPRINT;
            case R.id.radio_tmobile:
                return Carrier.TMOBILE;
        }
        return null;
    }

    private void setupSmartPhoneToggle(View view) {
        ToggleButton smartPhoneToggle = (ToggleButton) view.findViewById(R.id.toggle_smartphone);

        smartPhoneToggle.setChecked(mIsSmartphone);

        smartPhoneToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mIsSmartphone = b;
                updateETFLabel();
            }
        });
    }

    public void setContractEndDate(int year, int month, int day) {
        setContractEndDate(new LocalDate(
                year,
                month, // Android uses 0 based months
                day));
    }

    private void setContractEndDate(LocalDate date) {
        mContractDate = date;
        updateContractDateButton();
        updateETFLabel();
    }

    private void updateContractDateButton() {
        if (mContractDateButton != null && mContractDate != null) {
            mContractDateButton.setText(
                    String.format("%s/%s/%s",
                            mContractDate.getMonthOfYear(),
                            mContractDate.getDayOfMonth(),
                            mContractDate.getYear())
            );
        }
    }

    private void updateETFLabel() {
        if (getContractEndDate() == null || mETFLabel == null || getSelectedCarrier() == null)
            return;

        Carrier selectedCarrier = getSelectedCarrier();
        double etf = selectedCarrier.calculateEtf(getContractEndDate(), mIsSmartphone);
        mETFLabel.setText(String.format("$%.2f", etf));
    }

    private LocalDate getContractEndDate() {
        boolean useContractStartDate =
                EtfCalculatorPreferences.getPreferences(getActivity()).useContractStartDate();
        if (useContractStartDate) {
            return mContractDate.plusYears(2);
        }
        return mContractDate;
    }

    public LocalDate getContractDate() {
        return mContractDate;
    }

    public interface CalculatorCallbacks {
        void onContractDateButtonClicked(CalculatorFragment calculatorFragment);
        void onCalculatorSettingsClicked(CalculatorFragment calculatorFragment);
    }
}
