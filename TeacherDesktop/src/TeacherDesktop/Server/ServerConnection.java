package TeacherDesktop.Server;

import TeacherDesktop.Interfaces.NoConnectionInterface;
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
    private final ArrayList<JSONObject> messagePool;
    private ArrayList<JSONObject> packetList;
    private Thread receiverThread;

    private JFrame currentFrame;

    public ServerConnection(Socket socket) {
        this.socket = socket;
        messagePool = new ArrayList<>();
        packetList = new ArrayList<>();
        startReceivingMessage();
    }

    public void setCurrentFrame(JFrame currentFrame) {
        this.currentFrame = currentFrame;
    }

    public String getPassword(String phone) {
        //Creating Appropriate JSON String
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code", 1);
        JSONObject infoJsonObject = new JSONObject();
        infoJsonObject.put("phone", phone);
        jsonObject.put("info", infoJsonObject);

        //Sending Message
        long id = sendMessage(jsonObject);

        //Waiting till message is received
        JSONObject recvMessage = getResponseMessage(id);
        String password = recvMessage.getJSONObject("info").getString("password");
        return password;
    }

    public int addTeacherId(String firstname, String lastname, String phone, String email, String password, String gender) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code", 2);
        JSONObject infoJsonObject = new JSONObject();
        infoJsonObject.put("firstname", firstname);
        infoJsonObject.put("lastname", lastname);
        infoJsonObject.put("phone", phone);
        infoJsonObject.put("email", email);
        infoJsonObject.put("password", password);
        infoJsonObject.put("gender", gender);
        jsonObject.put("info", infoJsonObject);
        long id = sendMessage(jsonObject);

        //Getting Message
        JSONObject responseJsonObject = getResponseMessage(id);

        //returning response code
        return responseJsonObject.getJSONObject("info").getInt("response_code");
    }

    public JSONArray getTeacherList(JProgressBar progressBar) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code", 3);

        //sending message;
        long id = sendMessage(jsonObject);

        JSONObject responseMessage = getResponseMessage(id);
        int total = responseMessage.getJSONObject("info").getInt("total_teachers");

        JSONArray teacherListJsonArray = new JSONArray();
        for (int i = 1; i <= total; i++) {
            responseMessage = getResponseMessage(id);
            teacherListJsonArray.put(responseMessage.getJSONObject("info"));
            progressBar.setValue(i * 100 / total);
            progressBar.setString((i * 100 / total) + "%");
        }
        return teacherListJsonArray;
    }

    public JSONArray getTeacherList() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code", 3);

        //sending message;
        long id = sendMessage(jsonObject);

        JSONObject responseMessage = getResponseMessage(id);
        int total = responseMessage.getJSONObject("info").getInt("total_teachers");

        JSONArray teacherListJsonArray = new JSONArray();
        for (int i = 1; i <= total; i++) {
            responseMessage = getResponseMessage(id);
            teacherListJsonArray.put(responseMessage.getJSONObject("info"));
        }
        return teacherListJsonArray;
    }

    public int createClassroom(JSONObject infoJsonObject) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code", 4);
        jsonObject.put("info", infoJsonObject);

        //sending message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);
        return responseJsonObject.getJSONObject("info").getInt("response_code");
    }

    public int changePassword(String phone, String password) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code", 5);
        JSONObject infoJsonObject = new JSONObject();
        infoJsonObject.put("phone", phone);
        infoJsonObject.put("password", password);
        jsonObject.put("info", infoJsonObject);

        //Sending Message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);
        return responseJsonObject.getJSONObject("info").getInt("response_code");
    }

    public int updateTeacherId(JSONObject teacherObject) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code", 6);
        jsonObject.put("info", teacherObject);

        //sending message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);
        return responseJsonObject.getJSONObject("info").getInt("response_code");
    }

    public JSONArray getClassroomListForPrincipal(JProgressBar progressBar) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code", 7);

        //Sending Message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);
        int total = responseJsonObject.getJSONObject("info").getInt("total_classrooms");

        JSONArray classroomJsonArray = new JSONArray();
        for (int i = 1; i <= total; i++) {
            responseJsonObject = getResponseMessage(id);
            classroomJsonArray.put(responseJsonObject.getJSONObject("info"));
            progressBar.setValue(i * 100 / total);
            progressBar.setString((i * 100 / total) + "%");
        }

        return classroomJsonArray;
    }

    public int deleteTeacher(String phone) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code", 8);

        JSONObject infoJsonObject = new JSONObject();
        infoJsonObject.put("phone", phone);

        jsonObject.put("info", infoJsonObject);

        //Sending Message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);
        return responseJsonObject.getJSONObject("info").getInt("response_code");
    }

    public int updateClassroom(JSONObject infoJsonObject) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code", 9);
        jsonObject.put("info", infoJsonObject);

        //Send Message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);
        return responseJsonObject.getJSONObject("info").getInt("response_code");
    }

    public int deleteClassroom(JSONObject classroomJsonObject) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code", 10);

        JSONObject infoJsonObject = new JSONObject();
        infoJsonObject.put("standard", classroomJsonObject.getInt("standard"));
        infoJsonObject.put("division", classroomJsonObject.getString("division"));

        jsonObject.put("info", infoJsonObject);

        //Sending message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);
        return responseJsonObject.getJSONObject("info").getInt("response_code");
    }

    public JSONArray getStandardDivisionOfTeacher(String phone) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code", 11);

        JSONObject infoJsonObject = new JSONObject();
        infoJsonObject.put("phone", phone);

        jsonObject.put("info", infoJsonObject);

        //Sending Message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);
        return responseJsonObject.getJSONArray("info");
    }

    public int createStudent(JSONObject studentJsonObject) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code", 12);
        jsonObject.put("info", studentJsonObject);

        //Sending Message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);
        return responseJsonObject.getJSONObject("info").getInt("sid");
    }

    public JSONArray getStudentListForClassroomIncharge(String phone, JProgressBar progressBar) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code", 13);

        JSONObject infoJsonObject = new JSONObject();
        infoJsonObject.put("phone", phone);

        jsonObject.put("info", infoJsonObject);

        //sending message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);
        int totalStudents = responseJsonObject.getJSONObject("info").getInt("total_students");

        JSONArray studentJsonArray = new JSONArray();
        //Getting Student Objects
        int total = totalStudents;
        for (int i = 1; i <= total; i++) {
            JSONObject studentJsonObject = getResponseMessage(id).getJSONObject("info");
            studentJsonArray.put(studentJsonObject);
            progressBar.setValue(i * 100 / totalStudents);
            progressBar.setString((i * 100 / totalStudents) + "%");
        }

        return studentJsonArray;
    }

    public JSONArray getStudentListForSubjectTeacher(String phone, JProgressBar progressBar) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code", 14);

        JSONObject infoJsonObject = new JSONObject();
        infoJsonObject.put("phone", phone);

        jsonObject.put("info", infoJsonObject);

        //sending message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);
        int totalStudents = responseJsonObject.getJSONObject("info").getInt("total_students");

        JSONArray studentJsonArray = new JSONArray();
        //Getting Student Objects
        int total = totalStudents;
        for (int i = 1; i <= total; i++) {
            JSONObject studentJsonObject = getResponseMessage(id).getJSONObject("info");
            studentJsonArray.put(studentJsonObject);
            progressBar.setValue(i * 100 / totalStudents);
            progressBar.setString((i * 100 / totalStudents) + "%");
        }

        return studentJsonArray;
    }

    public int deleteStudentId(int sid) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code", 15);

        JSONObject infoStudentJsonObject = new JSONObject();
        infoStudentJsonObject.put("sid", sid);

        jsonObject.put("info", infoStudentJsonObject);

        //sending message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);
        return responseJsonObject.getJSONObject("info").getInt("response_code");
    }

    public int updateStudentId(JSONObject studentJsonObject) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code", 16);
        jsonObject.put("info", studentJsonObject);

        //sending message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);
        return responseJsonObject.getJSONObject("info").getInt("response_code");
    }

    public JSONObject getStudentInfoForPrincipal(JProgressBar progressBar) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code", 17);

        //Sending message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);

        JSONObject studentInfoJsonObject = new JSONObject();
        studentInfoJsonObject.put("classroom_list", responseJsonObject.getJSONObject("info").getJSONArray("classroom_list"));

        int totalStudents = responseJsonObject.getJSONObject("info").getInt("total_students");

        JSONArray studentJsonArray = new JSONArray();
        for (int i = 1; i <= totalStudents; i++) {
            JSONObject studentJsonObject = getResponseMessage(id).getJSONObject("info");
            studentJsonArray.put(studentJsonObject);
            progressBar.setValue(i * 100 / totalStudents);
            progressBar.setString((i * 100 / totalStudents) + "%");
        }

        studentInfoJsonObject.put("student_list", studentJsonArray);
        return studentInfoJsonObject;
    }

    public JSONObject getExamAndSubjectList(String phone) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code", 18);

        JSONObject infoJsonObject = new JSONObject();
        infoJsonObject.put("phone", phone);

        jsonObject.put("info", infoJsonObject);

        //Sending Message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);
        return responseJsonObject.getJSONObject("info");
    }

    public JSONArray getStudentListForExam(int standard, String division, JProgressBar progressBar) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code", 19);

        JSONObject infoJsonObject = new JSONObject();
        infoJsonObject.put("standard", standard);
        infoJsonObject.put("division", division);

        jsonObject.put("info", infoJsonObject);

        //Sending message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);
        int total = responseJsonObject.getJSONObject("info").getInt("total_students");

        JSONArray studentJsonArray = new JSONArray();
        for (int i = 1; i <= total; i++) {
            responseJsonObject = getResponseMessage(id);
            studentJsonArray.put(responseJsonObject.getJSONObject("info"));
            progressBar.setValue(i * 100 / total);
            progressBar.setString((i * 100 / total) + "%");
        }

        return studentJsonArray;
    }

    public int addNewExam(JSONObject examJsonObject) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code", 20);
        jsonObject.put("info", examJsonObject);

        //Sending Message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);
        return responseJsonObject.getJSONObject("info").getInt("response_code");
    }

    public JSONArray getExamListForTeacher(String phone, JProgressBar progressBar) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code", 21);

        JSONObject infoJsonObject = new JSONObject();
        infoJsonObject.put("phone", phone);

        jsonObject.put("info", infoJsonObject);

        //Sending message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);
        int total = responseJsonObject.getJSONObject("info").getInt("total_exams");

        JSONArray examJsonArray = new JSONArray();
        for (int i = 0; i < total; i++) {
            responseJsonObject = getResponseMessage(id);
            examJsonArray.put(responseJsonObject.getJSONObject("info"));
            progressBar.setValue(i * 100 / total);
            progressBar.setString((i * 100 / total) + "%");
        }

        return examJsonArray;
    }

    public JSONArray getScoreOfExam(int examId, JProgressBar progressBar) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code", 22);

        JSONObject infoJsonObject = new JSONObject();
        infoJsonObject.put("exam_id", examId);

        jsonObject.put("info", infoJsonObject);

        //Sending Message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);
        int total = responseJsonObject.getJSONObject("info").getInt("total_students");

        JSONArray studentScoreJsonArray = new JSONArray();
        for (int i = 0; i < total; i++) {
            responseJsonObject = getResponseMessage(id);
            studentScoreJsonArray.put(responseJsonObject.getJSONObject("info"));
            progressBar.setValue(i * 100 / total);
            progressBar.setString((i * 100 / total) + "%");
        }

        return studentScoreJsonArray;
    }

    public int updateExam(JSONObject examJsonObject) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code", 23);
        jsonObject.put("info", examJsonObject);

        //Sending Message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);
        return responseJsonObject.getJSONObject("info").getInt("response_code");
    }

    public int deleteExam(int examId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_code", 24);

        JSONObject infoJsonObject = new JSONObject();
        infoJsonObject.put("exam_id", examId);

        jsonObject.put("info", infoJsonObject);

        //Sending message
        long id = sendMessage(jsonObject);

        JSONObject responseJsonObject = getResponseMessage(id);
        return responseJsonObject.getJSONObject("info").getInt("response_code");
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
            new NoConnectionInterface();
            currentFrame.dispose();
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
                    currentFrame.dispose();
                    new NoConnectionInterface();
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
}
