package com.taxiapp.sync;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.j256.ormlite.dao.CloseableWrappedIterable;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.taxiapp.db.DriverLocationDAO;
import com.taxiapp.db.TransactionDBManager;
import com.taxiapp.model.DriverLocation;
import com.taxiapp.model.UserOtpToken;
import com.taxiapp.net.NetResponse;
import com.taxiapp.net.api.APIUtils;
import com.taxiapp.net.api.SyncStatus;
import com.taxiapp.utils.AppConstants;
import com.taxiapp.utils.AppLogger;
import com.taxiapp.utils.GlobalData;
import com.taxiapp.utils.PreferencesUtil;

import java.sql.SQLException;
import java.util.Iterator;

@SuppressWarnings("unchecked")
public class SyncService extends Service {
    public static final String SET_ALARM = "com.smargav.SyncService.SET_ALARM";
    public static String RUN_SYNC = "com.smargav.SyncService.RUN_SYNC";
    public static String RUN_SYNC_MANDATORY = "com.smargav.SyncService.RUN_SYNC_MANDATORY";
    private SyncRunner runner;
    private static final long TIME_INTERVAL = 5 * 60 * 1000;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null && (SET_ALARM.equals(intent.getAction()))) {
            resetAlarm();
            return START_STICKY;
        }
        if (intent != null
                && (RUN_SYNC.equals(intent.getAction()) || RUN_SYNC_MANDATORY.equals(intent.getAction()))) {

            if (RUN_SYNC_MANDATORY.equals(intent.getAction())) {
                AppSettings settings = GlobalData.get().getAppSettings();
                settings.setSyncRunning(false);
                GlobalData.get().setAppSettings(settings, this);
            }

            runSync();
            return START_STICKY;
        }

        long value = PreferencesUtil.getLong(this, ALARM_SERVICE, 0);
        if (!PreferencesUtil.contains(this, ALARM_SERVICE) || value != TIME_INTERVAL) {
            resetAlarm();
            PreferencesUtil.putLong(this, ALARM_SERVICE, TIME_INTERVAL);
        }

        runSync();
        return START_NOT_STICKY;
    }

    private void runSync() {
        runner = new SyncRunner();
        // if (runner != null && runner.isAlive()) {
        // runner.cancel();
        // }
        // ThreadUtils.sleepInSec(2);
        runner.start();
    }

    private void resetAlarm() {

        AlarmManager mgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent(this, SyncService.class);
        i.setAction(RUN_SYNC);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        mgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + TIME_INTERVAL,
                TIME_INTERVAL, pi);

        // run once every 1 hours..
        long timeInterval = AlarmManager.INTERVAL_HOUR;
        i = new Intent(this, SyncService.class);
        i.setAction(RUN_SYNC_MANDATORY);
        pi = PendingIntent.getService(this, 0, i, 0);
        mgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + timeInterval,
                timeInterval, pi);

    }

    private class SyncRunner extends Thread {

        public void run() {
            if (GlobalData.get() == null)
                GlobalData.init(getApplicationContext());
            AppSettings settings = GlobalData.get().getAppSettings();


            if (settings.isSyncRunning()) {
                AppLogger.get().i(getClass(), "Sync already in progress. Will try again later.");
                return;
            }

            try {

                AppLogger.get().i(getClass(), "Starting Sync Service.");
                settings.setSyncRunning(true);

                syncLocationData();
            } catch (Exception e) {
                e.printStackTrace();
                AppLogger.get().e(getClass(), e);
            }

            settings.setSyncRunning(false);
            settings.setLastSyncRunTime(System.currentTimeMillis());
            GlobalData.get().setAppSettings(settings, getApplicationContext());

        }

        public void cancel() {
            AppSettings settings = GlobalData.get().getAppSettings();
            settings.setSyncRunning(false);
            interrupt();
        }
    }

    public void syncLocationData() {

        UserOtpToken token = AppConstants.getToken(SyncService.this);

        if (token == null) {
            AppLogger.get().e(getClass(), "User token could not be loaded. Check if app is still active?");
            return;
        }
        CloseableWrappedIterable<DriverLocation> list = null;
        boolean isSynced = true;
        int successCount = 0;
        int failedCount = 0;
        try {

            DriverLocationDAO dao = TransactionDBManager.getInstance().getDriverLocationDAO();
            QueryBuilder<DriverLocation, Integer> builder = dao.queryBuilder();
            Where<DriverLocation, Integer> where = builder.where();
            where.ne("syncStatus", SyncStatus.SUCCESS);
            PreparedQuery<DriverLocation> query = builder.prepare();
            list = dao.getWrappedIterable(query);
            Iterator<DriverLocation> it = list.closeableIterator();
            DriverLocation details = null;
            while (it.hasNext()) {
                boolean isSuccess = false;
                try { // let sync continue anyways.
                    details = it.next();

                    NetResponse response = APIUtils.sendLocationData(token, details);

                    isSuccess = response != null;
                    if (!isSuccess) {
                        response = APIUtils.sendLocationData(token, details);
                    }

                    isSuccess = response != null;
                    if (!isSuccess) {
                        AppLogger.get().e(SyncService.class, "Error synchronizing DriverLocation details - " + details.toString());
                        continue;
                    }


                    details.setSyncStatus(isSuccess ? SyncStatus.SUCCESS : SyncStatus.FAILED);
                    dao.createOrUpdate(details);

                } catch (Exception e) {
                    e.printStackTrace();
                    details.setSyncStatus(SyncStatus.FAILED);
                    dao.createOrUpdate(details);

                }

                if (isSuccess) {
                    ++successCount;
                    AppLogger.get().i(SyncService.class, "DriverLocation Sync Success: " + details.toString());
                } else {
                    ++failedCount;
                    AppLogger.get().i(SyncService.class, "DriverLocation Sync Failed: " + details.toString());
                }

            }//while ends
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (list != null)
                try {
                    list.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

        }


        if (successCount > 0 || failedCount > 0) {
            AppLogger.get().i(SyncService.class, "DriverLocation Details Sync - Success: " + successCount + " Failed: " + failedCount);
        }

    }


}
