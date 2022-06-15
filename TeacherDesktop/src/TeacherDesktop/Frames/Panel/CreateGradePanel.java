package TeacherDesktop.Frames.Panel;

import TeacherDesktop.Server.ServerConnection;
import TeacherDesktop.Static.Constant;
import TeacherDesktop.Static.Constraint;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class CreateGradePanel extends JPanel implements KeyListener {

    private ServerConnection serverConnection;
    private JLabel standardLabel,divisionLabel, teacherInchargeLabel,messageLabel;
    private JTextField standardTextField,divisionTextField;
    private JComboBox teacherInchargeComboBox;
    private JButton createGradeButton;

    public CreateGradePanel(ServerConnection serverConnection){
        //Initialising Members
        this.serverConnection = serverConnection;
        standardLabel =  new JLabel("Standard : ");
        standardTextField = new JTextField(20);
        divisionLabel = new JLabel("Division : ");
        divisionTextField = new JTextField(20);
        teacherInchargeLabel = new JLabel("Teacher Incharge : ");
        teacherInchargeComboBox = new JComboBox();
        messageLabel = new JLabel();
        createGradeButton = new JButton("Create Grade");

        //Filling TeacherInchargeComboBox
        fillTeacherInchargeComboBox();

        //Editing Components
        createGradeButton.setBackground(Constant.BUTTON_BACKGROUND);

        //Adding Listeners
        standardTextField.addKeyListener(this);
        divisionTextField.addKeyListener(this);

        //panel details
        setLayout(new GridBagLayout());

        //Adding components to Panel
        add(standardLabel, Constraint.setPosition(0,0));
        add(standardTextField,Constraint.setPosition(1,0));
        add(divisionLabel,Constraint.setPosition(2,0));
        add(divisionTextField,Constraint.setPosition(3,0));
        add(teacherInchargeLabel,Constraint.setPosition(0,1,2,1));
        add(teacherInchargeComboBox,Constraint.setPosition(2,1,2,1));
        add(messageLabel,Constraint.setPosition(0,2,4,1));
        add(createGradeButton,Constraint.setPosition(0,3,4,1));
    }

    private void fillTeacherInchargeComboBox(){
        JSONArray jsonArray = serverConnection.getTeacherList();
        for( int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String firstname = jsonObject.getString("firstname");
            String lastname = jsonObject.getString("lastname");
            String phone = jsonObject.getString("phone");

            if(!phone.equals(Constant.PRINCIPAL_USERNAME+"")) {
                String x = Character.toUpperCase(firstname.charAt(0)) + firstname.substring(1) + " ";
                x += Character.toUpperCase(lastname.charAt(0)) + lastname.substring(1) + " ";
                x += "(" + jsonObject.getString("phone") + ")";

                teacherInchargeComboBox.insertItemAt(x, 0);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if( e.getSource() == standardTextField ){
            if( Character.isAlphabetic(e.getKeyChar()) || standardTextField.getText().length() > 0 ){
                e.consume();
            }
        }else if( e.getSource() == divisionTextField ){
            if( Character.isDigit(e.getKeyChar()) || divisionTextField.getText().length() > 0 ){
                e.consume();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
