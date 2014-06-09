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

    private Carrier mSelectedCarrier;
    private boolean mSmartphone;
    private Button mContactEndDateButton;
    private TextView mETFLabel;
    private CalculatorFragmentCallbacks mCallbacks;
    private LocalDate mContractEndDate;

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

        mSmartphone = savedInstanceState.getBoolean(SMARTPHONE_TOGGLE_STATE);
        mSelectedCarrier = Carrier.values()[savedInstanceState.getInt(SELECTED_CARRIER)];
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);

        setupCarrierSpinner(view);
        setupSmartPhoneToggle(view);
        setupETFLabel(view);

        if (view.findViewById(R.id.datepicker_fragment_container) != null) {
            if (savedInstanceState == null) {
                setupDatePickerFragment();
            }
        } else {
            setupContractEndButton(view);
        }

        return view;
    }

    private void setupDatePickerFragment() {
        getChildFragmentManager().beginTransaction()
                .add(R.id.datepicker_fragment_container,
                        DatePickerFragment.newInstance(
                                mContractEndDate.getYear(),
                                mContractEndDate.getMonthOfYear(),
                                mContractEndDate.getDayOfMonth()))
                .commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_CARRIER, mSelectedCarrier.ordinal());
        outState.putBoolean(SMARTPHONE_TOGGLE_STATE, mSmartphone);
        outState.putString(CONTRACT_END_DATE, mContractEndDate.toString());
    }

    private void setupETFLabel(View view) {
        mETFLabel = (TextView) view.findViewById(R.id.etf_label);
    }

    private void setupContractEndButton(View view) {

        mContactEndDateButton = (Button) view.findViewById(R.id.contract_end_date_button);

        if (mContactEndDateButton == null)
            return;

        mContactEndDateButton.setText(mContractEndDate.toString());

        mContactEndDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbacks.setContractEndDateButtonClicked(
                        mContractEndDate.getYear(),
                        mContractEndDate.getMonthOfYear() - 1, // Android uses 0 based months
                        mContractEndDate.getDayOfMonth());
            }
        });
    }

    private void setupCarrierSpinner(View view) {
        final Spinner mCarrierSpinner = (Spinner) view.findViewById(R.id.carrier_spinner);

        ArrayAdapter<Carrier> mCarrierAdapter = new ArrayAdapter<Carrier>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                Carrier.values());

        mCarrierSpinner.setAdapter(mCarrierAdapter);

        mCarrierSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSelectedCarrier = ((Carrier)mCarrierSpinner.getSelectedItem());
                updateETF();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Nothing to do here
            }
        });
    }

    private void setupSmartPhoneToggle(View view) {
        ToggleButton mSmartphoneToggle = (ToggleButton) view.findViewById(R.id.smartphone_toggle);

        mSmartphoneToggle.setChecked(mSmartphone);

        mSmartphoneToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mSmartphone = b;
                updateETF();
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
        if (mContactEndDateButton != null) {
            mContactEndDateButton.setText(mContractEndDate.toString());
        }
        updateETF();
    }

    private void updateETF() {
        double etf = mSelectedCarrier.calculateEtf(mContractEndDate, mSmartphone);
        mETFLabel.setText(String.format("$%.2f", etf));
    }

    public interface CalculatorFragmentCallbacks {
        void setContractEndDateButtonClicked(int year, int month, int day);
    }
}
