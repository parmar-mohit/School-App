package ServerProgram.ActionCode;

import ServerProgram.Client.Client;
import ServerProgram.DatabaseCon;
import ServerProgram.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;

public class GetClassroomListForPrincipal extends Thread {

    private JSONObject jsonObject;
    private Client client;
    private DatabaseCon db;

    public GetClassroomListForPrincipal(JSONObject jsonObject, Client client){
        this.jsonObject = jsonObject;
        this.client = client;
    }

    @Override
    public void run() {
        Log.info("Action Code 7 Started for Client at "+client.getIpAddress());

        try{
            db = new DatabaseCon();

            JSONObject responseJsonObject = new JSONObject();
            responseJsonObject.put("id",jsonObject.getLong("id"));

            JSONArray infoJsonArray = new JSONArray();
            ResultSet resultSet = db.getClassroomListForPrincipal();

            while(resultSet.next()){
                JSONObject classroomJsonObject = new JSONObject();
                classroomJsonObject.put("standard",resultSet.getInt("standard"));
                classroomJsonObject.put("division",resultSet.getString("division"));
                classroomJsonObject.put("teacher_incharge",resultSet.getBigDecimal("t_phone")+"");

                ResultSet nameResultSet = db.getTeacherName(classroomJsonObject.getString("teacher_incharge"));
                nameResultSet.next();
                classroomJsonObject.put("firstname",nameResultSet.getString("firstname"));
                classroomJsonObject.put("lastname",nameResultSet.getString("lastname"));

                JSONArray subjectListJsonArray = new JSONArray();
                ResultSet subjectListResultSet = db.getSubjectListOfClassroom(resultSet.getInt("standard"),resultSet.getString("division"));

                while( subjectListResultSet.next() ){
                    JSONObject subjectJsonObject = new JSONObject();
                    subjectJsonObject.put("subject_name",subjectListResultSet.getString("subject_name"));
                    subjectJsonObject.put("subject_incharge",subjectListResultSet.getBigDecimal("t_phone")+"");

                    ResultSet subjectTeacherNameResultSet = db.getTeacherName(subjectJsonObject.getString("subject_incharge"));
                    subjectTeacherNameResultSet.next();
                    subjectJsonObject.put("firstname",subjectTeacherNameResultSet.getString("firstname"));
                    subjectJsonObject.put("lastname",subjectTeacherNameResultSet.getString("lastname"));

                    subjectListJsonArray.put(subjectJsonObject);
                }
                classroomJsonObject.put("subject_list",subjectListJsonArray);

                infoJsonArray.put(classroomJsonObject);
            }

            responseJsonObject.put("info",infoJsonArray);
            client.sendMessage(responseJsonObject);
        }catch(Exception e){
            Log.error(e.toString());
        }finally {
            db.closeConnection();
        }

        Log.info("Action Code 7 Completed for Client at "+client.getIpAddress());
    }
}
