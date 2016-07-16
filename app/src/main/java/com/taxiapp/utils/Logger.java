package com.taxiapp.utils;

import android.util.Log;

/**
 * This is to simplify all app logging under one tag.
 * 
 * @author Sreedevi.Jagannath
 */
public class Logger {

    private static final String tag_message_separator = "__";
    private static Logger _instance;
    /**
     * This is the tag to look for in logcat. ex: adb logcat -s TaxiApp
     */
    public static final String MASTER_TAG = "TaxiApp";

    public static Logger getInstance() {
        if (_instance == null) {
            _instance = new Logger();
        }
        return _instance;
    }

    private Logger() {
    }

    /**
     * Logs a DEBUG message to logcat, under the tag "TaxiApp"
     * 
     * @param message
     *            the message to be printed. Should not be null.
     */
    public void d(String message) {
        if (message == null) {
            message = "Empty";
        }
        Log.d(MASTER_TAG, message);
    }

    /**
     * Logs a DEBUG message to logcat, under the tag "TaxiApp", but prefixed
     * with the supplied "tag" param. ex: MyActivity__hello world! (we add the
     * __ between the tag and the message.
     * 
     * @param message
     *            the message to be printed. Should not be null.
     * @param tag
     *            the tag to identify which component wrote the log. Should not
     *            be null.
     */
    public void d(String tag, String message) {
        if (message == null) {
            message = "Empty";
        }
        if (tag == null) {
            tag = "Empty";
        }
        Log.d(MASTER_TAG, tag + tag_message_separator + message);
    }

    /**
     * Logs an ERROR message to logcat, under the tag "TaxiApp"
     * 
     * @param message
     *            the message to be printed. Should not be null.
     */
    public void e(String message) {
        if (message == null) {
            message = "Empty";
        }
        Log.e(MASTER_TAG, message);
    }

    /**
     * Logs an ERROR message to logcat, under the tag "TaxiApp", but prefixed
     * with the supplied "tag" param. ex: MyActivity__hello world! (we add the
     * __ between the tag and the message.
     * 
     * @param message
     *            the message to be printed. Should not be null.
     * @param tag
     *            the tag to identify which component wrote the log. Should not
     *            be null.
     */
    public void e(String tag, String message) {
        if (message == null) {
            message = "Empty";
        }
        if (tag == null) {
            tag = "Empty";
        }
        Log.e(MASTER_TAG, tag + tag_message_separator + message);
    }

    /**
     * Logs an INFO message to logcat, under the tag "TaxiApp"
     * 
     * @param message
     *            the message to be printed. Should not be null.
     */
    public void i(String message) {
        if (message == null) {
            message = "Empty";
        }
        Log.i(MASTER_TAG, message);
    }

    /**
     * Logs an INFO message to logcat, under the tag "TaxiApp", but prefixed
     * with the supplied "tag" param. ex: MyActivity__hello world! (we add the
     * __ between the tag and the message.
     * 
     * @param message
     *            the message to be printed. Should not be null.
     * @param tag
     *            the tag to identify which component wrote the log. Should not
     *            be null.
     */
    public void i(String tag, String message) {
        if (message == null) {
            message = "Empty";
        }
        if (tag == null) {
            tag = "Empty";
        }
        Log.i(MASTER_TAG, tag + tag_message_separator + message);
    }

    /**
     * Logs a VERBOSE message to logcat, under the tag "TaxiApp"
     * 
     * @param message
     *            the message to be printed. Should not be null.
     */
    public void v(String message) {
        if (message == null) {
            message = "Empty";
        }
        Log.v(MASTER_TAG, message);
    }

    /**
     * Logs a VERBOSE message to logcat, under the tag "TaxiApp", but prefixed
     * with the supplied "tag" param. ex: MyActivity__hello world! (we add the
     * __ between the tag and the message.
     * 
     * @param message
     *            the message to be printed. Should not be null.
     * @param tag
     *            the tag to identify which component wrote the log. Should not
     *            be null.
     */
    public void v(String tag, String message) {
        if (message == null) {
            message = "Empty";
        }
        if (tag == null) {
            tag = "Empty";
        }
        Log.v(MASTER_TAG, tag + tag_message_separator + message);
    }

    /**
     * Logs a WTF message to logcat, under the tag "TaxiApp"
     * 
     * @param message
     *            the message to be printed. Should not be null.
     */
    public void wtf(String message) {
        if (message == null) {
            message = "Empty";
        }
        Log.wtf(MASTER_TAG, message);
    }

    /**
     * Logs a WTF message to logcat, under the tag "TaxiApp", but prefixed with
     * the supplied "tag" param. ex: MyActivity__hello world! (we add the __
     * between the tag and the message.
     * 
     * @param message
     *            the message to be printed. Should not be null.
     * @param tag
     *            the tag to identify which component wrote the log. Should not
     *            be null.
     */
    public void wtf(String tag, String message) {
        if (message == null) {
            message = "Empty";
        }
        if (tag == null) {
            tag = "Empty";
        }
        Log.wtf(MASTER_TAG, tag + tag_message_separator + message);
    }
}
