package com.school.parentandroidapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.school.parentandroidapp.Adapters.SelectStudentAdapter;
import com.school.parentandroidapp.Listeners.RecyclerItemClickListener;
import com.school.parentandroidapp.R;
import com.school.parentandroidapp.Server.ServerConnection;
import com.school.parentandroidapp.parser.json.JSONArray;
import com.school.parentandroidapp.parser.json.JSONObject;

public class SelectStudentActivity extends AppCompatActivity {

    private static final String TAG = "SelectStudentActivity";
    public static final String EXTRA_PHONE = "EXTRA_PHONE";
    public static final String EXTRA_SID = "EXTRA_SID";

    private String parentPhone;
    private RecyclerView studentRecyclerView;
    private ProgressBar progressBar;

    private ServerConnection serverConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_student);

        //Getting Phone No
        SharedPreferences sp = getSharedPreferences("login_data",MODE_PRIVATE);
        parentPhone = sp.getString("phone","default");

        //Getting ServerConnection
        serverConnection = MainActivity.serverConnection;

        //Getting Views
        studentRecyclerView = findViewById(R.id.studentRecyclerView);
        progressBar = findViewById(R.id.progressBar);

        studentRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Loading Students Details
        new GetStudentList().execute(parentPhone);

        //Setting Listener
        studentRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, studentRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(SelectStudentActivity.this,HomeScreenActivity.class);
                intent.putExtra(EXTRA_PHONE,parentPhone);
                SelectStudentAdapter adapter = (SelectStudentAdapter) studentRecyclerView.getAdapter();
                int sid = adapter.getSid(position);
                intent.putExtra(EXTRA_SID,sid);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
    }

    private class GetStudentList extends AsyncTask<String, Void, JSONArray>{

        @Override
        protected void onPreExecute() {
            Log.d(TAG,"Inside PreExecute");
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONArray doInBackground(String... strings) {
            String phone = strings[0];
            return serverConnection.getStudentList(phone);
        }

        @Override
        protected void onPostExecute(JSONArray objects) {
            Log.d(TAG,"Inside PostExecute");
            progressBar.setVisibility(View.GONE);
            studentRecyclerView.setAdapter(new SelectStudentAdapter(objects));
        }
    }
}