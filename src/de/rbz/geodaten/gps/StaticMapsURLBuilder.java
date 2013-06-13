/*
 * 
 */
package de.rbz.geodaten.gps;

import java.util.List;

/**
 * @author l_admin
 *
 */
public class StaticMapsURLBuilder {
	
	
	private static final String BASE_URL = "https://maps.googleapis.com/maps/api/staticmap?maptype=mobile/&sensor=true";
	private static final String PARAM_SIZE = "&size=";
	private static final String X = "x";
	private static final String PARAM_MARKERS = "&markers=";
	private static final String PARAM_PATH = "&path=";
	private static final String COMMA = ",";
	private static final String PIPE = "|";
	
	// https://maps.googleapis.com/maps/api/staticmap?size=512x512&maptype=mobile/&markers=54.3057,10.1631|54.3068,10.1487&sensor=true&path=54.3070,10.1537|54.3066,10.1582
	

	public static String buildUrl(List<GeoDataPoint> data) {
		StringBuilder url = new StringBuilder(BASE_URL);
		url.append(PARAM_SIZE);
		url.append(512);
		url.append(X);
		url.append(512);
		
		if(!data.isEmpty()) {
			GeoDataPoint point = data.get(0);
			url.append(PARAM_MARKERS);
			url.append(point.getLatitude());
			url.append(COMMA);
			url.append(point.getLongitude());
		}
		
		if(data.size() > 1) {
			GeoDataPoint point = data.get(data.size()-1);
			url.append(PIPE);
			url.append(point.getLatitude());
			url.append(COMMA);
			url.append(point.getLongitude());
			
			url.append(PARAM_PATH);
			
			for(int i = 0; i < data.size(); i++) {
				point = data.get(i);
				
				if(i != 0) {
					url.append(PIPE);
				}
				
				url.append(point.getLatitude());	
				url.append(COMMA);	
				url.append(point.getLongitude());
			}
		}
		
		return url.toString();
		
	}
	
			
}
