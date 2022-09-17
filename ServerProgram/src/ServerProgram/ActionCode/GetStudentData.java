package ServerProgram.ActionCode;

import ServerProgram.Client.Client;
import ServerProgram.DatabaseCon;
import ServerProgram.Log;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.ResultSet;
import java.util.Base64;

public class GetStudentData extends Thread {

    private JSONObject jsonObject;
    private Client client;
    private DatabaseCon db;

    public GetStudentData(JSONObject jsonObject, Client client){
        this.jsonObject = jsonObject;
        this.client = client;
    }

    @Override
    public void run() {
        Log.info("Action Code 29 Started for Client at "+client.getIpAddress());

        try{
            db = new DatabaseCon();

            int sid = jsonObject.getJSONObject("info").getInt("sid");

            ResultSet studentResultSet = db.getStudentData(sid);

            JSONObject responseJsonObject = new JSONObject();
            responseJsonObject.put("id",jsonObject.getLong("id"));

            JSONObject responseInfoJsonObject = new JSONObject();
            studentResultSet.next();
            responseInfoJsonObject.put("firstname",studentResultSet.getString("firstname"));
            responseInfoJsonObject.put("lastname",studentResultSet.getString("lastname"));
            responseInfoJsonObject.put("standard",studentResultSet.getInt("standard"));
            responseInfoJsonObject.put("division",studentResultSet.getString("division"));
            responseInfoJsonObject.put("img",getImageString(sid));

            responseJsonObject.put("info",responseInfoJsonObject);

            client.sendMessage(responseJsonObject);
        }catch(Exception e){
            Log.error(e.toString());
        }finally {
            db.closeConnection();
        }

        Log.info("Action Code 29 Completed for Client at "+client.getIpAddress());
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
