package com.taxiapp.app.common;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.taxiapp.model.business.Booking;
import com.taxiapp.model.business.TripType;
import com.taxiapp.utils.AppConstants;
import com.taxiapp.vendor.app.R;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * Created by Amit S on 18/11/15.
 */
public class BookingsListAdapter extends BaseAdapter {

    private Context context;
    private List<Booking> bookings;

    public BookingsListAdapter(Context context, List<Booking> bookingList) {

        this.context = context;
        this.bookings = bookingList;
    }

    @Override
    public int getCount() {
        return bookings.size();
    }

    @Override
    public Object getItem(int i) {
        return bookings.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Booking booking = (Booking) getItem(i);
        if (view == null) {
            view = View.inflate(context, R.layout.item_mybooking, null);
        }

        TextView bookingIdView = (TextView) view.findViewById(R.id.mybookings_bookingId);
        TextView dView = (TextView) view.findViewById(R.id.mybookings_date);
        TextView mView = (TextView) view.findViewById(R.id.mybookings_month);
        TextView yView = (TextView) view.findViewById(R.id.mybookings_year);
        TextView cityView = (TextView) view.findViewById(R.id.mybookings_city);


//ID
        bookingIdView.setText(booking.getBooking_id());

        //Journey Start Date
        LocalDate date = LocalDate.parse(booking.getTrip_start_date(), AppConstants.DATE_FORMAT);
        int day = date.getDayOfMonth();
        dView.setText((day < 10 ? "0" : "") + day);
        mView.setText(AppConstants.months[date.getMonthOfYear()]);
        yView.setText("" + date.getYearOfCentury());

        cityView.setText(booking.getPick_city());


        TripType tripType = TripType.getTripType(booking.getTrip_type());
        if (tripType == TripType.AIRPORT) {
            //
        } else if (tripType == TripType.LOCAL) {
            //
        } else if (tripType == TripType.OUTSTATION) {
            fillOutstationTripDetails(booking, view);
        }


        view.setTag(booking);
        return view;
    }

    private void fillOutstationTripDetails(Booking booking, View view) {
        TextView destView = (TextView) view.findViewById(R.id.mybookings_destinations);
        destView.setText(booking.getItinerary());

        TextView addrView = (TextView) view.findViewById(R.id.mybookings_address);
        addrView.setText(booking.getPick_loc());

    }
}
