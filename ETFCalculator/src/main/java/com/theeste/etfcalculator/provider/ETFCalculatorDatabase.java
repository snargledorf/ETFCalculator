package com.theeste.etfcalculator.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import static com.theeste.etfcalculator.provider.ETFCalculatorContract.DevicesColumns;

/**
 * Created by Ryan on 11/25/13.
 */
public class ETFCalculatorDatabase extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ETFCalculator.db";

    interface Tables {
        String DEVICES = "devices";
    }

    public ETFCalculatorDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Tables.DEVICES + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY,"
                + DevicesColumns.DEVICE_ID + " TEXT NOT NULL,"
                + DevicesColumns.DEVICE_NAME + " TEXT NOT NULL,"
                + DevicesColumns.DEVICE_CARRIER + " INTEGER NOT NULL,"
                + DevicesColumns.DEVICE_SMART_PHONE + " INTEGER NOT NULL,"
                + DevicesColumns.DEVICE_CONTRACT_END_DATE + " TEXT NOT NULL,"
                + DevicesColumns.DEVICE_NOTIFICATION_TYPE + " INTEGER NOT NULL,"
                + "UNIQUE (" + DevicesColumns.DEVICE_ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        // Nothing to upgrade yet
    }
}
