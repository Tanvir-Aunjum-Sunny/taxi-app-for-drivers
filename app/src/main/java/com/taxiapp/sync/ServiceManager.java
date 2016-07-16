package com.taxiapp.sync;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class ServiceManager {


    public static void scheduleMasterSyncService(Context context) {
        try {
            AlarmManager alarmManager = (AlarmManager) context
                    .getSystemService(Context.ALARM_SERVICE);

            long time = 5 * 60 * 1000;
            Intent intent = new Intent(context, SyncService.class);
            PendingIntent pIntent = PendingIntent.getService(context, 0, intent, 0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis(),
                    time, pIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeMasterSyncService(Context context) {
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, SyncService.class);
        PendingIntent pIntent = PendingIntent.getService(context, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pIntent);


    }

    public static void scheduleAllServices(Context context) {
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, SyncService.class);
        PendingIntent pIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                System.currentTimeMillis(),
                5 * 60 * 1000, pIntent);


    }

    public static void removeScheduledServices(Context context) {
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, SyncService.class);
        PendingIntent pIntent = PendingIntent.getService(context, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pIntent);


    }


}
