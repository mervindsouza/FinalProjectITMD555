package mdsouza5.finalprojectitmd555.recipefragments;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class MyRecipesFragment extends RecipeListFragment {
    public MyRecipesFragment() {
    }

    @Override
    public Query GetQuery(DatabaseReference databaseReference) {
        return databaseReference.child("user-recipes").child(GetUid());
    }
}
