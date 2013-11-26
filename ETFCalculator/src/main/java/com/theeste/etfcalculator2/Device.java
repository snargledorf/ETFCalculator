package com.theeste.etfcalculator2;

import android.database.Cursor;

import org.joda.time.LocalDate;

/**
 * Created by Ryan on 11/25/13.
 */
public class Device {
    private long mId;
    private String mName;
    private Carrier mCarrier;
    private LocalDate mContractEndDate;
    private int mNotificationType;
    private boolean mIsSmartPhone;

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public Carrier getCarrier() {
        return mCarrier;
    }

    public void setCarrier(Carrier mCarrier) {
        this.mCarrier = mCarrier;
    }

    public LocalDate getContractEndDate() {
        return mContractEndDate;
    }

    public void setContractEndDate(LocalDate mContractEndDate) {
        this.mContractEndDate = mContractEndDate;
    }

    public int getNotificationType() {
        return mNotificationType;
    }

    public void setNotificationType(int mNotificationType) {
        this.mNotificationType = mNotificationType;
    }

    public boolean isSmartPhone() {
        return mIsSmartPhone;
    }

    public void isSmartPhone(boolean isSmartPhone) {
        mIsSmartPhone = isSmartPhone;
    }

    public static Device from(Cursor cursor) {
        Device device = new Device();
        int position = 0;
        device.setId(cursor.getLong(position++));
        device.setName(cursor.getString(position++));
        device.setCarrier(Carrier.getCarrierInstance(cursor.getInt(position++)));
        device.isSmartPhone(cursor.getInt(position++) > 0);
        device.setContractEndDate(LocalDate.parse(cursor.getString(position++)));
        device.setNotificationType(cursor.getInt(position++));
        return device;
    }
}
