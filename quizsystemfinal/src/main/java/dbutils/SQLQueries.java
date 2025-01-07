/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dbutils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author MikeRD
 */
public class SQLQueries {

    public static String sendSQLQuery(String query) {
        StringBuilder response = null;

        // Define the URL of the PHP script
        return sendSQLQuery(query, "https://rhhscs.com/database/dbaccess.php");

    }

    public static String sendSQLQuery(String query, String apiUrl) {
        StringBuilder response = null;
        try {
            // Define the URL of the PHP script
            //    String apiUrl = "https://rhhscs.com/database/dbaccess.php";

            //String query = "INSERT INTO Persons VALUES (NULL,'Last','First','Add','Cit')";
            String password = "MRRD";

            // Create JSON payload (what gets sent with the http request
            String jsonPayload = "{\"query\": \"" + query + "\", \"password\": \"" + password + "\"}";

            // Create HTTP POST request
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Write JSON payload to the connection
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(jsonPayload);
            writer.flush();
            System.out.println(connection.getResponseMessage());
            // Read response from the connection and append it to a single string
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            // Close connections
            writer.close();
            reader.close();

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //parse JSON as Arraylist of Maps (key, value)
    public static ArrayList parseJSONMap(String jsonResponse) {
        //create the arraylist
        ArrayList<Map<String, Object>> dataList = new ArrayList<>();
        //parse string into JSONArray
        JSONArray jsonArray = new JSONArray(jsonResponse); // Assuming 'response' is your JSON string
        //for each item
        for (int i = 0; i < jsonArray.length(); i++) {
            //get the object
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            //create a new map
            Map<String, Object> map = new HashMap<>();

            // Iterate over keys and put them into the map
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                map.put(key, jsonObject.get(key));
            }
            //add to arraylist
            dataList.add(map);
        }
        return dataList;
    }

  public static ArrayList<List<Object>> parseJSONList(String jsonResponse) {
    ArrayList<List<Object>> dataList = new ArrayList<>();

    JSONArray jsonArray = new JSONArray(jsonResponse);

    for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject jsonObject = jsonArray.getJSONObject(i);
        List<Object> row = new ArrayList<>();

        // Iterate over keys and put them into the row
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            System.out.println(key);
            Object value = jsonObject.get(key);

            // Check if the value is JSONObject.NULL
            if (JSONObject.NULL.equals(value)) {
                row.add(null); // Add null to the row
            } else if (value instanceof Integer) {
                row.add(value); // Add Integer directly
            } else if (value instanceof Double) {
                row.add(value); // Add Double directly
            } else if (value instanceof String) {
                String strValue = (String) value;
                // Check if it's a valid integer or decimal number
                if (strValue.matches("-?\\d+")) {
                    row.add(Integer.parseInt(strValue));
                } else if (strValue.matches("-?\\d+(\\.\\d+)?")) {
                    row.add(Double.parseDouble(strValue));
                } else {
                    row.add(strValue);
                }
            } else {
                // Add other types if necessary
                row.add(value); // Add any other types directly
            }
        }
        dataList.add(row);
    }
    return dataList;
}
}

