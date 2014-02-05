package scoutingcenter;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PhpRequest {

    static ArrayList<String> fields;

    public static String getResponse(String query) {
        try {
            String url = "http://610stats.comuv.com/sqlQuery.php";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            //add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String urlParameters = "query=" + query;

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString());
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int[] getTeams() {
        JSONArray jArray;
        String result = getResponse("select * from tournament1");
        try {
            jArray = new JSONArray(result);
            JSONObject json_data;
            int[] teams = new int[jArray.length()];
            for (int i = 0; i < jArray.length(); i++) {
                json_data = jArray.getJSONObject(i);
                teams[i] = Integer.parseInt(json_data.getString("team_num"));
            }
            return teams;
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    public static Object[][] getTable(String table) {
        if (fields == null) {
            fields = new ArrayList();
            fields.add("match_num");
            fields.add("scouter_name");
            fields.add("num_cycles");
            fields.add("num_assist");
            fields.add("num_catches");
            fields.add("truss_goals");
            fields.add("high_goal");
            fields.add("low_goal");
            fields.add("high_goal_misses");
            fields.add("low_goal_misses");
            fields.add("defense_rating");
            fields.add("auton_high_goal");
            fields.add("auton_low_goal");
            fields.add("auton_miss");
            fields.add("auton_high_goal_hot");
            fields.add("auton_no_score");
            fields.add("auton_mobility");
            fields.add("blocked_shots");
            fields.add("start_in_goalie_zone");
            fields.add("no_show");
            fields.add("shooter");
            fields.add("intake");
            fields.add("herder");
            fields.add("goalie");
            fields.add("catcher");
        }
        String result = "";
        try {
            result = getResponse("select * from " + table + "");
        } catch (Exception ex) {
            Logger.getLogger(PhpRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            JSONArray rows = new JSONArray(result);
            Object[][] objs = new Object[rows.length()][];
            for (int i = 0; i < rows.length(); i++) {
                JSONArray cols = rows.getJSONObject(i).toJSONArray(
                        new JSONArray(fields));
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