package com.taxiapp.model.business;

import com.google.gson.annotations.SerializedName;

public class CityLag {

	@SerializedName("city_id")
	private String cityID;
	@SerializedName("trip_id")
	private String tripType;
	@SerializedName("time_lag")
	private String timeLag;
	@SerializedName("city_name")
	private String cityName;

	public String getCityID() {
		return cityID;
	}

	public void setCityId(String cityID) {
		this.cityID = cityID;
		System.out.println("City id in city lag" + cityID);
	}

	public String getTripType() {
		return tripType;
	}

	public void setTripType(String tripType) {
		this.tripType = tripType;
	}

	public String getTimeLag() {
		return timeLag;
	}

	public void setTimeLag(String timeLag) {
		this.timeLag = timeLag;
	}

	public String getcityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

}
