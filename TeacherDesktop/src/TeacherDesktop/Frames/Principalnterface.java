package TeacherDesktop.Frames;

import TeacherDesktop.Frames.Panel.*;
import TeacherDesktop.Server.ServerConnection;
import TeacherDesktop.Static.Constant;
import TeacherDesktop.Static.Constraint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Principalnterface extends JFrame implements ActionListener {

    private BrandingPanel brandingPanel;
    private ButtonPanel buttonPanel;
    private JPanel optionPanel;

    private ServerConnection serverConnection;

    public Principalnterface(ServerConnection serverConnection){
        //Initialisng Member Variables
        this.serverConnection = serverConnection;
        serverConnection.setCurrentFrame(this);
        brandingPanel = new BrandingPanel();
        buttonPanel = new ButtonPanel();



        //Adding Listener
        buttonPanel.teacherButton.addActionListener(this);
        buttonPanel.createClassroomButton.addActionListener(this);
        buttonPanel.changePasswordButton.addActionListener(this);
        buttonPanel.logoutButton.addActionListener(this);

        //Setting Size
        brandingPanel.setMinimumSize(new Dimension(Constant.screenSize.width,Constant.screenSize.height/5));
        brandingPanel.setPreferredSize(new Dimension(Constant.screenSize.width,Constant.screenSize.height/5));
        buttonPanel.setMinimumSize(new Dimension(Constant.screenSize.width/5,Constant.screenSize.height*4/5));
        buttonPanel.setPreferredSize(new Dimension(Constant.screenSize.width/5,Constant.screenSize.height*4/5));

        //Frame Details
        setTitle(Constant.SCHOOL_NAME);
        setIconImage(Toolkit.getDefaultToolkit().getImage(Constant.SCHOOL_LOGO));
        setSize(Constant.screenSize);
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
        }else if( e.getSource() == buttonPanel.createClassroomButton){
            optionPanel = new CreateClassroomPanel(serverConnection);
        }else if( e.getSource() == buttonPanel.changePasswordButton ){
            optionPanel = new ChangePasswordPanel(serverConnection);
        }else if( e.getSource() ==  buttonPanel.logoutButton ){
            dispose();
            new LoginInterface(serverConnection);
            return;
        }

        //Coloring Buttons
        buttonPanel.teacherButton.setBackground(Constant.BUTTON_BACKGROUND);
        buttonPanel.createClassroomButton.setBackground(Constant.BUTTON_BACKGROUND);
        buttonPanel.changePasswordButton.setBackground(Constant.BUTTON_BACKGROUND);

        JButton buttonClicked = (JButton)e.getSource();
        buttonClicked.setBackground(Constant.SELECTED_BUTTON);

        optionPanel.setMinimumSize(new Dimension(Constant.screenSize.width*4/5,Constant.screenSize.height*4/5));
        optionPanel.setPreferredSize(new Dimension(Constant.screenSize.width*4/5,Constant.screenSize.height*4/5));
        add(optionPanel,Constraint.setPosition(1,1));
        optionPanel.setVisible(true);
        revalidate();
        repaint();
    }
}

class ButtonPanel extends JPanel {

    protected JButton teacherButton, createClassroomButton,changePasswordButton,logoutButton;

    public ButtonPanel(){
        //Initialising Member Variables
        teacherButton = new JButton("Teacher");
        createClassroomButton = new JButton("Create Classroom");
        changePasswordButton = new JButton("Change Password");
        logoutButton = new JButton("Logout");

        //Editing Components
        teacherButton.setPreferredSize(Constant.BUTTON_SIZE);
        teacherButton.setBackground(Constant.BUTTON_BACKGROUND);
        createClassroomButton.setPreferredSize(Constant.BUTTON_SIZE);
        createClassroomButton.setBackground(Constant.BUTTON_BACKGROUND);
        changePasswordButton.setPreferredSize(Constant.BUTTON_SIZE);
        changePasswordButton.setBackground(Constant.BUTTON_BACKGROUND);
        logoutButton.setPreferredSize(Constant.BUTTON_SIZE);
        logoutButton.setBackground(Constant.BUTTON_BACKGROUND);

        //Panel Details
        setLayout(new GridBagLayout());
        setBackground(Constant.BUTTON_PANEL_BACKGROUND);

        //Adding Components to Frame
        add(teacherButton,Constraint.setPosition(0,0));
        add(createClassroomButton,Constraint.setPosition(0,1));
        add(changePasswordButton,Constraint.setPosition(0,2));
        add(logoutButton,Constraint.setPosition(0,3));
    }
}