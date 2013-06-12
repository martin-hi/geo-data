package de.rbz.geodaten;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import de.rbz.geodaten.db.Database;
import de.rbz.geodaten.gps.GeoDataPoint;
import de.rbz.geodaten.gps.GeodatenLocationListener;


/*
 * 
 * https://maps.google.at/maps?saddr=54.3252,10.1405&daddr=53.5534,9.9921+to:52.5234,13.4113+to:50.1115,+8.6805&hl=de&dirflg=w&z=7
 * 
 * 
 */

public class MainActivity extends Activity {

	private List<GeoDataPoint> dataPoints;
	private LocationManager locationManager;
	private GeodatenLocationListener locationListener;
	private ListView listView;
	private ArrayAdapter<List<GeoDataPoint>> arrayAdapter;
	
	private WebView mapView;

	private Database db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// initialize database
		db = new Database(this);
		
		// setup WebView
		mapView = (WebView) findViewById(R.id.map);
		mapView.getSettings().setJavaScriptEnabled(true);
		
		// read all records from database
		dataPoints = db.read();
		Collections.sort(dataPoints);

		// bind DataPoints to ListView
		listView = (ListView) findViewById(R.id.locationsList);
		arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, dataPoints);
		listView.setAdapter(arrayAdapter);
		arrayAdapter.notifyDataSetChanged();

		// initialize GPS
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new GeodatenLocationListener(dataPoints, db, arrayAdapter, mapView);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
