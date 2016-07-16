package com.taxiapp.model.business;

import com.google.gson.annotations.SerializedName;

public class City {

	@SerializedName("city_id")
	private String cityId;

	@SerializedName("city_name")
	private String name;

	private String state;
	@SerializedName("time_lag")
	private int timeLag;

	@SerializedName("terminal")
	private int numberOfTerminals;

	private boolean askLocalities = true;

	@SerializedName("google_city_name")
	private String googleName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getTimeLag() {
		return timeLag;
	}

	public void setTimeLag(int timeLag) {
		this.timeLag = timeLag;
	}

	public int getNumberOfTerminals() {
		return numberOfTerminals;
	}

	public void setNumberOfTerminals(int numberOfTerminals) {
		this.numberOfTerminals = numberOfTerminals;
	}

	public boolean isAskLocalities() {
		return askLocalities;
	}

	public void setAskLocalities(boolean askLocalities) {
		this.askLocalities = askLocalities;
	}

	public String getGoogleName() {
		return googleName;
	}

	public void setGoogleName(String googleName) {
		this.googleName = googleName;
	}

    private String mReachableFromCities;

    public String getReachableFromCities() {
        return mReachableFromCities;
    }

    public void setReachableFromCities(String mReachableFromCities) {
        this.mReachableFromCities = mReachableFromCities;
    }

    public City() {
    }

    public City(String name, String id) {
        if (name == null || name.isEmpty() || id == null || id.isEmpty()) {
            throw new IllegalArgumentException("name or id cannot be null");
        }
        setCityId(id);
        setName(name);
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cityId == null) ? 0 : cityId.hashCode());
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
		City other = (City) obj;
		if (cityId == null) {
			if (other.cityId != null)
				return false;
		} else if (!cityId.equals(other.cityId))
			return false;
		return true;
	}
}
