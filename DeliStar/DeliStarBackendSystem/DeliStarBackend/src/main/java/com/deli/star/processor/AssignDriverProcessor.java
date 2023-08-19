package com.deli.star.processor;

import com.deli.star.DTO.Admin;
import com.deli.star.DTO.Company;
import com.deli.star.DTO.Driver;
import com.deli.star.DTO.Order;
import com.deli.star.authentication.TokenAuthValidator;
import com.deli.star.dao.DAO;
import com.deli.star.response.codes.ResponseStatus;

public class AssignDriverProcessor {
	public boolean AssignDriver(Admin requestingAdmin, Driver assignedDriver, Order orderToAssign) {
		try {
			DAO fetchAndUpdate = new DAO();
			
			// Admin authentication by token.
			TokenAuthValidator adminAuth = new TokenAuthValidator();
			boolean authentic = adminAuth.ValidateAdminToken(requestingAdmin);
			if(!authentic) {
				return false;
			}
			
			
			// Authrization checking.
			
			// Checking if the admin belongs to the same company that the order belongs to.
			Company adminCompany = fetchAndUpdate.GetAdminCompany(requestingAdmin);
			
			Company orderCompany = new Company();
			orderToAssign.setCompanyId(adminCompany.getCompanyId());
			Order checkedOrder = fetchAndUpdate.GetOrder(orderToAssign);
			if(checkedOrder.getOrderId() == 0) {
				requestingAdmin.setAdminToken(ResponseStatus.Not_Authorized.toString());
				return false;
			}
			
			// Checking if the (admin, order) and driver all belong to the same company.
			Company driverCompany = fetchAndUpdate.GetDriverCompany(assignedDriver);
			if(driverCompany.getCompanyId() != adminCompany.getCompanyId()) {
				requestingAdmin.setAdminToken(ResponseStatus.Not_Authorized.toString());
				return false;
			}
			
			Driver checkDriverBan = fetchAndUpdate.GetDriver(assignedDriver); 
			if(checkDriverBan.getIsBanned()) {
				requestingAdmin.setAdminToken(ResponseStatus.Driver_Banned.toString());
				return false;
			}
			
			
			// Assigning driver to deliver the order.
			checkedOrder.assignDriver(assignedDriver);			
			boolean assigned = fetchAndUpdate.UpdateOrder(checkedOrder);
			
			if(!assigned) {
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
