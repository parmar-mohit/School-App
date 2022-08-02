package ServerProgram.ActionCode;

import ServerProgram.Client.Client;
import ServerProgram.DatabaseCon;
import ServerProgram.Log;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Base64;

public class UpdateStudentId extends Thread {

    private JSONObject jsonObject;
    private Client client;
    private DatabaseCon db;

    public UpdateStudentId(JSONObject jsonObject, Client client) {
        this.jsonObject = jsonObject;
        this.client = client;
    }

    @Override
    public void run() {
        Log.info("Action Code 16 Started for Client at " + client.getIpAddress());

        try {
            db = new DatabaseCon();
            JSONObject studentJsonObject = jsonObject.getJSONObject("info");
            db.updateStudentId(studentJsonObject);
            db.updateParent(studentJsonObject);
            if (studentJsonObject.has("img")) {
                updateImage();
            }

            JSONObject responseJsonObject = new JSONObject();
            responseJsonObject.put("id", jsonObject.getLong("id"));

            JSONObject responseInfoJsonObject = new JSONObject();
            responseInfoJsonObject.put("response_code", 0);

            responseJsonObject.put("info", responseInfoJsonObject);
            client.sendMessage(responseJsonObject);
        } catch (Exception e) {
            Log.error(e.toString());
        } finally {
            db.closeConnection();
        }

        Log.info("Action Code 16 Completed for Client at " + client.getIpAddress());
    }

    private void updateImage() {
        try {
            String studentImgString = jsonObject.getJSONObject("info").getString("img");
            byte[] imgArray = Base64.getDecoder().decode(studentImgString);
            ByteArrayInputStream bais = new ByteArrayInputStream(imgArray);
            BufferedImage studentImg = ImageIO.read(bais);
            int sid = jsonObject.getJSONObject("info").getInt("sid");
            File imageFile = new File("Student Images/" + sid + ".jpg");
            ImageIO.write(studentImg, "jpg", imageFile);
        } catch (Exception e) {
            Log.error(e.toString());
        }
    }
}
