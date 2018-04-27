package mdsouza5.finalprojectitmd555.recipieholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import mdsouza5.finalprojectitmd555.R;
import mdsouza5.finalprojectitmd555.models.Recipes;

public class RecipeViewHolder extends RecyclerView.ViewHolder {

    public TextView recipeTitleTextView;
    public TextView recipeAuthorTextView;
    public ImageView recipeStarsImageView;
    public TextView recipeStarCountTextView;
    public TextView recipeBodyTextView;

    public RecipeViewHolder(View itemView) {
        super(itemView);

        //Insert findViewById
        recipeTitleTextView = itemView.findViewById(R.id.recipe_title);
        recipeAuthorTextView = itemView.findViewById(R.id.recipe_author);
        recipeStarsImageView = itemView.findViewById(R.id.stars);
        recipeStarCountTextView = itemView.findViewById(R.id.number_of_stars);
        recipeBodyTextView = itemView.findViewById(R.id.recipe_body);
    }

    //Binding RecipeViewHolder to Recipe Model
    public void BindingToRecipe(Recipes recipes, View.OnClickListener starsClickListener) {
        recipeTitleTextView.setText(recipes.recipeTitle);
        recipeAuthorTextView.setText(recipes.recipeAuthor);
        recipeStarCountTextView.setText(String.valueOf(recipes.starredRecipesCount));
        recipeBodyTextView.setText(recipes.recipeBody);

        recipeStarsImageView.setOnClickListener(starsClickListener);
    }
}
