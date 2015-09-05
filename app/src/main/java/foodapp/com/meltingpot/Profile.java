package foodapp.com.meltingpot;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.parse.ParseUser;

import java.io.IOException;
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

        name.setText(user.getString("Name"));

        // Location
        Address address = getLocation(user);
        String cityName = null;
        if (address != null) {
            cityName = address.getLocality();
        }
        if (cityName == null) {
            cityName = "Could not determine location";
        }
        location.setText(cityName);
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
        startActivity(new Intent(this, AddIngredients.class));
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

    private Address getLocation(ParseUser user) {
        Context context = this.getApplicationContext();

        GoogleApiClient apiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .build();
        Location currLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
        Double latitude = null;
        Double longitude = null;
        if (currLocation != null) {
            latitude = currLocation.getLatitude();
            longitude = currLocation.getLongitude();
        } else {
            latitude = user.getDouble("Latitude");
            longitude = user.getDouble("Longitude");
        }

        Geocoder geocoder = new Geocoder(context);
        try {
            return geocoder.getFromLocation(latitude, longitude, 1).get(0);
        } catch (IOException e) {
            Log.e("Profile", e.getMessage());
        }

        return null;
    }
}
