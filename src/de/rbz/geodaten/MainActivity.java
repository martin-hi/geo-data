package de.rbz.geodaten;

import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import de.rbz.geodaten.db.Database;
import de.rbz.geodaten.gps.GeoCalculator;
import de.rbz.geodaten.gps.GeoDataPoint;
import de.rbz.geodaten.gps.GeodatenLocationListener;
import de.rbz.geodaten.gps.StaticMapsURLBuilder;


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
		
		dataPoints.add(new GeoDataPoint(54.3232d, 10.1227d));
		dataPoints.add(new GeoDataPoint(54.3099d, 10.1324d));
		dataPoints.add(new GeoDataPoint(54.3057d, 10.1631d));
		Collections.sort(dataPoints);
		
		if(!dataPoints.isEmpty()) {
			mapView.loadUrl(StaticMapsURLBuilder.buildUrl(dataPoints));
		}	
		

		// bind DataPoints to ListView
		listView = (ListView) findViewById(R.id.locationsList);
		arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, dataPoints);
		listView.setAdapter(arrayAdapter);
		arrayAdapter.notifyDataSetChanged();
		
		TextView text = (TextView) findViewById(R.id.textView1);		
		
		text.setText(GeoCalculator.calculateCoveredDistance(dataPoints) + " km");
		
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
