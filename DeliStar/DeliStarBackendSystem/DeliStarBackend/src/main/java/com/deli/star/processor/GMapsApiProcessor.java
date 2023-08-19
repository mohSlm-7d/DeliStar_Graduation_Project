package com.deli.star.processor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class GMapsApiProcessor {
	private static String apiKey = "AIzaSyDgH5E-XecEVZb1_64O6Ng5XAxAGe3am0Y";
	
	public String GetCoordinates(String plusCode) {

        try {
            String encodedPlusCode = URLEncoder.encode(plusCode, "UTF-8");
            String apiUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" +
                    encodedPlusCode + "&key=" + apiKey;

            URL url = new URL(apiUrl);
            
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet getRequest = new HttpGet(url.toString());
            HttpResponse httpResponse = httpClient.execute(getRequest);
            
            String responseStr = EntityUtils.toString(httpResponse.getEntity());            
            
            String coordinates = null;
            if(responseStr != null) {	            	
            	JSONParser parser = new JSONParser();
            	JSONObject responseJSON = (JSONObject)parser.parse(responseStr.toString());
            
            	JSONArray resultsArray = (JSONArray)responseJSON.get("results");
            
            	JSONObject resultJSON = (JSONObject)resultsArray.get(0);
            	
            	coordinates = "";
            	coordinates+= ((JSONObject)((JSONObject)(resultJSON.get("geometry"))).get("location")).get("lat").toString();
            	coordinates+= " ";
            	coordinates+= ((JSONObject)((JSONObject)(resultJSON.get("geometry"))).get("location")).get("lng").toString();
            }
            
            return coordinates;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        
        return null;
    }
	
	public Double GetEstimatedTime(double originLat, double originLng, double destLat, double destLng) {
		try {
			 String requestUrl = "https://maps.googleapis.com/maps/api/directions/json?origin=" + originLat + "," + originLng +
		                "&destination=" + destLat + "," + destLng + "&key=" + apiKey;
			 
			 HttpClient httpClient = HttpClients.createDefault();
			 HttpGet getRequest = new HttpGet(requestUrl);
			 HttpResponse response = httpClient.execute(getRequest);
			 String responseStr = EntityUtils.toString(response.getEntity());
			
			 JSONParser parser = new JSONParser();
			 JSONObject responseJSON = (JSONObject)parser.parse(responseStr);
			
			 JSONArray routesArray = (JSONArray) responseJSON.get("routes");
			 
			 JSONObject route = (JSONObject)routesArray.get(0);
			 JSONArray legs = (JSONArray)route.get("legs");
			 JSONObject leg = (JSONObject)legs.get(0);
			 JSONObject durationJSON =(JSONObject)leg.get("duration");
			 
			return Double.valueOf(durationJSON.get("value").toString());
			
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		return null;
	}
	
}
