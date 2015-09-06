package foodapp.com.meltingpot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(AccessToken.getCurrentAccessToken() != null) {
            ParseUser user = ParseUser.getCurrentUser();
            try {
                user.fetchIfNeeded();
            } catch (Exception e) {
                Log.e("LoginActivity", e.getMessage());
            }

            if (user.getBoolean("RequestPending") == false) {
                startActivity(new Intent(this, Profile.class));
            } else {
                startActivity(new Intent(this, PendingRequestInfo.class));
            }
        }
    }


    public void loginBtnClicked(View v) {
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, null, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (err == null && user != null) {
                    if (user.getBoolean("PendingRequest") == false) {
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                    } else {
                        startActivity(new Intent(getApplicationContext(), PendingRequestInfo.class));
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }
}

