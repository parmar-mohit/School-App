package ServerProgram.ActionCode;

import ServerProgram.Client.Client;
import ServerProgram.DatabaseCon;
import ServerProgram.Log;
import org.json.JSONObject;

public class GetCredentials extends Thread {
    private final JSONObject jsonObject;
    private final Client client;
    private DatabaseCon db;

    public GetCredentials(JSONObject jsonObject, Client client) {
        this.jsonObject = jsonObject;
        this.client = client;
    }

    @Override
    public void run() {
        Log.info("Action Code 1 Started for Client at " + client.getIpAddress());

        JSONObject infoJsonObject = jsonObject.getJSONObject("info");
        String phone = infoJsonObject.getString("phone");

        try {
            db = new DatabaseCon();
            String password = db.getPassword(phone);
            JSONObject responseJsonObject = new JSONObject();
            responseJsonObject.put("id", jsonObject.getLong("id"));
            infoJsonObject = new JSONObject();
            if (password == null) {
                infoJsonObject.put("password", "null");
            } else {
                infoJsonObject.put("password", password);
            }
            responseJsonObject.put("info", infoJsonObject);
            client.sendMessage(responseJsonObject);
        } catch (Exception e) {
            Log.debug(e.toString());
        } finally {
            db.closeConnection();
        }
        Log.info("Action Code 1 Completed for Client at " + client.getIpAddress());
    }
}
