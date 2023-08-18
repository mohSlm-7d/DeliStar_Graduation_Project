package com.deli.star.processor;

import com.deli.star.DTO.Driver;
import com.deli.star.authentication.TokenAuthValidator;
import com.deli.star.dao.DAO;
/*
 * @author Mohammad Salman
 * @Date 14 - 7 - 2023
 * 
 * */
import com.deli.star.response.codes.ResponseStatus;

public class DriverLogInProcessor {	
	public Driver LogIn(Driver requestingDriver) {
		try{		
			System.out.println("Entered The Driver Log In!!!!");
			
			DAO FetchDriverLogIn = new DAO();
			Driver foundDriver = FetchDriverLogIn.GetDriver(requestingDriver);
			
			// Checking if the driver is already logged in then the request is invalid.
//			if(foundDriver.getDriverToken() != null) {
//				requestingDriver.setDriverToken(ResponseStatus.Invalid_Access.toString());
//				return null;
//			}
			
			
			if(foundDriver == null || foundDriver.getDriverId() == 0) {
				requestingDriver.setDriverToken(ResponseStatus.Incorrect_Info.toString());
				return null;
			}
			
			
			
			requestingDriver.setDriverPassword("" + requestingDriver.getDriverPassword().hashCode());
			if(!requestingDriver.getDriverPassword().equals(foundDriver.getDriverPassword())) {
				requestingDriver.setDriverToken(ResponseStatus.Incorrect_Info.toString()); 
				return null;
			}
			
			// Token authentication
			TokenAuthValidator DriverTokenAuth = new TokenAuthValidator();
			boolean tokenCreated = DriverTokenAuth.CreateDriverToken(foundDriver);
			
			if(!tokenCreated) {
				return null;
			}
			
			DAO fetchDriver = new DAO();
			Driver loggedInDriver = fetchDriver.GetDriver(foundDriver);
			if(loggedInDriver == null || loggedInDriver.getDriverId() == 0) {
				return null;
			}
			
			return loggedInDriver;
		}catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			// throw e;
		}
		
		return null;
	}
}