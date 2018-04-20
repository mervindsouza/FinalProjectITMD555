package mdsouza5.finalprojectitmd555.models;

import com.google.firebase.database.IgnoreExtraProperties;

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
