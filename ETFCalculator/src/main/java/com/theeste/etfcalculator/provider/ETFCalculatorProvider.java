package com.theeste.etfcalculator.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.theeste.etfcalculator.provider.ETFCalculatorContract.Devices;
import com.theeste.etfcalculator.provider.ETFCalculatorDatabase.Tables;

/**
 * Created by Ryan on 11/25/13.
 */
public class ETFCalculatorProvider extends ContentProvider {

    private static final int DEVICES = 1;
    private static final int DEVICE_ID = 2;

    private static final String PATH_DEVICES = ETFCalculatorContract.PATH_DEVICES;
    private static final String PATH_DEVICE_ID = PATH_DEVICES + "/*";

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ETFCalculatorContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, PATH_DEVICES, DEVICES);
        matcher.addURI(authority, PATH_DEVICE_ID, DEVICE_ID);

        return matcher;
    }

    private ETFCalculatorDatabase mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new ETFCalculatorDatabase(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case DEVICES:
                return Devices.CONTENT_TYPE;
            case DEVICE_ID:
                return Devices.CONTENT_ITEM_TYPE;

            default:
                throwUnknownUriException(uri);
        }
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mDbHelper.getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        int uriType = sUriMatcher.match(uri);
        switch (uriType) {
            case DEVICES:
                queryBuilder.setTables(Tables.DEVICES);
                break;
            case DEVICE_ID:
                queryBuilder.setTables(Tables.DEVICES);
                queryBuilder.appendWhere(Devices.DEVICE_ID + "='" + Devices.getId(uri) + "'");
                break;
            default:
                throwUnknownUriException(uri);
        }

        Cursor cursor = queryBuilder.query(
                db,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int uriType = sUriMatcher.match(uri);
        switch (uriType) {
            case DEVICES:
                db.insertOrThrow(Tables.DEVICES, null, contentValues);
                break;
            default:
                throwUnknownUriException(uri);
        }

        notifyChange(uri);
        return Devices.buildDeviceUri(contentValues.getAsString(Devices.DEVICE_ID));
    }

    @Override
    public int delete(Uri uri, String whereClause, String[] whereArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsAffected = 0;
        int uriType = sUriMatcher.match(uri);
        switch (uriType) {
            case DEVICES:
                rowsAffected = db.delete(Tables.DEVICES, whereClause, whereArgs);
                break;
            case DEVICE_ID:
                String id = Devices.getId(uri);
                if (TextUtils.isEmpty(whereClause)) {
                    rowsAffected = db.delete(
                            Tables.DEVICES,
                            Devices.DEVICE_ID + "='" + id + "'",
                            null);
                } else {
                    rowsAffected = db.delete(
                            Tables.DEVICES,
                            whereClause + " and " + Devices.DEVICE_ID + "='" + id + "'",
                            whereArgs);
                }
                break;
            default:
                throwUnknownUriException(uri);
        }

        notifyChange(uri);
        return rowsAffected;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String whereClause, String[] whereArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsAffected = 0;
        int uriType = sUriMatcher.match(uri);
        switch (uriType) {
            case DEVICES:
                rowsAffected = db.update(Tables.DEVICES, contentValues, whereClause, whereArgs);
                break;
            case DEVICE_ID:
                String id = Devices.getId(uri);
                if (TextUtils.isEmpty(whereClause)) {
                    rowsAffected = db.update(
                            Tables.DEVICES,
                            contentValues,
                            Devices.DEVICE_ID + "=" + id,
                            null);
                } else {
                    rowsAffected = db.update(
                            Tables.DEVICES,
                            contentValues,
                            whereClause + " and " + Devices.DEVICE_ID + "='" + id + "'",
                            whereArgs);
                }
                break;
            default:
                throwUnknownUriException(uri);
        }

        notifyChange(uri);
        return rowsAffected;
    }

    private void notifyChange(Uri uri) {
        Context context = getContext();
        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.notifyChange(uri, null);
    }

    private void throwUnknownUriException(Uri uri) {
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }
}
