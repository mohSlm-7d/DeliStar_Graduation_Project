package com.deli.star.service;


import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

//import org.json.JSONObject;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.deli.star.DTO.Admin;
import com.deli.star.DTO.Driver;
import com.deli.star.processor.AdminLogInProcessor;
import com.deli.star.processor.DriverLogInProcessor;
import com.deli.star.response.codes.ResponseStatus;

@Path("/Login/")
public class LogInService {

	// return type is JSON and it accepts JSON as parameter
	@POST @Path("Admin") @Produces({MediaType.APPLICATION_JSON})
	public String AdminLogIn(/*@FormParam("loginRequest")*/ String loginRequest) {
		System.out.println("Admin Logged In!!!");
		System.out.println("REQUEST: "+ loginRequest);
		JSONObject response = new JSONObject();

		try {
			JSONParser parser = new JSONParser();
			Object parsedJSON = parser.parse(loginRequest);
			JSONObject adminRequest = (JSONObject)parsedJSON;


			Admin requestingAdmin = new Admin();

			requestingAdmin.setAdminEmail(adminRequest.get("adminEmail").toString());
			requestingAdmin.setAdminPassword(adminRequest.get("adminPassword").toString());

			AdminLogInProcessor loginRequestProcessor = new AdminLogInProcessor();

			Admin retrievedAdmin = loginRequestProcessor.LogIn(requestingAdmin);



			if(retrievedAdmin == null) {
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
			adminJSON.put("adminId", retrievedAdmin.getAdminId());
			adminJSON.put("adminName", retrievedAdmin.getAdminName());
			adminJSON.put("adminEmail", retrievedAdmin.getAdminEmail());
			adminJSON.put("adminToken", retrievedAdmin.getAdminToken());
			adminJSON.put("adminPhoneNo", retrievedAdmin.getAdminPhoneNo());
			adminJSON.put("adminGender", retrievedAdmin.getAdminGender());

			adminJSON.put("adminToken", retrievedAdmin.getAdminToken());
			adminJSON.put("companyId", retrievedAdmin.getCompany().getCompanyId());
			adminJSON.put("companyName", retrievedAdmin.getCompany().getCompanyName());
			adminJSON.put("companyEmail", retrievedAdmin.getCompany().getCompanyEmail());
			adminJSON.put("companyPhoneNo", retrievedAdmin.getCompany().getCompanyPhoneNo());


			response.put("admin", adminJSON);

			System.out.println("RESPINSE in login ADmin " + response);

			return response.toString();
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			response.put("status", ResponseStatus.Failed.toString());
		}


		return response.toString();
	}

	// return type is JSON and it accepts JSON as parameter
	@POST @Path("Driver") @Produces({MediaType.APPLICATION_JSON})
	public String DriverLogIn(String loginRequest) {
		JSONObject response = new JSONObject();

		try{
			JSONParser parser = new JSONParser();
			JSONObject driverRequest = (JSONObject)parser.parse(loginRequest);


			Driver requestingDriver = new Driver();

			requestingDriver.setDriverEmail(driverRequest.get("driverEmail").toString());
			requestingDriver.setDriverPassword(driverRequest.get("driverPassword").toString());

			DriverLogInProcessor loginRequestProcessor = new DriverLogInProcessor();

			Driver retrievedDriver = loginRequestProcessor.LogIn(requestingDriver);




			if(retrievedDriver == null) {
				ResponseStatus[] responses = ResponseStatus.class.getEnumConstants();
				for(ResponseStatus responseItr : responses) {
					if(requestingDriver.getDriverToken().toString().equals(responseItr.toString())) {
						response.put("status", requestingDriver.getDriverToken().toString());
						return response.toString();
					}
				}

				response.put("status", ResponseStatus.Failed.toString());
				response.put("driverToken", requestingDriver.getDriverToken().toString());
				return response.toString();
			}


			response.put("status", ResponseStatus.Success.toString());

			JSONObject driverJSON = new JSONObject();
			driverJSON.put("driverId", retrievedDriver.getDriverId());
			driverJSON.put("driverName", retrievedDriver.getDriverName());
			driverJSON.put("driverEmail", retrievedDriver.getDriverEmail());
			driverJSON.put("driverToken", retrievedDriver.getDriverToken());
			driverJSON.put("driverPhoneNo", retrievedDriver.getDriverPhoneNo());
			driverJSON.put("driverGender", retrievedDriver.getDriverGender());

			driverJSON.put("driverBannedUntil", retrievedDriver.getBannedUntil() != null ?
					retrievedDriver.getBannedUntil().toString()
					: null);
			driverJSON.put("driverCompanyId", retrievedDriver.getCompany().getCompanyId());
			driverJSON.put("driverIsBanned", retrievedDriver.getIsBanned());

			driverJSON.put("companyId", retrievedDriver.getCompany().getCompanyId());
			driverJSON.put("companyName", retrievedDriver.getCompany().getCompanyName());
			driverJSON.put("companyEmail", retrievedDriver.getCompany().getCompanyEmail());
			driverJSON.put("companyPhoneNo", retrievedDriver.getCompany().getCompanyPhoneNo());


			response.put("driver", driverJSON);


			return response.toString();
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			response.put("status", ResponseStatus.Failed.toString());
		}


		return response.toString();


	}
}
