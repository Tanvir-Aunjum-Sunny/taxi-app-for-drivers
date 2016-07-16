package com.taxiapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.telephony.TelephonyManager;

public class NetworkInfoProvider {
    public static boolean isRoaming(Context context) {
        ConnectivityManager cManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo actNetInfo = cManager.getActiveNetworkInfo();
        if (actNetInfo != null)
            return actNetInfo.isRoaming();
        return false;
    }

    public static String getHomeCarrierName(Context context) {
        TelephonyManager tpManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return (tpManager.getSimOperatorName());
    }

    public static String getCurrentCarrierName(Context context) {
        TelephonyManager tpManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return (tpManager.getNetworkOperatorName());
    }

    public static long getUsedMobileData() throws Exception {
        long mStartRX = TrafficStats.getMobileRxBytes();
        long mStartTX = TrafficStats.getMobileTxBytes();
        if (mStartRX == TrafficStats.UNSUPPORTED
                || mStartTX == TrafficStats.UNSUPPORTED) {
            throw new Exception("TrafficStats not supported");
        } else {
            return mStartRX + mStartTX;
        }

    }

    public static String getMobilePhoneNumber(Context context) {
        TelephonyManager mTelephonyMgr;
        mTelephonyMgr = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String number = mTelephonyMgr.getLine1Number();
        return number;

    }

    public static boolean isDataConnAvailable(Context context) {
        ConnectivityManager cManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo actNetInfo = cManager.getActiveNetworkInfo();
        if (actNetInfo != null)
            return actNetInfo.isConnected();
        return false;
    }
}
