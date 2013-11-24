package com.theeste.etfcalculator2;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by Ryan on 11/23/13.
 */
public abstract class Section extends Fragment {
    private SectionCallbacks mCallbacks;

    protected abstract String getTitle();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallbacks = (SectionCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement " +
                    SectionCallbacks.class.getName());
        }

        mCallbacks.setTitle(getTitle());
    }

    public interface SectionCallbacks {
        void setTitle(String title);
    }
}
