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

public class ClassroomPanel extends JPanel implements ActionListener  {

    private JLabel panelNameLabel;
    private JScrollPane scrollPane;
    private JPanel classroomListPanel;
    private ArrayList<ClassroomCardPanel> classroomCardPanelArrayList;
    private JButton createClassroomButton,backButton;
    private CreateClassroomPanel createClassroomPanel;
    private ServerConnection serverConnection;

    public ClassroomPanel(ServerConnection serverConnection){
        //Initialisng Members
        this.serverConnection = serverConnection;
        panelNameLabel = new JLabel("Classrooms");
        Image img = new ImageIcon(Constant.ADD_ICON).getImage();
        img = img.getScaledInstance(30,30,Image.SCALE_DEFAULT);
        createClassroomButton = new JButton("Create Classroom", new ImageIcon(img));
        classroomListPanel = new JPanel();
        scrollPane = new JScrollPane(classroomListPanel);
        classroomCardPanelArrayList = new ArrayList<>();

        //Editing Components
        panelNameLabel.setFont(new Font("SansSerif",Font.BOLD,22));
        createClassroomButton.setBackground(Constant.BUTTON_BACKGROUND);
        classroomListPanel.setLayout(new GridBagLayout());
        scrollPane.setMinimumSize(new Dimension(1000,400));
        scrollPane.setPreferredSize(new Dimension(1000,400));

        //Filling ClassroomCardPanel
        fillClassroomCard();

        //Adding Listeners
        createClassroomButton.addActionListener(this);

        //Editing Panel
        setLayout(new GridBagLayout());
        setBackground(Constant.PANEL_BACKGROUND);

        //Adding Components to Panel
        add(panelNameLabel, Constraint.setPosition(0,0,2,1));
        add(createClassroomButton,Constraint.setPosition(1,1,Constraint.RIGHT));
        add(scrollPane,Constraint.setPosition(0,2,2,1));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if( e.getSource() == createClassroomButton ){
            //Setting Components Invisible
            scrollPane.setVisible(false);
            createClassroomButton.setVisible(false);

            //Creating createClassroomPanel and BackButton
            createClassroomPanel = new CreateClassroomPanel(serverConnection);
            backButton = new JButton("Back");

            //Editing Components
            backButton.setBackground(Constant.BUTTON_BACKGROUND);

            //Adding ActionListener
            backButton.addActionListener(this);

            //Adding Components to Panel
            add(backButton,Constraint.setPosition(1,1,Constraint.RIGHT));
            add(createClassroomPanel,Constraint.setPosition(0,2,2,1));
        }else if(e.getSource() == backButton ){
            //Setting Components Visible
            scrollPane.setVisible(true);
            createClassroomButton.setVisible(true);

            //Removing Components
            remove(backButton);
            remove(createClassroomPanel);

            //Refreshing ClassroomPanelList
            fillClassroomCard();
        }
        revalidate();
        repaint();
    }

    public void fillClassroomCard(){
        for( int i = 0; i < classroomCardPanelArrayList.size(); i++){
            classroomListPanel.remove(classroomCardPanelArrayList.get(i));
        }
        classroomCardPanelArrayList = new ArrayList<>();

        JSONArray classroomJsonArray = serverConnection.getClassroomList();
        for( int i = 0; i < classroomJsonArray.length(); i++){
            ClassroomCardPanel classroomCardPanel = new ClassroomCardPanel(classroomJsonArray.getJSONObject(i),serverConnection,this);
            classroomCardPanel.setPreferredSize(new Dimension(900,100));
            classroomListPanel.add(classroomCardPanel,Constraint.setPosition(0,classroomCardPanelArrayList.size()));
            classroomCardPanelArrayList.add(classroomCardPanel);
        }
    }
}