package com.deli.star.DTO;

import java.util.Date;

public class Admin {
	private int adminId; 
	private String adminName;
	private String adminEmail; 
	private String adminPassword; 
	private String adminGender; 
	private String adminPhoneNo; 
	private String adminToken; 
	private Company company;
	
	public Admin() {
		super();
		this.company = new Company();
	}

	public Admin(int adminId, String adminName, String adminEmail, String adminPassword, String adminGender,
			String adminPhoneNo, String adminToken, Company company) {
		super();
		this.adminId = adminId;
		this.adminName = adminName;
		this.adminEmail = adminEmail;
		this.adminPassword = adminPassword;
		this.adminGender = adminGender;
		this.adminPhoneNo = adminPhoneNo;
		this.adminToken = adminToken;
		this.company = company;
	}

	public int getAdminId() {
		return adminId;
	}

	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getAdminEmail() {
		return adminEmail;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	public String getAdminGender() {
		return adminGender;
	}

	public void setAdminGender(String adminGender) {
		this.adminGender = adminGender;
	}

	public String getAdminPhoneNo() {
		return adminPhoneNo;
	}

	public void setAdminPhoneNo(String adminPhoneNo) {
		this.adminPhoneNo = adminPhoneNo;
	}

	public String getAdminToken() {
		return adminToken;
	}

	public void setAdminToken(String adminToken) {
		this.adminToken = adminToken;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		if(company == null) {
			this.company = null;
		}
		else {
			this.company = new Company();
			this.company.setCompanyEmail(company.getCompanyEmail());
			this.company.setCompanyId(company.getCompanyId());
			this.company.setCompanyName(company.getCompanyName());
			this.company.setCompanyPhoneNo(company.getCompanyPhoneNo());
		}
	}
	
	
}