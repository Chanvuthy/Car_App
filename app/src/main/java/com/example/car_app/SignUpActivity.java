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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private EditText firstNameSignUp, lastNameSignUp, emailSignUp, passwordSignUp, reEnterPasswordSignUp;
    private Button btnSignUp;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        LoadingDialog loadingDialog = new LoadingDialog( SignUpActivity.this);

        firstNameSignUp = findViewById(R.id.firstNameSignUp);
        lastNameSignUp = findViewById(R.id.lastNameSignUp);
        emailSignUp = findViewById(R.id.tvEmailReset);
        passwordSignUp = findViewById(R.id.passwordSignUp);
        reEnterPasswordSignUp = findViewById(R.id.reEnterPasswordSignUp);
        btnSignUp = findViewById(R.id.btnSignUp);

        mAuth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();
                signUp();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SignUpActivity.this,LogInActivity.class);
        startActivity(intent);
    }

    private void signUpTest(){
        String email = emailSignUp.getText().toString().trim();
        String password = passwordSignUp.getText().toString().trim();
        String reEnterPassword = reEnterPasswordSignUp.getText().toString().trim();
        String lastName = lastNameSignUp.getText().toString().trim();
        String firstName = firstNameSignUp.getText().toString().trim();

        if(firstName.isEmpty()){
            firstNameSignUp.setError("FirstName Required!");
            firstNameSignUp.requestFocus();
            return;
        }
        if(lastName.isEmpty()){
            lastNameSignUp.setError("LastName Required!");
            lastNameSignUp.requestFocus();
            return;
        }
        if(email.isEmpty()){
            emailSignUp.setError("Email Required!");
            emailSignUp.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailSignUp.setError("Email not match!");
            emailSignUp.requestFocus();
            return;
        }
        if(password.isEmpty()){
            passwordSignUp.setError("Password Required!");
            passwordSignUp.requestFocus();
            return;
        }
        if(password.length()<6){
            passwordSignUp.setError("Required at Least 6");
            passwordSignUp.requestFocus();
            return;
        }
        if(reEnterPassword.isEmpty()){
            reEnterPasswordSignUp.setError("Confirm Password!");
            reEnterPasswordSignUp.requestFocus();
            return;
        }
        if(!reEnterPassword.equals(password)){
            reEnterPasswordSignUp.setError("Password not match!");
            reEnterPasswordSignUp.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    User user = new User(firstName,lastName,email,password,reEnterPassword);

                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(SignUpActivity.this,"User has been Sign Up",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(SignUpActivity.this,"Failed to Sign Up",Toast.LENGTH_LONG).show();
                            }
                        }
                    });


                }

            }
        });

    }

    private void signUp(){
        String email = emailSignUp.getText().toString().trim();
        String password = passwordSignUp.getText().toString().trim();
        String reEnterPassword = reEnterPasswordSignUp.getText().toString().trim();
        String lastName = lastNameSignUp.getText().toString().trim();
        String firstName = firstNameSignUp.getText().toString().trim();

        if(firstName.isEmpty()){
            firstNameSignUp.setError("FirstName Required!");
            firstNameSignUp.requestFocus();
            return;
        }
        if(lastName.isEmpty()){
            lastNameSignUp.setError("LastName Required!");
            lastNameSignUp.requestFocus();
            return;
        }
        if(email.isEmpty()){
            emailSignUp.setError("Email Required!");
            emailSignUp.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailSignUp.setError("Email not match!");
            emailSignUp.requestFocus();
            return;
        }
        if(password.isEmpty()){
            passwordSignUp.setError("Password Required!");
            passwordSignUp.requestFocus();
            return;
        }
        if(password.length()<6){
            passwordSignUp.setError("Required at Least 6");
            passwordSignUp.requestFocus();
            return;
        }
        if(reEnterPassword.isEmpty()){
            reEnterPasswordSignUp.setError("Confirm Password!");
            reEnterPasswordSignUp.requestFocus();
            return;
        }
        if(!reEnterPassword.equals(password)){
            reEnterPasswordSignUp.setError("Password not match!");
            reEnterPasswordSignUp.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser rUser = mAuth.getCurrentUser();
                    assert rUser != null;
                    String userId = rUser.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("userId",userId);
                    hashMap.put("firstName",firstName);
                    hashMap.put("lastName",lastName);
                    hashMap.put("email",email);
                    hashMap.put("password",password);
                    hashMap.put("re_Password",reEnterPassword);
                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SignUpActivity.this,"Sign Up Successfully",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpActivity.this,LogInActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else{
                                Toast.makeText(SignUpActivity.this,"Sign Up Failed",Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }

            }
        });


//        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//
//                if(task.isSuccessful()){
//                    User user = new User(firstName,lastName,email,password,reEnterPassword);
//
//                    FirebaseDatabase.getInstance().getReference("Users")
//                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if(!task.isSuccessful()){
//                                Toast.makeText(SignUpActivity.this,"User has been Sign Up",Toast.LENGTH_LONG).show();
//                                Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
//                                startActivity(intent);
//                                finish();
//                            }else{
//                                Toast.makeText(SignUpActivity.this,"Failed to Sign Up",Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    });
//
//
//                }
//
//            }
//        });
//        mAuth.createUserWithEmailAndPassword(email,password)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if(task.isSuccessful()){
//                    User user = new User(firstName,lastName,email,password,reEnterPassword);
//
//                    FirebaseDatabase.getInstance().getReference("Users")
//                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if(task.isSuccessful()){
//                                Toast.makeText(SignUpActivity.this,"User has been Sign Up",Toast.LENGTH_LONG).show();
//                                Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
//                                startActivity(intent);
//                                finish();
//
//                            }else{
//                                Toast.makeText(SignUpActivity.this,"Failed to Sign Up",Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    });
//
//                }
//
//            }
//        });
    }

}