package foodapp.com.meltingpot;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PendingRequestInfo extends ListActivity {

    //List of Ingredients
    String[] ingredientsStrs = new String[0];

    TextView mealTime;

    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_request_info);

        mealTime = (TextView) findViewById(R.id.mealTimeTextView);
        mealTime.setText(getIntent().getStringExtra("Time"));

        ingredientsStrs = getIntent().getStringArrayExtra("Ingredients");
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
        startActivity(new Intent(this, Profile.class));
    }
}