package com.taxiapp.app.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.taxiapp.app.widget.SwipeLayout;
import com.taxiapp.db.DriverLocationDAO;
import com.taxiapp.db.TransactionDBManager;
import com.taxiapp.gps.DriverTrackingService;
import com.taxiapp.model.BookingSession;
import com.taxiapp.model.DriverLocation;
import com.taxiapp.model.business.Booking;
import com.taxiapp.model.business.DriverState;
import com.taxiapp.net.NetResponse;
import com.taxiapp.net.api.APIUtils;
import com.taxiapp.utils.AppConstants;
import com.taxiapp.utils.AppLogger;
import com.taxiapp.utils.GlobalData;
import com.taxiapp.utils.PreferencesUtil;
import com.taxiapp.utils.ProgressAsyncTask;
import com.taxiapp.utils.ThreadUtils;
import com.taxiapp.utils.Utils;
import com.taxiapp.vendor.app.R;

import de.greenrobot.event.EventBus;


/**
 * Created by sarath on 27/11/15.
 */
public class StartTripActivity extends BaseActivity implements OnClickListener {


    private Handler handler;
    private Gson gson = new Gson();

    private BookingSession currentBookingSession;

    private DistanceUpdater distanceUpdater = new DistanceUpdater();
    private GPSFinder gpsFinder = new GPSFinder();

    private TextView tripControlButton;
    private Button callButton;

    private TextView tripDistanceValue;
    //private AnimationDrawable arrowAnimation;
    private TranslateAnimation arrowAnimation;
    private ImageView arrowView;

    private DriverLocationDAO driverLocationDAO;
    private DriverLocation currentLocation;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_start_trip);
        initialize();
    }

    @Override
    public void onResume() {
        super.onResume();
        currentBookingSession = getBookingStateFromValidSource();

        if (currentBookingSession != null) {
            handler.removeCallbacks(distanceUpdater);
            handler.postDelayed(distanceUpdater, AppConstants.DISTANCE_UPDATE_IN_INTERVAL);
        }

        handler.removeCallbacks(gpsFinder);
        handler.post(gpsFinder);


        if (arrowView != null) {
            //arrowAnimation = (AnimationDrawable) arrowView.getDrawable();
            arrowAnimation = new TranslateAnimation(0.0f, 60.0f,
                    0.0f, 0.0f);
            arrowAnimation.setDuration(800);
            arrowAnimation.setRepeatCount(-1);
            arrowAnimation.setRepeatMode(1);
            arrowView.startAnimation(arrowAnimation);  // start animation

        }


    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    //Event Bus - Called to update the UI.
    public void onEventMainThread(DriverLocation driverLocation) {
        if (currentBookingSession.getDriverState() != DriverState.IDLE) {
            tripDistanceValue.setText(String.format("%.2f", driverLocation.getTravelled() / 1000.0));
        }
        currentBookingSession.setLastCheckPoint(driverLocation);

    }

    public void onPause() {
        super.onPause();
        try {
            handler.removeCallbacks(distanceUpdater);
            handler.removeCallbacks(gpsFinder);
            //arrowAnimation.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void initialize() {
        try {
            handler = new Handler();

            driverLocationDAO = TransactionDBManager.getInstance().getDriverLocationDAO();

            currentBookingSession = getBookingStateFromValidSource();

            populateUiWithBookingDetails();

            tripControlButton = (TextView) findViewById(R.id.tripControlBtn);

            callButton = (Button) findViewById(R.id.tab_call);
            callButton.setOnClickListener(this);

            findViewById(R.id.tab_clean_car).setOnClickListener(this);
            findViewById(R.id.tab_complementary).setOnClickListener(this);
            findViewById(R.id.tab_ontime).setOnClickListener(this);
            findViewById(R.id.tab_transparent_billing).setOnClickListener(this);
            findViewById(R.id.destination_address).setOnClickListener(this);

            arrowView = (ImageView) findViewById(R.id.arrowView);
            //arrowView.setAnimation(inFromLeftAnimation());


            applyButtonStyle(currentBookingSession.getDriverState());
            if (currentBookingSession.getPreviousState() == DriverState.END_DAY_TRIP && currentBookingSession.getDriverState() == DriverState.START_DAY_TRIP) {
                notifyDriverTrackService(DriverState.PAUSE_TRIP.name());
            } else {
                notifyDriverTrackService(DriverState.RESUME_TRIP.name());
            }

            //applyButtonStyle(currentBookingSession.getPreviousState());
//            notifyDriverTrackService(currentBookingSession.getPreviousState().name());


            final SwipeLayout swipeLayout = (SwipeLayout) findViewById(R.id.swipeLayout);
            swipeLayout.setSwipeCompletionPercentage(65); //default is 60 %
            swipeLayout.setOnSwipeCompleteListener(new SwipeLayout.OnSwipeCompleteListener() {
                @Override
                public void execute() {
                    swipeLayout.restoreToDefault(); //removing this will make its childs DISAPPEAR #EvilLaughter ;)
                    onClickingTripControlButton();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Animation inFromLeftAnimation() {
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromLeft.setDuration(500);
        inFromLeft.setRepeatMode(-1);
        inFromLeft.setInterpolator(new AccelerateInterpolator());
        return inFromLeft;
    }

    public void populateUiWithBookingDetails() {
        Booking booking = currentBookingSession.getBooking();
        if (booking == null) {
            return;
        }

        tripDistanceValue = (TextView) findViewById(R.id.trip_distance_value);

        ((TextView) findViewById(R.id.booking_id)).setText("" + booking.getBooking_id());
        ((TextView) findViewById(R.id.booking_tripType)).setText("" + booking.getTrip_type());
        ((TextView) findViewById(R.id.booking_carType)).setText("" + booking.getCar_type());
        ((TextView) findViewById(R.id.booking_numDays)).setText("" + booking.getNum_days());
        ((TextView) findViewById(R.id.booking_paymentType)).setText("" + booking.getPayment_method());
        ((TextView) findViewById(R.id.booking_route)).setText("" + Html.fromHtml(booking.getItinerary()));
        ((TextView) findViewById(R.id.booking_startTime)).setText("" + booking.getStart_time());
        ((TextView) findViewById(R.id.booking_pickupAddress)).setText("" + booking.getPick_loc());
        ((TextView) findViewById(R.id.booking_customerName)).setText("" + booking.getCustomer_name());
        ((TextView) findViewById(R.id.booking_customerMobile)).setText("" + booking.getCustomer_phone());

    }


    ///********** Button Style section

    /**
     * Update Button styles for next state!
     *
     * @param
     */
    private void applyButtonStyle(DriverState driverState) {

        switch (driverState) {

            case IDLE:
            case START_TRIP:
                applyStartTripBtnStyles();
                break;
            case END_DAY_TRIP:
                applyEndDayTripStyles();
                break;
            case REACHED_PICKUP:
                applyReachedPickupBtnStyles();
                break;
            case CUSTOMER_BOARDED:
                applyCustomerBoardedBtnStyles();
                break;
            case START_DAY_TRIP:
                applyStartDayTripBtnStyles();
                break;
            case END_TRIP:
                applyEndTripBtnStyles();
                break;
        }
    }

    private void applyStartDayTripBtnStyles() {
        tripControlButton.setText("Start Trip Day - " + currentBookingSession.getDay());
    }

    private void applyEndDayTripStyles() {
        tripControlButton.setText("End Trip Day - " + currentBookingSession.getDay());
    }


    private void goToCustomerBillScreen(NetResponse response) {

        GlobalData.get().setBookingSession(this, currentBookingSession);
        handler.removeCallbacks(distanceUpdater);
        PreferencesUtil.remove(this, AppConstants.BOOKING_STATE_KEY);
        //Only for testing...
//        try {
//            DriverLocationDAO driverLocationDAO = TransactionDBManager.getInstance().getDriverLocationDAO();
//            DeleteBuilder<DriverLocation, Integer> builder = driverLocationDAO.deleteBuilder();
//            builder.where().eq("bookingId", currentBookingSession.getBooking().getBooking_id()).and().eq("syncStatus", SyncStatus.SUCCESS);
//            builder.delete();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        //Start billing activity from Response;
        Intent intent = new Intent(this, CustomerBillActivity.class);
        if (response != null) {
            intent.putExtra("bill", gson.toJson(response));
        }
        startActivity(intent);

        finish();
    }

    private void applyEndTripBtnStyles() {
        tripControlButton.setText("End Trip");

        if (currentBookingSession.getBooking().getNum_days() > 1) {
            tripControlButton.setText("End Trip Day - " + currentBookingSession.getDay());
        }
    }

    private void applyCustomerBoardedBtnStyles() {
        tripControlButton.setText("Customer Boarded");
    }

    private void notifyDriverTrackService(String action) {

        Intent driverTrackingServiceIntent = new Intent(this, DriverTrackingService.class);
        driverTrackingServiceIntent.putExtra(DriverTrackingService.BOOKING_OBJ_KEY, currentBookingSession.getBooking());
        driverTrackingServiceIntent.setAction(action);
        AppLogger.get().d(this.getClass(), "Service is going to Start with " + action);
        startService(driverTrackingServiceIntent);
    }

    private void applyReachedPickupBtnStyles() {
        tripControlButton.setText("Reached Pickup");
    }

    private void applyStartTripBtnStyles() {
        tripControlButton.setText("Start Trip");
    }

    ////*****************


    //******* Trip Control Tracking section

    public void onClickingTripControlButton() {

        AppLogger.get().d(this.getClass(), "Driver State : " + currentBookingSession.getDriverState());
        new SendTripStateAsyncTask(this).execute();

    }


    private class SendTripStateAsyncTask extends ProgressAsyncTask {
        private NetResponse response;
        private DriverState currentState;
        private DriverState nextState;

        public SendTripStateAsyncTask(Context ctx) {
            super(ctx);
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        public void onPostExecute(Integer result) {
            super.onPostExecute(result);

            if (result == FAILED) {
                Utils.showPrompt(ctx, "Error", errorMessage);
                return;
            }

            //Update Next state & day of current session.
            currentBookingSession.setPreviousState(currentState);
            currentBookingSession.setDriverState(nextState);
            //This is to check if trip is over or not.
            if (currentState == DriverState.END_DAY_TRIP) {
                currentBookingSession.setDay(currentBookingSession.getDay() + 1);
            }
            GlobalData.get().setBookingSession(ctx, currentBookingSession);
            applyButtonStyle(nextState);
            if (nextState == DriverState.TRIP_OVER) {
                new BillGenerationAsyncTask(StartTripActivity.this).execute();
                notifyDriverTrackService(DriverState.END_TRIP.name());
                return;
            }


            if (currentState == DriverState.END_DAY_TRIP) {
                showDayEndDialogBox();
            }


        }

        @Override
        protected Integer doInBackground(String... strings) {
            try {

                currentState = currentBookingSession.getDriverState();
                nextState = getNextBookingState(currentState);

                //Spoof!
                if (nextState == DriverState.IDLE || nextState == DriverState.START_TRIP) {
                    nextState = DriverState.REACHED_PICKUP;
                    currentState = DriverState.START_TRIP;
                }

                AppLogger.get().d(getClass(), "Statuses " + currentState + " - " + nextState);
                DriverLocation loc = currentBookingSession.getLastCheckPoint();
                if (loc == null) {
                    errorMessage = "Could not get current location. Please try again.";
                    return FAILED;
                }

                DriverState checkpoint = currentState;
                if (checkpoint == DriverState.IDLE) {
                    checkpoint = DriverState.START_TRIP;
                }

                loc.setCheckpoint(checkpoint.name());
                loc.setBookingId(currentBookingSession.getBookingId());
                loc.setDay(currentBookingSession.getDay());


                response = APIUtils.sendCriticalData(StartTripActivity.this, loc);


                if (checkpoint == DriverState.CUSTOMER_BOARDED) {
                    loc.setSpanTravelled(0);
                    loc.setTravelled(0);
                }
                notifyDriverTrackService(currentState.name());
                ThreadUtils.sleepInSec(2);
                Dao.CreateOrUpdateStatus status = driverLocationDAO.createOrUpdate(loc);
                if (status.isCreated() || status.isUpdated()) {
                    return SUCCESS;
                }
            } catch (Exception e) {
                AppLogger.get().e(getClass(), e);
                errorMessage = "Could not send data to server. Please try again. " + e.getMessage();
            }
            return FAILED;
        }
    }

    private void showDayEndDialogBox() {
        final View view = getLayoutInflater().inflate(R.layout.dialog_day_end, null);

        DriverLocation checkpoint = currentBookingSession.getLastCheckPoint();
        String kms = String.format("%.2f", checkpoint.getTravelled() / 1000.0);
        ((TextView) view.findViewById(R.id.day_kms)).setText(kms + " Km");

        Context ctx = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setCancelable(false);
        View customTitleView = getLayoutInflater().inflate(
                R.layout.dialog_title, null);
        ((TextView) customTitleView.findViewById(R.id.dialog_title))
                .setText("Day Summary");
        builder.setCustomTitle(customTitleView).setView(view)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TimePicker picker = (TimePicker) view.findViewById(R.id.pickup_time);
                        //Do something with the pickup time.
                        finish();
                    }
                });
        final Dialog dialog = builder.create();
        customTitleView.findViewById(R.id.dialog_close).setVisibility(View.INVISIBLE);
        dialog.show();

    }


    private class BillGenerationAsyncTask extends ProgressAsyncTask {
        private NetResponse response;

        public BillGenerationAsyncTask(Context ctx) {
            super(ctx);
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            tripControlButton.setText("");
        }

        @Override
        public void onPostExecute(Integer result) {
            super.onPostExecute(result);

            if (result == FAILED) {
                //if (currentBookingSession.getDriverState() == DriverState.END_TRIP) {
                Utils.showPrompt(ctx, "Error", errorMessage);
                //  return;
                //}
                return;
            }


            goToCustomerBillScreen(response);


        }

        @Override
        protected Integer doInBackground(String... strings) {
            try {
                publishProgress("Generating Bill...");
                DriverLocation loc = currentBookingSession.getLastCheckPoint();
                if (loc == null) {
                    errorMessage = "Could not get current location. Make sure you are under open SKY";
                    return FAILED;
                }


                loc.setCheckpoint(DriverState.TRIP_OVER.name());
                loc.setBookingId(currentBookingSession.getBookingId());
                loc.setDay(currentBookingSession.getDay());
                response = APIUtils.sendCriticalData(StartTripActivity.this, loc);
                Dao.CreateOrUpdateStatus status = driverLocationDAO.createOrUpdate(loc);
                if (status.isCreated() || status.isUpdated()) {
                    return SUCCESS;
                }
            } catch (Exception e) {
                AppLogger.get().e(getClass(), e);
                errorMessage = "Could not generate bill. Please try again. " + e.getMessage();
            }
            return FAILED;
        }
    }


    /*
    * Init Booking object from Preferences if it is not there then initializes from Intent or
    * it returns null Value
    *
    * */
    public BookingSession getBookingStateFromValidSource() {
        BookingSession bookingSession = GlobalData.get().getBookingSession(this);
        if (bookingSession == null || bookingSession.getBooking() == null) {
            Booking booking = (Booking) getIntent().getSerializableExtra("booking");
            bookingSession = new BookingSession();
            bookingSession.setBooking(booking);
            bookingSession.setDriverState(DriverState.START_TRIP);
            GlobalData.get().setBookingSession(this, bookingSession);
        }
        return bookingSession;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tab_call:
                displayCallPopupWindow(view);
                break;
            case R.id.tab_clean_car:
            case R.id.tab_complementary:
            case R.id.tab_ontime:
            case R.id.tab_transparent_billing:
            case R.id.destination_address:
                displayPopupWindow(view);
                break;
        }
    }


    public class DistanceUpdater implements Runnable {
        public void run() {
            populateDistanceInPeriod();
            handler.postDelayed(this, AppConstants.DISTANCE_UPDATE_IN_INTERVAL);
        }
    }

    public void populateDistanceInPeriod() {

        if (currentBookingSession != null && currentBookingSession.getLastCheckPoint() != null) {
            tripDistanceValue.setText(String.format("%.2f", currentBookingSession.getLastCheckPoint().getTravelled() / 1000.0));
        }
    }


    private class GPSFinder implements Runnable {
        public void run() {

//            if (!Utils.isGpsEnabled(StartTripActivity.this)) {
//                Utils.turnGPSOn(StartTripActivity.this);
//            }

            if (!Utils.isGpsEnabled(StartTripActivity.this)) {
                Utils.showGpsEnablePrompt(StartTripActivity.this, StartTripActivity.this, StartTripActivity.this);
                return;
            }

            handler.postDelayed(this, AppConstants.DISTANCE_UPDATE_IN_INTERVAL);

        }

    }


    private DriverState getNextBookingState(DriverState currentState) {
        switch (currentState) {
            case IDLE:
                return DriverState.START_TRIP;
            case START_TRIP:
                return DriverState.REACHED_PICKUP;
            case REACHED_PICKUP:
                return DriverState.CUSTOMER_BOARDED;
            case CUSTOMER_BOARDED:
                if (currentBookingSession.getBooking().getNum_days() > 1) {
                    return DriverState.END_DAY_TRIP;
                }
                return DriverState.END_TRIP;
            case END_DAY_TRIP:
                if (currentBookingSession.getDay() >= currentBookingSession.getBooking().getNum_days()) {
                    return DriverState.TRIP_OVER;
                }
                return DriverState.START_DAY_TRIP;
            case START_DAY_TRIP:
                if (currentBookingSession.getBooking().getNum_days() > 1) {
                    return DriverState.END_DAY_TRIP;
                }
                return DriverState.END_TRIP;
            case END_TRIP:
                return DriverState.TRIP_OVER;
        }
        return DriverState.START_TRIP;
    }





    private void displayCallPopupWindow(View anchorView) {
        PopupMenu popup = new PopupMenu(this, anchorView);
        // Inflate the menu from xml
        popup.getMenuInflater().inflate(R.menu.call_menu, popup.getMenu());
        // Setup menu item selection
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_call_customer:
                        Utils.callNumber(StartTripActivity.this, currentBookingSession.getBooking().getCustomer_phone());
                        return true;
                    case R.id.menu_call_taxiapp:
                        Utils.callCustomerCare(StartTripActivity.this);
                        return true;
                    case R.id.menu_call_vendor:
                        Utils.callNumber(StartTripActivity.this, currentBookingSession.getBooking().getVendor_number());
                        return true;
                    default:
                        return false;
                }
            }
        });
        // Handle dismissal with: popup.setOnDismissListener(...);
        // Show the menu
        popup.show();


    }


    private void displayPopupWindow(View anchorView) {
        PopupWindow popup = new PopupWindow(this);
        View layout = getLayoutInflater().inflate(R.layout.anchor_view_content, null);
        TextView textView = (TextView) layout.findViewById(R.id.tvCaption);
        textView.setText("Lorem Ipsum. This is a dummy text. Amit is yet to receive proper text from TaxiApp");
        popup.setContentView(layout);

        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setHeight(200);
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        popup.setBackgroundDrawable(getResources().getDrawable(R.drawable.tooltip));
        popup.showAsDropDown(anchorView, 20, 0);


    }

}