package com.taxiapp.reciever;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.taxiapp.app.activities.HomeActivity;
import com.taxiapp.app.activities.SplashActivity;
import com.taxiapp.vendor.app.R;

/**
 * Created by Aslam on 05-11-2015.
 */
public class TaxiAppGCMReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("PUSH_NOTIFICATION", "PUSH NOTIFICATION RECEIVED");
        String message = "";
        if (intent.getExtras().containsKey("mp_message")) {
            message = intent.getExtras().getString("mp_message");
        } else {
            if (intent.getExtras().containsKey("message")) {
                message = intent.getExtras().getString("message");
                generateNotification(context, message);
                Log.d("Message: ", message);
            }
        }

    }


    private void generateNotification(Context context, String msg) {
        Log.d("PUSH_NOTIFICATION", "GENERATING NOTIFICATION");
        Intent intent = new Intent(context, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(HomeActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        NotificationManager mNotifM = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("TaxiApp")
                .setContentText(msg);
        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(resultPendingIntent);
        mNotifM.notify(1, mBuilder.build());
    }

}
