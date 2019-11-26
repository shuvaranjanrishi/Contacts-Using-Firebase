package com.example.contactsusingfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Contact> contactList;
    private ContactAdapter contactAdapter;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        getDataFromFirebase();
    }

    private void getDataFromFirebase() {
        String userId = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference contactsRef = databaseReference.child("users").child(userId).child("contacts");

        contactsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    contactList.clear();

                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                        Contact contact = data.getValue(Contact.class);
                        contactList.add(contact);
                        contactAdapter = new ContactAdapter(contactList, MainActivity.this);
                        recyclerView.setAdapter(contactAdapter);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "No Contact Found !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Read Data Failed: " + databaseError.getMessage());
            }
        });
    }

    private void initialize() {
        recyclerView = findViewById(R.id.recyclerViewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactList = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void fabClickAction(View view) {
        startActivity(new Intent(MainActivity.this, AddContactActivity.class));
    }
}
