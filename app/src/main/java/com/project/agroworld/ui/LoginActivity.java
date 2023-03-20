package com.project.agroworld.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import com.project.agroworld.R;
import com.project.agroworld.utils.Constants;
import com.project.agroworld.utils.CustomMultiColorProgressBar;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private static final int RC_SIGN_IN = 99;
    private static final String TAG = "GoogleLogin";
    ImageView ivGoogleLogin, ivFacebookLogin, ivGithubLogin, ivInstaLogin;
    TextView tvNewUser, tvForgetPasswd;
    Button btnLoginUp;
    FirebaseAuth mAuth;
    EditText etEmail, etPasswd;
    private GoogleSignInClient mGoogleSignInClient;
    private CustomMultiColorProgressBar progressBar;
    private int backPressCount = 0;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        progressBar = new CustomMultiColorProgressBar(this, getString(R.string.loader_message));
        loginWithGoogle();
        initViews();
    }


    private void initViews() {
        ivGoogleLogin = findViewById(R.id.ivGoogleLogin);
        ivFacebookLogin = findViewById(R.id.ivFacebookLogin);
        ivGithubLogin = findViewById(R.id.ivGithubLogin);
        ivInstaLogin = findViewById(R.id.ivInstaLogin);
        tvNewUser = findViewById(R.id.tvNewUser);
        tvForgetPasswd = findViewById(R.id.tvForgetPasswd);
        btnLoginUp = findViewById(R.id.btnLoginUp);
        etEmail = findViewById(R.id.etEmail);
        etPasswd = findViewById(R.id.etPasswd);

        ivGoogleLogin.setOnClickListener(LoginActivity.this);
        ivFacebookLogin.setOnClickListener(LoginActivity.this);
        ivGithubLogin.setOnClickListener(LoginActivity.this);
        ivInstaLogin.setOnClickListener(LoginActivity.this);
        tvNewUser.setOnClickListener(LoginActivity.this);
        tvForgetPasswd.setOnClickListener(LoginActivity.this);
        btnLoginUp.setOnClickListener(LoginActivity.this);

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
        }else if (requestCode == Constants.LOGOUT_REQUEST_CODE){
            mAuth.signOut();
            mGoogleSignInClient.revokeAccess();
        }
    }


    private void firebaseAuthWithGoogle(String idToken) {
        progressBar.showProgressBar();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.hideProgressBar();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Constants.showToast(LoginActivity.this, "signInWithCredential:success");
                            Constants.identifyUser(user, LoginActivity.this);
                            finish();
                        } else {
                            progressBar.hideProgressBar();
                            // If sign in fails, display a message to the user.
                            Constants.showToast(LoginActivity.this, "signInWithCredential:failure");
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }


    private void checkDataValidOrNot() {
        progressBar.showProgressBar();
        String email = etEmail.getText().toString();
        String passwd = etPasswd.getText().toString();

        if (!email.isEmpty() && passwd.length() > 6) {
            signInUser(email, passwd);
        } else {
            Constants.showToast(this, "Invalid entry");
            progressBar.hideProgressBar();
        }
    }

    private void signInUser(String email, String passwd) {
        mAuth.signInWithEmailAndPassword(email, passwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressBar.hideProgressBar();
                    // Sign in success, update UI with the signed-in user's information
                    Constants.showToast(LoginActivity.this, "Login Successful");
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(intent);
                } else {
                    progressBar.hideProgressBar();
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Constants.showToast(LoginActivity.this, task.getException().getLocalizedMessage());
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivFacebookLogin:
                Constants.showToast(this, "Facebook login not available");

                break;
            case R.id.ivGithubLogin:
                Constants.showToast(this, "GitHub login not available");
                break;
            case R.id.ivInstaLogin:
                Constants.showToast(this, "Instagram login not available");
                break;
            case R.id.ivGoogleLogin:
                signIn();
                break;
            case R.id.tvForgetPasswd:
                Constants.showToast(this, "Try to login with new account");
                break;
            case R.id.tvNewUser:
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                break;
            case R.id.btnLoginUp:
                checkDataValidOrNot();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        backPressCount++;
        new AlertDialog.Builder(this)
                .setTitle("Agro world exit")
                .setIcon(R.drawable.app_icon4)
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        LoginActivity.super.onBackPressed();
                    }
                }).create().show();
    }
}

