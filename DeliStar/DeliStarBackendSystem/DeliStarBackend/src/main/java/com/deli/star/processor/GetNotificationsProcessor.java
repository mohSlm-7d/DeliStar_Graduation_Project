package com.deli.star.processor;

import java.util.ArrayList;

import com.deli.star.DTO.Driver;
import com.deli.star.DTO.Notification;
import com.deli.star.authentication.TokenAuthValidator;
import com.deli.star.dao.DAO;
public class GetNotificationsProcessor {
	public ArrayList<Notification> GetNotifications(Driver requestingDriver){
		try {
			// Driver authentication by token.
			DAO fetchAndGet = new DAO();
			TokenAuthValidator driverAuth = new TokenAuthValidator();
			boolean authentic = driverAuth.ValidateDriverToken(requestingDriver);
			if(!authentic) {
				return null;
			}
			
			
			
			ArrayList<Notification> driverNotifications = fetchAndGet.GetNotifications(requestingDriver);
			
			return driverNotifications;
		}catch(Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		return null;
	}
}
