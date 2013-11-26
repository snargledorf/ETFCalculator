package com.theeste.etfcalculator2;

import android.provider.BaseColumns;

/**
 * Created by Ryan on 11/25/13.
 */
public class DeviceEntry implements BaseColumns {
    public static final String TABLE_NAME = "devices";
    public static final String NAME = "name";
    public static final String CARRIER = "carrier";
    public static final String SMART_PHONE = "smartphone";
    public static final String CONTRACT_END_DATE = "contract_end_date";
    public static final String NOTIFICATION_TYPE = "notification_type";
    public static final String[] ALL_COLUMNS = {
            _ID,
            NAME,
            CARRIER,
            SMART_PHONE,
            CONTRACT_END_DATE,
            NOTIFICATION_TYPE
    };

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    NAME + " TEXT," +
                    CARRIER + " INTEGER," +
                    SMART_PHONE + " INTEGER," +
                    CONTRACT_END_DATE + " TEXT," +
                    NOTIFICATION_TYPE + " INTEGER)";
}
