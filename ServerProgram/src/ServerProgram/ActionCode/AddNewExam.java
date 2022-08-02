package ServerProgram.ActionCode;

import ServerProgram.Client.Client;
import ServerProgram.DatabaseCon;
import ServerProgram.Log;
import org.json.JSONObject;

public class AddNewExam extends Thread {

    private JSONObject jsonObject;
    private Client client;
    private DatabaseCon db;

    public AddNewExam(JSONObject jsonObject, Client client) {
        this.jsonObject = jsonObject;
        this.client = client;
    }

    @Override
    public void run() {
        Log.info("Action Code 20 Started for Client at " + client.getIpAddress());

        try {
            db = new DatabaseCon();

            JSONObject examJsonObject = jsonObject.getJSONObject("info");
            db.addNewExam(examJsonObject);

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

        Log.info("Action Code 20 Completed for Client at " + client.getIpAddress());
    }
}
