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
    private Button mContractEndDateButton;

    private boolean mIsSmartphone;
    private LocalDate mContractEndDate;

    private CalculatorFragmentCallbacks mCallbacks;
    private ToggleViewGroupTableLayout mCarrierButtonsContainer;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (CalculatorFragmentCallbacks) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            mContractEndDate = LocalDate.now();
            return;
        }

        String contractEndDateString = savedInstanceState.getString(CONTRACT_END_DATE);
        mContractEndDate = LocalDate.parse(contractEndDateString);

        mIsSmartphone = savedInstanceState.getBoolean(SMARTPHONE_TOGGLE_STATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);

        setupSmartPhoneToggle(view);
        setupETFLabel(view);
        setupContractEndDateButton(view);
        setupCarrierButtons(view);

        if (savedInstanceState == null) {
            view.findViewById(R.id.radio_att).performClick();
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SMARTPHONE_TOGGLE_STATE, mIsSmartphone);
        outState.putString(CONTRACT_END_DATE, mContractEndDate.toString());
    }

    private void setupETFLabel(View view) {
        mETFLabel = (TextView) view.findViewById(R.id.label_etf);
    }

    private void setupContractEndDateButton(View view) {

        mContractEndDateButton = (Button)view.findViewById(R.id.button_contract_end_date);

        if (mContractEndDateButton == null)
            return;

        mContractEndDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCallbacks != null) {
                    mCallbacks.setContractEndDateButtonClicked(CalculatorFragment.this);
                }
            }
        });

        updateContractEndDateButton();
    }

    private void setupCarrierButtons(View view) {
        mCarrierButtonsContainer =
                (ToggleViewGroupTableLayout)view.findViewById(R.id.carrier_buttons_container);

        mCarrierButtonsContainer.setOnCheckChangedListener(new ToggleViewGroupTableLayout.OnCheckedChangeListener() {
            @Override
            public void onCheckedChange(View view, boolean checked) {
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
        mContractEndDate = date;
        updateContractEndDateButton();
        updateETFLabel();
    }

    private void updateContractEndDateButton() {
        if (mContractEndDateButton != null && mContractEndDate != null) {
            mContractEndDateButton.setText(
                    String.format("%s/%s/%s",
                            mContractEndDate.getMonthOfYear(),
                            mContractEndDate.getDayOfMonth(),
                            mContractEndDate.getYear()));
        }
    }

    private void updateETFLabel() {
        if (getContractEndDate() == null || mETFLabel == null || getSelectedCarrier() == null)
            return;

        Carrier selectedCarrier = getSelectedCarrier();
        double etf = selectedCarrier.calculateEtf(getContractEndDate(), mIsSmartphone);
        mETFLabel.setText(String.format("$%.2f", etf));
    }

    public LocalDate getContractEndDate() {
        return mContractEndDate;
    }

    public interface CalculatorFragmentCallbacks {
        void setContractEndDateButtonClicked(CalculatorFragment calculatorFragment);
    }
}
