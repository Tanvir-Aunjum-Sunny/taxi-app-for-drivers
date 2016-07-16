package com.taxiapp.gps.util;

import com.taxiapp.model.DriverLocation;

/**
 * Created by sarath on 20/11/15.
 */
public class GeoUtil {

    public static final double MTS_MULTIPLIER = 1609.344;

    public static double deg2rad(double deg) {
        return (deg * Math.PI) / 180.0;
    }

    public static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }


    public static double distance(DriverLocation pos1,DriverLocation pos2){
        double delta = pos1.getLongitude() - pos2.getLongitude();
        double dist = Math.sin(deg2rad(pos1.getLatitude())) * Math.sin(deg2rad(pos2.getLatitude()))
                + Math.cos(deg2rad(pos1.getLatitude()))*Math.cos(deg2rad(pos2.getLatitude())) * Math.cos(deg2rad(delta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        return dist*MTS_MULTIPLIER;
    }



    public static double distance(double lat1, double lon1, double lat2, double lon2,
                                  char unit) {
        double delta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))*Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(delta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return dist;
    }

    public static boolean isNear(double lat1, double lon1, double lat2, double lon2,double range) {
        boolean ys = false;
        double dist = distance(lat1, lon1,
                lat2,lon2, 'K');
        if(dist <= range){
            ys = true;
            System.out.println("Shortest Distance is " + dist);
        }

        return ys;
    }



}
