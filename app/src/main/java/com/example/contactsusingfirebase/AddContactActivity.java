package com.example.contactsusingfirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.UUID;

public class AddContactActivity extends AppCompatActivity {

    private ImageView imageView,imageView2;
    private EditText nameET, phoneNoET;
    private Button saveContactBtn;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private Uri uri;
    private String contactIdIntent, name, phoneNo;
    private String contactImage;

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
        name = nameET.getText().toString().trim();
        phoneNo = phoneNoET.getText().toString().trim();

        if (!validate(name, phoneNo)) {
            return;
        } else {

            if (contactIdIntent != null) {

                //uploadImageToFirebaseStorage();
               // updateData(contactImage, name,phoneNo);

            } else {
                insertDataToFirebase();
            }
        }

    }

    private void updateData(String contactImageLink, String name, String phoneNo) {

        String userId = firebaseAuth.getCurrentUser().getUid();

        DatabaseReference userRef = databaseReference.child("users").child(userId).child("contacts").child(contactIdIntent);

        Contact contact = new Contact(contactIdIntent, contactImageLink, name, phoneNo);
        
        userRef.setValue(contact).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(AddContactActivity.this, "Contact Saved Successfully", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddContactActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void insertDataToFirebase(){

        // progressBar.setVisibility(View.VISIBLE);
        final StorageReference imageRef = storageReference.child("image"+ UUID.randomUUID());

        imageRef.putFile(uri).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddContactActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                // progressBar.setVisibility(View.GONE);
            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        contactImage = uri.toString();
                        saveContactToFirebase(contactImage);
                    }
                });
            }
        });
    }

    private void saveContactToFirebase(String contactImage) {

        String userId = firebaseAuth.getCurrentUser().getUid();

        DatabaseReference userRef = databaseReference.child("users").child(userId).child("contacts");

        String contactId = userRef.push().getKey();

        Contact contact = new Contact(contactId,contactImage, name, phoneNo);

        userRef.child(contactId).setValue(contact).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(AddContactActivity.this, "Contact Saved Successfully", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddContactActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addPhotoBtnClickAction(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if(requestCode == 0){
                uri = data.getData();
                imageView.setImageURI(uri);
            }
        }
    }

    private boolean validate(String name, String phoneNo) {

        if (name.isEmpty()) {
            nameET.setError("Please give a name");
            return false;
        } else if (name.length() < 3) {
            nameET.setError("Name should be at least 3 character");
            return false;
        } else if (phoneNo.isEmpty()) {
            phoneNoET.setError("Please give a phone number");
            return false;
        } else if (phoneNo.charAt(0) != '0' || phoneNo.charAt(1) != '1') {
            phoneNoET.setError("Invalid Phone No (must be 01 first)");
            return false;
        } else if (phoneNo.length() != 11) {
            phoneNoET.setError("Phone number should be 11 digit");
            return false;
        }

        return true;
    }

    private void initialize() {
        imageView = findViewById(R.id.imageViewId);
        imageView2 = findViewById(R.id.imageView2Id);
        nameET = findViewById(R.id.nameETId);
        phoneNoET = findViewById(R.id.phoneNoETId);
        saveContactBtn = findViewById(R.id.saveContactBtnId);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
    }
}
