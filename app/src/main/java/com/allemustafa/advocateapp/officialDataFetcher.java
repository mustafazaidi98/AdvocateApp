package com.allemustafa.advocateapp;

import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class officialDataFetcher {
    private static MainActivity mainActivity;
    private static final String officialAPIUrl = "https://www.googleapis.com/civicinfo/v2/representatives";
    private static final String APIKey = "AIzaSyBhJ35WrcQ0pq9TmfC3M4i8bVvogq43MkQ";
    private static RequestQueue queue;

    public static void downloadOfficials(MainActivity mainActivityIn,
                                         String location) {
        mainActivity = mainActivityIn;
        if (mainActivity.hasNetworkConnection() == false) {
            return;
        }
        queue = Volley.newRequestQueue(mainActivity);

        Uri.Builder buildURL = Uri.parse(officialAPIUrl).buildUpon();
        buildURL.appendQueryParameter("address", location);
        buildURL.appendQueryParameter("key", APIKey);
        String urlToUse = buildURL.build().toString();

        Response.Listener<JSONObject> listener =
                response -> {
                    try {
                        parseJSON(response);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                };

        Response.ErrorListener error =
                error1 -> mainActivity.InvalidLocationSelected(error1);

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, urlToUse,
                        null, listener, error);

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    private static void parseJSON(JSONObject obj) throws JSONException {
        JSONObject normalizedInput = obj.getJSONObject("normalizedInput");
        List<officialClass> officialsList = new ArrayList<officialClass>();
        String col1=", ";
        String col2=", ";
        if(normalizedInput.getString("line1").isEmpty())
            col1="";
        if(normalizedInput.getString("zip").isEmpty())
            col2="";
        String Address = normalizedInput.getString("line1") +col1 +
                normalizedInput.getString("city") + ", " +
                normalizedInput.getString("state") + col2 +
                normalizedInput.getString("zip");
        JSONArray offices = obj.getJSONArray("offices");
        JSONArray officials = obj.getJSONArray("officials");
        for (int i = 0; i < offices.length(); i++) {
            JSONObject office = (JSONObject) offices.get(i);
            String officeName = office.getString("name");
            JSONArray indices = office.getJSONArray("officialIndices");
            for (int j = 0; j < indices.length(); j++) {
                int index = indices.getInt(j);
                JSONObject official = officials.getJSONObject(index);
                String name = official.getString("name");
                String photo = "";
                if(official.has("photoUrl")){
                    photo = official.getString("photoUrl");
                }
                String address = "";
                if(official.has("address")) {
                    JSONObject addressArray = official.getJSONArray("address").getJSONObject(0);
                    if(addressArray.getString("line1").isEmpty())
                        col1="";
                    if(addressArray.getString("zip").isEmpty())
                        col2="";
                    address = addressArray.getString("line1") + col1 +
                            addressArray.getString("city") + ", " +
                            addressArray.getString("state") + col2 +
                            addressArray.getString("zip");
                }
                String party = official.getString("party");
                String website = "";
                String phone = "";
                if (official.has("phones"))
                    phone = official.getJSONArray("phones").getString(0);
                if (official.has("urls"))
                    website = official.getJSONArray("urls").getString(0);
                String email = "";
                if (official.has("emails"))
                email = official.getJSONArray("emails").getString(0);
                String twitter = "";
                String facebook = "";
                String youtube = "";
                if (official.has("channels")) {
                    JSONArray channels = official.getJSONArray("channels");
                    for (int k = 0; k < channels.length(); k++) {
                        if (channels.getJSONObject(k).getString("type").equalsIgnoreCase("Twitter"))
                            twitter = channels.getJSONObject(k).getString("id");
                        if (channels.getJSONObject(k).getString("type").equalsIgnoreCase("Facebook"))
                            facebook = channels.getJSONObject(k).getString("id");
                        if (channels.getJSONObject(k).getString("type").equalsIgnoreCase("Youtube"))
                            youtube = channels.getJSONObject(k).getString("id");
                    }
                }
            officialClass officials1 = new officialClass(officeName,
                    name,
                    address,
                    party,
                    email,
                    photo,
                    facebook,
                    youtube,
                    twitter,
                    phone,
                    website);
                officialsList.add(officials1);
            }
        }
        AppDataClass appData = new AppDataClass(officialsList,Address);
        mainActivity.UpdateData(appData);
    }
}
