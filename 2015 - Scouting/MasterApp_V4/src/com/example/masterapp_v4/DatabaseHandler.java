package com.example.masterapp_v4;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

public class DatabaseHandler {
	
	public static String getResponse(String query) {
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		InputStream is = null;
		StringBuilder sb;
		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://www.610.hettex.com/sqlQuery.php ");
			httppost.setHeader("content-type",
					"application/x-www-form-urlencoded");
			nameValuePairs.add(new BasicNameValuePair("query", query));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection" + e.toString());
		}
		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			sb = new StringBuilder();
			sb.append(reader.readLine() + "\n");

			String line = "0";
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			
			return sb.toString();
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}
		return null;
	}


private static String[] fields = { "team_num", "match_num", "scouter_name",
			"auton_totes_moved", "auton_totes_stacked",
			"auton_containers_moved", "moved_to_auton_zone",
			"auton_tote_start_on_field", "stacks_raw_data", "totes_stacked",
			"highest_tote_stacked", "containers_stacked",
			"highest_container_stacked", "num_litter_in_containers",
			"cooperation_set", "cooperation_stack", "helped_in_cooperation",
			"num_fouls", "no_show" };

public static Object[][] getTable(int teamNum, String tournament) {
	String result = getResponse("select * from " + tournament
			+ " where team_num=" + teamNum);
	if (result.indexOf("null") == 0 || result.indexOf("<!") == 0) {
		Object[][] objs = new Object[1][fields.length+1];
		Arrays.fill(objs[0], 0);
		return objs;
	} else {
		try {
			JSONArray rows = new JSONArray(result);
			Object[][] objs = new Object[rows.length()][];
			for (int i = 0; i < rows.length(); i++) {
				JSONArray cols = rows.getJSONObject(i).toJSONArray(
						new JSONArray(Arrays.asList(fields)));
				objs[i] = new Object[cols.length()];
				for (int j = 0; j < cols.length(); j++) {
					objs[i][j] = cols.get(j);
				}
			}
			return objs;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}



}


