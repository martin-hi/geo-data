package de.rbz.geodaten;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import de.rbz.geodaten.db.Database;
import de.rbz.geodaten.gps.GeoDataPoint;
import de.rbz.geodaten.gps.GeodatenLocationListener;


public class MainActivity extends Activity {

	private ArrayList<GeoDataPoint> dataPoints;
	private List<String> dataDates;
	private LocationManager locationManager;
	private GeodatenLocationListener locationListener;
	private ListView listView;
	private ArrayAdapter<List<String>> arrayAdapter;
	
	private Database db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// initialize dataPoints
		dataPoints = new ArrayList<GeoDataPoint>();
		
		// initialize database
		db = new Database(this);
				
		
		// insert tests
//		if (!db.isTableExists()) {
//			boolean done = db.insertData();
//		}
		
		// read all dates from database
		dataDates = db.readDateString();

		
		// bind DataDates to ListView
		listView = (ListView) findViewById(R.id.datesList);
		arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, dataDates);
		listView.setAdapter(arrayAdapter);
		
		
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
					intent.putExtras(daten);
					startActivity(intent);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		});
		
		arrayAdapter.notifyDataSetChanged();
				
		// initialize GPS
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new GeodatenLocationListener(dataPoints, dataDates, db, arrayAdapter);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
}
