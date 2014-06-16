package com.theeste.etfcalculator;



import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
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

    private CalculatorSettingsCallbacks mCallbacks;

    public CalculatorSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (CalculatorSettingsCallbacks)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calculator_settings, container, false);

        setupEnableTmobileButton(view);
        setupUseContractStartButton(view);
        setupBackButton(view);
        setupAboutButton(view);
        setupThemeButtons(view);

        return view;
    }

    private void setupThemeButtons(View view) {
        RadioToggleButtonGroupTableLayout toggleButtonGroupTableLayout =
                (RadioToggleButtonGroupTableLayout)view.findViewById(R.id.theme_toggle_group);

        if (toggleButtonGroupTableLayout == null)
            return;

        int color = EtfCalculatorPreferences.getPreferences(getActivity()).getTheme();
        switch (color) {
            case ThemeUtils.THEME_BLUE:
                toggleButtonGroupTableLayout.check(R.id.toggle_blue_theme);
                break;
            case ThemeUtils.THEME_RED:
                toggleButtonGroupTableLayout.check(R.id.toggle_red_theme);
                break;
            case ThemeUtils.THEME_PURPLE:
                toggleButtonGroupTableLayout.check(R.id.toggle_purple_theme);
                break;
            case ThemeUtils.THEME_GREEN:
                toggleButtonGroupTableLayout.check(R.id.toggle_green_theme);
                break;
        }

        toggleButtonGroupTableLayout.setOnCheckChangedListener(
                new RadioToggleButtonGroupTableLayout.OnCheckedChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onCheckedChanged(RadioToggleButtonGroupTableLayout view, int viewId) {
                switch (viewId) {
                    case R.id.toggle_blue_theme:
                        setBackgroundColor(ThemeUtils.THEME_BLUE);
                        break;
                    case R.id.toggle_red_theme:
                        setBackgroundColor(ThemeUtils.THEME_RED);
                        break;
                    case R.id.toggle_purple_theme:
                        setBackgroundColor(ThemeUtils.THEME_PURPLE);
                        break;
                    case R.id.toggle_green_theme:
                        setBackgroundColor(ThemeUtils.THEME_GREEN);
                        break;
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setBackgroundColor(int themeColor) {
        if (themeColor == EtfCalculatorPreferences.getPreferences(getActivity()).getTheme())
            return;

        EtfCalculatorPreferences.getPreferences(getActivity()).setTheme(themeColor);

        ThemeUtils.setTheme(getActivity(), themeColor);
    }

    private void setupAboutButton(View view) {
        view.findViewById(R.id.button_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCallbacks != null)
                    mCallbacks.onCalculatorSettingsAboutClicked();
            }
        });
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


    public interface CalculatorSettingsCallbacks {
        void onCalculatorSettingsAboutClicked();
    }
}
