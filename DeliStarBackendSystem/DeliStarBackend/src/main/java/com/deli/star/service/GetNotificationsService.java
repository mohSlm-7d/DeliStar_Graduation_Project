package com.deli.star.service;

import java.util.ArrayList;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.deli.star.DTO.Driver;
import com.deli.star.DTO.Notification;
import com.deli.star.processor.GetNotificationsProcessor;
import com.deli.star.response.codes.ResponseStatus;

@Path("/Driver/Notifications/")
public class GetNotificationsService {
	@POST @Path("Fetch") @Produces(MediaType.APPLICATION_JSON)
	public String GetNotifications(String notificationsRequest) {
		JSONObject response = new JSONObject();
		
		try {
			System.out.println("In notifications: " + notificationsRequest);
			JSONParser parser = new JSONParser();
			JSONObject requestJSON = (JSONObject)parser.parse(notificationsRequest);
			Driver requestingDriver = new Driver();
			requestingDriver.setDriverToken(requestJSON.get("driverToken").toString());
			requestingDriver.setDriverId(Integer.parseInt(requestJSON.get("driverId").toString()));
			
			
			
			GetNotificationsProcessor getNotificationsProcess = new GetNotificationsProcessor();
			ArrayList<Notification> driverNotifications = getNotificationsProcess.GetNotifications(requestingDriver);
			
			if(driverNotifications == null) {
				ResponseStatus[] responses = ResponseStatus.class.getEnumConstants();
				for(ResponseStatus responseItr : responses) {
					if(requestingDriver.getDriverToken().toString().equals(responseItr.toString())) {
						response.put("status", requestingDriver.getDriverToken().toString());
						return response.toString();
					}
				}
				
				response.put("status", ResponseStatus.Failed.toString());
				response.put("driverToken", requestingDriver.getDriverToken().toString());
				System.out.println("notifications response 11111111111: " + response);
				return response.toString();
			}
			
			
			response.put("status", ResponseStatus.Success.toString());
			response.put("driverToken", requestingDriver.getDriverToken().toString());
			
			
			JSONArray notificationsList = new JSONArray();
			
			for(Notification notification : driverNotifications){
				JSONObject notificationJSON = new JSONObject();
				notificationsList.add(notification.getMessage().toString());
			}
			
			response.put("notifications", notificationsList);
			System.out.println("notifications response 333333: " + response);
			return response.toString();
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			response.put("status", ResponseStatus.Failed.toString());
		}
		
		
		return response.toString();
	}
}
