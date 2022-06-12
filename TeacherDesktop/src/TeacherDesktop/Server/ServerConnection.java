package TeacherDesktop.Server;

import TeacherDesktop.Frames.NoConnectionInterface;
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

    public String getPassword(int phone){
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
        while (true){
            for( int i = 0; i < messagePool.size(); i++){
                jsonObject = new JSONObject(messagePool.get(i));
                long messageId = jsonObject.getLong("id");
                System.out.println("id : "+messageId);
                if( messageId == id ){
                    String password = jsonObject.getJSONObject("info").getString("password");
                    messagePool.remove(i);
                    return password;
                }
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
                    new NoConnectionInterface();
                    currentFrame.dispose();
                }
            }
        };
        receiverThread.start();
    }

    public void closeConnection(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code",99);
        sendMessage(jsonObject);
    }
}
