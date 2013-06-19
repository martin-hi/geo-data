/**
 * 
 */
package de.rbz.geodaten.gps;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.rbz.geodaten.db.Database;

/**
 * @author l_admin
 * 
 */
public class GeodatenLocationListener implements LocationListener {

	private List<GeoDataPoint> datapoints;
	private List<String> datadates;
	private Database db;
	private ArrayAdapter<List<String>> arrayAdapter;
	private TextView text;
//	private WebView mapView;
	private ArrayAdapter<List<GeoDataPoint>> arrayAdapterDaily;

	public static final long WAIT_INTERVAL = 30l * 1000l;
	
	public static GeodatenLocationListener INSTANCE;

	public GeodatenLocationListener(List<GeoDataPoint> dataPoints, List<String> dataDates, Database DB,
			ArrayAdapter<List<String>> arrayAdapter, TextView textView/*, WebView map*/) {
		this.datapoints = dataPoints;
		this.datadates = dataDates;
		this.db = DB;
		this.arrayAdapter = arrayAdapter;
		this.text = textView;
		
		this.arrayAdapterDaily = null;
		INSTANCE = this;
//		this.mapView = map;
	}
	
	public void setDailyAdapter(ArrayAdapter<List<GeoDataPoint>> arrayAdapter) {
		this.arrayAdapterDaily = arrayAdapter;
	}

	@Override
	public void onLocationChanged(Location location) {
		double latitude = location.getLatitude();
		double longitude = location.getLongitude();

		GeoDataPoint dataPoint = new GeoDataPoint(latitude, longitude);

		if (db.waitingPeriodHasElapsed(dataPoint, WAIT_INTERVAL)) {
			Date date = new Date( dataPoint.getDate().getYear(), dataPoint.getDate().getMonth(), dataPoint.getDate().getDate());
			text.setText(date.toString());
			
			// TODO: Textausgabe. Wird die if-Bedingung erfüllt? 
			if ( this.arrayAdapterDaily != null && !this.datapoints.isEmpty() && this.datapoints.get(0).getDate().getTime() >= date.getTime() && this.datapoints.get(0).getDate().getTime() < date.getTime()+86400000) {
				this.datapoints.add(dataPoint);
				Collections.sort(this.datapoints);
				
				arrayAdapterDaily.notifyDataSetChanged();
			}
			
			String formattedDate = Database.DAY_FORMAT.format(date);
			if (!datadates.contains(formattedDate)) {
				this.datadates.add(formattedDate);
				
				text.setText("datadates aktualisiert");
				arrayAdapter.notifyDataSetChanged();
			}

			
			db.write(dataPoint);

//			mapView.loadUrl(StaticMapsURLBuilder.buildUrl(datapoints));

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

	private static boolean waitingPeriodHasElapsed(List<GeoDataPoint> data,	GeoDataPoint newData, long waitingPeriod) {
		if (data.isEmpty())
			return true;
		
		Collections.sort(data);
		long lastUpdate = data.get(data.size() - 1).getDate().getTime();
		long thisUpdate = newData.getDate().getTime();
		return thisUpdate - lastUpdate > waitingPeriod;
	}

}
