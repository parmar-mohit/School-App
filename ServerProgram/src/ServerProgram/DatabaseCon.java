package ServerProgram;

import javax.xml.transform.Result;
import java.math.BigDecimal;
import java.sql.*;

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

    public boolean checkSubjectExist(int standard,String division,String subjectName) throws Exception {
        PreparedStatement preparedStatement = db.prepareStatement("SELECT EXISTS( SELECT * FROM subject WHERE subject_name = ? AND sub_id IN (SELECT sub_id FROM subject_classroom WHERE standard = ? AND division = ? ) );");
        preparedStatement.setString(1,subjectName);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getBoolean(1);
    }
}
