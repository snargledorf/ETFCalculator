package com.theeste.etfcalculator2;

import android.app.Fragment;
import android.os.Bundle;
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

    private LocalDate mTodaysDate = LocalDate.now();

    private final Carrier[] mCarriers = new Carrier[]{
            new Carrier.Att(),
            new Carrier.Verizon(),
            new Carrier.Sprint(),
            new Carrier.TMobile()
    };

    private int mSelectedCarrier;
    private boolean mSmartphone;
    private LocalDate mContractEndDate;
    private Button mContactEndDateButton;
    private TextView mETFLabel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.section_calculator, container, false);

        setupCarrierSpinner(view, savedInstanceState);
        setupSmartPhoneToggle(view, savedInstanceState);
        setupContractEndButton(view, savedInstanceState);
        setupETFLabel(view);

        if (savedInstanceState != null && savedInstanceState.containsKey(CONTRACT_END_DATE)) {
            String contractEndDate = savedInstanceState.getString(CONTRACT_END_DATE);
            setContractEndDate(LocalDate.parse(contractEndDate));
        } else {
            setContractEndDate(LocalDate.now());
        }

        if (view.findViewById(R.id.datepicker_fragment_container) != null) {
            DatePickerFragment datePickerFragment = new DatePickerFragment();
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.datepicker_fragment_container, datePickerFragment)
                    .commit();
        }

        return view;
    }

    private void setupETFLabel(View view) {
        mETFLabel = (TextView) view.findViewById(R.id.etf_label);
    }

    private static final String SELECTED_CARRIER_POSITION = "selected_carrier_position";

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_CARRIER_POSITION, mSelectedCarrier);
        outState.putBoolean(SMARTPHONE_TOGGLE_STATE, mSmartphone);
        outState.putString(CONTRACT_END_DATE, mContractEndDate.toString());
    }

    private void setupContractEndButton(View view, Bundle savedInstanceState) {

        mContactEndDateButton = (Button) view.findViewById(R.id.contract_end_date_button);

        if (mContactEndDateButton == null) {
            return;
        }

        mContactEndDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getFragmentManager(), "datepicker_fragment");
            }
        });
    }

    public void setContractEndDate(LocalDate selectedDate) {
        mContractEndDate = selectedDate;
        if (mContactEndDateButton != null) {
            mContactEndDateButton.setText(mContractEndDate.toString());
        }
        updateETF();
    }

    private void setupCarrierSpinner(View view, Bundle savedInstanceState) {
        Spinner mCarrierSpinner = (Spinner) view.findViewById(R.id.carrier_spinner);

        ArrayAdapter<Carrier> mCarrierAdapter = new ArrayAdapter<Carrier>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                mCarriers);

        mCarrierSpinner.setAdapter(mCarrierAdapter);

        if (savedInstanceState != null) {
            int selectedCarrier = savedInstanceState.getInt(SELECTED_CARRIER_POSITION);
            mSelectedCarrier = selectedCarrier;
            mCarrierSpinner.setSelection(mSelectedCarrier);
        }

        mCarrierSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSelectedCarrier = i;
                updateETF();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Nothing to do here
            }
        });
    }

    private void setupSmartPhoneToggle(View view, Bundle savedInstanceState) {
        ToggleButton mSmartphoneToggle = (ToggleButton) view.findViewById(R.id.smartphone_toggle);

        if (savedInstanceState != null) {
            boolean smartphone = savedInstanceState.getBoolean(SMARTPHONE_TOGGLE_STATE, false);
            mSmartphone = smartphone;
            mSmartphoneToggle.setChecked(mSmartphone);
        }

        mSmartphoneToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mSmartphone = b;
                updateETF();
            }
        });
    }

    private void updateETF() {
        Carrier selectedCarrier = mCarriers[mSelectedCarrier];
        double etf = selectedCarrier.getETF(mTodaysDate, getContractEndDate(), mSmartphone);
        mETFLabel.setText(String.format("$%.2f", etf));
    }

    public LocalDate getContractEndDate() {
        return mContractEndDate;
    }
}
