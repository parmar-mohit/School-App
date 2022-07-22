package ServerProgram.ActionCode;

import ServerProgram.Client.Client;
import ServerProgram.DatabaseCon;
import ServerProgram.Log;
import org.json.JSONObject;

import java.sql.ResultSet;

public class GetScoreOfExam extends Thread {

    private JSONObject jsonObject;
    private Client client;
    private DatabaseCon db;

    public GetScoreOfExam(JSONObject jsonObject,Client client){
        this.jsonObject = jsonObject;
        this.client = client;
    }

    @Override
    public void run() {
        Log.info("Action Code 22 Started for Client at "+client.getIpAddress());

        try{
            db = new DatabaseCon();
            int examId = jsonObject.getJSONObject("info").getInt("exam_id");

            JSONObject responseJsonObject = new JSONObject();
            responseJsonObject.put("id",jsonObject.getLong("id"));

            JSONObject responseInfoJsonObject = new JSONObject();
            responseInfoJsonObject.put("total_students",db.getTotalStudentForExam(examId));

            responseJsonObject.put("info",responseInfoJsonObject);

            //Sending First message containing total no of students
            client.sendMessage(responseJsonObject);

            //Removing info attribute from responseJsonObject
            responseJsonObject.remove("info");

            ResultSet studentScoreResultSet = db.getStudentScoreOfExam(examId);
            while(studentScoreResultSet.next()){
                JSONObject studentScoreJsonObject = new JSONObject();
                studentScoreJsonObject.put("sid",studentScoreResultSet.getInt("sid"));
                studentScoreJsonObject.put("firstname",studentScoreResultSet.getString("firstname"));
                studentScoreJsonObject.put("lastname",studentScoreResultSet.getString("lastname"));
                studentScoreJsonObject.put("roll_no",studentScoreResultSet.getInt("roll_no"));
                studentScoreJsonObject.put("score",studentScoreResultSet.getInt("marks_obtained"));

                responseJsonObject.put("info",studentScoreJsonObject);

                //Sending Message
                client.sendMessage(responseJsonObject);

                responseJsonObject.remove("info");
            }
        }catch (Exception e){
            Log.error(e.toString());
        }finally {
            db.closeConnection();
        }

        Log.info("Action Code 22 Completed for Client at "+client.getIpAddress());
    }
}
