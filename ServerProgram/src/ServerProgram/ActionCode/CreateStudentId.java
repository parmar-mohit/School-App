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

public class CreateStudentId extends Thread {

    private JSONObject jsonObject;
    private Client client;
    private DatabaseCon db;

    public CreateStudentId(JSONObject jsonObject,Client client){
        this.jsonObject = jsonObject;
        this.client = client;
    }

    @Override
    public void run() {
        Log.info("Action Code 12 Started for Client at "+client.getIpAddress());

        try{
            db = new DatabaseCon();

            JSONObject infoJsonObject = jsonObject.getJSONObject("info");
            db.setAutoCommit(false);
            int sid = db.createStudentId(infoJsonObject);
            db.insertParent(sid,infoJsonObject);
            storeImage(sid,infoJsonObject.getString("img"));
            db.commit();
            db.setAutoCommit(true);

            JSONObject responseJsonObject = new JSONObject();
            responseJsonObject.put("id",jsonObject.getLong("id"));

            JSONObject responseInfoJsonObject = new JSONObject();
            responseInfoJsonObject.put("sid",sid);

            responseJsonObject.put("info",responseInfoJsonObject);
            client.sendMessage(responseJsonObject);
        }catch(Exception e){
            Log.error(e.toString());
        }finally {
            db.closeConnection();
        }

        Log.info("Action Code 12 Completed for Client at "+client.getIpAddress());
    }

    private void storeImage(int sid,String studentImgString) throws Exception{
        byte[] imgArray = Base64.getDecoder().decode(studentImgString);
        ByteArrayInputStream bais = new ByteArrayInputStream(imgArray);
        BufferedImage studentImg = ImageIO.read(bais);
        File imageDirectory = new File("Student Images");
        if( !imageDirectory.exists() ){
            imageDirectory.mkdir();
        }
        File imageFile = new File("Student Images/"+sid+".png");
        imageFile.createNewFile();
        ImageIO.write(studentImg,"png",imageFile);
    }
}
