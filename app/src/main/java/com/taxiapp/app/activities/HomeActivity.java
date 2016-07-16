package com.taxiapp.app.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import com.taxiapp.app.fragments.DriverHomeFragment;
import com.taxiapp.app.fragments.VendorHomeFragment;
import com.taxiapp.model.DeviceHardwareInfo;
import com.taxiapp.model.UserOtpToken;
import com.taxiapp.model.business.OperatorType;
import com.taxiapp.utils.AppConstants;
import com.taxiapp.utils.DeviceInfoDesk;
import com.taxiapp.utils.PreferencesUtil;
import com.taxiapp.utils.Utils;
import com.taxiapp.vendor.app.R;

/**
 * Created by Amit S on 06/11/15.
 */
public class HomeActivity extends BaseActivity {

    private UserOtpToken token;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_home);
        try {
            token = PreferencesUtil.get(this, AppConstants.USER_INFO, UserOtpToken.class);

            switch (OperatorType.get(token.getOperatorType())) {
                case D:
                    getFragmentManager().beginTransaction().add(R.id.frame, new DriverHomeFragment()).commit();
                    setTitle("Home - Driver");
                    break;
                case V:
                    setTitle("Home - Vendor");
                    getFragmentManager().beginTransaction().add(R.id.frame, new VendorHomeFragment()).commit();
                    break;
            }

            startBgInit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void onResume() {
        super.onResume();
        if (!Utils.hasNetwork(this)) {
            Utils.showNonCancelablePrompt(this, "Error", "Please connect to internet and retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));

                }
            }, new String[]{"OK"});
        }

    }


    private void startBgInit() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DeviceHardwareInfo hwInfo = DeviceInfoDesk.getDeviceHardwareInfo(HomeActivity.this);

                    PreferencesUtil.putLong(HomeActivity.this, AppConstants.DEVICE_DETAILS_UPD_TIME_KEY, System.currentTimeMillis());


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


}
