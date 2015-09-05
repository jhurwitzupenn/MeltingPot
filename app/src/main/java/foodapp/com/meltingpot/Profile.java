package foodapp.com.meltingpot;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class Profile extends ListActivity {
    ImageView profilePic;
    TextView name;
    TextView location;

    // Collaborator that the user selects
    ParseUser collaborator;

    //List of Collaborators
    List<ParseUser> collaboratorList = new ArrayList<>();
    String[] collaboratorStrs = new String[0];

    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profilePic = (ImageView) findViewById(R.id.profileImageView);
        name = (TextView) findViewById(R.id.nameTextView);
        location = (TextView) findViewById(R.id.locationTextView);

        ParseUser user = ParseUser.getCurrentUser();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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

    public void onStartCookingButtonClick(View view) {
        Intent myIntent = new Intent(this, AddIngredients.class);
        startActivity(myIntent);
    }

    protected void onListItemClick (ListView l, View v, int position, long id) {
        collaborator = collaboratorList.get(position);
        selectCollaborator();
    }

    public void selectCollaborator() {
        // TODO: maybe show collaborator profile
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    protected void updateStringArray() {
        collaboratorStrs = new String[collaboratorList.size()];

        for (int i = 0; i < collaboratorList.size(); i++) {
            collaboratorStrs[i] = collaboratorList.get(i).getString("Name");
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, collaboratorStrs);
        setListAdapter(adapter);
    }
}
