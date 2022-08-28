package com.school.parentandroidapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.school.parentandroidapp.Adapters.SelectStudentAdapter;
import com.school.parentandroidapp.R;

public class HomeScreenActivity extends AppCompatActivity {

    private String parentPhone;
    private int studentSID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //Getting EXTRAS
        parentPhone = getIntent().getStringExtra(SelectStudentActivity.EXTRA_PHONE);
        studentSID = getIntent().getIntExtra(SelectStudentActivity.EXTRA_SID,0);
    }
}