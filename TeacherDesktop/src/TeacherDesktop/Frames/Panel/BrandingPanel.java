package TeacherDesktop.Frames.Panel;

import TeacherDesktop.Static.Constant;
import TeacherDesktop.Static.Constraint;

import javax.swing.*;
import java.awt.*;

public class BrandingPanel extends JPanel {

    private JLabel imageLabel,schoolNameLabel;

    public BrandingPanel(){
        //Initialisng Members
        Image img = new ImageIcon(Constant.SCHOOL_LOGO).getImage();
        img = img.getScaledInstance(120,120,Image.SCALE_DEFAULT);
        imageLabel = new JLabel(new ImageIcon(img));
        schoolNameLabel = new JLabel(Constant.SCHOOL_NAME);

        //Editing Members
        imageLabel.setMinimumSize(new Dimension(120,120));
        imageLabel.setPreferredSize(new Dimension(120,120));
        schoolNameLabel.setFont(new Font("SansSerif",Font.BOLD,30));

        //Panel details
        setVisible(true);
        setBackground(Constant.FRAME_BACKGROUND);
        setLayout(new GridBagLayout());

        //Adding Components to Panel
        add(imageLabel, Constraint.setPosition(0,0));
        add(schoolNameLabel,Constraint.setPosition(1,0));
    }
}
