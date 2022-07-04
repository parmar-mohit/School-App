package ServerProgram.ActionCode;

import ServerProgram.Client.Client;
import ServerProgram.DatabaseCon;
import ServerProgram.Log;
import org.json.JSONObject;

public class CreateTeacherId extends Thread{

    private JSONObject jsonObject;
    private Client client;

    private DatabaseCon db;

    public CreateTeacherId(JSONObject jsonObject,Client client){
        this.jsonObject = jsonObject;
        this.client = client;
    }
    @Override
    public void run() {
        Log.info("Action Code 2 Started for Client at "+client.getIpAddress());

        JSONObject infoJsonObject = jsonObject.getJSONObject("info");
        String phone = infoJsonObject.getString("phone");
        String firstname = infoJsonObject.getString("firstname");
        String lastname = infoJsonObject.getString("lastname");
        String email = infoJsonObject.getString("email");
        String gender = infoJsonObject.getString("gender");
        String password = infoJsonObject.getString("password");

        try{
            db = new DatabaseCon();

            //Creating response object
            JSONObject responseJsonObject = new JSONObject();
            responseJsonObject.put("id",jsonObject.getLong("id"));
            JSONObject responseInfoObject = new JSONObject();

            //Checking if a teacher with same phone exist
            if( db.checkTeacherPhone(phone) ){
                responseInfoObject.put("response_code",1);
            }else{
                db.createTeacherId(phone,firstname,lastname,email,gender,password);
                responseInfoObject.put("response_code",0);
            }
            responseJsonObject.put("info",responseInfoObject);
            client.sendMessage(responseJsonObject);
        }catch( Exception e ){
            Log.error(e.toString());
        }finally {
            db.closeConnection();
        }
        Log.info("Action Code 2 Completed for Client at "+client.getIpAddress());
    }
}
