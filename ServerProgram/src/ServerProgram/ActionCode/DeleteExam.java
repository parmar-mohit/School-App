package ServerProgram.ActionCode;

import ServerProgram.Client.Client;
import ServerProgram.DatabaseCon;
import ServerProgram.Log;
import org.json.JSONObject;

public class DeleteExam extends Thread {

    private JSONObject jsonObject;
    private Client client;
    private DatabaseCon db;

    public DeleteExam(JSONObject jsonObject, Client client) {
        this.jsonObject = jsonObject;
        this.client = client;
    }

    @Override
    public void run() {
        Log.info("Action Code 24 Started for Client at " + client.getIpAddress());

        try {
            db = new DatabaseCon();

            int examId = jsonObject.getJSONObject("info").getInt("exam_id");
            db.deleteExam(examId);

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

        Log.info("Action Code 24 Completed for Client at " + client.getIpAddress());
    }
}
