package com.example.car_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText emailSignUp, passwordSignUp,tvEmailReset;
    private Button btnResetPassword;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        tvEmailReset = findViewById(R.id.tvEmailReset);
        emailSignUp = findViewById(R.id.tvEmailReset);
        passwordSignUp = findViewById(R.id.passwordSignUp);
        btnResetPassword = findViewById(R.id.btnSendResetMessage);
        mAuth = FirebaseAuth.getInstance();
        LoadingDialog loadingDialog = new LoadingDialog(ResetPasswordActivity.this);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email2 = tvEmailReset.getText().toString().trim();
                if(email2.isEmpty()){
                    tvEmailReset.setError("Email Required!");
                    tvEmailReset.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email2).matches()){
                    tvEmailReset.setError("Email not match!");
                    tvEmailReset.requestFocus();
                    return;
                }
                mAuth.fetchSignInMethodsForEmail(emailSignUp.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if(task.getResult().getSignInMethods().isEmpty()){

                            Toast.makeText(ResetPasswordActivity.this,"This email is not registered, you can create new account",Toast.LENGTH_LONG).show();

                        }else{
                            mAuth.sendPasswordResetEmail(emailSignUp.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        loadingDialog.startLoadingDialog();
                                        Toast.makeText(ResetPasswordActivity.this,"Reset Password has been send to your enail",Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(ResetPasswordActivity.this,LogInActivity.class));
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ResetPasswordActivity.this,LogInActivity.class);
        startActivity(intent);
    }
}