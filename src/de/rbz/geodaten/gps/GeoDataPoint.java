/**
 * 
 */
package de.rbz.geodaten.gps;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author l_admin
 *
 */
public class GeoDataPoint implements Comparable<GeoDataPoint>, Parcelable{
	
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
				"%s %nlat=%.4f, long=%.4f", formatter.format(date),
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

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);
		dest.writeLong(date.getTime());
		
	}
	
	public GeoDataPoint(Parcel parcel) {
		this.latitude = parcel.readDouble();
		this.longitude = parcel.readDouble();
		this.date = new Date(parcel.readLong());
	}
	
	public static final Parcelable.Creator<GeoDataPoint> CREATOR = new Creator<GeoDataPoint>() {

	    public GeoDataPoint createFromParcel(Parcel source) {

	        return new GeoDataPoint(source);
	    }

	    public GeoDataPoint[] newArray(int size) {

	        return new GeoDataPoint[size];
	    }

	};
	
}
