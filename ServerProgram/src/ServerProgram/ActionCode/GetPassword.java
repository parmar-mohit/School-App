package ServerProgram.ActionCode;

import ServerProgram.Client.Client;
import ServerProgram.DatabaseCon;
import ServerProgram.Log;
import org.json.JSONObject;

public class GetPassword extends Thread {

    private JSONObject jsonObject;
    private Client client;
    private DatabaseCon db;

    public GetPassword(JSONObject jsonObject, Client client) {
        this.jsonObject =jsonObject;
        this.client =client;
    }

    @Override
    public void run() {
        Log.info("Action Code 27 Started for Client at "+client.getIpAddress());

        try{
            db = new DatabaseCon();

            String phone = jsonObject.getJSONObject("info").getString("phone");

            JSONObject responseJsonObject = new JSONObject();
            responseJsonObject.put("id",jsonObject.getLong("id"));

            JSONObject responseInfoJsonObject = new JSONObject();

            if( db.checkIfUserExist(phone) ){
                String password = db.getParentPassword(phone);

                if( password == null ){
                    responseInfoJsonObject.put("password","not_registered");
                }else{
                    responseInfoJsonObject.put("password",password);
                }
            }else{
                responseInfoJsonObject.put("password","null");
            }

            responseJsonObject.put("info",responseInfoJsonObject);

            client.sendMessage(responseJsonObject);
        }catch(Exception e){
            Log.error(e.toString());
        }finally{
            db.closeConnection();
        }

        Log.info("Action Code 27 Completed for Client at "+client.getIpAddress());
    }
}
