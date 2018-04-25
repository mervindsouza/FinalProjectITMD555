package mdsouza5.finalprojectitmd555;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailActivity extends BaseActivity implements View.OnClickListener {

    public static final String EXTRA_RECIPE_KEY = "recipe_key";
    private static final String LOGTAG = "RecipeDetailActivity";

    private DatabaseReference fpDBReferenceForRecipe;
    private DatabaseReference fpDBReferenceForComments;
    private ValueEventListener fpRecipeListner;
    private String fpRecipeKey;
    private CommentAdapter fpCommentAdapter;


    @Override
    public void onClick(View v) {

    }


    private static class CommentViewHolder extends RecyclerView.ViewHolder {

        public TextView recipeAuthorView;
        public TextView recipeBodyView;

        public CommentViewHolder(View itemView) {
            super(itemView);
            recipeAuthorView = itemView.findViewById(R.id.recipe_comment_author);
            recipeBodyView = itemView.findViewById(R.id.recipe_comment_body);
        }
    }

    private static class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {
        private Context fpContext;
        private DatabaseReference fpDatabaseReference;
        private ChildEventListener fpChildEventListener;

        private List<String> fpCommentIds = new ArrayList<>();
        private List<Comment> fpComments = new ArrayList<>();

        public CommentAdapter(final Context fpContext, DatabaseReference fpDatabaseReference) {
            this.fpContext = fpContext;
            this.fpDatabaseReference = fpDatabaseReference;

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
                    Comment comment = dataSnapshot.getValue(Comment.class);
                    String commentKey = dataSnapshot.getKey();

                    int commentIndex = fpCommentIds.indexOf(commentKey);
                    if (commentIndex > -1) {
                        fpComments.set(commentIndex, comment);
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
            fpDatabaseReference.addChildEventListener(childEventListener);

            // Store reference to listener so it can be removed when app is stopped
            fpChildEventListener = childEventListener;
        }

        @NonNull
        @Override
        public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(fpContext);
            View view = layoutInflater.inflate(R.layout.item_comment, parent, false);
            return new CommentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CommentAdapter holder, int position) {
            Comment comment = fpComments.get(position);
            holder.
        }

        @Override
        public int getItemCount() {
            return fpComments.size();
        }

        public void CleanupListner() {
            if (fpChildEventListener != null) {
                fpDatabaseReference.removeEventListener(fpChildEventListener);
            }
        }
    }

}
