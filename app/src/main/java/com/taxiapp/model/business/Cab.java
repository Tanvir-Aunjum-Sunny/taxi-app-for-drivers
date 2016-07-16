package com.taxiapp.model.business;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class Cab {

	@SerializedName("soldout_flag")
	private boolean soldout;

	@SerializedName("car_id")
	private int carId;

	@SerializedName("car_type")
	private String carType;
	private String description;
	@SerializedName("seating_capacity")
	private int seatingCapacity;
	@SerializedName("laguage_capacity")
	private int luggageCapacity;
	@SerializedName("car_image")
	private String imagePath;
	// cost will vary depending on the city and also depending on the type of
	// booking.
	private double amount;
	@SerializedName("booking_cost")
	private double bookingCost;
	
	@SerializedName("service_tax")
	private double serviceTax;
	@SerializedName("discount")
	private double discount;

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	@SerializedName("car_category")
	private String category;

    @SerializedName("extra_usage_km_rate")
	private String extra_usage_km;

    @SerializedName("extra_usage_hour_rate")
	private String extra_usage_hour;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getSeatingCapacity() {
		return seatingCapacity;
	}

	public void setSeatingCapacity(int seatingCapacity) {
		this.seatingCapacity = seatingCapacity;
	}

	public int getLuggageCapacity() {
		return luggageCapacity;
	}

	public void setLuggageCapacity(int luggageCapacity) {
		this.luggageCapacity = luggageCapacity;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public int getCarId() {
		return carId;
	}

	public void setCarId(int carId) {
		this.carId = carId;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	// For Local
	public String getExtraKmRate() {
		return extra_usage_km;
	}

	public void setExtraKmRate(String km_rate) {
		this.extra_usage_km = km_rate;
	}

	public String getExtraHourRate() {
		return extra_usage_hour;
	}

	public void setExtraHourRate(String hr_rate) {
		this.extra_usage_hour = hr_rate;
	}

	/**
	 * adding soldout feature
	 */

	public boolean getSoldoutStatus() {
		return soldout;
	}

	public void setSoldoutStatus(boolean soldout) {
		this.soldout = soldout;
		System.out.println("Value set");
	}
	public double getBookingCost() {
		return bookingCost;
	}

	public void setBookingCost(double bookingCost) {
		this.bookingCost = bookingCost;
	}

	public double getServiceTax() {
		return serviceTax;
	}

	public void setServiceTax(double serviceTax) {
		this.serviceTax = serviceTax;
	}

    /**
     * Provides a clone of the cab passed in
     * 
     * @param cab
     *            The original object. Should not be null.
     * @return A clone of the cab object passed in. Will return null if cab ==
     *         null.
     */
    public static Cab clone(Cab cab) {
        if (cab != null) {
            Gson gson = new Gson();
            String json = gson.toJson(cab);
            Cab clone = gson.fromJson(json, Cab.class);
            return clone;
        }
        return null;
    }

}
