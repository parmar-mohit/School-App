package TeacherDesktop.Interfaces;

import TeacherDesktop.Panel.BrandingPanel;
import TeacherDesktop.Server.ServerConnection;
import TeacherDesktop.Static.Constant;
import TeacherDesktop.Static.Constraint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginInterface extends JFrame implements ActionListener, ItemListener, KeyListener {

    private final BrandingPanel brandingPanel;
    private final JLabel messageLabel;
    private final JTextField usernameTextField;
    private final JPasswordField passwordField;
    private final JCheckBox showPasswordCheckBox;
    private final JButton loginButton;

    private final ServerConnection serverConnection;

    public LoginInterface(ServerConnection serverConnection) {
        //Intialising Variables
        this.serverConnection = serverConnection;
        serverConnection.setCurrentFrame(this);
        brandingPanel = new BrandingPanel();
        usernameTextField = new JTextField("000000", 20);
        passwordField = new JPasswordField("password", 20);
        showPasswordCheckBox = new JCheckBox("Show Password");
        loginButton = new JButton("Login");
        messageLabel = new JLabel();

        //Editing Component
        usernameTextField.setHorizontalAlignment(SwingConstants.CENTER);
        passwordField.setHorizontalAlignment(SwingConstants.CENTER);
        passwordField.setEchoChar('*');
        showPasswordCheckBox.setBackground(Constant.FRAME_BACKGROUND);

        //Adding Listeners
        usernameTextField.addKeyListener(this);
        loginButton.addActionListener(this);
        showPasswordCheckBox.addItemListener(this);
        loginButton.setPreferredSize(Constant.BUTTON_SIZE);
        loginButton.setBackground(Constant.BUTTON_BACKGROUND);

        //Frame Details
        setTitle(Constant.SCHOOL_NAME);
        setIconImage(Toolkit.getDefaultToolkit().getImage(Constant.SCHOOL_LOGO));
        setSize(new Dimension(700, 500));
        setLayout(new GridBagLayout());
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Constant.FRAME_BACKGROUND);

        //Adding Components to Frame
        add(brandingPanel, Constraint.setPosition(0, 0));
        add(usernameTextField, Constraint.setPosition(0, 1));
        add(passwordField, Constraint.setPosition(0, 2));
        add(showPasswordCheckBox, Constraint.setPosition(0, 3));
        add(loginButton, Constraint.setPosition(0, 4));
        add(messageLabel, Constraint.setPosition(0, 5));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (usernameTextField.getText() == null) {
            messageLabel.setText("Enter Username");
            Constraint.labelDeleteAfterTime(messageLabel);
            return;
        }
        String phone = usernameTextField.getText();

        String password = new String(passwordField.getPassword());
        if (password == null) {
            messageLabel.setText("Enter Password");
            Constraint.labelDeleteAfterTime(messageLabel);
            return;
        }

        String savedPassword = serverConnection.getPassword(phone);

        if (!Constraint.hashPassword(password).equals(savedPassword)) {
            messageLabel.setText("Invalid Credentials");
            Constraint.labelDeleteAfterTime(messageLabel);
            return;
        } else if (phone.equals(Constant.PRINCIPAL_USERNAME)) {
            new PrincipalInterface(serverConnection);
            dispose();
        } else {
            new TeacherInterface(serverConnection, phone);
            dispose();
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
        if (!Character.isDigit(e.getKeyChar())) {
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
