package com.taxiapp.model.business;

public class TripLocal extends Trip {

	/*
	 * public enum Duration { DURATION_HALF_DAY, DURATION_FULL_DAY,
	 * DURATION_2_HOURS, DURATION_4_HOURS, };
	 */
	private Duration duration;
	private Locality locality;
	private String localPackage;

	public TripLocal() {
		setTripType(TripType.LOCAL);
	}

	public Locality getLocality() {
		return locality;
	}

	public void setLocality(Locality locality) {
		this.locality = locality;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public String getLocalPackage() {
		return localPackage;
	}

	public void setLocalPackage(String localPackage) {
		this.localPackage = localPackage;
	}

}
