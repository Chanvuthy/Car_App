package com.example.car_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.SignInClient;
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
import com.google.firebase.database.FirebaseDatabase;


public class LogInActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN =123 ;
    private static final String TAG ="123" ;
    GoogleSignInClient mGoogleSignInClient;
    private Button btnLogIn, btnLogInWithGoogle;
    private EditText tvEmail, tvPassword;
    private TextView tvSignUp, txtFogotPassword;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        LoadingDialog loadingDialog = new LoadingDialog(LogInActivity.this);


        btnLogIn = findViewById(R.id.btnLogIn);
        btnLogInWithGoogle = findViewById(R.id.btnLogInWithGoogle);
        tvEmail = findViewById(R.id.edtEmail);
        tvPassword = findViewById(R.id.edtPassword);
        tvSignUp = findViewById(R.id.tvSignUp);
        txtFogotPassword = findViewById(R.id.txtForgotYourPassword);



        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser != null){

            startActivity(new Intent(LogInActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }

        txtFogotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();
                Intent intent = new Intent(LogInActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();
                startActivity(new Intent(LogInActivity.this,SignUpActivity.class));
            }
        });
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogIn();
            }
        });

        btnLogInWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signIn();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("195016510584-frbdmulbm7jdsfrkk7ggs00n51tdnc5c.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        btnLogInWithGoogle.setVisibility(View.VISIBLE);
    }
    private void LogIn(){

        String email1 = tvEmail.getText().toString().trim();
        String password1 = tvPassword.getText().toString().trim();

        if(email1.isEmpty()){
            tvEmail.setError("Email Required!");
            tvEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email1).matches()){
            tvEmail.setError("Email not match!");
            tvEmail.requestFocus();
            return;
        }
        if(password1.isEmpty()){
            tvPassword.setError("Password Required!");
            tvPassword.requestFocus();
            return;
        }
        mAuth.signInWithEmailAndPassword(email1,password1)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        LoadingDialog loadingDialog = new LoadingDialog(LogInActivity.this);
                        if(task.isSuccessful()){
                            try {
                                loadingDialog.startLoadingDialog();
                                Intent intent = new Intent(LogInActivity.this,MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                Toast.makeText(LogInActivity.this,"LogIn Successfully",Toast.LENGTH_SHORT).show();
                                finish();
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }else{
                            loadingDialog.dismissDialog();
                            Toast.makeText(LogInActivity.this,"Failed to LogIn",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        LoadingDialog loadingDialog = new LoadingDialog(LogInActivity.this);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                handleSignInResult(task);
//            }
//            catch (Exception e){
//                Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
//            }

            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            if(signInAccountTask.isSuccessful()){

                try {
                    loadingDialog.startLoadingDialog();
                    GoogleSignInAccount account = signInAccountTask.getResult(ApiException.class);
                    if(account != null){
                        AuthCredential authCredential = GoogleAuthProvider
                                .getCredential(account.getIdToken(),null);
                        mAuth.signInWithCredential(authCredential)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if(task.isSuccessful()){
                                            loadingDialog.startLoadingDialog();
                                            startActivity(new Intent(LogInActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                            Toast.makeText(LogInActivity.this,"Log In Successfully",Toast.LENGTH_SHORT).show();
                                        }else{
                                            loadingDialog.dismissDialog();
                                            Toast.makeText(getApplicationContext(),"Log In Failed",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        System.exit(0);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Toast.makeText(LogInActivity.this,"Sign In Successfully",Toast.LENGTH_LONG).show();
            FirebaseGoogleAuth(account);
            // Signed in successfully, show authenticated UI.

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(LogInActivity.this,"Sign In Failed",Toast.LENGTH_LONG).show();
            FirebaseGoogleAuth(null);

        }
    }
    private void FirebaseGoogleAuth(GoogleSignInAccount account){
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LogInActivity.this,"Sign In Successfully",Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(LogInActivity.this,"Sign In Failed",Toast.LENGTH_LONG).show();

                }
            }
        });
    }
}