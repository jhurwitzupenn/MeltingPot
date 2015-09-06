package foodapp.com.meltingpot;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Formats yummly HttpRequests
 * API Authentication stuff
 * Create Yummly URL
 * Check nearby people’s location and time from database
 * Retrieve nearby people’s ingredients from database
 * For each nearby person:
 * Modify url to include ingredients search (both yours and nearby person’s ingredients)
 * Make JsonObjectRequest https://developer.android.com/training/volley/request.html
 * Read JSON response, (get recipe name, ingredients list and best rating)
 * Sort (or rank select) best ratings
 * Store results of pairings for previous collaborations
 */
public class YummlyApiHandler {
    public static JSONObject json;
    private final static String URL_BASE = "http://api.yummly.com/v1/api/recipes?";
    private final static String APP_ID = "_app_id=0ec6bbad";
    private final static String APP_KEY = "_app_key=0155d8195451ab1bdc4bd84c4082f948";
    private final static String ALLOWED_INGREDIENT_PARAMETER = "allowedIngredient[]=";
    private final static String AMPERSAND = "&";

    public static void makeYummlyRequest(List<String> ingredients, final Context context, final YummlyCallback y
            ) {
        String url = configureUrl(ingredients);

        Log.d("MELTING", url);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        json = response;
                        y.result(response);
                        Log.d("MELTING", json.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("MELTING", "Yummly Error response" + error.getLocalizedMessage());
                    }
                });
        Volley.newRequestQueue(context).add(jsObjRequest);
        // timeout after 10 seconds, blocks other threads
        /**
        String url = configureUrl(ingredients);
        Log.d("MELTING", url);
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(url, null, future, future);
        Volley.newRequestQueue(activity).add(request);
        try {
            json = future.get(10, TimeUnit.SECONDS);
            has_requested = true;
            Log.d("MELTING", "success");
        } catch (InterruptedException e) {
            // exception handling
        } catch (ExecutionException e) {
            // exception handling
        } catch(TimeoutException e) {
            Log.d("MELTING", "timeout");

        }
         */
    }

    private static String configureUrl(List<String> ingredients) {
        StringBuilder sb = new StringBuilder(URL_BASE + APP_ID + AMPERSAND + APP_KEY + AMPERSAND);
        for (int i = 0; i < ingredients.size(); i ++) {
            sb.append(ALLOWED_INGREDIENT_PARAMETER);
            sb.append(ingredients.get(i));
            if(i < ingredients.size() - 1) {
                sb.append("AMPERSAND");
            }
        }
        return sb.toString();
    }

    // use to extract hte matches from the public json object variable after requests
    public static JSONArray results(JSONObject j) {
        try {
            return j.getJSONArray("matches");

        } catch (JSONException e) {
            Log.d("MELTING", "JSON Parsing Error" + e.getLocalizedMessage());
        }
        return null;
    }

    public static List<String> getRecipeNames(JSONArray results) {
        List<String> l = new ArrayList<>();
        for (int i = 0; i < results.length(); i++) {
            try {
                l.add(results.getJSONObject(i).getString("recipeName"));
            }
            catch(JSONException e) {
                Log.d("MELTING", "JSON Parsing Error" + e.getLocalizedMessage());
            }
        }
        return l;
    }
    public static List<List<String>> getIngredients(JSONArray results) {
        List<List<String>> l = new ArrayList<>();
        for (int i = 0; i < results.length(); i++) {
            try {
                l.add(convertJSONArrayToList(results.getJSONObject(i).getJSONArray("ingredients")));
            }
            catch(JSONException e) {
                Log.d("MELTING", "JSON Parsing Error" + e.getLocalizedMessage());
            }
        }
        return l;
    }

    public static List<String> getRecipeUrls(JSONArray results) {
        List<String> l = new ArrayList<>();
        for (int i = 0; i < results.length(); i++) {
            try {
                l.add(results.getJSONObject(i).getString("sourceDisplayName"));
            }
            catch(JSONException e) {
                Log.d("MELTING", "JSON Parsing Error" + e.getLocalizedMessage());
            }
        }
        return l;
    }

    public static List<Integer> getRatings(JSONArray results) {
        List<Integer> l = new ArrayList<>();
        for (int i = 0; i < results.length(); i++) {
            try {
                l.add(results.getJSONObject(i).getInt("rating"));
            }
            catch(JSONException e) {
                Log.d("MELTING", "JSON Parsing Error" + e.getLocalizedMessage());
            }
        }
        return l;
    }

    //http://stackoverflow.com/questions/3395729/convert-json-array-to-normal-java-array
    private static List<String> convertJSONArrayToList(JSONArray a) {
        ArrayList<String> list = new ArrayList<>();
        if (a != null) {
            int len = a.length();
            for (int i=0;i<len;i++){
                try {
                    list.add(a.get(i).toString());
                }
                catch(JSONException e) {
                    Log.d("MELTING", "JSON Parsing Error" + e.getLocalizedMessage());
                }
            }
        }
        return list;
    }

    //please clean after done with current list of ingredients
    public void cleanJSON() {
        json = null;
    }
}
