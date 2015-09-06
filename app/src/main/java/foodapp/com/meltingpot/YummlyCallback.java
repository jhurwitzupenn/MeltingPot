package foodapp.com.meltingpot;

import org.json.JSONObject;

/**
 * Created by martingreenberg on 9/6/15.
 */
public interface YummlyCallback {
    void result(JSONObject j);
}