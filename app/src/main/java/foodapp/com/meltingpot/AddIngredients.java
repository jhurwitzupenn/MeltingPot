package foodapp.com.meltingpot;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class AddIngredients extends ListActivity {

    protected static int selectUpdateRequestCode = 0;

    //List of Updates
    List<String> ingredientsList = new ArrayList<String>();
    String[] ingredientsStrs = new String[0];

    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredients);

        /*// get health updates
        ParseQuery<ParseObject> foodQuery = ParseQuery.getQuery("FoodUpdate");
        foodQuery.setLimit(30);
        foodQuery.orderByAscending("date");
        ParseQuery<ParseObject> healthQuery = ParseQuery.getQuery("HealthUpdate");
        healthQuery.setLimit(30);
        healthQuery.orderByAscending("date");
        ParseQuery<ParseObject> medicationQuery = ParseQuery.getQuery("MedicationUpdate");
        medicationQuery.setLimit(30);
        medicationQuery.orderByAscending("date");
        if (ParseUser.getCurrentUser().get("Role").equals("Foster Parent")) {
            ArrayList<Integer> dogIdList = new ArrayList<Integer>();
            // get dogs
            try {
                UserMetaData metaData = (UserMetaData) ParseQuery.getQuery("UserMetaData")
                        .get(ParseUser.getCurrentUser().getString("MetaData"));
                List<String> dogObjIds = metaData.getList("DogList");

                for (String id : dogObjIds) {
                    Dog dog = (Dog) ParseQuery.getQuery("Dog").get(id);
                    dogIdList.add(dog.getIdNumber());
                }
            } catch (Exception e) {
                Log.d("MyDogsActivity", e.getMessage());
            }

            foodQuery.whereContainedIn("dogId", dogIdList);
            healthQuery.whereContainedIn("dogId", dogIdList);
            medicationQuery.whereContainedIn("dogId", dogIdList);
        }

        try {
            for (ParseObject o : foodQuery.find()) {
                updatesList.add((FoodUpdate) o);
            }
            for (ParseObject o : healthQuery.find()) {
                updatesList.add((HealthUpdate) o);
            }
            for (ParseObject o : medicationQuery.find()) {
                updatesList.add((MedicationUpdate) o);
            }
        } catch (Exception e) {
            Log.d("DashboardActivity", e.getMessage());
        }*/

        updateStringArray();
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

    protected void updateStringArray() {
        ingredientsStrs = new String[ingredientsList.size()];

        for (int i = 0; i < ingredientsList.size(); i++) {
            ingredientsStrs[i] = ingredientsList.get(i);
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ingredientsStrs);
        setListAdapter(adapter);
    }
}