package com.example.contactsusingfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddContactActivity extends AppCompatActivity {

    private EditText nameET,phoneNoET;
    private Button saveContactBtn;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private String contactIdIntent, name, phoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        setTitle("Create New Contact");

        initialize();

        getDataFromIntent();
    }

    private void getDataFromIntent() {

        contactIdIntent = getIntent().getStringExtra("CONTACT_ID");

        if (contactIdIntent != null) {
            name = getIntent().getStringExtra("NAME");
            phoneNo = getIntent().getStringExtra("PHONE_NO");

            nameET.setText(name);
            phoneNoET.setText(phoneNo);
            saveContactBtn.setText("Update Contact");
            setTitle(name);
        }
    }

    public void saveContactBtnAction(View view) {
        String name = nameET.getText().toString().trim();
        String phoneNo = phoneNoET.getText().toString().trim();

        if(!validate(name,phoneNo)){
            return;
        }else {
            insertData(name,phoneNo);
        }

    }

    private void insertData(String name, String phoneNo) {

        String userId = firebaseAuth.getCurrentUser().getUid();

        DatabaseReference userRef = databaseReference.child("users").child(userId).child("contacts");

        Contact contact = new Contact(userRef.push().getKey(),name,phoneNo);

        userRef.push().setValue(contact).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    Toast.makeText(AddContactActivity.this, "Contact Saved Successfully", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddContactActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
        }else if(phoneNo.charAt(0) != '0' || phoneNo.charAt(1) != '1'){
            phoneNoET.setError("Invalid Phone No (must be 01 first)");
            return false;
        }else if(phoneNo.length() != 11){
            phoneNoET.setError("Phone number should be 11 digit");
            return false;
        }

        return true;
    }

    private void initialize() {
        nameET = findViewById(R.id.nameETId);
        phoneNoET = findViewById(R.id.phoneNoETId);
        saveContactBtn = findViewById(R.id.saveContactBtnId);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }
}
