package ServerProgram.ActionCode;

import ServerProgram.Client.Client;
import ServerProgram.DatabaseCon;
import ServerProgram.Log;
import org.json.JSONObject;

import java.sql.ResultSet;

public class GetExamListForTeacher extends Thread {

    private JSONObject jsonObject;
    private Client client;
    private DatabaseCon db;

    public GetExamListForTeacher(JSONObject jsonObject, Client client) {
        this.jsonObject = jsonObject;
        this.client = client;
    }

    @Override
    public void run() {
        Log.info("Action Code 21 Started for Client at " + client.getIpAddress());

        try {
            db = new DatabaseCon();

            String phone = jsonObject.getJSONObject("info").getString("phone");

            JSONObject responseJsonObject = new JSONObject();
            responseJsonObject.put("id", jsonObject.getLong("id"));

            JSONObject responseInfoJsonObject = new JSONObject();
            responseInfoJsonObject.put("total_exams", db.getTotalExamCount(phone));

            responseJsonObject.put("info", responseInfoJsonObject);

            //Sending First Message Containing total no of exams
            client.sendMessage(responseJsonObject);

            //Removing Info Attribue
            responseJsonObject.remove("info");

            ResultSet examResultSet = db.getExamListForTeacher(phone);
            while (examResultSet.next()) {
                JSONObject examJsonObject = new JSONObject();
                examJsonObject.put("exam_id", examResultSet.getInt("exam_id"));
                examJsonObject.put("exam_name", examResultSet.getString("exam_name"));
                examJsonObject.put("date", examResultSet.getDate("exam_date").getTime());
                examJsonObject.put("total_marks", examResultSet.getInt("total_marks"));

                JSONObject subjectJsonObject = new JSONObject();
                ResultSet subjectResultSet = db.getSubjectDetails(examResultSet.getInt("sub_id"));
                subjectResultSet.next();
                subjectJsonObject.put("subject_id", subjectResultSet.getString("sub_id"));
                subjectJsonObject.put("subject_name", subjectResultSet.getString("subject_name"));
                subjectJsonObject.put("standard", subjectResultSet.getInt("standard"));
                subjectJsonObject.put("division", subjectResultSet.getString("division"));
                subjectJsonObject.put("phone", subjectResultSet.getBigDecimal("t_phone").toString());

                examJsonObject.put("subject", subjectJsonObject);

                responseJsonObject.put("info", examJsonObject);
                client.sendMessage(responseJsonObject);
                responseJsonObject.remove("info");
            }
        } catch (Exception e) {
            Log.error(e.toString());
        } finally {
            db.closeConnection();
        }

        Log.info("Action Code 21 Completed for Client at " + client.getIpAddress());
    }
}
