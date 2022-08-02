package ServerProgram.ActionCode;

import ServerProgram.Client.Client;
import ServerProgram.DatabaseCon;
import ServerProgram.Log;
import org.json.JSONObject;

import java.sql.ResultSet;

public class GetTeacherList extends Thread {

    private JSONObject jsonObject;
    private Client client;
    private DatabaseCon db;

    public GetTeacherList(JSONObject jsonObject, Client client) {
        this.jsonObject = jsonObject;
        this.client = client;
    }

    @Override
    public void run() {
        Log.info("Action Code 3 Started for Client at " + client.getIpAddress());

        try {
            db = new DatabaseCon();

            JSONObject responseJsonObject = new JSONObject();
            responseJsonObject.put("id", jsonObject.getLong("id"));

            JSONObject responseInfoJsonObject = new JSONObject();
            responseInfoJsonObject.put("total_teachers", db.getTotalTeachers());

            responseJsonObject.put("info", responseInfoJsonObject);

            //Sending first message containing total teachers
            client.sendMessage(responseJsonObject);

            //removing info
            responseJsonObject.remove("info");

            ResultSet resultSet = db.getTeacherList();
            while (resultSet.next()) {
                JSONObject teacherJsonObject = new JSONObject();
                teacherJsonObject.put("phone", resultSet.getBigDecimal("t_phone") + "");
                teacherJsonObject.put("firstname", resultSet.getString("firstname"));
                teacherJsonObject.put("lastname", resultSet.getString("lastname"));
                teacherJsonObject.put("email", resultSet.getString("email"));
                teacherJsonObject.put("gender", resultSet.getString("gender"));

                responseJsonObject.put("info", teacherJsonObject);
                client.sendMessage(responseJsonObject); //sending Teacher Object
                responseJsonObject.remove("info");
            }
        } catch (Exception e) {
            Log.error(e.toString());
        } finally {
            db.closeConnection();
        }

        Log.info("Action Code 3 Completed for Client at " + client.getIpAddress());
    }
}
