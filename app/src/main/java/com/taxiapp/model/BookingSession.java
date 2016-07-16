package com.taxiapp.model;

import com.taxiapp.model.business.Booking;
import com.taxiapp.model.business.DriverState;

import java.io.Serializable;

/**
 * Created by sarath on 20/11/15.
 */
public class BookingSession implements Serializable {

    private Booking booking;

    private DriverLocation lastCheckPoint;
    private DriverState driverState = DriverState.IDLE;
    private DriverState previousState = DriverState.IDLE;
    private int day = 1;

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public DriverLocation getLastCheckPoint() {
        return lastCheckPoint;
    }

    public void setLastCheckPoint(DriverLocation lastCheckPoint) {
        this.lastCheckPoint = lastCheckPoint;
    }

    public String getBookingId() {
        return booking.getBooking_id();
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public DriverState getDriverState() {
        return driverState;
    }

    public void setDriverState(DriverState driverState) {
        this.driverState = driverState;
    }

    public DriverState getPreviousState() {
        return previousState;
    }

    public void setPreviousState(DriverState previousState) {
        this.previousState = previousState;
    }
}
