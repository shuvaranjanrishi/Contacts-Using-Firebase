package com.example.contactsusingfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {

    private EditText emailET,passwordET;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initialize();
    }

    public void signInBtnClickAction(View view) {

        String email = emailET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();

        if(!validate(email,password)){
            return;
        }else {

            signIn(email,password);

        }
    }

    private void signIn(String email, String password) {

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    Toast.makeText(SignInActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignInActivity.this,MainActivity.class));
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean validate(String email, String password) {

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(email.isEmpty()){
            emailET.setError("Please Enter Your Email");
            return false;
        }else if(!email.matches(emailPattern)){
            emailET.setError("Please Enter a Valid Email");
            return false;
        }else if(password.isEmpty()){
            passwordET.setError("Please Enter a Password");
            return false;
        }else if(password.length() < 6){
            passwordET.setError("Password should be at least 6 character");
            return false;
        }

        return true;
    }

    public void SignUpTVClickAction(View view) {
        startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
    }

    private void initialize() {
        emailET = findViewById(R.id.emailETId);
        passwordET = findViewById(R.id.passwordETId);
        firebaseAuth = FirebaseAuth.getInstance();
    }
}
