package com.deli.star.processor;

import com.deli.star.DTO.Admin;
import com.deli.star.DTO.Company;
import com.deli.star.authentication.TokenAuthValidator;
import com.deli.star.dao.DAO;

public class EditAdminAndCompanyProcessor {
	public boolean EditAdminAndCompany(Admin requestingAdmin) {
		// Admin authentication by token.
		TokenAuthValidator adminAuth = new TokenAuthValidator();
		boolean authentic = adminAuth.ValidateAdminToken(requestingAdmin);
		if(!authentic) {
			return false;
		}
		
		DAO editAdmin = new DAO();
		
		boolean editedCompany = true;
		if(requestingAdmin.getCompany() != null) {
			// Authorization check.
			// Checking if the admin belongs to the same company they are updating its information.
			Company adminCompany = editAdmin.GetAdminCompany(requestingAdmin);
			if(adminCompany.getCompanyId() != requestingAdmin.getCompany().getCompanyId()) {
				return false;
			}
			
			String processedPhoneNo = "";
			String companyPhoneNo = requestingAdmin.getCompany().getCompanyPhoneNo();
			for(int i =0; i< companyPhoneNo.length(); i++) {
				if(companyPhoneNo.charAt(i) == '+') {
					processedPhoneNo += companyPhoneNo.charAt(i);
				}
				else if((int)companyPhoneNo.charAt(i) >= 48 && (int)companyPhoneNo.charAt(i) <= 57 ) {
					processedPhoneNo += companyPhoneNo.charAt(i);
				}
			}
			requestingAdmin.getCompany().setCompanyPhoneNo(processedPhoneNo);
			
			editedCompany = editAdmin.UpdateCompany(requestingAdmin.getCompany());
		}
		
		
		requestingAdmin.setAdminPassword("" + requestingAdmin.getAdminPassword().hashCode());
		
		String processedPhoneNo = "";
		String adminPhoneNo = requestingAdmin.getAdminPhoneNo();
		for(int i =0; i< adminPhoneNo.length(); i++) {
			if(adminPhoneNo.charAt(i) == '+') {
				processedPhoneNo += adminPhoneNo.charAt(i);
			}
			else if((int)adminPhoneNo.charAt(i) >= 48 && (int)adminPhoneNo.charAt(i) <= 57 ) {
				processedPhoneNo += adminPhoneNo.charAt(i);
			}
		}
		requestingAdmin.setAdminPhoneNo(processedPhoneNo);
		
		boolean editedAdmin = editAdmin.UpdateAdmin(requestingAdmin);	
		
		return editedAdmin && editedCompany;
		
	}
}
