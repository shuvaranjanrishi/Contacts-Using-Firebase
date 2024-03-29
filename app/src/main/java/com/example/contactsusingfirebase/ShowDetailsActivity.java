package com.example.contactsusingfirebase;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ShowDetailsActivity extends AppCompatActivity {

    private TextView nameTV, phoneNoTV;
    private Intent intent;

    private String contactId, name, phoneNo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initialize();

        getDataFromMainActivity();

        setData();
    }

    private void setData() {
        setTitle("Update "+name);
        nameTV.setText(name);
        phoneNoTV.setText(phoneNo);
    }

    private void getDataFromMainActivity() {
        if (intent != null) {
            contactId = intent.getStringExtra("CONTACT_ID");
            name = intent.getStringExtra("NAME");
            phoneNo = intent.getStringExtra("PHONE_NO");
        }
    }

    private void initialize() {
        nameTV = findViewById(R.id.nameTVId);
        phoneNoTV = findViewById(R.id.phoneNoTVId);
        intent = getIntent();
    }

    public void deleteBtnClickAction(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure to delete the Contact");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteContact();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void editContactBtnClickAction(View view) {
        Intent intent = new Intent(ShowDetailsActivity.this,AddContactActivity.class);
        intent.putExtra("CONTACT_ID",contactId);
        intent.putExtra("NAME",name);
        intent.putExtra("PHONE_NO",phoneNo);
        startActivity(intent);
    }

    private void deleteContact() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        Query applesQuery = ref.child("users").child(userId).child("contacts").orderByChild("contactId").equalTo(contactId);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {

                    appleSnapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                finish();
                                Toast.makeText(ShowDetailsActivity.this, "Contact Delete Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("myError: ", "onCancelled", databaseError.toException());
            }
        });
    }

}
