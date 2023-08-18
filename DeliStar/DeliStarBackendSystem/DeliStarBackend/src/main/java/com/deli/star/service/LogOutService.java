package com.deli.star.service;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.deli.star.DTO.Admin;
import com.deli.star.DTO.Driver;
import com.deli.star.processor.AdminLogOutProcessor;
import com.deli.star.processor.DriverLogOutProcessor;
import com.deli.star.response.codes.ResponseStatus;

@Path("/Logout/")
public class LogOutService {
	@POST @Path("Admin") @Produces(MediaType.APPLICATION_JSON)
	public String AdminLogOut(String logoutRequest) {
		JSONObject response = new JSONObject();
		try {
			JSONParser parser = new JSONParser();
			JSONObject requestJSON = (JSONObject)parser.parse(logoutRequest);
			
			Admin requestingAdmin = new Admin();
			requestingAdmin.setAdminId(Integer.parseInt(requestJSON.get("adminId").toString()));
			requestingAdmin.setAdminToken(requestJSON.get("adminToken").toString());
			
			AdminLogOutProcessor logoutProcess = new AdminLogOutProcessor();
			boolean loggedOut = logoutProcess.LogOut(requestingAdmin);
			
			if(!loggedOut) {
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
			return response.toString();
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			response.put("status", ResponseStatus.Failed.toString());
		}
		
		return response.toString();
	}
	
	@POST @Path("Driver") @Produces(MediaType.APPLICATION_JSON)
	public String DriverLogOut(String logoutRequest) {
		JSONObject response = new JSONObject();
		try {
			
			System.out.println("request:: " + logoutRequest);
			JSONParser parser = new JSONParser();
			
			JSONObject driverJSON = (JSONObject)parser.parse(logoutRequest);
			
			Driver requestingDriver = new Driver();
			requestingDriver.setDriverId(Integer.parseInt(driverJSON.get("driverId").toString()));
			requestingDriver.setDriverToken(driverJSON.get("driverToken").toString());
			
			DriverLogOutProcessor logoutProcess = new DriverLogOutProcessor();
			boolean loggedOut = logoutProcess.LogOut(requestingDriver);
			
			if(!loggedOut) {
				ResponseStatus[] responses = ResponseStatus.class.getEnumConstants();
				for(ResponseStatus responseItr : responses) {
					if(requestingDriver.getDriverToken().toString().equals(responseItr.toString())) {
						response.put("status", requestingDriver.getDriverToken().toString());
						System.out.println("failed logout 1: " + response);
						return response.toString();
					}
				}
				
				response.put("status", ResponseStatus.Failed.toString());
				response.put("driverToken", requestingDriver.getDriverToken().toString());
				System.out.println("failed logout 2 : " + response);
				return response.toString();
			}
			
			response.put("status", ResponseStatus.Success.toString());
			System.out.println("success logout: " + response);
			return response.toString();
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			response.put("status", ResponseStatus.Failed.toString());
		}
		
		return response.toString();
	}
}
