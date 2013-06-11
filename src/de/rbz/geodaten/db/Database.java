/**
 * 
 */
package de.rbz.geodaten.db;

import java.util.Locale;

import de.rbz.geodaten.gps.GeoDataPoint;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
	
	public void write(GeoDataPoint dataPoint) {
		ContentValues values = new ContentValues();
		values.put("timestamp", dataPoint.getDate().getTime());
		values.put("longitude", dataPoint.getLongitude());
		values.put("latitude",  dataPoint.getLatitude());
		
		try {
			db.insertOrThrow(tblname, null, values);			
		} catch (Exception e) {
			// TODO: catch code
		}
	}
	
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

}
