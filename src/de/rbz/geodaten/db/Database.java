/**
 * 
 */
package de.rbz.geodaten.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import de.rbz.geodaten.gps.GeoDataPoint;

/**
 * @author l_admin
 *
 */
public class Database {
	
	private static String tblname = "tbl_geodaten"; 
	
	private final static String CREATE_TABLE_GEODATA = "CREATE TABLE " + tblname + " ("
			+ "timestamp INTEGER PRIMARY KEY NOT NULL, "
			+ "longitude REAL NOT NULL, "
			+ "latitude REAL NOT NULL);";
	
	SQLiteDatabase db;
	
	/**
	 * opens or creates the app's database
	 * 
	 * @param activity
	 */
	public Database(Activity activity) {
		db = activity.openOrCreateDatabase("geodaten_database.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		
		db.setVersion(1);
		db.setLocale(Locale.getDefault());
		db.setLockingEnabled(true);

		// Datenbank existiert
		if ( isTableExists() == false ) {
			db.execSQL(CREATE_TABLE_GEODATA);
		}
	}
	
	/**
	 * Writes a single record to the database.
	 * 
	 * @param dataPoint
	 */
	public void write(GeoDataPoint dataPoint) {
		ContentValues values = new ContentValues();
		values.put("timestamp", dataPoint.getDate().getTime());
		values.put("longitude", dataPoint.getLongitude());
		values.put("latitude",  dataPoint.getLatitude());
		
		db.insertOrThrow(tblname, null, values);			

	}
	
	/**
	 * Checks if the "tbl_geodaten" table exists in the database. 
	 * 
	 * @return
	 */
	public boolean isTableExists() {
	    Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tblname+"'", null);
	    if(cursor!=null) {
	        if(cursor.getCount()>0) {
	        	cursor.close();
	            return true;
	        }
	        cursor.close();
	    }
	    return false;
	}
	
	/**
	 * Reads all records from the database.
	 * 
	 * @return
	 */
	public List<GeoDataPoint> read() {
		Cursor cursor = 
				db.query(
						tblname, 
						new String[] {"timestamp", "longitude", "latitude"},
						null, null, null, null, "timestamp DESC");
		
		cursor.moveToFirst();
		List<GeoDataPoint> list = new ArrayList<GeoDataPoint>();
		
		for(int i = 0; i < cursor.getCount(); i++) {
			long timestamp = cursor.getLong(0);
			double latitude = cursor.getDouble(1);
			double longitude = cursor.getDouble(2);
			Date date = new Date(timestamp);
			
			list.add(new GeoDataPoint(date, latitude, longitude));
			
			cursor.moveToNext();
		}
		
		return list;
		
	}

}
