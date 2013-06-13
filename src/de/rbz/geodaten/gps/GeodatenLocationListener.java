/**
 * 
 */
package de.rbz.geodaten.gps;

import java.util.Collections;
import java.util.List;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import de.rbz.geodaten.db.Database;

/**
 * @author l_admin
 * 
 */
public class GeodatenLocationListener implements LocationListener {

	private List<GeoDataPoint> datapoints;
	private Database db;
	private ArrayAdapter<List<GeoDataPoint>> arrayAdapter;
	private WebView mapView;

	private static final long WAIT_INTERVAL = 30l * 1000l;

	public GeodatenLocationListener(List<GeoDataPoint> dataPoints, Database DB,
			ArrayAdapter<List<GeoDataPoint>> arrayAdapter, WebView map) {
		this.datapoints = dataPoints;
		this.db = DB;
		this.arrayAdapter = arrayAdapter;
		this.mapView = map;
	}

	@Override
	public void onLocationChanged(Location location) {
		double latitude = location.getLatitude();
		double longitude = location.getLongitude();

		GeoDataPoint dataPoint = new GeoDataPoint(latitude, longitude);

		if (waitingPeriodHasElapsed(datapoints, dataPoint, WAIT_INTERVAL)) {
			this.datapoints.add(dataPoint);
			Collections.sort(this.datapoints);
			arrayAdapter.notifyDataSetChanged();
			db.write(dataPoint);

			mapView.loadUrl(StaticMapsURLBuilder.buildUrl(datapoints));

		}

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

	private static boolean waitingPeriodHasElapsed(List<GeoDataPoint> data,
			GeoDataPoint newData, long waitingPeriod) {
		if (data.isEmpty())
			return true;
		long lastUpdate = data.get(data.size() - 1).getDate().getTime();
		long thisUpdate = newData.getDate().getTime();
		return thisUpdate - lastUpdate > waitingPeriod;
	}

}
