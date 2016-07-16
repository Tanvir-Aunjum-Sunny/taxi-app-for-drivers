package com.taxiapp.model.business;

import com.google.gson.annotations.SerializedName;

public class LocalDuration {
	@SerializedName("id")
	String tripCode;
	@SerializedName("name")
	String tripValue;
	public String getTripCode() {
		return tripCode;
	}
	public void setTripCode(String tripCode) {
		this.tripCode = tripCode;
	}
	public String getTripValue() {
		return tripValue;
	}
	public void setTripValue(String tripValue) {
		this.tripValue = tripValue;
	}

}
