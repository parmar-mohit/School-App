package ServerProgram.ActionCode;

import ServerProgram.Client.Client;
import ServerProgram.DatabaseCon;
import ServerProgram.Log;
import org.json.JSONObject;

public class SetPasswordNewUser extends Thread {

    private JSONObject jsonObject;
    private Client client;
    private DatabaseCon db;

    public SetPasswordNewUser(JSONObject jsonObject, Client client){
        this.jsonObject = jsonObject;
        this.client = client;
    }

    @Override
    public void run() {
        Log.info("Action Code 26 Started for Client at "+client.getIpAddress());

        try{
            db = new DatabaseCon();

            String phone = jsonObject.getJSONObject("info").getString("phone");
            String password = jsonObject.getJSONObject("info").getString("password");

            db.setPasswordNewUser(phone,password);

            JSONObject responseJsonObject = new JSONObject();
            responseJsonObject.put("id",jsonObject.getLong("id"));

            JSONObject responseInfoJsonObject = new JSONObject();
            responseInfoJsonObject.put("response_code",0);

            responseJsonObject.put("info",responseInfoJsonObject);

            client.sendMessage(responseJsonObject);
        }catch (Exception e){
            Log.error(e.toString());
        }finally {
            db.closeConnection();
        }

        Log.info("Action Code 26 Completed for Client at "+client.getIpAddress());
    }
}
