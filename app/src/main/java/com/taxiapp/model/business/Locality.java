package com.taxiapp.model.business;

public class Locality {
	private String value;
	private String subarea_id;
	private boolean isPopular;
	private boolean isAirport;

	@Override
	public String toString() {
		return value;
	}

	public String getSubarea_id() {
		return subarea_id;
	}

	public void setSubarea_id(String subarea_id) {
		this.subarea_id = subarea_id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isPopular() {
		return isPopular;
	}

	public void setPopular(boolean isPopular) {
		this.isPopular = isPopular;
	}

	public boolean isAirport() {
		return isAirport;
	}

	public void setAirport(boolean isAirport) {
		this.isAirport = isAirport;
	}
}
