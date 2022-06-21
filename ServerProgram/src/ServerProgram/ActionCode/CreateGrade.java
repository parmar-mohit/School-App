package ServerProgram.ActionCode;

import ServerProgram.Client.Client;
import ServerProgram.DatabaseCon;
import ServerProgram.Log;
import org.json.JSONArray;
import org.json.JSONObject;

public class CreateGrade extends Thread {

    private JSONObject jsonObject;
    private Client client;
    private DatabaseCon db;

    public CreateGrade(JSONObject jsonObject,Client client){
        this.jsonObject = jsonObject;
        this.client = client;
    }

    @Override
    public void run() {
        Log.info("Action Code 4 Started for Client at "+client.getIpAddress());

        try{
           db = new DatabaseCon();

           JSONObject infoJsonObject = jsonObject.getJSONObject("info");
           int standard = infoJsonObject.getInt("standard");
           String division = infoJsonObject.getString("division");
           String teacherIncharge = infoJsonObject.getString("teacher_incharge");

            JSONObject responseJsonObject = new JSONObject();
            responseJsonObject.put("id",jsonObject.getLong("id"));

            JSONObject responseInfoJsonObject = new JSONObject();
           if( db.checkClassroom(standard,division) ){
               responseInfoJsonObject.put("response_code",1);
           }else{
               db.insertClassroom(standard,division,teacherIncharge);

               JSONArray subjectListJsonArray = infoJsonObject.getJSONArray("subject_list");
               for( int i = 0; i < subjectListJsonArray.length(); i++){
                   JSONObject subjectJsonObject = subjectListJsonArray.getJSONObject(i);
                   String subjectName = subjectJsonObject.getString("subject_name");
                   String phone = subjectJsonObject.getString("subject_teacher");
                   db.insertSubject(standard,division,subjectName,phone);
               }
               responseInfoJsonObject.put("response_code",0);
           }
            responseJsonObject.put("info",responseInfoJsonObject);
            client.sendMessage(responseJsonObject);
        }catch( Exception e){
            Log.error(e.toString());
        }finally {
            db.closeConnection();
        }
        Log.info("Action Code 4 Completed for Client at "+client.getIpAddress());
    }
}
