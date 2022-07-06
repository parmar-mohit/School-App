package TeacherDesktop.Panel;

import TeacherDesktop.Server.ServerConnection;
import TeacherDesktop.Static.Constant;
import TeacherDesktop.Static.Constraint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExamPanel extends JPanel implements ActionListener {

    private JLabel panelNameLabel;
    private JButton newExamButton,backButton;
    private ScrollPane scrollPane;
    private NewExamPanel newExamPanel;
    private ServerConnection serverConnection;
    private String phone;

    public ExamPanel(ServerConnection serverConnection,String phone){
        //Initialising Members
        this.serverConnection = serverConnection;
        this.phone = phone;
        panelNameLabel = new JLabel("Exams");
        Image img = new ImageIcon(Constant.ADD_ICON).getImage();
        img = img.getScaledInstance(30,30,Image.SCALE_DEFAULT);
        newExamButton = new JButton("New Exam",new ImageIcon(img));
        scrollPane = new ScrollPane();

        //Editing Components
        panelNameLabel.setFont(new Font("SansSerif",Font.BOLD,22));
        newExamButton.setBackground(Constant.BUTTON_BACKGROUND);
        scrollPane.setMinimumSize(Constant.SCROLLPANE_SIZE);
        scrollPane.setPreferredSize(Constant.SCROLLPANE_SIZE);

        //Adding Listeners
        newExamButton.addActionListener(this);

        //Editing Panel
        setLayout(new GridBagLayout());
        setBackground(Constant.PANEL_BACKGROUND);

        //Adding Components to Panel
        add(panelNameLabel, Constraint.setPosition(0,0,2,1));
        add(newExamButton,Constraint.setPosition(1,1,Constraint.RIGHT));
        add(scrollPane,Constraint.setPosition(0,2,2,1));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if( e.getSource() == newExamButton ){
            //Setting Components Invisible
            newExamButton.setVisible(false);
            scrollPane.setVisible(false);

            //Intialising Components
            backButton = new JButton("Back");
            newExamPanel = new NewExamPanel(serverConnection,phone);

            //Editing Components
            backButton.setBackground(Constant.BUTTON_BACKGROUND);

            //Adding Listeners
            backButton.addActionListener(this);

            //Adding Components to Panel
            add(backButton,Constraint.setPosition(1,1,Constraint.RIGHT));
            add(newExamPanel,Constraint.setPosition(0,2,2,1));
        }else if( e.getSource() == backButton ){
            //Removing Components
            remove(backButton);
            remove(newExamPanel);

            //Setting Components Visible
            newExamButton.setVisible(true);
            scrollPane.setVisible(true);
        }
        revalidate();
        repaint();
    }
}
