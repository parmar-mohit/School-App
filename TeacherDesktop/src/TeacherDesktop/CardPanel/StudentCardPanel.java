package TeacherDesktop.CardPanel;

import TeacherDesktop.Dialog.UpdateStudentDialog;
import TeacherDesktop.Panel.AllStudentPanel;
import TeacherDesktop.Panel.MyClassroomPanel;
import TeacherDesktop.Server.ServerConnection;
import TeacherDesktop.Static.Constant;
import TeacherDesktop.Static.Constraint;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class StudentCardPanel extends JPanel implements ActionListener, WindowListener {

    private JLabel imageLabel,sidLabel,firstnameLabel,lastnameLabel,emailLabel,phoneLabel,genderLabel,dobLabel,standardLabel,divisionLabel,fatherFirstnameLabel,fatherLastnameLabel,fatherPhoneLabel,fatherEmailLabel,motherFirstnameLabel,motherLastnameLabel,motherPhoneLabel,motherEmailLabel;
    private JButton updateButton,deleteButton;
    private JSONObject studentJsonObject;
    private ServerConnection serverConnection;
    private MyClassroomPanel myClassroomPanelParent;
    private AllStudentPanel allStudentPanelParent;
    private JSONArray classroomJSonArray;

    public StudentCardPanel(JSONObject studentJsonObject,ServerConnection serverConnection,JSONArray classroomJSonArray,MyClassroomPanel myClassroomPanelParent){
        this.studentJsonObject = studentJsonObject;
        this.serverConnection = serverConnection;
        this.classroomJSonArray = classroomJSonArray;
        fillPanel(true);
    }

    public StudentCardPanel(JSONObject studentJsonObject,ServerConnection serverConnection,JSONArray classroomJSonArray,AllStudentPanel allStudentPanelParent){
        this.studentJsonObject = studentJsonObject;
        this.serverConnection = serverConnection;
        this.classroomJSonArray = classroomJSonArray;
        this.allStudentPanelParent = allStudentPanelParent;
        fillPanel(true);
    }
    public StudentCardPanel(JSONObject studentJsonObject){
        //Intialising Members
        this.studentJsonObject = studentJsonObject;
        this.serverConnection = serverConnection;
        fillPanel(false);
    }

    private void fillPanel(boolean buttonVisibilty){
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        dobLabel = new JLabel("Date of Birth : "+sdf.format(new Date(studentJsonObject.getLong("dob"))));
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

        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");

        //Editing Components
        sidLabel.setFont(new Font("SansSerif",Font.BOLD,16));
        updateButton.setBackground(Constant.BUTTON_BACKGROUND);
        deleteButton.setBackground(Color.RED);

        //Editing Button Visibility
        updateButton.setVisible(buttonVisibilty);
        deleteButton.setVisible(buttonVisibilty);

        //Adding Listeners
        updateButton.addActionListener(this);
        deleteButton.addActionListener(this);

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
        add(updateButton,Constraint.setPosition(1,9));
        add(deleteButton,Constraint.setPosition(2,9));
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if( e.getSource() == updateButton ){
            JDialog dialog = new UpdateStudentDialog(serverConnection,classroomJSonArray,studentJsonObject,(JFrame)SwingUtilities.getWindowAncestor(this));
            dialog.addWindowListener(this);
        }else if( e.getSource() == deleteButton ){
            int result = JOptionPane.showConfirmDialog(this,"Are you sure you want to delete Student Id?");

            if( result == JOptionPane.YES_OPTION ){
                int response = serverConnection.deleteStudentId(studentJsonObject.getInt("sid"));
                if( response ==  0 ){
                    if( myClassroomPanelParent != null ) {
                        myClassroomPanelParent.fillStudentCard();
                        myClassroomPanelParent.revalidate();
                        myClassroomPanelParent.repaint();
                    }else{
                        allStudentPanelParent.fillStudentCard();
                        allStudentPanelParent.revalidate();
                        allStudentPanelParent.repaint();
                    }
                }
            }
        }
        revalidate();
        repaint();
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {
        if( myClassroomPanelParent != null ) {
            myClassroomPanelParent.fillStudentCard();
        }else{
            allStudentPanelParent.fillStudentCard();
        }
    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
