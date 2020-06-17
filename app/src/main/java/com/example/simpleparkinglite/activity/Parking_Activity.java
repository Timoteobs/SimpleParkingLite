package com.example.simpleparkinglite.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.simpleparkinglite.R;

public class Parking_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_);
        getSupportActionBar().hide();
    }
}
