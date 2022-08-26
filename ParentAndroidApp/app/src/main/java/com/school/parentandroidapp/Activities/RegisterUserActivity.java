package com.school.parentandroidapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.school.parentandroidapp.CustomComponents.DatePicker;
import com.school.parentandroidapp.R;
import com.school.parentandroidapp.Server.ServerConnection;
import com.school.parentandroidapp.Static.Constant;
import com.school.parentandroidapp.Static.Validator;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterUserActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private ServerConnection serverConnection;

    private EditText phoneRegisterEditText;
    private TextView dobTextView;
    private String phone;

    //EXTRAS
    private static final String TAG = "RegisterUserActivity";
    public static final String EXTRA_PHONE = "Phone";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        //Getting ServerConnection and Setting Current Context
        serverConnection = MainActivity.serverConnection;
        serverConnection.setCurrentContext(this);

        //Finding Views
        phoneRegisterEditText = findViewById(R.id.registerActivity_phoneEditText);
        dobTextView = findViewById(R.id.dobTextView);
    }

    public void dobTextViewOnClick(View view){
        DatePicker dobPicker = new DatePicker();
        dobPicker.show(getSupportFragmentManager(),"SELECT DOB");
    }

    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int dayOfMonth) {
        String selectedDate = dayOfMonth+"/"+(month+1)+"/"+year;
        dobTextView.setText(selectedDate);
    }

    public void registerButtonOnClick(View view){
        phone = phoneRegisterEditText.getText().toString();
        if( phoneRegisterEditText.getText().toString().isEmpty() ){
            Toast.makeText(this,"Enter Phone No",Toast.LENGTH_SHORT).show();
            return;
        }
        if( !Validator.isValidPhone(phone) ){
            Toast.makeText(this,"Enter a Valid 10 Digit Phone Number",Toast.LENGTH_SHORT).show();
            return;
        }

        String dob = dobTextView.getText().toString();
        if( dob.isEmpty() ){
            Toast.makeText(this,"Enter Date of Birth",Toast.LENGTH_SHORT).show();
            return;
        }

        new RegisterUser().execute(phone,dob);
    }

    private class RegisterUser extends AsyncTask<String,Void,Integer> {
        @Override
        protected Integer doInBackground(String... strings) {
            String phone = strings[0];
            String dobString = strings[1];

            Date dob = new Date();
            try {
                dob = new SimpleDateFormat("dd/MM/yyyy").parse(dobString);
            }catch(Exception e){
                Log.e(TAG,e.toString());
            }

            int responseCode = serverConnection.registerUser(phone,dob.getTime());

            return responseCode;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            switch(integer){
                case 0:
                    Intent intent = new Intent(RegisterUserActivity.this,SetPasswordNewUserActivity.class);
                    intent.putExtra(EXTRA_PHONE,phone);
                    startActivity(intent);
                    break;

                case 1:
                    Toast.makeText(RegisterUserActivity.this,"Entered Phone No is not Registered with School",Toast.LENGTH_LONG).show();
                    break;

                case 2:
                    Toast.makeText(RegisterUserActivity.this,"Entered Phone No is already Registered.Try Logging In",Toast.LENGTH_LONG).show();
                    break;

                case 3:
                    Toast.makeText(RegisterUserActivity.this,"Entered Date of Birth is Wrong",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}