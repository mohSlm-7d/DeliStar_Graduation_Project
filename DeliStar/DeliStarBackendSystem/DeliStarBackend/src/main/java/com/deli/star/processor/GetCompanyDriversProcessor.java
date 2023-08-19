package com.deli.star.processor;

import java.util.ArrayList;

import com.deli.star.DTO.Admin;
import com.deli.star.DTO.Company;
import com.deli.star.DTO.Driver;
import com.deli.star.authentication.TokenAuthValidator;
import com.deli.star.dao.DAO;

public class GetCompanyDriversProcessor {
	public ArrayList<Driver> GetDrivers(Admin requestingAdmin){
		try{
			// Admin authentication by token.
		
			TokenAuthValidator adminAuth = new TokenAuthValidator();
			boolean authentic = adminAuth.ValidateAdminToken(requestingAdmin);
			if(!authentic) {
				return null;
			}
			
			DAO fetch = new DAO();
			Company adminCompany = fetch.GetAdminCompany(requestingAdmin);
			ArrayList<Driver> listOfDrivers = fetch.GetCompanyDrivers(adminCompany);
			
			
			return listOfDrivers;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		return null;
	}
}
