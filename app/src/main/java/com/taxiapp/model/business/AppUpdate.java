package com.taxiapp.model.business;

public class AppUpdate {

	private String version;
	private String date;
	private int versionCode;
	private int frequency = 30;
	private String baseURL;
	private String trackURL;
	private boolean isCritical;
	private boolean isUpdatePending;

	public boolean isUpdatePending() {
		return isUpdatePending;
	}

	public void setUpdatePending(boolean isUpdatePending) {
		this.isUpdatePending = isUpdatePending;
	}

	public boolean isCritical() {
		return isCritical;
	}

	public void setCritical(boolean isCritical) {
		this.isCritical = isCritical;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public String getBaseURL() {
		return baseURL;
	}

	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	public String getTrackURL() {
		return trackURL;
	}

	public void setTrackURL(String trackURL) {
		this.trackURL = trackURL;
	}

}
