package ServerProgram.Client;

import ServerProgram.ActionCode.*;
import ServerProgram.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Client extends Thread{
    private Socket socket;
    private ArrayList<Client> clientList;
    private Thread currentWorkingThread;
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
                        currentWorkingThread = new GetCredentials(jsonObject,this);
                        break;

                    case 2:
                        currentWorkingThread = new CreateTeacherId(jsonObject,this);
                        break;

                    case 3:
                        currentWorkingThread = new GetTeacherList(jsonObject,this);
                        break;

                    case 4:
                        currentWorkingThread = new CreateClassroom(jsonObject,this);
                        break;

                    case 5:
                        currentWorkingThread = new ChangePassword(jsonObject,this);
                        break;

                    case 6:
                        currentWorkingThread = new UpdateTeacherAttributes(jsonObject,this);
                        break;

                    case 7:
                        currentWorkingThread = new GetClassroomListForPrincipal(jsonObject,this);
                        break;

                    case 8:
                        currentWorkingThread = new DeleteTeacherId(jsonObject,this);
                        break;

                    case 9:
                        currentWorkingThread = new UpdateClassroom(jsonObject,this);
                        break;

                    case 10:
                        currentWorkingThread = new DeleteClassroom(jsonObject,this);
                        break;

                    case 11:
                        currentWorkingThread = new GetStandardDivisionOfTeacher(jsonObject,this);
                        break;

                    case 12:
                        currentWorkingThread = new CreateStudentId(jsonObject,this);
                        break;

                    case 13:
                        currentWorkingThread = new GetStudentListForClassroomIncharge(jsonObject,this);
                        break;

                    case 14:
                        currentWorkingThread = new GetStudentListForSubjectTeacher(jsonObject,this);
                        break;

                    case 15:
                        currentWorkingThread = new DeleteStudentId(jsonObject,this);
                        break;

                    case 16:
                        currentWorkingThread = new UpdateStudentId(jsonObject,this);
                        break;

                    case 17:
                        currentWorkingThread = new GetStudentInfoForPrincipal(jsonObject,this);
                        break;

                    case 18:
                        currentWorkingThread = new GetExamAndSubjectList(jsonObject,this);
                        break;

                    case 19:
                        currentWorkingThread = new GetStudentListForExam(jsonObject,this);
                        break;

                    case 20:
                        currentWorkingThread = new AddNewExam(jsonObject,this);
                        break;

                    case 21:
                        currentWorkingThread = new GetExamListForTeacher(jsonObject,this);
                        break;

                    case 22:
                        currentWorkingThread = new GetScoreOfExam(jsonObject,this);
                        break;

                    case 23:
                        currentWorkingThread = new UpdateExam(jsonObject,this);
                        break;

                    case 24:
                        currentWorkingThread = new DeleteExam(jsonObject,this);
                        break;
                }
                currentWorkingThread.start();
            }
        }catch(SocketException e){
            Log.info("Connection Closed with Client at "+socket.getInetAddress().getHostAddress());
            clientList.remove(this);
        }catch(EOFException e){
            Log.info("Connection Closed with Client at "+socket.getInetAddress().getHostAddress());
            clientList.remove(this);
        }catch(Exception e){
            Log.error(e.toString());
        }finally{
            if( currentWorkingThread != null ){
                currentWorkingThread.stop();
            }
        }
    }


    public void sendMessage(JSONObject jsonObject){
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            int i = 0;
            while (i < jsonObject.toString().length()) {
                if (jsonObject.toString().length() < i + 65535) {
                    dataOutputStream.writeUTF(jsonObject.toString().substring(i));
                } else {
                    dataOutputStream.writeUTF(jsonObject.toString().substring(i, i + 65535));
                }
                i += 65535;
            }
            dataOutputStream.flush();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getIpAddress(){
        return socket.getInetAddress().getHostAddress();
    }
}