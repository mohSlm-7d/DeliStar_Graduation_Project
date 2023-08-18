package com.deli.star.processor;

import com.deli.star.DTO.Admin;
import com.deli.star.authentication.TokenAuthValidator;
import com.deli.star.dao.DAO;
import com.deli.star.response.codes.ResponseStatus;

public class AdminLogOutProcessor {
	public boolean LogOut(Admin requestingAdmin) {
		try {
			
			DAO updateTokenLogout = new DAO();
			
			Admin foundAdmin = updateTokenLogout.GetAdminCredentials(requestingAdmin);
			
			// Checking if the admin is already logged out then the request is invalid.
//			if(foundAdmin.getAdminToken() == null) {
//				requestingAdmin.setAdminToken(ResponseStatus.Invalid_Access.toString());
//				return false;
//			}
			
			
			// Admin Authentication by token.
			TokenAuthValidator adminAuth = new TokenAuthValidator();
			boolean authentic = adminAuth.ValidateAdminToken(requestingAdmin);
			if(!authentic) {
				return false;
			}
			
			
			requestingAdmin.setAdminToken(null);
			boolean updated = updateTokenLogout.UpdateAdminToken(requestingAdmin);
			
			if(!updated) {
				return false;
			}
		
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		return false;
	}
}
