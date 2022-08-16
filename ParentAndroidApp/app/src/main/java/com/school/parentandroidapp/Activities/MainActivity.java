package com.school.parentandroidapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.school.parentandroidapp.R;
import com.school.parentandroidapp.Server.ServerConnection;
import com.school.parentandroidapp.Static.Constant;

import java.net.Socket;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new ConnectingToServer().execute();
    }

    public class ConnectingToServer extends AsyncTask<Void, Void, Socket> {

        @Override
        protected Socket doInBackground(Void... voids) {
            Socket socket = null;
            try{
                Log.d(TAG,"Connecting to Server");
                socket = new Socket(Constant.IP_ADDRESS,Constant.PORT);
                Log.d(TAG,"Connected to Server");
            }catch(Exception e){
                Log.d(TAG,"Unable to Connect to Server");
                Log.e(TAG,e.toString());
            }

            return socket;
        }

        @Override
        protected void onPostExecute(Socket socket) {
            super.onPostExecute(socket);

            if( socket != null && socket.isConnected() ){
                ServerConnection serverConnection = new ServerConnection(socket);
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent(MainActivity.this,NoConnectionActivity.class);
                startActivity(intent);
            }
        }
    }
}