package mdsouza5.finalprojectitmd555.recipieholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
        //recipeTitleTextView = itemView.findViewById();
        //recipeAuthorTextView = itemView.findViewById();
        //recipeStarsImageView = itemView.findViewById();
        //recipeStarCountTextView = itemView.findViewById();
        //recipeBodyTextView = itemView.findViewById();
    }

    //Binding RecipeViewHolder to Recipe Model
    public void BindingToRecipe(Recipes recipeObj, View.OnClickListener starsClickListener) {
        recipeTitleTextView.setText(recipeObj.recipeTitle);
        recipeAuthorTextView.setText(recipeObj.recipeAuthor);
        recipeStarCountTextView.setText(String.valueOf(recipeObj.starredRecipesCount));
        recipeBodyTextView.setText(recipeObj.recipeBody);

        recipeStarsImageView.setOnClickListener(starsClickListener);
    }
}
