package TeacherDesktop.Frames.Panel;

import TeacherDesktop.Server.ServerConnection;
import TeacherDesktop.Static.Constant;
import TeacherDesktop.Static.Constraint;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class StudentPanel extends JPanel {

    private JLabel panelNameLabel;
    private CreateStudentPanel studentPanel;
    private JScrollPane scrollPane;
    private JPanel studentListPanel;
    private ArrayList<StudentCardPanel> studentCardPanelArrayList;
    private ServerConnection serverConnection;
    private String phone;

    public StudentPanel(ServerConnection serverConnection,String phone){
        //Initialising Members
        this.serverConnection = serverConnection;
        this.phone = phone;
        panelNameLabel = new JLabel("Students");
        studentListPanel = new JPanel();
        scrollPane = new JScrollPane(studentListPanel);
        studentCardPanelArrayList = new ArrayList<>();

        //Editing Components
        panelNameLabel.setFont(new Font("SansSerif",Font.BOLD,22));
        scrollPane.setMinimumSize(new Dimension(1000,500));
        scrollPane.setPreferredSize(new Dimension(1000,500));
        studentListPanel.setLayout(new GridBagLayout());

        //Filling Student Card
        fillStudentCard();

        //Editing Panel
        setLayout(new GridBagLayout());
        setBackground(Constant.PANEL_BACKGROUND);

        //Adding Components to Frame
        add(panelNameLabel, Constraint.setPosition(0,0));
        add(scrollPane,Constraint.setPosition(0,2));
    }

    private void fillStudentCard(){
        if( studentCardPanelArrayList.size() >  0 ){
            studentListPanel.removeAll();
            studentCardPanelArrayList = new ArrayList<>();
        }

        JSONArray studentListJsonArray = serverConnection.getSubjectTeacherStudentList(phone);
        for( int i = 0; i < studentListJsonArray.length(); i++ ){
            JSONObject studentJsonObject = studentListJsonArray.getJSONObject(i);
            StudentCardPanel studentCardPanel = new StudentCardPanel(studentJsonObject);
            studentCardPanel.setPreferredSize(new Dimension(900,350));
            studentListPanel.add(studentCardPanel,Constraint.setPosition(0,studentCardPanelArrayList.size()));
            studentCardPanelArrayList.add(studentCardPanel);
        }
    }
}
