package com.theeste.etfcalculator2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Ryan on 11/23/13.
 */
public class MyDevicesSection extends Section {

    @Override
    public String getTitle() {
        return getString(R.string.my_devices_title);
    }

    public MyDevicesSection() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.section_my_devices, container, false);
        TextView textView = (TextView) view.findViewById(R.id.section_label);
        textView.setText(getTitle());
        return view;
    }
}
