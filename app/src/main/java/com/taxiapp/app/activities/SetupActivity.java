package com.taxiapp.app.activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import com.taxiapp.app.fragments.SetupForm1Fragment;
import com.taxiapp.app.fragments.SetupForm2Fragment;
import com.taxiapp.db.TransactionDBManager;
import com.taxiapp.utils.AppConstants;
import com.taxiapp.utils.PreferencesUtil;
import com.taxiapp.vendor.app.R;

/**
 * Created by Amit S on 18/05/15.
 */
public class SetupActivity extends BaseActivity {

    private static final int STEP_0 = 0;
    private static final int STEP_1 = 1;
    private static final int STEP_2 = 2;
    private static final String KEY = "stepKey";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        int step = (int) PreferencesUtil.getLong(this, KEY, -1);

        switch (step) {
            case STEP_2:
                if (!PreferencesUtil.contains(this, AppConstants.USER_INFO)) {
                    goToStep1();
                } else {
                    goToStep2();
                }
                break;
            case STEP_0:
            case STEP_1:
            default:
                goToStep1();
        }


    }




    public void goToStep1() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(R.id.frame, new SetupForm1Fragment()).commit();
        PreferencesUtil.putLong(this, KEY, STEP_1);
    }

    public void goToStep2() {

        //Remove all flags from Step 1
        PreferencesUtil.remove(this, SetupForm1Fragment.NUM_RETRIES);
        PreferencesUtil.remove(this, SetupForm1Fragment.APP_LOCK_TIME);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setTransition(R.anim.slide_in);
        ft.replace(R.id.frame, new SetupForm2Fragment()).commit();
        PreferencesUtil.putLong(this, KEY, STEP_2);
    }

    public void setupComplete() {

        //Remove all flags from Step 2
        PreferencesUtil.remove(this, SetupForm2Fragment.NUM_RETRIES);
        PreferencesUtil.remove(this, SetupForm2Fragment.APP_LOCK_TIME);
        startActivity(new Intent(this, HomeActivity.class));
        finish();
        TransactionDBManager.getInstance().clearTables();
    }
}
