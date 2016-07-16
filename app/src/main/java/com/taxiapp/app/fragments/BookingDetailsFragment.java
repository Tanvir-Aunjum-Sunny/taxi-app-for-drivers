package com.taxiapp.app.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taxiapp.model.business.Booking;
import com.taxiapp.vendor.app.R;

/**
 * Created by Amit S on 11/01/16.
 */
public class BookingDetailsFragment extends Fragment {

    private View rootView;
    private Booking booking;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.booking_details, null);
        populateUiWithBookingDetails();

        return rootView;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public static Fragment newInstance(Booking booking) {
        BookingDetailsFragment fragment = new BookingDetailsFragment();
        fragment.setBooking(booking);
        return fragment;
    }

    public void populateUiWithBookingDetails() {
        Booking booking = getBooking();
        if (booking == null) {
            return;
        }

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

    private View findViewById(int id) {
        return rootView.findViewById(id);
    }


}
