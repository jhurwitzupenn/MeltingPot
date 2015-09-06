package foodapp.com.meltingpot;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by kellytan on 9/6/15.
 */
@ParseClassName("RecipeObject")
public class RecipeObject extends ParseObject {

    public RecipeObject(){}

    public RecipeObject(String name, List<String> ingredients, List<String> missingIngredients,
                        String url) {
        super();
        this.put("Name", name);
        this.put("Ingredients", new JSONArray(ingredients));
        this.put("MissingIngredients", new JSONArray(missingIngredients));
        this.put("Url", url);
        this.saveInBackground();
    }
}
