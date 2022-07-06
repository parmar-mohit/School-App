package TeacherDesktop.Panel;

import TeacherDesktop.CardPanel.TeacherCardPanel;
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

        //Editing Members
        panelNameLabel.setFont(new Font("SansSerif", Font.BOLD,22));
        createTeacherIdButton.setBackground(Constant.BUTTON_BACKGROUND);
        teacherListPanel.setLayout(new GridBagLayout());
        scrollPane.setMinimumSize(Constant.SCROLLPANE_SIZE);
        scrollPane.setPreferredSize(Constant.SCROLLPANE_SIZE);

        //Adding Listeners
        createTeacherIdButton.addActionListener(this);

        //Filling TeacherCards
        fillTeacherCard();

        //Editing Panel Details
        setLayout(new GridBagLayout());
        setBackground(Constant.PANEL_BACKGROUND);

        //Adding Components to Panel
        add(panelNameLabel,Constraint.setPosition(0,0,2,1));
        add(createTeacherIdButton,Constraint.setPosition(1,1,Constraint.RIGHT));
        add(scrollPane, Constraint.setPosition(0,2,2,1));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if( e.getSource() == createTeacherIdButton ){
            //Setting Components to Invisible
            scrollPane.setVisible(false);
            createTeacherIdButton.setVisible(false);

            //Instating createTeacherPanel and BackButton
            createTeacherPanel = new CreateTeacherPanel(serverConnection);
            backButton = new JButton("Back");

            //Editing Components
            backButton.setBackground(Constant.BUTTON_BACKGROUND);

            //Adding ActionListener
            backButton.addActionListener(this);

            //Adding Components to Frame
            add(backButton,Constraint.setPosition(1,1,Constraint.RIGHT));
            add(createTeacherPanel,Constraint.setPosition(0,2,2,1));
        }else if( e.getSource() == backButton ){
            //Setting Components to Visible
            scrollPane.setVisible(true);
            createTeacherIdButton.setVisible(true);

            //Removing Components
            remove(backButton);
            remove(createTeacherPanel);

            //Refreshing TeacherCardList
            fillTeacherCard();
        }
        revalidate();
        repaint();
    }

    public void fillTeacherCard(){
        if( teacherCardPanelList.size() > 0 ){
            teacherListPanel.removeAll();
            teacherCardPanelList = new ArrayList<>();
        }

        JSONArray teacherJsonArray = serverConnection.getTeacherList();
        for( int i = 0; i < teacherJsonArray.length(); i++ ){
            JSONObject teacherJsonObject = teacherJsonArray.getJSONObject(i);
            if( !teacherJsonObject.getString("phone").equals(Constant.PRINCIPAL_USERNAME) ){
                TeacherCardPanel teacherCardPanel = new TeacherCardPanel(teacherJsonObject,serverConnection,this);
                teacherCardPanel.setPreferredSize(new Dimension(900,220));
                teacherListPanel.add(teacherCardPanel,Constraint.setPosition(0,teacherCardPanelList.size()));
                teacherCardPanelList.add(teacherCardPanel);
            }
        }
    }
}
