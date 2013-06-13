/**
 * 
 */
package de.rbz.geodaten.gps;

import java.util.List;

import android.widget.TextView;

/**
 * @author l_admin
 *
 */
public class MapsUrlBuilder {
	
	
	private static final String URL_BASE = "https://maps.google.de/maps?hl=de&dirflg=w&z=7&source=embed&f=q";
	private static final String START_PARAM = "&saddr=";
	private static final String DESTINATION_PARAM = "&daddr=";
	private static final String TO_PARAM = "+to:";
	private static final String COMMA = ",";
	

	// https://maps.google.at/maps?saddr=54.3252,10.1405&daddr=53.5534,9.9921+to:52.5234,13.4113+to:50.1115,8.6805&hl=de&dirflg=w&z=7
	
	public static String buildURL(List<GeoDataPoint> data, TextView text) {
		
		StringBuilder url = new StringBuilder(URL_BASE);
		
		String debug = String.valueOf(data.size());
		
		for(int i = data.size()-1; i >= 0; i--) {
			GeoDataPoint point = data.get(i);
			debug += "|" + i;  
			if(i == data.size()-1) {
				url.append(START_PARAM);
			}else if(i == data.size()-2) {
				url.append(DESTINATION_PARAM);				
			}else {
				url.append(TO_PARAM);
			}
			
			url.append(point.getLatitude());
			url.append(COMMA);
			url.append(point.getLongitude());

		}
		text.setText(debug);
		
		return url.toString();
		
	}
}
