package foodapp.com.meltingpot;

import android.app.Application;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by martingreenberg on 9/5/15.
 */
public class MeltingPot extends Application {
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "RObjTBsw8FcRXFWSnwkS4T7quaNB95q3nzAqHtCL", "LHZacqbGSa22fiNaEcRyswaFq1ZjGM6smPyAD7AN");
        ParseFacebookUtils.initialize(this.getApplicationContext());
    };
}
