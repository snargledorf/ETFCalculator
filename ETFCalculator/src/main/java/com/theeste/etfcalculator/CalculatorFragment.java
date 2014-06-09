package com.theeste.etfcalculator;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
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
    private static final String SELECTED_CARRIER = "selected_carrier_position";

    private TextView mETFLabel;
    private Button mContractEndDateButton;

    private Carrier mSelectedCarrier;
    private boolean mIsSmartphone;
    private LocalDate mContractEndDate;

    private CalculatorFragmentCallbacks mCallbacks;

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
        mSelectedCarrier = Carrier.values()[savedInstanceState.getInt(SELECTED_CARRIER)];
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);

        setupCarrierSpinner(view);
        setupSmartPhoneToggle(view);
        setupETFLabel(view);
        setupContractEndDateButton(view);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_CARRIER, mSelectedCarrier.ordinal());
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

    private void setupCarrierSpinner(View view) {
        final Spinner carrierSpinner = (Spinner) view.findViewById(R.id.spinner_carrier);

        ArrayAdapter<Carrier> carrierAdapter = new ArrayAdapter<Carrier>(getActivity(),
                android.R.layout.simple_spinner_item,
                Carrier.values());
        carrierAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        carrierSpinner.setAdapter(carrierAdapter);

        carrierSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSelectedCarrier = ((Carrier) carrierSpinner.getSelectedItem());
                updateETFLabel();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Nothing to do here
            }
        });
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
        if (mContractEndDate == null || mETFLabel == null)
            return;

        double etf = mSelectedCarrier.calculateEtf(mContractEndDate, mIsSmartphone);
        mETFLabel.setText(String.format("$%.2f", etf));
    }

    public LocalDate getContractEndDate() {
        return mContractEndDate;
    }

    public interface CalculatorFragmentCallbacks {
        void setContractEndDateButtonClicked(CalculatorFragment calculatorFragment);
    }
}
