/**
 * 
 */
package de.rbz.geodaten.db;

import java.text.SimpleDateFormat;
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
	
	
	public static final SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
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
	 * Reads all dates from the database.
	 * @return 
	 * 
	 * @return
	 */
	public List<String> readDateString() {	
		
		Cursor cursor = 
				db.query(tblname, 
						 new String[] {"timestamp"},
						 null, null, null, null, "timestamp DESC");
		
		cursor.moveToFirst();
		List<String> list = new ArrayList<String>();
		
		for(int i = 0; i < cursor.getCount(); i++) {
			long date = cursor.getLong(0);
			Date datum = new Date(date);
						
			String formattedDate = DAY_FORMAT.format(datum.getTime());
			
			if ( !list.contains(formattedDate))  {
				list.add(formattedDate);
			}
			cursor.moveToNext();
		}
		cursor.close();
		return list;
		
	}
	
	
	/**
	 * Reads all records from the database.
	 * 
	 * @return
	 */
	public List<GeoDataPoint> read(Long longDate) {
		Cursor cursor = 
				db.query(
						tblname, 
						new String[] {"timestamp", "latitude", "longitude"},
						null, null, null, null, "timestamp DESC");
		
		cursor.moveToFirst();
		
		List<GeoDataPoint> list = new ArrayList<GeoDataPoint>();
		
		for(int i = 0; i < cursor.getCount(); i++) {
			long timestamp = cursor.getLong(0);
			
			if ( timestamp >= longDate && timestamp < (longDate + 86400000) ) { 
				double latitude = cursor.getDouble(1);
				double longitude = cursor.getDouble(2);
				Date date = new Date(timestamp);
			
				list.add(new GeoDataPoint(date, latitude, longitude));
			}
			cursor.moveToNext();
		}
		
		cursor.close();
		return list;
		
	}
	
	/**
	 * create test data
	 * 
	 * @return
	 */
	public boolean insertData() {
		ContentValues values = new ContentValues();
		values.put("timestamp", new Date().getTime()-86400000*3);
		values.put("latitude", 54.3232d);
		values.put("longitude",  10.1227d);
		
		db.insertOrThrow(tblname, null, values);
		
		values = new ContentValues();
		values.put("timestamp", new Date().getTime() - 86400000*3-100000);
		values.put("latitude", 54.3099d);
		values.put("longitude",  10.1324d);
		
		db.insertOrThrow(tblname, null, values);	

		values = new ContentValues();
		values.put("timestamp", new Date().getTime() - 200000 - 86400000*3);
		values.put("latitude", 54.3057d);
		values.put("longitude",  10.1631d);
		
		db.insertOrThrow(tblname, null, values);	

		// gestern
		values = new ContentValues();
		values.put("timestamp", new Date().getTime() - 86400000);
		values.put("latitude", 54.3232d);
		values.put("longitude",  10.1227d);
		
		db.insertOrThrow(tblname, null, values);
		
		values = new ContentValues();
		values.put("timestamp", new Date().getTime() - 86400000 - 100000);
		values.put("latitude", 54.3099d);
		values.put("longitude",  10.1324d);
		
		db.insertOrThrow(tblname, null, values);	

		// vorgestern
		values = new ContentValues();
		values.put("timestamp", new Date().getTime() - 2 * 86400000);
		values.put("latitude", 54.3057d);
		values.put("longitude",  10.1631d);
		
		db.insertOrThrow(tblname, null, values);	
		
		return true;
	}
	
	/**
	 * checks if enough time has passed since the last record was saved
	 * 
	 * @param newData
	 * @param period
	 * @return
	 */
	public boolean waitingPeriodHasElapsed(GeoDataPoint newData, long period) {
		Cursor cursor = db.query(tblname, new String[]{"MAX(timestamp)"}, null, null, null, null, null);
		cursor.moveToFirst();
				
		long maxTime = cursor.getLong(0);
		
		cursor.close();		
		
		return maxTime + period < newData.getDate().getTime();		
	}

}
