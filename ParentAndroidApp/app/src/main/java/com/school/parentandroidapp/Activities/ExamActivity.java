package com.school.parentandroidapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.school.parentandroidapp.R;
import com.school.parentandroidapp.Server.ServerConnection;

public class ExamActivity extends AppCompatActivity {

    private int studentSid;

    private ServerConnection serverConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        //Getting ServerConnection
        serverConnection = MainActivity.serverConnection;

        //Getting Intent Extra
        studentSid = getIntent().getIntExtra(HomeScreenActivity.EXTRA_SID,0);
    }
}