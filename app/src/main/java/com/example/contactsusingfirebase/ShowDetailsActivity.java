package com.example.contactsusingfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ShowDetailsActivity extends AppCompatActivity {

    private TextView nameTV, phoneNoTV;
    private Intent intent;

    private String name, phoneNo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        initialize();

        getDataFromIntent();

        setData();
    }

    private void setData() {
        setTitle(name);
        nameTV.setText(name);
        phoneNoTV.setText(phoneNo);
    }

    private void getDataFromIntent() {
        if (intent != null) {
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
        Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show();
    }
}
