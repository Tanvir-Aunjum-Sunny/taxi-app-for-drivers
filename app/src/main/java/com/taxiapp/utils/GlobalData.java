package com.taxiapp.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.taxiapp.model.BookingSession;
import com.taxiapp.model.DeviceHardwareInfo;
import com.taxiapp.model.business.Booking;
import com.taxiapp.sync.AppSettings;

import java.io.IOException;
import java.util.List;

/**
 * Created by Amit S on 25/11/15.
 */
public class GlobalData {
    private static GlobalData ourInstance;
    private AppSettings appSettings;
    private BookingSession bookingSession;
    private Gson gson = new Gson();
    private List<Booking> bookingList;


    public static void init(Context ctx) {
        ourInstance = new GlobalData(ctx);
    }

    public static GlobalData get() {
        return ourInstance;
    }


    private DeviceHardwareInfo deviceHardwareInfo = new DeviceHardwareInfo();

    private GlobalData(Context ctx) {
        try {
            deviceHardwareInfo = DeviceInfoDesk.getDeviceHardwareInfo(ctx);
            if (PreferencesUtil.contains(ctx, AppSettings.KEY)) {
                appSettings = PreferencesUtil.get(ctx, AppSettings.KEY, AppSettings.class);

            } else {
                appSettings = new AppSettings();
                PreferencesUtil.put(ctx, AppSettings.KEY, appSettings);
            }

            getBookingSession(ctx);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DeviceHardwareInfo getDeviceHardwareInfo() {
        return deviceHardwareInfo;
    }

    public void setDeviceHardwareInfo(DeviceHardwareInfo deviceHardwareInfo) {
        this.deviceHardwareInfo = deviceHardwareInfo;
    }

    public AppSettings getAppSettings() {
        return appSettings;
    }

    public synchronized void setAppSettings(AppSettings appSettings, Context ctx) {
        this.appSettings = appSettings;
        PreferencesUtil.put(ctx, AppSettings.KEY, appSettings);
    }

    public BookingSession getBookingSession(Context ctx) {
        if (bookingSession == null) {
            bookingSession = PreferencesUtil.get(ctx, AppConstants.BOOKING_STATE_KEY, BookingSession.class);
        }
        return bookingSession;
    }

    public void setBookingSession(Context ctx, BookingSession bookingSession) {
        this.bookingSession = bookingSession;
        PreferencesUtil.putString(ctx, AppConstants.BOOKING_STATE_KEY, gson.toJson(bookingSession));
    }


    //Used only for New Trips screen
    public List<Booking> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }
}
