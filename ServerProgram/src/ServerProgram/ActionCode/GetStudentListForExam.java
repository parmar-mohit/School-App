package ServerProgram.ActionCode;

import ServerProgram.Client.Client;
import ServerProgram.DatabaseCon;
import ServerProgram.Log;
import org.json.JSONObject;

import java.sql.ResultSet;

public class GetStudentListForExam extends Thread {

    private JSONObject jsonObject;
    private Client client;
    private DatabaseCon db;

    public GetStudentListForExam(JSONObject jsonObject, Client client) {
        this.jsonObject = jsonObject;
        this.client = client;
    }

    @Override
    public void run() {
        Log.info("Action Code 19 Started for Client at " + client.getIpAddress());

        try {
            db = new DatabaseCon();

            int standard = jsonObject.getJSONObject("info").getInt("standard");
            String division = jsonObject.getJSONObject("info").getString("division");

            JSONObject responseJsonObject = new JSONObject();
            responseJsonObject.put("id", jsonObject.getLong("id"));

            JSONObject responseInfoJsonObject = new JSONObject();
            responseInfoJsonObject.put("total_students", db.getTotalStudentsInClassroom(standard, division));

            responseJsonObject.put("info", responseInfoJsonObject);

            //Sending first message containing total no of students in classroom
            client.sendMessage(responseJsonObject);

            //Removing Info attribute from responseJsonObject
            responseJsonObject.remove("info");

            ResultSet studentResultSet = db.getStudentListForExam(standard, division);
            while (studentResultSet.next()) {
                JSONObject studentJsonObject = new JSONObject();

                studentJsonObject.put("sid", studentResultSet.getInt("sid"));
                studentJsonObject.put("firstname", studentResultSet.getString("firstname"));
                studentJsonObject.put("lastname", studentResultSet.getString("lastname"));
                studentJsonObject.put("roll_no", studentResultSet.getInt("roll_no"));

                //Sending message containing student data
                responseJsonObject.put("info", studentJsonObject);
                client.sendMessage(responseJsonObject);
                responseJsonObject.remove("info");
            }
        } catch (Exception e) {
            Log.error(e.toString());
        } finally {
            db.closeConnection();
        }

        Log.info("Action Code 19 Completed for Client at " + client.getIpAddress());
    }
}
