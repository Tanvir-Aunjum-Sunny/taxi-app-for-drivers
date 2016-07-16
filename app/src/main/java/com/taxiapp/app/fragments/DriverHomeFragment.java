package com.taxiapp.app.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.taxiapp.SampleJson;
import com.taxiapp.app.activities.NewTripsActivity;
import com.taxiapp.app.activities.StartTripActivity;
import com.taxiapp.model.BookingSession;
import com.taxiapp.model.HomeItem;
import com.taxiapp.model.UserOtpToken;
import com.taxiapp.model.business.Booking;
import com.taxiapp.net.WebSession;
import com.taxiapp.utils.AnalyticsUtility;
import com.taxiapp.utils.AppConstants;
import com.taxiapp.utils.AppLogger;
import com.taxiapp.utils.GlobalData;
import com.taxiapp.utils.PreferencesUtil;
import com.taxiapp.utils.ProgressAsyncTask;
import com.taxiapp.utils.Utils;
import com.taxiapp.vendor.app.R;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amit S on 16/11/15.
 */
public class DriverHomeFragment extends Fragment implements View.OnClickListener {
    private View rootView;

    private Button newTripBtn, startTripBtn;

    private UserOtpToken token;
    private TextView newTripNotification, startTripNotification;
    private List<HomeItem> response = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home_driver, null);
        token = AppConstants.getToken(getActivity());
        AppLogger.get().d(this.getClass(), "Creating Fragment DriverHome");
        addViewFunctionalities();
        //fetchHomeItems();
        return rootView;
    }

    private void fetchHomeItems() {
//        BookingSession state = getBookingStateFromValidSource();
        new HomeItemsTask(getActivity()).execute();
    }

    public void onResume() {
        super.onResume();
        if (Utils.hasNetwork(getActivity())) {
            fetchHomeItems();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newTrip:
                // SENDING ANALYTICS
                AnalyticsUtility.sendGAEvent(getActivity(), "Home-Page-Buttons", "New-Trip", "New-Trip-Home-Page-Item");
                new NewTripsTask(getActivity()).execute();
                break;
            case R.id.startTrip:
                AppLogger.get().d(this.getClass(), "Start Trip Clicked");
                //PreferencesUtil.remove(getActivity(), AppConstants.BOOKING_STATE_KEY);
                BookingSession state = getBookingStateFromValidSource();
                if (state == null) {
                    new GetBookingTask(getActivity()).execute();
                } else {
                    onClickingStartTrip(state.getBooking());
                }
                // SENDING ANALYTICS
                AnalyticsUtility.sendGAEvent(getActivity(), "Home-Page-Buttons", "Start-Trip", "Start-Trip-Home-Page-Item");


                break;

            case R.id.myBookings:
                // SENDING ANALYTICS
                AnalyticsUtility.sendGAEvent(getActivity(), "Home-Page-Buttons", "My-Bookings", "My-Bookings-Home-Page-Item");

                break;
        }
    }


    public void addViewFunctionalities() {
        newTripBtn = (Button) rootView.findViewById(R.id.newTrip);
        newTripBtn.setOnClickListener(this);

        startTripBtn = (Button) rootView.findViewById(R.id.startTrip);
        startTripBtn.setOnClickListener(this);

        newTripNotification = (TextView) rootView.findViewById(R.id.newTripNotification);
        startTripNotification = (TextView) rootView.findViewById(R.id.startTripNotification);


    }

    public void onClickingStartTrip(Booking booking) {
        Intent intent = new Intent(getActivity(), StartTripActivity.class);
        //Booking topBooking = currentBooking;//gson.fromJson(SampleJson.BOOKING, Booking.class);
        //Booking topBooking = gson.fromJson(SampleJson.BOOKING, Booking.class);
        intent.putExtra("booking", booking);

        startActivity(intent);
    }


    private class HomeItemsTask extends ProgressAsyncTask {
        public HomeItemsTask(Context ctx) {
            super(ctx);
        }

        @Override
        public void onPostExecute(Integer result) {
            super.onPostExecute(result);

            if (result == FAILED) {
                Utils.showPrompt(getActivity(), "Error", errorMessage, true);
            } else if (result == SUCCESS) {
                updateHomeNotifIcons();
            }

        }

        @Override
        protected Integer doInBackground(String... strings) {
            try {
                if (!Utils.hasNetwork(ctx)) {
                    return NO_NETWORK;
                }
                //response = APIUtils.homeScreenData(token);

//                if (response == null) {
//                    errorMessage = "Could not fetch booking details. Please try again";
//                    return FAILED;
//                }
                response = new ArrayList<>();
                response.add(new HomeItem(HomeItem.NEW_TRIP, 1));
                response.add(new HomeItem(HomeItem.START_A_TRIP, 1));

                return SUCCESS;
            } catch (Exception e) {
                AppLogger.get().e(getClass(), e);
            }
            return FAILED;
        }
    }

    private void updateHomeNotifIcons() {

        for (HomeItem item : response) {
            if (item.getContext() == HomeItem.NEW_TRIP) {
                newTripNotification.setText("" + item.getCount());
            }

            if (item.getContext() == HomeItem.START_A_TRIP) {
                startTripNotification.setText("" + item.getCount());
            }
        }
    }

    public BookingSession getBookingStateFromValidSource() {
        BookingSession bookingSession = null;
        bookingSession = PreferencesUtil.get(getActivity(), AppConstants.BOOKING_STATE_KEY, BookingSession.class);
//        if (bookingSession == null || bookingSession.getBooking() == null) {
//            bookingSession = new BookingSession();
//            Booking booking = new Gson().fromJson("{\"allocation_type\":\"manual\",\"bill_number\":\"\",\"booking_date\":\"05-Jan-2016\",\"booking_id\":\"2248480\",\"booking_status\":\"Active\",\"booking_time\":\"10:43:09\",\"cab_class\":\"3\",\"car_type\":\"AC Economy(Tata Indica or Equivalent)\",\"cashtocollect\":\"\",\"corporate_name\":\"\",\"cost_to_us\":\"\",\"customer_email\":\"amit@mailinator.com\",\"customer_name\":\"Test\",\"customer_phone\":\"919900123456\",\"driver_name\":\"TaxiApp_Driver_Test\",\"driver_number\":\"9900489498\",\"drop_location\":\"\",\"itinerary\":\"Bangalore \\u0026rarr; Adilabad\",\"local_office_comments\":\"\",\"locality\":\"\",\"vendor_number\":\"080-65659999,,26577886,09845831627,09845031627\",\"op_allocation\":\"\",\"payment_method\":\"OFFLN\",\"pick_city\":\"Bangalore, Karnataka\",\"pick_loc\":\"Srinagar Main Rd, Ponnaiah Raja Puram, Shanmuga Nagar, Selvapuram North, India\",\"pickup_station\":\"Others\",\"profit_and_loss\":\"\",\"profit_loss_percent\":\"0\",\"region\":\"\",\"start_reg\":\"\",\"start_time\":\"14:45:00\",\"total_amt\":\"19150\",\"total_tax\":\"1050\",\"transfer_direction\":\"NA\",\"trip_end_date\":\"2016-01-05\",\"trip_start_date\":\"2016-01-05\",\"trip_type\":\"Outstation\",\"usertype\":\"individual\",\"vendor_name\":\"Bangalore - GovindRaju\",\"num_days\":3}", Booking.class);
//            bookingSession.setBooking(booking);
//            bookingSession.setDriverState(DriverState.IDLE);
//            GlobalData.get().setBookingSession(getActivity(), bookingSession);
//        }
        return bookingSession;
    }


    private class GetBookingTask extends ProgressAsyncTask {
        private Booking currentBooking;

        public GetBookingTask(Context ctx) {
            super(ctx);
        }

        @Override
        public void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case FAILED:
                    Utils.showPrompt(getActivity(), "Error", errorMessage);
                    break;
                case NO_RESULT:
                    Utils.showPrompt(getActivity(), "No Result", "No trips found. Please check later.");
                    break;
                case SUCCESS:
                    onClickingStartTrip(currentBooking);
            }
        }

        @Override
        protected Integer doInBackground(String... strings) {
            try {
//                if (!Utils.hasNetwork(ctx)) {
//                    return NO_NETWORK;
//                }
//
//                String id = "";
//                for (HomeItem item : response) {
//
//                    if (item.getContext() == HomeItem.START_A_TRIP) {
//                        id = item.getIds();
//                    }
//                }
//
//                if (StringUtils.isBlank(id)) {
//                    return NO_RESULT;
//                }

                List<Booking> bookings = new ArrayList<>();//APIUtils.getBookings(AppConstants.getToken(ctx), id);
                bookings.add(WebSession.gson.fromJson(SampleJson.BOOKING, Booking.class));

                if (bookings.isEmpty()) {
                    return NO_RESULT;
                }

                currentBooking = bookings.get(0);
                return SUCCESS;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return FAILED;
        }
    }


    private class NewTripsTask extends ProgressAsyncTask {
        List<Booking> bookings;

        public NewTripsTask(Context ctx) {
            super(ctx);
        }

        @Override
        protected Integer doInBackground(String... strings) {
            try {
                if (!Utils.hasNetwork(ctx)) {
                    return NO_NETWORK;
                }

                String id = "";
                for (HomeItem item : response) {
                    if (item.getContext() == HomeItem.NEW_TRIP) {
                        id = item.getIds();
                    }
                }

                if (StringUtils.isBlank(id)) {
                    return NO_RESULT;
                }

                //bookings = APIUtils.getBookings(AppConstants.getToken(ctx), id);
                bookings = new ArrayList<>();
                bookings.add(WebSession.gson.fromJson(SampleJson.BOOKING, Booking.class));
                if (bookings.isEmpty()) {
                    return NO_RESULT;
                }

                return SUCCESS;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return FAILED;
        }

        @Override
        public void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case FAILED:
                    Utils.showPrompt(getActivity(), "Error", errorMessage);
                    break;
                case NO_RESULT:
                    Utils.showPrompt(getActivity(), "No Result", "No trips found. Please check later.");
                    break;
                case SUCCESS:
                    showNewTripScreen(bookings);
            }
        }

    }

    private void showNewTripScreen(List<Booking> bookings) {

        GlobalData.get().setBookingList(bookings);
        Intent intent = new Intent(getActivity(), NewTripsActivity.class);
        getActivity().startActivity(intent);

    }
}
