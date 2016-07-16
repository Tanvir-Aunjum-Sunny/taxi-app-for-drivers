package com.taxiapp.model;

public enum RequestType {

	MDM_REGISTRATION("urn:mdm_api_class#registerDevice", "urn:mdm_api_class",
			"registerDevice"), MDM_REAL_TIME_AUDIT_DATA(
			"urn:mdm_api_class#sendRealTimeAuditData", "urn:mdm_api_class",
			"sendRealTimeAuditData"), CONSUMER_GET_ALL_BOOKINGS(
			"urn:consumer_api_class#getAllBookings", "urn:consumer_api_class",
			"getAllBookings"), CONSUMER_GET_CURRENT_LOCATION(
			"urn:consumer_api_class#getCurrentLocation",
			"urn:consumer_api_class", "getCurrentLocation"), CONSUMER_GET_LOCATION_POINTS(
			"urn:consumer_api_class#getLocationPoints",
			"urn:consumer_api_class", "getLocationPoints"), ;

	private String action;
	private String namespace;
	private String methodName;

	private RequestType(String action, String namespace, String methodName) {
		this.action = action;
		this.namespace = namespace;
		this.methodName = methodName;
	}

	public String getAction() {
		return action;
	}

	public String getNamespace() {
		return namespace;
	}

	public String getMethodName() {
		return methodName;
	}
}
