package com.school.parentandroidapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.school.parentandroidapp.R;
import com.school.parentandroidapp.Server.ServerConnection;
import com.school.parentandroidapp.Static.Validator;

public class SetPasswordNewUserActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private ServerConnection serverConnection;

    private EditText passwordEditText, confirmPasswordEditText;
    private CheckBox showPasswordCheckBox;
    private String phone;

    private static final String TAG = "SetPasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password_new_user);

        //Getting ServerConnection and Setting Context
        serverConnection = MainActivity.serverConnection;
        serverConnection.setCurrentContext(this);

        //Getting Intent Extras
        phone = getIntent().getStringExtra(RegisterUserActivity.EXTRA_PHONE);

        //Getting Views
        passwordEditText = findViewById(R.id.setPasswordActivity_passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        showPasswordCheckBox = findViewById(R.id.showPasswordCheckBox);

        //Setting Listeners
        showPasswordCheckBox.setOnCheckedChangeListener(this);
    }

    public void setPasswordButtonOnClick(View view){
        String password = passwordEditText.getText().toString();
         if( !Validator.isValidPassword(password) ){
             Toast.makeText(this,"Password must contain 1 uppercase letter,1 lowercase letter and 1 digit",Toast.LENGTH_LONG).show();
             return;
         }

         String confirmPassword = confirmPasswordEditText.getText().toString();

         if( !password.equals(confirmPassword) ){
             Toast.makeText(this,"Passwords do not match",Toast.LENGTH_SHORT).show();
             return;
         }

         new SetPassword().execute(phone,password);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if( isChecked ){
            passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            confirmPasswordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }else {
            passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            confirmPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    private class SetPassword extends AsyncTask<String, Void, Integer>{
        @Override
        protected Integer doInBackground(String... strings) {
            String phone = strings[0];
            String password = strings[1];

            String hashedPassword = Validator.hashPassword(password);

            int responseCode = serverConnection.setPasswordNewUser(phone,hashedPassword);

            return responseCode;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            switch( integer ){
                case 0:
                    //Saving User details fo future login
                    SharedPreferences sp = getSharedPreferences("login_data",MODE_PRIVATE);
                    SharedPreferences.Editor ed = sp.edit();
                    ed.putString("phone",phone);
                    ed.apply();

                    //Moving to SelectStudentActivity
                    Intent intent = new Intent(SetPasswordNewUserActivity.this,SelectStudentActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }
}