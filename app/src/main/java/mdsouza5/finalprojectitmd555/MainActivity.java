package mdsouza5.finalprojectitmd555;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final String LOGTAG = "EmailPasswordAuth";

    //Declare main activity fields
    private TextView fpStatusTextView;
    private TextView fpDetailTextView;
    private EditText fpEmailField;
    private EditText fpPasswordField;

    //Declare Firebase Auth
    private FirebaseAuth fpFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get the Views
        fpStatusTextView = findViewById(R.id.mainstatus);
        fpDetailTextView = findViewById(R.id.maindetail);
        fpEmailField = findViewById(R.id.email_field);
        fpPasswordField = findViewById(R.id.password_field);

        //Get the Buttons
        findViewById(R.id.sign_in_email_button).setOnClickListener(this);
        findViewById(R.id.create_account_email_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.verify_email_button).setOnClickListener(this);

        //Start Firebase Instance
        fpFirebaseAuth = FirebaseAuth.getInstance();
    }

    // On Start Check for the status of the user
    @Override
    protected void onStart() {
        super.onStart();
        //Check if the user is either signed in or not
        FirebaseUser fpCurrentUser = fpFirebaseAuth.getCurrentUser();
        //updateUI(fpCurrentUser);
    }

    private void CreateAccountForUsers(String fpUserEmail, String fpUserPassword) {
        Log.d(LOGTAG, "CreateAccount:" + fpUserEmail);
        if (!ValidateForm()) {
            return;
        }

        ShowProgressDialog();

        // Create User For Firebase
        fpFirebaseAuth.createUserWithEmailAndPassword(fpUserEmail, fpUserPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Sign in is successful and Users details can be shown.
                            Log.d(LOGTAG, "createUserWithEmail:success");
                            FirebaseUser fpFirebaseUser = fpFirebaseAuth.getCurrentUser();
                            updateUI(fpFirebaseUser);
                        } else {
                            //Sign in is unsuccessful and Toast Error Shown to User
                            Log.w(LOGTAG, "createUserWithEmail:failed", task.getException());
                            Toast.makeText(MainActivity.this, "User Authentication Failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    // Sign In Function
    private void SignIn(String fpUserEmail, String fpUserPassword) {
        Log.d(LOGTAG, "SignIn:" + fpUserEmail);
        if (!ValidateForm()) {
            return;
        }

        fpFirebaseAuth.signInWithEmailAndPassword(fpUserEmail, fpUserPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Sign in is successful and Users details can be shown.
                            Log.d(LOGTAG, "signInWithEmail:success");
                            FirebaseUser fpFirebaseUser = fpFirebaseAuth.getCurrentUser();
                            updateUI(fpFirebaseUser);
                        } else {
                            //Sign in is unsuccessful and Toast Error Shown to User
                            Log.w(LOGTAG, "signInWithEmail:failure");
                            Toast.makeText(MainActivity.this, "User Authntication Failed", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        if(!task.isSuccessful()){
                            fpStatusTextView.setText(R.string.authentication_failed);
                        }

                        hideProgressDialog();
                    }
                });
    }

    private void SignOut(){
        fpFirebaseAuth.signOut();
        updateUI(null);
    }
}
