package mdsouza5.finalprojectitmd555.recipefragments;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Getts the top 100 recipes posted by all the users
 * limitToFirst() takes the number of records to be returned. In our case, records.
 * */

public class RecentRecipesFragment extends RecipeListFragment {

    public RecentRecipesFragment() {
    }

    @Override
    public Query GetQuery(DatabaseReference databaseReference) {
        Query recipesQuery = databaseReference.child("recipes").limitToFirst(100);
        return recipesQuery;
    }
}
