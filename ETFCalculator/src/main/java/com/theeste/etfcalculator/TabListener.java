package com.theeste.etfcalculator;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

/**
 * Created by Ryan on 11/26/13.
 */
public class TabListener<T extends Fragment> implements ActionBar.TabListener {
    private Fragment fragment;
    private final Activity activity;
    private final String tag;
    private final Class<T> fragmentClass;

    public TabListener(Activity activity, String tag, Class<T> fragmentClass) {
        this.activity = activity;
        this.tag = tag;
        this.fragmentClass = fragmentClass;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

        if (fragment == null)
            fragment = activity.getFragmentManager().findFragmentByTag(tag);

        if (fragment == null) {
            fragment = Fragment.instantiate(activity, fragmentClass.getName());
            fragmentTransaction.add(R.id.fragment_container, fragment, tag);
        } else {
            fragmentTransaction.attach(fragment);
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        if (fragment == null)
            return;

        fragmentTransaction.detach(fragment);
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // Nothing to do here
    }
}
