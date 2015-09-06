package foodapp.com.meltingpot;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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

import com.facebook.*;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        Bundle params = new Bundle();
        params.putString("redirect", "false");
        profilePic = (ImageView) findViewById(R.id.profileImageView);
        name = (TextView) findViewById(R.id.nameTextView);
        location = (TextView) findViewById(R.id.locationTextView);
        /*new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/picture",
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            JSONObject j = response.getJSONObject().getJSONObject("data");
                            final String img_url = j.getString("url");
                            Log.d("drawing image", img_url);
                            ImageView prof_view = (ImageView) findViewById(R.id.profileImageView);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    InputStream is = null;
                                    try {
                                        is = (InputStream) new URL(img_url).getContent();
                                        final Drawable d = Drawable.createFromStream(is, "profile_picture");
                                        ((ImageView) findViewById(R.id.profileImageView)).post(new Runnable() {
                                            @Override
                                            public void run() {
                                                ((ImageView) findViewById(R.id.profileImageView)).setImageDrawable(d);
                                            }
                                        });
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        } catch (Exception e) {
                            Log.d("Getting profile picture", e.toString());
                            return;
                        }
                    }
                }
        ).executeAsync();*/

        ParseUser user = ParseUser.getCurrentUser();

        if (user != null) {
            name.setText(user.getString("Name"));
        }

        // Location
        Address address = getLocation(user);
        String cityName = null;
        if (address != null) {
            cityName = address.getLocality();
        }
        if (cityName == null) {
            cityName = "Philadelphia, PA";
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
        GoogleApiClient apiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();
        Location currLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
        Double latitude = null;
        Double longitude = null;
        if (currLocation != null) {
            latitude = currLocation.getLatitude();
            longitude = currLocation.getLongitude();
        } else if (user != null){
            latitude = user.getDouble("Latitude");
            longitude = user.getDouble("Longitude");
        }

        if (latitude != null && longitude != null) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> address = geocoder.getFromLocation(latitude, longitude, 1);
                if (!address.isEmpty()) {
                    return address.get(0);
                }
            } catch (Exception e) {
                Log.e("Profile", e.getMessage());
            }
        }

        return null;
    }

    public void onEditLocationButtonClick(View view) {

    }
}
