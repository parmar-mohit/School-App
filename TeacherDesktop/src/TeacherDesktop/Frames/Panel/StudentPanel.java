package TeacherDesktop.Frames.Panel;

import TeacherDesktop.Server.ServerConnection;
import TeacherDesktop.Static.Constant;
import TeacherDesktop.Static.Constraint;
import org.json.JSONArray;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentPanel extends JPanel implements ActionListener {

    private JLabel panelNameLabel;
    private JButton createStudentIdButton,backButton;
    private CreateStudentPanel studentPanel;
    private JScrollPane scrollPane;
    private ServerConnection serverConnection;
    private String phone;

    public StudentPanel(ServerConnection serverConnection,String phone){
        //Initialising Members
        this.serverConnection = serverConnection;
        this.phone = phone;
        panelNameLabel = new JLabel("Students");
        Image img = new ImageIcon(Constant.ADD_ICON).getImage();
        img = img.getScaledInstance(30,30,Image.SCALE_DEFAULT);
        createStudentIdButton = new JButton("Create Student Id",new ImageIcon(img));
        scrollPane = new JScrollPane();

        //Editing Components
        panelNameLabel.setFont(new Font("SansSerif",Font.BOLD,22));
        createStudentIdButton.setBackground(Constant.BUTTON_BACKGROUND);
        scrollPane.setMinimumSize(new Dimension(1000,400));
        scrollPane.setPreferredSize(new Dimension(1000,400));

        //Adding Listeners
        createStudentIdButton.addActionListener(this);

        //Editing Panel
        setLayout(new GridBagLayout());
        setBackground(Constant.PANEL_BACKGROUND);

        //Adding Components to Frame
        add(panelNameLabel, Constraint.setPosition(0,0,2,1));
        add(createStudentIdButton,Constraint.setPosition(1,1,Constraint.RIGHT));
        add(scrollPane,Constraint.setPosition(0,2,2,1));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if( e.getSource() == createStudentIdButton ){
            JSONArray classroomArray = serverConnection.getClassrooms(phone);
            if( classroomArray.length() == 0 ){
                JOptionPane.showMessageDialog(this,"You are not Incharge of any class and hence cannot create student Id","Alert",JOptionPane.WARNING_MESSAGE);
            }else {
                //Setting Components Invisible
                createStudentIdButton.setVisible(false);
                scrollPane.setVisible(false);

                //Creating new Components
                backButton = new JButton("Back");
                studentPanel = new CreateStudentPanel(serverConnection,classroomArray);

                //Editing Components
                backButton.setBackground(Constant.BUTTON_BACKGROUND);
                studentPanel.setBackground(Constant.PANEL_BACKGROUND);

                //Adding Listeners
                backButton.addActionListener(this);

                //Adding Components to Panel
                add(backButton, Constraint.setPosition(1, 1, Constraint.RIGHT));
                add(studentPanel, Constraint.setPosition(0, 2, 2, 1));
            }
        }else if( e.getSource() == backButton ){
            //Setting Components Visible
            createStudentIdButton.setVisible(true);
            scrollPane.setVisible(true);

            //Removing Components from panel
            remove(backButton);
            remove(studentPanel);
        }
        revalidate();
        repaint();
    }
}
