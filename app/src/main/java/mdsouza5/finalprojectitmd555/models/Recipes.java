package mdsouza5.finalprojectitmd555.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Recipes {
    public String userId;
    public String recipeAuthor;
    public String recipeTitle;
    public String recipeBody;
    public int starredRecipesCount;
    public Map<String, Boolean> starsForRecipes = new HashMap<>();

    public Recipes() {
    }

    public Recipes(String userId, String recipeAuthor, String recipeTitle, String recipeBody) {
        this.userId = userId;
        this.recipeAuthor = recipeAuthor;
        this.recipeTitle = recipeTitle;
        this.recipeBody = recipeBody;
    }

    //Map Recipes to a HashMap
    @Exclude
    public Map<String, Object> ToMap() {
        HashMap<String, Object> stars = new HashMap<>();
        stars.put("userId", userId);
        stars.put("recipeAuthor", recipeAuthor);
        stars.put("recipeTitle", recipeTitle);
        stars.put("recipeBody", recipeBody);
        return stars;
    }
}
