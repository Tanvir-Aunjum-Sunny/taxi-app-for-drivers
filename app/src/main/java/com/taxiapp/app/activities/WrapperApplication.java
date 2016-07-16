package com.taxiapp.app.activities;

import android.app.Application;
import android.util.Log;
import android.view.ViewConfiguration;

import com.taxiapp.db.TransactionDBManager;
import com.taxiapp.net.api.APIUrl;
import com.taxiapp.utils.AppLogger;
import com.taxiapp.utils.GlobalData;
import com.taxiapp.utils.PreferencesUtil;
import com.taxiapp.utils.UnCaughtException;
import com.taxiapp.utils.Utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Main entry point to the Application process. We launch all the Initialization
 * threads here.
 *
 * @author Amit
 */
public class WrapperApplication extends Application {

    private static boolean activityVisible;

    public void onCreate() {
        super.onCreate();
        try {
            Utils.turnGPSOn(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        initConfig();
    }

    private void initConfig() {
        Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(this));

        //Initialize Log folders.
        try {
            FileUtils.forceMkdir(new File(AppLogger.SD_CARD + AppLogger.RELATIVE_BASE_FOLDER));
            FileUtils.forceMkdir(new File(AppLogger.LOG_DIR));
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (PreferencesUtil.contains(WrapperApplication.this, APIUrl.BASE_URL.name())) {
            APIUrl.BASE_URL.setPath(PreferencesUtil.getString(WrapperApplication.this, APIUrl.BASE_URL.name(), APIUrl.BASE_URL.getFullPath()));
        }


        AppLogger.init(this);
        TransactionDBManager.init(this);
//        GlobalData.init(this);

        //We initialize other required Global classes in thread.
        new Thread() {
            public void run() {
                initialize();
            }
        }.start();

        makeActionOverflowMenuShown();
    }


    private void makeActionOverflowMenuShown() {
        //devices with hardware menu button (e.g. Samsung Note) don't show action overflow menu
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            Log.d("OverflowMenuException", e.getLocalizedMessage());
        }
    }

    /**
     * This should be run inside a thread. Always.
     */
    private void initialize() {
        GlobalData.init(this);
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }


//
//    public void shutdownLocationLib() {
//        try {
//            LocationLibrary.stopAlarmAndListener(this);
//
//        } catch (UnsupportedOperationException ex) {
//            AppLogger.get().i(getClass(),
//                    "UnsupportedOperationException thrown - the device doesn't have any location providers");
//        }
//    }
}

