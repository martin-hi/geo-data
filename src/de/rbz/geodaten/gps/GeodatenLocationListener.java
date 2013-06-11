/**
 * 
 */
package de.rbz.geodaten.gps;

import java.util.List;

import de.rbz.geodaten.db.Database;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * @author l_admin
 *
 */
public class GeodatenLocationListener implements LocationListener {
	
	private List<GeoDataPoint> datapoints;
	private Database db;
	private ListAdapter listAdapter;
	private ListView listView;
		
	public GeodatenLocationListener(List<GeoDataPoint> dataPoints, Database DB) {
		this.datapoints = dataPoints;
		this.db = DB;
	}

	@Override
	public void onLocationChanged(Location location) {
		double latitude = location.getLatitude();
		double longitude = location.getLongitude();
		
		GeoDataPoint dataPoint = new GeoDataPoint(latitude, longitude);
		this.datapoints.add(dataPoint);
		
		listView.setAdapter(listAdapter);		
		
		db.write(dataPoint);
	}
	
	public void setViewObjects(ListAdapter listAdapter, ListView listView) {
		this.listAdapter = listAdapter;
		this.listView = listView;
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

}
