package mdsouza5.finalprojectitmd555.recipieholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.R;

import mdsouza5.finalprojectitmd555.models.Recipes;

public class RecipieViewHolder extends RecyclerView.ViewHolder {

    public TextView recipeTitleTextView;
    public TextView recipeAuthotTextView;
    public ImageView recipeStarsImageView;
    public TextView recipeStarCountTextView;
    public TextView recipeBodyTextView;

    public RecipieViewHolder(View itemView) {
        super(itemView);
    }
}
