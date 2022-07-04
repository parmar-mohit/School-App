package TeacherDesktop.Panel;

import TeacherDesktop.Server.ServerConnection;
import TeacherDesktop.Static.Constant;
import TeacherDesktop.Static.Constraint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ChangePasswordPanel extends JPanel implements ItemListener, ActionListener {

    private JLabel panelNameLabel,oldPasswordLabel,newPasswordLabel,confirmPasswordLabel,messageLabel;
    private JPasswordField oldPasswordField,newPasswordField,confirmPasswordField;
    private JCheckBox showPasswordCheckBox;
    private JButton changePasswordButton;
    private ServerConnection serverConnection;
    private String phone;

    public ChangePasswordPanel(ServerConnection serverConnection,String phone){
        //Initialising member Variables
        this.serverConnection = serverConnection;
        this.phone = phone;
        panelNameLabel = new JLabel("Change Password");
        oldPasswordLabel  = new JLabel("Old Password : ");
        oldPasswordField  = new JPasswordField(20);
        newPasswordLabel = new JLabel("New Password : ");
        newPasswordField = new JPasswordField(20);
        confirmPasswordLabel = new JLabel("Confirm Password : ");
        confirmPasswordField = new JPasswordField(20);
        showPasswordCheckBox = new JCheckBox("Show Password");
        messageLabel = new JLabel();
        changePasswordButton = new JButton("Change Password");

        //Editing Member
        panelNameLabel.setFont(new Font("SansSerif",Font.BOLD,22));
        oldPasswordField.setEchoChar('*');
        newPasswordField.setEchoChar('*');
        confirmPasswordField.setEchoChar('*');
        showPasswordCheckBox.setBackground(Constant.PANEL_BACKGROUND);
        changePasswordButton.setPreferredSize(Constant.BUTTON_SIZE);
        changePasswordButton.setBackground(Constant.BUTTON_BACKGROUND);

        //Adding Listeners
        showPasswordCheckBox.addItemListener(this);
        changePasswordButton.addActionListener(this);

        //Editing Panel Details
        setLayout(new GridBagLayout());
        setBackground(Constant.PANEL_BACKGROUND);

        //Adding Components to Panel
        add(panelNameLabel, Constraint.setPosition(0,0,2,1));
        add(oldPasswordLabel,Constraint.setPosition(0,1,Constraint.RIGHT));
        add(oldPasswordField,Constraint.setPosition(1,1,Constraint.LEFT));
        add(newPasswordLabel,Constraint.setPosition(0,2,Constraint.RIGHT));
        add(newPasswordField,Constraint.setPosition(1,2,Constraint.LEFT));
        add(confirmPasswordLabel,Constraint.setPosition(0,3,Constraint.RIGHT));
        add(confirmPasswordField,Constraint.setPosition(1,3,Constraint.LEFT));
        add(showPasswordCheckBox,Constraint.setPosition(1,4,Constraint.LEFT));
        add(messageLabel,Constraint.setPosition(0,5,2,1));
        add(changePasswordButton,Constraint.setPosition(0,6,2,1));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String oldPassword = new String(oldPasswordField.getPassword());
        if( oldPassword.equals("") ){
            messageLabel.setText("Enter Old Password");
            Constraint.labelDeleteAfterTime(messageLabel);
            return;
        }

        String newPassword = new String(newPasswordField.getPassword());
        if( newPassword.equals("")){
            messageLabel.setText("Enter New Password");
            Constraint.labelDeleteAfterTime(messageLabel);
            return;
        }

        String confirmPassword = new String(confirmPasswordField.getPassword());
        if( confirmPassword.equals("")){
            messageLabel.setText("Enter Confirm Password");
            Constraint.labelDeleteAfterTime(messageLabel);
            return;
        }

        if( !Constraint.isValidPassword(newPassword) ){
            messageLabel.setText("Password must contain 1 Uppercase letter, 1 Lowercase letter and 1 Number");
            Constraint.labelDeleteAfterTime(messageLabel);
            return;
        }

        if( !newPassword.equals(confirmPassword) ){
            messageLabel.setText("New Passwords Do not match");
            Constraint.labelDeleteAfterTime(messageLabel);
            return;
        }

        if( !Constraint.hashPassword(oldPassword).equals(serverConnection.getPassword(phone))){
            messageLabel.setText("Old Password is Incorrect");
            Constraint.labelDeleteAfterTime(messageLabel);
            return;
        }

        int response = serverConnection.changePassword(phone,Constraint.hashPassword(newPassword));
        if( response == 0 ){
            messageLabel.setText("Password Changed Successfully");
            Constraint.labelDeleteAfterTime(messageLabel);
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if( showPasswordCheckBox.isSelected() ){
            oldPasswordField.setEchoChar((char)0);
            newPasswordField.setEchoChar((char)0);
            confirmPasswordField.setEchoChar((char)0);
        }else{
            oldPasswordField.setEchoChar('*');
            newPasswordField.setEchoChar('*');
            confirmPasswordField.setEchoChar('*');
        }
    }
}
