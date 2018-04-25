package mdsouza5.finalprojectitmd555;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewRecipeActivity extends BaseActivity {

    private static final String LOGTAG = "NewRecipeActivity";
    private static final String REQUIRED_VALUE = "Required";

    private DatabaseReference fpDatabaseReference;

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

    }
}
