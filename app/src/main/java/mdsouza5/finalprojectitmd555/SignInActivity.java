/*
 * Copyright (c) 2018. mdsouza5
 */

package mdsouza5.finalprojectitmd555;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mdsouza5.finalprojectitmd555.models.User;

public class SignInActivity extends BaseActivity implements View.OnClickListener {

    private static final String LOGTAG = "SignInActivity";

    private DatabaseReference fpDatabase;
    private FirebaseAuth fpAuth;

    private EditText fpEmailField;
    private EditText fpPasswordField;
    private Button fpSignInButton;
    private Button fpSignUpButton;
    private TextView fpMainStatus;
    private boolean isEmailSent = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_activity);

        fpDatabase = FirebaseDatabase.getInstance().getReference();
        fpAuth = FirebaseAuth.getInstance();

        fpEmailField = findViewById(R.id.field_email);
        fpPasswordField = findViewById(R.id.field_password);
        fpSignInButton = findViewById(R.id.button_sign_in);
        fpSignUpButton = findViewById(R.id.button_sign_up);
        fpMainStatus = findViewById(R.id.mainstatus);

        fpSignInButton.setOnClickListener(this);
        fpSignUpButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (fpAuth.getCurrentUser() != null) {
            onAuthSuccess(fpAuth.getCurrentUser());
        }
    }

    private void signIn() {
        Log.d(LOGTAG, "signIn");
        if (!validateForm()) {
            return;
        }

        String email = fpEmailField.getText().toString();
        String password = fpPasswordField.getText().toString();

        fpAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(LOGTAG, "signIn:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            Toast.makeText(SignInActivity.this, "Sign In Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signUp() {
        Log.d(LOGTAG, "signUp");
        if (!validateForm()) {
            return;
        }

        String email = fpEmailField.getText().toString();
        String password = fpPasswordField.getText().toString();

        fpAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(LOGTAG, "createUser:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());

                            //Send Email Verification Here
                            boolean isVerificationEmailSent = sendmailVerification(fpAuth.getCurrentUser());
                            if (!isVerificationEmailSent) {
                                sendmailVerification(fpAuth.getCurrentUser());
                            }

                        } else {
                            Toast.makeText(SignInActivity.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean sendmailVerification(FirebaseUser user) {

        user.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    fpMainStatus.setText(R.string.email_verification_success_notify);
                    findViewById(R.id.mainstatus).setVisibility(View.VISIBLE);
                    Toast.makeText(SignInActivity.this, R.string.email_verification_success_notify, Toast.LENGTH_LONG).show();
                    isEmailSent = true;
                } else {
                    Toast.makeText(SignInActivity.this, R.string.email_verification_failure_notify, Toast.LENGTH_LONG).show();
                    isEmailSent = false;
                }
            }
        });
        return isEmailSent;
    }

    private boolean validateForm() {
        boolean isValid = true;
        if (TextUtils.isEmpty(fpEmailField.getText().toString())) {
            fpEmailField.setError("Required");
            isValid = false;
        } else {
            fpEmailField.setError(null);
        }

        if (TextUtils.isEmpty(fpPasswordField.getText().toString())) {
            fpPasswordField.setError("Required");
            isValid = false;
        } else {
            fpPasswordField.setError(null);
        }

        return isValid;
    }

    private void onAuthSuccess(FirebaseUser firebaseUser) {
        String userName = userNameFromEmail(firebaseUser.getEmail());

        writeNewUser(firebaseUser.getUid(), userName, firebaseUser.getEmail());

        //check is email has been verified; if yes then allow or else error
        if (!firebaseUser.isEmailVerified()) {
            Toast.makeText(SignInActivity.this, "Please Verify Your Email.", Toast.LENGTH_LONG).show();
            findViewById(R.id.button_sign_up).setEnabled(false);
        } else {
            startActivity(new Intent(SignInActivity.this, ShowPagesActivity.class));
            finish();
        }
    }

    public void writeNewUser(String uid, String userName, String email) {
        User user = new User(userName, email);
        fpDatabase.child("users").child(uid).setValue(user);
    }

    private String userNameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button_sign_in) {
            signIn();
        } else if (i == R.id.button_sign_up) {
            signUp();
        }
    }
}
