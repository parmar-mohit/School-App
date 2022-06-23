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
    private JButton editButton,saveButton,deleteButton;
    private JSONObject teacherJsonObject;
    private ServerConnection serverConnection;
    private TeacherPanel parent;

    public TeacherCardPanel(JSONObject teacherJsonObject, ServerConnection serverConnection,TeacherPanel parent){
        //Initialising Member Variables
        this.teacherJsonObject = teacherJsonObject;
        this.serverConnection = serverConnection;
        this.parent = parent;

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

        //Editing Components
        editButton.setBackground(Constant.BUTTON_BACKGROUND);

        //Adding listeners
        editButton.addActionListener(this);

        //Editing Panel Details
        setLayout(new GridBagLayout());
        setBackground(Constant.CARD_PANEL);

        //Adding Components to Panel
        add(imageLabel, Constraint.setPosition(0,0,1,5,Constraint.LEFT));
        add(firstnameLabel,Constraint.setPosition(1,0,Constraint.LEFT));
        add(lastnameLabel,Constraint.setPosition(1,1,Constraint.LEFT));
        add(phoneLabel,Constraint.setPosition(1,2,Constraint.LEFT));
        add(emailLabel,Constraint.setPosition(1,3,Constraint.LEFT));
        add(genderLabel,Constraint.setPosition(1,4,Constraint.LEFT));
        add(editButton,Constraint.setPosition(3,0,1,5,Constraint.RIGHT));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if( e.getSource() == editButton ){
            //Making editButton Invisible
            editButton.setVisible(false);

            //Setting Labels
            firstnameLabel.setText("Firstname : ");
            lastnameLabel.setText("Lastname : ");
            emailLabel.setText("Email : ");
            genderLabel.setText("Gender : ");

            //Creating Components
            firstnameTextField = new JTextField(20);
            lastnameTextField = new JTextField(20);
            emailTextField = new JTextField(20);
            genderComboBox = new JComboBox(new String[]{"Male","Female","Other"});
            saveButton = new JButton("Save");
            deleteButton = new JButton("Delete");
            messageLabel = new JLabel();

            //Editing Components
            saveButton.setBackground(Constant.BUTTON_BACKGROUND);
            deleteButton.setBackground(Color.RED);

            //Adding Listeners
            saveButton.addActionListener(this);
            deleteButton.addActionListener(this);

            //Setting text into TextFields
            firstnameTextField.setText(teacherJsonObject.getString("firstname"));
            lastnameTextField.setText(teacherJsonObject.getString("lastname"));
            emailTextField.setText(teacherJsonObject.getString("email"));
            genderComboBox.setSelectedItem(teacherJsonObject.getString("gender"));

            //Adding Components to Panel
            add(firstnameTextField,Constraint.setPosition(2,0));
            add(lastnameTextField,Constraint.setPosition(2,1));
            add(emailTextField,Constraint.setPosition(2,3));
            add(genderComboBox,Constraint.setPosition(2,4));
            add(saveButton,Constraint.setPosition(3,0,Constraint.RIGHT));
            add(deleteButton,Constraint.setPosition(3,1,Constraint.RIGHT));
            add(messageLabel,Constraint.setPosition(0,5,3,1));
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

                //Removing Components From Panel
                remove(firstnameTextField);
                remove(lastnameTextField);
                remove(emailTextField);
                remove(genderComboBox);
                remove(saveButton);
                remove(deleteButton);

                //Setting editButton Visible
                editButton.setVisible(true);
            }else if( response == 1 ){
                messageLabel.setText("There was some problem updating.Please try again later");
            }
        }else if( e.getSource() == deleteButton ){
            int result = JOptionPane.showConfirmDialog(this,"Are you sure you want to delete Teacher id?");

            if( result == JOptionPane.YES_OPTION ){
                int response = serverConnection.deleteTeacher(teacherJsonObject.getString("phone"));
                if( response ==  1 ){
                    messageLabel.setText("Teacher is Incharge of some Class or Subject and hence Id cannot be Deleted");
                    return;
                }else {
                    parent.fillTeacherCard();
                    parent.revalidate();
                    parent.repaint();
                }
            }
        }
        revalidate();
        repaint();
    }
}
