package com.taxiapp.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.taxiapp.app.activities.SplashActivity;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;
import java.util.Locale;

public class UnCaughtException implements UncaughtExceptionHandler {
    private Context context;
    final UncaughtExceptionHandler defaultHandler = Thread
            .getDefaultUncaughtExceptionHandler();

    public UnCaughtException(Context ctx) {
        context = ctx;
    }

    private StatFs getStatFs() {
        File path = Environment.getDataDirectory();
        return new StatFs(path.getPath());
    }

    private long getAvailableInternalMemorySize(StatFs stat) {
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    private long getTotalInternalMemorySize(StatFs stat) {
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    private void addInformation(StringBuilder message) {
        message.append("Locale: ").append(Locale.getDefault()).append('\n');
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi;
            pi = pm.getPackageInfo(context.getPackageName(), 0);
            message.append("Version: ").append(pi.versionName).append('\n');
            message.append("Package: ").append(pi.packageName).append('\n');
        } catch (Exception e) {
            Log.e("CustomExceptionHandler", "Error", e);
            message.append("Could not get Version information for ").append(
                    context.getPackageName());
        }
        message.append("Phone Model: ").append(android.os.Build.MODEL)
                .append('\n');
        message.append("Android Version: ")
                .append(android.os.Build.VERSION.RELEASE).append('\n');
        message.append("Board: ").append(android.os.Build.BOARD).append('\n');
        message.append("Brand: ").append(android.os.Build.BRAND).append('\n');
        message.append("Device: ").append(android.os.Build.DEVICE).append('\n');
        message.append("Host: ").append(android.os.Build.HOST).append('\n');
        message.append("ID: ").append(android.os.Build.ID).append('\n');
        message.append("Model: ").append(android.os.Build.MODEL).append('\n');
        message.append("Product: ").append(android.os.Build.PRODUCT)
                .append('\n');
        message.append("Type: ").append(android.os.Build.TYPE).append('\n');
        StatFs stat = getStatFs();
        message.append("Total Internal memory: ")
                .append(getTotalInternalMemorySize(stat)).append('\n');
        message.append("Available Internal memory: ")
                .append(getAvailableInternalMemorySize(stat)).append('\n');
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        try {
            StringBuilder report = new StringBuilder();
            Date curDate = new Date();
            report.append("Error Report collected on : ")
                    .append(curDate.toString()).append('\n').append('\n');
            report.append("Informations :").append('\n');
            addInformation(report);
            report.append('\n').append('\n');
            report.append("Stack:\n");
            final Writer result = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(result);
            e.printStackTrace(printWriter);
            report.append(result.toString());
            printWriter.close();
            report.append('\n');
            report.append("**** End of current Report ***");
            Log.e(UnCaughtException.class.getName(),
                    "Error while sendErrorMail" + report);
            SuperLogger logger = SuperLogger.getInstance();
            logger.logAppCrash(report.toString(), context);
            sendErrorMail(report.toString(), e);

            AlarmManager mgr = (AlarmManager) context
                    .getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(context, SplashActivity.class);
            PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 3000, pi);
            if (defaultHandler != null)
                defaultHandler.uncaughtException(t, e);
            System.exit(2);
        } catch (Throwable ignore) {
            Log.e(UnCaughtException.class.getName(),
                    "Error while sending error e-mail", ignore);
        }
    }

    /**
     * This method for call alert dialog when application crashed!
     */
    public void sendErrorMail(String msg, final Throwable e) {
        /*
		 * final AlertDialog.Builder builder = new
		 * AlertDialog.Builder(context1); builder.setTitle("Sorry...!");
		 * builder.create(); builder.setNegativeButton("Cancel", new
		 * DialogInterface.OnClickListener() {
		 *
		 * @Override public void onClick(DialogInterface dialog, int which) {
		 * System.exit(0); } }); builder.setPositiveButton("Report", new
		 * DialogInterface.OnClickListener() {
		 *
		 * @Override public void onClick(DialogInterface dialog, int which) {
		 */
        context.startActivity(SuperLogger.getEmailIntent(context, msg));
        System.exit(0);
		/*
		 * } }); builder.setMessage("Oops,Your application has crashed");
		 * builder.show(); //System.exit(0);
		 */

    }
}