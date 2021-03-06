package foodapp.com.meltingpot;

import android.app.Application;
import android.content.Context;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;

/**
 * Created by martingreenberg on 9/5/15.
 */
public class MeltingPot extends Application {
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(RecipeObject.class);
        Parse.initialize(this, "RObjTBsw8FcRXFWSnwkS4T7quaNB95q3nzAqHtCL", "LHZacqbGSa22fiNaEcRyswaFq1ZjGM6smPyAD7AN");
        ParseFacebookUtils.initialize(this.getApplicationContext());
    };
}
