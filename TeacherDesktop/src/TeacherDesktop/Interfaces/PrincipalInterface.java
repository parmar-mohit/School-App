package TeacherDesktop.Interfaces;

import TeacherDesktop.Panel.*;
import TeacherDesktop.Server.ServerConnection;
import TeacherDesktop.Static.Constant;
import TeacherDesktop.Static.Constraint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PrincipalInterface extends JFrame implements ActionListener {

    private BrandingPanel brandingPanel;
    private PrincipalButtonPanel buttonPanel;
    private JPanel optionPanel;

    private ServerConnection serverConnection;

    public PrincipalInterface(ServerConnection serverConnection){
        //Initialisng Member Variables
        this.serverConnection = serverConnection;
        serverConnection.setCurrentFrame(this);
        brandingPanel = new BrandingPanel();
        buttonPanel = new PrincipalButtonPanel();

        //Adding Listener
        buttonPanel.teacherButton.addActionListener(this);
        buttonPanel.allStudentButton.addActionListener(this);
        buttonPanel.classroomButton.addActionListener(this);
        buttonPanel.securityButton.addActionListener(this);
        buttonPanel.logoutButton.addActionListener(this);

        //Setting Size
        brandingPanel.setMinimumSize(new Dimension(Constant.SCREEN_SIZE.width,Constant.SCREEN_SIZE.height/5));
        brandingPanel.setPreferredSize(new Dimension(Constant.SCREEN_SIZE.width,Constant.SCREEN_SIZE.height/5));
        buttonPanel.setMinimumSize(new Dimension(Constant.SCREEN_SIZE.width/5,Constant.SCREEN_SIZE.height*4/5));
        buttonPanel.setPreferredSize(new Dimension(Constant.SCREEN_SIZE.width/5,Constant.SCREEN_SIZE.height*4/5));

        //Frame Details
        setTitle(Constant.SCHOOL_NAME);
        setIconImage(Toolkit.getDefaultToolkit().getImage(Constant.SCHOOL_LOGO));
        setSize(Constant.SCREEN_SIZE);
        setLayout(new GridBagLayout());
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Constant.FRAME_BACKGROUND);
        setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);

        //Adding Components to Frame
        add(brandingPanel,Constraint.setPosition(0,0,2,1));
        add(buttonPanel,Constraint.setPosition(0,1));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if( optionPanel != null ){
            remove(optionPanel);
        }

        if( e.getSource() == buttonPanel.teacherButton){
            optionPanel = new TeacherPanel(serverConnection);
        }else if( e.getSource() == buttonPanel.allStudentButton ){
            optionPanel = new AllStudentPanel(serverConnection);
        }else if( e.getSource() == buttonPanel.classroomButton){
            optionPanel = new ClassroomPanel(serverConnection);
        }else if( e.getSource() == buttonPanel.securityButton ){
            optionPanel = new ChangePasswordPanel(serverConnection,Constant.PRINCIPAL_USERNAME);
        }else if( e.getSource() ==  buttonPanel.logoutButton ){
            dispose();
            new LoginInterface(serverConnection);
            return;
        }

        //Coloring Buttons
        buttonPanel.teacherButton.setBackground(Constant.BUTTON_BACKGROUND);
        buttonPanel.allStudentButton.setBackground(Constant.BUTTON_BACKGROUND);
        buttonPanel.classroomButton.setBackground(Constant.BUTTON_BACKGROUND);
        buttonPanel.securityButton.setBackground(Constant.BUTTON_BACKGROUND);

        JButton buttonClicked = (JButton)e.getSource();
        buttonClicked.setBackground(Constant.SELECTED_BUTTON);

        optionPanel.setMinimumSize(new Dimension(Constant.SCREEN_SIZE.width*4/5,Constant.SCREEN_SIZE.height*4/5));
        optionPanel.setPreferredSize(new Dimension(Constant.SCREEN_SIZE.width*4/5,Constant.SCREEN_SIZE.height*4/5));
        add(optionPanel,Constraint.setPosition(1,1));
        optionPanel.setVisible(true);
        revalidate();
        repaint();
    }
}

class PrincipalButtonPanel extends JPanel {

    protected JButton teacherButton, allStudentButton, classroomButton,securityButton,logoutButton;

    public PrincipalButtonPanel(){
        //Initialising Member Variables
        teacherButton = new JButton("Teacher");
        allStudentButton = new JButton("All Students");
        classroomButton = new JButton("Classroom");
        securityButton = new JButton("Security");
        logoutButton = new JButton("Logout");

        //Editing Components
        teacherButton.setPreferredSize(Constant.BUTTON_SIZE);
        teacherButton.setBackground(Constant.BUTTON_BACKGROUND);
        allStudentButton.setPreferredSize(Constant.BUTTON_SIZE);
        allStudentButton.setBackground(Constant.BUTTON_BACKGROUND);
        classroomButton.setPreferredSize(Constant.BUTTON_SIZE);
        classroomButton.setBackground(Constant.BUTTON_BACKGROUND);
        securityButton.setPreferredSize(Constant.BUTTON_SIZE);
        securityButton.setBackground(Constant.BUTTON_BACKGROUND);
        logoutButton.setPreferredSize(Constant.BUTTON_SIZE);
        logoutButton.setBackground(Constant.BUTTON_BACKGROUND);

        //Panel Details
        setLayout(new GridBagLayout());
        setBackground(Constant.BUTTON_PANEL_BACKGROUND);

        //Adding Components to Frame
        add(teacherButton,Constraint.setPosition(0,0));
        add(allStudentButton,Constraint.setPosition(0,1));
        add(classroomButton,Constraint.setPosition(0,2));
        add(securityButton,Constraint.setPosition(0,3));
        add(logoutButton,Constraint.setPosition(0,4));
    }
}