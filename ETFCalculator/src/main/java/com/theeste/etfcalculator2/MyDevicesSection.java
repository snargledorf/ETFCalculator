package com.theeste.etfcalculator2;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ryan on 11/23/13.
 */
public class MyDevicesSection extends Section {

    MyDevicesCallbacks mCallbacks;
    ListView mDeviceList;

    @Override
    public String getTitle() {
        return getString(R.string.my_devices_title);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (MyDevicesCallbacks) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.section_my_devices, container, false);
        mDeviceList = (ListView) view.findViewById(R.id.device_list);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LoadDevicesAsyncTask loadDevicesAsyncTask = new LoadDevicesAsyncTask();
        loadDevicesAsyncTask.execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.my_devices, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem item = menu.findItem(R.id.add_device);
        item.setVisible(!mCallbacks.isDrawerOpen());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_device:
                mCallbacks.addDevice();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setDeviceListAdapter(ListAdapter adapter) {
        if (mDeviceList == null)
            return;
        mDeviceList.setAdapter(adapter);
    }

    public interface MyDevicesCallbacks extends ETFCalculatorDatasource.DatasourceProvider {
        void addDevice();

        boolean isDrawerOpen();
    }

    private class LoadDevicesAsyncTask extends AsyncTask<Void, Void, List<Device>> {

        @Override
        protected List<Device> doInBackground(Void... voids) {
            ETFCalculatorDatasource datasource = mCallbacks.getDatasource();
            if (!datasource.isOpen())
                datasource.open();
            return datasource.getAllDevices();
        }

        @Override
        protected void onPostExecute(List<Device> devices) {
            super.onPostExecute(devices);
            setDeviceListAdapter(new DeviceListAdapter(getActivity(), devices));
        }
    }

    ;

    private class DeviceListAdapter extends ArrayAdapter<Device> {

        public DeviceListAdapter(Context context, List<Device> devices) {
            super(context, 0, devices);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(getContext())
                        .inflate(R.layout.my_device_list_item, parent, false);
            }

            Device device = getItem(position);

            TextView deviceName = (TextView) view.findViewById(R.id.device_name);
            deviceName.setText(device.getName());

            return view;
        }
    }

}
