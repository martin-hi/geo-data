package de.rbz.geodaten;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import de.rbz.geodaten.db.Database;
import de.rbz.geodaten.gps.GeoCalculator;
import de.rbz.geodaten.gps.GeoDataPoint;
import de.rbz.geodaten.gps.GeodatenLocationListener;
import de.rbz.geodaten.gps.StaticMapsURLBuilder;

public class DailyActivity extends Activity {

	private List<GeoDataPoint> dataPoints;
	private TextView text;
	private ListView listView;
	private ArrayAdapter<List<GeoDataPoint>> arrayAdapter;
	
	private WebView mapView;
	
	private Database db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daily);
		
		
		
		Bundle daten = getIntent().getExtras();
//		dataPoints = daten.getParcelableArrayList("dataPoints");
		
		long longDate = daten.getLong("dataDates");
		
		text = (TextView) findViewById(R.id.textViewDaily);
		
		// initialize database
		db = new Database(this);
		
		// setup WebView
		mapView = (WebView) findViewById(R.id.map);
		mapView.getSettings().setJavaScriptEnabled(true);
		
		// read all records from database
//		dataPoints = new ArrayList<GeoDataPoint>();
		dataPoints = db.read(longDate);
		Collections.sort(dataPoints);

		if(!dataPoints.isEmpty()) {
			mapView.loadUrl(StaticMapsURLBuilder.buildUrl(dataPoints));
		}	
		
		// bind DataPoints to ListView
		listView = (ListView) findViewById(R.id.locationsList);
		arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, dataPoints);
		listView.setAdapter(arrayAdapter);
		GeodatenLocationListener.INSTANCE.setDailyAdapter(arrayAdapter);
		arrayAdapter.notifyDataSetChanged();
		
		String ausgabe = "";
		Date date = new Date(longDate);
		// TODO: Datum für den Montag wird falsch angezeigt
		ausgabe = "Am " + Database.DAY_FORMAT.format(date) + " hast du ";
				
		DecimalFormat f = new DecimalFormat("#0.00");
		ausgabe += f.format(GeoCalculator.calculateCoveredDistance(dataPoints)) + " km zurückgelegt.";
		
		text.setText(ausgabe);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.daily, menu);
		return true;
	}

	public void newGeoDataPoint (View view) {
		
		GeoDataPoint data = new GeoDataPoint(20.0d, -20.0d);
		dataPoints.add(data);
		db.write(data);
		
		Date date = new Date(data.getDate().getDate(), data.getDate().getMonth(), data.getDate().getYear());
		arrayAdapter.notifyDataSetChanged();
	}
	
}
