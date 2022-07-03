package ServerProgram;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.sql.*;

import static java.sql.Types.BIGINT;
import static java.sql.Types.VARCHAR;

public class DatabaseCon {

    private static final String URL = "jdbc:mysql://localhost:3306/school_app";
    private static final String USERNAME = "school_user";
    private static final String PASSWORD = "school_pass";
    private Connection db;

    public DatabaseCon() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver"); //Loading SQL Connector Driver
        db = DriverManager.getConnection(URL,USERNAME,PASSWORD); //Creating Connection with database
    }

    public void closeConnection(){
        try {
            db.close();
        }catch(Exception e ) {
            System.out.println(e);
        }
    }
    public String getPassword(String phone) throws Exception{
        PreparedStatement preparedStatement = db.prepareStatement("SELECT password FROM teacher WHERE t_phone=?;");
        preparedStatement.setBigDecimal(1,new BigDecimal(phone));
        ResultSet resultSet = preparedStatement.executeQuery();
        if(!resultSet.next()) {
            return null;
        }else {
            return resultSet.getString("password");
        }
    }

    public void createTeacherId(String phone,String firstname, String lastname, String email, String gender, String password) throws  Exception{
        PreparedStatement preparedStatement = db.prepareStatement("INSERT INTO teacher VALUES(?,?,?,?,?,?);");
        preparedStatement.setBigDecimal(1,new BigDecimal(phone));
        preparedStatement.setString(2,firstname.toLowerCase());
        preparedStatement.setString(3,lastname.toLowerCase());
        preparedStatement.setString(4,email.toLowerCase());
        preparedStatement.setString(5,gender);
        preparedStatement.setString(6,password);
        preparedStatement.executeUpdate();
    }

    public ResultSet getTeacherList() throws Exception {
        PreparedStatement preparedStatement = db.prepareStatement("SELECT t_phone,firstname,lastname,email,gender FROM teacher;");
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public Boolean checkPhone(String phone) throws Exception{
        PreparedStatement preparedStatement = db.prepareStatement("SELECT EXISTS (SELECT t_phone FROM teacher WHERE t_phone = ?);");
        preparedStatement.setBigDecimal(1,new BigDecimal(phone));
        ResultSet result = preparedStatement.executeQuery();
        result.next();
        return result.getBoolean(1);
    }

    public boolean checkClassroom(int standard,String division) throws Exception{
        PreparedStatement preparedStatement = db.prepareStatement("SELECT * FROM classroom WHERE standard = ? AND DIVISION = ?;");
        preparedStatement.setInt(1,standard);
        preparedStatement.setString(2,division);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

    public void insertClassroom(int standard, String division, String phone) throws Exception{
        PreparedStatement preparedStatement = db.prepareStatement("INSERT INTO classroom VALUES(?,?,?);");
        preparedStatement.setInt(1,standard);
        preparedStatement.setString(2,division);
        preparedStatement.setBigDecimal(3,new BigDecimal(phone));
        preparedStatement.executeUpdate();
    }


    public void insertSubject(int standard,String division,String subjectName,String phone) throws Exception{
        //Inserting New Subject
        PreparedStatement preparedStatement = db.prepareStatement("INSERT INTO subject(standard,division,subject_name,t_phone) VALUES(?,?,?,?);");
        preparedStatement.setInt(1,standard);
        preparedStatement.setString(2,division);
        preparedStatement.setString(3,subjectName);
        preparedStatement.setBigDecimal(4,new BigDecimal(phone));
        preparedStatement.executeUpdate();
    }

    public void changePassword(String phone, String password) throws Exception{
        PreparedStatement preparedStatement = db.prepareStatement("UPDATE teacher SET password = ? WHERE t_phone  = ?;");
        preparedStatement.setString(1,password);
        preparedStatement.setBigDecimal(2,new BigDecimal(phone));
        preparedStatement.executeUpdate();
    }

    public void updateTeacherAttributes(String phone,String firstname,String lastname,String email,String gender) throws Exception{
        //Updating in teacher Table
        PreparedStatement preparedStatement = db.prepareStatement("UPDATE teacher SET firstname =?, lastname = ?, email = ?, gender = ? WHERE t_phone = ?;");
        preparedStatement.setString(1,firstname);
        preparedStatement.setString(2,lastname);
        preparedStatement.setString(3,email);
        preparedStatement.setString(4,gender);
        preparedStatement.setBigDecimal(5,new BigDecimal(phone));
        preparedStatement.executeUpdate();
    }

    public ResultSet getClassroomList() throws Exception{
        PreparedStatement preparedStatement = db.prepareStatement("SELECT * FROM classroom;");
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public ResultSet getTeacherName(String phone) throws Exception{
        PreparedStatement preparedStatement = db.prepareStatement("SELECT firstname,lastname FROM teacher WHERE t_phone = ?;");
        preparedStatement.setBigDecimal(1,new BigDecimal(phone));
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public ResultSet getSubjectList(int standard,String division) throws Exception{
        PreparedStatement preparedStatement = db.prepareStatement("SELECT subject_name,t_phone FROM subject WHERE standard = ? AND division = ?;");
        preparedStatement.setInt(1,standard);
        preparedStatement.setString(2,division);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public boolean checkIfTeacherIncharge(String phone) throws Exception{
        PreparedStatement preparedStatement1 = db.prepareStatement("SELECT EXISTS ( SELECT * FROM subject WHERE t_phone = ? );");
        preparedStatement1.setBigDecimal(1,new BigDecimal(phone));
        PreparedStatement preparedStatement2 = db.prepareStatement("SELECT EXISTS ( SELECT * FROM classroom WHERE t_phone = ? );");
        preparedStatement2.setBigDecimal(1,new BigDecimal(phone));
        ResultSet resultSet1 = preparedStatement1.executeQuery();
        resultSet1.next();
        ResultSet resultSet2 = preparedStatement2.executeQuery();
        resultSet2.next();
        return resultSet1.getBoolean(1) || resultSet2.getBoolean(1);
    }

    public void deleteTeacherId(String phone) throws Exception {
        PreparedStatement preparedStatement = db.prepareStatement("DELETE FROM teacher WHERE t_phone = ?;");
        preparedStatement.setBigDecimal(1,new BigDecimal(phone));
        preparedStatement.executeUpdate();
    }

    public void updateClassroomTeacherIncharge(int standard,String divison,String phone) throws Exception{
        PreparedStatement preparedStatement = db.prepareStatement("UPDATE classroom SET t_phone = ? WHERE standard = ? AND division = ?;");
        preparedStatement.setBigDecimal(1,new BigDecimal(phone));
        preparedStatement.setInt(2,standard);
        preparedStatement.setString(3,divison);
        preparedStatement.executeUpdate();
    }

    public void updateSubject(int standard,String division,JSONObject subjectJsonObject) throws Exception {
        PreparedStatement preparedStatement = db.prepareStatement("UPDATE subject SET subject_name = ?, t_phone = ? WHERE standard = ? AND division =? AND subject_name = ?;");
        preparedStatement.setString(1,subjectJsonObject.getString("new_subject_name"));
        preparedStatement.setBigDecimal(2,new BigDecimal(subjectJsonObject.getString("new_subject_teacher")));
        preparedStatement.setInt(3,standard);
        preparedStatement.setString(4,division);
        preparedStatement.setString(5,subjectJsonObject.getString("old_subject_name"));
        preparedStatement.executeUpdate();
    }

    public void deleteExtraSubjects(int standard,String division,JSONArray subjectListJsonArray) throws Exception {
        String query = "DELETE FROM subject WHERE standard = ? AND division = ? AND subject_name NOT IN(";
        for( int i = 0; i < subjectListJsonArray.length(); i++) {
            query += "\"" + subjectListJsonArray.getJSONObject(i).getString("new_subject_name")+"\",";
        }
        query = query.substring(0,query.length()-1);
        query += ");";

        PreparedStatement preparedStatement = db.prepareStatement(query);
        preparedStatement.setInt(1,standard);
        preparedStatement.setString(2,division);
        preparedStatement.executeUpdate();
    }

    public void deleteClassroom(int standard,String division) throws Exception{
        PreparedStatement preparedStatement = db.prepareStatement("DELETE FROM classroom WHERE standard = ? AND division = ? ;");
        preparedStatement.setInt(1,standard);
        preparedStatement.setString(2,division);
        preparedStatement.executeUpdate();
    }

    public ResultSet getDistinctStandard(String phone) throws Exception{
        PreparedStatement preparedStatement = db.prepareStatement("SELECT DISTINCT(standard) FROM classroom WHERE t_phone = ?;");
        preparedStatement.setBigDecimal(1,new BigDecimal(phone));
        return preparedStatement.executeQuery();
    }

    public ResultSet getDistinctDivision(int standard,String phone) throws Exception {
        PreparedStatement preparedStatement = db.prepareStatement("SELECT DISTINCT(division) FROM classroom WHERE standard = ? AND t_phone = ?");
        preparedStatement.setInt(1,standard);
        preparedStatement.setBigDecimal(2,new BigDecimal(phone));
        return preparedStatement.executeQuery();
    }

    public int createStudentId(JSONObject studentJsonObject) throws Exception {
        PreparedStatement preparedStatement = db.prepareStatement("INSERT INTO student(firstname,lastname,gender,dob,email,phone,standard,division) VALUES(?,?,?,?,?,?,?,?);");
        preparedStatement.setString(1,studentJsonObject.getString("firstname"));
        preparedStatement.setString(2,studentJsonObject.getString("lastname"));
        preparedStatement.setString(3,studentJsonObject.getString("gender"));
        preparedStatement.setDate(4,new Date(studentJsonObject.getLong("dob")));
        if( studentJsonObject.getString("email").equals("null")  ){
            preparedStatement.setNull(5,VARCHAR);
        }else{
            preparedStatement.setString(5,studentJsonObject.getString("email"));
        }

        if( studentJsonObject.getString("phone").equals("null") ){
            preparedStatement.setNull(6,BIGINT);
        }else{
            preparedStatement.setBigDecimal(6,new BigDecimal(studentJsonObject.getString("phone")));
        }

        preparedStatement.setInt(7,studentJsonObject.getInt("standard"));
        preparedStatement.setString(8,studentJsonObject.getString("division"));
        preparedStatement.executeUpdate();

        preparedStatement = db.prepareStatement("SELECT LAST_INSERT_ID();");
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1);
    }

    public void insertParent(int sid,JSONObject studentJsonObject) throws Exception{
        //Execute only if father data exist
        if( !studentJsonObject.getString("father_phone").equals("null") ){
            //Checking if father phone exist
            String fatherPhone = studentJsonObject.getString("father_phone");

            PreparedStatement preparedStatement = db.prepareStatement("SELECT EXISTS( SELECT phone FROM parent WHERE phone = ? );");
            preparedStatement.setBigDecimal(1,new BigDecimal(fatherPhone));
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            if( !resultSet.getBoolean(1) ){
                preparedStatement = db.prepareStatement("INSERT INTO parent VALUES(?,?,?,?,?);");
                preparedStatement.setBigDecimal(1,new BigDecimal(fatherPhone));
                preparedStatement.setString(2,studentJsonObject.getString("father_firstname"));
                preparedStatement.setString(3,studentJsonObject.getString("father_lastname"));
                if( studentJsonObject.getString("father_email").equals("null") ){
                    preparedStatement.setNull(4,VARCHAR);
                }else{
                    preparedStatement.setString(4,studentJsonObject.getString("father_email"));
                }
                preparedStatement.setString(5,"Male");
                preparedStatement.executeUpdate();
            }

            preparedStatement = db.prepareStatement("INSERT INTO parent_child VALUES(?,?);");
            preparedStatement.setBigDecimal(1,new BigDecimal(fatherPhone));
            preparedStatement.setInt(2,sid);
            preparedStatement.executeUpdate();
        }

        //Execute only if mother data exist
        if( !studentJsonObject.getString("mother_phone").equals("null") ) {
            //Checking if father phone exist
            String motherPhone = studentJsonObject.getString("mother_phone");

            PreparedStatement preparedStatement = db.prepareStatement("SELECT EXISTS( SELECT phone FROM parent WHERE phone = ? );");
            preparedStatement.setBigDecimal(1, new BigDecimal(motherPhone));
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            if (!resultSet.getBoolean(1)) {
                preparedStatement = db.prepareStatement("INSERT INTO parent VALUES(?,?,?,?,?);");
                preparedStatement.setBigDecimal(1, new BigDecimal(motherPhone));
                preparedStatement.setString(2, studentJsonObject.getString("mother_firstname"));
                preparedStatement.setString(3, studentJsonObject.getString("mother_lastname"));
                if (studentJsonObject.getString("mother_email").equals("null")) {
                    preparedStatement.setNull(4, VARCHAR);
                } else {
                    preparedStatement.setString(4, studentJsonObject.getString("mother_email"));
                }
                preparedStatement.setString(5,"Female");
                preparedStatement.executeUpdate();
            }

            preparedStatement = db.prepareStatement("INSERT INTO parent_child VALUES(?,?);");
            preparedStatement.setBigDecimal(1, new BigDecimal(motherPhone));
            preparedStatement.setInt(2, sid);
            preparedStatement.executeUpdate();
        }
    }

    public void setAutoCommit(boolean b) throws Exception{
        db.setAutoCommit(b);
    }

    public void commit() throws Exception {
        db.commit();
    }

    public ResultSet getTeacherInchargeStudentList(String phone) throws Exception{
        PreparedStatement preparedStatement = db.prepareStatement("SELECT * FROM student WHERE standard = ( SELECT DISTINCT(standard) FROM classroom WHERE t_phone = ? ) AND division IN (SELECT DISTINCT(division) FROM classroom WHERE t_phone = ?);");
        preparedStatement.setBigDecimal(1,new BigDecimal(phone));
        preparedStatement.setBigDecimal(2,new BigDecimal(phone));
        return preparedStatement.executeQuery();
    }

    public ResultSet getParents(int sid) throws Exception{
        PreparedStatement preparedStatement = db.prepareStatement("SELECT * FROM parent WHERE phone IN ( SELECT phone FROM parent_child WHERE sid = ? );");
        preparedStatement.setInt(1,sid);
        return preparedStatement.executeQuery();
    }

    public ResultSet getSubjectTeacherStudentList(String phone) throws Exception{
        PreparedStatement preparedStatement = db.prepareStatement("SELECT * FROM student WHERE standard = ( SELECT DISTINCT(standard) FROM subject WHERE t_phone = ? ) AND division IN (SELECT DISTINCT(division) FROM subject WHERE t_phone = ?);");
        preparedStatement.setBigDecimal(1,new BigDecimal(phone));
        preparedStatement.setBigDecimal(2,new BigDecimal(phone));
        return preparedStatement.executeQuery();
    }

    public void deleteStudentId(int sid) throws Exception{
        PreparedStatement preparedStatement = db.prepareStatement("DELETE FROM student WHERE sid =?");
        preparedStatement.setInt(1,sid);
        preparedStatement.executeUpdate();
    }

    public void updateStudentId(JSONObject studentJsonObject) throws Exception{
        PreparedStatement preparedStatement = db.prepareStatement("UPDATE student SET firstname = ?, lastname=?, gender = ?, dob=? , email=?, phone =?, standard=?,division=? WHERE sid = ?");
        preparedStatement.setString(1,studentJsonObject.getString("firstname"));
        preparedStatement.setString(2,studentJsonObject.getString("lastname"));
        preparedStatement.setString(3,studentJsonObject.getString("gender"));
        preparedStatement.setDate(4,new Date(studentJsonObject.getLong("dob")));
        if( studentJsonObject.getString("email").equals("null")  ){
            preparedStatement.setNull(5,VARCHAR);
        }else{
            preparedStatement.setString(5,studentJsonObject.getString("email"));
        }

        if( studentJsonObject.getString("phone").equals("null") ){
            preparedStatement.setNull(6,BIGINT);
        }else{
            preparedStatement.setBigDecimal(6,new BigDecimal(studentJsonObject.getString("phone")));
        }

        preparedStatement.setInt(7,studentJsonObject.getInt("standard"));
        preparedStatement.setString(8,studentJsonObject.getString("division"));
        preparedStatement.setInt(9,studentJsonObject.getInt("sid"));
        preparedStatement.executeUpdate();
    }

    public void updateParent(JSONObject studentJsonObject) throws Exception{
        //Updating Father

        //Deleting Previous details
        if( !studentJsonObject.getString("father_old_phone").equals("null") ) {
            PreparedStatement preparedStatement = db.prepareStatement("DELETE FROM parent WHERE phone = ?");
            preparedStatement.setBigDecimal(1, new BigDecimal(studentJsonObject.getString("father_old_phone")));
            preparedStatement.executeUpdate();
        }
        if( !studentJsonObject.getString("father_new_phone").equals("null") ){
            PreparedStatement preparedStatement = db.prepareStatement("INSERT INTO parent VALUES(?,?,?,?,?);");
            preparedStatement.setBigDecimal(1,new BigDecimal(studentJsonObject.getString("father_new_phone")));
            preparedStatement.setString(2,studentJsonObject.getString("father_firstname"));
            preparedStatement.setString(3,studentJsonObject.getString("father_lastname"));
            if( studentJsonObject.getString("father_email").equals("null") ){
                preparedStatement.setNull(4,VARCHAR);
            }else{
                preparedStatement.setString(4,studentJsonObject.getString("father_email"));
            }
            preparedStatement.setString(5,"Male");
            preparedStatement.executeUpdate();

            preparedStatement = db.prepareStatement("INSERT INTO parent_child VALUES(?,?);");
            preparedStatement.setBigDecimal(1,new BigDecimal(studentJsonObject.getString("father_new_phone")));
            preparedStatement.setInt(2,studentJsonObject.getInt("sid"));
            preparedStatement.executeUpdate();
        }

        //Updating Mother

        //Deleting Previous details
        if( !studentJsonObject.getString("mother_old_phone").equals("null") ) {
            PreparedStatement preparedStatement = db.prepareStatement("DELETE FROM parent WHERE phone = ?");
            preparedStatement.setBigDecimal(1, new BigDecimal(studentJsonObject.getString("mother_old_phone")));
            preparedStatement.executeUpdate();
        }
        if( !studentJsonObject.getString("mother_new_phone").equals("null") ){
            PreparedStatement preparedStatement = db.prepareStatement("INSERT INTO parent VALUES(?,?,?,?,?);");
            preparedStatement.setBigDecimal(1,new BigDecimal(studentJsonObject.getString("mother_new_phone")));
            preparedStatement.setString(2,studentJsonObject.getString("mother_firstname"));
            preparedStatement.setString(3,studentJsonObject.getString("mother_lastname"));
            if( studentJsonObject.getString("mother_email").equals("null") ){
                preparedStatement.setNull(4,VARCHAR);
            }else{
                preparedStatement.setString(4,studentJsonObject.getString("mother_email"));
            }
            preparedStatement.setString(5,"Female");
            preparedStatement.executeUpdate();

            preparedStatement = db.prepareStatement("INSERT INTO parent_child VALUES(?,?);");
            preparedStatement.setBigDecimal(1,new BigDecimal(studentJsonObject.getString("mother_new_phone")));
            preparedStatement.setInt(2,studentJsonObject.getInt("sid"));
            preparedStatement.executeUpdate();
        }
    }
}
