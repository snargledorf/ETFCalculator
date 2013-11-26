package com.theeste.etfcalculator2;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.joda.time.LocalDate;

/**
 * A placeholder fragment containing a simple view.
 */
class AddDeviceFragment extends Fragment {

    ETFCalculatorDatasource.DatasourceProvider mDatasourceProvider;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mDatasourceProvider = (ETFCalculatorDatasource.DatasourceProvider) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_device, container, false);
        Button button = (Button) rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDeviceAsyncTask addDeviceAsyncTask = new AddDeviceAsyncTask();
                addDeviceAsyncTask.execute();
            }
        });
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class AddDeviceAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            ETFCalculatorDatasource datasource = mDatasourceProvider.getDatasource();

            if (!datasource.isOpen())
                datasource.open();

            datasource.createDevice("testDevice", 0, false, LocalDate.now(), 0);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getActivity().onBackPressed();
        }
    }
}
