package com.project.agroworldapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.project.agroworldapp.R;
import com.project.agroworldapp.databinding.ActivitySignUpBinding;
import com.project.agroworldapp.utils.Constants;
import com.project.agroworldapp.utils.CustomMultiColorProgressBar;
import com.project.agroworldapp.utils.Permissions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 99;
    private static final String TAG = "GoogleLogin";
    FirebaseAuth mAuth;
    boolean isEmailValid = true;
    private CustomMultiColorProgressBar progressBar;
    private GoogleSignInClient mGoogleSignInClient;
    private ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        progressBar = new CustomMultiColorProgressBar(this, getString(R.string.loader_message));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        loginWithGoogle();

        binding.btnSignup.setOnClickListener(v -> {
            String email = binding.etEmailSignUp.getText().toString();
            String passwd = binding.etPasswdUp.getText().toString();
            String number = binding.etNumberUp.getText().toString();
            String name = binding.etNameUp.getText().toString();

            if (!Constants.isValidEmail(email)) {
                isEmailValid = false;
                binding.etEmailSignUp.setError(getString(R.string.email_valid));
                return;
            }
            if (!Constants.contactValidation(number)) {
                binding.etNumberUp.setError(getString(R.string.contact_valid));
                return;
            }

            if (!isPasswordValid(passwd)) {
                binding.etPasswdUp.setError(getString(R.string.password_valid));
                return;
            }
            if (Permissions.checkConnection(this)) {
                createUserWithEmailAndPassword(email, passwd);
            }
        });

        binding.btnGoogleSignIn.setOnClickListener(v -> {
            if (Permissions.checkConnection(this)) {
                signIn();
            }
        });

        binding.fabAdminContactUp.setOnClickListener(v -> {
            Constants.adminEmailContact(this);
        });

        binding.tvHaveAnAccountUp.setOnClickListener(v -> {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        });

    }


    private void createUserWithEmailAndPassword(String email, String password) {
        progressBar.showProgressBar();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                progressBar.hideProgressBar();
                // Sign in success, update UI with the signed-in user's information
                Constants.showToast(SignUpActivity.this, "createUserWithEmail:success");
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                finish();
            } else {
                progressBar.hideProgressBar();
                // If sign in fails, display a message to the user.
                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                Toast.makeText(SignUpActivity.this, "Authentication failed\n" + task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginWithGoogle() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Constants.showToast(this, getString(R.string.google_signIn_success));
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Constants.showToast(this, "Google sign in failed.\n" + e.getLocalizedMessage());
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }


    private void firebaseAuthWithGoogle(String idToken) {
        progressBar.showProgressBar();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                progressBar.hideProgressBar();
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "");
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    Constants.showToast(SignUpActivity.this, getString(R.string.google_signIn_success));
                    Constants.identifyUser(user, SignUpActivity.this);
                    finish();
                } else {
                    Constants.showToast(SignUpActivity.this, "User not found.");
                }
            } else {
                progressBar.hideProgressBar();
                // If sign in fails, display a message to the user.
                Toast.makeText(SignUpActivity.this, "Authentication failed\n" + task.getException(), Toast.LENGTH_SHORT).show();
                Log.w(TAG, "signInWithCredential:failure", task.getException());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isPasswordValid(String passwd) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(passwd);
        return matcher.matches();
    }
}