package TeacherDesktop.Frames.Panel;

import TeacherDesktop.Static.Constant;
import TeacherDesktop.Static.Constraint;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class TeacherCardPanel extends JPanel {

    private JLabel imageLabel,nameLabel,phoneLabel,emailLabel,genderLabel;
    private JSONObject teacherJsonObject;

    public TeacherCardPanel(JSONObject teacherJsonObject){
        //Initialising Member Variables
        this.teacherJsonObject = teacherJsonObject;
        Image img;
        if( teacherJsonObject.getString("gender").equals("Male") ){
            img = new ImageIcon(Constant.MALE_AVATAR).getImage();
        }else{
            img = new ImageIcon(Constant.FEMALE_AVATAR).getImage();
        }
        img = img.getScaledInstance(100,100,Image.SCALE_DEFAULT);
        imageLabel = new JLabel(new ImageIcon(img));
        String firstname = teacherJsonObject.getString("firstname");
        String lastname = teacherJsonObject.getString("lastname");
        nameLabel = new JLabel("Name : "+Character.toUpperCase(firstname.charAt(0))+firstname.substring(1)+" "+Character.toUpperCase(lastname.charAt(0))+lastname.substring(1));
        phoneLabel = new JLabel("Phone : "+teacherJsonObject.getString("phone"));
        emailLabel = new JLabel("Email : "+teacherJsonObject.getString("email"));
        genderLabel = new JLabel("Gender : "+teacherJsonObject.getString("gender"));

        //Editing Panel Details
        setLayout(new GridBagLayout());
        setBackground(Constant.TEACHER_CARD_PANEL);

        //Adding Components to Panel
        add(imageLabel, Constraint.setPosition(0,0,1,4,Constraint.LEFT));
        add(nameLabel,Constraint.setPosition(1,0,Constraint.LEFT));
        add(phoneLabel,Constraint.setPosition(1,1,Constraint.LEFT));
        add(emailLabel,Constraint.setPosition(1,2,Constraint.LEFT));
        add(genderLabel,Constraint.setPosition(1,3,Constraint.LEFT));
    }
}
