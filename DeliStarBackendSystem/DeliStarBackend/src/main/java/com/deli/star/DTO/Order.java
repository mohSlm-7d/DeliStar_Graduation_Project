package com.deli.star.DTO;

import java.time.LocalDate;
import java.sql.Date;

public class Order {
	
	private int orderId; 
	private int orderNumber; 
	private String orderReport; 
	private String orderState; 
	private int orderRank;
	private LocalDate orderDeliveryDate; 
	private String orderDropOffAddress; 
	private String orderLocationLink; 
	private String locationCoordinates;
	private String locationPlusCode;
	private Customer customer;
	private Driver assignedDriver; 
	private int companyId;
	
	
	public Order() {
		this.customer = new Customer();
		this.assignedDriver = new Driver();
	}
	public Order(int orderId, LocalDate orderDeliveryDate, String orderDropOffAddress, String orderLocationLink,
			Customer customer, int companyId) {
		this.orderId = orderId;
		this.orderDeliveryDate = orderDeliveryDate;
		this.orderDropOffAddress = orderDropOffAddress;
		this.orderLocationLink = orderLocationLink;
		this.customer = customer;
		this.companyId = companyId;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getOrderReport() {
		return orderReport;
	}
	public void setOrderReport(String orderReport) {
		this.orderReport = orderReport;
	}
	public String getOrderState() {
		return orderState;
	}
	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}
	public int getOrderRank() {
		return orderRank;
	}
	public void setOrderRank(int orderRank) {
		this.orderRank = orderRank;
	}
	public LocalDate getOrderDeliveryDate() {
		return orderDeliveryDate;
	}
	public void setOrderDeliveryDate(LocalDate orderDeliveryDate) {
		this.orderDeliveryDate = orderDeliveryDate;
	}
	public String getOrderDropOffAddress() {
		return orderDropOffAddress;
	}
	public void setOrderDropOffAddress(String orderDropOffAddress) {
		this.orderDropOffAddress = orderDropOffAddress;
	}
	public String getOrderLocationLink() {
		return orderLocationLink;
	}
	public void setOrderLocationLink(String orderLocationLink) {
		this.orderLocationLink = orderLocationLink;
	}
	public Customer getCustomer() {
		return this.customer;
	}
	public void setCustomer(Customer customer) {
		this.customer.setCustomerId(customer.getCustomerId());
		this.customer.setCustomerName(customer.getCustomerName());
		this.customer.setCustomerPhoneNo(customer.getCustomerPhoneNo());
	}
	public Driver getAssignedDriver() {
		return this.assignedDriver;
	}
	public void assignDriver(Driver driver) {
		this.assignedDriver.setDriverId(driver.getDriverId());
		this.assignedDriver.setDriverName(driver.getDriverName());
		this.assignedDriver.setDriverEmail(driver.getDriverEmail());
		this.assignedDriver.setDriverPhoneNo(driver.getDriverPhoneNo());
		this.assignedDriver.setDriverGender(driver.getDriverGender());
		this.assignedDriver.setIsBanned(driver.getIsBanned());
		this.assignedDriver.setBannedUntil(driver.getBannedUntil());
	}
	public int getCompanyId() {
		return companyId;
	}
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
	
	
	public String getLocationCoordinates() {
		return locationCoordinates;
	}
	public void setLocationCoordinates(String locationCoordinates) {
		this.locationCoordinates = locationCoordinates;
	}
	public String getLocationPlusCode() {
		return locationPlusCode;
	}
	public void setLocationPlusCode(String locationPlusCode) {
		this.locationPlusCode = locationPlusCode;
	}
	
	
	
	
	
}