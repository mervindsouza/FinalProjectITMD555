package mdsouza5.finalprojectitmd555.recipefragments;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Returns the top recipes, based on the count of stars received by each of the recipes
 * */

public class MyTopRecipesFragment extends RecipeListFragment {

    public MyTopRecipesFragment() {
    }

    @Override
    public Query GetQuery(DatabaseReference databaseReference) {
        String myUserId = GetUid();

        Query topRecipesQuery = databaseReference.child("user-recipes").child(myUserId).orderByChild("starredRecipesCount");
        return topRecipesQuery;
    }
}
