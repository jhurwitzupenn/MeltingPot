package foodapp.com.meltingpot;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class PendingRequestInfo extends ListActivity {

    ArrayAdapter adapter;
    ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_request_info);

        user = ParseUser.getCurrentUser();

        // Time
        String timeStr = getIntent().getStringExtra("Time");
        if (timeStr == null) {
            timeStr = user.getString("Time");
        }
        TextView mealTime = (TextView) findViewById(R.id.mealTimeTextView);
        mealTime.setText(timeStr);

        // Ingredients
        String[] ingredientsStrs = getIntent().getStringArrayExtra("Ingredients");
        if (ingredientsStrs == null) {
            ingredientsStrs = fromJSONArray(user.getJSONArray("Ingredients"));
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ingredientsStrs);
        setListAdapter(adapter);
    }

    // use your own menu
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_ingredients, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onListItemClick (ListView l, View v, int position, long id) {
    }

    public void onCancelRequestButtonClick(View view) {
        user.remove("Time");
        user.remove("Ingredients");
        user.put("RequestPending", false);
        user.saveInBackground();

        startActivity(new Intent(this, Profile.class));
    }

    public String[] fromJSONArray(JSONArray jsonArr) {
        try {
            String[] arr = new String[jsonArr.length()];
            for (int i = 0; i < jsonArr.length(); i++) {
                arr[i] = jsonArr.getString(i);
            }
            return arr;
        } catch (Exception e) {
            Log.e("PendingRequestInfo", e.getMessage());
        }
        return new String[0];
    }
}