package com.taxiapp.model.business;

public enum BookingType {
	UPCOMING("Upcoming", 0x1F92B1), COMPLETED("Completed", 0x149600), CANCELLED(
			"Cancelled", 0xFF6669);

	private String title;
	private int code;

	private BookingType(String title, int code) {
		this.title = title;
		this.code = code;
	}

	@Override
	public String toString() {
		return title;
	}

	public int getCode() {
		return code;
	}

	public static BookingType getTripType(int position) {
		return values()[position];
	}

	public static BookingType getTripType(String alias) {
		for (BookingType t : values()) {
			if (alias.equals(t.code)) {
				return t;
			}
		}
		return null;
	}
};