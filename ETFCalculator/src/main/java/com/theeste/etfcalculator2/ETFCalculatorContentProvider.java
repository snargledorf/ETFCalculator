package com.theeste.etfcalculator2;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import org.joda.time.LocalDate;

/**
 * Created by Ryan on 11/25/13.
 */
public class ETFCalculatorContentProvider extends ContentProvider {

    private static final String AUTHORITY = "com.theeste.etfcalculator2.provider";
    public static final Uri DEVICES_URI = Uri.parse("content://" + AUTHORITY
            + "/" + DeviceEntry.TABLE_NAME);

    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/device";
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/devices";

    private static final int DEVICES = 1;
    private static final int DEVICE_ID = DEVICES + 1;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, DeviceEntry.TABLE_NAME, DEVICES);
        sUriMatcher.addURI(AUTHORITY, DeviceEntry.TABLE_NAME + "/#", DEVICE_ID);
    }

    private ETFCalculatorDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new ETFCalculatorDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] columns, String whereClause,
                        String[] whereClauseArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DeviceEntry.TABLE_NAME);
        int uriType = sUriMatcher.match(uri);
        switch (uriType) {
            case DEVICES:
                break;
            case DEVICE_ID:
                queryBuilder.appendWhere(DeviceEntry._ID + " = " + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        Cursor cursor = queryBuilder.query(mDbHelper.getReadableDatabase(),
                columns, whereClause, whereClauseArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase sqlDB = mDbHelper.getWritableDatabase();
        long id = 0;
        int uriType = sUriMatcher.match(uri);
        switch (uriType) {
            case DEVICES:
                id = sqlDB.insert(DeviceEntry.TABLE_NAME, null, contentValues);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(DEVICES_URI + "/" + id);
    }

    @Override
    public int delete(Uri uri, String whereClause, String[] whereArgs) {
        SQLiteDatabase sqlDB = mDbHelper.getWritableDatabase();
        int rowsAffected = 0;
        int uriType = sUriMatcher.match(uri);
        switch (uriType) {
            case DEVICES:
                rowsAffected = sqlDB.delete(DeviceEntry.TABLE_NAME,
                        whereClause,
                        whereArgs);
                break;
            case DEVICE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(whereClause)) {
                    rowsAffected = sqlDB.delete(DeviceEntry.TABLE_NAME,
                            DeviceEntry._ID + "=" + id, null);
                } else {
                    rowsAffected = sqlDB.delete(DeviceEntry.TABLE_NAME,
                            whereClause + " and " + DeviceEntry._ID + "=" + id,
                            whereArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown or Invalid URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsAffected;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String whereClause, String[] whereArgs) {
        SQLiteDatabase sqlDB = mDbHelper.getWritableDatabase();
        int rowsAffected = 0;
        int uriType = sUriMatcher.match(uri);
        switch (uriType) {
            case DEVICES:
                rowsAffected =
                        sqlDB.update(DeviceEntry.TABLE_NAME,
                                contentValues,
                                whereClause,
                                whereArgs);
                break;
            case DEVICE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(whereClause)) {
                    rowsAffected = sqlDB.update(DeviceEntry.TABLE_NAME,
                            contentValues,
                            DeviceEntry._ID + "=" + id, null);
                } else {
                    rowsAffected = sqlDB.update(DeviceEntry.TABLE_NAME,
                            contentValues,
                            whereClause + " and " + DeviceEntry._ID + "=" + id,
                            whereArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsAffected;
    }

    public static Device createDevice(String name,
                                      int carrier,
                                      boolean smartPhone,
                                      LocalDate contractEndDate,
                                      int notificationType,
                                      ContentResolver contentResolver) {

        ContentValues values = new ContentValues();
        values.put(DeviceEntry.NAME, name);
        values.put(DeviceEntry.CARRIER, carrier);
        values.put(DeviceEntry.SMART_PHONE, smartPhone ? 1 : 0);
        values.put(DeviceEntry.CONTRACT_END_DATE, contractEndDate.toString());
        values.put(DeviceEntry.NOTIFICATION_TYPE, notificationType);

        Uri deviceUri = contentResolver.insert(
                ETFCalculatorContentProvider.DEVICES_URI,
                values);

        Cursor cursor = contentResolver.query(deviceUri, null, null, null, null);
        cursor.moveToFirst();
        Device device = Device.from(cursor);
        cursor.close();

        return device;
    }

    public static void deleteDevice(Device device, ContentResolver contentResolver) {
        Uri uri = Uri.parse(ETFCalculatorContentProvider.DEVICES_URI + "/" + device.getId());
        contentResolver.delete(uri, null, null);
    }
}
