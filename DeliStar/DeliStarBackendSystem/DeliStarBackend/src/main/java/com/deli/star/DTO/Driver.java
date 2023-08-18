package com.deli.star.DTO;

import java.time.LocalDate;
import java.util.Date;

public class Driver {
	private int driverId; 
	private String driverName;
	private String driverEmail; 
	private String driverPassword; 
	private String driverGender; 
	private String driverPhoneNo; 
	private boolean isBanned; 
	private LocalDate bannedUntil;
	private String driverToken; 
	private Company company;
	
	
	public Driver() {
		this.company = new Company();
	}
	public Driver(int driverId, String driverName, String driverEmail, String driverPassword, String driverGender,
			String driverPhoneNo, boolean isBanned, LocalDate bannedUntil, String driverToken, Company company) {
		super();
		this.driverId = driverId;
		this.driverName = driverName;
		this.driverEmail = driverEmail;
		this.driverPassword = driverPassword;
		this.driverGender = driverGender;
		this.driverPhoneNo = driverPhoneNo;
		this.isBanned = isBanned;
		this.bannedUntil = bannedUntil;
		this.driverToken = driverToken;
		this.company = company;
	}
	
	
	public int getDriverId() {
		return driverId;
	}
	public void setDriverId(int driverId) {
		this.driverId = driverId;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getDriverEmail() {
		return driverEmail;
	}
	public void setDriverEmail(String driverEmail) {
		this.driverEmail = driverEmail;
	}
	public String getDriverPassword() {
		return driverPassword;
	}
	public void setDriverPassword(String driverPassword) {
		this.driverPassword = driverPassword;
	}
	public String getDriverGender() {
		return driverGender;
	}
	public void setDriverGender(String driverGender) {
		this.driverGender = driverGender;
	}
	public String getDriverPhoneNo() {
		return driverPhoneNo;
	}
	public void setDriverPhoneNo(String driverPhoneNo) {
		this.driverPhoneNo = driverPhoneNo;
	}
	public boolean getIsBanned() {
		if(this.bannedUntil == null) {
			this.isBanned = false;
			return false;
		}

		LocalDate today = LocalDate.now();
		if(today.compareTo(this.bannedUntil) >= 0) {
			this.bannedUntil = null;
			return false;
		}
		
		this.isBanned = true;
		return isBanned;
	}
	public void setIsBanned(boolean isBanned) {
		this.isBanned = isBanned;
	}
	public LocalDate getBannedUntil() {
		return bannedUntil;
	}
	public void setBannedUntil(LocalDate bannedUntil) {
		if(bannedUntil == null) {
			this.bannedUntil = null;
		}
		this.bannedUntil = bannedUntil;
	}
	public String getDriverToken() {
		return driverToken;
	}
	public void setDriverToken(String driverToken) {
		this.driverToken = driverToken;
	}
	public Company getCompany() {
		return this.company;
	}
	public void setCompany(Company company) {
		this.company.setCompanyEmail(company.getCompanyEmail());
		this.company.setCompanyId(company.getCompanyId());
		this.company.setCompanyName(company.getCompanyName());
		this.company.setCompanyPhoneNo(company.getCompanyPhoneNo());
	}
	
	
	
}
