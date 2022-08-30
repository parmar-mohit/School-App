package com.school.parentandroidapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.school.parentandroidapp.R;

public class NoConnectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_connection);
    }

    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
    }
}