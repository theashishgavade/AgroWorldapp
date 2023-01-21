package com.project.agroworld.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 99;
    private static final String TAG = "GoogleLogin";
    private CustomMultiColorProgressBar progressBar;
    ImageView ivFbSignup, ivGithubSignup, ivInstaSignup, ivGoogleSignup;
    TextView tvHaveAnAccountUp;
    Button btnSignup;
    FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        progressBar = new CustomMultiColorProgressBar(this, "Please wait...\nWe're running your request");
        loginWithGoogle();
        initViews();
    }

    private void initViews() {
        ivFbSignup = findViewById(R.id.ivFbSignup);
        ivGithubSignup = findViewById(R.id.ivGithubSignup);
        ivInstaSignup = findViewById(R.id.ivInstaSignup);
        ivGoogleSignup = findViewById(R.id.ivGoogleSignup);
        tvHaveAnAccountUp = findViewById(R.id.tvHaveAnAccountUp);
        btnSignup = findViewById(R.id.btnSignup);

        ivFbSignup.setOnClickListener((View.OnClickListener) SignUpActivity.this);
        ivGithubSignup.setOnClickListener((View.OnClickListener) SignUpActivity.this);
        ivInstaSignup.setOnClickListener((View.OnClickListener) SignUpActivity.this);
        ivGoogleSignup.setOnClickListener((View.OnClickListener) SignUpActivity.this);


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
                showToast("Google sign in successful");
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                progressBar.hideProgressBar();
                // Google Sign In failed, update UI appropriately
                showToast("Google sign in failed");
                Log.w(TAG, "Google sign in failed", e);
            }
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
                            showToast("signInWithCredential:success");
                            Constants.identifyUser(user, SignUpActivity.this);
                        } else {
                            progressBar.hideProgressBar();
                            // If sign in fails, display a message to the user.
                            showToast("signInWithCredential:failure");
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivFbSignup:
                showToast("Facebook login not available");
                break;
            case R.id.ivGithubSignup:
                showToast("GitHub login not available");
                break;
            case R.id.ivInstaSignup:
                showToast("Instagram login not available");
                break;
            case R.id.ivGoogleSignup:
                signIn();
                break;
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}