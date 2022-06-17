package ServerProgram;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Vector;

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
        preparedStatement.setString(4,email);
        preparedStatement.setString(5,gender);
        preparedStatement.setString(6,password);
        preparedStatement.executeUpdate();
    }

    public ResultSet getTeacherList() throws Exception {
        PreparedStatement preparedStatement = db.prepareStatement("SELECT t_phone,firstname,lastname FROM teacher;");
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public Boolean checkPhone(String column,String table,String entry) throws Exception{
        PreparedStatement preparedStatement = db.prepareStatement("SELECT EXISTS (SELECT "+column+" FROM "+table+" WHERE "+column+"=?);");
        preparedStatement.setBigDecimal(1,new BigDecimal(entry));
        ResultSet result = preparedStatement.executeQuery();
        result.next();

        return result.getBoolean(1);
    }

    public boolean checkGrade(int standard,String division) throws Exception{
        PreparedStatement preparedStatement = db.prepareStatement("SELECT * FROM grade WHERE standard = ? AND DIVISION = ?;");
        preparedStatement.setInt(1,standard);
        preparedStatement.setString(2,division);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

    public void insertGrade(int standard, String division, String phone) throws Exception{
        PreparedStatement preparedStatement = db.prepareStatement("INSERT INTO grade VALUES(?,?,?);");
        preparedStatement.setInt(1,standard);
        preparedStatement.setString(2,division);
        preparedStatement.setBigDecimal(3,new BigDecimal(phone));
        preparedStatement.executeUpdate();
    }

    public void insertSubject(int standard,String division,String subjectName,String phone) throws Exception{
        //Inserting New Subject
        PreparedStatement preparedStatement = db.prepareStatement("INSERT INTO subject(subject_name,t_phone) VALUES(?,?);");
        preparedStatement.setString(1,subjectName);
        preparedStatement.setBigDecimal(2,new BigDecimal(phone));
        preparedStatement.executeUpdate();

        //Retrieving new Subject Id
        preparedStatement = db.prepareStatement("SELECT LAST_INSERT_ID();");
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        int id = resultSet.getInt(1);

        //Inserting Subject Grade
        preparedStatement = db.prepareStatement("INSERT INTO subject_grade VALUES(?,?,?);");
        preparedStatement.setInt(1,id);
        preparedStatement.setInt(2,standard);
        preparedStatement.setString(3,division);
        preparedStatement.executeUpdate();
    }
}
