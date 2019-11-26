package com.example.contactsusingfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
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
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        initialize();
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

        Contact contact = new Contact(name,phoneNo);

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
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }
}
