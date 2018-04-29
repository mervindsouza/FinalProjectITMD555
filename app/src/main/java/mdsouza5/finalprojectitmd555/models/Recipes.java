package mdsouza5.finalprojectitmd555.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Recipe class which provides a model for user recipes
 * @author merv
 */

@IgnoreExtraProperties
public class Recipes {
    public String uid;
    public String recipeAuthor;
    public String recipeTitle;
    public String recipeBody;
    public int starredRecipesCount;
    public Map<String, Boolean> starsForRecipes = new HashMap<>();

    public Recipes() {
    }

    public Recipes(String uid, String recipeAuthor, String recipeTitle, String recipeBody) {
        this.uid = uid;
        this.recipeAuthor = recipeAuthor;
        this.recipeTitle = recipeTitle;
        this.recipeBody = recipeBody;
    }

    /**
     * This function returns the HashMap with the values to be persisted to firebase
     * @return hashmap for a user, recipe author, recipe title and recipe body
     */
    @Exclude
    public Map<String, Object> ToMap() {
        HashMap<String, Object> stars = new HashMap<>();
        stars.put("uid", uid);
        stars.put("recipeAuthor", recipeAuthor);
        stars.put("recipeTitle", recipeTitle);
        stars.put("recipeBody", recipeBody);

        return stars;
    }
}
