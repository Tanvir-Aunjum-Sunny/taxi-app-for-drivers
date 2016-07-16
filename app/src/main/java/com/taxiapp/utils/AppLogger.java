package com.taxiapp.utils;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.google.code.microlog4android.Level;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
import com.google.code.microlog4android.appender.FileAppender;
import com.google.code.microlog4android.format.PatternFormatter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings(value = {"rawtypes"})
public class AppLogger {

    private Logger logger = LoggerFactory.getLogger();
    private File appLogsFileName;

    public static final String SD_CARD = "/sdcard";
    public static final String RELATIVE_BASE_FOLDER = "/taxiappdriver/logs/";
    public static final String LOG_DIR = SD_CARD + RELATIVE_BASE_FOLDER;

    private FileAppender fileAppender;

    private static AppLogger INSTANCE;

    public static void init(Context ctx) {
        if (INSTANCE == null) {
            INSTANCE = new AppLogger(ctx);
        }
    }

    public static AppLogger get() {
        if (INSTANCE == null) {
            return new AppLogger();
        }
        return INSTANCE;
    }

    private AppLogger(Context ctx) {
        initLogger(ctx);
    }

    private AppLogger() {

    }

    private void initLogger(Context ctx) {

        try {

            File logDir = new File(LOG_DIR);

            if (!logDir.exists()) {
                logDir.mkdirs();
            }

            purgeLogs();
            long date = System.currentTimeMillis();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
            String logFile = RELATIVE_BASE_FOLDER + dateFormat.format(date) + "_log.txt";

            appLogsFileName = new File(SD_CARD + logFile);

            fileAppender = new FileAppender();
            fileAppender.setFormatter(new LogFormatter());
            fileAppender.setAppend(true);
            fileAppender.setFileName(logFile);

            logger.addAppender(fileAppender);
            logger.setLevel(Level.INFO);

            try {
                String versionName = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionName;
                int versionCode = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionCode;
                String info = "App Version - " + versionName + " (" + versionCode + ")";
                i(AppLogger.class, "App start time -  " + new Date());
                i(AppLogger.class, info);
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }

        } catch (Exception ex) {
            System.out.println("ex: " + ex);
        }

    }

    private void checkFile() {
        try {
            if (!appLogsFileName.exists())
                //appLogsFileName.createNewFile();
                fileAppender.open();
        } catch (Exception e) {
        }
    }

    public void e(Class c, String msg) {
        String tag = getClassName(c);
        Log.e(tag, msg);
        if (logger != null)
            logger.error(tag + ": " + msg);
    }

    public void e(Class c, Throwable e) {
        String tag = getClassName(c);
        Log.e(tag, e.getMessage(), e);
        if (logger != null)
            logger.error(tag + ": " + e.getLocalizedMessage(), e);
    }

    public void e(Class c, String msg, Exception thr) {
        String tag = getClassName(c);
        Log.e(tag, msg, thr);
        if (logger != null)
            logger.error(tag, thr);
    }

    public void d(Class c, String msg) {
        String tag = getClassName(c);
        Log.d(tag, msg);
        if (logger != null)
            logger.debug(tag + " - " + msg);
    }

    public void w(Class c, String msg) {
        String tag = getClassName(c);
        Log.w(tag, msg);
        if (logger != null)
            logger.warn(tag + " - " + msg);
    }

    public void i(Class c, String msg) {
        String tag = getClassName(c);
        Log.i(tag, msg);
        if (logger != null)
            logger.info(tag + " - " + msg);

    }

    public String getClassName(Class c) {
        checkFile();
        return "SGV[" + c.getSimpleName() + "] ";
    }

    public File getAppLogsFileName() {
        return appLogsFileName;
    }

    public void setAppLogsFileName(File appLogsFileName) {
        this.appLogsFileName = appLogsFileName;
    }

    public void purgeLogs() {
        try {
            File jfsLogDir = new File(LOG_DIR);
            File[] list = jfsLogDir.listFiles();
            // if file is older than 7 days delete.
            long purgeDuration = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000;
            for (File file : list) {
                if (file.lastModified() < purgeDuration) {
                    file.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class LogFormatter extends PatternFormatter {

        public LogFormatter() {
            setPattern("%d %c{1} [%P] %m %T");
        }

        public String format(String clientID, String name, long time, Level level, Object message,
                             Throwable t) {
            return super.format(clientID, name, System.currentTimeMillis(), level, message, t);
        }
    }
}
