package com.taxiapp.model.business;

import org.apache.commons.lang.StringUtils;

public class Address implements Comparable<Address> {

	private int addressType = 2;
	private String doorNumber;
	private String building;
	private String area;
	private String street;
	private City city;
	private String pinCode;
	private String landmark;
	private Locality locality;
	// This field is used to stored a one line address entered by User. We
	// do not ask user to fill area, street, etc info.
	private String userEnteredAddress;

	public String getDoorNumber() {
		return doorNumber;
	}

	public void setDoorNumber(String doorNumber) {
		this.doorNumber = doorNumber;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	public String getFormattedAddress() {
        return locality == null ? "Not given" : locality.getValue();
	}

	public Locality getLocality() {
		return locality;
	}

	public void setLocality(Locality locality) {
		this.locality = locality;
	}

	public String getUserEnteredAddress() {
		return userEnteredAddress;
	}

	public void setUserEnteredAddress(String userEnteredAddress) {
		this.userEnteredAddress = userEnteredAddress;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		if (StringUtils.isNotBlank(doorNumber)) {
			// buffer.append("Door Number: " + doorNumber + ", ");
			buffer.append(doorNumber + ", ");
		}
		if (StringUtils.isNotBlank(area)) {
			// buffer.append("Area: " + area + ", ");
			buffer.append(area + ", ");
		}
		if (StringUtils.isNotBlank(street)) {
			// buffer.append("Street: " + street + ", ");
			buffer.append(street + ", ");
		}

		return buffer.toString();
	}

	public int getAddressType() {
		return addressType;
	}

	public void setAddressType(int addressType) {
		this.addressType = addressType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((userEnteredAddress == null) ? 0 : userEnteredAddress
						.hashCode());
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
		Address other = (Address) obj;
		if (userEnteredAddress == null) {
			if (other.userEnteredAddress != null)
				return false;
		} else if (!userEnteredAddress.equals(other.userEnteredAddress))
			return false;
		return true;
	}

	@Override
	public int compareTo(Address another) {
		if (StringUtils.isBlank(userEnteredAddress))
			return -1;
		else if (StringUtils.isBlank(another.userEnteredAddress))
			return 1;
		return userEnteredAddress
				.compareToIgnoreCase(another.userEnteredAddress);
	}

}