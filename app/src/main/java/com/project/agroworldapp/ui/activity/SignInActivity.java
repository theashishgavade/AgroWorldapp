package com.project.agroworldapp.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.project.agroworldapp.R;
import com.project.agroworldapp.databinding.ActivityLoginBinding;
import com.project.agroworldapp.utils.Constants;
import com.project.agroworldapp.utils.CustomMultiColorProgressBar;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 99;
    private static final String TAG = "GoogleLogin";
    FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private CustomMultiColorProgressBar progressBar;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        progressBar = new CustomMultiColorProgressBar(this, getString(R.string.loader_message));
        loginWithGoogle();
        initViews();
    }

    private void initViews() {
        binding.btnGoogleLogin.setOnClickListener(SignInActivity.this);
        binding.tvNewUser.setOnClickListener(SignInActivity.this);
        binding.tvForgetPasswd.setOnClickListener(SignInActivity.this);
        binding.btnLoginUp.setOnClickListener(SignInActivity.this);
        binding.fabAdminContact.setOnClickListener(SignInActivity.this);
    }

    private void loginWithGoogle() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * On google account selection it will pass token ID to firebaseAuthWithGoogle()
     * Once successful It will identify the userType as per it will navigate user to particular screen.
     * */

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
                progressBar.hideProgressBar();
                // Google Sign In failed, update UI appropriately
                Constants.showToast(this, "Google sign in failed");
                Log.w(TAG, "Google sign in failed", e);
            }
        } else if (requestCode == Constants.LOGOUT_REQUEST_CODE) {
            mAuth.signOut();
            mGoogleSignInClient.revokeAccess();
        }
    }

    /*
    Once user select the appropiate email from dialog box it will itendify & navigate to the user as per verification
    */
    private void firebaseAuthWithGoogle(String idToken) {
        progressBar.showProgressBar();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        progressBar.hideProgressBar();
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "");
                        FirebaseUser user = mAuth.getCurrentUser();
                        assert user != null;
                        Constants.showToast(SignInActivity.this, "signInWithCredential:success\n" + user.getEmail());
                        Constants.identifyUser(user, SignInActivity.this);
                        finish();
                    } else {
                        progressBar.hideProgressBar();
                        // If sign in fails, display a message to the user.
                        Constants.showToast(SignInActivity.this, "signInWithCredential:failure");
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                    }
                });
    }


    private void checkDataValidOrNot() {
        progressBar.showProgressBar();
        String email = binding.etEmail.getText().toString();
        String passwd = binding.etPasswd.getText().toString();

        if (!email.isEmpty() && passwd.length() > 6) {
            signInUser(email, passwd);
        } else {
            Constants.showToast(this, "Invalid entry");
            progressBar.hideProgressBar();
        }
    }

    /**
    * Email password signUp
    * */
    private void signInUser(String email, String passwd) {
        mAuth.signInWithEmailAndPassword(email, passwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressBar.hideProgressBar();
                    // Sign in success, update UI with the signed-in user's information
                    Constants.showToast(SignInActivity.this, "Login Successful");
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        Constants.identifyUser(user, SignInActivity.this);
                    } else {
                        Constants.showToast(SignInActivity.this, "Null user found");
                    }
                } else {
                    progressBar.hideProgressBar();
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Constants.showToast(SignInActivity.this, task.getException().getLocalizedMessage());
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGoogleLogin:
                signIn();
                break;
            case R.id.tvForgetPasswd:
                Constants.showToast(this, "Try to login with new account");
                break;
            case R.id.tvNewUser:
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
                break;
            case R.id.btnLoginUp:
                checkDataValidOrNot();
                break;
            case R.id.fabAdminContact:
                Constants.adminEmailContact(this);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Agro world exit")
                .setIcon(R.drawable.app_icon4)
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        SignInActivity.super.onBackPressed();
                    }
                }).create().show();
    }
}

