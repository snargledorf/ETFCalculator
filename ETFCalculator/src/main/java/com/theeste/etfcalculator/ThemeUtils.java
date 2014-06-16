package com.theeste.etfcalculator;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;

/**
 * Created by Ryan on 6/12/2014.
 */
public class ThemeUtils {

    public static final int THEME_BLUE = 0;
    public static final int THEME_RED = THEME_BLUE+1;
    public static final int THEME_PURPLE = THEME_RED+1;
    public static final int THEME_GREEN = THEME_PURPLE+1;

    public static void setTheme(Activity activity, int themeColor) {
        switch (themeColor) {
            case ThemeUtils.THEME_BLUE:
                setBackgroundColor(activity, activity.getResources().getColor(R.color.holo_blue_dark));
                break;
            case ThemeUtils.THEME_RED:
                setBackgroundColor(activity, activity.getResources().getColor(R.color.holo_red_dark));
                break;
            case ThemeUtils.THEME_PURPLE:
                setBackgroundColor(activity, activity.getResources().getColor(R.color.holo_purple));
                break;
            case ThemeUtils.THEME_GREEN:
                setBackgroundColor(activity, activity.getResources().getColor(R.color.holo_green_dark));
                break;
        }
    }

    public static void setBackgroundColor(Activity activity, int color) {
        activity.findViewById(android.R.id.content)
                .getRootView()
                .setBackgroundColor(color);
    }
}
