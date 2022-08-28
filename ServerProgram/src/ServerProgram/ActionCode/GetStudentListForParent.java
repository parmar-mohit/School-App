package ServerProgram.ActionCode;

import ServerProgram.Client.Client;
import ServerProgram.DatabaseCon;
import ServerProgram.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.ResultSet;
import java.util.Base64;

public class GetStudentListForParent extends Thread {

    private JSONObject jsonObject;
    private Client client;
    private DatabaseCon db;

    public GetStudentListForParent(JSONObject jsonObject,Client client){
        this.jsonObject = jsonObject;
        this.client = client;
    }

    @Override
    public void run() {
        Log.info("Action Code 28 Started for Client at "+client.getIpAddress());

        try{
            db = new DatabaseCon();

            String phone = jsonObject.getJSONObject("info").getString("phone");

            JSONArray studentJsonArray = new JSONArray();

            ResultSet studentResultSet = db.getStudentListForParent(phone);
            while( studentResultSet.next() ){
                JSONObject studentJsonObject = new JSONObject();
                int sid = studentResultSet.getInt("sid");

                studentJsonObject.put("sid",sid);
                studentJsonObject.put("firstname",studentResultSet.getString("firstname"));
                studentJsonObject.put("lastname",studentResultSet.getString("lastname"));
                studentJsonObject.put("standard",studentResultSet.getInt("standard"));
                studentJsonObject.put("division",studentResultSet.getString("division"));
                studentJsonObject.put("img",getImageString(sid));

                studentJsonArray.put(studentJsonObject);
            }

            JSONObject responseJsonObject = new JSONObject();
            responseJsonObject.put("id",jsonObject.getLong("id"));
            responseJsonObject.put("info",studentJsonArray);

            client.sendMessage(responseJsonObject);
        }catch(Exception e){
            Log.error(e.toString());
        }finally {
            db.closeConnection();
        }

        Log.info("Action Code 28 Completed for Client at "+client.getIpAddress());
    }

    private String getImageString(int sid) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new File("Student Images/" + sid + ".jpg"));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", baos);
            byte[] imgArray = baos.toByteArray();
            return Base64.getEncoder().encodeToString(imgArray);
        } catch (Exception e) {
            Log.error(e.toString());
        }

        return null;
    }
}
