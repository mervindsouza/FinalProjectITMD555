package mdsouza5.finalprojectitmd555.recipefragments;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/*
* This class returns based on the user id returns all the recipes for the user
* each userid is passed and it gets the user-recipes object from database
* */

public class MyRecipesFragment extends RecipeListFragment {
    public MyRecipesFragment() {
    }

    @Override
    public Query GetQuery(DatabaseReference databaseReference) {
        return databaseReference.child("user-recipes").child(GetUid());
    }
}
