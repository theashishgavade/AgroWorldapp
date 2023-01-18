package com.project.agroworld.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.project.agroworld.DashboardActivity;
import com.project.agroworld.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private static final int RC_SIGN_IN = 99;
    private static final String TAG = "GoogleLogin";
    ImageView ivGoogleLogin, ivFacebookLogin, ivGithubLogin, ivInstaLogin;
    TextView tvNewUser, tvForgetPasswd;
    Button btnLoginUp;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    EditText etEmail, etPasswd;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginWithGoogle();
        mAuth = FirebaseAuth.getInstance();
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
        progressBar = findViewById(R.id.progress_circular);
        etEmail = findViewById(R.id.etEmail);
        etPasswd = findViewById(R.id.etPasswd);

        ivGoogleLogin.setOnClickListener((View.OnClickListener) LoginActivity.this);
        ivFacebookLogin.setOnClickListener((View.OnClickListener) LoginActivity.this);
        ivGithubLogin.setOnClickListener((View.OnClickListener) LoginActivity.this);
        ivInstaLogin.setOnClickListener((View.OnClickListener) LoginActivity.this);
        tvNewUser.setOnClickListener((View.OnClickListener) LoginActivity.this);
        tvForgetPasswd.setOnClickListener((View.OnClickListener) LoginActivity.this);
        btnLoginUp.setOnClickListener((View.OnClickListener) LoginActivity.this);

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

    private void checkDataValidOrNot(){
        progressBar.setVisibility(View.VISIBLE);
        String email = etEmail.getText().toString();
        String passwd = etPasswd.getText().toString();

        if (!email.isEmpty() && passwd.length() > 6){
            signInUser(email, passwd);
        } else {
            showToast("Invalid formation entered");
        }
    }

    private void signInUser(String email, String passwd) {
        mAuth.signInWithEmailAndPassword(email, passwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    // Sign in success, update UI with the signed-in user's information
                    showToast("signInWithEmail:success");
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(intent);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    showToast("Authentication failed.");
                }
            }
        });
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivFacebookLogin:
                showToast("Facebook login not available");

                break;
            case R.id.ivGithubLogin:
                showToast("GitHub login not available");
                break;
            case R.id.ivInstaLogin:
                showToast("Instagram login not available");
                break;
            case R.id.ivGoogleLogin:
                signIn();
                break;
            case  R.id.tvForgetPasswd:
                showToast("Try to login with new account");
                break;
            case  R.id.tvNewUser:
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                break;
            case R.id.btnLoginUp:
                checkDataValidOrNot();
                break;

        }
    }
}