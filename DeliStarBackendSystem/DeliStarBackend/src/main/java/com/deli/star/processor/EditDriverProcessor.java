package com.deli.star.processor;

import com.deli.star.DTO.Driver;
import com.deli.star.authentication.TokenAuthValidator;
import com.deli.star.dao.DAO;

public class EditDriverProcessor {
	public boolean EditDriver(Driver editedDriver) {
		// Driver authentication by token.
		TokenAuthValidator driverAuth = new TokenAuthValidator();
		boolean authentic = driverAuth.ValidateDriverToken(editedDriver);
		if(!authentic) {
			return false;
		}
		
		
		DAO editDriver = new DAO();
		Driver foundDriver = editDriver.GetDriver(editedDriver);
		editedDriver.setIsBanned(foundDriver.getIsBanned());
		editedDriver.setBannedUntil(foundDriver.getBannedUntil());
		
		editedDriver.setDriverPassword("" + editedDriver.getDriverPassword().hashCode());
		boolean edited = editDriver.UpdateDriver(editedDriver);
		
		return edited;
		
	}
}
