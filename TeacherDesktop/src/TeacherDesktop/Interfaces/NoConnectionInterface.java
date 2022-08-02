package TeacherDesktop.Interfaces;

import TeacherDesktop.Static.Constant;
import TeacherDesktop.Static.Constraint;

import javax.swing.*;
import java.awt.*;

public class NoConnectionInterface extends JFrame {
    private final JLabel messageLabel;

    public NoConnectionInterface() {
        //Initialising Varibles
        messageLabel = new JLabel("We are unable to connect to server.\nCheck you Internet Connection and try again");

        //Frame details
        setTitle(Constant.SCHOOL_NAME);
        setIconImage(Toolkit.getDefaultToolkit().getImage(Constant.SCHOOL_LOGO));
        setSize(new Dimension(500, 300));
        setLayout(new GridBagLayout());
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Adding Components to Frame
        add(messageLabel, Constraint.setPosition(0, 0));
    }
}
