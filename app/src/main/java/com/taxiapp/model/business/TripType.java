package com.taxiapp.model.business;

import android.util.Log;

public enum TripType {
    OUTSTATION("Outstation", "OUT"), LOCAL("Local", "LOC"), AIRPORT("Airport", "TRA"), UNKNOWN("UNKNOWN", "UNKNOWN");

    private String title;
    private String code;

    private TripType(String title, String code) {
        this.title = title;
        this.code = code;
    }

    @Override
    public String toString() {
        return title;
    }

    public String getCode() {
        return code;
    }

    public static TripType getTripType(int position) {
        return values()[position];
    }

    public static TripType getTripType(String alias) {
        for (TripType t : values()) {
            if (alias.equals(t.code)) {
                return t;
            }
        }
        Log.d("TaxiApp", "getTripType::returning null");
        return UNKNOWN;
    }
}