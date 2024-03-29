package TeacherDesktop.Panel;

import TeacherDesktop.Server.ServerConnection;
import TeacherDesktop.Static.Constant;
import TeacherDesktop.Static.Constraint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CreateTeacherPanel extends JPanel implements KeyListener, ItemListener, ActionListener {

    private final JLabel panelNameLabel;
    private final JLabel firstnameLabel;
    private final JLabel lastnameLabel;
    private final JLabel phoneLabel;
    private final JLabel emailLabel;
    private final JLabel passwordLabel;
    private final JLabel genderLabel;
    private final JLabel messageLabel;
    private final JTextField firstnameTextField;
    private final JTextField lastnameTextField;
    private final JTextField phoneTextField;
    private final JTextField emailTextField;
    private final JPasswordField passwordField;
    private final JCheckBox showPasswordCheckBox;
    private final JComboBox genderComboBox;
    private final JButton addTeacherButton;
    private final ServerConnection serverConnection;

    public CreateTeacherPanel(ServerConnection serverConnection) {
        //Initialising Members
        this.serverConnection = serverConnection;
        panelNameLabel = new JLabel("Create Teacher Id");
        firstnameLabel = new JLabel("Firstname : ");
        firstnameTextField = new JTextField(20);
        lastnameLabel = new JLabel("Lastname : ");
        lastnameTextField = new JTextField(20);
        phoneLabel = new JLabel("Phone : ");
        phoneTextField = new JTextField(20);
        emailLabel = new JLabel("Email : ");
        emailTextField = new JTextField(20);
        passwordLabel = new JLabel("Password : ");
        passwordField = new JPasswordField(20);
        showPasswordCheckBox = new JCheckBox("Show Password");
        genderLabel = new JLabel("Gender : ");
        genderComboBox = new JComboBox(new String[]{"Male", "Female", "Other"});
        messageLabel = new JLabel();
        addTeacherButton = new JButton("Add Teacher");

        //Editing Components
        panelNameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        passwordField.setEchoChar('*');
        showPasswordCheckBox.setBackground(Constant.PANEL_BACKGROUND);
        addTeacherButton.setBackground(Constant.BUTTON_BACKGROUND);

        //Adding Listener
        phoneTextField.addKeyListener(this);
        showPasswordCheckBox.addItemListener(this);
        addTeacherButton.addActionListener(this);

        //Panel Details
        setLayout(new GridBagLayout());
        setBackground(Constant.PANEL_BACKGROUND);

        //Adding components to Panel
        add(panelNameLabel, Constraint.setPosition(0, 0, 4, 1));
        add(firstnameLabel, Constraint.setPosition(0, 1));
        add(firstnameTextField, Constraint.setPosition(1, 1));
        add(lastnameLabel, Constraint.setPosition(2, 1));
        add(lastnameTextField, Constraint.setPosition(3, 1));
        add(phoneLabel, Constraint.setPosition(0, 2));
        add(phoneTextField, Constraint.setPosition(1, 2));
        add(emailLabel, Constraint.setPosition(2, 2));
        add(emailTextField, Constraint.setPosition(3, 2));
        add(passwordLabel, Constraint.setPosition(0, 3));
        add(passwordField, Constraint.setPosition(1, 3));
        add(showPasswordCheckBox, Constraint.setPosition(2, 3));
        add(genderLabel, Constraint.setPosition(0, 4));
        add(genderComboBox, Constraint.setPosition(1, 4));
        add(messageLabel, Constraint.setPosition(1, 5, 4, 1));
        add(addTeacherButton, Constraint.setPosition(0, 6, 4, 1));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String firstname = firstnameTextField.getText();
        if (firstname.equals("")) {
            messageLabel.setText("Enter Firstname");
            Constraint.labelDeleteAfterTime(messageLabel);
            return;
        }

        String lastname = lastnameTextField.getText();
        if (lastname.equals("")) {
            messageLabel.setText("Enter Lastname");
            Constraint.labelDeleteAfterTime(messageLabel);
            return;
        }

        if (phoneTextField.getText().length() < 10) {
            messageLabel.setText("Phone No should be of 10 Digit");
            Constraint.labelDeleteAfterTime(messageLabel);
            return;
        }
        String phone = phoneTextField.getText();

        String email = emailTextField.getText();
        if (!Constraint.emailCheck(email)) {
            messageLabel.setText("Enter a Valid Email");
            Constraint.labelDeleteAfterTime(messageLabel);
            return;
        }

        String password = new String(passwordField.getPassword());
        if (password.equals("")) {
            messageLabel.setText("Enter Password");
            Constraint.labelDeleteAfterTime(messageLabel);
            return;
        }

        if (!Constraint.isValidPassword(password)) {
            messageLabel.setText("Password Must contain 1 Uppercase, 1 Lowercase and 1 Digit");
            Constraint.labelDeleteAfterTime(messageLabel);
            return;
        }

        password = Constraint.hashPassword(password);

        String gender = (String) genderComboBox.getSelectedItem();

        int response = serverConnection.addTeacherId(firstname, lastname, phone, email, password, gender);
        if (response == 0) {
            messageLabel.setText("Teacher Id Created");
            Constraint.labelDeleteAfterTime(messageLabel);

            //Clearing TextFields
            firstnameTextField.setText(null);
            lastnameTextField.setText(null);
            phoneTextField.setText(null);
            emailTextField.setText(null);
            passwordField.setText(null);
            showPasswordCheckBox.setSelected(false);
            genderComboBox.setSelectedItem(0);

            revalidate();
            repaint();
        } else if (response == 1) {
            messageLabel.setText("Teacher with same phone already exist in Database");
            Constraint.labelDeleteAfterTime(messageLabel);
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (showPasswordCheckBox.isSelected()) {
            passwordField.setEchoChar((char) 0);
        } else {
            passwordField.setEchoChar('*');
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (!Character.isDigit(e.getKeyChar()) || phoneTextField.getText().length() > 9) {
            e.consume();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}