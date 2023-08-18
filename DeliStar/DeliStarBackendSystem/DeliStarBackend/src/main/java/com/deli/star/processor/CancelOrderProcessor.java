package com.deli.star.processor;

import java.util.ArrayList;

import com.deli.star.DTO.Company;
import com.deli.star.DTO.Driver;
import com.deli.star.DTO.Order;
import com.deli.star.authentication.TokenAuthValidator;
import com.deli.star.dao.DAO;
import com.deli.star.response.codes.ResponseStatus;

public class CancelOrderProcessor {
	public ArrayList<Order> CancelOrderDelivery(Driver requestingDriver, Order cancelledOrder, double driverLat, double driverLng, ArrayList<Character> status) {
		try {
			
			TokenAuthValidator driverAuth = new TokenAuthValidator();
			boolean authentic = driverAuth.ValidateDriverToken(requestingDriver);
			if(!authentic) {
				return null;
			}
			
			DAO fetch = new DAO();
			
			// Authorization check.
			// Checking if there is an order that belongs to the same company 
			// that the driver belongs to and have the specified order number.
			Company driverCompany = fetch.GetDriverCompany(requestingDriver);
			
			cancelledOrder.setCompanyId(driverCompany.getCompanyId());
			Order foundOrder = fetch.GetOrder(cancelledOrder);
			
			if(foundOrder.getOrderId() == 0) {
				requestingDriver.setDriverToken(ResponseStatus.Not_Authorized.toString());
				return null;
			}
			
			Driver checkDriverBan = fetch.GetDriver(requestingDriver); 
			if(checkDriverBan.getIsBanned()) {
				String statusStr = new String(ResponseStatus.Driver_Banned.toString());
				for(int i=0; i< statusStr.length(); i++) {
					status.add(statusStr.charAt(i));
				}
				return null;
			}
			
			
			
			// Updating the order state to confirmed only if it's the order that the driver has to deliver it currently.
			if(foundOrder.getOrderRank() == 0) {
				requestingDriver.setDriverToken(ResponseStatus.Invalid_Access.toString());
				return null;
			}
			
			
			if(foundOrder.getOrderState().equals("cancelled")) {
				requestingDriver.setDriverToken(ResponseStatus.Invalid_Access.toString());
				return null;
			}
			
			
			// Updating the order state to cancelled and along with it the order report to the given order report.
			foundOrder.setOrderState("cancelled");
			foundOrder.setOrderReport(cancelledOrder.getOrderReport());
			boolean orderUpdated = fetch.UpdateOrderState(foundOrder);
			if(!orderUpdated) {
				return null;
			}
			


			// Get the next pending order that the driver has to deliver next.
			ArrayList<Order> ordersList = fetch.GetDriverOrders(requestingDriver, "pending");
			ArrangePathProcessor getNextOrderProcess = new ArrangePathProcessor();
			
			if(ordersList == null || ordersList.size() == 0) {
				return new ArrayList<Order>(1);
			}
			
			Order nextOrder = getNextOrderProcess.GetNextOrder(ordersList, driverLat, driverLng, foundOrder.getOrderRank() + 1);
			
			boolean updatedRank = fetch.UpdateOrderRank(nextOrder);
			if(!updatedRank) {
				return null;
			}
			
			int nextOrderId = nextOrder.getOrderId();
			int index = 0;
			while(index < ordersList.size()) {
				if(ordersList.get(index).getOrderId() == nextOrderId) {
					break;
				}
				index++;
			}
			
			if(index < ordersList.size()) {
				ordersList.set(index, ordersList.get(0));
				ordersList.set(0, nextOrder);
			}
			else {
				ordersList.add(0, nextOrder);
			}
			
			return ordersList;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		return null;
	}
}
