package mdsouza5.finalprojectitmd555.recipefragments;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class MyTopRecipesFragment extends RecipeListFragment {

    public MyTopRecipesFragment() {
    }

    @Override
    public Query GetQuery(DatabaseReference databaseReference) {
        Query topRecipesQuery = databaseReference.child("user-recipes").child(GetUid()).orderByChild("starCount");
        return topRecipesQuery;
    }
}
