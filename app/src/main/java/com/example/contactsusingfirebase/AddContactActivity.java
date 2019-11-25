package com.example.contactsusingfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddContactActivity extends AppCompatActivity {

    private EditText nameET,phoneNoET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        init();
    }

    private void init() {
        nameET = findViewById(R.id.nameETId);
        phoneNoET = findViewById(R.id.phoneNoETId);
    }

    public void saveContactBtnAction(View view) {
        String name = nameET.getText().toString().trim();
        String phoneNo = phoneNoET.getText().toString().trim();

        if(!validate(name,phoneNo)){
            return;
        }else {

        }

    }

    private boolean validate(String name, String phoneNo) {

        if(name.isEmpty()){
            nameET.setError("Please give a name");
            return false;
        }else if(name.length() < 3){
            nameET.setError("Name should be at least 3 character");
            return false;
        }else if(phoneNo.isEmpty()){
            phoneNoET.setError("Please give a phone number");
            return false;
        }else if(phoneNo.length() != 11){
            phoneNoET.setError("Phone number should be 11 digit");
            return false;
        }

        return true;
    }
}
