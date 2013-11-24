package com.theeste.etfcalculator2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;

/**
 * Created by Ryan on 11/23/13.
 */
public class CalculatorSection extends Section {

    @Override
    public String getTitle() {
        return getString(R.string.calculator);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calc, container, false);
        return view;
    }
}
