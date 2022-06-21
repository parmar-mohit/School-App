package TeacherDesktop.Server;

import TeacherDesktop.Frames.NoConnectionInterface;
import TeacherDesktop.Static.Constraint;
import org.json.JSONArray;
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
    private ArrayList<String> messagePool;
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
        infoJsonObject.put("password", Constraint.hashPassword(password));
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

    public int createGrade(JSONObject infoJsonObject){
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

    public int updateTeacherAttributes(JSONObject teacherObject){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code",6);
        jsonObject.put("info",teacherObject);

        //sending message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);
        return responseJsonObject.getJSONObject("info").getInt("response_code");
    }

    private JSONObject getResponseMessage(long messageId){
        while (true){
            for( int i = 0; i < messagePool.size(); i++){
                JSONObject jsonObject = new JSONObject(messagePool.get(i));
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
    }


    private long sendMessage(JSONObject jsonObject){
        long messageId = new Date().getTime();
        jsonObject.put("id",messageId);
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(jsonObject.toString());
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
                        //Receiving Message
                        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                        String recvMessage = dataInputStream.readUTF();

                        //Storing Message in MessagePool
                        messagePool.add(recvMessage);
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
