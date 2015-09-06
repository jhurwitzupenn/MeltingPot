package foodapp.com.meltingpot;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

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
    private static JSONArray results;
    private final static String URL_BASE = "http://api.yummly.com/v1/api/recipes?";
    private final static String APP_ID = "_app_id=0ec6bbad";
    private final static String APP_KEY = "_app_key=0155d8195451ab1bdc4bd84c4082f948";
    private final static String ALLOWED_INGREDIENT_PARAMETER = "allowedIngredient[]=";
    private final static String REQUIRE_PICS = "requirePictures=true";
    private final static String AMPERSAND = "&";
    private static boolean has_requested;

    public static void makeYummlyRequest(List<String> ingredients, final Activity activity) {
        String url = null;
        try {
            url = configureUrl(ingredients);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Unable to encode in UTF-8 format", e);
        }
        Log.d("MELTING", url);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        json = response;
                        Log.d("MELTING", json.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("MELTING", "Yummly Error response" + error.getLocalizedMessage());
                    }
                });
        Volley.newRequestQueue(activity).add(jsObjRequest);
        has_requested = true;
    }

    private static String configureUrl(List<String> ingredients) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder(URL_BASE + APP_ID + AMPERSAND + APP_KEY + AMPERSAND +
        REQUIRE_PICS + AMPERSAND);
        for (int i = 0; i < ingredients.size(); i ++) {
            sb.append(ALLOWED_INGREDIENT_PARAMETER);
            sb.append(ingredients.get(i));
            if(i < ingredients.size() - 1) {
                sb.append("AMPERSAND");
            }
        }
        return sb.toString();
    }

    // call this first before using below methods
    public static void generateJSONResults() {
        if (has_requested) {
            try {
                results = json.getJSONArray("matches");

            } catch (JSONException e) {
                Log.d("MELTING", "JSON Parsing Error" + e.getLocalizedMessage());
            }
        }
    }

    public static List<String> getRecipeNames() {
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
    public static List<List<String>> getIngredients() {
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

    public static List<List<String>> getImageUrls() {
        List<List<String>> l = new ArrayList<>();
        for (int i = 0; i < results.length(); i++) {
            try {
                l.add(convertJSONArrayToList(results.getJSONObject(i).getJSONArray("smallImageUrls")));
            }
            catch(JSONException e) {
                Log.d("MELTING", "JSON Parsing Error" + e.getLocalizedMessage());
            }
        }
        return l;
    }

    public static List<Integer> getRatings() {
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
        results = null;
        has_requested = false;
    }
}
