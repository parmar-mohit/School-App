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

public class GetTeacherInchargeStudentList extends Thread {

    private JSONObject jsonObject;
    private Client client;
    private DatabaseCon db;

    public GetTeacherInchargeStudentList(JSONObject jsonObject, Client client){
        this.jsonObject = jsonObject;
        this.client = client;
    }

    @Override
    public void run() {
        Log.info("Action Code 13 Started for Client at "+client.getIpAddress());

        try{
            db = new DatabaseCon();
            JSONObject infoJsonObject = jsonObject.getJSONObject("info");
            String phone = infoJsonObject.getString("phone");

            JSONObject responseJsonObject = new JSONObject();
            responseJsonObject.put("id",jsonObject.getLong("id"));
            JSONArray responseInfoJsonArray = new JSONArray();
            ResultSet studentResultSet = db.getTeacherInchargeStudentList(phone);
            while( studentResultSet.next() ){
                JSONObject studentJsonObject = new JSONObject();
                studentJsonObject.put("sid",studentResultSet.getInt("sid"));
                studentJsonObject.put("firstname",studentResultSet.getString("firstname"));
                studentJsonObject.put("lastname",studentResultSet.getString("lastname"));
                if( studentResultSet.getString("email") == null ){
                    studentJsonObject.put("email","null");
                }else{
                    studentJsonObject.put("email",studentResultSet.getString("email"));
                }
                if( studentResultSet.getBigDecimal("phone") == null ){
                    studentJsonObject.put("phone","null");
                }else{
                    studentJsonObject.put("phone",studentResultSet.getBigDecimal("phone").toString());
                }
                studentJsonObject.put("gender",studentResultSet.getString("gender"));
                studentJsonObject.put("dob",studentResultSet.getDate("dob").getTime());
                studentJsonObject.put("standard",studentResultSet.getInt("standard"));
                studentJsonObject.put("division",studentResultSet.getString("division"));

                ResultSet parentResultSet = db.getParents(studentResultSet.getInt("sid"));
                while( parentResultSet.next() ){
                    if( parentResultSet.getString("gender").equals("Male") ){
                        studentJsonObject.put("father_firstname",parentResultSet.getString("firstname"));
                        studentJsonObject.put("father_lastname",parentResultSet.getString("lastname"));
                        studentJsonObject.put("father_phone",parentResultSet.getString("phone"));
                        if( parentResultSet.getString("email") == null ){
                            studentJsonObject.put("father_email","null");
                        }else{
                            studentJsonObject.put("father_email",parentResultSet.getString("email"));
                        }
                    }

                    if( parentResultSet.getString("gender").equals("Female") ){
                        studentJsonObject.put("mother_firstname",parentResultSet.getString("firstname"));
                        studentJsonObject.put("mother_lastname",parentResultSet.getString("lastname"));
                        studentJsonObject.put("mother_phone",parentResultSet.getString("phone"));
                        if( parentResultSet.getString("email") == null ){
                            studentJsonObject.put("mother_email","null");
                        }else{
                            studentJsonObject.put("mother_email",parentResultSet.getString("email"));
                        }
                    }
                }

                //Checking and inserting null for parent details if parent details does not exist
                if( !studentJsonObject.has("father_phone") ){
                    studentJsonObject.put("father_firstname","null");
                    studentJsonObject.put("father_lastname","null");
                    studentJsonObject.put("father_phone","null");
                    studentJsonObject.put("father_email","null");
                }
                if(!studentJsonObject.has("mother_phone") ){
                    studentJsonObject.put("mother_firstname","null");
                    studentJsonObject.put("mother_lastname","null");
                    studentJsonObject.put("mother_phone","null");
                    studentJsonObject.put("mother_email","null");
                }
                studentJsonObject.put("img",getImageString(studentResultSet.getInt("sid")));

                responseInfoJsonArray.put(studentJsonObject);
            }

            responseJsonObject.put("info",responseInfoJsonArray);
            client.sendMessage(responseJsonObject);
        }catch(Exception e){
            Log.error(e.toString());
        }finally {
            db.closeConnection();
        }

        Log.info("Action Code 13 Completed for Client at "+client.getIpAddress());
    }

    private String getImageString(int sid){
        try{
            BufferedImage bufferedImage = ImageIO.read(new File("Student Images/"+sid+".jpg"));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage,"jpg",baos);
            byte[] imgArray = baos.toByteArray();
            return Base64.getEncoder().encodeToString(imgArray);
        }catch( Exception e){
            Log.error(e.toString());
        }

        return null;
    }
}
