package com.theeste.etfcalculator2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryan on 11/25/13.
 */
public class ETFCalculatorDatasource {

    private ETFCalculatorDbHelper mDbHelper;
    private SQLiteDatabase mDatabase;

    public ETFCalculatorDatasource(Context context) {
        mDbHelper = new ETFCalculatorDbHelper(context);
    }

    public void open() {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
        mDatabase = null;
    }

    public boolean isOpen() {
        return mDatabase != null;
    }

    public Device createDevice(String name, int carrier, boolean smartPhone, LocalDate contractEndDate, int notificationType) {
        checkIfDatabaseIsOpen();

        ContentValues values = new ContentValues();
        values.put(DeviceEntry.NAME, name);
        values.put(DeviceEntry.CARRIER, carrier);
        values.put(DeviceEntry.SMART_PHONE, smartPhone ? 1 : 0);
        values.put(DeviceEntry.CONTRACT_END_DATE, contractEndDate.toString());
        values.put(DeviceEntry.NOTIFICATION_TYPE, notificationType);

        long insertId = mDatabase.insertOrThrow(DeviceEntry.TABLE_NAME, null, values);

        Cursor cursor = mDatabase.query(
                DeviceEntry.TABLE_NAME,
                DeviceEntry.ALL_COLUMNS,
                DeviceEntry._ID + " = " + insertId,
                null, null, null, null);
        cursor.moveToFirst();
        Device device = deviceFromCursor(cursor);
        cursor.close();

        return device;
    }

    public void deleteDevice(Device device) {
        checkIfDatabaseIsOpen();
        mDatabase.delete(DeviceEntry.TABLE_NAME, DeviceEntry._ID + " = " + device.getId(), null);
    }

    public List<Device> getAllDevices() {
        checkIfDatabaseIsOpen();

        List<Device> devices = new ArrayList<Device>();

        Cursor cursor = mDatabase.query(DeviceEntry.TABLE_NAME, DeviceEntry.ALL_COLUMNS,
                null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Device device = deviceFromCursor(cursor);
            devices.add(device);
            cursor.moveToNext();
        }
        cursor.close();

        return devices;
    }

    private void checkIfDatabaseIsOpen() {
        if (mDatabase == null)
            throw new IllegalArgumentException("Database not open");
    }

    private Device deviceFromCursor(Cursor cursor) {
        Device device = new Device();
        int position = 0;
        device.setId(cursor.getLong(position++));
        device.setName(cursor.getString(position++));
        device.setCarrier(cursor.getInt(position++));
        device.isSmartPhone(cursor.getInt(position++) > 0);
        device.setContractEndDate(LocalDate.parse(cursor.getString(position++)));
        device.setNotificationType(cursor.getInt(position++));
        return device;
    }

    public interface DatasourceProvider {
        ETFCalculatorDatasource getDatasource();
    }
}
