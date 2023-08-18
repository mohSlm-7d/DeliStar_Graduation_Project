package com.deli.star.DTO;

public class Notification {
	int driverId;
	String message;
	
	
	public Notification() {
		super();
	}
	public Notification(int driverId, String message) {
		super();
		this.driverId = driverId;
		this.message = message;
	}
	
	
	public int getDriverId() {
		return driverId;
	}
	public void setDriverId(int driverId) {
		this.driverId = driverId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
