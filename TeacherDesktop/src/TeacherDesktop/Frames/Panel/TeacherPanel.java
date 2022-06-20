package TeacherDesktop.Frames.Panel;

import TeacherDesktop.Server.ServerConnection;
import TeacherDesktop.Static.Constant;
import TeacherDesktop.Static.Constraint;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TeacherPanel extends JPanel implements ActionListener {

    private JLabel panelNameLabel;
    private JScrollPane scrollPane;
    private JPanel teacherListPanel;
    private ArrayList<TeacherCardPanel> teacherCardPanelList;
    private JButton createTeacherIdButton,backButton;
    private CreateTeacherPanel createTeacherPanel;
    private ServerConnection serverConnection;

    public TeacherPanel(ServerConnection serverConnection){
        //Initialising Member Variables
        this.serverConnection = serverConnection;
        panelNameLabel = new JLabel("Teachers");
        teacherListPanel = new JPanel();
        teacherCardPanelList = new ArrayList<>();
        scrollPane = new JScrollPane(teacherListPanel);
        Image img = new ImageIcon(Constant.ADD_ICON).getImage();
        img = img.getScaledInstance(30,30,Image.SCALE_DEFAULT);
        createTeacherIdButton = new JButton("Create Teacher Id",new ImageIcon(img));
        backButton = new JButton("Back");

        //Editing Members
        panelNameLabel.setFont(new Font("SansSerif", Font.BOLD,22));
        createTeacherIdButton.setBackground(Constant.BUTTON_BACKGROUND);
        backButton.setBackground(Constant.BUTTON_BACKGROUND);
        backButton.setVisible(false);
        teacherListPanel.setLayout(new GridBagLayout());
        scrollPane.setMinimumSize(new Dimension(1000,400));
        scrollPane.setPreferredSize(new Dimension(1000,400));
        //Adding Listeners
        createTeacherIdButton.addActionListener(this);
        backButton.addActionListener(this);

        //Filling TeacherCards
        fillTeacherCard();

        //Editing Panel Details
        setLayout(new GridBagLayout());
        setBackground(Constant.PANEL_BACKGROUND);

        //Adding Components to Panel
        add(panelNameLabel,Constraint.setPosition(0,0,2,1));
        add(createTeacherIdButton,Constraint.setPosition(1,1,Constraint.RIGHT));
        add(backButton,Constraint.setPosition(1,1,Constraint.RIGHT));
        add(scrollPane, Constraint.setPosition(0,2,2,1));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if( e.getSource() == createTeacherIdButton ){
            scrollPane.setVisible(false);
            createTeacherIdButton.setVisible(false);
            backButton.setVisible(true);
            createTeacherPanel = new CreateTeacherPanel(serverConnection);
            createTeacherPanel.setVisible(true);
            add(createTeacherPanel,Constraint.setPosition(0,2,2,1));
        }else if( e.getSource() == backButton ){
            scrollPane.setVisible(true);
            createTeacherIdButton.setVisible(true);
            backButton.setVisible(false);
            remove(createTeacherPanel);
            fillTeacherCard();
        }
        revalidate();
        repaint();
    }

    private void fillTeacherCard(){
        if( teacherCardPanelList.size() > 0 ){
            for( int i = 0; i < teacherCardPanelList.size(); i++){
                teacherListPanel.remove(teacherCardPanelList.get(i));
            }
            teacherCardPanelList = new ArrayList<>();
        }

        JSONArray teacherJsonArray = serverConnection.getTeacherList();
        for( int i = 0; i < teacherJsonArray.length(); i++ ){
            JSONObject teacherJsonObject = teacherJsonArray.getJSONObject(i);
            if( !teacherJsonObject.getString("phone").equals(Constant.PRINCIPAL_USERNAME) ){
                TeacherCardPanel teacherCardPanel = new TeacherCardPanel(teacherJsonObject);
                teacherCardPanel.setPreferredSize(new Dimension(900,150));
                teacherListPanel.add(teacherCardPanel,Constraint.setPosition(0,teacherCardPanelList.size()));
                teacherCardPanelList.add(teacherCardPanel);
            }
        }
    }
}
