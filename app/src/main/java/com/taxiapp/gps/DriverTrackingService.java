package com.taxiapp.gps;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.taxiapp.db.DriverLocationDAO;
import com.taxiapp.db.TransactionDBManager;
import com.taxiapp.gps.util.GeoUtil;
import com.taxiapp.model.BookingSession;
import com.taxiapp.model.DriverLocation;
import com.taxiapp.model.UserOtpToken;
import com.taxiapp.model.business.DriverState;
import com.taxiapp.utils.AppConstants;
import com.taxiapp.utils.AppLogger;
import com.taxiapp.utils.GlobalData;

import de.greenrobot.event.EventBus;

/**
 * Created by sarath on 19/11/15.
 */
public class DriverTrackingService extends Service implements LocationListener {

    private static final String LOG_TAG = "DriverTrackService";

    //Bundle Keys
    public static final String BOOKING_OBJ_KEY = "bookingObject";

    public static final String DEFAULT_PROVIDER = LocationManager.GPS_PROVIDER;

    public static final int DEFAULT_CHECKPOINTS_INMEM = 4;
    public static final int SEND_SERVER_IN_METERS = 0;
    private static final float MIN_DISTANCE = 10;


    private Intent intent = null;
    private BookingSession bookingSession;
    private DriverLocation currentDriverLocation;

    public static Gson gson = new Gson();
    private DriverLocationDAO driverLocationDAO;

    private LocationManager locationManager = null;

    private UserOtpToken token;


    @Override
    public void onCreate() {
        Log.d(LOG_TAG, "Service is Created");
        super.onCreate();
        try {
            if (GlobalData.get() == null)
                GlobalData.init(this);
            driverLocationDAO = TransactionDBManager.getInstance().getDriverLocationDAO();
            token = AppConstants.getToken(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(DEFAULT_PROVIDER, 1000, 10, this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.intent = intent;
        runOnThread();
        AppLogger.get().d(getClass(), "Trip Tracking Service is Started");
        return super.onStartCommand(intent, flags, startId);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    //Location listener.......
    @Override
    public void onLocationChanged(Location location) {
        if (!location.hasAccuracy()) {
            return;
        }

        currentDriverLocation = DriverLocation.newDriverLocation(location);

        bookingSession = GlobalData.get().getBookingSession(this);
        //Precautionary action.
        if (bookingSession == null) {
            initBookingSession();
            if (bookingSession == null) {
                AppLogger.get().i(getClass(), "Service ON, but booking session is NULL. Stopping service");
                stopSelf();
            }
            return;
        }

        //We do broadcast Driver location even in IDLE state.
        if (bookingSession.getDriverState() == DriverState.START_TRIP) {
            EventBus.getDefault().post(currentDriverLocation);
            return;
        }

        AppLogger.get().d(getClass(), "Driver State - " + bookingSession.getDriverState());

        onTripGoing(currentDriverLocation);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pauseLocationListener();
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(LOG_TAG, "Provider Enables : " + provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(LOG_TAG, "Provider Disabled : " + provider);
    }
    //Location listener.......Ends

    public DriverLocation getLastCheckPoint() {
        try {
            if (bookingSession != null) {
                return bookingSession.getLastCheckPoint();
            } else {
                return driverLocationDAO.getLast(DriverLocation.IDLE_TRIP_ID);
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    public void onTripGoing(DriverLocation currentLocation) {
        try {
            currentLocation.setBookingId(bookingSession.getBookingId());
            currentLocation.setDay(bookingSession.getDay());
            currentLocation.setCheckpoint(DriverState.ONGOING.name());

            DriverLocation prevLocation = getLastCheckPoint();

            if (prevLocation == null) {
                bookingSession.setLastCheckPoint(currentLocation);
                uploadAndSaveLocation(currentLocation);
                return;
            }

            double distance = GeoUtil.distance(prevLocation, currentLocation);
            if (DriverState.get(prevLocation.getCheckpoint()) == DriverState.END_DAY_TRIP) {
                distance = 0;
            }

            currentLocation.setSpanTravelled(distance);
            currentLocation.setTravelled(distance + prevLocation.getTravelled());

            if (DriverState.get(currentLocation.getCheckpoint()) == DriverState.CUSTOMER_BOARDED) {
                currentLocation.setTravelled(0.0);
            }

            bookingSession.setLastCheckPoint(currentLocation);

            uploadAndSaveLocation(currentLocation);

            EventBus.getDefault().post(currentLocation);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadAndSaveLocation(DriverLocation currentLocation) {
        try {
            //Send it on the spot.
//            NetResponse response = APIUtils.sendLocationData(token, currentLocation);
//            if (response != null) {
//                currentLocation.setSyncStatus(SyncStatus.SUCCESS);
//            }
            Dao.CreateOrUpdateStatus status = driverLocationDAO.createOrUpdate(currentLocation);
        } catch (Exception e) {
            AppLogger.get().e(getClass(), e);
        }

    }


    public void runOnThread() {

        if (intent == null) {
            if (initBookingSession()) {
                initializeLocationListener();
            }
            return;
        }
        switch (DriverState.get(intent.getAction())) {
            case ONGOING:
            case CONTINUE:
            case CUSTOMER_BOARDED:
            case IDLE:
            case REACHED_PICKUP:
            case START_DAY_TRIP:
            case START_TRIP:
            case RESUME_TRIP:
                if (initBookingSession()) {
                    initializeLocationListener();
                }
                break;
            case PAUSE_TRIP:
            case TRIP_OVER:
            case END_DAY_TRIP:
            case END_TRIP:
                pauseLocationListener();
                break;
        }
    }

    private void pauseLocationListener() {
        locationManager.removeUpdates(DriverTrackingService.this);
    }

    private void initializeLocationListener() {
        locationManager.removeUpdates(DriverTrackingService.this);
        locationManager.requestLocationUpdates(DEFAULT_PROVIDER, 1000, MIN_DISTANCE, DriverTrackingService.this);
    }


    public boolean initBookingSession() {
        //Preferences #3
        if (GlobalData.get() == null) {
            GlobalData.init(this);
        }
        bookingSession = GlobalData.get().getBookingSession(this);
        if (bookingSession == null || bookingSession.getBooking() == null) {
            return false;
        }
        restoreLastCheckpoint();

        return true;
    }


    public void restoreLastCheckpoint() {
        try {
            DriverLocation lastCheckPoint = driverLocationDAO.getLast(bookingSession.getBookingId());
            bookingSession.setLastCheckPoint(lastCheckPoint);
            AppLogger.get().d(this.getClass(), "Last Check Point: " + lastCheckPoint);
            if (lastCheckPoint != null) {
                EventBus.getDefault().post(lastCheckPoint);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}