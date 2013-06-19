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
	
	
	/**
	 * earth's radius
	 */
	private static final int RADIUS = 6371;
	
	/**
	 * Calculates the covered distance in kilometers from a List of GeoDataPoints
	 * 
	 * The list must be sorted beforehand.
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

	/**
	 * Calculates the distance between to GeoDataPoints (km).
	 * 
	 * @param pointA
	 * @param pointB
	 * @return
	 */
	private static double calculatedDistance(GeoDataPoint pointA, GeoDataPoint pointB) {
		
		// translated from JavaScript, see: http://stackoverflow.com/questions/365826/calculate-distance-between-2-gps-coordinates
				
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
