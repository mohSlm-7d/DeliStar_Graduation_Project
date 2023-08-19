package com.deli.star.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.deli.star.DTO.Admin;
import com.deli.star.DTO.Notification;
import com.deli.star.processor.AddNotificationProcessor;
import com.deli.star.response.codes.ResponseStatus;

@Path("/Admin/Notify/")
public class NotifyDriverService {
	@POST @Path("Driver") @Produces(MediaType.APPLICATION_JSON)
	public String NotifyDriver(String notifyRequest) {
		JSONObject response = new JSONObject();
		try {
			JSONParser parser = new JSONParser();
			JSONObject requestJSON = (JSONObject)parser.parse(notifyRequest);
			
			
			Admin requestingAdmin = new Admin();
			requestingAdmin.setAdminId(Integer.parseInt(requestJSON.get("adminId").toString()));
			requestingAdmin.setAdminToken(requestJSON.get("adminToken").toString());
			
			String notificationText = requestJSON.get("notificationMssg").toString();
			
			JSONArray listOfDrivers = (JSONArray)requestJSON.get("driverIds");
			
			ArrayList<Notification> notificationList = new ArrayList<Notification>();
			for(int i=0; i< listOfDrivers.size(); i++) {
				Notification notificationItr = new Notification();
				notificationItr.setDriverId(Integer.parseInt(listOfDrivers.get(i).toString()));
				notificationItr.setMessage(notificationText);
				notificationList.add(notificationItr);
			}
			
			AddNotificationProcessor addProcess = new AddNotificationProcessor();
			boolean added = addProcess.AddNotification(requestingAdmin, notificationList);
			if(!added) {
				ResponseStatus[] responses = ResponseStatus.class.getEnumConstants();
				for(ResponseStatus responseItr : responses) {
					if(requestingAdmin.getAdminToken().toString().equals(responseItr.toString())) {
						response.put("status", requestingAdmin.getAdminToken().toString());
						return response.toString();
					}
				}
				
				response.put("status", ResponseStatus.Failed.toString());
				
				JSONObject adminJSON = new JSONObject();
				adminJSON.put("adminToken", requestingAdmin.getAdminToken().toString());
				response.put("admin", adminJSON);
				
				return response.toString();
			}
			
			response.put("status", ResponseStatus.Success.toString());
			
			JSONObject adminJSON = new JSONObject();
			adminJSON.put("adminToken", requestingAdmin.getAdminToken().toString());
			response.put("admin", adminJSON);
			
			return response.toString();
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			response.put("status", ResponseStatus.Failed.toString());
		}
		
		return response.toString();
	}
}
