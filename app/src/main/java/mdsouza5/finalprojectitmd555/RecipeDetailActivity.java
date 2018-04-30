package mdsouza5.finalprojectitmd555;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import mdsouza5.finalprojectitmd555.models.Comment;
import mdsouza5.finalprojectitmd555.models.Recipes;
import mdsouza5.finalprojectitmd555.models.User;

/**
 * This class shows the comments and the details of the recipes based
 * on the details added by the user
 * whenever each recipe is clicked, it's details are shown to the user
 * */

public class RecipeDetailActivity extends BaseActivity implements View.OnClickListener {

    public static final String EXTRA_RECIPE_KEY = "recipe_key";
    private static final String LOGTAG = "RecipeDetailActivity";

    private DatabaseReference fpDBReferenceForRecipe;
    private DatabaseReference fpDBReferenceForComments;
    private ValueEventListener fpRecipeListener;
    private String recipeKey;
    private CommentAdapter fpCommentAdapter;

    private TextView fpAuthorView;
    private TextView fpTitleView;
    private TextView fpBodyView;
    private EditText fpCommentField;
    private Button fpCommentButton;
    private RecyclerView fpCommentsRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        recipeKey = getIntent().getStringExtra(EXTRA_RECIPE_KEY);
        if (recipeKey == null) {
            throw new IllegalArgumentException("EXTRA_RECIPE_KEY Must be passed.");
        }

        fpDBReferenceForRecipe = FirebaseDatabase.getInstance().
                getReference().child("recipes").child(recipeKey);
        fpDBReferenceForComments = FirebaseDatabase.getInstance().
                getReference().child("recipe-comments").child(recipeKey);

        fpAuthorView = findViewById(R.id.recipe_author);
        fpTitleView = findViewById(R.id.recipe_title);
        fpBodyView = findViewById(R.id.recipe_body);

        fpCommentField = findViewById(R.id.field_recipe_comment);
        fpCommentButton = findViewById(R.id.button_recipe_comment);
        fpCommentsRecycler = findViewById(R.id.recycler_recipe_comments);

        fpCommentButton.setOnClickListener(this);
        fpCommentsRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();


        ValueEventListener recipeValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Recipes recipes = dataSnapshot.getValue(Recipes.class);
                fpAuthorView.setText(recipes.recipeAuthor);
                fpTitleView.setText(recipes.recipeTitle);
                fpBodyView.setText(recipes.recipeBody);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // If recipe isn't obtained, Log a error message
                Log.w(LOGTAG, "loadRecipe:onCancelled", databaseError.toException());
                Toast.makeText(RecipeDetailActivity.this, "Failed To Load Recipe. Try Later.", Toast.LENGTH_SHORT).show();
            }
        };

        fpDBReferenceForRecipe.addValueEventListener(recipeValueEventListener);
        fpRecipeListener = recipeValueEventListener;

        fpCommentAdapter = new CommentAdapter(this, fpDBReferenceForComments);
        fpCommentsRecycler.setAdapter(fpCommentAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (fpRecipeListener != null) {
            fpDBReferenceForRecipe.removeEventListener(fpRecipeListener);
        }

        fpCommentAdapter.CleanupListener();
    }

    @Override
    public void onClick(View v) {
        int k = v.getId();
        if (k == R.id.button_recipe_comment) {
            PostComment();
        }
    }

    // This function when called posts the comments for the user on the recipe selected by the user
    private void PostComment() {
        final String uid = GetFirebaseUserId();
        FirebaseDatabase.getInstance().getReference().child("users").child(uid).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        String authorName = user.userName;

                        String commentText = fpCommentField.getText().toString();
                        Comment comment = new Comment(uid, authorName, commentText);

                        fpDBReferenceForComments.push().setValue(comment);

                        fpCommentField.setText(null);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    // View holder for the comments which takes in all the comments and shows them on the recipe
    private static class CommentViewHolder extends RecyclerView.ViewHolder {

        public TextView commentAuthorView;
        public TextView commentBodyView;

        public CommentViewHolder(View itemView) {
            super(itemView);

            commentAuthorView = itemView.findViewById(R.id.comment_author);
            commentBodyView = itemView.findViewById(R.id.comment_body);
        }
    }

    private static class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {
        private Context fpContext;
        private DatabaseReference fpDatabaseReference;
        private ChildEventListener fpChildEventListener;

        private List<String> fpCommentIds = new ArrayList<>();
        private List<Comment> fpComments = new ArrayList<>();

        public CommentAdapter(final Context fpContext, DatabaseReference ref) {
            this.fpContext = fpContext;
            this.fpDatabaseReference = ref;

            ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(LOGTAG, "onChildAdded:" + dataSnapshot.getKey());

                    Comment comment = dataSnapshot.getValue(Comment.class);

                    fpCommentIds.add(dataSnapshot.getKey());
                    fpComments.add(comment);
                    notifyItemInserted(fpCommentIds.size() - 1);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(LOGTAG, "onChildChanged:" + dataSnapshot.getKey());
                    // If the comment is changed, we compare with the key if we are displaying the right comment or not
                    // If it is changed, show the updated comment
                    Comment newComment = dataSnapshot.getValue(Comment.class);
                    String commentKey = dataSnapshot.getKey();

                    int commentIndex = fpCommentIds.indexOf(commentKey);
                    if (commentIndex > -1) {
                        fpComments.set(commentIndex, newComment);
                        notifyItemChanged(commentIndex);
                    } else {
                        Log.w(LOGTAG, "onChildChanged:unknown_child:" + commentKey);
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Log.d(LOGTAG, "onChildRemoved:" + dataSnapshot.getKey());

                    // A comment has changed, check if we are displaying it, if yes then remove it
                    String commentKey = dataSnapshot.getKey();

                    int commentIndex = fpCommentIds.indexOf(commentKey);

                    if (commentIndex > -1) {
                        fpCommentIds.remove(commentIndex);
                        fpComments.remove(commentIndex);

                        notifyItemRemoved(commentIndex);
                    } else {
                        Log.w(LOGTAG, "onChildRemoved:unknown_child" + commentKey);
                    }
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(LOGTAG, "onChildMoved:" + dataSnapshot.getKey());

                    // If a comment has changed position, use the key to determine it and move it
                    Comment movedComment = dataSnapshot.getValue(Comment.class);
                    String commentKey = dataSnapshot.getKey();

                    //
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(LOGTAG, "postComments:onCancelled", databaseError.toException());
                    Toast.makeText(fpContext, "Failed to load comments.", Toast.LENGTH_SHORT).show();
                }
            };
            ref.addChildEventListener(childEventListener);

            // Store reference to listener so it can be removed when app is stopped
            fpChildEventListener = childEventListener;
        }

        @NonNull
        @Override
        public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(fpContext);
            View view = layoutInflater.inflate(R.layout.item_comment, parent, false);
            return new CommentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
            Comment comment = fpComments.get(position);
            holder.commentAuthorView.setText(comment.commentAuthor);
            holder.commentBodyView.setText(comment.recipeComment);
        }

        @Override
        public int getItemCount() {
            return fpComments.size();
        }

        public void CleanupListener() {
            if (fpChildEventListener != null) {
                fpDatabaseReference.removeEventListener(fpChildEventListener);
            }
        }
    }

}
