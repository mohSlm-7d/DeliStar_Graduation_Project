package com.deli.star.processor;
import com.deli.star.DTO.Admin;
import com.deli.star.authentication.TokenAuthValidator;
import com.deli.star.dao.DAO;
import com.deli.star.response.codes.ResponseStatus;

public class AdminLogInProcessor {
	public Admin LogIn(Admin requestingAdmin) {
		try{
			
			DAO adminLogIn = new DAO();
			Admin foundAdmin = adminLogIn.GetAdminCredentials(requestingAdmin);
			// Checking if the admin is already logged in then the request is invalid.
//			if(foundAdmin.getAdminToken() != null) {
//				requestingAdmin.setAdminToken(ResponseStatus.Invalid_Access.toString());
//				return null;
//			}
			
			if(foundAdmin == null || foundAdmin.getAdminEmail() == null) {
				requestingAdmin.setAdminToken(ResponseStatus.Incorrect_Info.toString());
				return null;
			}
			
			requestingAdmin.setAdminPassword("" + requestingAdmin.getAdminPassword().hashCode());
			if(!requestingAdmin.getAdminPassword().equals(foundAdmin.getAdminPassword())) {
				requestingAdmin.setAdminToken(ResponseStatus.Incorrect_Info.toString()); 
				return null;
			}
			
			// Token authentication
			TokenAuthValidator AdminTokenAuth = new TokenAuthValidator();
			boolean tokenCreated = AdminTokenAuth.CreateAdminToken(foundAdmin);
			
			
			if(!tokenCreated) {
				return null;
			}
			
			
			
			Admin loggedInAdmin = adminLogIn.GetAdmin(foundAdmin);
			if(loggedInAdmin == null || loggedInAdmin.getAdminId() == 0) {
				return null;
			}
			
			
			
			return loggedInAdmin;
		}catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			// throw e;
		}
		
		return null;
	}
	
}
