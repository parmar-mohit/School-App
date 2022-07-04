package TeacherDesktop.Interfaces;

import TeacherDesktop.Panel.BrandingPanel;
import TeacherDesktop.Panel.ChangePasswordPanel;
import TeacherDesktop.Panel.MyClassroomPanel;
import TeacherDesktop.Panel.StudentPanel;
import TeacherDesktop.Server.ServerConnection;
import TeacherDesktop.Static.Constant;
import TeacherDesktop.Static.Constraint;
import org.json.JSONArray;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TeacherInterface extends JFrame implements ActionListener {

    private BrandingPanel brandingPanel;
    private TeacherButtonPanel buttonPanel;
    private JPanel optionPanel;
    private ServerConnection serverConnection;
    private String phone;

    public TeacherInterface(ServerConnection serverConnection,String phone){
        this.serverConnection = serverConnection;
        this.serverConnection.setCurrentFrame(this);
        this.phone = phone;
        //Initialising Members
        brandingPanel = new BrandingPanel();
        buttonPanel = new TeacherButtonPanel();

        //Setting Size
        brandingPanel.setMinimumSize(new Dimension(Constant.screenSize.width,Constant.screenSize.height/5));
        brandingPanel.setPreferredSize(new Dimension(Constant.screenSize.width,Constant.screenSize.height/5));
        buttonPanel.setMinimumSize(new Dimension(Constant.screenSize.width/5,Constant.screenSize.height*4/5));
        buttonPanel.setPreferredSize(new Dimension(Constant.screenSize.width/5,Constant.screenSize.height*4/5));

        //Adding Listeners
        buttonPanel.myClassroomButton.addActionListener(this);
        buttonPanel.studentButton.addActionListener(this);
        buttonPanel.securityButton.addActionListener(this);
        buttonPanel.logoutButton.addActionListener(this);

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

        if( e.getSource() == buttonPanel.myClassroomButton ){
            JSONArray classroomJsonArray = serverConnection.getStandardDivisionOfTeacher(phone);
            if( classroomJsonArray.length() == 0 ){
                JOptionPane.showMessageDialog(this,"You are not Incharge of any class and hence cannot access My Classroom","Alert",JOptionPane.WARNING_MESSAGE);
                return;
            }else {
                optionPanel = new MyClassroomPanel(serverConnection, phone,classroomJsonArray);
            }
        }else if( e.getSource() == buttonPanel.studentButton ){
            optionPanel = new StudentPanel(serverConnection,phone);
        }else if( e.getSource() == buttonPanel.securityButton ){
            optionPanel = new ChangePasswordPanel(serverConnection,phone);
        }else if( e.getSource() ==  buttonPanel.logoutButton ){
            dispose();
            new LoginInterface(serverConnection);
            return;
        }

        //Coloring Buttons
        buttonPanel.myClassroomButton.setBackground(Constant.BUTTON_BACKGROUND);
        buttonPanel.studentButton.setBackground(Constant.BUTTON_BACKGROUND);
        buttonPanel.examButton.setBackground(Constant.BUTTON_BACKGROUND);
        buttonPanel.securityButton.setBackground(Constant.BUTTON_BACKGROUND);

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

class TeacherButtonPanel extends JPanel{

    public JButton myClassroomButton,studentButton,examButton,securityButton,logoutButton;

    public TeacherButtonPanel(){
        //Initialisng Members
        myClassroomButton = new JButton("My Classroom");
        studentButton = new JButton("Students");
        examButton = new JButton("Exam");
        securityButton = new JButton("Security");
        logoutButton = new JButton("Logout");

        //Editing Components
        myClassroomButton.setPreferredSize(Constant.BUTTON_SIZE);
        myClassroomButton.setBackground(Constant.BUTTON_BACKGROUND);
        studentButton.setPreferredSize(Constant.BUTTON_SIZE);
        studentButton.setBackground(Constant.BUTTON_BACKGROUND);
        examButton.setPreferredSize(Constant.BUTTON_SIZE);
        examButton.setBackground(Constant.BUTTON_BACKGROUND);
        securityButton.setPreferredSize(Constant.BUTTON_SIZE);
        securityButton.setBackground(Constant.BUTTON_BACKGROUND);
        logoutButton.setPreferredSize(Constant.BUTTON_SIZE);
        logoutButton.setBackground(Constant.BUTTON_BACKGROUND);

        //Editing Panel
        setLayout(new GridBagLayout());
        setBackground(Constant.BUTTON_PANEL_BACKGROUND);

        //Adding Components to Panel
        add(myClassroomButton,Constraint.setPosition(0,0));
        add(studentButton, Constraint.setPosition(0,1));
        add(examButton,Constraint.setPosition(0,2));
        add(securityButton,Constraint.setPosition(0,3));
        add(logoutButton,Constraint.setPosition(0,4));
    }
}
