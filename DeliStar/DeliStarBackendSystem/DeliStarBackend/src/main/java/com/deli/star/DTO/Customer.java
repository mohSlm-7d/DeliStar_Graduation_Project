package com.deli.star.DTO;

public class Customer {
	
	private int customerId; 
	private String customerName; 
	private String customerPhoneNo;
	
	
	public Customer() {
		
	}
	public Customer(int customerId, String customerName, String customerPhoneNo) {
		super();
		this.customerId = customerId;
		this.customerName = customerName;
		this.customerPhoneNo = customerPhoneNo;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerPhoneNo() {
		return customerPhoneNo;
	}
	public void setCustomerPhoneNo(String customerPhoneNo) {
		this.customerPhoneNo = customerPhoneNo;
	}



}