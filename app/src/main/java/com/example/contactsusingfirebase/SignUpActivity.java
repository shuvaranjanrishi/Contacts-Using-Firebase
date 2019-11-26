package com.example.contactsusingfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SignUpActivity extends AppCompatActivity {

    private EditText nameET,emailET,passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initialize();
    }

    public void SignUpBtnClickAction(View view) {

        String name = nameET.getText().toString().trim();
        String email = emailET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();

        if(!validate(name,email,password)){
            return;
        }else {

            signUp(name,email,password);

        }
    }

    private void signUp(String name, String email, String password) {



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
    }

}
