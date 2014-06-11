package com.theeste.etfcalculator;



import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
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

        int theme = EtfCalculatorPreferences.getPreferences(getActivity()).getTheme();
        switch (theme) {
            case R.style.BlueTheme:
                toggleButtonGroupTableLayout.check(R.id.toggle_blue_theme);
                break;
            case R.style.RedTheme:
                toggleButtonGroupTableLayout.check(R.id.toggle_red_theme);
                break;
            case R.style.PurpleTheme:
                toggleButtonGroupTableLayout.check(R.id.toggle_purple_theme);
                break;
            case R.style.GreenTheme:
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
                        setTheme(R.style.BlueTheme);
                        break;
                    case R.id.toggle_red_theme:
                        setTheme(R.style.RedTheme);
                        break;
                    case R.id.toggle_purple_theme:
                        setTheme(R.style.PurpleTheme);
                        break;
                    case R.id.toggle_green_theme:
                        setTheme(R.style.GreenTheme);
                        break;
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setTheme(int themeResId) {
        if (themeResId == EtfCalculatorPreferences.getPreferences(getActivity()).getTheme())
            return;

        EtfCalculatorPreferences.getPreferences(getActivity()).setTheme(themeResId);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            Intent intent = getActivity().getIntent();
            getActivity().finish();
            startActivity(intent);
        } else {
            getActivity().recreate();
        }
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
