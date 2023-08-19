package com.deli.star.DTO;

public class Company {
	
	private int companyId; 
	private String companyName; 
	private String companyEmail;
	private String companyPhoneNo;
	
	
	public Company() {
		
	}
	public Company(int companyId, String companyName, String companyEmail, String companyPhoneNo) {
		super();
		this.companyId = companyId;
		this.companyName = companyName;
		this.companyEmail = companyEmail;
		this.companyPhoneNo = companyPhoneNo;
	}
	public int getCompanyId() {
		return companyId;
	}
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyEmail() {
		return companyEmail;
	}
	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}
	public String getCompanyPhoneNo() {
		return companyPhoneNo;
	}
	public void setCompanyPhoneNo(String companyPhoneNo) {
		this.companyPhoneNo = companyPhoneNo;
	}

	
	
	
	
}
