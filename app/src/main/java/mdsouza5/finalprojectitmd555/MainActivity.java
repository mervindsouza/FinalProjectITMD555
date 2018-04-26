package mdsouza5.finalprojectitmd555;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mdsouza5.finalprojectitmd555.models.User;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LOGTAG = "EmailPasswordAuth";

    //Declare main activity fields
    private TextView fpStatusTextView;
    private TextView fpDetailTextView;
    private EditText fpEmailField;
    private EditText fpPasswordField;

    //Declare Firebase Auth
    private FirebaseAuth fpFirebaseAuth;

    //Declare Firebase Database
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

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

    private void CreateAccountForUsers(final String fpUserEmail, String fpUserPassword) {
        Log.d(LOGTAG, "CreateAccount:" + fpUserEmail);
        if (!ValidateForm()) {
            return;
        }

        //ShowProgressDialog();

        // Create User For Firebase
        fpFirebaseAuth.createUserWithEmailAndPassword(fpUserEmail, fpUserPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Sign in is successful and Users details can be shown.
                            Log.d(LOGTAG, "createUserWithEmail:success");
                            FirebaseUser fpFirebaseUser = fpFirebaseAuth.getCurrentUser();
                            writeNewUser(fpFirebaseUser.getUid().toString(), "test name", fpUserEmail);
                            updateUI(fpFirebaseUser);
                        } else {
                            //Sign in is unsuccessful and Toast Error Shown to User
                            Log.w(LOGTAG, "createUserWithEmail:failed", task.getException());
                            Toast.makeText(getApplicationContext(), "User Authentication Failed.", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getApplicationContext(), "Authentication Failed.", Toast.LENGTH_LONG).show();
                            updateUI(null);
                        }

                        if (!task.isSuccessful()) {
                            fpStatusTextView.setText(R.string.authentication_failed);
                        }

                        if(fpFirebaseAuth.getCurrentUser().isEmailVerified()){
                            startActivity(new Intent(MainActivity.this, ShowPagesActivity.class));
                        }else{
                            Toast.makeText(getApplicationContext(), "Email Isn't Verified.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void SignOut() {
        fpFirebaseAuth.signOut();
        updateUI(null);
    }

    private void SendEmailVerificationToUser() {
        // Disable the button initially
        findViewById(R.id.verify_email_button).setEnabled(false);

        // Prepare to send Verification Email
        final FirebaseUser fpFirebaseUser = fpFirebaseAuth.getCurrentUser();
        fpFirebaseUser.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Re Enable the Button to request verification email
                        findViewById(R.id.verify_email_button).setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Verification Email Send To: " + fpFirebaseUser.getEmail(), Toast.LENGTH_LONG).show();
                        } else {
                            Log.e(LOGTAG, "SendEmailVerificationToUser:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Verification Email Not Send.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean ValidateForm() {
        boolean isValidForm = true;

        // Check Validity of Email
        String fpEmail = fpEmailField.getText().toString();
        if (TextUtils.isEmpty(fpEmail)) {
            fpEmailField.setError("Email Required.");
            isValidForm = false;
        } else {
            fpEmailField.setError(null);
        }

        // Check Validity of Password
        String fpPassword = fpPasswordField.getText().toString();
        if (TextUtils.isEmpty(fpPassword)) {
            fpPasswordField.setError("Password Required.");
            isValidForm = false;
        } else {
            fpPasswordField.setError(null);
        }
        return isValidForm;
    }

    private void updateUI(FirebaseUser fpFirebaseUser) {
        //hideProgressDialog();
        if (fpFirebaseUser != null) {
            fpStatusTextView.setText(getString(R.string.firebase_status_fmt, fpFirebaseUser.getUid()));
            findViewById(R.id.email_and_pwd_buttons).setVisibility(View.GONE);
            findViewById(R.id.email_and_pwd_fields).setVisibility(View.GONE);
            findViewById(R.id.signed_in_buttons).setVisibility(View.VISIBLE);
            findViewById(R.id.verify_email_button).setEnabled(!fpFirebaseUser.isEmailVerified());
        } else {
            findViewById(R.id.email_and_pwd_buttons).setVisibility(View.VISIBLE);
            findViewById(R.id.email_and_pwd_fields).setVisibility(View.VISIBLE);
            findViewById(R.id.signed_in_buttons).setVisibility(View.GONE);
        }
    }

    public void onClick(View v) {
        int j = v.getId();
        if (j == R.id.create_account_email_button) {
            CreateAccountForUsers(fpEmailField.getText().toString(), fpPasswordField.getText().toString());
        } else if (j == R.id.sign_in_email_button) {
            SignIn(fpEmailField.getText().toString(), fpPasswordField.getText().toString());
        } else if (j == R.id.sign_out_button) {
            SignOut();
        } else if (j == R.id.verify_email_button) {
            SendEmailVerificationToUser();
        }
    }

    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        databaseReference.child("users").child(userId).setValue(user);
    }
}
