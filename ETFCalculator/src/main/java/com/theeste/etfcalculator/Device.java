package com.theeste.etfcalculator;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.theeste.etfcalculator.provider.ETFCalculatorContract.Devices;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/**
 * Created by Ryan on 11/25/13.
 */
public class Device {

    private long dbId;
    private String deviceId;
    private String deviceName;
    private Carrier deviceCarrier;
    private LocalDate deviceContractEndDate;
    private int deviceNotificationType;
    private boolean deviceIsSmartphone;

    private Device() {
    }

    public String getId() {
        return deviceId;
    }

    public String getName() {
        return deviceName;
    }

    public void setName(String mName) {
        deviceName = mName;
    }

    public Carrier getCarrier() {
        return deviceCarrier;
    }

    public void setCarrier(Carrier mCarrier) {
        deviceCarrier = mCarrier;
    }

    public LocalDate getContractEndDate() {
        return deviceContractEndDate;
    }

    public void setContractEndDate(LocalDate mContractEndDate) {
        deviceContractEndDate = mContractEndDate;
    }

    public int getNotificationType() {
        return deviceNotificationType;
    }

    public void setNotificationType(int mNotificationType) {
        deviceNotificationType = mNotificationType;
    }

    public boolean isSmartPhone() {
        return deviceIsSmartphone;
    }

    public void isSmartPhone(boolean isSmartPhone) {
        deviceIsSmartphone = isSmartPhone;
    }

    public void update(ContentResolver contentResolver) {

        ContentValues values = new ContentValues();
        values.put(Devices.DEVICE_NAME, getName());
        values.put(Devices.DEVICE_CARRIER, getCarrier().getCarrierId());
        values.put(Devices.DEVICE_SMART_PHONE, isSmartPhone() ? 1 : 0);
        values.put(Devices.DEVICE_CONTRACT_END_DATE, getContractEndDate().toString());
        values.put(Devices.DEVICE_NOTIFICATION_TYPE, getNotificationType());

        Device.update(getId(), values, contentResolver);
    }

    public void delete(ContentResolver contentResolver) {
        Device.delete(getId(), contentResolver);
    }

    public static Device create(String name,
                                Carrier carrier,
                                boolean smartPhone,
                                LocalDate contractEndDate,
                                int notificationType,
                                ContentResolver contentResolver) {

        ContentValues values = new ContentValues();
        values.put(Devices.DEVICE_ID, Devices.generateDeviceId(name, DateTime.now().getMillis()));
        values.put(Devices.DEVICE_NAME, name);
        values.put(Devices.DEVICE_CARRIER, carrier.getCarrierId());
        values.put(Devices.DEVICE_SMART_PHONE, smartPhone ? 1 : 0);
        values.put(Devices.DEVICE_CONTRACT_END_DATE, contractEndDate.toString());
        values.put(Devices.DEVICE_NOTIFICATION_TYPE, notificationType);

        return create(values, contentResolver);
    }

    private static Device create(ContentValues contentValues, ContentResolver contentResolver) {
        Uri deviceUri = contentResolver.insert(
                Devices.CONTENT_URI,
                contentValues);

        return get(deviceUri, contentResolver);
    }

    private static void update(String deviceId, ContentValues contentValues, ContentResolver contentResolver) {
        Uri uri = Devices.buildDeviceUri(deviceId);
        contentResolver.update(uri, contentValues, null, null);
    }

    public static Device get(String deviceId, ContentResolver contentResolver) {
        Uri deviceUri = Devices.buildDeviceUri(deviceId);
        return get(deviceUri, contentResolver);
    }

    private static Device get(Uri deviceUri, ContentResolver contentResolver) {
        Cursor cursor = contentResolver.query(deviceUri, DevicesQuery.PROJECTION, null, null, null);
        cursor.moveToFirst();
        Device device = Device.from(cursor);
        cursor.close();
        return device;
    }

    private static void delete(String deviceId, ContentResolver contentResolver) {
        Uri uri = Devices.buildDeviceUri(deviceId);
        contentResolver.delete(uri, null, null);
    }

    public static Device from(Cursor cursor) {
        Device device = new Device();
        device.dbId = cursor.getLong(DevicesQuery._ID);
        device.deviceId = cursor.getString(DevicesQuery.DEVICE_ID);
        device.deviceName = cursor.getString(DevicesQuery.DEVICE_NAME);
        device.deviceCarrier = Carrier.getCarrier(cursor.getInt(DevicesQuery.DEVICE_CARRIER));
        device.deviceIsSmartphone = cursor.getInt(DevicesQuery.DEVICE_SMART_PHONE) > 0;
        device.deviceContractEndDate = LocalDate.parse(
                cursor.getString(DevicesQuery.DEVICE_CONTRACT_END_DATE));
        device.deviceNotificationType = cursor.getInt(DevicesQuery.DEVICE_NOTIFICATION_TYPE);
        return device;
    }

    public interface DevicesQuery {
        String[] PROJECTION = {
                BaseColumns._ID,
                Devices.DEVICE_ID,
                Devices.DEVICE_NAME,
                Devices.DEVICE_CARRIER,
                Devices.DEVICE_SMART_PHONE,
                Devices.DEVICE_CONTRACT_END_DATE,
                Devices.DEVICE_NOTIFICATION_TYPE
        };

        int _ID = 0;
        int DEVICE_ID = 1;
        int DEVICE_NAME = 2;
        int DEVICE_CARRIER = 3;
        int DEVICE_SMART_PHONE = 4;
        int DEVICE_CONTRACT_END_DATE = 5;
        int DEVICE_NOTIFICATION_TYPE = 6;
    }

}
