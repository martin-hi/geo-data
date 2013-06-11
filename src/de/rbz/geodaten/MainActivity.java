package de.rbz.geodaten;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import de.rbz.geodaten.db.Database;
import de.rbz.geodaten.gps.GeoDataPoint;
import de.rbz.geodaten.gps.GeodatenLocationListener;

public class MainActivity extends Activity {
	
	private List<GeoDataPoint> dataPoints;
	private LocationManager locationManager;
	private GeodatenLocationListener locationListener;
	private ListView txt_locations;
	
	private Database db;
		
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        db = new Database(this);
        
        dataPoints = new ArrayList<GeoDataPoint>();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);                     
        locationListener = new GeodatenLocationListener(dataPoints, db);        
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        
		txt_locations = (ListView)findViewById(R.id.locationsList);
		ListAdapter listAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, dataPoints);
		
		locationListener.setViewObjects(listAdapter, txt_locations);
		
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void click(View view) {
	
    }
    
}
