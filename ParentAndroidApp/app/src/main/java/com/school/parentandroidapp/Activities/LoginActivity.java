package com.school.parentandroidapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.school.parentandroidapp.R;
import com.school.parentandroidapp.Server.ServerConnection;
import com.school.parentandroidapp.Static.Validator;

public class LoginActivity extends AppCompatActivity {

    private ServerConnection serverConnection;

    private static final String TAG = "LoginActivity";

    private EditText phoneEditText,passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Checking if user has previously logged in
        SharedPreferences sp = getSharedPreferences("login_data",MODE_PRIVATE);
        String phone = sp.getString("phone","default");

        if( !phone.equals("default") ){
            //Moving to SelectStudentActivity
            Intent intent = new Intent(this,SelectStudentActivity.class);
            startActivity(intent);
        }
        

        //Getting ServerConnection and Setting Context
        serverConnection = MainActivity.serverConnection;
        serverConnection.setCurrentContext(this);

        //Getting Views
        phoneEditText = findViewById(R.id.loginActivity_phoneEditText);
        passwordEditText = findViewById(R.id.loginActivity_passwordEditText);
    }

    public void loginButtonOnClick(View view){
        String phone = phoneEditText.getText().toString();

        if( !Validator.isValidPhone(phone) ){
            Toast.makeText(this,"Enter a Valid 10 digit Phone No",Toast.LENGTH_LONG).show();
            return;
        }

        String password = passwordEditText.getText().toString();

        new LoginUser().execute(phone,password);
    }

    public void registerTextViewOnClick(View view){
        Intent intent = new Intent(this, RegisterUserActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
    }

    private class LoginUser extends AsyncTask<String, Void, String> {
        private String phone;
        private String password;

        @Override
        protected String doInBackground(String... strings) {
            phone = strings[0];
            password = strings[1];

            return serverConnection.getPassword(phone);
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d(TAG,"Password : "+s);
            if(  s.equals( Validator.hashPassword(password) ) ){
                //Saving User details fo future login
                SharedPreferences sp = getSharedPreferences("login_data",MODE_PRIVATE);
                SharedPreferences.Editor ed = sp.edit();
                ed.putString("phone",phone);
                ed.apply();

                //Moving to SelectStudentActivity
                Intent intent = new Intent(LoginActivity.this,SelectStudentActivity.class);
                startActivity(intent);
                finish();
            }else if( s.equals("not_registered") ){
                Toast.makeText(LoginActivity.this,"User not registered",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(LoginActivity.this,"Invalid Credentials",Toast.LENGTH_SHORT).show();
            }
        }
    }
}