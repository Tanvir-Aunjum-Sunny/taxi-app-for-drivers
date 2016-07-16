package com.taxiapp.model.business;

import com.taxiapp.utils.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * An object to hold information pertaining to the user's chosen trip. Made this
 * new class, because I didn't want to break any of the old classes. This is a
 * more intelligent class than the {@link Trip} class.
 * 
 * @author Sreedevi.Jagannath
 */
public class TripInformation {
    private static final String ARROW_UNICODE = " \u0026rarr; ";
    private static final String DEFAULT_DROP_ADDRESS = "dropoff_address";
    private static final String PARAM_DROPOFF_ADDRESS = "dropoff_address";
    private static final String PARAM_LOCALITY_ID = "locality_id";
    private static final String TIME_FORMAT_12_HR_HH_MM_AA = "hh:mm aa";
    private static final String TIME_FORMAT_HH_MM_SS = "HH:mm:ss";
    private static final String PARAM_PICKUP_TIME = "pickup_time";
    private static final String PARAM_JOURNEY_END_DATE = "journey_end_date";
    private static final String PARAM_JOURNEY_START_DATE = "journey_start_date";
    private static final String PARAM_TRIP_SUB_TYPE = "trip_sub_type";
    private static final String PARAM_CITY_ID = "city_id";
    public static final String PARAM_PICKUP_ADDRESS = "pickup_address";
    private static final String DATE_FORMAT_yyyy_MM_dd = "yyyy-MM-dd";
    public static final String TRIP_SUBTYPE_OWI = "OUT_ONEWAY";
    public static final String TRIP_SUBTYPE_TWI = "OUT_ROUND";
    private static final String PARAM_DESTINATION_CITY = "dest_city_id";
    private static final String PARAM_ITINERARY_CITIES = "itinerary_city_id";
    private static final String PARAM_SOURCE = "source";
    // this is not to change, source for mobile is always APP
    private static final String DEFAULT_VALUE_PARAM_SOURCE = "APP";
    private static final String PARAM_API_KEY = "api_key";
    /**
     * {@linkplain TripType} Airport, Local, Outstation
     */
    private TripType mTripType;
    private Date mJourneyStartDate;
    private Date mJourneyEndDate;
    private List<City> mPointCities;
    private Locality mPickupLocality;
    private Locality mDropLocality;
    private String mTripSubType;
    private int mDuration;
    private static final String CITY_ID_SEPARATOR = "|";
    private static final String TAG = TripInformation.class.getSimpleName();

    public TripInformation(TripType tripType, String tripSubType) throws IllegalArgumentException {
        if (tripType == null) {
            throw new IllegalArgumentException("Invalid trip type: " + tripType);
        }
        if (tripSubType == null || tripSubType.isEmpty()) {
            throw new IllegalArgumentException("Invalid trip sub type:" + tripSubType);
        }
        setTripType(tripType);
        setTripSubType(tripSubType);
    }

    public Date getJourneyEndDate() {
        return mJourneyEndDate;
    }

    public void setJourneyEndDate(Date mJourneyEndDate) {
        this.mJourneyEndDate = mJourneyEndDate;
    }

    public int calculateDuration() {
        if (mJourneyEndDate == null || mJourneyStartDate == null) {
            setDuration(0);
        } else {
            if (TRIP_SUBTYPE_OWI.equals(getTripSubType())) {
                setDuration(1);
            } else {
                long diff = mJourneyEndDate.getTime() - mJourneyStartDate.getTime();
                double daysDiff = Math.ceil(diff / (double) (24 * 60 * 60 * 1000)) + 1;
                logd("daysDiff = " + daysDiff + "\n" + mJourneyEndDate + "\n" + mJourneyStartDate);
                setDuration((int) daysDiff);
            }
        }
        return getDuration();
    }

    public Date getJourneyStartDate() {
        return mJourneyStartDate;
    }

    public void setJourneyStartDate(Date mJourneyStartDate) {
        this.mJourneyStartDate = mJourneyStartDate;
    }

    public String getStartDateInFormat(String format) {
        return getDateInFormat(mJourneyStartDate, format);
    }

    public String getEndDateInFormat(String format) {
        return getDateInFormat(mJourneyEndDate, format);
    }

    /**
     * @param date
     *            The {@linkplain Date} object to get the string format for
     * @param format
     *            The string pattern ex: DD-MM-YYYY
     * @return null, if the @a
     */
    private String getDateInFormat(Date date, String format) {
        if (date == null) {
            return null;
        }
        if (format == null || format.isEmpty()) {
            format = DATE_FORMAT_yyyy_MM_dd;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        return dateFormat.format(date);
    }

    public TripType getTripType() {
        return mTripType;
    }

    public void setTripType(TripType tripType) {
        this.mTripType = tripType;
    }

    public List<City> getPointCities() {
        return mPointCities;
    }

    public void setPointCities(List<City> pointCities) {
        this.mPointCities = pointCities;
    }

    public Locality getPickupLocality() {
        return mPickupLocality;
    }

    public void setPickupLocality(Locality pickupLocality) {
        this.mPickupLocality = pickupLocality;
    }

    public Locality getDropLocality() {
        return mDropLocality;
    }

    public void setDropLocality(Locality dropLocality) {
        this.mDropLocality = dropLocality;
    }

    public String getTripSubType() {
        return mTripSubType;
    }

    public void setTripSubType(String mTripSubType) {
        this.mTripSubType = mTripSubType;
    }

    /**
     * @return a Map<String, String> of params required for the trip type
     *         specified by the internal TripType object. Null if not all fields
     *         are available or valid.
     */
    public Map<String, String> getCabsListApiParams() {
        if (!validateSelf(false)) {
            return null;
        }
        Map<String, String> params = getBasicParams();
        switch (TripType.getTripType(mTripType.getCode())) {
            case AIRPORT:
            case LOCAL:
                // not implemented yet
                return null;
            case OUTSTATION:
                if (TRIP_SUBTYPE_OWI.equals(getTripSubType())) {
                    params.put(PARAM_DESTINATION_CITY, mPointCities.get(1).getCityId());
                    params.put(PARAM_LOCALITY_ID, getPickupLocality().getSubarea_id());
                } else {
                    params.put(PARAM_ITINERARY_CITIES, getCityIdsList());
                }
            break;
        }
        return params;
    }

    private Map<String, String> getBasicParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(PARAM_API_KEY, "");
        params.put(PARAM_SOURCE, DEFAULT_VALUE_PARAM_SOURCE);
        params.put(PARAM_CITY_ID, mPointCities.get(0).getCityId());
        //params.put(Constants.TRIP_TYPE, mTripType.getCode());
        params.put(PARAM_JOURNEY_START_DATE, getStartDateInFormat(null));
        String journeyEndDate = getEndDateInFormat(null);
        params.put(PARAM_JOURNEY_END_DATE, journeyEndDate == null ? getStartDateInFormat(null) : journeyEndDate);
        params.put(PARAM_PICKUP_TIME, getStartDateInFormat(TIME_FORMAT_HH_MM_SS));
        params.put(PARAM_TRIP_SUB_TYPE, getTripSubType());
        // the following params are required, but not necessary.
        // putting in blank here, if code needs to replace params, will
        // replace
        params.put(PARAM_LOCALITY_ID, "0");
        params.put(PARAM_DESTINATION_CITY, "");
        params.put(PARAM_ITINERARY_CITIES, "");
        return params;
    }

    /**
     * @param userAddress
     *            The address manually entered in the edit text
     * @return a Map<String, String> of params required for the trip type
     *         specified by the internal TripType object. Null if not all fields
     *         are available or valid.
     */
    public Map<String, String> getBookingApiParams(String userAddress) {
        // since all of those are already necessary
        Map<String, String> params = getCabsListApiParams();
        // then add the extras
        params.put(PARAM_PICKUP_ADDRESS, userAddress);
        switch (TripType.getTripType(mTripType.getCode())) {
            case AIRPORT:
            case LOCAL:
                // not implemented yet
                return null;
            case OUTSTATION:
                params.put(PARAM_DROPOFF_ADDRESS, DEFAULT_DROP_ADDRESS);
            break;
        }
        return params;
    }

    private String getCityIdsList() {
        String itineraryCityIds = "";
        if (mPointCities != null && mPointCities.size() > 1) {
            for (int i = 1; i < mPointCities.size(); i++) {
                itineraryCityIds += mPointCities.get(i).getCityId() + CITY_ID_SEPARATOR;
            }
        }
        // replace last "|"
        itineraryCityIds = itineraryCityIds.replaceAll("\\" + CITY_ID_SEPARATOR + "$", "");
        return itineraryCityIds;
    }

    /**
     * @param isKnownIncomplete
     *            some parameters will not be validated when rebook = true. Set
     *            to false if all required params to be validated.
     * @return true if all fields are available or valid, satisfying params
     *         required for the trip type specified by the internal TripType
     *         object. False otherwise.
     */
    public boolean validateSelf(boolean isKnownIncomplete) {
        if (mTripType == null || mTripSubType == null || mJourneyStartDate == null || mJourneyEndDate == null
                || mPointCities == null || mPointCities.isEmpty()) {
            logd("basic info missing: " + mTripType + mTripSubType + mJourneyStartDate + mJourneyEndDate + mPointCities);
            return false;
        }
        switch (TripType.getTripType(mTripType.getCode())) {
            case AIRPORT:
            case LOCAL:
            // not validated yet
            break;
            case OUTSTATION:
                boolean isValid = true;// because we are using &=
                if (mTripSubType.equalsIgnoreCase(TRIP_SUBTYPE_OWI)) {
                    if (!isKnownIncomplete) {
                        isValid &= !(mPickupLocality == null);
                    }
                    isValid &= (mPointCities.size() == 2);
                    isValid &= !(mPointCities.get(0).equals(mPointCities.get(1)));
                } else if (mTripSubType.equalsIgnoreCase(TRIP_SUBTYPE_TWI)) {
                    if (!isKnownIncomplete) {
                        // because in rebook scenario, destination are null
                        isValid &= mPointCities.size() >= 2;
                    }
                    isValid &= mDuration > 0;
                } else {
                    // invalid subtype
                    isValid = false;
                }
                return isValid;
        }
        return false;
    }

    /**
     * @return the first city in the list of cities chosen for the journey. Null
     *         if no cities chosen yet.
     */
    public City getStartCity() {
        if (mPointCities != null && !mPointCities.isEmpty()) {
            return mPointCities.get(0);
        }
        return null;
    }

    public String getHumanReadableTime(Date date) {
        if (date == null)
            return null;
        return getDateInFormat(date, TIME_FORMAT_12_HR_HH_MM_AA);
    }

    public String getHumanReadableDateAndTime(Date date) {
        if (date == null)
            return null;
        return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.ENGLISH).format(date);
    }

    public String getDestinations() {
        if (mPointCities != null && mPointCities.size() > 1) {
            String to = "";
            for (int i = 1; i < mPointCities.size() - 1; i++) {
                String cityName = mPointCities.get(i).getName();
                String[] cityNameParts = cityName.split(",");
                to += cityNameParts[0] + ARROW_UNICODE;
            }
            String[] cityNameParts = mPointCities.get(mPointCities.size() - 1).getName().split(",");
            to += cityNameParts[0];
            return to;
        }
        return null;
    }

    /**
     * @return the duration of the trip, in days
     */
    public int getDuration() {
        return mDuration;
    }

    /**
     * @param duration
     *            the duration of the trip, in days
     */
    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    /********************** LOGGING **********************/
    private static Logger mLogger = Logger.getInstance();

    private static void logd(String message) {
        mLogger.d(TAG, message);
    }
    /********************** END_TRIP LOGGING **********************/
}
