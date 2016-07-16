package com.taxiapp.app.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import com.taxiapp.model.UserOtpToken;
import com.taxiapp.sync.SyncService;
import com.taxiapp.utils.AppConstants;
import com.taxiapp.utils.AppLogger;
import com.taxiapp.utils.PreferencesUtil;
import com.taxiapp.utils.ProgressAsyncTask;
import com.taxiapp.utils.Utils;
import com.taxiapp.vendor.app.R;

public class SplashActivity extends BaseActivity {

    // VARIABLE FOR MIXPANEL ANALYTICS


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                initApp();
//            }
//        }, 2500);


        startBgInit();

        initApp();

        syncPendingData();
    }

    public void syncPendingData() {
        try {

            Intent intent = new Intent(SplashActivity.this, SyncService.class);
            intent.setAction(SyncService.SET_ALARM);
            startService(intent);

//            ServiceManager.removeScheduledServices(this);
//            ServiceManager.scheduleAllServices(this);
        } catch (Exception e) {
            e.printStackTrace();
            AppLogger.get().e(getClass(), e);

        }

    }


    private class CheckerAsyncTask extends ProgressAsyncTask {

        public CheckerAsyncTask(Context ctx, boolean isSilent) {
            super(ctx, isSilent);
        }

        @Override
        public void onPostExecute(Integer result) {
            super.onPostExecute(result);

            if (result == 100) {
                Utils.showNonCancelablePrompt(SplashActivity.this, "Error", "Please connect to internet and restart the App", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                        finish();
                    }
                }, new String[]{"OK"});
                return;
            }
            if (result == FAILED) {
                Utils.showNonCancelablePrompt(SplashActivity.this, "Error", errorMessage, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(SplashActivity.this, SetupActivity.class));
                        finish();
                    }
                }, new String[]{"OK"});
            }
            if (result == SUCCESS) {
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                finish();
            }
        }

        @Override
        protected Integer doInBackground(String... strings) {
            try {

                UserOtpToken token = PreferencesUtil.get(SplashActivity.this, AppConstants.USER_INFO, UserOtpToken.class);

//                if (!Utils.hasNetwork(ctx)) {
//                    Utils.enableData(ctx, true);
//                    ThreadUtils.sleepInSec(7);
//                }
//
//                if (token != null && !Utils.hasNetwork(ctx)) {
//                    return 100;
//                }
//
//                if (token == null) {
//                    return FAILED;
//                }
//
//                NetResponse resp = APIUtils.authDriver(token.getToken());
//
//                if (resp == null || resp.getStatusCode() != APIStatusCodes.SUCCESS.getCode()) {
//                    errorMessage = resp != null ? resp.getStatusDetails() : "Unable to verify the device. Please check with Administrator";
//                    return FAILED;
//                }

                return SUCCESS;

            } catch (Exception e) {
                AppLogger.get().e(getClass(), e);
            }
            return FAILED;
        }
    }

    private void startBgInit() {
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();

    }


    private void initApp() {

        if (Utils.isGpsProviderAvailable(this)) {
            loadNext();
            return;
        } else {
            Utils.showPrompt(this, "No GPS", "This app requires hardware GPS. Please use different phone.", true);
        }

//        Utils.showGpsEnablePrompt(this, new GoogleApiClient.ConnectionCallbacks() {
//            @Override
//            public void onConnected(Bundle bundle) {
//                loadNext();
//            }
//
//            @Override
//            public void onConnectionSuspended(int i) {
//
//            }
//        }, new GoogleApiClient.OnConnectionFailedListener() {
//            @Override
//            public void onConnectionFailed(ConnectionResult connectionResult) {
//
//            }
//        });


    }

    private void loadNext() {
        if (PreferencesUtil.contains(this, AppConstants.PHONE_NUMBER_KEY) && PreferencesUtil.getBool(this, AppConstants.APP_INITIALIZED, false)) {
            new CheckerAsyncTask(this, true).execute();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, SetupActivity.class));
                    finish();
                }
            }, 2500);
        }

    }


}
