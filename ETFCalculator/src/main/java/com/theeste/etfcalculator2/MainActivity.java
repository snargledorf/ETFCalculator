package com.theeste.etfcalculator2;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements Section.SectionCallbacks {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    private CharSequence mTitle;
    private int mCurrentSelectedPosition = 0;
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private DrawerAdapter mDrawerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.setProperty("org.joda.time.DateTimeZone.Provider",
                "com.theeste.etfcalculator2.FastDateTimeZoneProvider");

        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        mTitle = mDrawerTitle = getTitle();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                    .getDefaultSharedPreferences(MainActivity.this);
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(mTitle);

        mDrawerList = (ListView) findViewById(R.id.navigation_drawer);

        mDrawerAdapter = new DrawerAdapter(
                getActionBar().getThemedContext(),
                new SectionDrawerItem[]{
                        new CalculatorDrawerItem(),
                        new PlaceholderDrawerItem("Section 1")
                });

        mDrawerList.setAdapter(mDrawerAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectItem(i);
            }
        });

        mDrawerList.setItemChecked(mCurrentSelectedPosition, true);

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(Gravity.START);
        }

        if (!mFromSavedInstanceState) {
            selectItem(0);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerList != null) {
            mDrawerList.setItemChecked(position, true);
        }

        SectionDrawerItem sectionDrawerItem = mDrawerAdapter.getItem(position);
        Section section = sectionDrawerItem.getSectionInstance();

        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, section)
                .commit();

        mDrawerLayout.closeDrawer(mDrawerList);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(String title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    public class PlaceholderDrawerItem extends SectionDrawerItem {

        private String mTitle;

        public PlaceholderDrawerItem(String title) {
            mTitle = title;
        }

        @Override
        public Section getSectionInstance() {
            return new PlaceholderSection(getTitle());
        }

        @Override
        public String getTitle() {
            return mTitle;
        }
    }

    public class CalculatorDrawerItem extends SectionDrawerItem {

        @Override
        public Section getSectionInstance() {
            return new CalculatorSection();
        }

        @Override
        public String getTitle() {
            return getString(R.string.calculator);
        }
    }

    public abstract class SectionDrawerItem {
        public abstract Section getSectionInstance();

        public abstract String getTitle();
    }

    private class DrawerAdapter extends ArrayAdapter<SectionDrawerItem> {
        public DrawerAdapter(Context context, SectionDrawerItem[] objects) {
            super(context, R.layout.drawer_item, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SectionDrawerItem item = getItem(position);
            View view = convertView;
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.drawer_item, parent, false);
            }

            TextView textView = (TextView) view.findViewById(R.id.drawer_item_text);
            textView.setText(item.getTitle());

            return view;
        }
    }
}
