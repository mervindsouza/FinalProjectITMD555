package mdsouza5.finalprojectitmd555;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import mdsouza5.finalprojectitmd555.models.Recipes;
import mdsouza5.finalprojectitmd555.models.User;

public class NewRecipeActivity extends BaseActivity {

    private static final String LOGTAG = "NewRecipeActivity";
    private static final String REQUIRED_FIELD = "Required";

    private DatabaseReference fpDatabaseReference;

    private FirebaseAuth firebaseAuth;

    private EditText fpRecipeTitleField;
    private EditText fpRecipeBodyField;
    private FloatingActionButton fpSubmitButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);

        fpDatabaseReference = FirebaseDatabase.getInstance().getReference();

        fpRecipeTitleField = findViewById(R.id.field_recipe_title);
        fpRecipeBodyField = findViewById(R.id.field_recipe_body);
        fpSubmitButton = findViewById(R.id.fab_submit_recipe);

        fpSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubmitRecipe();
            }
        });
    }

    private void SubmitRecipe() {
        final String recipeTitle = fpRecipeTitleField.getText().toString();
        final String recipeBody = fpRecipeBodyField.getText().toString();

        if (TextUtils.isEmpty(recipeTitle)) {
            fpRecipeTitleField.setError(REQUIRED_FIELD);
            return;
        }

        if (TextUtils.isEmpty(recipeBody)) {
            fpRecipeBodyField.setError(REQUIRED_FIELD);
            return;
        }

        SetEditingEnabled(false);
        Toast.makeText(this, "Adding Your Recipe...", Toast.LENGTH_SHORT).show();

        final String userId = GetFirebaseUserId();
        fpDatabaseReference.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user == null) {
                    Log.e(LOGTAG, "User " + userId + " is surprisingly null.");
                    Toast.makeText(NewRecipeActivity.this, "Error: Couldn't Fetch User", Toast.LENGTH_SHORT).show();
                } else {
                   WriteNewRecipe(userId, user.userName, recipeTitle, recipeBody);
                }

                SetEditingEnabled(true);
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(LOGTAG, "GetUser:onCancelled", databaseError.toException());
                SetEditingEnabled(true);
            }
        });
    }

    private void SetEditingEnabled(boolean isEnabled) {
        fpRecipeTitleField.setEnabled(isEnabled);
        fpRecipeBodyField.setEnabled(isEnabled);

        if (isEnabled) {
            fpSubmitButton.setVisibility(View.VISIBLE);
        } else {
            fpSubmitButton.setVisibility(View.GONE);
        }
    }

    private void WriteNewRecipe(String userId, String userName, String recipeTitle, String recipeBody) {
        //Create new recipe at /user-recipes/$userid/$recipeid and at
        // /recipes/$recipeid simultaneously
        String key = fpDatabaseReference.child("recipes").push().getKey();
        Recipes recipe = new Recipes(userId, userName, recipeTitle, recipeBody);
        Map<String, Object> recipeValues = recipe.ToMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/recipes/" + key, recipeValues);
        childUpdates.put("/user-recipes/" + userId + "/" + key, recipeValues);

        fpDatabaseReference.updateChildren(childUpdates);
    }
}
