package com.taxiapp.model.business;

import org.apache.commons.lang.StringUtils;

/**
 * Created by sarath on 27/11/15.
 */
public enum DriverState {
    IDLE,

    START_TRIP,

    REACHED_PICKUP,

    CUSTOMER_BOARDED,

    START_DAY_TRIP,

    END_DAY_TRIP,

    END_TRIP,

    ONGOING,

    RESUME_TRIP,

    PAUSE_TRIP,

    CONTINUE,

    TRIP_OVER;

    public static DriverState get(String checkPoint) {
        for (DriverState driverState : DriverState.values()) {
            if (StringUtils.equalsIgnoreCase(driverState.name(), checkPoint)) {
                return driverState;
            }
        }
        return CONTINUE;
    }

    public static DriverState getNext(DriverState driverState) {
        switch (driverState) {
            case IDLE:
                return START_TRIP;
            case START_TRIP:
                return REACHED_PICKUP;
            case REACHED_PICKUP:
                return CUSTOMER_BOARDED;
            case CUSTOMER_BOARDED:
                return END_TRIP;
            case PAUSE_TRIP:
                return ONGOING;
            case RESUME_TRIP:
                return ONGOING;
            case END_TRIP:
                return END_TRIP;
        }

        return ONGOING;
    }
}
