package ServerProgram.Client;

import ServerProgram.ActionCode.*;
import ServerProgram.Log;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

public class Client extends Thread {
    private Socket socket;
    private ArrayList<Client> clientList;
    private Thread currentWorkingThread;

    public Client(Socket socket, ArrayList<Client> clientList) {
        this.socket = socket;
        this.clientList = clientList;
    }

    @Override
    public void run() {
        try {
            while (true) {
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                String recvString = dataInputStream.readUTF();
                JSONObject jsonObject = new JSONObject(recvString);

                if (jsonObject.has("packet_id")) {
                    ArrayList<JSONObject> packetList = new ArrayList<>();
                    packetList.add(jsonObject);

                    int totalPackets = jsonObject.getInt("total_packets");
                    for (int i = 1; i < totalPackets; i++) {
                        recvString = dataInputStream.readUTF();
                        packetList.add(new JSONObject(recvString));
                    }

                    String jsonString = new String();
                    for (int i = 1; i <= totalPackets; i++) {
                        for (int j = 0; j < packetList.size(); j++) {
                            JSONObject packet = packetList.get(j);
                            if (packet.getInt("packet_no") == i) {
                                jsonString += packet.getString("data");
                                packetList.remove(j);
                                break;
                            }
                        }
                    }

                    jsonObject = new JSONObject(jsonString);
                }

                if (currentWorkingThread != null && currentWorkingThread.isAlive()) {
                    currentWorkingThread.stop();
                    Log.info("Action Code Interrupted for Client at " + getIpAddress());
                }

                switch (jsonObject.getInt("action_code")) {
                    case 1:
                        currentWorkingThread = new GetCredentials(jsonObject, this);
                        break;

                    case 2:
                        currentWorkingThread = new CreateTeacherId(jsonObject, this);
                        break;

                    case 3:
                        currentWorkingThread = new GetTeacherList(jsonObject, this);
                        break;

                    case 4:
                        currentWorkingThread = new CreateClassroom(jsonObject, this);
                        break;

                    case 5:
                        currentWorkingThread = new ChangePassword(jsonObject, this);
                        break;

                    case 6:
                        currentWorkingThread = new UpdateTeacherAttributes(jsonObject, this);
                        break;

                    case 7:
                        currentWorkingThread = new GetClassroomListForPrincipal(jsonObject, this);
                        break;

                    case 8:
                        currentWorkingThread = new DeleteTeacherId(jsonObject, this);
                        break;

                    case 9:
                        currentWorkingThread = new UpdateClassroom(jsonObject, this);
                        break;

                    case 10:
                        currentWorkingThread = new DeleteClassroom(jsonObject, this);
                        break;

                    case 11:
                        currentWorkingThread = new GetStandardDivisionOfTeacher(jsonObject, this);
                        break;

                    case 12:
                        currentWorkingThread = new CreateStudentId(jsonObject, this);
                        break;

                    case 13:
                        currentWorkingThread = new GetStudentListForClassroomIncharge(jsonObject, this);
                        break;

                    case 14:
                        currentWorkingThread = new GetStudentListForSubjectTeacher(jsonObject, this);
                        break;

                    case 15:
                        currentWorkingThread = new DeleteStudentId(jsonObject, this);
                        break;

                    case 16:
                        currentWorkingThread = new UpdateStudentId(jsonObject, this);
                        break;

                    case 17:
                        currentWorkingThread = new GetStudentInfoForPrincipal(jsonObject, this);
                        break;

                    case 18:
                        currentWorkingThread = new GetExamAndSubjectList(jsonObject, this);
                        break;

                    case 19:
                        currentWorkingThread = new GetStudentListForExam(jsonObject, this);
                        break;

                    case 20:
                        currentWorkingThread = new AddNewExam(jsonObject, this);
                        break;

                    case 21:
                        currentWorkingThread = new GetExamListForTeacher(jsonObject, this);
                        break;

                    case 22:
                        currentWorkingThread = new GetScoreOfExam(jsonObject, this);
                        break;

                    case 23:
                        currentWorkingThread = new UpdateExam(jsonObject, this);
                        break;

                    case 24:
                        currentWorkingThread = new DeleteExam(jsonObject, this);
                        break;

                    case 25:
                        currentWorkingThread = new RegisterUser(jsonObject,this);
                        break;

                    case 26:
                        currentWorkingThread = new SetPasswordNewUser(jsonObject,this);
                        break;

                    case 27:
                        currentWorkingThread = new GetPassword(jsonObject,this);
                        break;

                    case 28:
                        currentWorkingThread = new GetStudentListForParent(jsonObject,this);
                        break;

                    case 29:
                        currentWorkingThread = new GetStudentData(jsonObject,this);
                        break;
                }
                currentWorkingThread.start();
            }
        } catch (Exception e) {
            Log.error(e.toString()+" (Client Addr : "+getIpAddress()+")");
            Log.info("Connection Closed with Client at " + getIpAddress());
            clientList.remove(this);
        } finally {
            if (currentWorkingThread != null) {
                currentWorkingThread.stop();
            }
        }
    }


    public void sendMessage(JSONObject jsonObject) {
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
        }
    }

    public String getIpAddress() {
        return socket.getInetAddress().getHostAddress();
    }
}