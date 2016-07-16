package com.taxiapp.app.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.taxiapp.utils.ActivityHelper;
import com.taxiapp.utils.AnalyticsUtility;
import com.taxiapp.utils.Utils;
import com.taxiapp.vendor.app.R;

/**
 * Created by amu on 31/05/15.
 */
public class BaseActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public Toolbar toolbar;

    public DrawerLayout mDrawerLayout;
    public ActionBarDrawerToggle mDrawerToggle;



    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // SENDING ANALYTICS
        AnalyticsUtility.trackScreenView(this, this.getClass().getSimpleName());

        if (!Utils.isGpsEnabled(BaseActivity.this)) {
            Utils.showGpsEnablePrompt(BaseActivity.this, BaseActivity.this, BaseActivity.this);
            return;
        }
//        if (!Utils.isGpsEnabled(this)) {
//            Utils.showNonCancelablePrompt(this, "Error", "GPS Disabled. Click OK to open GPS Settings", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                }
//            }, new String[]{"OK"});
//
//            return;
//        }

    }

    public void setContentView(int r) {
        super.setContentView(r);
        ActivityHelper.initialize(this);
        setupHeadLayout();
    }

    private void setupHeadLayout() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar == null) {
            return;
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("TaxiApp");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
//        int id = item.getItemId();
//
//        switch (id) {
//            case R.id.menu_events:
//                loadEvents();
//                break;
//            case R.id.menu_ads:
//                loadAds();
//                break;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mDrawerToggle != null)
            mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mDrawerToggle != null)
            mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerToggle != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
