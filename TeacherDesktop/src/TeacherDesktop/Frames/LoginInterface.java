package TeacherDesktop.Frames;

import TeacherDesktop.Static.Constant;
import TeacherDesktop.Static.Constraint;
import TeacherDesktop.Server.ServerConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginInterface extends JFrame implements ActionListener, ItemListener, KeyListener {
    private JLabel schoolImageLabel, schoolNameLabel,messageLabel;
    private JTextField usernameTextField;
    private JPasswordField passwordField;
    private JCheckBox showPasswordCheckBox;
    private JButton loginButton;

    private ServerConnection serverConnection;

    public LoginInterface(ServerConnection serverConnection){
        //Intialising Variables
        this.serverConnection = serverConnection;
        serverConnection.setCurrentFrame(this);
        Image img = new ImageIcon(Constant.SCHOOL_LOGO).getImage();
        img = img.getScaledInstance(150,150,Image.SCALE_DEFAULT);
        schoolImageLabel = new JLabel(new ImageIcon(img));
        schoolNameLabel = new JLabel(Constant.SCHOOL_NAME);
        usernameTextField = new JTextField("000000",20);
        passwordField =new JPasswordField("password",20);
        showPasswordCheckBox = new JCheckBox("Show Password");
        loginButton = new JButton("Login");
        messageLabel = new JLabel();

        //Editing Component
        schoolImageLabel.setPreferredSize(new Dimension(150,150));
        schoolNameLabel.setFont(new Font("SansSerif",Font.BOLD,30));
        usernameTextField.setHorizontalAlignment(SwingConstants.CENTER);
        passwordField.setHorizontalAlignment(SwingConstants.CENTER);
        passwordField.setEchoChar('*');

        //Adding Listeners
        usernameTextField.addKeyListener(this);
        loginButton.addActionListener(this);
        showPasswordCheckBox.addItemListener(this);

        //Frame Details
        setTitle(Constant.SCHOOL_NAME);
        setIconImage(Toolkit.getDefaultToolkit().getImage(Constant.SCHOOL_LOGO));
        setSize(new Dimension(700,500));
        setLayout(new GridBagLayout());
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Adding Components to Frame
        add(schoolImageLabel,Constraint.setPosition(0,0));
        add(schoolNameLabel,Constraint.setPosition(1,0));
        add(usernameTextField,Constraint.setPosition(0,1,2,1));
        add(passwordField,Constraint.setPosition(0,2,2,1));
        add(showPasswordCheckBox,Constraint.setPosition(0,3,2,1));
        add(loginButton,Constraint.setPosition(0,4,2,1));
        add(messageLabel,Constraint.setPosition(0,5,2,1));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if( usernameTextField.getText() == null ){
            messageLabel.setText("Enter Username");
            return;
        }
        int phone = Integer.parseInt(usernameTextField.getText());

        String password = new String(passwordField.getPassword());
        if( password == null ){
            messageLabel.setText("Enter Password");
            return;
        }

        String savedPassword = serverConnection.getPassword(phone);

        if( !Constraint.hashPassword(password).equals(savedPassword) ){
            messageLabel.setText("Invalid Credentials");
            return;
        }else{
            //New Interface
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if( showPasswordCheckBox.isSelected() ) {
            passwordField.setEchoChar((char)0);
        }else {
            passwordField.setEchoChar('*');
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if( !Character.isDigit(e.getKeyChar()) ){
            e.consume();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    protected void finalize() throws Throwable {
        serverConnection.closeConnection();
    }
}
