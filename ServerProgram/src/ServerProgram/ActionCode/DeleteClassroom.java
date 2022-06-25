package ServerProgram.ActionCode;

import ServerProgram.Client.Client;
import ServerProgram.DatabaseCon;
import ServerProgram.Log;
import netscape.javascript.JSObject;
import org.json.JSONObject;

public class DeleteClassroom extends Thread {

    private JSONObject jsonObject;
    private Client client;
    private DatabaseCon db;

    public DeleteClassroom(JSONObject jsonObject,Client client){
        this.jsonObject = jsonObject;
        this.client = client;
    }

    @Override
    public void run() {
        Log.info("Action Code 10 Started for Client at "+client.getIpAddress());

        try{
            db = new DatabaseCon();

            JSONObject infoJsonObject = jsonObject.getJSONObject("info");
            int standard = infoJsonObject.getInt("standard");
            String division = infoJsonObject.getString("division");
            db.deleteClassroom(standard,division);

            JSONObject responseJsonObject = new JSONObject();
            responseJsonObject.put("id",jsonObject.getLong("id"));

            JSONObject responseInfoJsonObject = new JSONObject();
            responseInfoJsonObject.put("response_code",0);

            responseJsonObject.put("info",responseInfoJsonObject);

            client.sendMessage(responseJsonObject);
        }catch(Exception e){
            Log.error(e.toString());
        }finally {
            db.closeConnection();
        }

        Log.info("Action Code 10 Completed for Client at "+client.getIpAddress());
    }
}
