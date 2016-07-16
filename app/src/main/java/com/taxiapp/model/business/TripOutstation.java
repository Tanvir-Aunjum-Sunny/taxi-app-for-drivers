package com.taxiapp.model.business;

import com.taxiapp.utils.Logger;
import com.taxiapp.utils.Utils;

import java.util.ArrayList;

public class TripOutstation extends Trip {

    private static final int CITY_ID_BANGALORE = 377;
    private static final int CITY_ID_CHENNAI = 81;
    private static final String CITY_NAME_CHENNAI = "Chennai";
    private static final String CITY_NAME_BANGALORE = "Bangalore";
    private static final String TAG = TripOutstation.class.getSimpleName();
    private TripInformation mTripInformation;

    public TripOutstation(TripInformation tripInfo) {
        mTripInformation = tripInfo;
        if (tripInfo != null) {
            setTripType(tripInfo.getTripType());
            setLocal_type(tripInfo.getTripSubType());
        }
    }

    public TripInformation getTripInformation() {
        return mTripInformation;
    }

    /**
     * Fill certain information into self from instance of {@link Booking}
     * present in object. For use in "rebook" scenario. This should ideally be
     * constructor injection, but that meant a lot of code adjustment. This is
     * not a bad way, just not the best way.
     */
    public void fromBooking() {
        Booking booking = getBooking();
        if (booking == null) {
            logd("fromBooking::booking is null.");
            return;
        }
        City city = new City();
        city.setCityId(booking.getPick_city());
        city.setName(booking.getPick_city());
        String subType = booking.getTrip_type();
        if(mTripInformation == null){
            mTripInformation = new TripInformation(getTripType(), subType);
        } 
        ArrayList<City> pointCities = new ArrayList<>();
        pointCities.add(0, city);
        if (TripInformation.TRIP_SUBTYPE_OWI.equals(subType)) {
            City otherCity;
            int startCityId = Integer.parseInt(city.getCityId());
            otherCity = getOtherCity(startCityId);
            pointCities.add(1, otherCity);
            mTripInformation.setPickupLocality(getPickupAddress().getLocality());
            Locality dropLocality = new Locality();
            dropLocality.setValue(otherCity.getName());
            dropLocality.setSubarea_id("0");
            mTripInformation.setDropLocality(dropLocality);
        }
        mTripInformation.setPointCities(pointCities);
        try {
            mTripInformation.setJourneyStartDate(Utils.getDateFromString(booking.getTrip_start_date(),
                    booking.getStart_time()));
            mTripInformation.setJourneyEndDate(Utils.getDateFromString(booking.getTrip_end_date(), null));
            mTripInformation.calculateDuration();
        } catch (IllegalArgumentException ex) {
            logd("getDateFromString::" + ex.getMessage());
        }
    }

    /**
     * This is a simple hack for one way intercity. Currently only two cities
     * are known, hence we use this technique. Remove from multicity.
     * 
     * @param startCityId
     *            either 377 (Bangalore) or 81 (Chennai)
     * @return null if startCity id is < 0, or neither of bangalore/chennai city
     *         ids.
     */
    public City getOtherCity(int startCityId) {
        if(startCityId < 0 || !(startCityId == CITY_ID_BANGALORE || startCityId == CITY_ID_CHENNAI) ){
            logd("getOtherCity::Invalid city id: " + startCityId);
            return null;
        }
        int endCityId;
        String endCityName;
        if(startCityId == CITY_ID_BANGALORE){
            endCityId = CITY_ID_CHENNAI;
            endCityName = CITY_NAME_CHENNAI;
        }else{
            endCityId = CITY_ID_BANGALORE;
            endCityName = CITY_NAME_BANGALORE;
        }
        City city = new City();
        city.setCityId(String.valueOf(endCityId));
        city.setName(endCityName);
        return city;
    }

    /********************** LOGGING **********************/
    private static Logger mLogger = Logger.getInstance();

    private static void logd(String message) {
        mLogger.d(TAG, message);
    }

    /********************** END_TRIP LOGGING **********************/
}
