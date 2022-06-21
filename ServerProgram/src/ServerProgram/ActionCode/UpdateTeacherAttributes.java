package ServerProgram.ActionCode;

import ServerProgram.Client.Client;
import ServerProgram.DatabaseCon;
import ServerProgram.Log;
import org.json.JSONObject;

public class UpdateTeacherAttributes extends Thread {
    private JSONObject jsonObject;
    private Client client;
    private DatabaseCon db;

    public UpdateTeacherAttributes(JSONObject jsonObject, Client client){
        this.jsonObject = jsonObject;
        this.client = client;
    }

    @Override
    public void run() {
        Log.info("Action Code 6 Started for Client at "+client.getIpAddress());
        try{
            db = new DatabaseCon();

            JSONObject infoJsonObject = jsonObject.getJSONObject("info");
            JSONObject responseJsonObject = new JSONObject();
            responseJsonObject.put("id",jsonObject.getLong("id"));
            JSONObject responseInfoJsonObject = new JSONObject();
            responseInfoJsonObject.put("response_code",0);

            //Fetching values from JSON Objects
            String phone = infoJsonObject.getString("phone");
            String firstname = infoJsonObject.getString("firstname");
            String lastname = infoJsonObject.getString("lastname");
            String email = infoJsonObject.getString("email");
            String gender = infoJsonObject.getString("gender");

            db.updateTeacherAttributes(phone,firstname,lastname,email,gender);

            responseInfoJsonObject.put("response_code",0);

            responseJsonObject.put("info",responseInfoJsonObject);
            client.sendMessage(responseJsonObject);
        }catch(Exception e){
            Log.error(e.toString());
        }finally {
            db.closeConnection();
        }

        Log.info("Action Code 6 Completed for Client at "+client.getIpAddress());
    }
}
