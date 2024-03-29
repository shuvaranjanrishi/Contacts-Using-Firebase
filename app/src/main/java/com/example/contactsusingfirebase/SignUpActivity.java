package com.example.contactsusingfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private EditText nameET,emailET,passwordET;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initialize();
    }

    public void signUpBtnClickAction(View view) {

        String name = nameET.getText().toString().trim();
        String email = emailET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();

        if(!validate(name,email,password)){
            return;
        }else {

            signUp(name,email,password);

        }
    }

    private void signUp(final String name, final String email, final String password) {

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    String userId = firebaseAuth.getUid();
                    DatabaseReference userRef = databaseReference.child("users").child(userId);

                    HashMap<String,Object> user = new HashMap<>();
                    user.put("name",name);
                    user.put("email",email);

                    userRef.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(SignUpActivity.this, "Sign Up Successful", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private boolean validate(String name, String email, String password) {

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(name.isEmpty()){
            nameET.setError("Please Enter Your Name");
            return false;
        }else if(name.length() < 3){
            nameET.setError("Name should be at least 3 character");
            return false;
        }else if(email.isEmpty()){
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

    private void initialize() {
        nameET = findViewById(R.id.nameETId);
        emailET = findViewById(R.id.emailETId);
        passwordET = findViewById(R.id.passwordETId);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

}
