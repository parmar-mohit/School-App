package ServerProgram.ActionCode;

import ServerProgram.Client.Client;
import ServerProgram.DatabaseCon;
import ServerProgram.Log;
import org.json.JSONObject;

import java.io.File;

public class DeleteStudentId extends Thread {

    private JSONObject jsonObject;
    private Client client;
    private DatabaseCon db;

    public DeleteStudentId(JSONObject jsonObject, Client client) {
        this.jsonObject = jsonObject;
        this.client = client;
    }

    @Override
    public void run() {
        Log.info("Action Code 15 Started for Client at " + client.getIpAddress());

        try {
            db = new DatabaseCon();
            int sid = jsonObject.getJSONObject("info").getInt("sid");
            db.deleteStudentId(sid);
            deleteImage(sid);

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

        Log.info("Action Code 15 Completed for Client at " + client.getIpAddress());
    }

    private void deleteImage(int sid) {
        File imageFile = new File("Student Images/" + sid + ".jpg");
        imageFile.delete();
    }
}
