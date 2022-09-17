package com.school.parentandroidapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.school.parentandroidapp.Adapters.SelectStudentAdapter;
import com.school.parentandroidapp.R;
import com.school.parentandroidapp.Server.ServerConnection;
import com.school.parentandroidapp.Static.Validator;
import com.school.parentandroidapp.parser.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.util.Base64;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeScreenActivity extends AppCompatActivity {

    private CircleImageView studentImageView;
    private TextView studentNameTextView,standardTextView,divisionTextView;
    private ProgressBar profileLayoutProgressBar;

    private String parentPhone;
    private int studentSid;

    private ServerConnection serverConnection;

    private static final String TAG = "HomeScreenActivity";
    public static final String EXTRA_SID = "EXTRA_SID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //Getting EXTRAS
        parentPhone = getIntent().getStringExtra(SelectStudentActivity.EXTRA_PHONE);
        studentSid = getIntent().getIntExtra(SelectStudentActivity.EXTRA_SID,0);

        //Getting ServerConnection
        serverConnection = MainActivity.serverConnection;

        //Getting Views
        studentImageView = findViewById(R.id.homeScreenActivity_studentImageView);
        studentNameTextView = findViewById(R.id.homeScreenActivity_studentNameTextView);
        standardTextView = findViewById(R.id.homeScreenActivity_standardTextView);
        divisionTextView = findViewById(R.id.homeScreenActivity_divisionTextView);
        profileLayoutProgressBar = findViewById(R.id.homeScreenActivity_profileLayout_progressBar);

        //Loading Student Profile
        new GetStudentData().execute(studentSid);
    }

    class GetStudentData extends AsyncTask<Integer,Void,JSONObject> {

        @Override
        protected void onPreExecute() {
            //View Visibility
            studentImageView.setVisibility(View.INVISIBLE);
            studentNameTextView.setVisibility(View.INVISIBLE);
            standardTextView.setVisibility(View.INVISIBLE);
            divisionTextView.setVisibility(View.INVISIBLE);
            profileLayoutProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONObject doInBackground(Integer... integers) {
            int sid = integers[0];
            JSONObject studentJsonObject = serverConnection.getStudentData(sid);
            return studentJsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            //Setting Image
            String imgString = jsonObject.getString("img");
            byte[] imgArray = Base64.getDecoder().decode(imgString);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imgArray,0, imgArray.length);
            studentImageView.setImageBitmap(bitmap);

            //Setting Name
            String firstname = jsonObject.getString("firstname");
            String lastname = jsonObject.getString("lastname");
            Log.d(TAG,firstname);
            Log.d(TAG,lastname);
            String name = Validator.getFormattedName(firstname,lastname);
            Log.d(TAG,name);
            studentNameTextView.setText(name);

            //Setting standard
            int standard = jsonObject.getInt("standard");
            standardTextView.setText("Standard : "+standard);

            //Setting Division
            String division = jsonObject.getString("division");
            divisionTextView.setText("Division : "+division);

            //View Visibility
            profileLayoutProgressBar.setVisibility(View.GONE);
            studentImageView.setVisibility(View.VISIBLE);
            studentNameTextView.setVisibility(View.VISIBLE);
            standardTextView.setVisibility(View.VISIBLE);
            divisionTextView.setVisibility(View.VISIBLE);
        }
    }

    public void examLayoutOnClick(View view){
        Intent intent = new Intent(this,ExamActivity.class);
        intent.putExtra(EXTRA_SID,studentSid);
        startActivity(intent);
    }
}