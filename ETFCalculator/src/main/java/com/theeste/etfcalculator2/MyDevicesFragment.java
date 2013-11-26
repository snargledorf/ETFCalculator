package com.theeste.etfcalculator2;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

/**
 * Created by Ryan on 11/23/13.
 */
public class MyDevicesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = "mydevices";
    GridView mDeviceGrid;
    private CursorAdapter mDeviceGridAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.section_my_devices, container, false);
        mDeviceGrid = (GridView) view.findViewById(R.id.device_grid);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadDevices();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.my_devices, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_device:
                addDevice();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addDevice() {
        AddDeviceFragment addDeviceFragment = new AddDeviceFragment();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, addDeviceFragment)
                .addToBackStack(null)
                .commit();
    }

    private void loadDevices() {
        getLoaderManager().initLoader(0, null, this);
        mDeviceGridAdapter = new DeviceListAdapter(null);
        mDeviceGrid.setAdapter(mDeviceGridAdapter);
    }

    private void deleteDevice(Device device) {
        ETFCalculatorContentProvider.deleteDevice(device, getActivity().getContentResolver());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(),
                ETFCalculatorContentProvider.DEVICES_URI,
                DeviceEntry.ALL_COLUMNS, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mDeviceGridAdapter.changeCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mDeviceGridAdapter.changeCursor(null);
    }

    private class DeviceListAdapter extends CursorAdapter {

        public DeviceListAdapter(Cursor cursor) {
            super(getActivity(), cursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            return LayoutInflater.from(context)
                    .inflate(R.layout.my_device_grid_item, viewGroup, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            Device device = Device.from(cursor);

            TextView deviceName = (TextView) view.findViewById(R.id.device_name);
            deviceName.setText(device.getName());

            TextView carrier = (TextView) view.findViewById(R.id.carrier);
            carrier.setText(device.getCarrier().getName());

            ImageView menuIcon = (ImageView) view.findViewById(R.id.menu_icon);
            menuIcon.setOnClickListener(new DeviceMenuOnClickListener(device));
        }

        private class DeviceMenuOnClickListener implements View.OnClickListener {

            private Device device;

            public DeviceMenuOnClickListener(Device device) {
                this.device = device;
            }

            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                popupMenu.inflate(R.menu.device);
                popupMenu.setOnMenuItemClickListener(menuItemClicked);
                popupMenu.show();
            }

            private PopupMenu.OnMenuItemClickListener menuItemClicked =
                    new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {

                            switch (menuItem.getItemId()) {
                                case R.id.delete:
                                    deleteDevice(device);
                                    return true;
                            }

                            return false;
                        }
                    };
        }
    }
}
