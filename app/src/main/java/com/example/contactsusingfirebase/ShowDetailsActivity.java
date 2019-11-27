package com.example.contactsusingfirebase;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ShowDetailsActivity  extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);


    }

    public void deleteBtnClickAction(View view){
        Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show();
    }
}
