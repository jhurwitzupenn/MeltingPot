package foodapp.com.meltingpot;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class AddIngredients extends ListActivity {
    EditText newIngredient;
    EditText mealTime;

    protected static int selectUpdateRequestCode = 0;

    //List of Updates
    List<String> ingredientsList = new ArrayList<String>();
    String[] ingredientsStrs = new String[0];

    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredients);

        newIngredient = (EditText) findViewById(R.id.addIngredientEditText);
        mealTime = (EditText) findViewById(R.id.mealTimeEditText);

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

    public void onAddIngredientButtonClick(View view) {
        String ingredient = newIngredient.getText().toString();
        if (ingredient != null && !ingredientsList.contains(ingredient)) {
            ingredientsList.add(ingredient);
            updateStringArray();
        }
        newIngredient.setText("");
    }

    public void onFindMatchButtonClick(View view) {
        Intent myIntent = new Intent(this, PendingRequestInfo.class);
        myIntent.putExtra("Ingredients", ingredientsStrs);
        myIntent.putExtra("Time", mealTime.getText().toString());
        startActivity(myIntent);
    }
}