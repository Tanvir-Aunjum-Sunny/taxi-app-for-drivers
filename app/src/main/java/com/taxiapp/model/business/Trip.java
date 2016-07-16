package com.taxiapp.model.business;

import java.util.Date;

public abstract class Trip {

	// For outstation it can be multiple, otherwise only one input city!
	// private List<City> cities = new ArrayList<City>();
	private City city;
	private User user = new User();
	private Address pickupAddress;
	private Address dropAddress;
	private Date dateTime;
	private String StartDate;
	// private String StartDate;
	private String coupon = "";
	private String EndDate;

	private String tripId;
	private Cab selectedCab;
	private boolean payNow = false;
	private boolean paySuccessful = false;
	private Booking booking;
	private TripType tripType;
	private String local_type;
	private String transfer_type_id;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getCoupon() {
		return coupon;
	}

	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}

	// public List<City> getCities() {
	// return cities;
	// }
	//
	// public void setCities(List<City> cities) {
	// this.cities = cities;
	// }

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getStartDate() {
		return StartDate;
	}

	public void setStartDate(String string) {
		this.StartDate = string;
	}

	public String getEndDate() {
		return EndDate;
	}

	public void setEndDate(String dateTime) {
		this.EndDate = dateTime;
	}

	public String getTripId() {
		return tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}

	public TripType getTripType() {
		return tripType;
	}

	public void setTripType(TripType tripType) {
		this.tripType = tripType;
	}

	public Cab getSelectedCab() {
		return selectedCab;
	}

	public void setSelectedCab(Cab selectedCab) {
		this.selectedCab = selectedCab;
	}

	public Address getPickupAddress() {
		return pickupAddress;
	}

	public void setPickupAddress(Address pickupAddress) {
		this.pickupAddress = pickupAddress;
	}

	public Address getDropAddress() {
		return dropAddress;
	}

	public void setDropAddress(Address dropAddress) {
		this.dropAddress = dropAddress;
	}

	public boolean isPayNow() {
		return payNow;
	}

	public void setPayNow(boolean payNow) {
		this.payNow = payNow;
	}

	public Booking getBooking() {
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}

	public boolean isPaySuccessful() {
		return paySuccessful;
	}

	public void setPaySuccessful(boolean paySuccessful) {
		this.paySuccessful = paySuccessful;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public String getLocal_type() {
		return local_type;
	}

	public void setLocal_type(String localtype) {
		this.local_type = localtype;
	}

	public String getTransferType() {
		return transfer_type_id;
	}

	public void setTransferType(String transfer_type_id) {
		this.transfer_type_id = transfer_type_id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((booking == null) ? 0 : booking.hashCode());
		result = prime * result
				+ ((dateTime == null) ? 0 : dateTime.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Trip other = (Trip) obj;
		if (booking == null) {
			if (other.booking != null)
				return false;
		} else if (!booking.equals(other.booking))
			return false;
		return true;
	}
}
