package com.theeste.etfcalculator2;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.joda.time.LocalDate;

/**
 * A placeholder fragment containing a simple view.
 */
class AddDeviceFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_device, container, false);
        Button button = (Button) rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDevice();
            }
        });
        return rootView;
    }

    private void addDevice() {
        ETFCalculatorContentProvider.createDevice("Test Device",
                Carrier.CARRIER_ID_ATT,
                false,
                LocalDate.now(),
                0,
                getActivity().getContentResolver());

        getActivity().onBackPressed();
    }
}
