package mdsouza5.finalprojectitmd555.recipefragments;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class RecentRecipesFragment extends RecipeListFragment {

    public RecentRecipesFragment() {
    }

    @Override
    public Query GetQuery(DatabaseReference databaseReference) {
        Query recipesQuery = databaseReference.child("recipes").limitToFirst(100);
        return recipesQuery;
    }
}
