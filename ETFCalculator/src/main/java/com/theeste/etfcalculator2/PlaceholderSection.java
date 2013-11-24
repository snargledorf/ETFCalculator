package com.theeste.etfcalculator2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Ryan on 11/23/13.
 */
public class PlaceholderSection extends Section {
    String mTitle;

    @Override
    public String getTitle() {
        return mTitle;
    }

    public PlaceholderSection(String title) {
        mTitle = title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        TextView textView = (TextView) view.findViewById(R.id.section_label);
        textView.setText(getTitle());
        return view;
    }
}
