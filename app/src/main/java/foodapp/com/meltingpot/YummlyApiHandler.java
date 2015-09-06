package foodapp.com.meltingpot;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
    private final static String URL_BASE = "http://api.yummly.com/v1/api/recipes?";
    private final static String APP_ID = "_app_id=0ec6bbad";
    private final static String APP_KEY = "_app_key=0155d8195451ab1bdc4bd84c4082f948";
    private final static String ALLOWED_INGREDIENT_PARAMETER = "allowedIngredient[]=";
    private final static String AMPERSAND = "&";

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
    }

    private static String configureUrl(List<String> ingredients) throws UnsupportedEncodingException {
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
}
