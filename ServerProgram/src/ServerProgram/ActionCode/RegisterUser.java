package ServerProgram.ActionCode;

import ServerProgram.Client.Client;
import ServerProgram.DatabaseCon;
import ServerProgram.Log;
import org.json.JSONObject;

public class RegisterUser extends Thread {

    private JSONObject jsonObject;
    private Client client;
    private DatabaseCon db;

    public RegisterUser(JSONObject jsonObject,Client client){
        this.jsonObject = jsonObject;
        this.client = client;
    }

    @Override
    public void run() {
        Log.info("Action Code 25 Started for Client at "+client.getIpAddress());

        try{
            db = new DatabaseCon();

            String phone = jsonObject.getJSONObject("info").getString("phone");
            long dob = jsonObject.getJSONObject("info").getLong("dob_of_student");

            JSONObject responseJsonObject = new JSONObject();
            responseJsonObject.put("id",jsonObject.getLong("id"));

            JSONObject responseInfoJsonObject = new JSONObject();

            if( ! db.checkIfUserExist(phone) ){
                responseInfoJsonObject.put("response_code",1);
            }else if( db.isUserRegistered(phone) ){
                responseInfoJsonObject.put("response_code",2);
            }else if( !db.isUserDOBValid(phone,dob) ){
                responseInfoJsonObject.put("response_code",3);
            }else{
                responseInfoJsonObject.put("response_code",0);
            }

            responseJsonObject.put("info",responseInfoJsonObject);

            client.sendMessage(responseJsonObject);
        }catch(Exception e){
            Log.error(e.toString());
        }finally {
            db.closeConnection();
        }

        Log.info("Action Code 25 Completed for Client at "+client.getIpAddress());
    }
}
