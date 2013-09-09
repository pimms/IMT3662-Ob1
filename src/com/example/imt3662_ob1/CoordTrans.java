package com.example.imt3662_ob1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.*;

public class CoordTrans {
	public static final int TRANSLATE_FAILURE = 0;
	public static final int TRANSLATE_SUCCESS = 1;
	
	
	/* The address is translated via Google's geo-coding webAPI
	 * in a background thread, and sent to the CoordinateT.
	 * The background work is performed by an instance of "TranslateWorker".
	 */
	public void translateLocation(Location location, Handler handler) {
		Thread t = new Thread(new TranslateWorker(location, handler));
		t.start();
	}
	
	/* Private class used to translate the GPS coordinates to 
	 * a human-readable street-address.
	 */
	private class TranslateWorker implements Runnable {
		private Location location;
		private Handler handler;
		
		public TranslateWorker(Location loc, Handler hnd) {
			location = loc;
			handler = hnd;
		}
		
		@Override
		public void run() {
			Message msg = handler.obtainMessage();
			
			String rawJson = getRawJson();
			if (rawJson == null) {
				msg.what = TRANSLATE_FAILURE;
				msg.obj = "Could not connect to googleapis.com";
			} else {
				String result = getAddress(rawJson);
				msg.what = TRANSLATE_SUCCESS;
				msg.obj = result;
			}
			
			handler.sendMessage(msg);
		}
		
		private String getAddress(String rawJson) {
			JSONObject json;
			
			try {
				json = new JSONObject(rawJson);
				
				String status = (String)json.get("status");
				if (status.equals("ZERO_RESULTS")) {
					return "Uncharted Territory";
				} else if (status.equals("OK")) {
					JSONArray array = json.getJSONArray("results");
					return array.getJSONObject(0).getString("formatted_address");
				} else {
					return "Weird status: \"" + status + "\"";
				}
			} catch (JSONException e) {
				return "Exceptions! Exceptions everywhere!";
			}
		}
		
		private String getRawJson() {
			double lat = location.getLatitude();
			double lon = location.getLongitude();
			
			StringBuilder sb = new StringBuilder();
			sb.append("http://maps.googleapis.com/maps/api/geocode/json?latlng=");
			sb.append(lat).append(",");
			sb.append(lon).append("&sensor=true");
			
			StringBuilder content = new StringBuilder();
			
			try {
				URL url = new URL(sb.toString());
				InputStreamReader isr = new InputStreamReader(url.openStream());
				BufferedReader br = new BufferedReader(isr);
				
				String line = br.readLine();
				while (line != null) {
					content.append(line);
					line = br.readLine();
				}
			} catch (IOException e) {
				Log.e("DBG", e.getMessage());
			}
			
			return content.toString();
		}
	}
}
