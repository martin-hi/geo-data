/**
 * 
 */
package de.rbz.geodaten.gps;

import java.util.List;

/**
 * @author l_admin
 *
 */
public class GeoCalculator {
	
	private static final int RADIUS = 6371;
	
	/**
	 * kilometers
	 * 
	 * @param data
	 * @return
	 */
	public static double calculateCoveredDistance(List<GeoDataPoint> data) {
		double distance = 0.0;
		
		for(int i = 0; i < data.size()-1; i++) {
			distance += calculatedDistance(data.get(i), data.get(i+1));
		}		
		
		return distance;
	}

	private static double calculatedDistance(GeoDataPoint pointA, GeoDataPoint pointB) {
				
//		var R = 6371; // km
//		var dLat = (lat2-lat1).toRad();
//		var dLon = (lon2-lon1).toRad();
//		var lat1 = lat1.toRad();
//		var lat2 = lat2.toRad();
//
//		var a = Math.sin(dLat/2) * Math.sin(dLat/2) +
//		        Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2); 
//		var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
//		var d = R * c;
				
		
		double latA = pointA.getLatitude();
		double longA = pointA.getLongitude();
		double latB = pointB.getLatitude();
		double longB = pointB.getLongitude();
		
		double dlat = Math.toRadians(latB-latA);
		double dlong = Math.toRadians(longB-longA);
		
		double latARad = Math.toRadians(latA);
		double latBRad = Math.toRadians(latB);
		
		double a = Math.sin(dlat/2.0d) * Math.sin(dlat/2.0d) +
					Math.sin(dlong/2.0d) * Math.sin(dlong/2.0d) * Math.cos(latARad) * Math.cos(latBRad);
		
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		return RADIUS * c;
		
	}

}
