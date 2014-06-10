package com.theeste.etfcalculator;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ToggleButton;

/**
 * Created by Ryan on 6/10/2014.
 */
public class RadioToggleButton extends ToggleButton {
    public RadioToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RadioToggleButton(Context context) {
        super(context);
    }

    @Override
    public void toggle() {
        if (isChecked())
            return;
        super.toggle();
    }
}
