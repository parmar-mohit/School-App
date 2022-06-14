package TeacherDesktop.Frames;

import TeacherDesktop.Frames.Panel.CreateTeacherPanel;
import TeacherDesktop.Server.ServerConnection;
import TeacherDesktop.Static.Constant;
import TeacherDesktop.Static.Constraint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Principalnterface extends JFrame implements ActionListener {
    private JLabel schoolImageLabel,schoolNameLabel;

    private JButton createTeacherIdButton,createGradeButton,changePasswordButton,logoutButton;
    private JPanel optionPanel;

    private ServerConnection serverConnection;

    public Principalnterface(ServerConnection serverConnection){
        //Initialisng Member Variables
        this.serverConnection = serverConnection;
        serverConnection.setCurrentFrame(this);
        Image img = new ImageIcon(Constant.SCHOOL_LOGO).getImage();
        img = img.getScaledInstance(150,150,Image.SCALE_DEFAULT);
        schoolImageLabel = new JLabel(new ImageIcon(img));
        schoolNameLabel = new JLabel(Constant.SCHOOL_NAME);
        createTeacherIdButton = new JButton("Create Teacher Id");
        createGradeButton = new JButton("Create Grade");
        changePasswordButton = new JButton("Change Password");
        logoutButton = new JButton("Logout");

        //Editing Components
        createTeacherIdButton.setPreferredSize(Constant.BUTTON_SIZE);
        createTeacherIdButton.setBackground(Constant.BUTTON_BACKGROUND);
        createGradeButton.setPreferredSize(Constant.BUTTON_SIZE);
        createGradeButton.setBackground(Constant.BUTTON_BACKGROUND);
        changePasswordButton.setPreferredSize(Constant.BUTTON_SIZE);
        changePasswordButton.setBackground(Constant.BUTTON_BACKGROUND);
        logoutButton.setPreferredSize(Constant.BUTTON_SIZE);
        logoutButton.setBackground(Constant.BUTTON_BACKGROUND);

        //Editing Component
        schoolImageLabel.setPreferredSize(new Dimension(150,150));
        schoolNameLabel.setFont(new Font("SansSerif",Font.BOLD,30));

        //Adding Listener
        createTeacherIdButton.addActionListener(this);
        createGradeButton.addActionListener(this);
        changePasswordButton.addActionListener(this);
        logoutButton.addActionListener(this);

        //Frame Details
        setTitle(Constant.SCHOOL_NAME);
        setIconImage(Toolkit.getDefaultToolkit().getImage(Constant.SCHOOL_LOGO));
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setLayout(new GridBagLayout());
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Constant.FRAME_BACKGROUND);

        //Adding Components to Frame
        add(schoolImageLabel,Constraint.setPosition(0,0));
        add(schoolNameLabel,Constraint.setPosition(1,0));
        add(createTeacherIdButton, Constraint.setPosition(0,1));
        add(createGradeButton,Constraint.setPosition(0,2));
        add(changePasswordButton,Constraint.setPosition(0,3));
        add(logoutButton,Constraint.setPosition(0,4));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if( optionPanel != null ){
            remove(optionPanel);
        }

        if( e.getSource() == createTeacherIdButton){
            optionPanel = new CreateTeacherPanel(serverConnection);
        }

        add(optionPanel,Constraint.setPosition(1,1,1,4));
        optionPanel.setVisible(true);
        revalidate();
        repaint();
    }
}
