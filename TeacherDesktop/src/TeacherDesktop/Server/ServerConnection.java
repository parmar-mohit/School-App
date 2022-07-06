package TeacherDesktop.Server;

import TeacherDesktop.Interfaces.NoConnectionInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

import static java.lang.Thread.sleep;

public class ServerConnection {
    private final Socket socket;
    private ArrayList<JSONObject> messagePool;
    private Thread receiverThread;

    private JFrame currentFrame;

    public ServerConnection(Socket socket){
        this.socket = socket;
        messagePool = new ArrayList<>();
        startReceivingMessage();
    }

    public void setCurrentFrame(JFrame currentFrame) {
        this.currentFrame = currentFrame;
    }

    public String getPassword(String phone){
        //Creating Appropriate JSON String
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code",1);
        JSONObject infoJsonObject = new JSONObject();
        infoJsonObject.put("phone",phone);
        jsonObject.put("info",infoJsonObject);

        //Sending Message
        long id = sendMessage(jsonObject);

        //Sleep Sometime before checking for response
        try {
            sleep(300);
        }catch(Exception e){
            e.printStackTrace();
        }

        //Waiting till message is received
        JSONObject recvMessage = getResponseMessage(id);
        String password = recvMessage.getJSONObject("info").getString("password");
        return password;
    }

    public int addTeacherId(String firstname,String lastname,String phone,String email, String password, String gender){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code",2);
        JSONObject infoJsonObject = new JSONObject();
        infoJsonObject.put("firstname",firstname);
        infoJsonObject.put("lastname",lastname);
        infoJsonObject.put("phone",phone);
        infoJsonObject.put("email",email);
        infoJsonObject.put("password", password);
        infoJsonObject.put("gender",gender);
        jsonObject.put("info",infoJsonObject);
        long id = sendMessage(jsonObject);

        //Getting Message
        JSONObject responseJsonObject = getResponseMessage(id);

        //returning response code
        return responseJsonObject.getJSONObject("info").getInt("response_code");
    }

    public JSONArray getTeacherList(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code",3);

        //sending message;
        long id = sendMessage(jsonObject);

        //waiting before checking for response
        try {
            sleep(300);
        }catch(Exception e){
            e.printStackTrace();
        }

        jsonObject = getResponseMessage(id);
        JSONArray infoJsonArray = jsonObject.getJSONArray("info");
        return infoJsonArray;
    }

    public int createClassroom(JSONObject infoJsonObject){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code",4);
        jsonObject.put("info",infoJsonObject);

        //sending message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);
        return responseJsonObject.getJSONObject("info").getInt("response_code");
    }

    public int changePassword(String phone,String password){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code",5);
        JSONObject infoJsonObject = new JSONObject();
        infoJsonObject.put("phone",phone);
        infoJsonObject.put("password",password);
        jsonObject.put("info",infoJsonObject);

        //Sending Message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);
        return responseJsonObject.getJSONObject("info").getInt("response_code");
    }

    public int updateTeacherId(JSONObject teacherObject){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code",6);
        jsonObject.put("info",teacherObject);

        //sending message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);
        return responseJsonObject.getJSONObject("info").getInt("response_code");
    }

    public JSONArray getClassroomListForPrincipal(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code",7);

        //Sending Message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);
        return responseJsonObject.getJSONArray("info");
    }

    public int deleteTeacher(String phone){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code",8);

        JSONObject infoJsonObject = new JSONObject();
        infoJsonObject.put("phone",phone);

        jsonObject.put("info",infoJsonObject);

        //Sending Message
       long id = sendMessage(jsonObject);

       JSONObject responseJsonObject = getResponseMessage(id);
       return responseJsonObject.getJSONObject("info").getInt("response_code");
    }

    public int updateClassroom(JSONObject infoJsonObject){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code",9);
        jsonObject.put("info",infoJsonObject);

        //Send Message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);
        return responseJsonObject.getJSONObject("info").getInt("response_code");
    }

    public int deleteClassroom(JSONObject classroomJsonObject){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code",10);

        JSONObject infoJsonObject = new JSONObject();
        infoJsonObject.put("standard",classroomJsonObject.getInt("standard"));
        infoJsonObject.put("division",classroomJsonObject.getString("division"));

        jsonObject.put("info",infoJsonObject);

        //Sending message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);
        return responseJsonObject.getJSONObject("info").getInt("response_code");
    }

    public JSONArray getStandardDivisionOfTeacher(String phone){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code",11);

        JSONObject infoJsonObject = new JSONObject();
        infoJsonObject.put("phone",phone);

        jsonObject.put("info",infoJsonObject);

        //Sending Message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);
        return responseJsonObject.getJSONArray("info");
    }

    public int createStudent(JSONObject studentJsonObject){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code",12);
        jsonObject.put("info",studentJsonObject);

        //Sending Message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);
        return responseJsonObject.getJSONObject("info").getInt("sid");
    }

    public JSONArray getStudentListForClassroomIncharge(String phone){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code",13);

        JSONObject infoJsonObject = new JSONObject();
        infoJsonObject.put("phone",phone);

        jsonObject.put("info",infoJsonObject);

        //sending message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);
        return responseJsonObject.getJSONArray("info");
    }

    public JSONArray getStudentListForSubjectTeacher(String phone){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code",14);

        JSONObject infoJsonObject = new JSONObject();
        infoJsonObject.put("phone",phone);

        jsonObject.put("info",infoJsonObject);

        //sending message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);
        return responseJsonObject.getJSONArray("info");
    }

    public int deleteStudentId(int sid){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code",15);

        JSONObject infoStudentJsonObject = new JSONObject();
        infoStudentJsonObject.put("sid",sid);

        jsonObject.put("info",infoStudentJsonObject);

        //sending message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);
        return responseJsonObject.getJSONObject("info").getInt("response_code");
    }

    public int updateStudentId(JSONObject studentJsonObject){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code",16);
        jsonObject.put("info",studentJsonObject);

        //sending message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);
        return responseJsonObject.getJSONObject("info").getInt("response_code");
    }

    public JSONObject getStudentInfoForPrincipal(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code",17);

        //Sending message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);
        return responseJsonObject.getJSONObject("info");
    }

    public JSONObject getExamAndSubjectList(String phone){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code",18);

        JSONObject infoJsonObject = new JSONObject();
        infoJsonObject.put("phone",phone);

        jsonObject.put("info",infoJsonObject);

        //Sending Message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);
        return responseJsonObject.getJSONObject("info");
    }

    private JSONObject getResponseMessage(long messageId){
        long startTime = System.currentTimeMillis();
        while ( System.currentTimeMillis() <= startTime + 60000 ){ // Loop for minute
            for( int i = 0; i < messagePool.size(); i++){
                JSONObject jsonObject = messagePool.get(i);
                long id = jsonObject.getLong("id");
                if( messageId == id ){
                    messagePool.remove(i);
                    return jsonObject;
                }
            }

            try{
                //Sleeping Before checking for message again
                sleep(100);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        try {
            socket.close();
        }catch(Exception e){
            //do nothing
        }
        return null;
    }

    private long sendMessage(JSONObject jsonObject){
        long messageId = new Date().getTime();
        jsonObject.put("id",messageId);
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
            new NoConnectionInterface();
            currentFrame.dispose();
        }
        return messageId;
    }

    private void startReceivingMessage(){
        receiverThread = new Thread(){
            @Override
            public void run() {
                try{
                    while(true){
                        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                        //Receiving and Storing  and Message in MessagePool
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
                        messagePool.add(jsonObject);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    currentFrame.dispose();
                    new NoConnectionInterface();
                }
            }
        };
        receiverThread.start();
    }
}
