package com.taxiapp.model.business;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * { "location": [ { "lat": "77.6408", "lng": "12.947" }, { "lat": "77.6414",
 * "lng": "12.9573" }, { "lat": "77.6394", "lng": "12.9645" }, { "lat":
 * "77.6374", "lng": "12.9664" }, { "lat": "77.6393", "lng": "12.9647" }, {
 * "lat": "77.6395", "lng": "12.9653" } ], "driver_name": "Test1234",
 * "driver_num": "8105810543", "car_number": "test1234", "car_brand": "Indica",
 * "pick_loc":
 * "Sony World, 100 Feet Road, Koramangala 4 Block, Koramangala Layout, Bengaluru, Karnataka 560095, India"
 * , "driver_gps_locations":
 * "100 Feet Road, Challaghatta, Bengaluru, Karnataka 560071, India",
 * "distance": "2.7 km", "duration": "5 mins", "pickup_reached_time":
 * "16:01:30", "status_code": 101, "status_description": "SUCCESS" }
 * 
 * @author macair
 * 
 */
public class Tracking {
	public static class Location {
		@SerializedName("lt")
		public String lat;
		@SerializedName("lg")
		public String lng;
	}

	@SerializedName("l")
	private List<Location> location = new ArrayList<Tracking.Location>();
	@SerializedName("d_name")
	private String driver_name;
	@SerializedName("d_num")
	private String driver_num;
	@SerializedName("c_num")
	private String car_number;
	@SerializedName("c_brand")
	private String car_brand;
	@SerializedName("p_loc")
	private String pick_loc;
	@SerializedName("d_gps_l")
	private String driver_gps_locations;
	@SerializedName("dt")
	private String distance;
	@SerializedName("du")
	private String duration;
	@SerializedName("p_lt")
	private String pickup_lat;
	@SerializedName("p_lg")
	private String pickup_lng;

	@SerializedName("dr_i")
	private String driverimagePath;

	@SerializedName("te_s")
	private boolean tripEndStatus;
	@SerializedName("cb_s")
	private boolean customer_boarded_status;
	@SerializedName("pr")
	private String pickup_reached_time;
	@SerializedName("cb_t")
	private String customer_boarded_time;
	private int status_code;
	private String status_description;

	public List<Location> getLocation() {
		return location;
	}

	public void setLocation(List<Location> location) {
		this.location = location;
	}

	public String getImagePath() {
		return driverimagePath;
	}

	public void setImagePath(String imagePath) {
		this.driverimagePath = imagePath;
	}

	public String getDriver_name() {
		return driver_name;
	}

	public void setDriver_name(String driver_name) {
		this.driver_name = driver_name;
	}

	public String getDriver_num() {
		return driver_num;
	}

	public void setDriver_num(String driver_num) {
		this.driver_num = driver_num;
	}

	public String getCar_number() {
		return car_number;
	}

	public void setCar_number(String car_number) {
		this.car_number = car_number;
	}

	public String getCar_brand() {
		return car_brand;
	}

	public void setCar_brand(String car_brand) {
		this.car_brand = car_brand;
	}

	public String getPick_loc() {
		return pick_loc;
	}

	public void setPick_loc(String pick_loc) {
		this.pick_loc = pick_loc;
	}

	public String getDriver_gps_locations() {
		return driver_gps_locations;
	}

	public void setDriver_gps_locations(String driver_gps_locations) {
		this.driver_gps_locations = driver_gps_locations;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getPickup_reached_time() {
		return pickup_reached_time;
	}

	public void setPickup_reached_time(String pickup_reached_time) {
		this.pickup_reached_time = pickup_reached_time;
	}

	/**
	 * changes for track my car v2
	 * 
	 */
	public String getCustomer_boarded_time() {
		return customer_boarded_time;
	}

	public void setCustomer_boarded_time(String customer_boarded_time) {
		this.customer_boarded_time = customer_boarded_time;
	}

	public int getStatus_code() {
		return status_code;
	}

	public void setStatus_code(int status_code) {
		this.status_code = status_code;
	}

	public String getStatus_description() {
		return status_description;
	}

	public void setStatus_description(String status_description) {
		this.status_description = status_description;
	}

	public void setTripEndStatus(boolean status) {
		this.tripEndStatus = status;
	}

	public boolean getTripEndStatus() {
		return tripEndStatus;

	}

	public void setCustBoardStatus(boolean status) {
		this.customer_boarded_status = status;
	}

	public boolean getCustBoardStatusStatus() {
		return customer_boarded_status;

	}

	public String getCustLat() {
		// TODO Auto-generated method stub
		return pickup_lat;
	}

	public void setCustLat(String custLat) {
		this.pickup_lat = custLat;
	}

	public String getCustLang() {
		// TODO Auto-generated method stub
		return pickup_lng;
	}

	public void setCustLang(String custLang) {
		this.pickup_lng = custLang;
	}

}
