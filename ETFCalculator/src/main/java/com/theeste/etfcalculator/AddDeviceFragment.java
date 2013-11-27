package com.theeste.etfcalculator;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.joda.time.LocalDate;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddDeviceFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_device, container, false);
        Button button = (Button) rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Device.create(
                        "Test Device",
                        Carrier.CARRIER_ATT,
                        false,
                        LocalDate.now(),
                        0,
                        getActivity().getContentResolver());

                getFragmentManager().popBackStack();
            }
        });
        return rootView;
    }
}
