package ServerProgram.ActionCode;

import ServerProgram.Client.Client;
import ServerProgram.DatabaseCon;
import ServerProgram.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;

public class GetTeacherList extends Thread {

    private JSONObject jsonObject;
    private Client client;
    private DatabaseCon db;

    public GetTeacherList(JSONObject jsonObject,Client client){
        this.jsonObject = jsonObject;
        this.client = client;
    }

    @Override
    public void run() {
        Log.info("Action Code 3 Started for Client at "+client.socket.getInetAddress().getHostAddress());

        try{
            db = new DatabaseCon();
            ResultSet resultSet = db.getTeacherList();
            JSONObject responseJsonObject = new JSONObject();
            responseJsonObject.put("id",jsonObject.getLong("id"));

            JSONArray jsonArray = new JSONArray();
            while(resultSet.next()){
                JSONObject teacherJsonObject = new JSONObject();
                teacherJsonObject.put("phone",resultSet.getBigDecimal("t_phone")+"");
                teacherJsonObject.put("firstname",resultSet.getString("firstname"));
                teacherJsonObject.put("lastname",resultSet.getString("lastname"));
                jsonArray.put(teacherJsonObject);
            }

            responseJsonObject.put("info",jsonArray);
            client.sendMessage(responseJsonObject);
        }catch (Exception  e){
            Log.error(e.toString());
        }finally {
            db.closeConnection();
        }

        Log.info("Action Code 3 Completed for Client at "+client.socket.getInetAddress().getHostAddress());
    }
}
