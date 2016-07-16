package com.taxiapp.model.business;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.taxiapp.db.BookingDao;

import java.io.Serializable;

@DatabaseTable(daoClass = BookingDao.class)
public class Booking implements Serializable {

    @DatabaseField(id = true)
    private String booking_id;

    @DatabaseField
    private String region;

    @DatabaseField
    private String cashtocollect;

    @DatabaseField
    private String drop_location;

    @DatabaseField
    private String start_reg;

    @DatabaseField
    private int num_days;


    @DatabaseField
    private String driver_name;

    @DatabaseField
    private String booking_date;

    @DatabaseField
    private String customer_phone;

    @DatabaseField
    private String corporate_name;

    @DatabaseField
    private String booking_time;

    @DatabaseField
    private String car_type;

    @DatabaseField
    private String cost_to_us;

    @DatabaseField
    private String profit_and_loss;

    @DatabaseField
    private String bill_number;

    @DatabaseField
    private String pick_city;

    @DatabaseField
    private String transfer_direction;

    @DatabaseField
    private String total_amt;

    @DatabaseField
    private String itinerary;

    @DatabaseField
    private String total_tax;

    @DatabaseField
    private String local_office_comments;

    @DatabaseField
    private String vendor_name;

    @DatabaseField
    private String trip_end_date;

    @DatabaseField
    private String driver_number;

    @DatabaseField
    private String vendor_number;

    @DatabaseField
    private String booking_status;

    @DatabaseField
    private String customer_email;

    @DatabaseField
    private String customer_name;

    @DatabaseField
    private String usertype;

    @DatabaseField
    private String trip_start_date;

    @DatabaseField
    private String pick_loc;

    @DatabaseField
    private String trip_type;

    @DatabaseField
    private String pickup_station;

    @DatabaseField
    private String allocation_type;

    @DatabaseField
    private String profit_loss_percent;

    @DatabaseField
    private String op_allocation;

    @DatabaseField
    private String start_time;

    @DatabaseField
    private String locality;

    @DatabaseField
    private String cab_class;

    @DatabaseField
    private String payment_method;

    public Booking() {
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCashtocollect() {
        return cashtocollect;
    }

    public void setCashtocollect(String cashtocollect) {
        this.cashtocollect = cashtocollect;
    }

    public String getDrop_location() {
        return drop_location;
    }

    public void setDrop_location(String drop_location) {
        this.drop_location = drop_location;
    }

    public String getStart_reg() {
        return start_reg;
    }

    public void setStart_reg(String start_reg) {
        this.start_reg = start_reg;
    }

    public int getNum_days() {
        return num_days;
    }

    public void setNum_days(int num_days) {
        this.num_days = num_days;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public String getCorporate_name() {
        return corporate_name;
    }

    public void setCorporate_name(String corporate_name) {
        this.corporate_name = corporate_name;
    }

    public String getBooking_time() {
        return booking_time;
    }

    public void setBooking_time(String booking_time) {
        this.booking_time = booking_time;
    }

    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }

    public String getCost_to_us() {
        return cost_to_us;
    }

    public void setCost_to_us(String cost_to_us) {
        this.cost_to_us = cost_to_us;
    }

    public String getProfit_and_loss() {
        return profit_and_loss;
    }

    public void setProfit_and_loss(String profit_and_loss) {
        this.profit_and_loss = profit_and_loss;
    }

    public String getBill_number() {
        return bill_number;
    }

    public void setBill_number(String bill_number) {
        this.bill_number = bill_number;
    }

    public String getPick_city() {
        return pick_city;
    }

    public void setPick_city(String pick_city) {
        this.pick_city = pick_city;
    }

    public String getTransfer_direction() {
        return transfer_direction;
    }

    public void setTransfer_direction(String transfer_direction) {
        this.transfer_direction = transfer_direction;
    }

    public String getTotal_amt() {
        return total_amt;
    }

    public void setTotal_amt(String total_amt) {
        this.total_amt = total_amt;
    }

    public String getItinerary() {
        return itinerary;
    }

    public void setItinerary(String itinerary) {
        this.itinerary = itinerary;
    }

    public String getTotal_tax() {
        return total_tax;
    }

    public void setTotal_tax(String total_tax) {
        this.total_tax = total_tax;
    }

    public String getLocal_office_comments() {
        return local_office_comments;
    }

    public void setLocal_office_comments(String local_office_comments) {
        this.local_office_comments = local_office_comments;
    }

    public String getVendor_name() {
        return vendor_name;
    }

    public void setVendor_name(String vendor_name) {
        this.vendor_name = vendor_name;
    }

    public String getTrip_end_date() {
        return trip_end_date;
    }

    public void setTrip_end_date(String trip_end_date) {
        this.trip_end_date = trip_end_date;
    }

    public String getDriver_number() {
        return driver_number;
    }

    public void setDriver_number(String driver_number) {
        this.driver_number = driver_number;
    }

    public String getVendor_number() {
        return vendor_number;
    }

    public void setVendor_number(String vendor_number) {
        this.vendor_number = vendor_number;
    }

    public String getBooking_status() {
        return booking_status;
    }

    public void setBooking_status(String booking_status) {
        this.booking_status = booking_status;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getTrip_start_date() {
        return trip_start_date;
    }

    public void setTrip_start_date(String trip_start_date) {
        this.trip_start_date = trip_start_date;
    }

    public String getPick_loc() {
        return pick_loc;
    }

    public void setPick_loc(String pick_loc) {
        this.pick_loc = pick_loc;
    }

    public String getTrip_type() {
        return trip_type;
    }

    public void setTrip_type(String trip_type) {
        this.trip_type = trip_type;
    }

    public String getPickup_station() {
        return pickup_station;
    }

    public void setPickup_station(String pickup_station) {
        this.pickup_station = pickup_station;
    }

    public String getAllocation_type() {
        return allocation_type;
    }

    public void setAllocation_type(String allocation_type) {
        this.allocation_type = allocation_type;
    }

    public String getProfit_loss_percent() {
        return profit_loss_percent;
    }

    public void setProfit_loss_percent(String profit_loss_percent) {
        this.profit_loss_percent = profit_loss_percent;
    }

    public String getOp_allocation() {
        return op_allocation;
    }

    public void setOp_allocation(String op_allocation) {
        this.op_allocation = op_allocation;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCab_class() {
        return cab_class;
    }

    public void setCab_class(String cab_class) {
        this.cab_class = cab_class;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    @Override
    public String toString() {
        return "ClassPojo [region = " + region + ", cashtocollect = " + cashtocollect + ", drop_location = " + drop_location + ", start_reg = " + start_reg + ", num_days = " + num_days + ", booking_id = " + booking_id + ", driver_name = " + driver_name + ", booking_date = " + booking_date + ", customer_phone = " + customer_phone + ", corporate_name = " + corporate_name + ", booking_time = " + booking_time + ", car_type = " + car_type + ", cost_to_us = " + cost_to_us + ", profit_and_loss = " + profit_and_loss + ", bill_number = " + bill_number + ", pick_city = " + pick_city + ", transfer_direction = " + transfer_direction + ", total_amt = " + total_amt + ", itinerary = " + itinerary + ", total_tax = " + total_tax + ", local_office_comments = " + local_office_comments + ", vendor_name = " + vendor_name + ", trip_end_date = " + trip_end_date + ", driver_number = " + driver_number + ", vendor_number = " + vendor_number + ", booking_status = " + booking_status + ", customer_email = " + customer_email + ", customer_name = " + customer_name + ", usertype = " + usertype + ", trip_start_date = " + trip_start_date + ", pick_loc = " + pick_loc + ", trip_type = " + trip_type + ", pickup_station = " + pickup_station + ", allocation_type = " + allocation_type + ", profit_loss_percent = " + profit_loss_percent + ", op_allocation = " + op_allocation + ", start_time = " + start_time + ", locality = " + locality + ", cab_class = " + cab_class + ", payment_method = " + payment_method + "]";
    }

}