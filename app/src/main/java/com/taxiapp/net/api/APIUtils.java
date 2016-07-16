package com.taxiapp.net.api;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;

import com.google.gson.reflect.TypeToken;
import com.taxiapp.model.DriverLocation;
import com.taxiapp.model.DeviceHardwareInfo;
import com.taxiapp.model.HomeItem;
import com.taxiapp.model.UserOtpToken;
import com.taxiapp.model.business.Booking;
import com.taxiapp.model.business.DriverState;
import com.taxiapp.net.NetResponse;
import com.taxiapp.net.WebSession;
import com.taxiapp.utils.AnalyticsUtility;
import com.taxiapp.utils.AppConstants;
import com.taxiapp.utils.AppLogger;
import com.taxiapp.utils.GlobalData;
import com.taxiapp.utils.PreferencesUtil;
import com.taxiapp.utils.Utils;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Amit S on 16/11/15.
 */
public class APIUtils {


    public static NetResponse receiveOtp(String phoneNumber) {
        try {
            DeviceHardwareInfo info = GlobalData.get().getDeviceHardwareInfo();
            WebSession session = new WebSession();
            Map<String, String> params = new HashMap<>();
            params.put("mobile_no", phoneNumber);
            params.put("imei_no", info.getImeiNumber());

            String httpResponse = session.get(APIUrl.OTP_REQUEST_URL.getFullPath(), params, null);
            NetResponse response = WebSession.gson.fromJson(httpResponse, NetResponse.class);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static NetResponse authDriver(String token) {
        try {
            DeviceHardwareInfo info = GlobalData.get().getDeviceHardwareInfo();
            WebSession session = new WebSession();
            Map<String, String> params = new HashMap<>();
            params.put("token", token);
            params.put("imei_no", info.getImeiNumber());

            String httpResponse = session.get(APIUrl.DRIVER_AUTHENTICATION.getFullPath(), params, null);
            NetResponse response = WebSession.gson.fromJson(httpResponse, NetResponse.class);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //?action_no=7&user_type=V&token=1ac5f6c69e7884af93f519fa7282f7bc&count_context_id=3&cellphone=9562337008&imei_no=76542332
    public static List<HomeItem> homeScreenData(UserOtpToken token) {
        try {
            DeviceHardwareInfo info = GlobalData.get().getDeviceHardwareInfo();
            WebSession session = new WebSession();
            Map<String, String> params = new HashMap<>();
            params.put("token", token.getToken());
            params.put("action_no", "7");
            params.put("user_type", token.getOperatorType());
            params.put("imei_no", info.getImeiNumber());
            params.put("cellphone", token.getPhone());
            String httpResponse = session.get(APIUrl.DRIVER_API.getFullPath(), params, null);
            NetResponse<List<HomeItem>> response = WebSession.gson.fromJson(httpResponse, new TypeToken<NetResponse<List<HomeItem>>>() {
            }.getType());

            if (response == null || response.getStatusCode() != APIStatusCodes.SUCCESS.getCode() || response.getRows() == null || response.getRows().isEmpty()) {
                return null;
            }
            return response.getRows();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static NetResponse sendCriticalData(Context activity, final DriverLocation loc) {

        try {
            UserOtpToken token = AppConstants.getToken(activity);

            Map<String, String> params = new HashMap<>();
            params.put("bookingId", loc.getBookingId());
            params.put("checkpoint", loc.getCheckpoint());
            params.put("location", "" + loc.getLatitude() + "," + loc.getLongitude());
            AnalyticsUtility.sendGenericEvent(activity, "CriticalDataPoint", "TripTracking", loc.getCheckpoint(), "Trip status update", params);


            NetResponse response = APIUtils.sendLocationData(token, loc);
            if (response == null) {
                response = APIUtils.sendLocationData(token, loc);
            }

            if (response != null) {
                loc.setSyncStatus(SyncStatus.SUCCESS);
                return response;
            }

        } catch (Exception e) {
            AppLogger.get().e(APIUtils.class, e);
        }

        return null;
    }

    //Usage: APIUtils.sendLocationData(AppConstants.getToken(this), location);
    //?action_no=1&booking_id=2006127&mobile_no=9739258200&lat=78.3214&long=11.7669&car_id=2&client_datetime=2015-11-27%2016:58:00&token=d4f871f702ebc86ddc35a68458f43c28&imei_no=9911a22b33c44d55eD
    public static NetResponse sendLocationData(UserOtpToken token, DriverLocation location) {
        try {
            DeviceHardwareInfo info = GlobalData.get().getDeviceHardwareInfo();

            WebSession session = new WebSession();
            Map<String, String> params = new HashMap<>();
            params.put("token", token.getToken());
            params.put("action_no", getActionNo(location.getCheckpoint()));
            params.put("user_type", token.getOperatorType());
            params.put("lat", "" + location.getLatitude());
            params.put("longitude", "" + location.getLongitude());
            params.put("imei_no", info.getImeiNumber());
            params.put("mobile_no", token.getPhone());
            params.put("booking_id", location.getBookingId());
            params.put("client_datetime", new DateTime(location.getCapturedTime()).toString("yyyy-MM-dd HH:mm:ss"));
            params.put("total_distance_travelled", "" + ((int) (location.getTravelled() / 1000.00)));
            params.put("day_no", location.getDay() + "");

            String httpResponse = session.get(APIUrl.DRIVER_API.getFullPath(), params, null);
            NetResponse response = WebSession.gson.fromJson(httpResponse, NetResponse.class);

            if (response.getStatusCode() == APIStatusCodes.SUCCESS.getCode()) {
                return response;
            }

        } catch (Exception e) {
            AppLogger.get().e(APIUtils.class, e);
        }
        return null;
    }


    //http://betataxiapp.com/admin/driver_api.php?
    // action_no=8
    // &user_type=V
    // &token=1ac5f6c69e7884af93f519fa7282f7bc
    // &imei_no=9911a22b33c44d55e&cellnumber=9562337008
    // &start_date=2015-11-16
    // &end_date=2015-11-18&
    // bookingids=2006037,2006035
    public static List<Booking> getBookings(UserOtpToken token, String ids) {
        try {
            DeviceHardwareInfo info = GlobalData.get().getDeviceHardwareInfo();

            WebSession session = new WebSession();
            Map<String, String> params = new HashMap<>();
            params.put("token", token.getToken());
            params.put("action_no", "8");
            params.put("user_type", token.getOperatorType());
            params.put("imei_no", info.getImeiNumber());
            params.put("cellnumber", token.getPhone());
            params.put("bookingids", ids);

            String httpResponse = session.get(APIUrl.DRIVER_API.getFullPath(), params, null);
            NetResponse<List<Booking>> response = WebSession.gson.fromJson(httpResponse, new TypeToken<NetResponse<List<Booking>>>() {
            }.getType());

            if (response == null || response.getStatusCode() != APIStatusCodes.SUCCESS.getCode() || response.getRows() == null || response.getRows().isEmpty()) {
                return new ArrayList<>();
            }
            return response.getRows();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    private static String getActionNo(String checkPoint) {
        DriverState state = DriverState.CONTINUE;
        for (DriverState driverState : DriverState.values()) {
            if (StringUtils.equalsIgnoreCase(driverState.name(), checkPoint)) {
                state = driverState;
                break;
            }
        }
        switch (state) {

            case START_TRIP:
                return "1";
            case REACHED_PICKUP:
                return "2";
            case CUSTOMER_BOARDED:
                return "3";
            case END_DAY_TRIP:
                return "4";
            //
            case TRIP_OVER:
                return "6";
            case START_DAY_TRIP:
                return "5";
            case END_TRIP:
            case ONGOING:
                return "9";
            default:
                return "5";
        }
    }

    public static boolean sendDeviceDetails(Context context, DeviceHardwareInfo hwInfo, UserOtpToken token, String distinctId) {
        try {

            WebSession session = new WebSession();
            Map<String, String> params = new HashMap<>();
            params.put("app_name", "Consumer");
            params.put("device_id", hwInfo.getGcmId());
            params.put("imei_number", hwInfo.getImeiNumber());
            params.put("model", hwInfo.getModel());
            params.put("screen_height", String.valueOf(hwInfo.getScreenHeight()));
            params.put("screen_width", String.valueOf(hwInfo.getScreenWidth()));
            params.put("screen_pdi", String.valueOf(hwInfo.getDpi()));
            params.put("bluetooth_enabled", hwInfo.isBtEnabled() ? "1" : "0");
            params.put("bluetooth_version", hwInfo.getBtVersion());
            params.put("brand", Build.BRAND);
            params.put("has_nfc", String.valueOf(hwInfo.isNfsAvailable()));
            params.put("has_telephone", hwInfo.isTelephoneAvailable() ? "1" : "0");
            params.put("manufacturer", hwInfo.getManufacturer());
            params.put("cpu_speed", String.valueOf(hwInfo.getCpuSpeed()));
            params.put("total_memory", String.valueOf(hwInfo.getTotalMemory()));
            params.put("total_storage", String.valueOf(hwInfo.getTotalStorage()));

//            User user;
//            if (PreferencesUtil.contains(this, PreferencesUtil.USER)) {
//                user = PreferencesUtil.get(this, PreferencesUtil.USER,
//                        User.class);
//            } else {
//                user = new User();
//
//            }

            // params.put("first_name", user.getFirstName());
            //params.put("last_name", user.getLastName());
            params.put("phone", token.getPhone());
            Location locationInfo = ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE)).getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (locationInfo != null) {
                params.put("latitude", locationInfo.getLatitude() + "");
                params.put("longitude", locationInfo.getLongitude() + "");
            }
            params.put("city", "");
            params.put("country", "");
            params.put("distinct_id", distinctId);
            params.put("play_store_email_id", Utils.getAccountMail(context));
            params.put("region", "");
            params.put("app_version", hwInfo.getTaxiAppAppVersion());
            params.put("taxiapp_application_id", "");
            params.put("taxiapp_app_gcm_registration_id", PreferencesUtil.getString(context, AppConstants.GOOGLE_SENDER_ID, ""));
            //     String taxiappProfileEmail = user.getEmailAddress();

            //   params.put("taxiapp_profile_email_id", taxiappProfileEmail);
            params.put("operating_system", hwInfo.getOperatingSystem());
            params.put("os_version", hwInfo.getOsVersion());
            params.put("mixpanel_library", "");
            params.put("mixpanel_library_version", "");
            params.put("carrier", hwInfo.getCarrier());
            params.put("wifi", "");
            params.put("data_connection", String.valueOf(hwInfo.isDataConnAvailable()));
            params.put("data_mode", "");
            params.put("sim_card_number", hwInfo.getSimCardNumber());
            params.put("is_roaming", hwInfo.isRoaming() ? "1" : "0");

            String r = session.get(APIUrl.DEVICE_DATA_COLLECTION.getFullPath(), params, null);
            if (StringUtils.isBlank(r)) {
                throw new IOException();
            }
            AppLogger.get().i(APIUtils.class, "Sending Device Details Response:" + r);
            return true;
        } catch (Exception e) {
            AppLogger.get().e(APIUtils.class, e);
        }

        return false;

    }
}
