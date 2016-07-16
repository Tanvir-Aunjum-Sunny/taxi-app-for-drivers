package com.taxiapp.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.taxiapp.model.business.Pair;
import com.taxiapp.net.WebSession;
import com.taxiapp.vendor.app.R;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Utils {

    public static boolean hasNetwork(Context ctx) {
        ConnectivityManager mgr = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = mgr.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }


    public static boolean isGpsEnabled(Context ctx) {
        LocationManager mgr = (LocationManager) ctx
                .getSystemService(Context.LOCATION_SERVICE);
        return mgr.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static boolean isGpsProviderAvailable(Context ctx) {
        LocationManager mgr = (LocationManager) ctx
                .getSystemService(Context.LOCATION_SERVICE);
        return mgr.getProvider(LocationManager.GPS_PROVIDER) != null;
    }


    public static void showToast(Context ctx, String message) {
        Toast toast = Toast.makeText(ctx, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if (v != null) {
            v.setGravity(Gravity.CENTER);
            v.setTextSize(22);
        }
        toast.show();
    }


    public static void showPrompt(Context ctx, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customTitleView = inflater.inflate(
                R.layout.dialog_title, null);
        ((TextView) customTitleView.findViewById(R.id.dialog_title))
                .setText(title);
        builder.setCustomTitle(customTitleView).setMessage(message)
                .setPositiveButton("OK", null);
        final Dialog dialog = builder.create();
        dialog.setCancelable(false);
        customTitleView.findViewById(R.id.dialog_close).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
        // new
        // AlertDialog.Builder(ctx).setTitle(title).setMessage(message).setPositiveButton("OK",
        // null).create().show();
    }


    public static void showPromptinvite(Activity ctx, String title,
                                        String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        View customTitleView = ctx.getLayoutInflater().inflate(
                R.layout.dialog_title, null);
        ((TextView) customTitleView.findViewById(R.id.dialog_title))
                .setText(title);
        builder.setCustomTitle(customTitleView).setMessage(message)
                .setPositiveButton("OK", null);
        final Dialog dialog = builder.create();
        customTitleView.findViewById(R.id.dialog_close).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        dialog.dismiss();

						/*
                         * ctx.setResult(Activity.RESULT_OK); ctx.finish();
						 */
                    }
                });
        dialog.show();
    }

    public static void showPrompt(final Activity ctx, String title,
                                  String message, final boolean finish) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setCancelable(false);
        View customTitleView = ctx.getLayoutInflater().inflate(
                R.layout.dialog_title, null);
        ((TextView) customTitleView.findViewById(R.id.dialog_title))
                .setText(title);
        builder.setCustomTitle(customTitleView).setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (finish) {
                            ctx.setResult(Activity.RESULT_OK);
                            ctx.finish();
                        }
                    }
                });
        final Dialog dialog = builder.create();
        customTitleView.findViewById(R.id.dialog_close).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        dialog.dismiss();
                        if (finish) {
                            ctx.setResult(Activity.RESULT_OK);
                            ctx.finish();
                        }
                    }
                });
        dialog.show();
    }

    public static void showNonCancelablePrompt(final Activity ctx, String title, String message,
                                               OnClickListener listener, String[] buttons) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        View customTitleView = ctx.getLayoutInflater().inflate(R.layout.dialog_title, null);
        TextView titleText = ((TextView) customTitleView.findViewById(R.id.dialog_title));
        titleText.setText(title);
        builder.setCustomTitle(customTitleView).setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(buttons[0], listener);
        if (buttons.length == 2) {
            builder.setNegativeButton(buttons[1], listener);
        }
        if (buttons.length == 3) {
            builder.setNeutralButton(buttons[2], listener);
        }

        final Dialog dialog = builder.create();
        customTitleView.findViewById(R.id.dialog_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //dialog.dismiss();
            }
        });
        dialog.show();
    }


    public static void showYesNoPrompt(final Activity ctx, String title,
                                       String message, final boolean finishIfTrue) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        View customTitleView = ctx.getLayoutInflater().inflate(
                R.layout.dialog_title, null);
        ((TextView) customTitleView.findViewById(R.id.dialog_title))
                .setText(title);
        builder.setCustomTitle(customTitleView).setMessage(message)
                .setPositiveButton("OK", null);
        final Dialog dialog = builder.create();
        customTitleView.findViewById(R.id.dialog_close).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        dialog.dismiss();
                        if (finishIfTrue) {
                            ctx.setResult(Activity.RESULT_OK);
                            ctx.finish();
                        }
                    }
                });
        dialog.show();
    }

    public static void showPrompt(final Activity ctx, String title,
                                  String message, OnClickListener listener, String positiveText, String negativeText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        View customTitleView = ctx.getLayoutInflater().inflate(
                R.layout.dialog_title, null);
        ((TextView) customTitleView.findViewById(R.id.dialog_title))
                .setText(title);
        builder.setCustomTitle(customTitleView).setMessage(message)
                .setPositiveButton(positiveText, listener)
                .setNegativeButton(negativeText, listener);
        final Dialog dialog = builder.create();
        customTitleView.findViewById(R.id.dialog_close).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }


    public static void showPrompt(final Activity ctx, String title,
                                  View view, final boolean finish) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setCancelable(false);
        View customTitleView = ctx.getLayoutInflater().inflate(
                R.layout.dialog_title, null);
        ((TextView) customTitleView.findViewById(R.id.dialog_title))
                .setText(title);
        builder.setCustomTitle(customTitleView).setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (finish) {
                            ctx.setResult(Activity.RESULT_OK);
                            ctx.finish();
                        }
                    }
                });
        final Dialog dialog = builder.create();
        customTitleView.findViewById(R.id.dialog_close).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        dialog.dismiss();
                        if (finish) {
                            ctx.setResult(Activity.RESULT_OK);
                            ctx.finish();
                        }
                    }
                });
        dialog.show();
    }


    public static LatLng getLocationFromString(Context ctx, String address)
            throws Exception {
        if (Geocoder.isPresent()) {
            Geocoder geocoder = new Geocoder(ctx, Locale.ENGLISH);
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocationName(address, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (addresses != null && addresses.size() > 0) {
                Address addr = addresses.get(0);
                LatLng l = new LatLng(addr.getLatitude(), addr.getLongitude());
                return l;
            }
        }

        WebSession session = new WebSession();
        String url =
                "http://maps.google.com/maps/api/geocode/json?address="
                        + URLEncoder.encode(address, "UTF-8")
                        + "&ka&sensor=false";
        try {
            String response = session.get(url);
            JSONObject jsonObject = new JSONObject();
            jsonObject = new JSONObject(response);

            double lng = ((JSONArray) jsonObject.get("results"))
                    .getJSONObject(0).getJSONObject("geometry")
                    .getJSONObject("location").getDouble("lng");

            double lat = ((JSONArray) jsonObject.get("results"))
                    .getJSONObject(0).getJSONObject("geometry")
                    .getJSONObject("location").getDouble("lat");

            return new LatLng(lat, lng);

        } catch (Exception e) {
            Log.e(Utils.class.getName(),
                    "Error Geocoding - " + e.getLocalizedMessage());
        }
        return null;

    }

    public static Address getStringFromLocation(Context ctx, double lat,
                                                double lng) /*throws Exception*/ {
        if (Geocoder.isPresent()) {
            Log.d("Geocoder", "Geocoder Available");
            Geocoder geocoder = new Geocoder(ctx, Locale.ENGLISH);
            List<Address> addresses = new ArrayList<Address>();
            try {

                addresses = geocoder.getFromLocation(lat, lng, 1);
            } catch (Exception e) {
                Log.d("Geocoder", "exception found");
                e.printStackTrace();
            }
            Log.d("Geocoder", addresses.toString());
            if (addresses != null && addresses.size() > 0) {
                Address addr = addresses.get(0);
                // Remove Locality & Pincode.
                String city = addr.getLocality();
                String pin = addr.getPostalCode();
                String state = addr.getAdminArea();
                String country = addr.getCountryName();
                for (int i = 0; i < addr.getMaxAddressLineIndex(); i++) {
                    String field = addr.getAddressLine(i);
                    boolean isModified = false;
                    if (StringUtils.containsIgnoreCase(field, city)) {
                        field = StringUtils.replace(field, city, "");
                        isModified = true;
                    }

                    if (StringUtils.containsIgnoreCase(field, pin)) {
                        field = StringUtils.replace(field, pin, "");
                        isModified = true;
                    }

                    if (StringUtils.containsIgnoreCase(field, state)) {
                        field = StringUtils.replace(field, state, "");
                        field = StringUtils.replace(field, ",", "");
                        isModified = true;
                    }

                    if (StringUtils.containsIgnoreCase(field, country)) {
                        field = StringUtils.replace(field, country, "");
                        isModified = true;
                    }

                    if (isModified) {
                        addr.setAddressLine(i, field);
                    }

                }
                return addr;
            } else {
                return stringToLocWithoutGeocoder(lat, lng);
            }
        }

        try {
            String address = String
                    .format(Locale.ENGLISH,
                            "http://maps.googleapis.com/maps/api/geocode/json?latlng=%f,%f&sensor=true&language=en",
                            lat, lng);
            WebSession session = new WebSession();
            String response = session.get(address);
            JSONObject jsonObject = new JSONObject();
            jsonObject = new JSONObject(response);

            ArrayList<Address> retList = new ArrayList<Address>();

            if ("OK".equalsIgnoreCase(jsonObject.getString("status"))) {
                JSONArray results = jsonObject.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject result = results.getJSONObject(i);
                    String indiStr = result.getString("formatted_address");
                    Address addr = new Address(Locale.getDefault());
                    processAddressComponents(addr, result);
                    retList.add(addr);
                }
            }

            return retList.get(0);
        } catch (Exception e) {
            Log.e(Utils.class.getName(),
                    "Error Getting Address - " + e.getLocalizedMessage());
            e.printStackTrace();
        }

        return null;
    }

    private static void processAddressComponents(Address addr, JSONObject result)
            throws JSONException {
        Map<String, String> lines = new HashMap<String, String>();
        if (result.has("address_components")) {
            JSONArray array = result.getJSONArray("address_components");
            for (int j = 0; j < array.length(); j++) {
                JSONObject obj = array.getJSONObject(j);
                if (obj.has("types")) {
                    JSONArray types = obj.getJSONArray("types");
                    for (int k = 0; k < types.length(); k++) {
                        lines.put(types.getString(k),
                                obj.getString("long_name"));
                    }
                }
            }

            int addrLines = 0;
            if (lines.containsKey("street_number")) {
                addr.setAddressLine(addrLines++, lines.get("street_number"));
            }

            if (lines.containsKey("route")) {
                addr.setAddressLine(addrLines++, lines.get("route"));
            }

            if (lines.containsKey("neighborhood")) {
                addr.setAddressLine(addrLines++, lines.get("neighborhood"));
            }

            if (lines.containsKey("sublocality_level_1")) {
                addr.setAddressLine(addrLines++,
                        lines.get("sublocality_level_1"));
            } else if (lines.containsKey("sublocality")) {
                addr.setAddressLine(addrLines++, lines.get("sublocality"));
            }

            if (lines.containsKey("locality")) {
                addr.setLocality(lines.get("locality"));
            }

            if (lines.containsKey("administrative_area_level_1")) {
                addr.setAdminArea(lines.get("administrative_area_level_1"));
            }

            if (lines.containsKey("country")) {
                addr.setCountryName(lines.get("country"));
            }

            if (lines.containsKey("postal_code")) {
                addr.setPostalCode(lines.get("postal_code"));
            }
        }

    }

    public static LatLng getLocationFromAddress(Context ctx, String strAddress) {

        Geocoder coder = new Geocoder(ctx);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {

                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
            return p1;
        } catch (Exception e) {

        }
        return null;
    }

    public static boolean isValidPhoneNumber(String username) {
        return PhoneNumberUtils.isGlobalPhoneNumber(username);
        // return false;
    }

    public static void callCustomerCare(Activity ctx) {
        String uri = AppConstants.CUSTOMER_CARE_NUMBER;
        callNumber(ctx, uri);

    }

    public static void callNumber(Activity ctx, String number) {
        String uri = "tel:"+number;
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));
        ctx.startActivity(intent);

    }

    @SuppressLint("NewApi")
    public static String getUserName(Context ctx) {
        Cursor c = ctx.getContentResolver().query(
                ContactsContract.Profile.CONTENT_URI, null, null, null, null);
        String name = "";
        int count = c.getCount();

        String[] columnNames = c.getColumnNames();
        List<Pair> profileList = new LinkedList<Pair>();
        boolean b = c.moveToFirst();
        int position = c.getPosition();
        if (count == 1 && position == 0) {
            for (int i = 0; i < count; i++) {
                for (int j = 0; j < columnNames.length; j++) {
                    String columnName = columnNames[j];
                    Pair pair = new Pair(columnName, c.getString(c
                            .getColumnIndex(columnName)));
                    if (columnName.equals("display_name")) {
                        Log.i("p1",
                                "Name : "
                                        + c.getString(c
                                        .getColumnIndex(columnName)));
                        name = c.getString(c.getColumnIndex(columnName));
                    }
                    profileList.add(pair);
                }
                boolean b2 = c.moveToNext();
            }
        }
        c.close();

        return name;
    }

    public static String getAccountMail(Context ctx) {
        AccountManager manager = (AccountManager) ctx
                .getSystemService(Context.ACCOUNT_SERVICE);
        Account[] list = manager.getAccounts();
        String gmail = null;

        for (Account account : list) {
            // String
            // name=AccountManager.get(ctx).getAccountsByType("com.google").toString();
            if (account.type.equalsIgnoreCase("com.google")) {
                gmail = account.name;

                break;
            }
        }
        return gmail;
    }

    public static void sanitizeAddresses(
            List<com.taxiapp.model.business.Address> list) {
        Iterator<com.taxiapp.model.business.Address> it = list.iterator();
        while (it.hasNext()) {
            com.taxiapp.model.business.Address address = it.next();
            if (StringUtils.containsIgnoreCase(address.getUserEnteredAddress(),
                    "airport")
                    || StringUtils.containsIgnoreCase(
                    address.getUserEnteredAddress(), "railway")
                    || StringUtils.containsIgnoreCase(
                    address.getUserEnteredAddress(), "station")) {
                it.remove();
            }
        }

    }

    private static Address stringToLocWithoutGeocoder(double lat, double lng) {
        try {
            String address = String
                    .format(Locale.ENGLISH,
                            "http://maps.googleapis.com/maps/api/geocode/json?latlng=%f,%f&sensor=true&language=en",
                            lat, lng);
            Log.d("Geocoder", address);

            List<Address> retList = null;

            WebSession session = new WebSession();
            String response = session.get(address);
            JSONObject jsonObject = new JSONObject();
            jsonObject = new JSONObject(response);

            if ("OK".equalsIgnoreCase(jsonObject.getString("status"))) {
                JSONArray results = jsonObject.getJSONArray("results");
                if (results != null && results.length() > 0) {
                    retList = new ArrayList<Address>();
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject result = results.getJSONObject(i);
                        String indiStr = result.getString("formatted_address");
                        Address addr = new Address(Locale.getDefault());
                        processAddressComponents(addr, result);
                        retList.add(addr);
                    }
                    return retList.get(0);
                }
            }
            Log.d("Geocoder", "Address Line" + retList.get(0).toString());

        } catch (Exception e) {
            Log.e(Utils.class.getName(),
                    "Error Getting Address - " + e.getLocalizedMessage());
            e.printStackTrace();
        }

        return null;
    }

    // TO HIDE SOFT KEYBORD
    public static void hideKeyBoard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = ((InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE));
            inputMethodManager.hideSoftInputFromWindow(activity
                    .getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ***** BEGIN CODE: SREEDEVI ******************/

    /**
     * Given a properly formatted date string (time stri
     *
     * @param date date represented as a string of the format yyyy-MM-dd
     */
    public static Date getDateFromString(String date, String time) throws IllegalArgumentException {
        // this should be fixed, should not use deprecated, but keeping for now
        // since the backend already uses this format; I don't want to break it
        if (date == null || date.isEmpty()) {
            throw new IllegalArgumentException("date cannot be null.");
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat formatter1 = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        Date d11;
        Date d22;

        try {
            d11 = formatter.parse(date);
            Calendar dCal = Calendar.getInstance();
            dCal.setTime(d11);
            if (time != null) {
                d22 = formatter1.parse(time);
                Calendar tCal = Calendar.getInstance();
                tCal.setTime(d22);
                dCal.set(Calendar.HOUR_OF_DAY, tCal.get(Calendar.HOUR_OF_DAY));
                dCal.set(Calendar.MINUTE, tCal.get(Calendar.MINUTE));
                dCal.set(Calendar.SECOND, tCal.get(Calendar.SECOND));
                dCal.set(Calendar.MILLISECOND, tCal.get(Calendar.MILLISECOND));
            }
            return dCal.getTime();

        } catch (ParseException e2) {
            e2.printStackTrace();
        }
        return null;
    }

    public static void turnGPSOn(Context ctx) {

        if (!canToggleGPS(ctx)) {
            return;
        }

        String provider = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (!provider.contains("gps")) { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            ctx.sendBroadcast(poke);
        }
    }


    private static boolean canToggleGPS(Context ctx) {
        PackageManager pacman = ctx.getPackageManager();
        PackageInfo pacInfo = null;

        try {
            pacInfo = pacman.getPackageInfo("com.android.settings", PackageManager.GET_RECEIVERS);
        } catch (PackageManager.NameNotFoundException e) {
            return false; //package not found
        }

        if (pacInfo != null) {
            for (ActivityInfo actInfo : pacInfo.receivers) {
                //test if recevier is exported. if so, we can toggle GPS.
                if (actInfo.name.equals("com.android.settings.widget.SettingsAppWidgetProvider") && actInfo.exported) {
                    return true;
                }
            }
        }

        return false; //default
    }

    public static void enableData(Context context, boolean enabled) {
        try {
            final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final Class conmanClass = Class.forName(conman.getClass().getName());
            final Field connectivityManagerField = conmanClass.getDeclaredField("mService");
            connectivityManagerField.setAccessible(true);
            final Object connectivityManager = connectivityManagerField.get(conman);
            final Class connectivityManagerClass = Class.forName(connectivityManager.getClass().getName());
            final Method setMobileDataEnabledMethod = connectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);

            setMobileDataEnabledMethod.invoke(connectivityManager, enabled);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // ***** END_TRIP CODE: SREEDEVI ******************/

    public static void showGpsEnablePrompt(final Activity context, GoogleApiClient.ConnectionCallbacks listener, GoogleApiClient.OnConnectionFailedListener failed) {

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(listener)
                .addOnConnectionFailedListener(failed).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        //**************************
        builder.setAlwaysShow(true); //this is the key ingredient
        //**************************

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    context, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

}
