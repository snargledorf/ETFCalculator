package com.theeste.etfcalculator2;

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
public class CalculatorSection extends Section {

    private LocalDate mTodaysDate = LocalDate.now();

    private ArrayAdapter<Carrier> mCarrierAdapter;

    private Carrier mSelectedCarrier;
    private boolean mSmartphone;
    private LocalDate mContractEndDate;
    private Button mContactEndDateButton;
    private TextView mETFLabel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContractEndDate = LocalDate.now();
    }

    @Override
    public String getTitle() {
        return getString(R.string.calculator);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.section_calculator, container, false);

        setupCarrierSpinner(view);
        setupSmartPhoneToggle(view);
        setupContractEndButton(view);

        setContractEndDate(mContractEndDate);

        mETFLabel = (TextView)view.findViewById(R.id.etf_label);

        updateETF();

        if (view.findViewById(R.id.datepicker_fragment_container) != null) {
            DatePickerFragment datePickerFragment = new DatePickerFragment();
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.datepicker_fragment_container, datePickerFragment)
                    .commit();
        }

        return view;
    }

    private void setupContractEndButton(View view) {

        mContactEndDateButton = (Button) view.findViewById(R.id.contract_end_date_button);

        if (mContactEndDateButton == null) {
            return;
        }

        mContactEndDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getChildFragmentManager(), "datepicker_fragment");
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

    private void setupCarrierSpinner(View view) {
        Spinner carrierSpinner = (Spinner) view.findViewById(R.id.carrier_spinner);

        mCarrierAdapter = new ArrayAdapter<Carrier>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                new Carrier[]{
                        new Carrier.Att(),
                        new Carrier.Verizon(),
                        new Carrier.Sprint(),
                        new Carrier.TMobile()
                });

        carrierSpinner.setAdapter(mCarrierAdapter);

        carrierSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSelectedCarrier = mCarrierAdapter.getItem(i);
                updateETF();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Nothing to do here
            }
        });
    }

    private void setupSmartPhoneToggle(View view) {
        ToggleButton smartphoneToggle =
                (ToggleButton) view.findViewById(R.id.smartphone_toggle);
        smartphoneToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mSmartphone = b;
                updateETF();
            }
        });
    }

    private void updateETF() {
        if (mSelectedCarrier == null)
            return;

        double etf = mSelectedCarrier.getETF(mTodaysDate, getContractEndDate(), mSmartphone);

        mETFLabel.setText(String.format("$%.2f", etf));
    }

    public LocalDate getContractEndDate() {
        return mContractEndDate;
    }
}
