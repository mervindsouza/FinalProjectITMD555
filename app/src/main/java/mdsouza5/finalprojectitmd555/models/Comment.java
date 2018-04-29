package mdsouza5.finalprojectitmd555.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Comment class which provides a model for user comments on recipes
 * @author merv
 */

@IgnoreExtraProperties
public class Comment {

    public String uid;
    public String commentAuthor;
    public String recipeComment;

    public Comment() {
    }

    public Comment(String uid, String commentAuthor, String recipeComment) {
        this.uid = uid;
        this.commentAuthor = commentAuthor;
        this.recipeComment = recipeComment;
    }
}
