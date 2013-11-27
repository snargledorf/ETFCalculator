package com.theeste.etfcalculator.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Ryan on 11/27/13.
 */
public class ETFCalculatorContract {
    public static final String CONTENT_AUTHORITY = ETFCalculatorProvider.class.getPackage().getName();
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_DEVICES = "devices";

    interface DevicesColumns {
        String DEVICE_ID = "device_id";
        String DEVICE_NAME = "device_name";
        String DEVICE_CARRIER = "device_carrier";
        String DEVICE_SMART_PHONE = "device_smartphone";
        String DEVICE_CONTRACT_END_DATE = "device_contract_end_date";
        String DEVICE_NOTIFICATION_TYPE = "device_notification_type";
    }

    public static class Devices implements DevicesColumns, BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_DEVICES).build();

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
                + "/device";
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
                + "/devices";

        public static Uri buildDeviceUri(String deviceId) {
            return CONTENT_URI.buildUpon().appendPath(deviceId).build();
        }

        public static String getId(Uri uri) {
            return uri.getLastPathSegment();
        }

        public static String generateDeviceId(String name, long date) {
            return name.replace(' ', '_') + "_" + date;
        }
    }
}
