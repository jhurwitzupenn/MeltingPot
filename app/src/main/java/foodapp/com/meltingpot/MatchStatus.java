package foodapp.com.meltingpot;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MatchStatus extends Activity {
    ImageView matchProfilePic;
    TextView matchName;
    TextView matchTime;
    TextView recipeName;
    TextView matchStatus;

    String[] matchIngredients;
    String[] missingIngredients;
    String recipeUrl;
    RecipeObject recipeObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_status);

        matchName = (TextView) findViewById(R.id.matchNameTextView);
        matchTime = (TextView) findViewById(R.id.matchTimeTextView);
        matchStatus = (TextView) findViewById(R.id.matchStatusTextView);
        recipeName = (TextView) findViewById(R.id.recipeNameTextView);

        ParseUser user = ParseUser.getCurrentUser();
        try {
            user.fetch();

            // Match user
            ParseUser matchUser = ParseUser.getQuery().get(user.getString("MatchId"));
            if (matchUser != null) {
                matchName.setText(matchUser.getString("name"));
                matchTime.setText(matchUser.getString("Time"));
                matchIngredients = fromJSONArray(matchUser.getJSONArray("Ingredients"));

                matchStatus.setText(matchUser.getBoolean("AcceptMatch")
                    ? "Accepted"
                    : "Pending");

                Bundle params = new Bundle();
                params.putString("redirect", "false");
                matchProfilePic = (ImageView) findViewById(R.id.matchProfileImageView);
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/" + matchUser.getString("FBUserId") + "/picture",
                        params,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                try {
                                    JSONObject j = response.getJSONObject().getJSONObject("data");
                                    final String img_url = j.getString("url");
                                    Log.d("drawing image", img_url);
                                    ImageView prof_view = (ImageView) findViewById(R.id.matchProfileImageView);
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            InputStream is = null;
                                            try {
                                                is = (InputStream) new URL(img_url).getContent();
                                                final Drawable d = Drawable.createFromStream(is, "profile_picture");
                                                ((ImageView) findViewById(R.id.matchProfileImageView)).post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        ((ImageView) findViewById(R.id.matchProfileImageView)).setImageDrawable(d);
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
                ).executeAsync();
            }

            // Recipe
            recipeObject = ParseQuery.getQuery(RecipeObject.class).get(user.getString("RecipeId"));
            if (recipeObject != null) {
                recipeName.setText(recipeObject.getString("Name"));
                missingIngredients = fromJSONArray(recipeObject.getJSONArray("MissingIngredients"));
                recipeUrl = recipeObject.getString("Url");
            }

        } catch (Exception e) {
            Log.e("Match", e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_match_status, menu);
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

    public void onMatchIngredientsButtonClick(View view) {
        Intent myIntent = new Intent(this, ViewIngredients.class);
        myIntent.putExtra("Ingredients", matchIngredients);
        startActivity(myIntent);
    }

    public void onFullRecipeButtonClick(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(recipeUrl));
        startActivity(i);
    }

    public void onMissingIngredientsButtonClick(View view) {
        Intent myIntent = new Intent(this, ViewIngredients.class);
        myIntent.putExtra("Ingredients", missingIngredients);
        startActivity(myIntent);
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
