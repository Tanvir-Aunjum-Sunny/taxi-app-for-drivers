package com.taxiapp.utils;

import android.content.Context;
import android.os.Build;

import com.taxiapp.model.DeviceHardwareInfo;

import java.io.FileNotFoundException;
import java.io.IOException;

public class DeviceInfoDesk {
    private static String regId = null;

    public static DeviceHardwareInfo getDeviceHardwareInfo(Context context)
            throws IOException, NumberFormatException, FileNotFoundException {
        DeviceHardwareInfo hwInfo = new DeviceHardwareInfo();
        // Screen Dimensions
        hwInfo.setScreenHeight(HardwareInfoProvider.getScreenHeight(context));
        hwInfo.setScreenWidth(HardwareInfoProvider.getScreenWidth(context));
        hwInfo.setDpi(HardwareInfoProvider.getScreenDpi(context));
        // Device Model
        hwInfo.setModel(Build.MODEL);
        // Os Verison
        hwInfo.setOsVersion(Build.VERSION.RELEASE);
        // Os
        hwInfo.setOperatingSystem("Android");
        // CPU Clock speed
        hwInfo.setCpuSpeed(HardwareInfoProvider.getCpuClockSpeed(context));
        // MemInfo
        hwInfo.setTotalMemory(HardwareInfoProvider.getTotalSystemMemory());
        // storage
        hwInfo.setTotalStorage(HardwareInfoProvider
                .getTotalExternalStorageSize(0)
                + HardwareInfoProvider.getTotalInternalStorageSize());
        hwInfo.setUid(HardwareInfoProvider.getUID(context));

        hwInfo.setImeiNumber(HardwareInfoProvider.getImeiNumber(context));
        hwInfo.setSimCardNumber(HardwareInfoProvider.getSimId(context));
        hwInfo.setGcmId(getRegId(context));

        hwInfo.setBtEnabled(HardwareInfoProvider.getBluetoothStatus());
        hwInfo.setRoaming(NetworkInfoProvider.isRoaming(context));

        hwInfo.setCarrier(NetworkInfoProvider.getCurrentCarrierName(context));
        hwInfo.setDataConnAvailable(NetworkInfoProvider.isDataConnAvailable(context));
        
        return hwInfo;
    }


    public static String getRegId(Context context) throws IOException {
        regId = PreferencesUtil.getString(context, AppConstants.GOOGLE_SENDER_ID,
                null);
        if (regId == null) {
            regId = registerToGCM(context);
        }
        return regId;
    }

    private static String registerToGCM(final Context context)
            throws IOException {
//        Log.w("GCM", "Getting GCM Registration id");
//        try{
//            GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
//            int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
//            if (resultCode == ConnectionResult.SUCCESS) {
//                InstanceID instanceID = InstanceID.getInstance(context);
//                String token = instanceID.getToken(AppConstants.GOOGLE_SENDER_ID,
//                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
//                // [END get_token]
//                Log.d("GCM", "GCM Registration Id: " + token);
//                PreferencesUtil.putString(context, AppConstants.GOOGLE_SENDER_ID, token);
//                regId = token;
//            }
//        }
//        catch (Exception ex){
//            regId = null;
//            Log.e("GCM", "Error while registering to GCM", ex);
//        }
        return regId;
    }

}