package mdsouza5.finalprojectitmd555;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

public class BaseActivity extends AppCompatActivity {

    private static final String LOGTAG = "BaseActivity";

    public String GetFirebaseUserId() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
