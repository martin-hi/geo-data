package de.rbz.geodaten;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import de.rbz.geodaten.db.Database;
import de.rbz.geodaten.gps.GeoCalculator;
import de.rbz.geodaten.gps.GeoDataPoint;
import de.rbz.geodaten.gps.GeodatenLocationListener;
import de.rbz.geodaten.gps.StaticMapsURLBuilder;


public class MainActivity extends Activity {

	private ArrayList<GeoDataPoint> dataPoints;
	private List<String> dataDates;
	private LocationManager locationManager;
	private GeodatenLocationListener locationListener;
	private ListView listView;
	private ArrayAdapter<List<String>> arrayAdapter;
	
//	private WebView mapView;

	private Database db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		
		// initialize dataPoints
		dataPoints = new ArrayList<GeoDataPoint>();
		
		// initialize database
		db = new Database(this);
		
		// setup WebView
//		mapView = (WebView) findViewById(R.id.map);
//		mapView.getSettings().setJavaScriptEnabled(true);
		
		// read all records from database
//		dataPoints = db.read();
		
//		dataPoints.add(new GeoDataPoint(54.3232d, 10.1227d));
//		dataPoints.add(new GeoDataPoint(54.3099d, 10.1324d));
//		dataPoints.add(new GeoDataPoint(54.3057d, 10.1631d));
//		Collections.sort(dataPoints);

		
		
		// insert tests
//		if (!db.isTableExists()) {
//			boolean done = db.insertData();
//		}
		
		// read all dates from database
		dataDates = db.readDateString();
		
//		dataDates.add(new Date(2013,06,14));
//		dataDates.add(new Date(2013,06,13));
//		dataDates.add(new Date(2013,06,12));
		//Collections.sort(dataDates);
				
//		if(!dataPoints.isEmpty()) {
//			mapView.loadUrl(StaticMapsURLBuilder.buildUrl(dataPoints));
//		}	
		
		// bind DataDates to ListView
		listView = (ListView) findViewById(R.id.datesList);
		arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, dataDates);
		listView.setAdapter(arrayAdapter);
		
		
		// bind DataPoints to ListView
//		listView = (ListView) findViewById(R.id.locationsList);
//		arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, dataPoints);
//		listView.setAdapter(arrayAdapter);
		
		listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
								
				Date date;
				try {
					date = Database.DAY_FORMAT.parse(dataDates.get(position));
					Intent intent = new Intent();
					intent.setClassName(getPackageName(), getPackageName() + ".DailyActivity");
					Bundle daten = new Bundle();
					daten.putLong("dataDates", date.getTime());
//					daten.putParcelableArrayList("dataPoints", dataPoints);
					intent.putExtras(daten);
					startActivity(intent);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		});
		
		arrayAdapter.notifyDataSetChanged();
		
		TextView text = (TextView) findViewById(R.id.textViewMain);	
//		text.setText(GeoCalculator.calculateCoveredDistance(dataPoints) + " km");
		
		// initialize GPS
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// TODO: mapview übergeben
		locationListener = new GeodatenLocationListener(dataPoints, dataDates, db, arrayAdapter, text/*, mapView*/);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void insertData(View view) {
		db.insertData();
	}
	
	public void insertRecord(View view) {
		GeoDataPoint data = new GeoDataPoint(10.0d, -50.0d);
//		dataPoints.add(data);
//		db.write(data);
//		
//		Date date = new Date(data.getDate().getYear(), data.getDate().getMonth(), data.getDate().getDate());
//		
//		if (!dataDates.contains(date.getTime())) {
//			
//			dataDates.add(date.getTime());
//		}
//		
//		arrayAdapter.notifyDataSetChanged();
		//db.waitingPeriodHasElapsed(data, GeodatenLocationListener.WAIT_INTERVAL, (TextView) findViewById(R.id.textViewMain));
		
	}

}
