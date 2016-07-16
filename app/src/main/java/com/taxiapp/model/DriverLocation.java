package com.taxiapp.model;

import android.location.Location;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.taxiapp.db.DriverLocationDAO;
import com.taxiapp.net.api.SyncStatus;

import java.util.Date;


/**
 * Created by sarath on 19/11/15.
 */
@DatabaseTable(daoClass = DriverLocationDAO.class)
public class DriverLocation {


    public static final String IDLE_TRIP_ID = "idle";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    @SerializedName("phone_number")
    private String phoneNumber;

    @DatabaseField
    @SerializedName("token")
    private String token;

    @DatabaseField
    @SerializedName("booking_id")
    private String bookingId;

    @DatabaseField
    @SerializedName("latitude")
    private double latitude;

    @DatabaseField
    @SerializedName("longitude")
    private double longitude;

    @DatabaseField
    @SerializedName("accuracy")
    private double accuracy;

    @DatabaseField
    @SerializedName("checkpoint")
    private String checkpoint;

    @DatabaseField
    @SerializedName("captured_time")
    private long capturedTime;

    @DatabaseField
    @SerializedName("travelled")
    private double travelled;

    @DatabaseField
    private double spanTravelled;

    @DatabaseField
    private int day;

    @DatabaseField
    private SyncStatus syncStatus = SyncStatus.FAILED;


    public DriverLocation() {

    }

    public DriverLocation(String bookingId, double latitude, double longitude, double accuracy, String checkpoint, long capturedTime, double travelled) {
        this.bookingId = bookingId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
        this.checkpoint = checkpoint;
        this.capturedTime = capturedTime;
        this.travelled = travelled;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getCapturedTime() {
        return capturedTime;
    }

    public void setCapturedTime(long capturedTime) {
        this.capturedTime = capturedTime;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public double getTravelled() {
        return travelled;
    }

    public void setTravelled(double travelled) {
        this.travelled = travelled;
    }

    public String getCheckpoint() {
        return checkpoint;
    }

    public void setCheckpoint(String checkpoint) {
        this.checkpoint = checkpoint;
    }

    public double getSpanTravelled() {
        return spanTravelled;
    }

    public void setSpanTravelled(double spanTravelled) {
        this.spanTravelled = spanTravelled;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public SyncStatus getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(SyncStatus syncStatus) {
        this.syncStatus = syncStatus;
    }

    public static DriverLocation newDriverLocation(Location location) {
        DriverLocation loc = new DriverLocation();

        if (location != null) {
            loc.setLatitude(location.getLatitude());
            loc.setLongitude(location.getLongitude());
            loc.setAccuracy(location.getAccuracy());
        }

        loc.setCapturedTime(new Date().getTime());
        return loc;
    }

    public static DriverLocation newDriverLocation(DriverLocation location) {
        DriverLocation loc = new DriverLocation();


        loc.setLatitude(location.getLatitude());
        loc.setLongitude(location.getLongitude());
        loc.setAccuracy(location.getAccuracy());
        loc.setCapturedTime(new Date().getTime());
        return loc;
    }


    @Override
    public String toString() {
        return "DriverLocation{" +
                "id=" + id +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", token='" + token + '\'' +
                ", bookingId='" + bookingId + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", accuracy=" + accuracy +
                ", checkpoint='" + checkpoint + '\'' +
                ", capturedTime=" + capturedTime +
                ", travelled=" + travelled +
                ", spanTravelled=" + spanTravelled +
                '}';
    }
}
