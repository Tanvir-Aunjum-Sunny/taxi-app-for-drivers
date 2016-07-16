package com.taxiapp.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.PowerManager;

public abstract class ProgressAsyncTask extends AsyncTask<String, String, Integer> {
    public static final int SUCCESS = 0;
    public static final int FAILED = 1;
    public static final int NO_RESULT = 2;
    public static final int NO_NETWORK = 3;
    private static final long WAKE_LOCK_TIMEOUT = 30;
    public final static int ONLINE_SUBMIT_PROMPT = 0x123123;
    public final static int OFFLINE_SUBMIT_PROMPT = 0x123125;

    protected String errorMessage = "Some unknown error occured. Please try again later";
    public Context ctx;
    public ProgressDialog dialog;
    private boolean isSilent = false;

    public ProgressAsyncTask(Context ctx) {
        this.ctx = ctx;
    }

    public ProgressAsyncTask(Context ctx, boolean isSilent) {
        this.ctx = ctx;
        this.isSilent = isSilent;
    }

    @Override
    public void onPreExecute() {
        super.onPreExecute();

        if (!isSilent) {
            dialog = ProgressDialog.show(ctx, "", "Processing...");
            dialog.setCancelable(false);
            dialog.show();
            ((PowerManager) ctx.getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.PARTIAL_WAKE_LOCK
                            | PowerManager.ON_AFTER_RELEASE,
                    "ProgressAsyncTask").acquire(WAKE_LOCK_TIMEOUT);
        }
    }

    @Override
    public void onProgressUpdate(String... params) {
        if (!isSilent) {
            dialog.setMessage(params[0]);
        }
    }

    @Override
    public void onPostExecute(Integer result) {
        if (!isSilent) {
            dialog.dismiss();
        }

        if (result == NO_NETWORK) {
            Utils.showPrompt((Activity) ctx, "Error", "Please connect to internet and retry");
            return;
        }

        super.onPostExecute(result);
    }

//    public void publishProgress(String p, int num) {
//        publishProgress(p, "" + num);
//    }

}
