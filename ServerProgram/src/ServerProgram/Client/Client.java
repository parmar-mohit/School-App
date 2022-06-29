package ServerProgram.Client;

import ServerProgram.ActionCode.*;
import ServerProgram.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Client extends Thread{
    private Socket socket;
    private ArrayList<Client> clientList;
    public Client(Socket socket,ArrayList<Client> clientList) {
        this.socket = socket;
        this.clientList = clientList;
    }

    @Override
    public void run() {
        try{
            while(true){
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                String recvString = dataInputStream.readUTF();
                JSONObject jsonObject;
                while(true) {
                    try {
                        jsonObject = new JSONObject(recvString);
                        break;
                    } catch (JSONException e) {
                        recvString += dataInputStream.readUTF();
                    }
                }

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

                    case 4:
                        new CreateClassroom(jsonObject,this).start();
                        break;

                    case 5:
                        new ChangePassword(jsonObject,this).start();
                        break;

                    case 6:
                        new UpdateTeacherAttributes(jsonObject,this).start();
                        break;

                    case 7:
                        new GetClassroomList(jsonObject,this).start();
                        break;

                    case 8:
                        new DeleteTeacherId(jsonObject,this).start();
                        break;

                    case 9:
                        new UpdateClassroom(jsonObject,this).start();
                        break;

                    case 10:
                        new DeleteClassroom(jsonObject,this).start();
                        break;

                    case 11:
                        new GetClassrooms(jsonObject,this).start();
                        break;

                    case 12:
                        new CreateStudentId(jsonObject,this).start();
                        break;
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

    public String getIpAddress(){
        return socket.getInetAddress().getHostAddress();
    }
}