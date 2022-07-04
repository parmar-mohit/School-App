package ServerProgram.ActionCode;

import ServerProgram.Client.Client;
import ServerProgram.DatabaseCon;
import ServerProgram.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;

public class GetStandardDivisionOfTeacher extends Thread {

    private JSONObject jsonObject;
    private Client client;
    private DatabaseCon db;

    public GetStandardDivisionOfTeacher(JSONObject jsonObject, Client client){
        this.jsonObject = jsonObject;
        this.client = client;
    }

    @Override
    public void run() {
        Log.info("Action Code 11 Started for Client at "+client.getIpAddress());

        try{
            db = new DatabaseCon();

            String phone = jsonObject.getJSONObject("info").getString("phone");

            JSONObject responseJsonObject = new JSONObject();
            responseJsonObject.put("id",jsonObject.getLong("id"));

            JSONArray infoJsonArray = new JSONArray();

            ResultSet standardResultSet = db.getDistinctStandard(phone);
            while( standardResultSet.next() ){
                int standard = standardResultSet.getInt(1);
                JSONObject standardJsonObject = new JSONObject();
                standardJsonObject.put("standard",standard);

                JSONArray divisionArray = new JSONArray();

                ResultSet divisionResultSet = db.getDistinctDivision(standard,phone);
                while( divisionResultSet.next() ){
                    divisionArray.put(divisionResultSet.getString(1));
                }
                standardJsonObject.put("division",divisionArray);
                infoJsonArray.put(standardJsonObject);
            }

            responseJsonObject.put("info",infoJsonArray);

            client.sendMessage(responseJsonObject);
        }catch(Exception e){
            Log.error(e.toString());
        }finally {
            db.closeConnection();
        }

        Log.info("Action Code 11 Completed for Client at "+client.getIpAddress());
    }
}
