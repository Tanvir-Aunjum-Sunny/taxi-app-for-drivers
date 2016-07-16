package com.taxiapp.model.business;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {

	@SerializedName("name")
	private String firstName;
	private String lastName;
	// mostly in our case email or phone number will be the username.
	private String username;
	private char[] password;
	private List<Address> addresses;
	private Address primaryAddress;
	@SerializedName("mobileno")
	private String phoneNumber;

	@SerializedName("email")
	private String emailAddress;

	@SerializedName("salutation")
	private String _salutation;
	@SerializedName("city")
	private City primaryCity;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSalutation() {
		return _salutation;
	}

	public void setSalutation(String _salutation) {
		this._salutation = _salutation;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public char[] getPassword() {
		return password;
	}

	public void setPassword(char[] password) {
		this.password = password;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	public Address getPrimaryAddress() {
		return primaryAddress;
	}

	public void setPrimaryAddress(Address primaryAddress) {
		this.primaryAddress = primaryAddress;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public City getPrimaryCity() {
		return primaryCity;
	}

	public void setPrimaryCity(City primaryCity) {
		this.primaryCity = primaryCity;
	}

}
