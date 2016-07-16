package com.taxiapp.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Debug;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class HardwareInfoProvider {

    public static int getScreenHeight(Context context) {
        DisplayMetrics displaymetrics = context.getResources()
                .getDisplayMetrics();
        return displaymetrics.heightPixels;
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics displaymetrics = context.getResources()
                .getDisplayMetrics();
        return displaymetrics.widthPixels;
    }

    public static int getScreenDpi(Context context) {
        DisplayMetrics displaymetrics = context.getResources()
                .getDisplayMetrics();
        return ((int) Math.round((displaymetrics.density * 160.0)));
    }

    public static double getCpuClockSpeed(Context context) throws IOException {
        RandomAccessFile reader = new RandomAccessFile(
                "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq", "r");
        Double clockSpeed = (Double.parseDouble(reader.readLine()) / 1000);
        reader.close();
        return clockSpeed;
    }

    public static float cpuUsed(Context context) {
        try {
            RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
            String load = reader.readLine();
            String[] toks = load.split(" ");
            long idle1 = Long.parseLong(toks[5]);
            long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3])
                    + Long.parseLong(toks[4]) + Long.parseLong(toks[6])
                    + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);
            try {
                Thread.sleep(360);
            } catch (Exception e) {
            }
            reader.seek(0);
            load = reader.readLine();
            reader.close();
            toks = load.split(" ");
            long idle2 = Long.parseLong(toks[5]);
            long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3])
                    + Long.parseLong(toks[4]) + Long.parseLong(toks[6])
                    + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);
            return (float) (cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public static long memoryUsed() {
        long used_memory = 1L;
        System.err.println(Runtime.getRuntime().totalMemory());
        // Get Allocated VM Memory by calling:
        used_memory = Runtime.getRuntime().totalMemory()
                - Runtime.getRuntime().freeMemory();
        System.err.println(used_memory);
        // Get VM Heap Size Limit by calling:
        System.err.println(Runtime.getRuntime().maxMemory());
        // Get Native Allocated Memory by calling:
        Debug.getNativeHeapAllocatedSize();
        return used_memory;
    }

    public static int batteryLevel(Context context) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int batteryPct = (level * 100) / scale;
        return batteryPct;
    }

    public static String batteryStatus(Context context) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        int status = batteryStatus
                .getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        switch (status) {
            case 0:
                return "discharging";
            case BatteryManager.BATTERY_PLUGGED_AC:
                return "ac";
            case BatteryManager.BATTERY_PLUGGED_USB:
                return "usb";
            // case BatteryManager.BATTERY_PLUGGED_WIRELESS:
            // return "wireless";
            default:
                return "unknown";
        }
    }

    public static double getTotalSystemMemory() throws IOException {
        RandomAccessFile reader = new RandomAccessFile("/proc/meminfo", "r");
        String line = reader.readLine();
        reader.close();
        // line = line.replaceAll("\\s{2,}", " "); // replace a sequence of
        // whitespace with a single
        // whitespace
        String tokens[] = line.split("\\s{1,}");
        int i = 0;
        for (String s : tokens) {
            Log.d("Check", s + "$" + i);
            i++;
        }
        return (Double.parseDouble(tokens[1].trim()) / 1024.0);
    }

    public static boolean externalStorageAvailable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public static long getAvailableInternalStorageSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return (availableBlocks * blockSize) / (1024 * 1024);
    }

    public static long getTotalInternalStorageSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return (totalBlocks * blockSize) / (1024 * 1024);
    }

    public static long getAvailableExternalStorageSize(int retValue) {
        if (externalStorageAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return (availableBlocks * blockSize);
        } else {
            return retValue;
        }
    }

    public static String getImeiNumber(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();

    }

    public static String getUID(Context context) {
        return DeviceID.getID(context);

    }

    public static String getSimId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getSimSerialNumber();
    }

    public static long getTotalExternalStorageSize(int retValue) {
        if (externalStorageAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return (totalBlocks * blockSize) / (1024 * 1024);
        } else {
            return retValue;
        }
    }

    public static boolean getBluetoothStatus() {
        return BluetoothAdapter.getDefaultAdapter().isEnabled();
    }


    private static class DeviceID {
        private static String ID = null;
        // static Context context;
        private static final String SHARED_PREF = "deviceId";

        // return a cached unique ID for each device
        public static String getID(Context context) {
            SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF,
                    Context.MODE_PRIVATE);
            // if the ID isn't cached inside the class itself
            if (ID == null) {
                // get it from database / settings table (implement your own method
                // here)
                ID = prefs.getString("DeviceID", "0");
            }

            // if the saved value was incorrect
            if (ID.equals("0")) {
                // generate a new ID
                ID = generateID(context);

                if (ID != null) {
                    // save it to database / setting (implement your own method
                    // here)
                    prefs.edit().putString("DeviceID", ID).commit();
                }
            }

            return ID;
        }

        // generate a unique ID for each device
        // use available schemes if possible / generate a random signature instead
        private static String generateID(Context context) {

            // use the ANDROID_ID constant, generated at the first device boot
            String deviceId = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);

            // in case known problems are occured
            if ("9774d56d682e549c".equals(deviceId) || deviceId == null) {

                // get a unique deviceID like IMEI for GSM or ESN for CDMA phones
                // don't forget:
                // <uses-permission
                // android:name="android.permission.READ_PHONE_STATE" />
                deviceId = ((TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();

                // if nothing else works, generate a random number
                if (deviceId == null) {

                    Random tmpRand = new Random();
                    deviceId = String.valueOf(tmpRand.nextLong());
                }

            }

            // any value is hashed to have consistent format
            return getHash(deviceId);
        }

        // generates a SHA-1 hash for any string
        public static String getHash(String stringToHash) {

            MessageDigest digest = null;
            try {
                digest = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            byte[] result = null;

            try {
                result = digest.digest(stringToHash.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            StringBuilder sb = new StringBuilder();

            for (byte b : result) {
                sb.append(String.format("%02X", b));
            }

            String messageDigest = sb.toString();
            return messageDigest;
        }
    }
}
