package com.theeste.etfcalculator;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class CalculatorSettingsFragment extends Fragment {


    public CalculatorSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calculator_settings, container, false);

        setupEnableTmobileButton(view);
        setupUseContractStartButton(view);
        setupBackButton(view);

        return view;
    }

    private void setupBackButton(View view) {
        view.findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
    }

    private void setupUseContractStartButton(View view) {
        ToggleButton contractDateModeButton =
                (ToggleButton) view.findViewById(R.id.toggle_contract_date_mode);

        contractDateModeButton
                .setChecked(
                        EtfCalculatorPreferences
                                .getPreferences(getActivity())
                                .useContractStartDate()
                );

        contractDateModeButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean contractStart) {
                EtfCalculatorPreferences
                        .getPreferences(getActivity())
                        .setUseContractStartDate(contractStart);
            }
        });
    }

    private void setupEnableTmobileButton(View view) {
        ToggleButton enableTmobileButton =
                (ToggleButton)view.findViewById(R.id.toggle_enable_tmobile);

        enableTmobileButton
                .setChecked(EtfCalculatorPreferences.getPreferences(getActivity()).enableTmobile());

        enableTmobileButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean enableTmobile) {
                EtfCalculatorPreferences.getPreferences(getActivity()).setEnableTmobile(enableTmobile);
            }
        });
    }


}
