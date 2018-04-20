package mdsouza5.finalprojectitmd555.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserComments {

    public String userId;
    public String recipeAuthor;
    public String recipeComment;

    public UserComments() {
    }

    public UserComments(String userId, String recipeAuthor, String recipeComment) {
        this.userId = userId;
        this.recipeAuthor = recipeAuthor;
        this.recipeComment = recipeComment;
    }
}
