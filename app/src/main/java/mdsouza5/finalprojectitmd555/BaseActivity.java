package mdsouza5.finalprojectitmd555;

import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

public class BaseActivity extends AppCompatActivity {

    public String GetFirebaseUserId() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
