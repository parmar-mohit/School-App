package ServerProgram.ActionCode;

import ServerProgram.Client.Client;
import ServerProgram.DatabaseCon;
import ServerProgram.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;

public class GetExamAndSubjectList extends Thread {

    private JSONObject jsonObject;
    private Client client;
    private DatabaseCon db;

    public GetExamAndSubjectList(JSONObject jsonObject, Client client){
        this.jsonObject = jsonObject;
        this.client = client;
    }

    @Override
    public void run() {
        Log.info("Action Code 18 Started for Client at "+client.getIpAddress());

        try{
            db = new DatabaseCon();

            JSONArray examNameArray = new JSONArray();
            ResultSet examNameResultSet = db.getDistinctExamName();
            while( examNameResultSet.next() ){
                examNameArray.put(examNameResultSet.getString(1));
            }

            String phone = jsonObject.getJSONObject("info").getString("phone");
            JSONArray subjectListJsonArray = new JSONArray();
            ResultSet subjectListResultSet = db.getSubjectListOfTeacher(phone);
            while(subjectListResultSet.next()){
                JSONObject subjectJsonObject = new JSONObject();
                subjectJsonObject.put("subject_id",subjectListResultSet.getInt("sub_id"));
                subjectJsonObject.put("subject_name",subjectListResultSet.getString("subject_name"));
                subjectJsonObject.put("standard",subjectListResultSet.getInt("standard"));
                subjectJsonObject.put("division",subjectListResultSet.getString("division"));
                subjectJsonObject.put("phone",subjectListResultSet.getBigDecimal("t_phone").toString());

                subjectListJsonArray.put(subjectJsonObject);
            }

            JSONObject responseJsonObject = new JSONObject();
            responseJsonObject.put("id",jsonObject.getLong("id"));

            JSONObject responseInfoJsonObject = new JSONObject();
            responseInfoJsonObject.put("exam_name",examNameArray);
            responseInfoJsonObject.put("subject_list",subjectListJsonArray);

            responseJsonObject.put("info",responseInfoJsonObject);
            client.sendMessage(responseJsonObject);
        }catch(Exception e){
            Log.error(e.toString());
        }finally {
            db.closeConnection();
        }

        Log.info("Action Code 18 Completed for Client at "+client.getIpAddress());
    }
}
