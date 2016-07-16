package com.taxiapp.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Simplified location helper class. Good for getting updates in lat, long and
 * human readable address based on current location.
 * 
 * @author Sreedevi.Jagannath
 */
public class LocationUtils {

    private static final String TAG = LocationUtils.class.getSimpleName();
    private static final int MSG_UPDATE_ADDRESS = 0;
    private static final int MSG_NEW_ADDRESS_AVAILABLE = 1;
    private static final int DELAY_BETWEEN_ADDRESS_FETCHES = 1000 * 15;
    private static final int TWO_MINUTES = 1000 * 60 * 2;

    private Set<String> mEnabledProviders = new HashSet<String>();
    private Location mCurrentBestLocation;
    private LocationManager mLocationManager;
    private Geocoder mGeocoder;
    protected Address mCurrentAddress;
    private UpdateAddressAsyncTask mUpdateAddressAsyncTask;
    private Context mContext;
    private ILocationUpdatesListener mLocationUpdatesListener;

    /**
     * Implement this interface for call back with regards to location updates.
     * 
     * @author Sreedevi.Jagannath
     */
    public interface ILocationUpdatesListener {
        public static final int REASON_NO_LOCATION_MANAGER = 0;
        public static final int REASON_NO_LOCATION_PROVIDER = 1;

        public void onNewCoOrdinatesAvailable(LatLng newCoOrdinates);

        /**
         * This will be called when there can be no more updates possible,
         * usually because all providers are disabled or there is no support for
         * location on the device. If temporarily no providers are available,
         * but later become available, a call to onProvidersAvailable() will be
         * made.
         * 
         * @param reason
         *            ILocationUpdatesListener.REASON_NO_LOCATION_MANAGER or
         *            ILocationUpdatesListener.REASON_NO_LOCATION_PROVIDER (the
         *            second reason is possibly temporary)
         */
        public void onNoMoreUpdates(int reason);

        /**
         * Called when a provider is made available after the object of this
         * class has been initialised. For example, if the user turns on GPS
         * after some time.
         * 
         * @param provider
         *            a string representation of the provider. will be one of
         *            LocationManager.NETWORK_PROVIDER or
         *            LocationManager.GPS_PROVIDER
         */
        public void onProvidersAvailable(String provider);

        /**
         * Called when a provider is no more available after the object of this
         * class has been initialised. For example, if the user turns off GPS
         * after some time.
         * 
         * @param provider
         *            a string representation of the provider. will be one of
         *            LocationManager.NETWORK_PROVIDER or
         *            LocationManager.GPS_PROVIDER
         */
        public void onProvidersUnavailble(String provider);
    }

    /**
     * Constructor
     * 
     * @param context
     *            Preferably an activity context. Should never be null.
     * @param listener
     *            The listener implementing the {@link ILocationUpdatesListener}
     *            interface. Should not be null if you want any location related
     *            callbacks.
     */
    public LocationUtils(Context context, ILocationUpdatesListener listener) {
        mContext = context;
        mLocationUpdatesListener = listener;
    }

    /**
     * Initialise the location listeners, etc. You <b>MUST</b> call this method
     * if you want this class to be of any use.
     */
    public void initLocationListener() {
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (mLocationManager == null) {
            logd("unable to get location manager");
            if (mLocationUpdatesListener != null) {
                mLocationUpdatesListener.onNoMoreUpdates(ILocationUpdatesListener.REASON_NO_LOCATION_MANAGER);
            }
            return;
        }
        if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            logd("Network provider location enabled");
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 100, mLocationListener);
            mEnabledProviders.add(LocationManager.NETWORK_PROVIDER);
        }
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            logd("GPS provider location enabled");
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 100, mLocationListener);
            mEnabledProviders.add(LocationManager.GPS_PROVIDER);
        }
        if (mEnabledProviders.size() == 0) {
            if (mLocationUpdatesListener != null) {
                mLocationUpdatesListener.onNoMoreUpdates(ILocationUpdatesListener.REASON_NO_LOCATION_PROVIDER);
            }
        } else {
            mCurrentAddressUpdateHandler.sendEmptyMessage(MSG_UPDATE_ADDRESS);
        }
    }

    private void updateGeoCoderAddress() {
        if (mGeocoder == null) {
            mGeocoder = new Geocoder(mContext, Locale.getDefault());
        }
        if (mCurrentBestLocation != null) {
            mUpdateAddressAsyncTask = new UpdateAddressAsyncTask();
            mUpdateAddressAsyncTask.execute();
        }
    }

    private LocationListener mLocationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (status == LocationProvider.AVAILABLE) {
                if (mLocationManager != null) {
                    mLocationManager.requestLocationUpdates(provider, 5000, 100, mLocationListener);
                    mEnabledProviders.add(provider);
                    if (mLocationUpdatesListener != null) {
                        mLocationUpdatesListener.onProvidersAvailable(provider);
                    }
                } else if (status == LocationProvider.OUT_OF_SERVICE) {
                    mEnabledProviders.remove(provider);
                    if (mLocationUpdatesListener != null) {
                        mLocationUpdatesListener.onProvidersUnavailble(provider);
                    }
                    if (mEnabledProviders.size() == 0 && mLocationManager != null) {
                        logd("No more providers enabled.");
                        mLocationManager.removeUpdates(mLocationListener);
                        if (mLocationUpdatesListener != null) {
                            mLocationUpdatesListener
                            .onNoMoreUpdates(ILocationUpdatesListener.REASON_NO_LOCATION_PROVIDER);
                        }
                    }
                }
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
            if (mLocationManager != null) {
                mLocationManager.requestLocationUpdates(provider, 5000, 100, mLocationListener);
                mEnabledProviders.add(provider);
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            mEnabledProviders.remove(provider);
            if (mEnabledProviders.size() == 0 && mLocationManager != null) {
                logd("No more providers enabled.");
                mLocationManager.removeUpdates(mLocationListener);
                if (mLocationUpdatesListener != null) {
                    mLocationUpdatesListener.onNoMoreUpdates(ILocationUpdatesListener.REASON_NO_LOCATION_PROVIDER);
                }
            }
        }

        @Override
        public void onLocationChanged(Location location) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            boolean isBetter = isBetterLocation(location);
            logd("onLocationChanged__isbetter?" + isBetter + "::" + latLng);
            if (isBetter ){
                mCurrentBestLocation = location;
                if(mLocationUpdatesListener != null) {
                    mLocationUpdatesListener.onNewCoOrdinatesAvailable(latLng);
                }
            }
        };
    };

    /**
     * Determines whether one Location reading is better than the current
     * Location fix. Straight off of the Google developers website.
     * 
     * @param location
     *            The new Location that you want to evaluate
     */
    private boolean isBetterLocation(Location location) {
        if (mCurrentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - mCurrentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use
        // the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be
            // worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - mCurrentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 50;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(), mCurrentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and
        // accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    private Logger mLogger = Logger.getInstance();

    private void logd(String message) {
        mLogger.d(TAG, message);
    }

    private void loge(String message) {
        mLogger.e(TAG, message);
    }

    private Handler mCurrentAddressUpdateHandler = new Handler() {
        @Override
		public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_ADDRESS:
                    logd("MSG_UPDATE_ADDRESS");
                    updateGeoCoderAddress();
                    this.sendEmptyMessageDelayed(MSG_UPDATE_ADDRESS, DELAY_BETWEEN_ADDRESS_FETCHES);
                    break;
                case MSG_NEW_ADDRESS_AVAILABLE:
                    String newLocation = getUserRecognizableAddress();
                    logd("MSG_NEW_ADDRESS_AVAILABLE:" + newLocation);
                    break;
            }
        }
    };

    private class UpdateAddressAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                List<Address> mAddresses = mGeocoder.getFromLocation(mCurrentBestLocation.getLatitude(),
                        mCurrentBestLocation.getLongitude(), 1);
                if (mAddresses != null && mAddresses.size() != 0) {
                    mCurrentAddress = mAddresses.get(0);
                    if (mCurrentAddress != null) {
                        mCurrentAddressUpdateHandler.sendEmptyMessage(MSG_NEW_ADDRESS_AVAILABLE);
                    }
                }
            } catch (IOException e) {
                loge(e.getMessage());
            }
            return null;
        }
    };

    /**
     * To get the address in a user readable form.
     * 
     * @return A string with the AddressLine1, Locality, Country information.
     */
    public String getUserRecognizableAddress() {
        StringBuilder builder = new StringBuilder();
        // String addressLine0 = mCurrentAddress.getAddressLine(0);
        // if (addressLine0 != null) {
        // builder.append(addressLine0 + ",");
        // }
        String addressLine1 = getAddressLine(1);
        if (addressLine1 != null) {
            builder.append(addressLine1 + ",");
        }
        String stateName = getLocality();
        if (stateName != null) {
            builder.append(stateName + ",");
        }
        String countryName = getCountry();
        if (countryName != null) {
            builder.append(countryName);
        }
        return builder.toString();
    }

    /**
     * @return a human readable country name, such as "India" if such is
     *         available, null otherwise.
     */
    public String getCountry() {
        if (mCurrentAddress != null) {
            return mCurrentAddress.getCountryName();
        }
        return null;
    }

    /**
     * @return a human readable locality, such as "Bengaluru" if such is
     *         available, null otherwise.
     */
    public String getLocality() {
        if (mCurrentAddress != null) {
            return mCurrentAddress.getLocality();
        }
        return null;
    }

    /**
     * Gets the sub-address such as addressLine0, addressLine1, etc. ex: Hal 2nd
     * Stage, Doopanahalli, Indiranagar
     * 
     * @param index
     *            a non-negative integer starting at 0
     * @return a string with human readable address if such an address is
     *         available, null otherwise.
     */
    public String getAddressLine(int index) {
        if (mCurrentAddress != null && index >= 0) {
            return mCurrentAddress.getAddressLine(index);
        }
        return null;
    }

    /**
     * @return the last known {@linkplain Address} object from which various
     *         parameters can be obtained; null if none available yet.
     */
    public Address getLastKnownAddress() {
        return mCurrentAddress;
    }

    /**
     * <b>MUST</b> Call this method once you are done using location utils
     * object/ instance is going out of scope.
     */
    public void stopListening() {
        logd("Stopping location updates.");
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(mLocationListener);
        }
        mCurrentAddressUpdateHandler.removeMessages(MSG_NEW_ADDRESS_AVAILABLE);
        mCurrentAddressUpdateHandler.removeMessages(MSG_UPDATE_ADDRESS);
    }
}
