package com.school.parentandroidapp.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

import static java.lang.Thread.sleep;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import com.school.parentandroidapp.Activities.NoConnectionActivity;
import com.school.parentandroidapp.parser.json.JSONObject;

public class ServerConnection implements Parcelable {
    private Socket socket;
    private ArrayList<JSONObject> messagePool;
    private ArrayList<JSONObject> packetList;
    private Thread receiverThread;
    private Context currentContext;

    public ServerConnection(Socket socket) {
        this.socket = socket;
        messagePool = new ArrayList<>();
        packetList = new ArrayList<>();
        startReceivingMessage();
    }

    protected ServerConnection(Parcel in) {
    }

    public static final Creator<ServerConnection> CREATOR = new Creator<ServerConnection>() {
        @Override
        public ServerConnection createFromParcel(Parcel in) {
            return new ServerConnection(in);
        }

        @Override
        public ServerConnection[] newArray(int size) {
            return new ServerConnection[size];
        }
    };

    public void setCurrentContext(Context currentContext) {
        this.currentContext = currentContext;
    }

    public int registerUser(String phone,long dob){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code",25);

        JSONObject infoJsonObject = new JSONObject();
        infoJsonObject.put("phone",phone);
        infoJsonObject.put("dob_of_student",dob);

        jsonObject.put("info",infoJsonObject);

        //Sending message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);

        return responseJsonObject.getJSONObject("info").getInt("response_code");
    }

    public int setPasswordNewUser(String phone,String hashedPassword){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code",26);

        JSONObject infoJsonObject = new JSONObject();
        infoJsonObject.put("phone",phone);
        infoJsonObject.put("password",hashedPassword);

        jsonObject.put("info",infoJsonObject);

        //sending message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);
        return responseJsonObject.getJSONObject("info").getInt("response_code");
    }

    public String getPassword(String phone){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code",27);

        JSONObject infoJsonObject = new JSONObject();
        infoJsonObject.put("phone",phone);

        jsonObject.put("info",infoJsonObject);

        //Sending Message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);

        return responseJsonObject.getJSONObject("info").getString("password");
    }

    private JSONObject getResponseMessage(long messageId) {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() <= startTime + 60000) { // Loop for minute
            for (int i = 0; i < messagePool.size(); i++) {
                JSONObject jsonObject = messagePool.get(i);
                long id = jsonObject.getLong("id");
                if (messageId == id) {
                    messagePool.remove(i);
                    return jsonObject;
                }
            }

            try {
                //Sleeping Before checking for message again
                sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            socket.close();
        } catch (Exception e) {
            //do nothing
        }
        return null;
    }

    private long sendMessage(JSONObject jsonObject) {
        packetList = new ArrayList<>();
        long messageId = new Date().getTime();
        jsonObject.put("id", messageId);
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            if (jsonObject.toString().length() < 65535) {
                dataOutputStream.writeUTF(jsonObject.toString());
            } else {
                JSONObject packet = new JSONObject();
                packet.put("packet_id", new Date().getTime());
                int totalPackets = jsonObject.toString().length() / 65000;
                if (jsonObject.toString().length() % 65000 > 0) {
                    totalPackets++;
                }
                packet.put("total_packets", totalPackets);
                for (int i = 0; i < totalPackets; i++) {
                    packet.put("packet_no", i + 1);
                    if (jsonObject.toString().length() > (i + 1) * 65000) {
                        packet.put("data", jsonObject.toString().substring(i * 65000, (i + 1) * 65000));
                    } else {
                        packet.put("data", jsonObject.toString().substring(i * 65000));
                    }
                    dataOutputStream.writeUTF(packet.toString());
                    packet.remove("packet_no");
                    packet.remove("data");
                }
            }
            dataOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent(currentContext, NoConnectionActivity.class);
            currentContext.startActivity(intent);
        }
        return messageId;
    }

    private void startReceivingMessage() {
        receiverThread = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                        //Receiving and Storing  and Message in MessagePool
                        String recvString = dataInputStream.readUTF();
                        JSONObject jsonObject = new JSONObject(recvString);
                        if (jsonObject.has("packet_id")) {
                            packetList.add(jsonObject);
                            if (jsonObject.getInt("packet_no") == jsonObject.getInt("total_packets")) {
                                joinPackets(jsonObject.getLong("packet_id"), jsonObject.getInt("total_packets"));
                            }
                        } else {
                            messagePool.add(jsonObject);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Intent intent = new Intent(currentContext,NoConnectionActivity.class);
                    currentContext.startActivity(intent);
                }
            }
        };
        receiverThread.start();
    }

    private void joinPackets(long packetId, int totalPackets) {
        String jsonString = "";
        for (int i = 1; i <= totalPackets; i++) {
            for (int j = 0; j < packetList.size(); j++) {
                JSONObject packet = packetList.get(j);
                if (packet.getLong("packet_id") == packetId && packet.getInt("packet_no") == i) {
                    jsonString += packet.getString("data");
                    packetList.remove(j);
                    break;
                }
            }
        }

        messagePool.add(new JSONObject(jsonString));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }

    public Socket getSocket() {
        return socket;
    }

    public ArrayList<JSONObject> getMessagePool() {
        return messagePool;
    }

    public ArrayList<JSONObject> getPacketList() {
        return packetList;
    }
}
