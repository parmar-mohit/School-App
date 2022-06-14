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
    public String getPassword(int phone) throws Exception{
        PreparedStatement preparedStatement = db.prepareStatement("SELECT password FROM teacher WHERE t_phone=?;");
        preparedStatement.setInt(1,phone);
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
}
