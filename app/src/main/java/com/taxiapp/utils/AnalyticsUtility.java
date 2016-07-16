package com.taxiapp.utils;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.analytics.Tracker;

import java.util.Map;

/**
 * Created by Amit S on 01-12-2015.
 */
public class AnalyticsUtility {

//    public static AnalyticsUtility getInstance(Activity activity){
//        if (instance == null) {
//            instance = new AnalyticsUtility(activity);
//        }
//        return instance;
//    }

    public static void trackScreenView(Activity activity, String screenName) {
        gaTrackScreenView(activity, screenName);
    }

    // FOR GOOGLE ANALYTICS
    // ---------------------------------------------------------------------------------------------
    public static synchronized Tracker getGoogleAnalyticsTracker(Context activity) {
//        try {
//            AnalyticsTrackers.initialize(activity.getApplicationContext());
//            AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
//            return analyticsTrackers.get(AnalyticsTrackers.Target.APP);
//        } catch (Exception ex) {
//            Log.e("ANALYTICS", "Exception while getting tracker: ", ex);
//            return null;
//        }
        return null;
    }

    /***
     * Tracking screen view
     *
     * @param screenName screen name to be displayed on GA dashboard
     */
    public static void gaTrackScreenView(Activity activity, String screenName) {

    }

    /***
     * Tracking exception
     *
     * @param e exception to be tracked
     */
    public static void gaTrackException(Activity activity, Exception e) {

    }

    /***
     * Tracking event
     *
     * @param activity activity
     * @param category event category
     * @param action   action of the event
     * @param label    label
     */
    public static void sendGAEvent(Activity activity, String category, String action, String label) {

    }

    /***
     * Tracking event
     *
     * @param activity activity
     * @param category event category
     * @param action   action of the event
     * @param label    label
     */
    public static void sendGAEvent(Context activity, String category, String action, String label, Map<String, String> params) {

    }


    public static void sendGenericEvent(Context activity,
                                        String eventName,
                                        String category,
                                        String action,
                                        String label,
                                        Map<String, String> extraParams) {
        sendGAEvent(activity, category, action, label, extraParams);

    }
}
