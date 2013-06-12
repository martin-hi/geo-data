/**
 * 
 */
package de.rbz.geodaten.gps;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author l_admin
 *
 */
public class GeoDataPoint implements Comparable<GeoDataPoint>{
	
	private static final SimpleDateFormat formatter = new SimpleDateFormat();
	
	private final Date date;
	private final double latitude, longitude;

	public GeoDataPoint(double latitude, double longitude) {
		this.date = new Date();
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public GeoDataPoint(Date date, double latitude, double longitude) {
		this.date = date;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Date getDate() {
		return date;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	@Override
	public String toString() {
		return String.format(
				"%s, lat=%s, long=%s", formatter.format(date),
				latitude, longitude);
	}

	@Override
	public int compareTo(GeoDataPoint another) {
	    final int BEFORE = -1;
	    final int EQUAL = 0;
	    final int AFTER = 1;
	    
	    long thisTime = this.date.getTime();
	    long otherTime = another.getDate().getTime();
	    
	    if( thisTime == otherTime) {
	    	return EQUAL;
	    }else if(thisTime < otherTime) {
	    	return AFTER;
	    }else{
	    	return BEFORE;
	    }
	}
	
}
