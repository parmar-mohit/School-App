package TeacherDesktop.Frames.Panel;

import TeacherDesktop.Server.ServerConnection;
import TeacherDesktop.Static.Constant;
import TeacherDesktop.Static.Constraint;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TeacherCardPanel extends JPanel implements ActionListener {

    private JLabel imageLabel,firstnameLabel,lastnameLabel,phoneLabel,emailLabel,genderLabel,messageLabel;
    private JTextField firstnameTextField,lastnameTextField,emailTextField;
    private JComboBox genderComboBox;
    private JButton editButton,saveButton;
    private JSONObject teacherJsonObject;
    private ServerConnection serverConnection;

    public TeacherCardPanel(JSONObject teacherJsonObject, ServerConnection serverConnection){
        //Initialising Member Variables
        this.teacherJsonObject = teacherJsonObject;
        this.serverConnection = serverConnection;

        //Panel for Viewing
        Image img;
        if( teacherJsonObject.getString("gender").equals("Male") ){
            img = new ImageIcon(Constant.MALE_AVATAR).getImage();
        }else{
            img = new ImageIcon(Constant.FEMALE_AVATAR).getImage();
        }
        img = img.getScaledInstance(100,100,Image.SCALE_DEFAULT);
        imageLabel = new JLabel(new ImageIcon(img));
        String firstname = teacherJsonObject.getString("firstname");
        firstnameLabel = new JLabel("Firstname : "+Character.toUpperCase(firstname.charAt(0))+firstname.substring(1));
        String lastname = teacherJsonObject.getString("lastname");
        lastnameLabel = new JLabel("Lastname : "+Character.toUpperCase(lastname.charAt(0))+lastname.substring(1));
        phoneLabel = new JLabel("Phone : "+teacherJsonObject.getString("phone"));
        emailLabel = new JLabel("Email : "+teacherJsonObject.getString("email"));
        genderLabel = new JLabel("Gender : "+teacherJsonObject.getString("gender"));
        editButton = new JButton("Edit");

        //Panel for Editing
        firstnameTextField = new JTextField(20);
        lastnameTextField = new JTextField(20);
        emailTextField = new JTextField(20);
        genderComboBox = new JComboBox(new String[]{"Male","Female","Other"});
        saveButton = new JButton("Save");
        messageLabel = new JLabel();

        //Setting Visibilty to False for editing panel
        firstnameTextField.setVisible(false);
        lastnameTextField.setVisible(false);
        emailTextField.setVisible(false);
        genderComboBox.setVisible(false);
        saveButton.setVisible(false);

        //Editing Components
        editButton.setBackground(Constant.BUTTON_BACKGROUND);
        saveButton.setBackground(Constant.BUTTON_BACKGROUND);

        //Adding listeners
        editButton.addActionListener(this);
        saveButton.addActionListener(this);

        //Editing Panel Details
        setLayout(new GridBagLayout());
        setBackground(Constant.CARD_PANEL);

        //Adding Components to ViewPanel
        add(imageLabel, Constraint.setPosition(0,0,1,5,Constraint.LEFT));
        add(firstnameLabel,Constraint.setPosition(1,0,Constraint.LEFT));
        add(lastnameLabel,Constraint.setPosition(1,1,Constraint.LEFT));
        add(phoneLabel,Constraint.setPosition(1,2,Constraint.LEFT));
        add(emailLabel,Constraint.setPosition(1,3,Constraint.LEFT));
        add(genderLabel,Constraint.setPosition(1,4,Constraint.LEFT));
        add(editButton,Constraint.setPosition(3,0,1,5,Constraint.RIGHT));

        //Adding Components to EditingPanel
        add(firstnameTextField,Constraint.setPosition(2,0));
        add(lastnameTextField,Constraint.setPosition(2,1));
        add(emailTextField,Constraint.setPosition(2,3));
        add(genderComboBox,Constraint.setPosition(2,4));
        add(saveButton,Constraint.setPosition(3,0,1,5,Constraint.RIGHT));
        add(messageLabel,Constraint.setPosition(0,5,3,1));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if( e.getSource() == editButton ){
            //Setting Labels
            firstnameLabel.setText("Firstname : ");
            lastnameLabel.setText("Lastname : ");
            emailLabel.setText("Email : ");
            genderLabel.setText("Gender : ");

            //Setting text into TextFields
            firstnameTextField.setText(teacherJsonObject.getString("firstname"));
            lastnameTextField.setText(teacherJsonObject.getString("lastname"));
            emailTextField.setText(teacherJsonObject.getString("email"));
            genderComboBox.setSelectedItem(teacherJsonObject.getString("gender"));

            //Making TextFields Visible
            firstnameTextField.setVisible(true);
            lastnameTextField.setVisible(true);
            emailTextField.setVisible(true);
            genderComboBox.setVisible(true);
            saveButton.setVisible(true);

            //Making editButton Invisible
            editButton.setVisible(false);
        }else if ( e.getSource() == saveButton ){
            //Updating Changes
            JSONObject updateTeacherJsonObject = new JSONObject();
            String firstname = firstnameTextField.getText();
            if( firstname.equals("") ){
                messageLabel.setText("Enter Firstname");
                return;
            }
            updateTeacherJsonObject.put("firstname",firstname);

            String lastname = lastnameTextField.getText();
            if( lastname.equals("") ){
                messageLabel.setText("Enter Lastname");
                return;
            }
            updateTeacherJsonObject.put("lastname",lastname);

            updateTeacherJsonObject.put("phone",teacherJsonObject.getString("phone"));

            String email = emailTextField.getText();
            if( !Constraint.emailCheck(email) ){
                messageLabel.setText("Enter a Valid Email");
                return;
            }
            updateTeacherJsonObject.put("email",email);

            String gender = (String)genderComboBox.getSelectedItem();
            updateTeacherJsonObject.put("gender",gender);

            int response = serverConnection.updateTeacherAttributes(updateTeacherJsonObject);
            if( response == 0 ) {
                teacherJsonObject = updateTeacherJsonObject;
                //Setting Image and Labels
                Image img;
                if (teacherJsonObject.getString("gender").equals("Male")) {
                    img = new ImageIcon(Constant.MALE_AVATAR).getImage();
                } else {
                    img = new ImageIcon(Constant.FEMALE_AVATAR).getImage();
                }
                img = img.getScaledInstance(100, 100, Image.SCALE_DEFAULT);
                imageLabel.setIcon(new ImageIcon(img));

                firstname = teacherJsonObject.getString("firstname");
                firstnameLabel.setText("Firstname : " + Character.toUpperCase(firstname.charAt(0)) + firstname.substring(1));

                lastname = teacherJsonObject.getString("lastname");
                lastnameLabel.setText("Lastname : " + Character.toUpperCase(lastname.charAt(0)) + lastname.substring(1));
                phoneLabel.setText("Phone : " + teacherJsonObject.getString("phone"));
                emailLabel.setText("Email : " + teacherJsonObject.getString("email"));
                genderLabel.setText("Gender : " + teacherJsonObject.getString("gender"));

                //Setting TextField Invisible
                firstnameTextField.setVisible(false);
                lastnameTextField.setVisible(false);
                emailTextField.setVisible(false);
                genderComboBox.setVisible(false);
                saveButton.setVisible(false);

                //Setting editButton Visible
                editButton.setVisible(true);
            }else if( response == 1 ){
                messageLabel.setText("Teacher with same phone already exist");
            }
        }
    }
}
