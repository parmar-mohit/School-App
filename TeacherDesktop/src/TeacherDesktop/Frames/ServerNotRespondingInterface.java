package TeacherDesktop.Frames;

import TeacherDesktop.Static.Constant;
import TeacherDesktop.Static.Constraint;

import javax.swing.*;
import java.awt.*;

public class ServerNotRespondingInterface extends JFrame{
    private JLabel messageLabel;

    public ServerNotRespondingInterface() {
        //Initialising Varibles
        messageLabel = new JLabel("Server is Not Responding.Please Restart the Application");

        //Frame details
        setTitle(Constant.SCHOOL_NAME);
        setIconImage(Toolkit.getDefaultToolkit().getImage(Constant.SCHOOL_LOGO));
        setSize(new Dimension(500,300));
        setLayout(new GridBagLayout());
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Adding Components to Frame
        add(messageLabel, Constraint.setPosition(0,0));
    }
}