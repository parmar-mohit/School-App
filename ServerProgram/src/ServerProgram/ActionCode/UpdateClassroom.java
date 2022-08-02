package ServerProgram.ActionCode;

import ServerProgram.Client.Client;
import ServerProgram.DatabaseCon;
import ServerProgram.Log;
import org.json.JSONArray;
import org.json.JSONObject;

public class UpdateClassroom extends Thread {

    private JSONObject jsonObject;
    private Client client;
    private DatabaseCon db;

    public UpdateClassroom(JSONObject jsonObject, Client client) {
        this.jsonObject = jsonObject;
        this.client = client;
    }

    @Override
    public void run() {
        Log.info("Action Code 9 Started for Client at " + client.getIpAddress());

        try {
            db = new DatabaseCon();

            JSONObject infoJsonObject = jsonObject.getJSONObject("info");
            int standard = infoJsonObject.getInt("standard");
            String division = infoJsonObject.getString("division");
            String phone = infoJsonObject.getString("teacher_incharge");

            //Updating Classroom TeacherIncharge
            db.updateClassroomTeacherIncharge(standard, division, phone);

            //Updating Subjects
            JSONArray subjectListJsonArray = infoJsonObject.getJSONArray("subject_list");
            for (int i = 0; i < subjectListJsonArray.length(); i++) {
                JSONObject subjectJsonObject = subjectListJsonArray.getJSONObject(i);
                if (subjectJsonObject.getString("old_subject_name").equals("null")) {
                    db.insertSubject(standard, division, subjectJsonObject.getString("new_subject_name"), subjectJsonObject.getString("new_subject_teacher"));
                } else {
                    db.updateSubject(standard, division, subjectJsonObject);
                }
            }

            //Deleting extraSubject
            db.deleteExtraSubjects(standard, division, subjectListJsonArray);

            JSONObject responseJsonObject = new JSONObject();
            responseJsonObject.put("id", jsonObject.getLong("id"));

            JSONObject responseInfoJsonObject = new JSONObject();
            responseInfoJsonObject.put("response_code", 0);

            responseJsonObject.put("info", responseInfoJsonObject);
            client.sendMessage(responseJsonObject);
        } catch (Exception e) {
            Log.error(e.toString());
        } finally {
            db.closeConnection();
        }

        Log.info("Action Code 9 Completed for Client at " + client.getIpAddress());
    }
}
