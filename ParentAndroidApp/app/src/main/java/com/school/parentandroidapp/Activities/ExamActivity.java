package com.school.parentandroidapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.school.parentandroidapp.Adapters.ExamDetailsAdapter;
import com.school.parentandroidapp.R;
import com.school.parentandroidapp.Server.ServerConnection;
import com.school.parentandroidapp.parser.json.JSONArray;

public class ExamActivity extends AppCompatActivity {

    private static final String TAG = "ExamActivity";
    private int studentSid;

    private ServerConnection serverConnection;

    private ProgressBar progressBar;
    private RecyclerView examRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        //Getting ServerConnection
        serverConnection = MainActivity.serverConnection;

        //Getting Intent Extra
        studentSid = getIntent().getIntExtra(HomeScreenActivity.EXTRA_SID,0);

        //Getting Views
        progressBar = findViewById(R.id.examActivity_progressBar);
        examRecyclerView = findViewById(R.id.examActivity_recylcerView);

        examRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Filling Exam Details
        new GetExamData().execute(studentSid);
    }

    private class GetExamData extends AsyncTask<Integer,Void, JSONArray>{
        @Override
        protected void onPreExecute() {
            Log.d(TAG,"Inside PreExecute");
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONArray doInBackground(Integer... integers) {
            int sid = integers[0];

            JSONArray examJsonArray = serverConnection.getExamDetails(sid);

            return examJsonArray;
        }

        @Override
        protected void onPostExecute(JSONArray objects) {
            Log.d(TAG,"Inside PostExecute");
            Log.d(TAG,objects.toString());
            progressBar.setVisibility(View.GONE);
            examRecyclerView.setAdapter(new ExamDetailsAdapter(objects));
        }
    }
}