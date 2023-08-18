package com.deli.star.processor;

import java.util.ArrayList;

import com.deli.star.DTO.Admin;
import com.deli.star.DTO.Company;
import com.deli.star.DTO.Driver;
import com.deli.star.DTO.Notification;
import com.deli.star.authentication.TokenAuthValidator;
import com.deli.star.dao.DAO;
import com.deli.star.response.codes.ResponseStatus;

public class AddNotificationProcessor {
	public boolean AddNotification(Admin requestingAdmin, ArrayList<Notification> notificationsToAdd) {
		try {
			// Admin Authentication by token.
			TokenAuthValidator adminAuth = new TokenAuthValidator(); 
			boolean authentic = adminAuth.ValidateAdminToken(requestingAdmin);
			if(!authentic) {
				return false;
			}
			
			
			
			DAO fetchAndAdd = new DAO();
			
			final Company adminCompany = fetchAndAdd.GetAdminCompany(requestingAdmin);
			
			for(final Notification notification : notificationsToAdd) {
				final Driver driverIterator = new Driver();
				driverIterator.setDriverId(notification.getDriverId());
				final Company driverCompany = fetchAndAdd.GetDriverCompany(driverIterator);	
				
				// The admin is not authorized to notify a driver who belongs to different company.
				if(driverCompany.getCompanyId() != adminCompany.getCompanyId()) {
					requestingAdmin.setAdminToken(ResponseStatus.Not_Authorized.toString());
					return false;
				}
				
				boolean added = fetchAndAdd.AddNotification(notification);
				if(!added) {
					return false;
				}
				
			}
			
			
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		return false;
	}
}
