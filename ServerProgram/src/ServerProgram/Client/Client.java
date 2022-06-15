package ServerProgram.Client;

import ServerProgram.ActionCode.CreateTeacherId;
import ServerProgram.ActionCode.GetCredentials;
import ServerProgram.ActionCode.GetTeacherList;
import ServerProgram.Log;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Client extends Thread{
    public Socket socket;
    private ArrayList<Client> clientList;
    public Client(Socket socket,ArrayList<Client> clientList) {
        this.socket = socket;
        this.clientList = clientList;
    }

    @Override
    public void run() {
        try{
            exit:
            while(true){
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                JSONObject jsonObject = new JSONObject(dataInputStream.readUTF());

                switch(jsonObject.getInt("action_code")){
                    case 1:
                        new GetCredentials(jsonObject,this).start();
                        break;

                    case 2:
                        new CreateTeacherId(jsonObject,this).start();
                        break;

                    case 3:
                        new GetTeacherList(jsonObject,this).start();
                        break;

                    case 99:
                        Log.info("Closing Connection with Client at "+socket.getInetAddress().getHostAddress());
                        socket.close();
                        clientList.remove(this);
                        break exit;
                }
            }
        }catch(SocketException e){
            Log.info("Connection Closed with Client at "+socket.getInetAddress().getHostAddress());
            clientList.remove(this);
        }catch(Exception e){
            Log.error(e.toString());
        }
    }


    public void sendMessage(JSONObject jsonObject){
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(jsonObject.toString());
            dataOutputStream.flush();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}