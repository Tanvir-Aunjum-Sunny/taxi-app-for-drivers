package com.taxiapp.utils;

import android.content.Context;

import com.taxiapp.model.UserOtpToken;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class AppConstants {

    //15 minutes.
    public static final long APP_LOCK_TIME_INTERVAL = 15 * 60 * 1000;

    public static final String USER_INFO = "*TM@6S3!Un^ay_Q-7s";

    public static final String PHONE_NUMBER_KEY = "phoneNumberKey";
    public static final String APP_INITIALIZED = "appInitializedKey";

    //We store the OTP in different name, just for security purpose.
    public static final String PREFS_KEY = "prefsKey";

    public static final String CUSTOMER_CARE_NUMBER = "09045450000";

    public static final String USER_DETAILS = "userDetails";
    public static final String IMEIID = "IMEIID";
    // public static final String FLURRY_API_KEY = "PMY3VQ7269QG4YXV9XJV";//Smargav key
    public static final String FLURRY_API_KEY = "NQGQPKJZ9S3TZH3DSDWJ"; // TaxiApp  Key

    // GOOGLE SENDER ID
    public static final String GOOGLE_SENDER_ID = "166073051920";
    // MIXPANEL TOKEN KEY
    public static final String MIXPANEL_TOKEN = "297e7611c82bbb7be4937563f1e2cdb5";
    //public static final String MIXPANEL_TOKEN = "f9c9bf63e1542da9d2fde165ee19f13a";


    public static final String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov",
            "Dec"};

    public static final String BOOKING_STATE_KEY = "booking_state";
    public static final int DISTANCE_UPDATE_IN_INTERVAL = 2000;
    //public static final int TWO_MINUTES = 2 * 60 * 1000;
    public static final String DEVICE_DETAILS_UPD_TIME_KEY = "deviceDataUpdateTimeKey";

    public static DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("dd-MMM-yyyy");


    public static UserOtpToken getToken(Context ctx) {
        try {
//            String jwt = PreferencesUtil.getString(ctx, AppConstants.USER_INFO, null);
//            if (jwt == null) {
//                return null;
//            }

            UserOtpToken token = PreferencesUtil.get(ctx, AppConstants.USER_INFO, UserOtpToken.class);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
