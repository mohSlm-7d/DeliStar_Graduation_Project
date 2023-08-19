package com.deli.star.processor;

import java.time.LocalDate;

import com.deli.star.DTO.Admin;
import com.deli.star.DTO.Company;
import com.deli.star.DTO.Driver;
import com.deli.star.authentication.TokenAuthValidator;
import com.deli.star.dao.DAO;
import com.deli.star.response.codes.ResponseStatus;

public class BanDriverProcessor {
	public boolean BanDriver(Admin requestingAdmin, Driver driverToBan) {
		try {
			// Admin token authentication.
			TokenAuthValidator adminAuth = new TokenAuthValidator();
			boolean authentic = adminAuth.ValidateAdminToken(requestingAdmin);
			if(!authentic) {
				return false;
			}
			
			DAO fetchAndBan = new DAO();
			
			// Authorization check.
			Company adminCompany = fetchAndBan.GetAdminCompany(requestingAdmin);
			Company driverCompany = fetchAndBan.GetDriverCompany(driverToBan);
			if(adminCompany.getCompanyId() != driverCompany.getCompanyId()) {
				requestingAdmin.setAdminToken(ResponseStatus.Not_Authorized.toString());
				return false;
			}
			
			
			if(driverToBan.getBannedUntil().compareTo(LocalDate.now()) > 0) {
				Driver foundDriver = fetchAndBan.GetDriver(driverToBan);
				
				foundDriver.setIsBanned(true);
				foundDriver.setBannedUntil(driverToBan.getBannedUntil());
				boolean banned = fetchAndBan.UpdateDriver(foundDriver);
				if(!banned) {
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
