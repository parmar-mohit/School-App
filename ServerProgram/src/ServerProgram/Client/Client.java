package ServerProgram.Client;

import ServerProgram.ActionCode.*;
import ServerProgram.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.SocketException;
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

                    case 13:
                        new GetTeacherInchargeStudentList(jsonObject,this).start();
                        break;

                    case 14:
                        new GetSubjectTeacherStudentList(jsonObject,this).start();
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
            int i = 0;
            while( i < jsonObject.toString().length() ) {
                if( jsonObject.toString().length() < i + 65535 ) {
                    dataOutputStream.writeUTF(jsonObject.toString().substring(i));
                }else{
                    dataOutputStream.writeUTF(jsonObject.toString().substring(i,i+65535));
                }
                i += 65535;
            }
            dataOutputStream.flush();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public String getIpAddress(){
        return socket.getInetAddress().getHostAddress();
    }
}