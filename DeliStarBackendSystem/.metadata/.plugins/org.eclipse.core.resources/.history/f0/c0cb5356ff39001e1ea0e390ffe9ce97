package com.deli.star.processor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import com.deli.star.DTO.Admin;
import com.deli.star.DTO.Company;
import com.deli.star.DTO.Order;
import com.deli.star.authentication.TokenAuthValidator;
import com.deli.star.dao.DAO;
import com.deli.star.response.codes.ResponseStatus;

public class ReschedulingOrderProcessor {
	public Map<String, String> RescheduleOrder(Admin requestingAdmin, Order rescheduledOrder) {
		Map<String, String> result = new HashMap<String, String>();
		try{

			EditOrderProcessor editingOrderProcess = new EditOrderProcessor();
			return editingOrderProcess.EditOrder(requestingAdmin, rescheduledOrder);

		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			result.put("status", ResponseStatus.Failed.toString());
		}
		return result;
	}
}
