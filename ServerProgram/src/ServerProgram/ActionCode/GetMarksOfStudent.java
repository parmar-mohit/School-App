package ServerProgram.ActionCode;

import ServerProgram.Client.Client;
import ServerProgram.DatabaseCon;
import ServerProgram.Log;
import com.mysql.cj.xdevapi.JsonString;
import org.json.JSONObject;

import java.sql.ResultSet;

public class GetMarksOfStudent extends Thread {

    private JSONObject jsonObject;
    private Client client;
    private DatabaseCon db;


    public GetMarksOfStudent(JSONObject jsonObject, Client client){
        this.jsonObject = jsonObject;
        this.client = client;
    }

    @Override
    public void run() {
        Log.info("Action Code 30 Started for Client at "+client.getIpAddress());

        try{
            db = new DatabaseCon();

            int sid  = jsonObject.getJSONObject("info").getInt("sid");

            JSONObject responseJsonObject = new JSONObject();
            responseJsonObject.put("id",jsonObject.getLong("id"));

            JSONObject responseInfoJsonObject = new JSONObject();
            responseInfoJsonObject.put("total_exam",db.getTotalExamCount(sid));

            responseJsonObject.put("info",responseInfoJsonObject);
            client.sendMessage(responseJsonObject);

            //removing infoJsonObject
            responseJsonObject.remove("info");

            ResultSet examResultSet = db.getExamDetails(sid);
            while(examResultSet.next()){
                JSONObject examJsonObject = new JSONObject();
                examJsonObject.put("exam_name",examResultSet.getString("exam_name"));
                examJsonObject.put("total_marks",examResultSet.getInt("total_marks"));
                examJsonObject.put("subject_name",examResultSet.getString("subject_name"));
                examJsonObject.put("date",examResultSet.getDate("exam_date").getTime());
                examJsonObject.put("marks",examResultSet.getInt("marks_obtained"));

                responseJsonObject.put("info",examJsonObject);
                client.sendMessage(responseJsonObject);
                responseJsonObject.remove("info");
            }
        }catch(Exception e){
            Log.error(e.toString());
        }finally {
            db.closeConnection();
        }

        Log.info("Action Code 30 Completed for Client at "+client.getIpAddress());
    }
}
