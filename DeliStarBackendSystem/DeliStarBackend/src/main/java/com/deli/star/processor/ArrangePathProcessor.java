package com.deli.star.processor;

import java.util.ArrayList;
import java.util.HashSet;

import com.deli.star.DTO.Order;

public class ArrangePathProcessor {
	private Order NextNearestOrder(ArrayList<Order> list, double driverLat, double driverLng, int currentRank) {		
		
		double minEstimatedTime = Double.MAX_VALUE;
		int minIndex = -1;
		Order minOrder = null;
		int noOfOrders = list.size();
		
		try {
				int i = 0;
				while(i < noOfOrders) {
					Order orderItr = list.get(i);
					String orderCoordinates = orderItr.getLocationCoordinates();
					double orderLat = Double.valueOf(orderCoordinates.split(" ")[0].toString());
					double orderLng = Double.valueOf(orderCoordinates.split(" ")[1].toString());
						
					GMapsApiProcessor estimateTimeProcess = new GMapsApiProcessor();
					
					System.out.println("before getting estimatedTime in ArrangePathProcessor in line 26!!!!");
					
					double orderEstimatedTime = estimateTimeProcess.GetEstimatedTime(driverLat, driverLng, orderLat, orderLng);
					
					System.out.println("after getting estimatedTime in ArrangePathProcessor in line 30!!!!");
					
					if(orderEstimatedTime < minEstimatedTime) {
						minIndex = i;
						minOrder = orderItr;
						minEstimatedTime = orderEstimatedTime;
					}
						 
					
					
					i++;
				}
				
				if(minIndex != -1 && minOrder != null) {
					minOrder.setOrderRank(currentRank);
					return minOrder;
				}
				
				System.out.println("getting nextOrder as null in ArrangePathProcessor in line 42!!!!");
			
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		return null;

	}
	
	public Order GetNextOrder(ArrayList<Order> ordersList, double driverLat, double driverLng, int currentRank) {
		try {
			System.out.println("before getting nextOrder in ArrangePathProcessor!!!!");
			return this.NextNearestOrder(ordersList, driverLat, driverLng, currentRank);
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return null;
	}
}
