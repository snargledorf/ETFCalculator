package com.theeste.etfcalculator2;

import org.joda.time.LocalDate;

/**
 * Created by Ryan on 11/25/13.
 */
public class Device {
    private long mId;
    private String mName;
    private int mCarrier;
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

    public int getCarrier() {
        return mCarrier;
    }

    public void setCarrier(int mCarrier) {
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
}
