package mdsouza5.finalprojectitmd555.recipefragments;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;

import mdsouza5.finalprojectitmd555.R;
import mdsouza5.finalprojectitmd555.models.Recipes;
import mdsouza5.finalprojectitmd555.recipieholder.RecipeViewHolder;
import mdsouza5.finalprojectitmd555.RecipeDetailActivity;

public abstract class RecipeListFragment extends Fragment {
    private static final String LOGTAG = "RecipeListFragment";

    private DatabaseReference fpDatabase;

    private FirebaseRecyclerAdapter<Recipes, RecipeViewHolder> fpAdapter;
    private RecyclerView fpRecycler;
    private LinearLayoutManager fpLinearLayoutManager;

    public RecipeListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View fpRootView = inflater.inflate(mdsouza5.finalprojectitmd555.R.layout.fragment_all_recipes, container, false);

        fpDatabase = FirebaseDatabase.getInstance().getReference();
        fpRecycler = fpRootView.findViewById(mdsouza5.finalprojectitmd555.R.id.list_of_messages);
        fpRecycler.setHasFixedSize(true);

        return fpRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fpLinearLayoutManager = new LinearLayoutManager(getActivity());
        fpLinearLayoutManager.setReverseLayout(true);
        fpLinearLayoutManager.setStackFromEnd(true);
        fpRecycler.setLayoutManager(fpLinearLayoutManager);

        Query recipeQuery = GetQuery(fpDatabase);

        FirebaseRecyclerOptions fpOptions = new FirebaseRecyclerOptions.Builder<Recipes>().setQuery(recipeQuery, Recipes.class).build();

        fpAdapter = new FirebaseRecyclerAdapter<Recipes, RecipeViewHolder>(fpOptions) {
            @Override
            public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater fpLayoutInflater = LayoutInflater.from(parent.getContext());
                return new RecipeViewHolder(fpLayoutInflater.inflate(mdsouza5.finalprojectitmd555.R.layout.item_recipe, parent, false));
            }

            @Override
            protected void onBindViewHolder(RecipeViewHolder holder, int position, final Recipes model) {
                final DatabaseReference recipeReference = getRef(position);

                final String recipeKey = recipeReference.getKey();
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
                        intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_KEY, recipeKey);
                        startActivity(intent);
                    }
                });

                if (model.starsForRecipes.containsKey(GetUid())) {
                    holder.recipeStarsImageView.setImageResource(R.drawable.filled_star);
                } else {
                    holder.recipeStarsImageView.setImageResource(R.drawable.star_outline);
                }

                holder.BindingToRecipe(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View starView) {
                        DatabaseReference globalRecipeReference = fpDatabase.child("recipes").child(recipeReference.getKey());
                        DatabaseReference userRecipeReference = fpDatabase.child("user-recipes").child(model.uid).child(recipeReference.getKey());

                        onStarClickedByUser(globalRecipeReference);
                        onStarClickedByUser(userRecipeReference);
                    }
                });
            }
        };
        fpRecycler.setAdapter(fpAdapter);
    }

    // Star Clicked Event to check the occurance of the database transaction
    private void onStarClickedByUser(DatabaseReference recipeReference) {
        recipeReference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Recipes recipesObj = mutableData.getValue(Recipes.class);
                if (recipesObj == null) {
                    return Transaction.success(mutableData);
                }

                if (recipesObj.starsForRecipes.containsKey(GetUid())) {
                    recipesObj.starredRecipesCount = recipesObj.starredRecipesCount - 1;
                    recipesObj.starsForRecipes.remove(GetUid());
                } else {
                    recipesObj.starredRecipesCount = recipesObj.starredRecipesCount + 1;
                    recipesObj.starsForRecipes.put(GetUid(), true);
                }

                mutableData.setValue(recipesObj);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                Log.d(LOGTAG, "recipeTransaction:onComplete " + databaseError);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if(fpAdapter!=null){
            fpAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(fpAdapter!=null){
            fpAdapter.stopListening();
        }
    }

    public abstract Query GetQuery(DatabaseReference databaseReference);

    public String GetUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
