package TeacherDesktop.Frames.Panel;

import TeacherDesktop.Static.Constant;
import TeacherDesktop.Static.Constraint;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.util.Base64;

public class StudentCardPanel extends JPanel {

    private JLabel imageLabel,sidLabel,firstnameLabel,lastnameLabel,emailLabel,phoneLabel,genderLabel,dobLabel,standardLabel,divisionLabel,fatherFirstnameLabel,fatherLastnameLabel,fatherPhoneLabel,fatherEmailLabel,motherFirstnameLabel,motherLastnameLabel,motherPhoneLabel,motherEmailLabel;
    private JSONObject studentJsonObject;

    public StudentCardPanel(JSONObject studentJsonObject){
        //Intialising Members
        this.studentJsonObject = studentJsonObject;
        imageLabel = new JLabel(getImage());
        sidLabel = new JLabel("SID : "+studentJsonObject.getInt("sid"));
        String firstname = studentJsonObject.getString("firstname");
        firstnameLabel = new JLabel("Firstname : "+Character.toUpperCase(firstname.charAt(0))+firstname.substring(1));
        String lastname = studentJsonObject.getString("lastname");
        lastnameLabel = new JLabel("Lastname : "+Character.toUpperCase(lastname.charAt(0))+lastname.substring(1));
        if( studentJsonObject.getString("email").equals("null") ){
            emailLabel = new JLabel("Email : N/A");
        }else{
            emailLabel = new JLabel("Email : "+studentJsonObject.getString("email"));
        }
        if( studentJsonObject.getString("phone").equals("null") ){
            phoneLabel = new JLabel("Phone : N/A");
        }else{
            phoneLabel = new JLabel("Phone : "+studentJsonObject.getString("phone"));
        }
        genderLabel = new JLabel("Gender : "+studentJsonObject.getString("gender"));
        dobLabel = new JLabel("Date of Birth : "+studentJsonObject.getString("dob"));
        standardLabel = new JLabel("Standard : "+studentJsonObject.getInt("standard"));
        divisionLabel = new JLabel("Division : "+studentJsonObject.getString("division"));
        if( studentJsonObject.getString("father_firstname").equals("null")){
            fatherFirstnameLabel = new JLabel("Father's Firstname : N/A");
            fatherLastnameLabel = new JLabel("Father's Lastname : N/A");
            fatherPhoneLabel = new JLabel("Father's Phone : N/A");
        }else{
            String fatherFirstname = studentJsonObject.getString("father_firstname");
            fatherFirstnameLabel = new JLabel("Father's Firstname : "+Character.toUpperCase(fatherFirstname.charAt(0))+fatherFirstname.substring(1));
            String fatherLastname = studentJsonObject.getString("father_lastname");
            fatherLastnameLabel = new JLabel("Father's Lastname : "+Character.toUpperCase(fatherLastname.charAt(0))+fatherLastname.substring(1));
            fatherPhoneLabel = new JLabel("Father's Phone : "+studentJsonObject.getString("father_phone"));
        }

        if( studentJsonObject.getString("father_email").equals("null") ){
            fatherEmailLabel = new JLabel("Father's Email : N/A");
        }else{
            fatherEmailLabel = new JLabel("Father's Email : "+studentJsonObject.getString("father_email"));
        }

        if( studentJsonObject.getString("mother_firstname").equals("null")){
            motherFirstnameLabel = new JLabel("Mother's Firstname : N/A");
            motherLastnameLabel = new JLabel("Mother's Lastname : N/A");
            motherPhoneLabel = new JLabel("Mother's Phone : N/A");
        }else{
            String motherFirstname = studentJsonObject.getString("mother_firstname");
            motherFirstnameLabel = new JLabel("Mother's Firstname : "+Character.toUpperCase(motherFirstname.charAt(0))+motherFirstname.substring(1));
            String motherLastname = studentJsonObject.getString("mother_lastname");
            motherLastnameLabel = new JLabel("Mother's Lastname : "+Character.toUpperCase(motherLastname.charAt(0))+motherLastname.substring(1));
            motherPhoneLabel = new JLabel("Mother's Phone : "+studentJsonObject.getString("mother_phone"));
        }

        if( studentJsonObject.getString("mother_email").equals("null") ){
            motherEmailLabel = new JLabel("Mother's Email : N/A");
        }else{
            motherEmailLabel = new JLabel("Mother's Email : "+studentJsonObject.getString("mother_email"));
        }

        //Editing Components
        sidLabel.setFont(new Font("SansSerif",Font.BOLD,16));

        //Editing Panel Details
        setLayout(new GridBagLayout());
        setBackground(Constant.CARD_PANEL);

        //Adding Components to Panel
        add(sidLabel, Constraint.setPosition(1,0,2,1));
        add(imageLabel,Constraint.setPosition(0,0,1,9));
        add(firstnameLabel,Constraint.setPosition(1,1,Constraint.LEFT));
        add(lastnameLabel,Constraint.setPosition(2,1,Constraint.LEFT));
        add(emailLabel,Constraint.setPosition(1,2,Constraint.LEFT));
        add(phoneLabel,Constraint.setPosition(2,2,Constraint.LEFT));
        add(genderLabel,Constraint.setPosition(1,3,Constraint.LEFT));
        add(dobLabel,Constraint.setPosition(2,3,Constraint.LEFT));
        add(standardLabel,Constraint.setPosition(1,4,Constraint.LEFT));
        add(divisionLabel,Constraint.setPosition(2,4,Constraint.LEFT));
        add(fatherFirstnameLabel,Constraint.setPosition(1,5,Constraint.LEFT));
        add(fatherLastnameLabel,Constraint.setPosition(2,5,Constraint.LEFT));
        add(fatherPhoneLabel,Constraint.setPosition(1,6,Constraint.LEFT));
        add(fatherEmailLabel,Constraint.setPosition(2,6,Constraint.LEFT));
        add(motherFirstnameLabel,Constraint.setPosition(1,7,Constraint.LEFT));
        add(motherLastnameLabel,Constraint.setPosition(2,7,Constraint.LEFT));
        add(motherPhoneLabel,Constraint.setPosition(1,8,Constraint.LEFT));
        add(motherEmailLabel,Constraint.setPosition(2,8,Constraint.LEFT));
    }

    private ImageIcon getImage(){
        byte[] imgArray = Base64.getDecoder().decode(studentJsonObject.getString("img"));
        ByteArrayInputStream bais = new ByteArrayInputStream(imgArray);
        Image studentImg = new ImageIcon(Constant.MALE_AVATAR).getImage();
        try {
             studentImg = ImageIO.read(bais);
        }catch(Exception e){
            e.printStackTrace();
        }
        studentImg = studentImg.getScaledInstance(150,150,Image.SCALE_DEFAULT);
        return new ImageIcon(studentImg);
    }
}
