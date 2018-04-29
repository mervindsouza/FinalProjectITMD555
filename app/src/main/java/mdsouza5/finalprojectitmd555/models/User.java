package mdsouza5.finalprojectitmd555.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * User class which provides a model for user
 * @author merv
 */

@IgnoreExtraProperties
public class User {
    public String userName;
    public String userEmail;

    public User() {
    }

    public User(String userName, String userEmail) {
        this.userName = userName;
        this.userEmail = userEmail;
    }
}
