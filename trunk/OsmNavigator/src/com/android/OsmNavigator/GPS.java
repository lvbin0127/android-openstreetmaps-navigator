package com.android.OsmNavigator;

import android.location.Location;

public class GPS {
	public Location location = new Location("gps");
	public Location target = new Location("gps");
	private int Radius = 6371000;
	
	public GPS(){
		location.setLatitude(45.4722635);
		location.setLongitude(9.1875991);
	}
	public void setActualLocation(double latitude, double longitude){
		location.setLatitude(latitude);
		location.setLongitude(longitude);
	}
	
	public void setTargetLocation(double latitude, double longitude){
		target.setLatitude(latitude);
		target.setLongitude(longitude);
	}
	public double getHaversineDistance(){
	    
		double dLat = radians(target.getLatitude() - location.getLatitude());
   	    double dLon = radians(target.getLongitude() - location.getLongitude()); 
   	    
   	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
   	        Math.cos(radians(location.getLatitude())) * Math.cos(radians(target.getLatitude())) * 
   	        Math.sin(dLon/2) * Math.sin(dLon/2); 
   	    
   	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
   	   
   	    return Radius * c;
   	
   }
	
   private double radians(double n) {
  	  return n * Math.PI / 180;
   }
}
